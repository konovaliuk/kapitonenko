<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="${sessionScope.messages}" var="msg"/>
<fmt:setBundle basename="${sessionScope.settings}" var="settings"/>

<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0, shrink-to-fit=no'
          name='viewport'/>
    <title>CashRegister</title>
    <link href="https://fonts.googleapis.com/css?family=Montserrat:400,500,700&amp;subset=cyrillic-ext"
          rel="stylesheet"/>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/latest/css/font-awesome.min.css"/>
    <link href="<c:url value="/assets/css/bootstrap.min.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/assets/css/now-ui-kit.css?v=1.1.0"/>" rel="stylesheet"/>
    <link href="<c:url value="/assets/css/main.css"/>" rel="stylesheet"/>
</head>
