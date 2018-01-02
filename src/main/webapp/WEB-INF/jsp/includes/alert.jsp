<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--@elvariable id="view" type="ua.kapitonenko.controller.helpers.ViewHelper"--%>
<c:if test="${view.hasMessages}">
    <div class="alert alert-${view.messageType} clearfix" role="alert">
        <div class="container">
            <div class="message-body">
                <ul class="list-unstyled">
                    <c:forEach var="message" items="${view.messageList}">
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
