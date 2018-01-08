<%--@elvariable id="report" type="ua.kapitonenko.app.domain.Report"--%>
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
                        <h4 class="card-title"><fmt:message key="${Keys.REPORT_LIST}" bundle="${msg}"/></h4>
                    </div>
                    <u:can action="/receipts">
                        <div class="col-2 pl-0 ml-auto mb-4">
                            <a class="btn btn-outline-secondary btn-block" href="/receipts" role="button">
                                <fmt:message key="${Keys.RECEIPT_LIST}" bundle="${msg}"/></a>

                        </div>
                    </u:can>
                    <u:can action="/create-report">
                        <div class="col-2 pl-0 ml-auto mb-4">
                            <a class="btn btn-primary btn-block" href="/create-report" role="button">
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
                                ${report.fields.size()}
                                <c:forEach var="field" items="${report.fields}">
                                    <%--<c:if test="${field.showInList}">--%>
                                    <fmt:setBundle basename="${field.bundle}" var="custom"/>

                                    <th scope="col">
                                        <fmt:message key="${field.name}" bundle="${custom}"/><br>
                                        (<fmt:message key="${Keys.SALES}" bundle="${msg}"/>)
                                    </th>

                                    <th scope="col">
                                        <fmt:message key="${field.name}" bundle="${custom}"/><br>
                                        (<fmt:message key="${Keys.REFUNDS}" bundle="${msg}"/>)
                                    </th>

                                    <%--</c:if>--%>
                                </c:forEach>
                            </tr>
                            </thead>
                            <tbody>

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