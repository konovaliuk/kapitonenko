<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="alert" type="ua.kapitonenko.app.controller.helpers.AlertContainer"--%>

<c:if test="${not empty alert && alert.hasMessages}">
    <div class="alert alert-${alert.messageType} clearfix" role="alert">
        <div class="container">
            <div class="message-body">
                <ul class="list-unstyled">
                    <c:forEach var="message" items="${alert.messageList}">
                        <li>${message}</li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
            <span aria-hidden="true">&times;</span>
        </button>
    </div>
</c:if>
