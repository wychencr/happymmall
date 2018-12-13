## 九、支付模块

### 支付接口设计

- 支付接口

  [链接](https://gitee.com/imooccode/happymmallwiki/wikis/%E9%97%A8%E6%88%B7_%E6%94%AF%E4%BB%98%E6%8E%A5%E5%8F%A3?sort_id=9915)

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



- 之后再运行主函数Main.java（测试**当面付2.0生成支付二维码**），从日志中可以看到demo运行成功，生成了一笔订单

  ![](https://ws1.sinaimg.cn/large/e4eff812gy1fy33dn6d2xj20s104adfp.jpg)

- 复制qr_code后面的网址，进入[草料二维码](https://cli.im/text?8fb35e2b0b2b99f81191ffc83b0a15c4)生成页面，粘贴网址，可以**生成付款码**

- 下载安装**沙箱钱包**安卓版，分别使用**买家账号**扫码支付，使用**卖家账号**登录查看余额，可以观察到交易成功

- 将项目部署成Web项目：

  ![](https://ws1.sinaimg.cn/large/e4eff812ly1fy3489uae5j20je0atgml.jpg)

  ![](https://ws1.sinaimg.cn/large/e4eff812gy1fy349fpcpqj20kz07dt8o.jpg)

- 运行项目

   ![](https://ws1.sinaimg.cn/large/e4eff812ly1fy34asskdjj20ep05ba9u.jpg)



### 移植到项目中调试

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



### 生成订单和付款码

- 控制器中传入参数为HttpSession session, 订单号Long orderNo和HttpServletRequest request，session用于校验用户是否登录，request可以获取上传路径，将参数userId、path、orderNo交由服务层处理；
- 服务层处理函数首先根据订单号和用户ID查询数据库，校验前端传过来的订单是否存在，如果存在则将订单号放到Map中；
- 接下来将SDK中的**当面付2.0生成支付二维码**函数**main.test_trade_precreate()**里面的代码移植到服务层处理函数中，修改相关的订单生成参数，其中商品明细列表通过在for循环中，根据userId和orderNo查表order_item得到
- 之后创建扫码支付请求builder，然后得到订单创建结果，如果成功则生成图片二维码，并上传到FTP服务器中，将二维码的url放到Map中，将Map作为ServerResponse的data返回；失败则返回对应的错误消息



### 支付宝回调验签

- 当收银台调用预下单请求API生成二维码展示给用户后，用户通过手机扫描二维码进行支付，支付宝会将该笔订单的变更信息，沿着商户调用预下单请求时所传入的通知地址主动推送给商户，其中包含了多项参数，程序需要做的就是：
  - 验证异步通知是不是支付宝发过来的，通过SDK中的**AlipaySignature.rsaCheckV2()**方法，需要在通知返回参数列表中，先除去sign(sdk中已经移除)、**sign_type**两个参数 

  - 对通知中的信息进行校验，同时要避免重复通知，官方文档的提示：

    > 商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，并判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），同时需要校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），上述有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功

  - 给支付宝服务器返回处理信息，否则支付宝服务器会不断重复发送通知

    > 程序执行完后必须打印输出“success”（不包含引号）。如果商户反馈给支付宝的字符不是success这7个字符，支付宝服务器会不断重发通知，直到超过24小时22分钟 

  

- 控制器中传入参数为HttpServletRequest request，从request中获取Map参数，需要将`Map<String, String[]>` --> `Map<String, String>` 以便于作为后面的验签参数；

- 接下来验证回调的正确性，是否来自于支付宝，要先移除**sign_type**参数：

  ```java
  // 除去sign(sdk中已经移除)、sign_type两个参数，对其他参数进行验签
  params.remove("sign_type");
  try {
      boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
      if (!alipayRSACheckedV2) {
          return ServerResponse.createByErrorMessage("非法请求，验证不通过!!!");
      }
  } catch (AlipayApiException e) {
      logger.info("支付宝回调异常", e);
  }
  ```

- 接下来将`Map<String, String>`作为参数交给服务层处理，对其中的参数进行校验；

- 首先从参数中获取**商户订单号**orderNo、**支付宝流水号**tradeNo、**交易状态**tradeStatus；

- 查询数据库校验是否是商户订单号，如果不是返回“非法”，如果是则校验订单状态，再检验交易状态(**如果交易成功则更新数据库中订单状态和付款时间**)，然后存储支付信息PayInfo到数据库中；

- 最后在控制器中给支付宝服务器返回处理信息：

  ```java
  // 验证各种数据并返回给支付宝服务器通知
  ServerResponse serverResponse = iOrderService.aliCallback(params);
  if(serverResponse.isSuccess()) {
      return Const.AlipayCallback.RESPONSE_SUCCESS;
  }
  return Const.AlipayCallback.RESPONSE_FAILED;
  ```



### 查询订单支付状态

- 控制器中传入参数为session和订单号，首先校验用户是否登录，如果是则将用户id和订单号交给服务层控制函数处理；
- 服务层控制函数根据用户id和订单号查询数据库，如果查不到则返回“用户没有订单”，否则继续校验订单状态，如果是已支付（在支付宝回调验证中，如果付款成功会更新数据库订单的状态）则返回成功，否则返回错误



### Ngrok 内网穿透

一般情况下，本机IP是内网IP，无法从外网访问本机，当把tomcat web工程部署到8080端口后，也只能从本机局域网访问，这样就无法接收到支付宝的回调通知，内网穿透可以使外网访问到局域网内的本机。

常用的内网穿透软件有花生壳、NatApp、**Ngrok**等，这里使用免费的Ngrok。

> 详细的Ngrok编译方法参见[链接](https://blog.csdn.net/zhangguo5/article/details/77848658?utm_source=5ibc.net&utm_medium=referral)
>
> 免费的Ngrok客户端**[下载地址](http://ngrok.ciqiuwl.cn/)**

- 下载客户端，解压，运行启动脚本

- 输入自定义的三级域名前缀，如“cr”

- 输入绑定的本机端口，如“8080”

- 启动成功后，部署好本地web工程，从外网访问“http://cr.ngrok.xiaomiqiu.cn/  ”即可

- 如果要自定义域名，需要先将自己的域名解析到120.78.180.104，然后执行

  ```
  ngrok -config=ngrok.cfg -hostname xxx.xxx.xxx 8080
  ```

  

### 功能测试

- 启动Tomcat  http://localhost:8080/

- 启动FTP  [ftp://127.0.0.1/]( ftp://127.0.0.1/)  （绑定21端口）

- 启动Nginx  http://localhost/  （默认80端口）   http://image.imooc.com/1.png

- 启动Ngrok  http://cr.ngrok.xiaomiqiu.cn/    （绑定本机8080端口）

- 修改属性文件中的**alipay.callback.url**，以便于本机能接收到支付宝回调通知：

  ```
  alipay.callback.url=http://cr.ngrok.xiaomiqiu.cn/order/alipay_callback
  ```

- 测试pay接口（传入的订单号和userId要在数据库中存在，并且支付状态为未支付）能否生成订单并上传二维码，返回二维码的url

- 然后使用沙箱支付宝扫码支付，查看买卖双方的账单

- 使用query_order_pay_status接口查看支付前后的订单状态



## 十、订单管理模块

### 订单管理接口设计

- 前台订单管理接口

  [链接](https://gitee.com/imooccode/happymmallwiki/wikis/%E9%97%A8%E6%88%B7_%E8%AE%A2%E5%8D%95%E6%8E%A5%E5%8F%A3?sort_id=9918)

- 后台订单管理接口

  [链接](https://gitee.com/imooccode/happymmallwiki/wikis/%E5%90%8E%E5%8F%B0_%E8%AE%A2%E5%8D%95%E6%8E%A5%E5%8F%A3?sort_id=9913)

```
<insert id="batchInsert" parameterType="list">
  insert into mmall_order_item (id, order_no,user_id, product_id,
    product_name, product_image, current_unit_price,
    quantity, total_price, create_time,
    update_time)
  values
  <foreach collection="orderItemList" index="index" item="item" separator=",">
    (
    #{item.id},#{item.orderNo},#{item.userId},#{item.productId},#{item.productName},#{item.productImage},#{item.currentUnitPrice},#{item.quantity},#{item.totalPrice},now(),now()
    )
  </foreach>
</insert>
```