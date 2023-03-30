<%@ page import="com.tian.domain.User" %><%--
  Created by IntelliJ IDEA.
  User: zht08
  Date: 2023/3/30
  Time: 13:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    // 获取用户信息。
    User userInfo = (User) session.getAttribute("userInfo");
    String username = userInfo.getUsername();
    int balance = userInfo.getBalance();
    // 获取atm标号。
    Object atmid = request.getSession().getAttribute("atmid");
%>
<h1>用户控制台</h1>
<p>用户名：<%=username%></p>
<p>用户余额：<%=balance%></p>
<p>当前atm标号：<%=atmid%></p>
<h2>功能选项</h2>
<table>
    <tr>
        <th>功能选择：</th>
        <th><a href="javascript:void(0)" id="outputMonetLink">取款</a></th>
        <script>
            let outputMonetLinkElt = document.getElementById("outputMonetLink");
            outputMonetLinkElt.onclick = function () {
                alert("hello");
                let changedMoney = prompt("输入取款金额：");
                alert("取款金额为：" + changedMoney);
                location = "<%=request.getContextPath()%>/function/editBalance?changedBalance="+changedMoney+"&direction=out";
            }
        </script>
        <th><a href="javascript:void(0)" id="inputMoneyLink">存款</a></th>
        <script>
            let inputMoneyLinkElt = document.getElementById("inputMoneyLink");
            inputMoneyLinkElt.onclick = function () {
                alert("hello");
                let changedMoney = prompt("输入存款金额：");
                alert("取款金额为：" + changedMoney);
                location = "<%=request.getContextPath()%>/function/editBalance?changedBalance="+changedMoney+"&direction=in";
            }
        </script>
    </tr>
</table>
</body>
</html>
