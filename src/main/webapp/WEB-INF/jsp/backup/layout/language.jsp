<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="lang">
    <form id="lang-form" class="form" method="POST" action="/language" autocomplete="off">
        <ul class="list-inline">
            <c:set var="current" value="${sessionScope.language.substring(0,2)}"/>
            <c:forEach var="lang" items="${sessionScope.languageSet}">
                <li>
                    <button type="submit"
                            name="l" value="${lang}"
                            class="btn btn-link ${(lang eq current) ? 'active' : ''}">${lang}</button>
                </li>
            </c:forEach>
        </ul>
    </form>
</div>

