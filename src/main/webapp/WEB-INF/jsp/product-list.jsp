<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="ua.kapitonenko.controller.keys.Keys" %>
<!DOCTYPE html>
<html>
<%@ include file="includes/inner-header.jsp" %>
<body>
<%--@elvariable id="alert" type="ua.kapitonenko.controller.helpers.AlertContainer"--%>
<c:if test="${not empty alert && alert.hasMessages}">
    <div class="alert alert-${alert.messageType} clearfix" role="alert">
        <div class="container">
            <div class="message-body">
                <ul class="list-unstyled">
                    <c:forEach var="message" items="${alert.messageList}">
                        <li>${message}</li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
				<span aria-hidden="true">
				<i class="now-ui-icons ui-1_simple-remove"></i>
			</span>
        </button>
    </div>
</c:if>
<%@ include file="includes/logout.jsp" %>
<main role="main">
    <div class="container">
        <div class="card">
            <div class="card-header">
                <ul class="nav nav-tabs card-header-tabs pull-left">
                    <c:set var="list" value="/products"/>
                    <c:set var="newProd" value="/create-product"/>
                    <c:set var="activeList" value="${action eq list ? 'active' : ''}"/>
                    <c:set var="activeForm" value="${action eq newProd ? 'active' : ''}"/>
                    <li class="nav-item">
                        <a class="nav-link ${activeList}" href="${list}"><fmt:message key="${Keys.PRODUCT_LIST}"
                                                                         bundle="${msg}"/></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${activeForm}" href="${newProd}"><fmt:message key="${Keys.PRODUCT_NEW}" bundle="${msg}"/></a>
                    </li>
                </ul>
            </div>
            <div class="card-body">
                <h5 class="card-title"><fmt:message key="${Keys.PRODUCT_LIST}" bundle="${msg}"/></h5>
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                        <tr>
                            <th scope="col" width="50px"><fmt:message key="${Keys.ID}" bundle="${msg}"/></th>
                            <th scope="col" width="300px"><fmt:message key="${Keys.PRODUCT_NAME}" bundle="${msg}"/></th>
                            <th scope="col" width="160px"><fmt:message key="${Keys.PRODUCT_UNIT}" bundle="${msg}"/></th>
                            <th scope="col" width="100px" class="text-right"><fmt:message key="${Keys.PRODUCT_QUANTITY}" bundle="${msg}"/></th>
                            <th scope="col" width="120px" class="text-right"><fmt:message key="${Keys.PRODUCT_PRICE}" bundle="${msg}"/></th>
                            <th scope="col" width="140px" class="text-center"><fmt:message key="${Keys.PRODUCT_TAX}" bundle="${msg}"/></th>
                            <th scope="col" width="180px" class="text-center" ><fmt:message key="${Keys.ACTION}" bundle="${msg}"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <%--@elvariable id="products" type="java.util.List<ua.kapitonenko.domain.Product>"--%>
                        <c:forEach var="product" items="${products}">
                            <tr>
                                <th scope="row">${product.id}</th>
                                <td>
                                        <c:forEach items="${product.names}" var="name">
                                            <p>${name.propertyValue} (${name.locale.language})</p>
                                        </c:forEach>
                                </td>
                                <td><fmt:message key="${product.unit.bundleKey}" bundle="${settings}"/></td>
                                <td class="text-right">${product.quantity}</td>
                                <td class="text-right">${product.price}</td>
                                <td class="text-center"><fmt:message key="${product.taxCategory.bundleKey}" bundle="${settings}"/></td>
                                <td class="text-center">
                                    <form action="/delete-product" method="POST" role="form" >
                                        <a href="/update-product?id=${product.id}" class="btn btn-link ">
                                            <fmt:message key="${Keys.UPDATE}" bundle="${msg}"/></a>

                                        <input type="hidden" name="id" value="${product.id}">
                                        <button type="submit" class="btn btn-link"><fmt:message key="${Keys.DELETE}" bundle="${msg}"/></button>
                                    </form>

                                </td>
                            </tr>
                        </c:forEach>

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</main>
<%@ include file="includes/language.jsp" %>
<%@ include file="includes/footer.jsp" %>
</body>
</html>
