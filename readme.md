## 一、环境配置

### Jdk安装

### Maven安装

### Tomcat安装

### Ftp服务器配置

- 下载绿色版ftpserver，[链接](http://learning.happymmall.com/ftpserver/)，解压
- 双击**FTPServer.exe**，即可以配置用户名、密码、文件操作权限、文件目录等

![](https://ws1.sinaimg.cn/large/e4eff812gy1fwvxv0zh4aj20gj0akmx5.jpg)

- 点击**`启动`**按钮，浏览器访问**ftp://localhost/**，即可登录



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

### 用户接口设计

- 门户用户接口设计

  [链接](https://gitee.com/imooccode/happymmallwiki/wikis/%E9%97%A8%E6%88%B7_%E7%94%A8%E6%88%B7%E6%8E%A5%E5%8F%A3?sort_id=9917)

- 后台用户接口设计

  [链接](https://gitee.com/imooccode/happymmallwiki/wikis/%E5%90%8E%E5%8F%B0_%E7%94%A8%E6%88%B7%E6%8E%A5%E5%8F%A3?sort_id=9912)

### 登录功能

- 在common包下创建三个类，Const，ResponseCode，ServerResponse，ResponseCode是一个枚举类，包含了不同的状态信息和状态码；ServerResponse实现了序列化接口，包含了三个字段，分别是状态码，状态信息，和泛型的数据，通过不同的静态方法create*方法就可以给这三个字段赋值，并返回新的ServerResponse对象
- 服务层UserServiceImpl中注入UserMapper方法，有一个login登录方法，参数为用户名和密码，返回的是ServerResponse <User>类型。login方法中通过UserMapper接口检查数据库，判断用户名是否错误，密码是否错误，如果都正确，从数据库中查询到对应的User对象返回到ServerResponse的data字段中，状态消息msg为“登录成功“，状态码status为0
- 控制层的UserController类中注入了服务层的IUserService，含有login方法，uri对应为/login/login，限定post方法，返回值为ServerResponse<User>。根据前端传来的username和password方法，调用IUserService的login方法，获得ServerResponse <User>实例，通过isSuccess方法(状态码是否为0)判断是否登录成功，如果是则通过getData()方法将data(即User讯息)传给httpsession

### 登出功能

- 在UserController类中添加logout方法，对应的uri为/login/logout，从session中移除刚刚登录时传入的User信息即可

### 注册功能

- 在Util包下添加MD5Util工具类，用于密码的MD5加密，这样存入数据库的密码不会明文显示
- 服务层UserServiceImpl类中添加register方法，参数为User，返回类型ServerResponse <String>。首先检查用户名和Email是否重复，如果核验通过则设置User的Role属性为普通用户，并将密码MD5加密，然后调用UserMapper接口的insert方法插入用户信息到数据库中

### 校验功能

- 便于用户在注册时实时检查用户名和邮件是否有效

- 服务层UserServiceImpl类中添加checkValid方法，参数为String型的str和type，如果type是“username”，则将str作为username在数据库中查找是否重复；如果type是“email”，则将str作为email在数据库中查找是否重复
- 控制器UserController类中添加checkValid方法，uri为/login/check_valid

