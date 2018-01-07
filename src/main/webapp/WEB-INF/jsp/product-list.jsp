<%--@elvariable id="products" type="java.util.List<ua.kapitonenko.app.domain.records.Product>"--%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="ua.kapitonenko.app.config.keys.Keys" %>
<%@ taglib prefix="u" uri="/WEB-INF/access.tld" %>
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
                <div class="row">
                    <div class="col-8">
                        <h4 class="card-title"><fmt:message key="${Keys.PRODUCT_LIST}" bundle="${msg}"/></h4>
                    </div>
                        <u:can action="/receipts">
                            <div class="col-2 pl-0 ml-auto mb-4">
                                <a class="btn btn-outline-secondary btn-block" href="/receipts" role="button">
                                    <fmt:message key="${Keys.RECEIPT_LIST}" bundle="${msg}"/></a>

                            </div>
                        </u:can>
                    <u:can action="/create-product">
                        <div class="col-2 pl-0 ml-auto mb-4">
                            <a class="btn btn-primary btn-block" href="/create-product" role="button">
                                <fmt:message key="${Keys.CREATE}" bundle="${msg}"/></a>
                        </div>
                        </u:can>
                </div>
                <div class="card">
                    <%-- TODO show product name in all lang only to merchandiser --%>
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <th scope="col" width="50px"><fmt:message key="${Keys.ID}" bundle="${msg}"/></th>

                                <th scope="col" width="300px"><fmt:message key="${Keys.PRODUCT_NAME}"
                                                                           bundle="${msg}"/></th>

                                <th scope="col" width="160px" class="text-right"><fmt:message
                                        key="${Keys.PRODUCT_QUANTITY}" bundle="${msg}"/></th>

                                <th scope="col" width="120px" class="text-center"><fmt:message
                                        key="${Keys.PRODUCT_UNIT}"
                                        bundle="${msg}"/></th>

                                <th scope="col" width="120px" class="text-right"><fmt:message
                                        key="${Keys.PRODUCT_PRICE}" bundle="${msg}"/></th>

                                <th scope="col" width="140px" class="text-center"><fmt:message key="${Keys.PRODUCT_TAX}"
                                                                                               bundle="${msg}"/></th>

                                <th scope="col" width="140px" class="text-center"><fmt:message key="${Keys.ACTION}"
                                                                                               bundle="${msg}"/></th>
                            </tr>
                            </thead>
                            <tbody>

                            <c:forEach var="product" items="${products}">
                                <tr>
                                    <th scope="row">${product.id}</th>

                                    <td>
                                        <c:forEach items="${product.names}" var="name">
                                            <p>${name.propertyValue} (${name.locale.language})</p>
                                        </c:forEach>
                                    </td>

                                    <td class="text-right"><fmt:formatNumber type="number" groupingUsed="false"
                                                                             maxFractionDigits="3"
                                                                             minFractionDigits="3"
                                                                             value="${product.quantity}"/></td>

                                    <td class="text-center"><fmt:message key="${product.unit.bundleKey}"
                                                                         bundle="${settings}"/></td>

                                    <td class="text-right"><fmt:formatNumber type="number" groupingUsed="false"
                                                                             maxFractionDigits="2"
                                                                             minFractionDigits="2"
                                                                             value="${product.price}"/></td>

                                    <td class="text-center"><fmt:message key="${product.taxCategory.bundleKey}"
                                                                         bundle="${settings}"/></td>
                                    <td class="text-center">
                                        <form action="/delete-product" method="POST" role="form">
                                            <input type="hidden" name="id" value="${product.id}">
                                            <u:can action="/delete-product">
                                                <button type="submit" class="btn btn-link">
                                                    <fmt:message key="${Keys.DELETE}" bundle="${msg}"/></button>
                                            </u:can>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                <%@ include file="includes/pager.jsp" %>
            </div>
        </div>
    </div>
</main>
<%@ include file="includes/footer.jsp" %>
</body>
</html>