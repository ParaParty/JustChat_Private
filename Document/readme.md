# 报文结构
## 总述
- 报头 7字节
	- 0x11 0x45 0x14
	- 4字节 表示包体长度 0x????????
- 报体 根据报头取得的报体长度获取
	- json结构

## 报体结构
- 报体为json结构，解析后可得到
	1. int version
		- 表示数据包版本
		- 当前版本 : ```version= 3```
	2. int type
		- 表示数据包类型	
- 心跳包消息 ```type= 0```
	1. 当收到一个心跳包消息时，需要回应一个心跳包消息
	```
	{
		"version": 3,
		"type": 0
	}
	```
- 注册消息 ```type= 1```
	- 当连接到JustChat服务器时，需要向JustChat服务器确认身份
	1. int identity
		- ```0``` 当前主机为Minecraft服务端
		- ```1``` 当前主机为聊天机器人
	2. string id
		- 格式为UUID 当前主机的编号
	3. string name
		- 当前服务器名字
		- 本字段内容为将原字符串内容按UTF-8编码后再进行Base64编码的字符串
	```
	{
		"version": 3,
		"type": 1,
		"identity": 0,
		"id": "",
		"name" : ""
		
	}
	```
- 消息广播 ```type= 100```
	1. int event
		- 1 玩家加入游戏 
		- 2 玩家退出游戏
		- 3 玩家死亡
		- 可和sender字段同时不存在
	2. string content
		- 本字段内容表示本段消息的显示内容
		- 本字段内容为将原字符串内容按UTF-8编码后再进行Base64编码的字符串
		- 本字段也许不存在
	3. string sender
		- 本字段内容为将原字符串内容按UTF-8编码后再进行Base64编码的字符串
		- 表示消息发送者用户名
		- 可和event字段同时不存在
	```
	{
		"version": 3,
		"type": 1,
		"event": 1,
		"sender": "5rWL6K+V55So5oi3",
		"content": "W+a1i+ivleeUqOaIt13lt7LliqDlhaXmuLjmiI8="
	}
	```
- 聊天消息 ```type= 101```
	1. string world
		- 表示发送者所在的世界的名字 或 发送者所在的群的群号
	2. string world_display
		- 本字段内容为将原字符串内容按UTF-8编码后再进行Base64编码的字符串
		- 表示发送者所在的世界的显示名字 或 发送者所在的群的群名
	2. string sender
		- 本字段内容为将原字符串内容按UTF-8编码后再进行Base64编码的字符串
		- 表示消息发送者用户名
	3. jsonArray content
		- 下面是每一个对象的字段介绍
		1. string type
			- 本字段内容表示本段消息的消息类型
		`其他的有空再更`
		
	- 普通消息样例数据包
		1. 普通消息
			```
			{
				"version": 3,
				"type": 2,
				"world": "576493373",
				"world_display": "5py65Zmo5Lq65rWL6K+V576k",
				"sender": "5rWL6K+V5raI5oGv5Y+R6YCB6ICF",
				"content": [{
					"type": "text",
					"content": "5rWL6K+V5paH5pys5a2X5q61"
				},
				{
					"type": "cqcode",
					"function": "CQ:at",
					"target": "5rWL6K+V5raI5oGv6KKrYXTogIU="
				},
				{
					"type": "cqcode",
					"function": "CQ:image",
					"url": "http://",
					"content": "W+WbvueJh10="
				},
				{
					"type": "text",
					"content": "ZW1vamnmtYvor5Xwn5CO"
				},
				{
					"type": "cqcode",
					"function": "CQ:face",
					"id": 212,
					"content": "L+aJmOiFrg==",
					"extension": "gif"
				}]
			}
			```
		1. 红包消息
			```
			{
				"version": 3,
				"type": 101,
				"world": "576493373",
				"world_display": "5py65Zmo5Lq65rWL6K+V576k",
				"sender": "5rWL6K+V5raI5oGv5Y+R6YCB6ICF",
				"content": [{
					"type": "cqcode",
					"function": "CQ:hb",
					"title": "57qi5YyF5raI5oGv5rWL6K+V"
				}]
			}
			```
		1. 富文本消息
			```
			{
				"version": 3,
				"type": 101,
				"world": "576493373",
				"world_display": "5py65Zmo5Lq65rWL6K+V576k",
				"sender": "5rWL6K+V5raI5oGv5Y+R6YCB6ICF",
				"content": [{
					"type": "cqcode",
					"function": "CQ:rich",
					"url": "aHR0cDovL3VybC5jbi81RGR5dk5K",
					"text": "W+WIhuS6q10g572R5Y+L5a625YW75LqG5LiA5Y+q54yr5ZKM5LiA5Y+q54uX77yM54uX6ICB5piv6KKr54yr5omT77yM5Y205LmQ5Zyo5YW25Lit4oCm"
				}]
			}
			```
		1. 连接分享消息
			```
			{
				"version": 3,
				"type": 101,
				"world": "576493373",
				"world_display": "5py65Zmo5Lq65rWL6K+V576k",
				"sender": "5rWL6K+V5raI5oGv5Y+R6YCB6ICF",
				"content": [{
					"type": "cqcode",
					"function": "CQ:share",
					"title": "5YWo572R5pyA5YWo4oCc6ZuG56aP4oCd5pS755WlfOS9oOacieS4gOW8oOiKseiKseWNoeW+hemihu+8gQ==",
					"url": "aHR0cDovL3VybC5jbi81cjZ2N0Ny",
					"content": "572R5oGL5ZCXP+aIkeacieiKseiKseWNoSE=",
					"image": "aHR0cDovL3VybC5jbi81SUJnVTF5"
				}]
			}
			```
- 玩家列表 ```type= 200```
	1. int subtype
		- 0 请求包
		- 1 响应包
	2. int count
		- 当前在线玩家数量
	3. int max
		- 最大允许在线玩家数量
	4. string[] playerlist
		- 在线玩家的用户名按照UTF-8编码的Base64编码后的字符串数组。
	- 请求包样例
	```
	{
		"version": 3,
		"type": 200,
		"subtype": 0
	}
	```
	- 响应包样例
	```
	{
		"version": 3,
		"type": 200,
		"subtype": 1,
		"count": 2,
		"max": 20,
		"playerlist": ["5rWL6K+V546p5a62MQ==","5rWL6K+V546p5a62Mg=="]
	}
	```