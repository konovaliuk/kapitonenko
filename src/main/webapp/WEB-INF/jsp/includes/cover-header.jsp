<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="${sessionScope.messages}" var="msg"/>
<fmt:setBundle basename="${sessionScope.settings}" var="settings"/>

<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0, shrink-to-fit=no'
          name='viewport'/>
    <title>CashRegister</title>
    <link href="<c:url value="/assets/css/bootstrap.min.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/assets/css/now-ui-kit.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/assets/css/cover.css"/>" rel="stylesheet"/>
</head>