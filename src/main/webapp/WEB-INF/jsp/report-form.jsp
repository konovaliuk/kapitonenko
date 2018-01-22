<%--@elvariable id="report" type="ua.kapitonenko.app.domain.Report"--%>
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
            <div class="card-body">
                <h5 class="card-title mb-4"><fmt:message key="${Keys.REPORT_NEW}" bundle="${msg}"/></h5>
                <form class="form" method="POST" action="${action}" autocomplete="off">
                    <div class="form-row">
                        <div class="form-group col-md-4">
                            <label><fmt:message key="${Keys.CASHBOX}" bundle="${msg}"/></label>
                            <select class="form-control" name="report.cashbox">
                                <option></option>
                                <%--@elvariable id="cashboxList" type="java.util.List<ua.kapitonenko.app.persistence.records.Cashbox>"--%>
                                <c:forEach items="${cashboxList}" var="option">
                                    <option value="${option.id}" ${report.cashbox.id.equals(option.id) ? "selected" : ""}>
                                            ${option.id}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="mb-1"><fmt:message key="${Keys.REPORT_TYPE}" bundle="${msg}"/></label>
                        <br>
                        <%--@elvariable id="reportTypes" type="ua.kapitonenko.app.domain.ReportType"--%>
                        <c:forEach items="${reportTypes}" var="type">
                            <div class="form-check form-check-inline">
                                <input class="form-check-input" type="radio" name="report.type" id="type-${type.label}"
                                       value="${type}" ${report.type.equals(type) ? "checked" : ""}>
                                <label class="form-check-label" for="type-${type.label}">
                                    <fmt:message key="${type.label}" bundle="${msg}"/>
                                </label>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="form-row d-print-none">
                        <div class="col-xs-6 col-md-2">
                            <button type="submit" class="btn btn-primary btn-block"
                                    name="button" value="button.save">
                                <fmt:message key="${Keys.CREATE}" bundle="${msg}"/></button>
                        </div>
                        <div class="col-xs-6 col-md-2">
                            <a href="/reports" class="btn btn-secondary btn-block">
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
