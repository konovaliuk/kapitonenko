<%--@elvariable id="report" type="ua.kapitonenko.app.domain.Report"--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="ua.kapitonenko.app.config.keys.Keys" %>
<!DOCTYPE html>
<html>

<%@ include file="includes/inner-header.jsp" %>
<body>
<%@ include file="includes/alert.jsp" %>
<%@ include file="includes/logout.jsp" %>

<main role="main">
    <div class="container report">
        <div class="card pb-2">
            <div class="card-body pb-3">
                <h5 class="card-title mb-4">
                    <fmt:message key="${report.type.label}" bundle="${msg}"/>&nbsp;
                    <c:if test="${not empty report.record}">
                        <fmt:message key="${Keys.ID}" bundle="${msg}"/>${report.record.id}
                    </c:if>
                </h5>
                <div class="row mb-4 cashbox">
                    <div class="col-2">
                        <label><fmt:message key="${Keys.CASHBOX}" bundle="${msg}"/>:</label>
                        <b>${report.cashbox.id}</b>
                    </div>
                    <div class="col-3">
                        <label><fmt:message key="${Keys.CASHBOX_FN}" bundle="${msg}"/>:</label>
                        <b>${report.cashbox.fnNumber}</b>
                    </div>
                    <div class="col-4">
                        <label><fmt:message key="${Keys.CASHBOX_ZN}" bundle="${msg}"/>:</label>
                        <b>${report.cashbox.znNumber}</b>
                    </div>
                    <div class="col-3 text-right">
                        <fmt:formatDate type="both" value="${report.createdAt}"/>
                    </div>
                </div>

                <div class="card">
                    <table class="table  table-sm table-striped">
                        <thead>
                        <tr>
                            <th scope="col" width="40%">&nbsp;</th>
                            <th scope="col" class="text-center" width="30%"><fmt:message key="${Keys.SALES}"
                                                                                         bundle="${msg}"/></th>
                            <th scope="col" class="text-center" width="30%"><fmt:message key="${Keys.REFUNDS}"
                                                                                         bundle="${msg}"/></th>
                        </tr>
                        </thead>
                        <c:forEach var="field" items="${report.fields}">
                            <tr>
                                <fmt:setBundle basename="${field.bundle}" var="custom"/>

                                <th scope="row"><fmt:message key="${field.name}"
                                                             bundle="${custom}"/></th>

                                <td class="text-right"><fmt:formatNumber type="number"
                                                                         groupingUsed="false"
                                                                         maxFractionDigits="2"
                                                                         minFractionDigits="${field.fractionalDigits}"
                                                                         value="${field.salesValue}"/></td>

                                <td class="text-right"><fmt:formatNumber type="number" groupingUsed="false"
                                                                         maxFractionDigits="2"
                                                                         minFractionDigits="${field.fractionalDigits}"
                                                                         value="${field.refundsValue}"/></td>
                            </tr>
                        </c:forEach>
                        <tbody>
                        </tbody>
                    </table>
                    <div class="card-footer">
                        <dl class="row mb-0" id="total">
                            <dt class="col-4">
                                <fmt:message key="${Keys.DEPOSIT}" bundle="${msg}"/></dt>
                            <dd class="col-8 text-right">
                                <fmt:formatNumber type="number"
                                                  groupingUsed="false"
                                                  maxFractionDigits="2"
                                                  minFractionDigits="2"
                                                  value="${report.deposit}"/>

                            </dd>

                            <dt class="col-4">
                                <fmt:message key="${Keys.WITHDRAWAL}" bundle="${msg}"/></dt>
                            <dd class="col-8 text-right">
                                <fmt:formatNumber type="number"
                                                  groupingUsed="false"
                                                  maxFractionDigits="2"
                                                  minFractionDigits="2"
                                                  value="${report.withdrawal}"/>

                            </dd>

                            <dt class="col-4">
                                <fmt:message key="${Keys.CASH_IN_SAFE}" bundle="${msg}"/></dt>
                            <dd class="col-8 text-right">
                                <fmt:formatNumber type="number"
                                                  groupingUsed="false"
                                                  maxFractionDigits="2"
                                                  minFractionDigits="2"
                                                  value="${report.cashBalance}"/>

                            </dd>
                        </dl>
                    </div>
                </div>
            </div>
            <div class="d-none d-print-block text-uppercase text-center pb-2">
                <c:if test="${not empty report.docType}">
                    <fmt:message key="${report.docType}" bundle="${msg}"/>
                </c:if>
            </div>

            <div class="form-row d-print-none mt-2 mb-3 ml-3">
                <div class=" col-md-2">
                    <button class="btn btn-primary btn-block" onclick="window.print()">
                        <fmt:message key="${Keys.PRINT}" bundle="${msg}"/></button>
                </div>
                <div class=" col-md-2">
                    <form class="form" method="POST" action="${action}" autocomplete="off">
                        <button type="submit" class="btn btn-secondary btn-block">
                            <fmt:message key="${Keys.CLOSE}" bundle="${msg}"/></button>
                    </form>
                </div>
            </div>

        </div>
    </div>
</main>
<%@ include file="includes/footer.jsp" %>
</body>
</html>
