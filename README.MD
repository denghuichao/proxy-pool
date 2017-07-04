# proxy-pool 代理IP池

## 简介  
最近在玩爬虫, 很多网站有反爬虫机制,会对访问频率太高的IP进行禁封.于是到网上去找免费的代理,使用过程中发现这些代理的质量不高,很多都不能使用,因此想到做个代理池.程序主动收集代理IP,并定时对这些代理进行可用性验证,同时提供接口获取可用的代理供爬虫使用.  

## 代理IP来源
有很多网站提供代理IP服务,有的免费,质量好的则付费.这里只收集免费的代理IP.目前爬取了[goubanjia](http://www.goubanjia.com/),[kuaidaili](http://www.kuaidaili.com),[66IP](http://www.66ip.cn),[xicidaili](http://www.xicidaili.com)这几个网站的代理IP.每个网站IP只爬取了前面10页,后面的基本上不可用.

## 可用性保证
爬回来的IP有大部分都是不能用的,要保证爬虫拿到的IP质量较高,就需要进行可用性的验证,在代理不可用时将其删除.

## 接口方式
为了方便客户端(爬虫)获取代理,将接口做成了服务的形式,客户端通过restapi获取代理,并在发现代理不可用时,主动调用api将其删除.

## proxy-pool设计  

![原理图](https://raw.githubusercontent.com/denghuichao/proxy-pool/master/images/design.png)

整个项目主要分成以下几个模块:
- fetcher 收集网上代理IP的爬虫,目前有`GoubanjiaFetcher`,`KuaiDailiFetcher`,`Www66IPFetcher`,`XichiDailiFetcher`四个爬虫,分别收集四个网站的代理IP.如果需要收集其他网站的IP,可以在该模块下增加相应实现.
- schedule 定时任务,定时从网络上爬取最新IP,定时验证已有代理的可用性.
- db 存储功能, 目前采用redis保存可用代理
- boot 项目的启动类
- api restapi实现
- utils 一些工具类库
## 项目部署

项目基于SpringBoot开发,直接打包成jar并启动jar包即可.

## 使用方式
启动程序即可通过浏览器获取代理ip,目前支持以下操作:
- get 获取一个可用代理  
  
![get](https://raw.githubusercontent.com/denghuichao/proxy-pool/master/images/get.png)
- get_list 获取多个可用代理  

![get_list](https://raw.githubusercontent.com/denghuichao/proxy-pool/master/images/get_list.png)
- verify 验证代理可用性  
  
![verify](https://raw.githubusercontent.com/denghuichao/proxy-pool/master/images/verify.png)
- delete 删除不可用代理  
  
![delete](https://raw.githubusercontent.com/denghuichao/proxy-pool/master/images/delete.png)

### 测试地址  
http://hcdeng.pp.ngrok.cc/api/get_list?num=5

## 写在最后
由于能力有限,代码写的比较水,proxy-pool的功能也不完善,欢迎大家指正.如果您认为项目对您有帮助,请给个star~~




