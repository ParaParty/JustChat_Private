<?php
namespace Workerman\Protocols;

use Workerman\Connection\ConnectionInterface;

class JustChat implements \Workerman\Protocols\ProtocolInterface
{
	const MessageHeader = array(0x11, 0x45, 0x14);
	
	public static function input($buffer, ConnectionInterface $connection)
    {
		if(strlen($buffer) < 7)
        {
            return 0;//不足7字节返回0等待数据
        }
		$str = substr($buffer, 0, 7);
		$len = strlen(substr($buffer, 7));
		$header = array();
        for($i = 0; $i < strlen($str); $i++){
             $header[] = ord($str[$i]);
        }
		if($header[0] != static::MessageHeader[0] || $header[1] != static::MessageHeader[1] || $header[2] != static::MessageHeader[2]){
			return false;//前三位验证失败则断开连接
		}
		if($len == ($header[3]&0xff)*(2<<23) + ($header[4]&0xff)*(2<<15) + ($header[5]&0xff)*(2<<7) + ($header[6]&0xff)){
			return $len + 7;//包长相等返回包长
		}else{
			return 0;//包长不等则返回0等待数据
		}
	}
	
	public static function decode($buffer, ConnectionInterface $connection)
	{
		return substr($buffer, 7);
	}
	
	public static function encode($data, ConnectionInterface $connection)
	{
		$bytes = array(0x11, 0x45, 0x14);
		$str = '';
        $bytes[3] = (strlen($data) & 0xff);
        $bytes[4] = (strlen($data) >> 8 & 0xff);
        $bytes[5] = (strlen($data) >> 16 & 0xff);
        $bytes[6] = (strlen($data) >> 24 & 0xff);
		$str = chr($bytes[0]) . chr($bytes[1]) . chr($bytes[2]) . chr($bytes[6]) . chr($bytes[5]) . chr($bytes[4]) . chr($bytes[3]);
		return $str . $data;
	}
}