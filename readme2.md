## 九、支付模块

### 沙箱环境

> [沙箱环境使用说明](https://docs.open.alipay.com/200/105311/)
>
> [沙箱平台登录](https://openhome.alipay.com/platform/appDaily.htm)
>
> [生成RSA密钥](https://docs.open.alipay.com/291/105971)
>
> [线上创建应用](https://docs.open.alipay.com/200/105310/)

蚂蚁沙箱环境(Beta)是协助开发者进行接口功能开发及主要功能联调的辅助环境。沙箱环境模拟了开放平台部分产品的主要功能和主要逻辑 



### Alipay扫码支付

> [当面付产品介绍](https://support.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.1bae4b70orczSN&treeId=193&articleId=105072&docType=1)
>
> [扫码支付接入指引](https://support.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.41034b702zJN4l&treeId=193&articleId=106078&docType=1)
>
> [当面付异步通知](https://support.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.75194b70hI5vwP&treeId=193&articleId=103296&docType=1)
>
> **[当面付快速接入](https://support.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.19324b70FFy692&treeId=193&articleId=105170&docType=1)**
>
> **[当面付SDK&Demo](https://support.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.eea74b70g3qLFC&treeId=193&articleId=105201&docType=1)**
>
> [服务端SDK](https://docs.open.alipay.com/54/103419/)



关键入参

| 关键入参         | 参数说明     |
| ---------------- | ------------ |
| out_trade_no     | 商户订单号   |
| subject          | 订单标题     |
| total_amount     | 订单金额     |
| store_id         | 商户门店编号 |
| timeout_express  | 交易超时时间 |
| buyer_pay_amount | 付款金额     |
| trade_status     | 交易状态     |

关键出参

| 关键入参 | 参数说明           |
| -------- | ------------------ |
| qr_code  | 订单二维码图片地址 |

交易状态说明

| 枚举名称       | 枚举说明                                 |
| -------------- | ---------------------------------------- |
| WAIT_BUYER_PAY | 交易创建，等待买家付款                   |
| TRADE_CLOSED   | 未付款交易超时关闭，或支付完成后全额退款 |
| TRADE_SUCCESS  | 交易支付成功                             |
| TRADE_FINISHED | 交易结束，不可退款                       |

通知触发条件

| 触发条件名     | 触发条件描述 | 触发条件默认值      |
| -------------- | ------------ | ------------------- |
| TRADE_FINISHED | 交易完成     | false（不触发通知） |
| TRADE_SUCCESS  | 支付成功     | true（触发通知）    |
| WAIT_BUYER_PAY | 交易创建     | false（不触发通知） |
| TRADE_CLOSED   | 交易关闭     | false（不触发通知） |



支付渠道说明

| 支付渠道代码   | 支付渠道   |
| -------------- | ---------- |
| COUPON         | 支付宝红包 |
| ALIPAYACCOUNT  | 支付宝余额 |
| POINT          | 集分宝     |
| DISCOUNT       | 折扣券     |
| PCARD          | 预付卡     |
| FINANCEACCOUNT | 余额宝     |
| MCARD          | 商家储值卡 |
| MDISCOUNT      | 商户优惠券 |
| MCOUPON        | 商户红包   |
| PCREDIT        | 蚂蚁花呗   |

### 当面付Demo调试

- 首先进入[网址](https://support.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.eea74b70g3qLFC&treeId=193&articleId=105201&docType=1)下载当面付的SDK和Demo

  ![](https://ws1.sinaimg.cn/large/e4eff812ly1fy32mn7u1nj20kk074jrb.jpg)

- 下载后解压，将**TradePayDemo**项目导入到IDEA中，并添加好`\TradePaySDK\lib`下的jar包

- 接下来配置属性文件**zfbinfo.properties**

- 生成RSA密钥，进入[网址](https://docs.open.alipay.com/291/105971)下载RSA签名验签工具，解压打开文件夹，运行“RSA签名验签工具.bat”（WINDOWS） ，生成私钥与公钥，如下图：

  ![](https://ws1.sinaimg.cn/large/e4eff812ly1fy32ttz5y9j20m50iv780.jpg)

- [登录](https://openhome.alipay.com/platform/appDaily.htm)沙箱，将生成的**商户应用公钥**填入到沙箱**RSA2(SHA256)密钥**项，同时设置应用网关为https://openapi.alipaydev.com/gateway.do  ，授权回调地址为http://www.happymmall.com/order/alipay_callback ，点击AES密钥项，自动生成一个密钥

- 配置属性文件**zfbinfo.properties**，与沙箱中的项目一一对应，其他项默认：

  | 配置文件          | 沙箱                      |
  | ----------------- | ------------------------- |
  | open_api_domain   | 支付宝网关                |
  | pid               | 商户UID                   |
  | appid             | APPID                     |
  | private_key       | RSA签名验签工具生成的私钥 |
  | public_key        | RSA签名验签工具生成的公钥 |
  | alipay_public_key | 查看支付宝公钥            |

  ![](https://ws1.sinaimg.cn/large/e4eff812ly1fy335fqg6sj20s40b2dh3.jpg)



- 之后再运行主函数Main.java，从日志中可以看到demo运行成功，生成了一笔订单

  ![](https://ws1.sinaimg.cn/large/e4eff812gy1fy33dn6d2xj20s104adfp.jpg)

- 复制qr_code后面的网址，进入[草料二维码](https://cli.im/text?8fb35e2b0b2b99f81191ffc83b0a15c4)生成页面，粘贴网址，可以**生成付款码**

- 下载安装**沙箱钱包**安卓版，分别使用**买家账号**扫码支付，使用**卖家账号**登录查看余额，可以观察到交易成功

- 将项目部署成Web项目：

  ![](https://ws1.sinaimg.cn/large/e4eff812ly1fy3489uae5j20je0atgml.jpg)

  ![](https://ws1.sinaimg.cn/large/e4eff812gy1fy349fpcpqj20kz07dt8o.jpg)

- 运行项目

   ![](https://ws1.sinaimg.cn/large/e4eff812ly1fy34asskdjj20ep05ba9u.jpg)



### 集成到项目

- 将原demo中的前缀为alipay的jar包复制到项目工程/`webapp/WEB-INF/lib`下，并将其他几个公共的jar包添加到maven配置文件中（版本保持一致，避免冲突），在`项目结构-模块-依赖`设置中，添加lib文件下的jar包；

- 在maven的pom文件中添加编译插件，可以将lib下的jar包打包发布：

  ```xml
  <!-- maven的核心插件之-complier插件默认只支持编译Java 1.4，因此需要加上支持高版本jre的配置，在pom.xml里面加上 增加编译插件 -->
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
      <source>1.8</source>
      <target>1.8</target>
      <encoding>UTF-8</encoding>
      <!--将支付宝的sdk放到lib中-->
      <compilerArguments>
        <extdirs>${project.basedir}/src/main/webapp/WEB-INF/lib</extdirs>
      </compilerArguments>
    </configuration>
  </plugin>
  ```

- 在项目工程中建立`com.alipay.demo.trade`包，并将demo中的两个java源文件复制过来

- 运行项目工程中的Main.java，可以看到下单成功；













## 十、订单管理模块