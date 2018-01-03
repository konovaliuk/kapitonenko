<%@ page import="ua.kapitonenko.controller.keys.Keys" %>

<div class="card-header d-print-none">
    <ul class="nav nav-tabs card-header-tabs pull-left">
        <c:set var="productList" value="/products"/>
        <c:set var="productListClass" value="${action eq productList ? 'active' : ''}"/>
        <c:set var="productForm" value="/create-product"/>
        <c:set var="productFormClass" value="${action eq productForm ? 'active' : ''}"/>
        <c:set var="receiptForm" value="/create-receipt"/>
        <c:set var="receiptFormClass" value="${action eq receiptForm ? 'active' : ''}"/>
        <c:set var="receiptList" value="/receipts"/>
        <c:set var="receiptListClass" value="${action eq receiptList ? 'active' : ''}"/>
        <li class="nav-item">
            <a class="nav-link ${productListClass}" href="${productList}"><fmt:message
                    key="${Keys.PRODUCT_LIST}"
                    bundle="${msg}"/></a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${productFormClass}" href="${productForm}"><fmt:message
                    key="${Keys.PRODUCT_NEW}" bundle="${msg}"/></a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${receiptListClass}" href="${receiptList}"><fmt:message
                    key="${Keys.RECEIPT_LIST}" bundle="${msg}"/></a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${receiptFormClass}" href="${receiptForm}"><fmt:message
                    key="${Keys.RECEIPT_NEW}" bundle="${msg}"/></a>
        </li>
    </ul>
</div>
