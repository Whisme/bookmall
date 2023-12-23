<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/"; %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户列表</title>
    <base href="<%=basePath%>">
    <script type="text/javascript" src="js/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="js/bootstrap.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/bs.css"/>
</head>
<script type="text/javascript">

    function editUser(userId) {
        location.href = "<%=basePath%>admin/user/echo/"+userId;
    }

    function deleteUser(userId) {
        if(confirm("真的要删除？")){
            location.href = "<%=basePath%>admin/user/deletion/"+userId;
        }
    }

</script>
<body>
<div class="container" style="border: 1px solid #CCCCCC;height: 1000px;">

    <div style="height:100px;border-bottom: 1px solid #CCCCCC;padding-top:30px;margin-bottom: 10px">
        <h2 class="h2">用户列表</h2>
    </div>

    <table class="table table-hover">
        <caption>用户列表</caption>
        <thead>
        <tr>
            <th>序号</th>
            <th>用户名</th>
            <th>昵称</th>
            <th>性别</th>
            <th>手机号码</th>
            <th>邮箱地址</th>
            <th>居住城市</th>
            <th>职业</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${users.list}" var="user" varStatus="vs">
            <tr>
                <td>${vs.count}</td>
                <td>${user.username}</td>
                <td>${user.nickname}</td>
                <td>${empty user.gender ? "未设置":(user.gender == 1?"男":"女")}</td>
                <td>${user.phone}</td>
                <td>${user.email}</td>
                <td>${user.location}</td>
                <td>${user.identity}</td>
                <td>${user.active == 1?"已激活":"未激活"}</td>
                <td>
                    <button type="button" class="btn btn-xs btn-info" onclick="editUser(${user.userId})">
                        <span class="glyphicon glyphicon-pencil"></span> 编辑
                    </button>
                    <button type="button" class="btn btn-xs btn-danger" onclick="deleteUser(${user.userId})">
                        <span class='glyphicon glyphicon-remove'></span> 删除
                    </button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <ul class="pagination pagination-lg">

        <c:if test="${users.isFirstPage}">
            <li class="disabled"><a href="javascript:void(0);">&laquo;</a></li>
        </c:if>

        <c:if test="${!users.isFirstPage}">
            <li>
                <a href="admin/user/list?page=${users.prePage}">&laquo;</a>
            </li>
        </c:if>
        <c:forEach
                begin="${users.pageNum < 6 ? 1 :users.pageNum-5}"
                end="${users.pages<6?users.pages:(users.pageNum < 6 ? 6 :users.pageNum) }"
                var="current">
            <li
                    class="${(current == users.pageNum) ? 'active':''}">
                <a href="admin/user/list?page=${current}">
                        ${current}
                </a>
            </li>
        </c:forEach>
        <c:if test="${users.isLastPage}">
            <li class="disabled"><a href="javascript:void(0);">&raquo;</a></li>
        </c:if>
        <c:if test="${!users.isLastPage}">
            <li><a href="admin/user/list?page=${users.nextPage}">&raquo;</a>
            </li>
        </c:if>
        <li>
            <a href="admin/user/list?page=${users.pages}">末页</a>
        </li>
        <li><a href="javascript:void(0);">共${users.pages}页</a></li>
        <li><a href="javascript:void(0);">共${users.total}用户</a></li>
    </ul>

</div>

</body>
</html>
