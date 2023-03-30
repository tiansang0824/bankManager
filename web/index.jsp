<%--
  Created by IntelliJ IDEA.
  User: zht08
  Date: 2023/3/30
  Time: 11:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Index</title>
</head>
<body>

<h1>用户登录</h1>

<form action="<%=request.getContextPath()%>/user/login" method="post">
    <label> 用户名:
        <input type="text" name="username">
    </label>
    <label> 密码:
        <input type="text" name="password">
    </label>
    <label> 选择ATM机:
        <input type="text" name="atmid">
    </label>
    <input type="submit">
</form>

</body>
</html>
