<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html>
<html>
<body>
<div >
    <span style="color:#00BFFF;margin-left: 20px;">欢迎您，${sessionScope.loginUser.username}，登录后端管理系统！</span>
</div>
</body>
</html>
