技术说明
1.Login8001-登陆模块
* 用户登出时间和异地IP验证：token，在网关GateWay5001中的全局过滤器中进行验证
* 前后端数据交互采用RSA加密，后端数据存储到数据库采用SHA256进行加密
* 框架采用SSM

2.Grouping8002-分组信息模块
* 框架采用SSM

3.Fans8003-粉丝信息模块
* 框架采用SSM

4.Push8004
* rocketMq发送消息队列至Task8006端口-->创建任务
* 利用feign调用Task8006的updateStatus方法-->对已有Task的状态进行更新
* 定时任务：每30min对特定对象推送任务
* 采用mybatis-plus作为持久层框架

5.Template8005-模版信息模块
* 采用了Mybatis-plus作为持久层框架进行分页查询

6.Task8006
* CreateTask作为消费者对Push8004发送的消息进行消费
* 采用了Mybatis-plus作为持久层框架进行分页查询

关于数据库的一些说明：
wechat_group_info表格：tagType: 0-自定义分组；2-规则分组 / rule字段只有规则分组有，规则分组在粉丝信息模块可添加
wewechat_fans_info: groupid字段用于兼容旧的分组接口
