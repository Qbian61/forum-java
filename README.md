# 一 部署效果

- 开源版地址：http://42.51.0.196/
- 商业版二开后地址：https://www.developers.pub

# 二 简介

[forum-java](https://www.developers.pub/) 是一个开源的现代化社区平台，它实现了：

- 面向内容讨论的论坛；
- 面向知识问答的社区；
- 100% 开源；
- 欢迎到 [开发者客栈](https://www.developers.pub/) 官方讨论区了解更多。同时也欢迎关注官方公众号 智猿其说（下图）；

![image.png](https://static.developers.pub/d6d7135e5dfe4af2b1e7067fe57ac836)

- 感谢 破冰安全实验室 帮忙做的项目安全漏洞扫描，保障这个项目的每个用户可以放心大胆的使用，关注 破冰安全实验室 公众号了解更多安全知识（下图）；

![破冰安全实验室](https://static.developers.pub/292955c12fca41528a329ca24daf9cc4)

# 三 为什么要做这个项目？

- 企业在做开放平台领域建设，都需要一个开发者社区的网站供开发者学习交流（本项目参考了[微信开放平台](https://developers.weixin.qq.com/community/pay)、[头条开发者社区](https://forum.microapp.bytedance.com/mini-app)、[有赞云开发者社区](https://developers.youzanyun.com/)、[淘宝开放平台](https://open.bbs.taobao.com/list.html)等社区功能为需求背景开发）；
- 市面上已有的开源社区大部分都是PHP语言的，Java开源的很少，spring boot框架开发的几乎没有。大多数互联网公司内部开发语言都是Java，很多都是spring boot/cloud开发框架，对于PHP开源的社区，无法很好的接入公司内部系统；
- 现有的开源社区功能过于简单，无法满足企业的大部分需求；
- 现有的开源社区界面风格老式，没有跟上时代发展的审美，且定制能力差；
- 现有的开源社区缺乏实际运营需求的功能，管理功能过于简单，二开成本过高；


# 四 功能列表

```$xslt

用户端
    文章分类
        筛选文章
    标签
        查看详情
        筛选文章/问答
    文章
        写文章
        编辑
        删除
        评论
        点赞
        查看详情
    问答
        提问题
        编辑
        删除
        查看详情
        评论
        关注
        设置评论为最佳答案
        筛选已解决问题
        筛选未解决问题
    用户
        查看详情
        编辑个人资料
        更新登录密码
        关注好友
        查看粉丝
    消息
        文章/问答被关注通知
        文章/问答被评论通知
        个人被关注通知
        设置消息为已读
    关注
        关注的用户文章/问答
        关注的问答
        评论的问答
        点赞的文章
        评论的文章
    搜索
        根据文章/问答标题/内容模糊搜索
        
管理端
    用户管理
        禁用/启用
        设置为管理员/取消管理员
    操作日志
        操作类别筛选
    文章管理
        设置为官方
        设置为置顶
        设置为加精
        审核通过（可见）
        审核不过（不可见）
    文章类别管理
        审核通过（可见）
        审核不过（不可见）
        新增分类
    问答管理
        审核通过（可见）
        审核不过（不可见）
    标签管理
        审核通过（可见）
        审核不过（不可见）
        新增标签
```

 
![开发者客栈.png](https://static.developers.pub/8a71564c56c74416bb81ce87f3f2e719?)
 

# 五 特性

##  前端

- 多终端适配（手机端，pc端）
- 自定义主题颜色，方便企业用户自定义主题
- 编辑器支持control + s保存
- 编辑器支持control + v复制图片上传

##  后端

- 日志带有调用链，方便排查问题
- 分布式session，支持集群部署
- 用户角色权限分级，便于用户管理
- 接口权限校验，接口操作更安全

## 可扩展功能接口

- 文章/问答更新时自带审核，可接入审核中心便于运营管理
- 文件存储抽象接口，可支持自定义接入企业内部文件储存服务
- 缓存服务抽象接口，可支持自定义接入企业内部缓存服务
- 搜索服务抽象接口，可支持自定义接入企业内部搜索服务

# 六 技术栈

## 后端

- 数据库：mysql
- 持久层框架：mybatis
- 数据库连接池管理：hikaricp
- 数据库分页插件：github pagehelper
- mvc框架：spring mvc
- 应用层容器：spring boot
- json序列化工具：fastjson
- 邮件发送sdk：javax mail
- 七牛云存储sdk：qiniu java sdk
- 服务端页面渲染：thymeleaf

## 前端

- 前端markdown编辑器：mavon-editor
- 管理后台js框架：vue
- 用户端UI框架：bootstrap
- 管理后台UI框架 iview

# 七 部分页面展示

## 用户页面展示

- 首页

 
![首页]( https://static.developers.pub/81c6695a0e374ea89eb4037ff248259c)
 

 
![image.png](https://static.developers.pub/5092d67341a14cc6b155d21727a79227)
 


- 问答页

 
![问答页]( https://static.developers.pub/bfe0760841cd444a88942b9131355d30)
 

 
![image.png](https://static.developers.pub/ee20c1508a234b229613d681dc3cd913)
 


- 关注页

 
![image.png](https://static.developers.pub/cf523137fa964bb0a60691b7b37a2594)
 

- 消息列表页

 
![image.png](https://static.developers.pub/ff047bbafb6d43b2b497ee7188d5b6c2)
 

- 文章详情页

 
![文章详情页]( https://static.developers.pub/e537e76e4ad34177b2ab3a5b21624f25)
 


 
![image.png](https://static.developers.pub/83e559536c0e48408d276f96de9ed5fc)
 



- 标签详情页

 
![标签详情页]( https://static.developers.pub/57d3af8df85e421fba035dcc688fbf1c)
 

- 搜索页

 
![image.png](https://static.developers.pub/47dfbec4db884c668734df94749d2410)
 


- 用户主页

 
![用户主页](https://static.developers.pub/37da306856a844f5b6e9194f8a3217f2)
 

 
![image.png](https://static.developers.pub/02897af0bc794e3b9b9a4cc8d429cd14)
 


- 写文章页

 
![写文章页]( https://static.developers.pub/359f88bd5c6240b7aceb52cbf4f23ed5)
 

# 八 管理后台页面

 
![image.png](https://static.developers.pub/17475abfff6442fc8cb102301379c0e0)
 

# 九 安装

请参考 [forum-java安装指南](https://www.developers.pub/article/1005736)。

# 使用说明

**社区版只允许个人使用。商业用途请联系作者购买。**