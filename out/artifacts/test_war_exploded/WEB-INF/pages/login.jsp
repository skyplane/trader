<%--
  Created by IntelliJ IDEA.
  User: skyplane
  Date: 04.05.17
  Time: 17:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Авторизация</title>

    <link rel="stylesheet" href="../styles/css/style.css">
    <!--[if lt IE 9]><script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]-->

</head>
<body onload='document.loginForm.username.focus();'>


<form class="login" action="<c:url value='/login'></c:url>" name="loginForm" method="post" role="form">
    <h1>Вход</h1>
    <input type="text" name="username" placeholder="Логин..." class="login-input" id="username" autofocus>
    <input type="password" name="password" class="login-input" placeholder="Пароль">
    <input type="submit" value="Войти" class="login-submit">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>

<section class="about">
    <p class="about-links">
        Traderpage ©
    </p>
    <p class="about-author">

    </p>
</section>



</body>
</html>
