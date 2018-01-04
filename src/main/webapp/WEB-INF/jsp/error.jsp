<%@ page language="java" contentType="text/html; charset=UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<%@ include file="includes/cover-header.jsp" %>
<body class="login-page">
<%@ include file="includes/alert.jsp" %>

<div class="page-header">
    <div class="container">
        <div class="col-md-4 content-center">
            <div class="card card-login card-plain">
                <h1>${pageContext.errorData.statusCode}</h1>
                Something bad happened :(
                <%-- TODO replace placeholder --%>
            </div>
        </div>
    </div>
</div>
</body>
</html>