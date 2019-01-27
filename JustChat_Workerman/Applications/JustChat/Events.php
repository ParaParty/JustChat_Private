<?php
/**
 * This file is part of workerman.
 *
 * Licensed under The MIT License
 * For full copyright and license information, please see the MIT-LICENSE.txt
 * Redistributions of files must retain the above copyright notice.
 *
 * @author walkor<walkor@workerman.net>
 * @copyright walkor<walkor@workerman.net>
 * @link http://www.workerman.net/
 * @license http://www.opensource.org/licenses/mit-license.php MIT License
 */

/**
 * 用于检测业务代码死循环或者长时间阻塞等问题
 * 如果发现业务卡死，可以将下面declare打开（去掉//注释），并执行php start.php reload
 * 然后观察一段时间workerman.log看是否有process_timeout异常
 */
//declare(ticks=1);

use \GatewayWorker\Lib\Gateway;

/**
 * 主逻辑
 * 主要是处理 onConnect onMessage onClose 三个方法3CHSDJ53NXM26FG5I5MHCHV
 * onConnect 和 onClose 如果不需要可以不用实现并删除
 */
class Events
{
	public static $PackVersion = 3;
	
	public static function onMessage($client_id, $data){
		echo $data . "\n";
		$json = json_decode($data, true);
		if($json["version"] != self::$PackVersion){
			Gateway::closeClient($client_id);
		}
		switch($json["type"]){
			case 0:
				Gateway::sendToClient($client_id, $data);
			break;
			case 1:
				Gateway::bindUid($client_id, base64_decode($json["name"]));
				if($json["identity"] == 0){
					Gateway::joinGroup($client_id, "mc");
				}elseif($json["identity"] == 1){
					Gateway::joinGroup($client_id, "cq");
				}
				foreach(Gateway::getAllClientIdList() as $client){
					if($client != $client_id){
						Gateway::sendToClient($client_id, json_encode(array("version" => self::$PackVersion, "type" => 1, "content" => "MC服务器 " . base64_decode($json["name"]) . " 已上线")));
					}
				}
			break;
			default:
				foreach(Gateway::getAllClientIdList() as $client){
					if($client != $client_id){
						Gateway::sendToClient($client_id, data);
					}
				}
			break;
		}
	}

	public static function onClose($client_id){

	}
}