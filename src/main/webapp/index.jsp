<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<h2>Hello World!</h2>

<h2>Spring mvc 上传文件</h2>
<form name="form1" action="/manage/product/upload" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="Spring mvc 上传文件"/>
</form>


<h2>富文本图片 上传文件</h2>
<form name="form2" action="/manage/product/richtext_img_upload" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="富文本图片 上传文件"/>
</form>

</body>
</html>
