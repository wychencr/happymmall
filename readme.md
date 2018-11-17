## 一、环境配置

### Jdk安装

### Maven安装

### Tomcat安装

### Ftp服务器配置

- 下载绿色版ftpserver，[链接](http://learning.happymmall.com/ftpserver/)，解压
- 双击**FTPServer.exe**，即可以配置用户名、密码、文件操作权限、文件目录等

![](https://ws1.sinaimg.cn/large/e4eff812gy1fwvxv0zh4aj20gj0akmx5.jpg)

- 点击**`启动`**按钮，浏览器访问**[ftp://localhost/](ftp://localhost/)**，即可登录



- **使用windows自带的IIS添加FTP的方法 [链接](https://www.cnblogs.com/popfisher/p/7992036.html)**，IIS启动后会与nginx冲突

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



### 文件上传

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

  

- 控制器需要先验证用户是否登录并且具备管理员权限，然后从HttpServletRequest获取上传路径path，然后调用服务层的实现方法上传文件，返回目标文件的名称(已经是UUID命名格式)，即uri，再拼接好ftp服务器的http前缀域名和文件名，组成url，然后封装到map中，作为data返回

- 服务层的实现方法是上传文件，参数为原始的MultipartFile file和String path。根据file可以获得原始文件名，再通过分割得到文件扩展名，然后生成一个随机的uuid，拼接上扩展名，得到目标文件名。接下来处理文件路径，根据传入的参数path和目标文件名，生成targetFile对象，然后通过file的transferTo方法，将文件进行转化，再调用FTPUtil的uploadFile方法上传文件



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
- 接下来是分页和排序处理，PageHelper.startPage(pageNum, pageSize)开始分页，使用PageHelper.orderBy()方法来设置排序规则，然后将非空的keyword和非空的categoryIdList作为参数，去数据库中查询符合要求的product列表，之后再讲product列表转化为ProductListVo列表，最后pageHelper收尾，将pageInfo的结果作为data返回















## 七、购物车模块

## 八、收货地址管理模块

## 九、支付模块

## 十、订单管理模块