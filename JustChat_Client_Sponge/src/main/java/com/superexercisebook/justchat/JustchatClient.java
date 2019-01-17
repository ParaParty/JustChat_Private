package com.superexercisebook.justchat;

import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.protocol.IReaderProtocol;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.Text;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Base64;

import static org.spongepowered.api.text.format.TextColors.*;

public class JustchatClient extends Thread{

    final static byte[] MessageHeader = {0x11,0x45,0x14};

    public ConnectionInfo info;
    public IConnectionManager clientManager;
    public Logger logger;
    //设置自定义解析头
    private OkSocketOptions.Builder okOptionsBuilder;


    @Override
    public void run(){

        info = new ConnectionInfo("115.159.36.210", 38440);

        //调用OkSocket,开启这次连接的通道,拿到通道Manager
        clientManager = OkSocket.open(info);

        okOptionsBuilder = new OkSocketOptions.Builder();
        okOptionsBuilder.setReaderProtocol(new IReaderProtocol() {
            @Override
            public int getHeaderLength(){
                /*
                 * 返回不能为零或负数的报文头长度(字节数)。
                 * 您返回的值应符合服务器文档中的报文头的固定长度值(字节数),可能需要与后台同学商定
                 */
                return MessageHeader.length+4 /*固定报文头的长度(字节数)*/;
            }

            @Override
            public int getBodyLength(byte[] header, ByteOrder byteOrder) {
                /*
                 * 体长也称为有效载荷长度，
                 * 该值应从作为函数输入参数的header中读取。
                 * 从报文头数据header中解析有效负载长度时，最好注意参数中的byteOrder。
                 * 我们强烈建议您使用java.nio.ByteBuffer来做到这一点。
                 * 你需要返回有效载荷的长度,并且返回的长度中不应该包含报文头的固定长度
                 */
                /*
                String a= new String("");
                for (int i=0;i<7;i++){
                    a=a+(header[i]&0xff)+" ";
                }
                logger.info(a);

                logger.info("||" +(header[3]&0xff)*(2<<23)+""+
                        (header[4]&0xff)*(2<<15)+""+
                        (header[5]&0xff)*(2<<7)+""+
                        (header[6]&0xff));
                */
                //0 1 2 3 4 5 6
                /*if (PulsePacker.isPulse(header)){
                    return 0;
                }
                else*/ if (MessagePacker.isMessage(header)){
                    int len = (header[3]&0xff)*(2<<23)+
                            (header[4]&0xff)*(2<<15)+
                            (header[5]&0xff)*(2<<7)+
                            (header[6]&0xff);
                    //logger.info(""+len);
                    return len /*有效负载长度(字节数)，固定报文头长度(字节数)除外*/;
                };
                return 0;
            }
        });
        //将新的修改后的参配设置给连接管理器
        clientManager.option(okOptionsBuilder.build());

        //注册Socket行为监听器,SocketActionAdapter是回调的Simple类,其他回调方法请参阅类文档
        clientManager.registerReceiver(new SocketActionAdapter(){
            @Override
            public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
                logger.info("connected.");
            }

            @Override
            public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
                //遵循以上规则,这个回调才可以正常收到服务器返回的数据,数据在OriginalData中,为byte[]数组,该数组数据已经处理过字节序问题,直接放入ByteBuffer中即可使用

                /*if (PulsePacker.isPulse(data.getHeadBytes())){
                    logger.info("1");
                    clientManager.getPulseManager().pulse();
                }
                else */if (MessagePacker.isMessage(data.getHeadBytes())){
                    try {
                        String str= new String (data.getBodyBytes(), Charset.forName("UTF-8"));

                        //logger.info(str);

                        JSONObject jsonObject = new JSONObject(str);
                        int version = jsonObject.getInt("version");
                        if (version==MessagePacker.PackVersion) {
                            int messageType = jsonObject.getInt("type");

                            if (messageType==MessagePackType.MESSAGE) {

                                String sender = MessageTools.Base64Decode(jsonObject.getString("sender"));
                                MessageContentUnpacker content = new MessageContentUnpacker(jsonObject.getJSONArray("content"));
                                content.logger = logger;

                                Text TTag = Text.builder("[*] ").color(DARK_GREEN).build();
                                Text TSender = Text.builder(sender).color(DARK_GREEN).build();
                                Text TSplit = Text.builder(": ").build();
                                Text TContent = content.toText();

                                Text Content = Text.builder().append(TTag, TSender, TSplit, TContent).build();
                                MessageChannel.TO_ALL.send(Content);

                            } else {
                                logger.info("Received a message with an unrecognized type.");
                            }
                        } else
                        {
                            if (version>MessagePacker.PackVersion) {
                                logger.info("Received a message made by a higher-version server.");
                            } else {
                                logger.info("Received a message made by a lower-version server.");
                            }
                        }
                    } catch (JSONException e) {
                        logger.error("Received an unrecognized message.");
                    }


                    /*
                    Text ot = Text.builder("[From Socket Server] "+str).build();
                    Text text = Text.builder().color(GREEN).append(ot).build();
                    */
                };


            }


        });

        //调用通道进行连接
        clientManager.connect();
    }
}
