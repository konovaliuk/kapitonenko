<%--@elvariable id="user" type="ua.kapitonenko.app.domain.records.User"--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="ua.kapitonenko.app.config.keys.Keys" %>
<!DOCTYPE html>
<html>

<%@ include file="includes/cover-header.jsp" %>
<body class="login-page">
<%@ include file="includes/alert.jsp" %>

<div class="page-header">
    <div class="container">
        <div class="col-md-4 content-center">
            <div class="card card-login card-plain">

                <form class="form" method="POST" action="${action}" autocomplete="off">

                    <div class="header header-primary text-center">
                        <div class="logo">
                            <span>Cash</span><span class="green">Register</span>
                        </div>
                    </div>
                    <div class="content">

                        <div class="input-group form-group-no-border input-lg">
                            <input type="text" class="form-control"
                                   name="${Keys.USERNAME}"
                                   placeholder="<fmt:message key="${Keys.USERNAME}" bundle="${msg}"/> ..."
                                   value="${user.username}"/>
                        </div>

                        <div class="input-group form-group-no-border input-lg">
                            <input type="password" class="form-control"
                                   name="${Keys.PASSWORD}"
                                   placeholder="<fmt:message key="${Keys.PASSWORD}" bundle="${msg}"/> ..."/>
                        </div>

                        <c:set var="signup" value="/signup"/>
                        <c:if test="${action eq signup}">

                            <div class="input-group form-group-no-border input-lg">
                                <input type="password" class="form-control"
                                       name="${Keys.CONFIRM_PASS}"
                                       placeholder="<fmt:message key="${Keys.CONFIRM_PASS}" bundle="${msg}"/> ..."/>
                            </div>

                            <%--@elvariable id="roleList" type="java.util.List<ua.kapitonenko.app.domain.records.UserRole>"--%>
                            <c:if test="${not empty roleList}">
                                <div class="form-group option-list">
                                    <c:forEach items="${roleList}" var="option">
                                        <div class="form-check">
                                            <label class="form-check-label">
                                                <input class="form-check-input" type="radio"
                                                       name="${Keys.ROLE}"
                                                       value="${option.id}" ${user.userRoleId.equals(option.id) ? "checked" : ""}/>
                                                <fmt:message key="${option.bundleKey}" bundle="${settings}"/>
                                            </label>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:if>

                        </c:if>

                        <c:if test="${action ne signup}">
                            <div class="input-group form-group-no-border input-lg">
                                <input type="text" class="form-control"
                                       name="${Keys.CASHBOX}"
                                       placeholder="<fmt:message key="${Keys.CASHBOX}" bundle="${msg}"/> ..."
                                       value="${cashbox}"/>
                            </div>
                        </c:if>
                    </div>

                    <c:set var="submit" value="${action eq signup ? Keys.SIGN_UP : Keys.LOGIN}"/>

                    <button type="submit" class="btn btn-primary btn-round btn-lg btn-block">
                        <fmt:message key="${submit}" bundle="${msg}"/>
                    </button>

                    <c:set var="linkText" value="${action eq signup ? Keys.LOGIN : Keys.SIGN_UP}"/>
                    <a href="${link}" class="link"><fmt:message key="${linkText}" bundle="${msg}"/></a>

                </form>
            </div>
        </div>
    </div>
    <%@ include file="includes/footer.jsp" %>
</div>
<script src="<c:url value="/assets/js/now-ui-kit.js?v=1.1.0"/>" type="text/javascript"></script>
</body>
</html>