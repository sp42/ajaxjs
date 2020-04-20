<%@page pageEncoding="UTF-8" %>
<%@taglib prefix="html" tagdir="/WEB-INF/tags" %>
<html:html title="新增标签">
    <form method="post" action="add.do">
        网址 http:// <input name="url" value="\${param.url}"><br>
        网页名称：<input name="title" value="\${param.title}"><br>
        分　　类：<input type="text" name="category" value="\${param.category}"><br>
         <input value="送出" type="submit"><br>
    </form>
</html:html>