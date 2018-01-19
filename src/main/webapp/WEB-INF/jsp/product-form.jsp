<%--@elvariable id="product" type="ua.kapitonenko.app.domain.Product"--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="ua.kapitonenko.app.config.keys.Keys" %>
<!DOCTYPE html>
<html>

<%@ include file="includes/inner-header.jsp" %>
<body>
<%@ include file="includes/alert.jsp" %>
<%@ include file="includes/logout.jsp" %>

<main role="main">
    <div class="container">
        <div class="card">
            <div class="card-body">
                <h5 class="card-title mb-4"><fmt:message key="${Keys.PRODUCT_NEW}" bundle="${msg}"/></h5>
                <form class="form" method="POST" action="${action}" autocomplete="off">
                    <div class="form-group">
                        <label><fmt:message key="${Keys.PRODUCT_NAME}" bundle="${msg}"/></label>
                        <div class="form-row">
                            <c:forEach var="name" items="${product.names}">
                                <div class="col">
                                    <input name="${Keys.PRODUCT_NAME}" value="${name.propertyValue}"
                                           type="text" class="form-control" placeholder="${name.locale.language}"
                                           title="${name.locale.language}">
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label><fmt:message key="${Keys.PRODUCT_QUANTITY}" bundle="${msg}"/></label>
                            <input type="text" class="form-control"
                                   name="${Keys.PRODUCT_QUANTITY}"
                                   value="<fmt:formatNumber type="number" groupingUsed="false" value="${product.quantity}" />">
                        </div>
                        <div class="form-group col-md-6">
                            <label><fmt:message key="${Keys.PRODUCT_UNIT}" bundle="${msg}"/></label>
                            <select class="form-control" name="${Keys.PRODUCT_UNIT}">
                                <option></option>
                                <c:forEach items="${unitList}" var="option">
                                    <fmt:setBundle basename="${option.bundleName}" var="custom"/>
                                    <option value="${option.id}" ${product.unitId.equals(option.id) ? "selected" : ""}>
                                        <fmt:message key="${option.bundleKey}" bundle="${custom}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label><fmt:message key="${Keys.PRODUCT_PRICE}" bundle="${msg}"/></label>
                            <input type="text" class="form-control"
                                   name="${Keys.PRODUCT_PRICE}"
                                   value="<fmt:formatNumber type="number" groupingUsed="false" value="${product.price}" />">
                        </div>
                        <div class="form-group col-md-6">
                            <label><fmt:message key="${Keys.PRODUCT_TAX}" bundle="${msg}"/></label>
                            <select class="form-control" name="${Keys.PRODUCT_TAX}">
                                <option></option>
                                <c:forEach items="${taxList}" var="option">
                                    <fmt:setBundle basename="${option.bundleName}" var="custom"/>
                                    <option value="${option.id}" ${product.taxCategoryId.equals(option.id) ? "selected" : ""}>
                                        <fmt:message key="${option.bundleKey}" bundle="${custom}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-row mt-3">
                        <div class="col-xs-6 col-md-2">
                            <button type="submit" class="btn btn-primary btn-block">
                                <fmt:message key="${Keys.SAVE}" bundle="${msg}"/></button>
                        </div>
                        <div class="col-xs-6 col-md-2">
                            <a href="/products" class="btn btn-secondary btn-block">
                                <fmt:message key="${Keys.CLOSE}" bundle="${msg}"/></a>
                        </div>
                    </div>

                </form>
            </div>
        </div>
    </div>
</main>
<%@ include file="includes/footer.jsp" %>
</body>
</html>
