<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>

<%@ include file="includes/inner-header.jsp" %>
<body>
<%@ include file="includes/alert.jsp" %>
<%@ include file="includes/logout.jsp" %>

<main role="main">
    <div class="container">
        <div class="card">

            <%@ include file="includes/navigation.jsp" %>

            <div class="card-body">
                <h5 class="card-title mb-3"><fmt:message key="${Keys.PRODUCT_LIST}" bundle="${msg}"/></h5>
                <div class="card">
                    <div class="table-responsive">
                        <table class="table table-striped">
                            <thead>
                            <tr>
                                <th scope="col" width="50px"><fmt:message key="${Keys.ID}" bundle="${msg}"/></th>
                                <th scope="col" width="300px"><fmt:message key="${Keys.PRODUCT_NAME}"
                                                                           bundle="${msg}"/></th>
                                <th scope="col" width="160px"><fmt:message key="${Keys.PRODUCT_UNIT}"
                                                                           bundle="${msg}"/></th>
                                <th scope="col" width="100px" class="text-right"><fmt:message
                                        key="${Keys.PRODUCT_QUANTITY}" bundle="${msg}"/></th>
                                <th scope="col" width="120px" class="text-right"><fmt:message
                                        key="${Keys.PRODUCT_PRICE}" bundle="${msg}"/></th>
                                <th scope="col" width="140px" class="text-center"><fmt:message key="${Keys.PRODUCT_TAX}"
                                                                                               bundle="${msg}"/></th>
                                <th scope="col" width="180px" class="text-center"><fmt:message key="${Keys.ACTION}"
                                                                                               bundle="${msg}"/></th>
                            </tr>
                            </thead>
                            <tbody>
                            <%--@elvariable id="products" type="java.util.List<ua.kapitonenko.domain.entities.Product>"--%>
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
                                    <td class="text-center"><fmt:message key="${product.taxCategory.bundleKey}"
                                                                         bundle="${settings}"/></td>
                                    <td class="text-center">
                                        <form action="/delete-product" method="POST" role="form">
                                            <a href="/update-product?id=${product.id}" class="btn btn-link ">
                                                <fmt:message key="${Keys.UPDATE}" bundle="${msg}"/></a>

                                            <input type="hidden" name="id" value="${product.id}">
                                            <button type="submit" class="btn btn-link"><fmt:message key="${Keys.DELETE}"
                                                                                                    bundle="${msg}"/></button>
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
    </div>
</main>
<%@ include file="includes/footer.jsp" %>
</body>
</html>
