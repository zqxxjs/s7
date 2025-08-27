# struts7.03+spring6.0.13

struts2 是一个糟粕，不值得研究了，其设计思路没什么大问题，有问题的是细节。
举个例子： 想做前后分离项目，struts2作为springmvc的替代是可行的，首先
需要启用HttpMethod 拦截器，其次，7.0.3版本的代码逻辑是判断是否是特殊方法+相关
注解或者Action类上有HttpMethod的相关注解。重点来了，struts2的json解析如果
用struts-json包+内置的JsonInterceptor，那么不能确定伪造的参数会解析什么结果
它的JsonUtil类写的不咋地，对错误的json的数据也能解析。
