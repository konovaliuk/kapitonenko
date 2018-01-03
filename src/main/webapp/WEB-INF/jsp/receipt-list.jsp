<jsp:useBean id="newProduct" scope="request" class="ua.kapitonenko.domain.entities.Product"/>
<%--@elvariable id="rcalculator" type="ua.kapitonenko.domain.ReceiptCalculator"--%>
<%--@elvariable id="product" type="ua.kapitonenko.domain.entities.Product"--%>
<%--@elvariable id="receipts" type="java.util.List<ua.kapitonenko.domain.ReceiptCalculator>"--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="ua.kapitonenko.config.keys.Keys" %>
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
                        <h4 class="card-title mb-3"><fmt:message key="${Keys.RECEIPT_LIST}" bundle="${msg}"/></h4>
                    </div>
                    <div class="col-2 pl-0">
                        <a class="btn btn-outline-secondary btn-block" href="/products" role="button">
                            <fmt:message key="${Keys.PRODUCT_LIST}" bundle="${msg}"/></a>
                    </div>
                    <div class="col-2 pl-0 mb-4">
                        <a class="btn btn-primary btn-block" href="/create-receipt" role="button">
                            <fmt:message key="${Keys.CREATE}" bundle="${msg}"/></a>
                    </div>
                </div>
                <div class="card">
                    <div class="table-responsive">
                        <form class="form" method="POST" autocomplete="off" id="receipt-list-form">
                            <input type="hidden" name="id" id="rId">
                            <input type="hidden" name="action" id="actionId">

                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th scope="col" width="50px"><fmt:message key="${Keys.ID}" bundle="${msg}"/></th>

                                    <th scope="col" width="100px"><fmt:message
                                            key="${Keys.RECEIPT_TYPE}" bundle="${msg}"/></th>

                                    <th scope="col" width="50px"><fmt:message
                                            key="${Keys.CASHBOX}" bundle="${msg}"/></th>

                                    <th scope="col" width="100px"><fmt:message
                                            key="${Keys.PAYMENT}" bundle="${msg}"/></th>

                                    <th scope="col" width="50px"><fmt:message
                                            key="${Keys.NUMBER_ARTICLES}" bundle="${msg}"/></th>


                                    <th scope="col" width="100px" class="text-right"><fmt:message
                                            key="${Keys.TAX_AMOUNT}" bundle="${msg}"/></th>

                                    <th scope="col" width="100px" class="text-right"><fmt:message
                                            key="${Keys.RECEIPT_TOTAL}" bundle="${msg}"/></th>

                                    <th scope="col" width="100px"><fmt:message
                                            key="${Keys.CREATED_AT}" bundle="${msg}"/></th>

                                    <th scope="col" width="50px"><fmt:message
                                            key="${Keys.STATUS}" bundle="${msg}"/></th>

                                    <th scope="col" width="80px" class="text-center d-print-none"><fmt:message
                                            key="${Keys.ACTION}"
                                            bundle="${msg}"/></th>
                                </tr>
                                </thead>
                                <tbody>

                                <c:forEach var="calculator" items="${receipts}">
                                    <tr>
                                        <th scope="row">${calculator.receipt.id}</th>
                                        <td><fmt:message key="${calculator.receipt.receiptType.bundleKey}"
                                                         bundle="${settings}"/></td>
                                        <td class="text-centert">${calculator.receipt.cashboxId}</td>
                                        <td><fmt:message key="${calculator.receipt.paymentType.bundleKey}"
                                                         bundle="${settings}"/></td>
                                        <td class="text-centert">${calculator.products.size()}</td>
                                        <td class="text-right">${calculator.taxAmount}</td>
                                        <td class="text-right">${calculator.totalCost}</td>
                                        <td><fmt:formatDate type="both" value="${calculator.receipt.createdAt}"/></td>
                                        <td>
                                            <c:if test="${calculator.receipt.cancelled}">
                                                <fmt:message key="${Keys.CANCELLED}" bundle="${msg}"/>
                                            </c:if>
                                        </td>

                                        <td class="text-center">
                                            <c:if test="${!calculator.receipt.cancelled}">
                                                <c:if test="${calculator.returnVisible}">
                                                    <button type="button" class="btn btn-link"
                                                            onclick="var form = $('#receipt-list-form');
                                                                    $('#rId').val(${calculator.receipt.id});
                                                                    form.attr('action', '/return-receipt');
                                                                    form.submit();">
                                                        <fmt:message key="${Keys.RETURN}" bundle="${msg}"/>
                                                    </button>

                                                </c:if>

                                                <button type="button" class="btn btn-link"
                                                        onclick="var form = $('#receipt-list-form');
                                                                $('#rId').val(${calculator.receipt.id});
                                                                form.attr('action', '/cancel-receipt');
                                                                form.submit();">
                                                    <fmt:message key="${Keys.CANCEL}" bundle="${msg}"/>
                                                </button>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </form>
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
