<%--@elvariable id="user" type="ua.kapitonenko.domain.User"--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<%@ include file="layout/header.jsp" %>

<body class="inner-page">
<%@ include file="layout/alert.jsp" %>

<div class="page-header">
    <div class="container">
        <div class="col-xs-12 ml-auto mr-auto">
            <div class="header header-primary">
                <div class="logo pull-left">
                    <span>Cash</span><span class="green">Register</span>
                </div>
                <div class="button-group pull-right">
                    <form class="form" method="POST" action="logout" autocomplete="off">
                        <button type="submit" class="btn btn-link dark-bg">
                            <fmt:message key="${Messages.LOGOUT}" bundle="${msg}"/> (${user.username})
                        </button>
                    </form>
                </div>
            </div>
            <div class="card">
                <%--                <ul class="nav nav-tabs">
                                    <li class="nav-item">
                                        <a class="nav-link" href="#home">
                                            <i class="now-ui-icons objects_umbrella-13"></i> Home
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link active" href="#profile">
                                            <i class="now-ui-icons shopping_cart-simple"></i> Profile
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="#messages">
                                            <i class="now-ui-icons shopping_shop"></i> Messages
                                        </a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" href="#settingsMap">
                                            <i class="now-ui-icons ui-2_settings-90"></i> Settings
                                        </a>
                                    </li>
                                </ul>--%>
                <div class="card-body">
                </div>
            </div>
        </div>
        <%@ include file="layout/language.jsp" %>
    </div>
</div>
</body>
<%@ include file="layout/footer.jsp" %>
</html>