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

    <script>
        // 这里存放一些备用的函数，比如检查用户名和密码的合法性。
        // 用户名的正则表达式。
        // 这个正则表达式要求用户名是16，17或者19位，并且全部数字，非0开头。
        const username_regex = /^(?!0)\d{16}|\d{17}|\d{19}$/;
        let isUsername = function (username) {
            // 判断是否符合用户名的函数
            return username_regex.test(username);
        }
        // 密码的正则表达式
        // 这个正则表达式要求8-20位，不能是同一个字符，不能按照从小到大或者从大到小排列。
        const password_regex = /^(?!.*(\d)\1{7})[a-zA-Z0-9_-]{6,18}(?<!^(.)\2+$)(?!012345678|123456789|234567890|345678901|456789012|567890123|678901234|789012345|890123456|901234567|01234567|12345678|23456789|34567890|45678901|56789012|67890123|78901234|89012345|90123456|0123456789|0987654321$)/;
        let isPassword = function (password) {
            // 判断是否符合用户名的函数
            return username_regex.test(password);
        }
    </script>

</head>
<body>

<h1>用户登录</h1>

<form action="<%=request.getContextPath()%>/user/login" method="post" id="formElm">
    <label> 用户名:
        <input type="text" name="username" id="usernameElm" value="请输入用户名" onclick="document.getElementById('usernameElm').value=''">
    </label>
    <label> 密码:
        <input type="text" name="password" id="passwordElm" value="请输入密码，要求8-20位，全部数字，不能有递增或者递减的密码。" onclick="document.getElementById('passwordElm').value=''">
    </label>
    <label> 选择ATM机:
        <input type="text" name="atmid">
    </label>
    <input type="button" id="submitBtn" value="登录">
    <script>
        // 获取提交按钮对象。
        let submitObj = document.getElementById("submitBtn");
        // 单机提交按钮出发功能。
        submitObj.onclick = function () {
            // 点击提交后的功能。
            // 获取表单对象。
            let formEltObj = document.getElementById("formElm");
            // 获取用户名和密码对象。
            let usernameElm = document.getElementById("usernameElm");
            let passwordElm = document.getElementById("usernameElm");
            // 创建一个flag用于判断表单正确性。
            let flag = false;
            // 开始判断表单。
            let isUsername = false;
            let isPassword = false;

            if (!isUsername(usernameElm.getValue)()){
                // 用户名不对
                alert("取款失败，非银行卡，退卡。");
            }

            if (isUsername(usernameElm.getValue()) && isPassword(passwordElm.getValue())) {
                // 符合要求，提交表单。
                formEltObj.submit();
            } else {
                // 不符合要求，拒绝提交表单，并且给出提示。
                alert("用户名或密码不符合要求，请重新填写。");
            }
        }
    </script>
</form>

</body>
</html>
