<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ua.kapitonenko.app.config.keys.Keys" %>

<footer class="footer">
    <div class="container">
        <div class="lang pull-left">
            <form id="lang-form" class="form" method="POST" action="/language" autocomplete="off">
                <ul class="list-inline">
                    <li><fmt:message key="${Keys.LANGUAGES}" bundle="${msg}"/>:</li>
                    <c:set var="current" value="${locale.substring(0,2)}"/>
                    <c:forEach var="lang" items="${languages}">
                        <li>
                            <button type="submit"
                                    name="l" value="${lang}"
                                    class="btn btn-link ${(lang eq current) ? 'active' : ''}">${lang}</button>
                        </li>
                    </c:forEach>
                </ul>
            </form>
        </div>
        <%--@elvariable id="company" type="ua.kapitonenko.app.dao.records.Company"--%>
        <c:if test="${not empty user.id}">
            <p class="company pull-right mb-0">
                <fmt:setBundle basename="${company.bundleName}" var="comBundle"/>
                <fmt:message key="${company.bundleKeyName}" bundle="${comBundle}"/>&nbsp;
                <fmt:message key="${company.bundleKeyAddress}" bundle="${comBundle}"/>&nbsp;
                <fmt:message key="${Keys.COMPANY_PN}" bundle="${msg}"/>&nbsp;${company.pnNumber}
            </p>
        </c:if>
    </div>
</footer>

<script src="<c:url value="/assets/js/core/jquery.3.2.1.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/assets/js/core/popper.min.js"/>" type="text/javascript"></script>
<script src="<c:url value="/assets/js/core/bootstrap.min.js"/>" type="text/javascript"></script>

