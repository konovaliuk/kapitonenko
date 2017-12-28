<%@include file="layout/header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<body class="inner-page">
<div class="page-header">
    <div class="container">
        <div class="col-xs-12 ml-auto mr-auto">
            <div class="header header-primary">
                <div class="logo pull-left">
                    <span>Cash</span><span class="green">Register</span>
                </div>
                <div class="button-group pull-right">
                    <a class="btn-link" href="#profile">Logout (admin)</a>
                    <div class="lang">
                        <a class="link" href="#profile">УКР</a>
                        <span>&nbsp;&nbsp;|&nbsp;&nbsp;</span>
                        <a class="link green" href="#profile">EN</a>
                    </div>
                </div>
            </div>
            <div class="card">
                <ul class="nav nav-tabs">
                    <li class="nav-item">
                        <a class="nav-link" href="#home">
                            <i class="now-ui-icons objects_umbrella-13"></i> Home
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="#profile">
                            <i class="now-ui-icons shopping_cart-simple"></i> Profile
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#messages">
                            <i class="now-ui-icons shopping_shop"></i> Messages
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#settingsMap">
                            <i class="now-ui-icons ui-2_settings-90"></i> Settings
                        </a>
                    </li>
                </ul>
                <div class="card-body">
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script src="../../assets/js/core/jquery.3.2.1.min.js" type="text/javascript"></script>
<script src="../../assets/js/core/popper.min.js" type="text/javascript"></script>
<script src="../../assets/js/core/bootstrap.min.js" type="text/javascript"></script>
<script src="../../assets/js/now-ui-kit.js?v=1.1.0" type="text/javascript"></script>

</html>