<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<footer class="footer">
    <div class="container">
        <div class="lang">
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
    </div>
</footer>
