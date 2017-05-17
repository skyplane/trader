<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    String redirectURL = "/login";
    response.sendRedirect(redirectURL);
%>
</body>
</html>
