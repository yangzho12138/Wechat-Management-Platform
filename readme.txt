1.Login8001-登陆模块
2.Grouping8002-分组信息模块
3.Fans8003-粉丝信息模块

5.Template8005-模版信息模块 采用了Mybatis-plus作为持久层框架（分页查询）

关于数据库的一些说明：
wechat_group_info表格：tagType: 0-自定义分组；2-规则分组 / rule字段只有规则分组有，规则分组在粉丝信息模块可添加
wewechat_fans_info: groupid字段用于兼容旧的分组接口

用户登出时间和异地IP验证：token