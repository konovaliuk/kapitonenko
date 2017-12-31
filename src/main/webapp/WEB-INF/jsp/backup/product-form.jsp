<%--@elvariable id="user" type="ua.kapitonenko.domain.User"--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<%@ include file="layout/header.jsp" %>

<body class="inner-page">
<%@ include file="layout/alert.jsp" %>
<div class="container">
    <div class="col-xs-12 ml-auto mr-auto">
        <%@ include file="layout/logout.jsp" %>
        <div class="card">
            <ul class="nav nav-tabs">
                <li class="nav-item">
                    <a class="nav-link" href="#home">Products</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active" href="#profile">Add Product</a>
                </li>
            </ul>
            <div class="card-body">
                <form class="form" method="POST" action="${view.action}" autocomplete="off">
                    <div class="form-group row">
                        <label class="col-sm-2 col-form-label">Email</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control"
                                   name="${Messages.USERNAME}"
                                   placeholder="<fmt:message key="${Messages.USERNAME}" bundle="${msg}"/>..."
                                   value="${view.model.username}"
                            />
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-2 col-form-label">Email</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control"
                                   name="${Messages.USERNAME}"
                                   placeholder="<fmt:message key="${Messages.USERNAME}" bundle="${msg}"/>..."
                                   value="${view.model.username}"
                            />
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-2 col-form-label">Email</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control"
                                   name="${Messages.USERNAME}"
                                   placeholder="<fmt:message key="${Messages.USERNAME}" bundle="${msg}"/>..."
                                   value="${view.model.username}"
                            />
                        </div>
                    </div>
                    <div class="form-group row">
                        <label class="col-sm-2 col-form-label">Email</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control"
                                   name="${Messages.USERNAME}"
                                   placeholder="<fmt:message key="${Messages.USERNAME}" bundle="${msg}"/>..."
                                   value="${view.model.username}"
                            />
                        </div>
                    </div>
                    <c:set var="submit" value="${view.action eq signup ? Messages.SIGN_UP : Messages.LOGIN}"/>
                    <button type="submit" class="btn btn-primary btn-lg"><fmt:message
                            key="${submit}" bundle="${msg}"/></button>

                </form>
            </div>
        </div>
    </div>
    <%@ include file="layout/language.jsp" %>
</div>
</body>
<%@ include file="layout/footer.jsp" %>
</html>