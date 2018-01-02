<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<footer class="footer">
    <div class="container">
        <div class="lang pull-left">
            <form id="lang-form" class="form" method="POST" action="/language" autocomplete="off">
                <ul class="list-inline">
                    <li><fmt:message key="${Keys.LANGUAGES}" bundle="${msg}"/>:</li>
                    <c:set var="current" value="${sessionScope.locale.substring(0,2)}"/>
                    <c:forEach var="lang" items="${sessionScope.languages}">
                        <li>
                            <button type="submit"
                                    name="l" value="${lang}"
                                    class="btn btn-link ${(lang eq current) ? 'active' : ''}">${lang}</button>
                        </li>
                    </c:forEach>
                </ul>
            </form>
        </div>
        <%--@elvariable id="company" type="ua.kapitonenko.domain.entities.Company"--%>
        <c:if test="${company != null}">
            <p class="company pull-right mb-0">
                <fmt:message key="${company.bundleKeyName}" bundle="${settings}"/>&nbsp;
                <fmt:message key="${company.bundleKeyAddress}" bundle="${settings}"/>&nbsp;
                <fmt:message key="${Keys.COMPANY_PN}" bundle="${msg}"/>&nbsp;${company.pnNumber}
            </p>
        </c:if>
    </div>
</footer>
