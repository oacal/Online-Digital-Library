<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 13-8-3
  Time: 下午4:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <title>${book.editType}</title>
    <jsp:include page="../comomResource.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css" href="/js/kindeditor-4.1.7/themes/default/default.css"/>
    <script type="text/javascript" src="/js/kindeditor-4.1.7/kindeditor-min.js"></script>
    <script type="text/javascript" src="/js/kindeditor-4.1.7/lang/zh_CN.js"></script>
    <script type="text/javascript" src="/js/pages/admin/booEdit.js"></script>
</head>
<body>
<div class="span8">
    <fieldset>
        <legend>${book.editType}</legend>
        <form method="post" class="form-horizontal" action="/admin/book/save.do">

            <form:hidden path="book.id"></form:hidden>
            <div class="control-group">
                <label class="control-label" for="name">书名：</label>

                <div class="controls">
                    <form:input path="book.name" cssClass="input-xlarge" placeholder="书名：倚天屠龙记"></form:input>
                    <form:errors cssClass="error text-error" path="book.name"></form:errors>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="author">作者：</label>

                <div class="controls">
                    <form:input path="book.author" cssClass="input-xlarge" placeholder="作者：金庸"></form:input>
                     <form:errors cssClass="error text-error" path="book.author"></form:errors>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="bookNumber">书号：</label>

                <div class="controls">
                    <form:input path="book.bookNumber" cssClass="input-xlarge" placeholder="书号：ISO00001"></form:input>

                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="price">售价：</label>

                <div class="controls">
                    <form:input path="book.price" cssClass="input-xlarge" placeholder="售价：0"></form:input>
                    <form:errors cssClass="error text-error" path="book.price"></form:errors>

                </div>
            </div>
            <div class="control-group">

                <label class="control-label" for="publisher">出版社：</label>

                <div class="controls">
                    <form:input path="book.publisher" cssClass="input-xlarge" placeholder="出版社：新华社"></form:input>

                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="publishDate">出版日期：</label>

                <div class="controls">
                    <form:input path="book.publishDate" cssClass="input-xlarge form-date" placeholder="出版日期：2000-1-1"
                                readonly="readonly"></form:input>
                    <form:errors cssClass="error text-error" path="book.publishDate"></form:errors>


                </div>
            </div>
            <div class="control-group ">
                <label class="control-label" for="category">书籍分类：</label>

                <div class="controls">
                    <form:select path="book.category" items="${categories}" itemValue="name" itemLabel="name"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="keywords">关键字：</label>

                <div class="controls">
                    <form:input path="book.keywords" cssClass="input-xlarge" placeholder="请使用空格分开：武侠 经典"></form:input>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label" for="bookCoverImgPath">书籍封面：</label>

                <div class="controls">
                    <form:input path="book.bookCoverImgPath" cssClass="input-xlarge" placeholder="请上传书籍封面..."></form:input>
                    <input id="coverUpload" name="coverUpload" type="button" value="选择封面图片"/>
                </div>
            </div>
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">保存</button>
                <a class="btn" href="/admin/book/list.do">取消</a>
            </div>
        </form>
    </fieldset>

</div>
</body>
</html>