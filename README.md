# messageBorad2
+ #### 登录注册有前端界面，可不用postman测试此部分
  + 有邮箱验证，工具邮箱及密码存在数据库里，不嵌入代码，测试需自行添加工具邮箱
  + 三处正则过滤
  + 密码加密入库
+ #### 留言板部份只有接口，有postman测试记录
  + 有过滤器，不登录则无法访问添加留言、删除留言、更新留言的接口
  + 模糊查询可获得留言的子内容，无法获得父内容
+ #### mybaits使用“#”防sql注入（用的注解）
+ #### 更新：加入了测试切面和swagger2页面
