<%--@elvariable id="product" type="ua.kapitonenko.domain.Product"--%>
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
                <h5 class="card-title"><fmt:message key="${Keys.PRODUCT_NEW}" bundle="${msg}"/></h5>
                <form class="form" method="POST" action="${action}" autocomplete="off">
                    <div class="form-group">
                        <label><fmt:message key="${Keys.PRODUCT_NAME}" bundle="${msg}"/></label>
                        <div class="form-row">
                            <c:forEach var="name" items="${product.names}">
                                <div class="col">
                                    <input name="${Keys.PRODUCT_NAME}" value="${name.propertyValue}"
                                           type="text" class="form-control" placeholder="${name.locale.language}" title="${name.locale.language}">
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label><fmt:message key="${Keys.PRODUCT_QUANTITY}" bundle="${msg}"/></label>
                            <input type="text" class="form-control"
                                   name="${Keys.PRODUCT_QUANTITY}" value="<fmt:formatNumber type="number" groupingUsed="false" value="${product.quantity}" />">
                        </div>
                        <div class="form-group col-md-6">
                            <label><fmt:message key="${Keys.PRODUCT_UNIT}" bundle="${msg}"/></label>
                            <select class="form-control" name="${Keys.PRODUCT_UNIT}">
                                <option></option>
                                <c:forEach items="${unitList}" var="option">
                                    <option value="${option.id}" ${product.unitId.equals(option.id) ? "selected" : ""}>
                                        <fmt:message key="${option.bundleKey}" bundle="${settings}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label><fmt:message key="${Keys.PRODUCT_PRICE}" bundle="${msg}"/></label>
                            <input type="text" class="form-control"
                                   name="${Keys.PRODUCT_PRICE}" value="<fmt:formatNumber type="number" groupingUsed="false" value="${product.price}" />">
                        </div>
                        <div class="form-group col-md-6">
                            <label><fmt:message key="${Keys.PRODUCT_TAX}" bundle="${msg}"/></label>
                            <select class="form-control" name="${Keys.PRODUCT_TAX}">
                                <option></option>
                                <c:forEach items="${taxList}" var="option">
                                    <option value="${option.id}" ${product.taxCategoryId.equals(option.id) ? "selected" : ""}>
                                        <fmt:message key="${option.bundleKey}" bundle="${settings}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="col-xs-6 col-md-2">
                            <button type="submit" class="btn btn-primary btn-block"><fmt:message key="${Keys.SAVE}" bundle="${msg}"/></button>
                        </div>
                        <div class="col-xs-6 col-md-2">
                            <button type="submit" class="btn btn-default btn-block"><fmt:message key="${Keys.CANCEL}" bundle="${msg}"/></button>
                        </div>
                    </div>

                </form>
            </div>
        </div>
    </div>
</main>
<%@ include file="includes/language.jsp" %>
<%@ include file="includes/footer.jsp" %>
</body>
</html>
