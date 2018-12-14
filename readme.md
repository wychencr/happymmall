## 一、环境配置

### Jdk安装

### Maven安装

### Tomcat安装

### Ftp服务器配置

- 下载绿色版ftpserver，[链接](http://learning.happymmall.com/ftpserver/)，解压
- 双击**FTPServer.exe**，即可以配置用户名、密码、文件操作权限、文件目录等

![](https://ws1.sinaimg.cn/large/e4eff812gy1fwvxv0zh4aj20gj0akmx5.jpg)

- 点击**`启动`**按钮，浏览器访问**[ftp://localhost/](ftp://localhost/)**，即可登录



- 如果上面的ftp服务器不能使用，可以使用Windows自带的IIS，**使用windows自带的IIS添加FTP的方法 [链接](https://www.cnblogs.com/popfisher/p/7992036.html)**，IIS默认启动后会与nginx冲突，所以要修改默认页的端口，nginx设定的监听端口是80，所以可以将IIS默认页的端口设定为如8086，tomcat的默认端口是8080，这样就不冲突了

  ![](https://ws1.sinaimg.cn/large/e4eff812ly1fxc6mu28gdj20nz0d474l.jpg)



- 后面在使用org.apache.commons.net.ftp.FTPClient登录ftp服务器时，发现**匿名身份**不能连接成功，所以需要设置ftp登录的账户和密码。在IIS管理器的FTP身份验证设置中，禁用匿名身份验证，启用基本身份验证，然后在计算管理中，新建一个测试账户，比如用户名和密码都为ftpuser，如下图

  ![](https://ws1.sinaimg.cn/large/e4eff812gy1fxcbtko7twj20hx06pt8l.jpg)

  ![](https://ws1.sinaimg.cn/large/e4eff812gy1fxcbtpxhb1j20gv059dfo.jpg)



### Nginx安装

```
Nginx是一款轻量级Web服务器，也是一款反向代理服务器。
```

- 下载Nginx，http://learning.happymmall.com/nginx/，解压到D盘

- 进入cmd，找到nginx.exe所在目录，命令行输入

  ```
  D:\windows-nginx-1.10.2\nginx-1.10.2 > nginx.exe
  ```

  这样就启动了Nginx，在任务管理器的进程中也可以看到

- 进入`C:\Windows\System32\drivers\etc`，找到hosts文件，增加一行：

  ```
  127.0.0.1 www.imooc.com
  ```

- 然后浏览器输入www.imooc.com或者localhost，可以看到Nginx的欢迎页面

- 下载http://learning.happymmall.com/nginx/windows_conf/下的**nginx.conf**主配置文件，覆盖放到Nginx安装目录/cong/下

- 在/cong目录下新建**vhost**文件夹，下载http://learning.happymmall.com/nginx/windows_conf/vhost/下的`image.imooc.com.conf`  和 `tomcat.imooc.com.conf`  ，放置到本地vhost文件夹下

- 在F盘建立文件夹F:\ftpfile\img，并添加一个图片，例如`1.png`

- 然后修改`image.imooc.com.conf`：

  ```
  server {
      listen 80;
      autoindex off;
      server_name image.imooc.com;
      access_log f:/access.log combined;
      index index.html index.htm index.jsp index.php;
      #error_page 404 /404.html;
      if ( $query_string ~* ".*[\;'\<\>].*" ){
          return 404;
      }
  
      location ~ /(mmall_fe|mmall_admin_fe)/dist/view/* {
          deny all;
      }
  
      location / {
          root F:\ftpfile\img;
          add_header Access-Control-Allow-Origin *;
      }
  }
  ```

  这样浏览器访问image.imooc.com/*就可以重定向到F:\ftpfile\img文件目录下的文件了。

  需要注意的的，这里的配置项**autoindex off**，如果关闭，浏览器访问image.imooc.com就会显示403无权限，这样更安全。如果开启，文件夹下的所有文件就会暴露出来。

- 修改`tomcat.imooc.com.conf`

  ```
  server {
      listen 80;
      autoindex on;
      server_name tomcat.imooc.com;
      access_log f:/access.log combined;
      index index.html index.htm index.jsp index.php;
      #error_page 404 /404.html;
      if ( $query_string ~* ".*[\;'\<\>].*" ){
          return 404;
      }
      location / {
          proxy_pass http://127.0.0.1:8080;
          add_header Access-Control-Allow-Origin *;
      }
  }
  ```

  这样当浏览器访问tomcat.imooc.com时，就可以被重定向到http://127.0.0.1:8080，即Tomcat的主页。

- 修改hosts文件最后一行：

  ```
  # 127.0.0.1 www.imooc.com 修改这行
  127.0.0.1 image.imooc.com
  127.0.0.1 tomcat.imooc.com
  ```

- 进入cmd界面，输入指令

  ```
  > nginx.exe -t
  > nginx.exe -s reload
  ```

- 浏览器进入http://image.imooc.com/1.png，可以显示图片；启动tomcat安装目录/bin下的**startup.bat**，启动tomcat服务器，然后浏览器进入http://tomcat.imooc.com/，就可以重定向到tomcat主页，说明配置完成。



### MySQL安装

### Git安装



## 二、数据表结构设计

### 数据表结构

#### 用户表

![](https://ws1.sinaimg.cn/large/e4eff812gy1fww1td3tm7j20ro09zdih.jpg)

表示注册用户的信息，用户名、密码、密保等。

用户名建立唯一索引。

#### 分类表

![](https://ws1.sinaimg.cn/large/e4eff812gy1fww1psd66sj20td08in4n.jpg)

表示分类信息，id对应的是当前分类，是parent_id所对应的分类的子分类。



#### 产品表

![](https://ws1.sinaimg.cn/large/e4eff812gy1fww1rs2kfjj20ru0aigo7.jpg)

表示商品信息，包括名称、价格、详情介绍等。

decimal(20， 2)表示18个整数位，两个小数位。



#### 购物车表

![](https://ws1.sinaimg.cn/large/e4eff812gy1fww1ynyc28j20sf09tdhn.jpg)



#### 支付信息表

![](https://ws1.sinaimg.cn/large/e4eff812gy1fww22110prj20si09hwgg.jpg)



#### 订单表

![](https://ws1.sinaimg.cn/large/e4eff812gy1fww24i17w6j20sn0aiq4y.jpg)



#### 订单明细表

![](https://ws1.sinaimg.cn/large/e4eff812gy1fww7i3xn4pj20rf0ak101.jpg)

可以使用用户id和订单号组合索引。



#### 收货地址表

![](https://ws1.sinaimg.cn/large/e4eff812gy1fww2c3cjtgj20rl0a0tbi.jpg)



### 数据表关系

![](https://ws1.sinaimg.cn/large/e4eff812gy1fww39883r2j20ji0h5mzr.jpg)



- 用户注册后，信息填入user表中，登陆是读取user表
- 搜索产品时需要分类表和产品表，对于传入的分类id，进行分类表递归，找到所有符合分类的产品
- 选定产品加入购物车时，将user_id、product_id等信息放到购物车表
- 提交的订单后，需要填写购物地址等信息，存在收货地址表
- 然后生成订单号，对订单表进行处理，支付信息根据购物车表来计算
- 最后根据支付信息表的状态，修改订单表的状态



**索引与时间戳**



- 唯一索引可以避免信息提交重复，并且加快搜索时间
- 时间戳包括了**创建时间**和**更新时间**两个字段



## 三、项目初始化

### 数据库建立

- 安装并打开**Navicat for MySQL**，新建一个数据库**happymmall**，字符集**UTF-8**，默认collation: **UTF-8_Bin**
- 新建一个查询Queries，在查询编辑器中黏贴**mmall.sql**的内容，然后运行Run即可

### IDEA安装

- 官网下载专业版，可用学生邮箱激活

### Jdk配置

- 选择安装的jdk 1.8版本

### Maven配置

- 官网下载，解压缩，IDEA中设置Maven的文件夹路径，并修改settings.xml中的相关设置，修改为阿里云镜像源，修改仓库地址

### Tomcat配置

- 创建Maven Archetype Webapp项目后，设置好java源文件夹，resources资源文件夹、Test文件夹等，然后添加Tomact Server，选择解压的tomcat文件夹路径，再配置**Deployment**。

### 初始化Git仓库

- 在Github上新建仓库**happymmall**，然后clone到本地的工程文件夹HappyMMall

  ```
  git clone git@github.com:wychencr/happymmall.git
  cd happymmall
  ```

- 添加**.gitignore**文件

  ```
  *.class
  
  # package file
  *.war
  *.ear
  
  # kdiff3 ignore
  *.org
  
  # maven ignore
  target/
  
  # eclipse ignore
  .settings/
  .project
  .classpatch
  
  # idea
  .idea/
  /idea/
  *.ipr
  *.iml
  *.iws
  
  # temp file
  *.log
  *.cache
  *.diff
  *.patch
  *.tmp
  
  # system ignore
  .DS_Store
  Thumbs.db
  ```

- 再添加项目文件、readme.md等，commit后push到远程仓库中

  ```
  git add -A
  git commit -m "add some files"
  git push
  ```

- 分支开发，主干发布

  ```
  # 查看本地和远程分支
  git branch
  git branch -r
  
  # 新建一个分支，并切换到该分支
  git checkout -b v1.0 origin/master
  
  # 推送到远程分支
  git push origin HEAD -u
  ```

  

### 配置pom.xml

- 存在几个jar包不能导入的情况，此时适当降低版本号，可以解决，比如mybatis的分页插件，改为4.1.0版本后可正常使用

### 项目包结构初始化

- 在WEB-INF下需要额外建立一个lib文件夹，存放支付宝的SDK包
- 在SSM工程的基础上，要修改好tomcat和module配置
- logback日志配置
- ftp服务器属性配置
- 修改IDEA设置，`spring core` `code` `autowriting for bean class` 为 **warning**

### chrome插件安装

- WEB前端助手(FeHelper)

  ```
  包括JSON格式化、二维码生成与解码、信息编解码、代码压缩、美化、页面取色、Markdown与HTML互转、网页滚动截屏、正则表达式、时间转换工具、编码规范检测、页面性能检测、Ajax接口调试、密码生成器、JSON比对工具、网页编码设置
  ```

  

- Restlet Client

  ```
  Visually create and run single HTTP requests
  ```



## 四、用户模块开发

### 横向越权与纵向越权

- 横向越权：攻击者尝试访问与他拥有相同权限的用户的资源，比如A用户尝试查询B用户的订单号
- 纵向越权：低级别攻击者尝试访问高级别用户的资源，比如普通用户通过分析接口尝试上升到管理员级别

### MD5加盐值salt

- MD5存在一些字典，如果是单纯的MD5加密，则可能通过字典将密码破解出来
- 盐值salt可以是自定义的一个字符串，当获取用户密码后，拼接上这个字符串，再进行MD5加密，这样就增加了破解难度

### 用户接口设计

- 门户用户接口设计

  [链接](https://gitee.com/imooccode/happymmallwiki/wikis/%E9%97%A8%E6%88%B7_%E7%94%A8%E6%88%B7%E6%8E%A5%E5%8F%A3?sort_id=9917)

- 后台用户接口设计

  [链接](https://gitee.com/imooccode/happymmallwiki/wikis/%E5%90%8E%E5%8F%B0_%E7%94%A8%E6%88%B7%E6%8E%A5%E5%8F%A3?sort_id=9912)

### 登录功能

- 在common包下创建三个类，Const，ResponseCode，ServerResponse，ResponseCode是一个枚举类，包含了不同的状态信息和状态码；ServerResponse实现了序列化接口，包含了三个字段，分别是状态码，状态信息，和泛型的数据，通过不同的静态方法create*方法就可以给这三个字段赋值，并返回新的ServerResponse对象
- 服务层UserServiceImpl中注入UserMapper方法，有一个login登录方法，参数为用户名和密码，返回的是ServerResponse <User>类型。login方法中通过UserMapper接口检查数据库，判断用户名是否错误，密码是否错误，如果都正确，从数据库中查询到对应的User对象返回到ServerResponse的data字段中，状态消息msg为“登录成功“，状态码status为0
- 控制层的UserController类中注入了服务层的IUserService，含有login方法，uri对应为/login/login，限定post方法，返回值为ServerResponse<User>，使用**@ResponseBody**注解实现将Controller类方法返回对象转换为json响应给客户端(在springmvc-servlet.xml已经配置了HttpMessageConverter和JSON消息转换器)。根据前端传来的username和password方法，调用IUserService的login方法，获得ServerResponse <User>实例，通过isSuccess方法(状态码是否为0)判断是否登录成功，如果是则通过getData()方法将data(即User讯息)传给httpsession

### 登出功能

- 在UserController类中添加logout方法，对应的uri为/login/logout，从session中移除刚刚登录时传入的User信息即可

### 注册功能

- 在Util包下添加MD5Util工具类，用于密码的MD5加密，这样存入数据库的密码不会明文显示
- 服务层UserServiceImpl类中添加register方法，参数为User，返回类型ServerResponse <String>。首先检查用户名和Email是否重复，如果核验通过则设置User的Role属性为普通用户，并将密码MD5加密，然后调用UserMapper接口的insert方法插入用户信息到数据库中

### 校验功能

- 便于用户在注册时实时检查用户名和邮件是否有效

- 服务层UserServiceImpl类中添加checkValid方法，参数为String型的str和type，如果type是“username”，则将str作为username在数据库中查找是否重复；如果type是“email”，则将str作为email在数据库中查找是否重复
- 控制器UserController类中添加checkValid方法，uri为/login/check_valid

### 获取用户登录信息

- 控制器UserController类中添加getUserInfo方法，通过session.getAttribute中获取User信息

### 忘记密码

#### 1、找到密保问题

- 服务层UserServiceImpl类中添加selectQuestion方法，校验用户名是否存在，如果有则查询数据库中的密码提示问题，将问题作为data返回即可
- 控制器UserController中调用服务层的selectQuestion方法

#### 2、检验密保问题是否正确

- common包下添加TokenCache类，可以调用其setKey方法将key：token_username和value：randomUUID放入**本地缓存**中也可以通过getKey方法查询value值
- 服务层UserServiceImpl类中添加checkAnswer方法，验证用户名-问题-答案在数据库中是否匹配，如果是，则生成一个随机的uuid，与token_username形成键值对，加入到本地缓存中，并将token(uuid)作为data返回；如果否则显示密码错误
- 控制器UserController中调用服务层的checkAnswer方法，将**token**返回到前端，修改密码的时候需要用这个 

#### 3、重置新密码

- 服务层UserServiceImpl类中添加resetPassword方法，传入的参数为用户名、新密码、forgetToken，首先检查传入的forgetToken是否为空白，然后检查用户名有效性，再根据用户名查询本地缓存中的token，再比较本地的token和传入的forgetToken是否一致，如果是则更新密码，把新密码MD5加密后更新到数据库中
- 控制器UserController中调用服务层的forgetResetPassword方法返回即可

### 登录状态

#### 1、登录状态下重置密码

- 服务层UserServiceImpl类中添加resetPassword方法，传入的参数为旧密码、新密码、用户。首先是验证旧密码是否正确，注意这里要**防止横向越权**，校验用户旧密码的同时，一定要同时校验用户名，否则容易被人用密码字典撞库，如果查不到则返回错误消息，否则下一步将新密码MD5加密更新到User中，再更新到数据库中
- 控制器UserController中添加resetPassword方法，参数为旧密码、新密码和HttpSession，先从session中获取当前用户信息，如用户存在则调用上面的UserServiceImpl的resetPassword方法，否则返回“用户未登录”消息

#### 2、更新个人信息 

- 服务层UserServiceImpl类中添加updateInformation方法，传入的参数为User，其中username和userId被设置为当前登录用户的信息(username和userId是不可被修改部分，其他信息就来自于用户修改的信息，从前端传过来，此部分操作在控制器中执行)。然后首先要校验下email是否有效，如果email和当前登录用户的email不一致，则不能与数据库中其他用户的email重复。如果校验成功就可以new一个User实例updateUser，将可更新的内容赋给updateUser，然后调用userMapper的updateByPrimaryKeySelective方法，对非null的项，根据userId更新到数据库中。如果更新成功，则显示更新成功消息，并且把更新后的updateUser保存到data中，以便于在控制器中可以更新session参数
- 控制器UserController中添加updateInformation方法，参数为HTTPSession和User，先检查用户是否登录，即session中是否有user信息。如果是，则把当前登录用户的用户名和ID赋给传进来的user，之后在调用服务层UserServiceImpl类中的updateInformation方法，如果更新成功，则将更新的用户对象存在session中，否则直接返回即可

#### 3、根据用户ID获取登录用户信息

- 如果未登录，则返回status=10，**前端可以强制跳转到登录界面**

- 服务层通过userId从数据库查询用户信息
- 控制器中先判断用户是否登录，如果是则从session中获取User对象，再通过userId调用服务层方法查询数据库，得到的User信息返回给前端

### 后台管理员登录 

- controller/backend包下添加UserManagerController类，注入服务层实现类iUserService，添加login方法
- 在login方法中，首先调用iUserService的login方法，判断是否登录成功，如果成功，则根据role判定是否为管理员



## 五、分类管理模块

### 分类接口设计

- 后台分类接口

  [链接](https://gitee.com/imooccode/happymmallwiki/wikis/%E5%90%8E%E5%8F%B0_%E5%93%81%E7%B1%BB%E6%8E%A5%E5%8F%A3?sort_id=9911)

### 添加品类

- 根据传入的品类名称和父节点ID类添加品类
- 控制器中首先检查管理员是否登录，如果登录调用服务层以下的方法
- 首先需要检查传入参数的有效性，如果为空则返回参数错误，之后new一个Category对象，设置品类名称、父节点ID和状态，然后调用categoryMapper的insert方法，插入category到数据库

### 更改品类名称

- 传入参数为品类ID和品类名，将根据这个ID来修改品类名
- 控制器中首先检查管理员是否登录，如果登录调用服务层以下的方法
- 首先需要检查传入参数的有效性，如果为空则返回参数错误，之后new一个Category对象，设置品类名称、品类ID，然后调用categoryMapper的updateByPrimaryKeySelective方法，更新品类名到数据库

### 查询次级子节点品类

- 传入参数为品类ID，将根据这个ID来查询其所有的次级子节点品类，返回值类型为ServerResponse <List <Category>> (在正确查询的情况下，否则返回错误消息)
- 控制器中首先检查管理员是否登录，如果登录调用服务层getChildrenParallelCategory方法
- 首先需要检查传入参数的有效性，如果为空则返回参数错误，然后调用categoryMapper的selectCategoryChildrenByParentId方法，将查到的List <Category>作为data返回

### 查询当前节点和所有子节点

- 传入参数为品类ID，将根据这个ID来查询当前节点和所有子节点的ID值，放到list中，作为data返回

- 控制器中首先检查管理员是否登录，如果登录调用服务层的selectCategoryAndChildrenById方法

- 服务层的findChildCategory方法，参数为set(去重，使用set要重写category  pojo的hashcode和equal方法，以id为判断准则)，每次根据id来从数据库查找category对象，如果存在则加入到set中，然后查找数据库中所有以当前节点为父节点的子节点，存储到list中，然后遍历子节点，循环调用findChildCategory方法，如果当前节点没有子节点了，那么遍历的过程也不会进入，最后返回set，即查到了所有的子节点和当前节点，代码如下：

  ```java
  // 递归算法，算出左右的子节点
      private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
          // 查找当前节点，加入到Set中
          Category category = categoryMapper.selectByPrimaryKey(categoryId);
          if (category != null) {
              categorySet.add(category);
          }
          // 查找次级所有子节点
          List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
          // 如果查不到子节点，则不会执行以下循环；如果查到了，则遍历子节点，递归
          for (Category categoryItem : categoryList) {
              findChildCategory(categorySet, categoryItem.getId());
          }
          return categorySet;
      }
  ```

- 服务层的selectCategoryAndChildrenById方法，先初始化一个空set，然后根据传入的categoryId，调用findChildCategory方法，之后再把set中所有的category的id提取到一个list中，作为ServerResponse的data返回，代码如下;

  ```java
   public  ServerResponse selectCategoryAndChildrenById(Integer categoryId) {
          Set<Category> categorySet = Sets.newHashSet();
          findChildCategory(categorySet, categoryId);
  
          // 将set转为list返回
          List<Integer> categoryIdList = Lists.newArrayList();
          if (categoryId != null) {
              for (Category categoryItem : categorySet) {
                  categoryIdList.add(categoryItem.getId());
              }
          }
          return ServerResponse.createBySuccess(categoryIdList);
      }
  ```



## 六、商品管理模块

### POJO、BO、VO

![](https://ws1.sinaimg.cn/large/e4eff812ly1fx8mz27s65j20j309g3yh.jpg)

### 产品接口设计

- 后台产品接口

  [链接](https://gitee.com/imooccode/happymmallwiki/wikis/%E5%90%8E%E5%8F%B0_%E4%BA%A7%E5%93%81%E6%8E%A5%E5%8F%A3?sort_id=9910)

- 门户产品接口

  [链接](https://gitee.com/imooccode/happymmallwiki/wikis/%E9%97%A8%E6%88%B7_%E4%BA%A7%E5%93%81%E6%8E%A5%E5%8F%A3?sort_id=9914)

### 新增或更新产品

- 传入参数为Product对象，如果是新增产品，则Product实例中的Id应该为空，这样就调用mapper接口的insert方法，来更新产品信息；如果是更新产品信息，则Id应该不为空，调用mapper的updateByPrimaryKey方法来更新产品信息，其中主图来自于子图的第一个
- 控制器需要先验证用户是否登录并且具备管理员权限

### 产品上下架

- 传入参数为产品Id和状态status(表示在售、下架、删除)
- 控制器需要先验证用户是否登录并且具备管理员权限
- 服务层实现方法要先校验参数有效性，使用mapper接口的updateByPrimaryKeySelective来更新参数

### 产品详情

- 传入参数为产品Id，很据这个id返回产品详情，返回的产品详情相比于pojo中的product对象内容更加丰富，所以这里添加一个**vo(value object)**类，除了原来的product属性外，还添加了imageHost和parentCategoryId，同时创建时间和更新时间也需要转换成年月日的形式**(joda**)
- 控制器需要先验证用户是否登录并且具备管理员权限
- 服务层实现方法要先校验参数productId的有效性，然后检查这个id能否从数据库中查到product对象，如果查到了，将product装配到productDetailVo中，调用装配函数assembleProductDetailVo，参数为product，将productDetailVo作为data返回
- assembleProductDetailVo返回值为ProductDetailVo，先装配product的各个属性，然后装配imageHost(ftp服务器url的前缀)，从属性文件中获取，再装配ParentId，根据当前产品的分类id来从数据库中查父节点分类ParentId，如果查不到当前分类，则将ParentId置为0

### 商品列表动态分页

- 传入参数为pageNum(默认=1)和pageSize(默认=10)，返回产品列表，这里添加一个ProductListVo，相比于product的pojo，省略了一些不必要的展示信息
- 控制器需要先验证用户是否登录并且具备管理员权限
- 服务层实现方法先配置**pageHelper**开始页，然后写sql查询产品列表，再遍历列表转化为ProductListVo的列表，最后pageHelper收尾，将pageResult的结果作为data返回

### 产品搜索

- 传入参数为产品名称、产品ID、pageNum(默认=1)、pageSize(默认=10)，搜索返回产品列表

- 与上一个功能类似，区别在于sql查询语句上，如果productName或者productId为空，就不作为查询条件，如果productName不为空，前后要加上sql的通配符“%”，这样可以实现模糊搜索

- 查询的SQL语句使用where标签和if标签，如下：

  ```mysql
  SELECT
      <include refid="Base_Column_List"/>
  from mmall_product
  <where>
      <if test="productName != null">
          and name like #{productName}
      </if>
      <if test="productId != null">
          and id = #{productId}
       </if>
  </where>
  ```



### 文件上传(重要)

- 通过form表单，发起post请求，文件类型为multipart/form-data，匹配spring mvc的格式

  ```jsp
  <h2>Spring mvc 上传文件</h2>
  <form name="form1" action="/manage/product/upload" method="post" enctype="multipart/form-data">
      <input type="file" name="upload_file" />
      <input type="submit" value="Spring mvc 上传文件" />
  </form>
  ```

- 控制器返回的是一个map，包括了上传图片URL和URI。当图片上传到ftp服务器后，将图片的地址返回给前端，这样就可以把图片在网页上呈现出来，图片的命名通过随机的UUID生成，避免重复，返回的json格式为：

  ```json 
  {
      "status": 0,
      "data": {
          "uri": "e6604558-c0ff-41b9-b6e1-30787a1e3412.jpg",
          "url": "http://img.happymmall.com/e6604558-c0ff-41b9-b6e1-		   30787a1e3412.jpg"
      }
  }
  ```

  

- 控制器的传入参数为

  ```java
  public ServerResponse upload(HttpSession session, @RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request)
  ```

  首先需要根据session验证用户是否登录并且具备管理员权限，然后从request获取上传文件夹的路径path(`/target/happymmall/upload`)，然后调用服务层的实现方法上传文件，返回目标文件的名称(已经是UUID命名格式)，即uri，再拼接好ftp服务器的http前缀域名和文件名，组成url，然后封装到map中，作为data返回

- 服务层的实现方法是上传文件，参数为原始文件MultipartFile file和String path。根据file可以获得原始文件名，再通过分割得到文件扩展名，然后生成一个**随机的uuid(避免重复)**，拼接上扩展名，得到目标文件名。

- 接下来处理文件路径，根据传入的参数path和目标文件名，生成targetFile对象，然后通过file的**transferTo**方法(相当于将原始文件复制并重命名到上传文件夹，再调用FTPUtil的**uploadFile**方法上传文件，上传结束后将targetFile文件删除，否则上传文件夹会越变越大

- 接下来是FTPUtil的uploadFile方法，传入参数为targetFile(列表)。首先尝试连接和登录ftp服务器，需要host，port(默认21)，用户名和密码，ftp属性文件`mmall.properties`的配置内容为：

  ```properties
  ftp.server.ip=127.0.0.1
  ftp.user=ftpuser
  ftp.pass=ftpuser
  ftp.server.http.prefix=http://image.imooc.com/
  ```

- 然后配置ftp连接的相关属性，最重要的就是配置被动模式，否则会报**SocketException: Connection reset**或者**FTPConnectionClosedException: Connection closed without indication**异常，在这里困扰了很长时间，[FTPClient类的说明文档](http://commons.apache.org/proper/commons-net/apidocs/org/apache/commons/net/ftp/FTPClient.html#storeFileStream(java.lang.String))，最后参考自[stackoverflow](https://stackoverflow.com/questions/8333797/ftpclient-uploading-file-socketexception-connection-reset)解决，FTP的几种连接模式简介[PORT/PASV/EPRT/EPSV](https://www.cnblogs.com/wuyuxuan/p/5544725.html)

  ```java
   ftpClient.changeWorkingDirectory(remotePath);
   ftpClient.setBufferSize(1024);
   ftpClient.setControlEncoding("UTF-8");
   ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
  
   /************************** 很重要 ***********************/
   ftpClient.enterLocalPassiveMode();
   ftpClient.setUseEPSVwithIPv4(true);
   /********************************************************/
  ```

- **上传功能测试**：

  1、启动项目(运行tomcat)

  2、启动IIS，并开启ftp站点，以便于java程序可以向ftp的/img路径下上传图片文件

  3、启动Nginx，上传后的文件拼接了域名前缀，这样就可以通过返回的URL：http://image.imooc.com/xxx.png来直接访问到图片

  4、先登录管理员账户，然后在首页选择一个图片上传，之后返回Map数据，包含了上传后图片的URL，点击可以打开，再访问ftp服务器，发现新增了一张图片，日志如下：

  ```
  [com.cr.mmall.service.impl.FileServiceImpl] - 开始上传文件，上传文件的文件名:2d3fa4e3-87d0-41d7-bd5b-8135b13acbcf.png，上传的路径:E:\Java Projects\HappyMMall\happymmall\target\happymmall\upload，新文件名:0ee91666-8a46-4d4b-9e52-557a37f24b96.png
  [com.cr.mmall.service.impl.FileServiceImpl] - 文件开始上传
  [com.cr.mmall.util.FTPUtil] - 开始连接ftp服务器
  [com.cr.mmall.util.FTPUtil] - 尝试连接FTP服务器
  [com.cr.mmall.util.FTPUtil] - 连接FTP服务器状态：true
  [com.cr.mmall.util.FTPUtil] - 连接ftp服务器成功
  [com.cr.mmall.util.FTPUtil] - 结束上传，上传结果:true
  [com.cr.mmall.service.impl.FileServiceImpl] - 已经上传到ftp服务器上
  [com.cr.mmall.service.impl.FileServiceImpl] - 将目标文件删除
  [com.cr.mmall.service.impl.FileServiceImpl] - 目标文件名称：0ee91666-8a46-4d4b-9e52-557a37f24b96.png
  ```

  

  

### 富文本图片上传

- 总体上与文件上传类似，不同的是，富文本对返回值有要求，按照simditor的要求进行返回即可

  ```json
  {
      "success": true/false,
      "msg": "error message", # optional
      "file_path": "[real file path]"
  }
  ```

  

### 前台获取产品详情

- 与后台获取产品详情基本一致，不同之处在于，这里需要多判断一次，如果product的status表示下架或者删除状态，则返回错误消息“产品已下架或者删除”，并且不要判断管理员是否登录



### 前台产品搜索及排序

- 传入参数为产品关键词、分类ID、排序规则(价格升序、降序)，pageNum(默认=1)和pageSize(默认=10)，通过关键词或者分类来筛选产品，以产品分页列表的形式返回，与上面的**商品列表动态分页**类似
- 首先是对输入参数进行判断，如果keyword和categoryId都为空，则返回参数错误；如果categoryId不为空，但是查不到该分类的产品，且keyword为空，此时返回空结果集，不算是错误，new一个空的List<ProductListVo>，作为data返回即可；如果keyword不为空，则给它两端加上通配符“%”；如果categoryId不为空且可以查到该分类，则需要递归查询属于它的所有子分类，存储到一个list中categoryIdList
- 接下来是**分页和排序处理**，PageHelper.startPage(pageNum, pageSize)开始分页，使用PageHelper.orderBy()方法来设置排序规则，然后将非空的keyword和非空的categoryIdList作为参数，去数据库中查询符合要求的product列表，之后再将product列表转化为ProductListVo列表，最后pageHelper收尾，将pageInfo的结果作为data返回



## 七、购物车模块

### 购物车接口设计

- 门户购物车接口

  [链接](https://gitee.com/imooccode/happymmallwiki/wikis/%E9%97%A8%E6%88%B7_%E8%B4%AD%E7%89%A9%E8%BD%A6%E6%8E%A5%E5%8F%A3?sort_id=9919)

### 查询购物车列表

- 控制层只需要HttpSession参数，获取登录信息，并得到userId供服务层处理

- 将查询的购物车列表封装成一个**CartVo**对象，包含了一个CartProductVo列表、购物车总价、是否全选、imageHost，其结构为：

  ```java
  public class CartVo {
      private List<CartProductVo> cartProductVoList;
      private BigDecimal cartTotalPrice;
      private Boolean allChecked;
      private String imageHost;
  }
  ```

  其中**CartProductVo**也是一个Vo对象，表示购物车中某一种产品的详细信息，其结构为：

  ```java
  public class CartProductVo {
      private Integer id;
      private Integer userId;
      private Integer productId;
      private Integer quantity;
      private String productName;
      private String productSubtitle;
      private String productMainImage;
      private BigDecimal productPrice;
      private Integer productStatus;
      private BigDecimal productTotalPrice;
      private Integer productStock;  // 产品库存
      private Integer productChecked;  // 产品是否被勾选
  
      private String limitQuantity;  // 限制数量的返回结果
  }
  ```

- 在服务层定义一个getCartVoLimit方法，参数为userId，返回值为**CartVo**对象。实现过程为：首先通过userId从数据库中查找属于该用户的cart列表，然后遍历列表，根据单个的cart中的产品Id查询对应的product，再结合cart和product中的信息，封装成**CartProductVo**，再将CartProductVo添加到cartProductVoList中；

- cartTotalPrice购物车总价的计算方式是**已勾选**产品的产品总价（单价*数量）之和，其中计**算价格时使用BigDecimal避免精度丢失**；

- 在计算产品总价时，需要先判断购物车中的数量是否大于产品库存数量，如果大于则更新数据库，将购物车中的数量设定为等于产品库存数量，如果小于则直接计算总价；

- allChecked是根据从数据库中查未checked的数量是不是0得到的；imageHost从属性文件中获得

- 经过以上步骤，可以通过一个userId得到一个CartVo，直接将CartVo作为ServerResponse的data返回即可

- 在测试发现，使用查询购物车列表时出现**500错误**，经过调试，发现是数据库中cart表的productId在product表中查不到，导致了后续的程序错误。解决方法是，在getCartVoLimit方法中，当查询productId时，如果查不到就将cartVo作为null返回；服务层处理函数中，再对cartVo进行判断，如果是null就返回参数错误

### 添加到购物车

- 控制器传入参数为产品Id和数量，首先判断登录情况，如果未登录强制登录，否则将参数交给服务层处理；
- 服务层首先校验参数的有效性，然后通过userId和productId从数据库中查询cart；如果cart未查到，则新建一个cart，设置userId、productId、count，设置勾选状态，然后插入到数据库中；否则在查到的cart的基础上更改count，再更新到数据库；
- 对数据库更新完成后，查询购物车列表，即根据userId获取cartVo，作为ServerResponse的data返回

### 更新购物车

- 控制器传入参数为产品Id和数量，首先判断登录情况，如果未登录强制登录，否则将参数交给服务层处理；
- 服务层首先校验参数的有效性，然后通过userId和productId从数据库中查询cart，在查到的cart的基础上更改count，再更新到数据库；
- 对数据库更新完成后，查询购物车列表，即根据userId获取cartVo，作为ServerResponse的data返回

### 删除购车产品

- 控制器传入参数为productId(**productId是多个产品ID的字符串，由逗号隔开**)，首先判断登录情况，如果未登录强制登录，否则将参数交给服务层处理；

- 服务层将productIds处理成productList，然后再结合userId，从数据库中删除对应的产品项，sql语句为：

  ```xml
  <delete id="deleteByUserIdProductIds" parameterType="map">
    delete from mmall_cart
    where user_id = #{userId}
    <if test="productIdList != null">
      and product_id in
      <foreach collection="productIdList" item="item" index="index" open="(" separator="," close=")">
        #{item}
      </foreach>
    </if>
  </delete>
  ```

- 对数据库更新完成后，查询购物车列表，即根据userId获取cartVo，作为ServerResponse的data返回



### 单选/取消单选

- 控制器传入参数为HttpSession和productId，首先判断登录情况，如果未登录强制登录，否则将参数交给服务层处理；

- 服务层处理函数的参数为userId、productId和勾选状态（单选checked=1，取消单选checked=0），根据这三个参数对数据库中属于该用户的某个产品项的勾选状态进行更改，其中sql语句为：

  ```xml
  <update id="checkedOrUncheckedProduct" parameterType="map">
      UPDATE  mmall_cart
      set checked = #{checked},
      update_time = now()
      where user_id = #{userId}
      <if test="productId != null">
        and product_id = #{productId}
      </if>
  </update>
  ```

- 最后查询购物车列表，将cartVo作为ServerResponse的data返回即可

### 全选/取消全选

- 控制器传入参数为HttpSession，首先判断登录情况，如果未登录强制登录，否则将参数交给服务层处理；
- 服务层处理函数的参数同上，因此**对于productId参数，在控制器传入时设置为null**，在mysql语句中已经对null情况作了判断

### 查询购物车产品总数

- 控制器传入参数为HttpSession，首先判断登录情况，如果**未登录则返回0**，否则将参数交给服务层处理；

- 服务层处理函数根据userId查询购物车中产品总数量，查询语句为：

  ```xml
  <select id="selectCartProductCount" parameterType="int" resultType="int">
    select IFNULL(sum(quantity),0) as count from mmall_cart where user_id = #{userId}
  </select>
  ```

  值得注意的是，有可能根据userId查不到结果，为了避免出现null的情况，使用IFNULL()语句，设置默认返回值为0；

- 最后将查到的整形返回值作为ServerResponse的data返回



## 八、收货地址管理模块

### 收货地址接口设计

- 门户收货地址接口

  [链接](https://gitee.com/imooccode/happymmallwiki/wikis/%E9%97%A8%E6%88%B7_%E6%94%B6%E8%B4%A7%E5%9C%B0%E5%9D%80%E6%8E%A5%E5%8F%A3?sort_id=9916)

### 添加地址

- 控制器传入参数为HttpSession和Shipping对象，首先判断登录情况，如果未登录强制登录，否则将shipping参数和userId交给服务层处理；
- 传过来的shipping对象中userId正常情况下是空的，所以在服务层处理函数中，首先对shipping的userId项赋值，然后再将shipping插入到数据库中；
- 如果插入成功，则返回“新建地址成功”，并将插入后这条记录的shippingId返回



### 删除地址

- 控制器传入参数为HttpSession和shippingId，首先判断登录情况，如果未登录强制登录，否则将shippingId和userId交给服务层处理；

- 在服务层处理函数中，根据shippingId和userId把数据库中的对应项删除，然后返回“删除地址成功”或者“删除地址失败”；

  ```
  这里要注意横向越权，对于一个已登录用户，删除地址时不能仅通过shippingId，还同时要校验userId，否则会删除掉别人的地址记录
  ```



### 更新地址

- 控制器传入参数为HttpSession和Shipping对象（需要包含id项），首先判断登录情况，如果未登录强制登录，否则将shipping参数和userId交给服务层处理；

- 在服务层处理函数中，首先对shipping的userId项赋值，然后根据userId和shippingId在数据库中更新，最后返回“更新地址成功”或者“更新地址失败”

  ```
  传过来的userId可能会被模拟，造成横向越权，所以这里要重新赋值一下
  ```

- mysql更新语句为：

  ```xml
  <update id="updateByShipping" parameterType="com.cr.mmall.pojo.Shipping">
    update mmall_shipping
    set
      receiver_name = #{receiverName,jdbcType=VARCHAR},
      receiver_phone = #{receiverPhone,jdbcType=VARCHAR},
      receiver_mobile = #{receiverMobile,jdbcType=VARCHAR},
      receiver_province = #{receiverProvince,jdbcType=VARCHAR},
      receiver_city = #{receiverCity,jdbcType=VARCHAR},
      receiver_district = #{receiverDistrict,jdbcType=VARCHAR},
      receiver_address = #{receiverAddress,jdbcType=VARCHAR},
      receiver_zip = #{receiverZip,jdbcType=VARCHAR},
      create_time = #{createTime,jdbcType=TIMESTAMP},
      update_time = now()
    where id = #{id,jdbcType=INTEGER}
    and user_id = #{userId,jdbcType=INTEGER}
  </update>
  ```

### 查询地址

- 控制器传入参数为HttpSession和shippingId，首先判断登录情况，如果未登录强制登录，否则将shippingId和userId交给服务层处理；
- 在服务层处理函数中，根据shippingId和userId查询数据库，如果查询成功则显示“查询地址成功”并将查到的shipping对象返回，否则返回“查询地址失败”

### 查询用户地址列表

- 控制器传入参数为HttpSession和分页参数（pageNum、pageSize，使用RequestParam设置默认值为1和10），首先判断登录情况，如果未登录强制登录，否则将分页参数和userId交给服务层处理；

- 在服务层处理函数中，根据userId查询数据库，得到地址列表，然后通过pageHelper的分页，将分页信息pageInfo返回即可，相关代码如下：

  ```java
  @Override
  public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize) {
      PageHelper.startPage(pageNum, pageSize);
      List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
      PageInfo pageInfo = new PageInfo(shippingList);
      return ServerResponse.createBySuccess(pageInfo);
  }
  ```



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



### 创建订单

- 控制器中传入参数为session和收货地址ID，首先校验用户是否登录，如果是则将用户id和shippingId交给服务层控制函数处理；

- 服务层处理函数首先根据userId查询购物车表，得到购物车中**已勾选**的商品明细，然后遍历得到订单总价（前提要校验产品售卖状态和库存），转换为订单明细的列表，再生成订单Order（订单号的生成方式是`currentTime + new Random().nextInt(100)`，避免重复，后续可以更详细的对订单号的生成方式进行编排），接着遍历订单明细的列表，给每个订单明细OrderItem添加订单号，之后将订单明细列表批量插入到order_item表中

  ```xml
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

- 通过购物车商品生成订单后，将产品库存减少，并清空购物车中已选的商品

- 返回给前端的订单数据还需要进一步装配，将Order和OrderItemList装配成OrderVo返回即可



### 取消订单

- 控制器中传入参数为session和订单号orderNo，首先校验用户是否登录，如果是则将用户id和orderNo交给服务层控制函数处理；
- 服务层处理函数首先根据userId和orderNo查询数据库中的对应订单order，判断订单是否存在，判断订单状态**是否为已支付**，如果订单存在且未支付，则更新订单状态为**`CANCELED`**，更新到数据库订单列表中，返回成功



### 获取订单商品信息

- 控制器中传入参数为session，首先校验用户是否登录，如果是则将用户id交给服务层控制函数处理；
- 服务层处理函数中首先根据userId查询购物车中已勾选商品的明细列表cartList，然后转换成订单明细列表orderItemList，再转换成orderItemVo的列表orderItemVoList，并计算订单商品总价，根据以上信息装配orderProductVo，作为ServerResponse的data返回即可



### 获取订单详情

- 控制器中传入参数为session和订单号orderNo，首先校验用户是否登录，如果是则将用户id和orderNo交给服务层控制函数处理；
- 服务层处理函数根据userId和orderNo查询数据库订单表，判断订单order是否存在，然后再根据userId和orderNo查询数据库订单明细表，得到订单明细列表orderItemList，然后根据order和orderItemList装配orderVo，作为ServerResponse的data返回



### 获取用户订单分页列表

- 控制器中传入参数为session、**pageNum**、**pageSize**，首先校验用户是否登录，如果是则将用户id和pageNum、pageSize交给服务层控制函数处理；

- 服务层处理函数仅根据userId查询数据库订单表，得到订单的列表orderList，然后装配成orderVo的列表orderVoList，作为分页信息的列表，最后将分页信息pageInfo作为ServerResponse的data返回即可

  ```java
  public ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize) {
      PageHelper.startPage(pageNum, pageSize);
  
      List<Order> orderList = orderMapper.selectByUserId(userId);
      List<OrderVo> orderVoList = assembleOrderVoList(orderList, userId);
  
      PageInfo pageInfo = new PageInfo(orderList);
      pageInfo.setList(orderVoList);
      return ServerResponse.createBySuccess(pageInfo);
  }
  ```



### 管理员获取用户订单分页列表

- 与上面类似，不同之处在于控制器中需要判断是否是管理员，服务层查订单表不需要userId，把全部订单表都查出来，分页即可

  ```java
  public ServerResponse<PageInfo> manageList(int pageNum, int pageSize) {
      PageHelper.startPage(pageNum, pageSize);
  
      List<Order> orderList = orderMapper.selectAllOrder();
      List<OrderVo> orderVoList = assembleOrderVoList(orderList, null);
  
      PageInfo pageInfo = new PageInfo(orderList);
      pageInfo.setList(orderVoList);
      return ServerResponse.createBySuccess(pageInfo);
  }
  ```



### 管理员获取订单详情

- 与普通用户查看订单详情类似，传入参数为session和订单号orderNo，不同之处在于控制器中需要判断是否是管理员，查询数据库时不需要传入userId



### 管理员按订单号查询

- 与管理员获取订单详情类似，传入参数为session和订单号orderNo，不同之处在于还要添加分页分页参数，为后续的模糊匹配查询作准备



### 管理员订单发货

- 控制器中传入参数为session和订单号orderNo，首先校验用户是否登录，判断管理员权限，如果是则将orderNo交给服务层控制函数处理；
- 服务层处理函数根据orderNo查询数据库订单表，判断订单是否存在，再判断订单状态是否为已支付，如果是则更新状态为**`SHIPPED`**，并同时更新发货时间，更新到数据库中，返回“发货成功”