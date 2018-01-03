<c:if test="${noOfPages gt 1}">
    <nav class="mt-4">
        <ul class="pagination mb-0">
            <c:set var="pdisabled" value="${currentPage == 1 ? 'disabled' : ''}"/>
            <li class="page-item ${pdisabled}">
                <a class="page-link" href="${action}?page=${currentPage - 1}" tabindex="-1" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                    <span class="sr-only">Previous</span>
                </a>
            </li>
            <table border="1" cellpadding="5" cellspacing="5">
                <tr>
                    <c:forEach begin="1" end="${noOfPages}" var="i">
                        <c:choose>
                            <c:when test="${currentPage eq i}">
                                <li class="page-item active">
                                    <a class="page-link">${i}<span class="sr-only">(current)</span></a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="page-item"><a class="page-link" href="${action}?page=${i}">${i}</a></li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </tr>
            </table>
            <c:set var="ndisabled" value="${currentPage eq noOfPages ? 'disabled' : ''}"/>
            <li class="page-item ${ndisabled}">
                <a class="page-link" href="${action}?page=${currentPage + 1}" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                    <span class="sr-only">Next</span>
                </a>
            </li>

        </ul>
    </nav>
</c:if>