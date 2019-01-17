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
		- 当前版本 : ```version= 2```
	2. int type
		- 表示数据包类型	
- 心跳包消息 ```type= 2```
	1. 当收到一个心跳包消息时，需要回应一个心跳包消息
- 消息广播 ```type= 1```
- 普通消息 ```type= 2```
	1. string world
		- 表示发送者所在的世界的名字 或 发送者所在的群的群号
	2. string world_display
		- 本字段内容为将原字符串内容按UTF-8编码后再进行Base64编码的字符串
		- 表示发送者所在的世界的显示名字 或 发送者所在的群的群名
	2. string sender
		- 本字段内容为将原字符串内容按UTF-8编码后再进行Base64编码的字符串
		- 表示消息发送者用户名
	3. string content
		- 本段内容为一段json对象数组的Base64编码后的字符串
		- 下面是每一个对象的字段介绍
		1. string type
			- 本字段内容表示本段消息的消息类型
		2. string content
			- 存在于 ```type = "text"``` 和 ```type = "cqcode" 且 function = "CQ:image"```
			- 本字段内容表示本段消息的显示内容
			- 本字段内容为将原字符串内容按UTF-8编码后再进行Base64编码的字符串
		3. string function
			- 存在于 ```type = "cqcode"``` 
			- 本字段内容表示为酷Q码的具体函数名
		4. string target
			- 本字段存在于 ```type = "cqcode" 且 function = "CQ:at"```
			- 本字段内容表示at的目标用户的显示昵称
			- 本字段内容为将原字符串内容按UTF-8编码后再进行Base64编码的字符串
		5. string url
			- 本字段存在于 ```type = "cqcode" 且 function = "CQ:image"```
			- 本字段内容表示图片的url

	- 普通消息样例数据包
	```
	{
		"version": 1,
		"type": 0,
		"world": "world",
		"world_display": "5Li75LiW55WM",
		"sender": "5rWL6K+V5raI5oGv5Y+R6YCB6ICF",
		"content": "W3sidHlwZSI6ICJ0ZXh0IiwiY29udGVudCI6ICI1cldMNksrVjVwYUg1cHlzNWEyWDVxNjEifSx7InR5cGUiOiAiY3Fjb2RlIiwiZnVuY3Rpb24iOiAiQ1E6YXQiLCJ0YXJnZXQiOiAiNXJXTDZLK1Y1cmFJNW9HdjZLS3JZWFRvZ0lVPSJ9LHsidHlwZSI6ICJjcWNvZGUiLCJmdW5jdGlvbiI6ICJDUTppbWFnZSIsInVybCI6ICJodHRwOi8vIiwiY29udGVudCI6ICJXK1didnVlSmgxMD0ifSx7InR5cGUiOiAidGV4dCIsImNvbnRlbnQiOiAiWlcxdmFtbm10WXZvcjVYd241Q08ifV0="
	}
	```