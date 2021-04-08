## socks5说明文档
* 基于socks5协议实现的代理服务器
* 采用netty框架，代码高度类似netty官方提供的example
* server分支的server工程为基础版（只有服务器），配合chrome的SwitchyOmega插件使用
* main分支拆分为客户端（需要chrome插件或privoxy支持）和服务端

#### 环境配置
* `host port等信息都定义在common工程的ConstantDef中`

#### 概念
* `代理服务器：分为正向代理和反向代理`
* `常见的作用：内网击穿、分流等`
* `常用的代理协议：http socks5`
* `明确开发目标：实现的是代理服务器server 不是local`
* `属于网络编程的范畴，区别于App`

#### socks5协议
* https://www.jianshu.com/p/e44a149610ae tcp
* https://www.jianshu.com/p/1fe80fcccef9 身份验证
* https://www.jianshu.com/p/cf88c619ee5c udp

#### netty基本概念
* `bootstrap: netty应用程序通过bootstrap引导`
* `eventloop: 用于处理IO操作，一个eventloop通常处理多个Channel事件`
* `channel: 封装socket`
* `channelpipeline: 管道，一个channel对应一个pipeline，链式结构`
* `channelhandler: 注册到pipeline中，处理数据的容器`
* `channelfuture: netty中所有的操作都是异步，所以提供了future类，通过addlistener方法通知注册的channelfuturelistener`

#### ChannelHandler接口
* ChannelInboundHandler子接口 - 处理进站数据和所有状态更改事件<br>
  实现类：ChannelInboundHandlerAdapter<br>
  SimpleChannelInboundHandler
* ChannelOutboundHandler子接口 - 处理出站数据，允许拦截各种操作<br>
  实现类：ChannelOutboundHandlerAdapter<br>
  
#### 待开发功能
* 加密
* client扩展<br>
  pac<br>
  整合privoxy<br>
  图形化，生成exe文件<br>
  自定义的其他功能
  
