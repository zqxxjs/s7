<html>
<head>
    <title>UploadPage</title>
</head>

<body>
<h2>文件上传</h2>

<h3>Languages</h3>
<ul>
    <@s.actionerror />
    <!-- 文件上传表单 -->
    <@s.form action="/upload/files" method="post" enctype="multipart/form-data">
        <!-- 单文件上传 -->
        <@s.file name="uploadFiles" multiple="true" label="选择文件" />
        <@s.submit value="上传" />
    </@s.form>
</ul>

</body>
</html>
