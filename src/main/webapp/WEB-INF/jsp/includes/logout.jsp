<%@ page import="ua.kapitonenko.app.config.keys.Keys" %>
<header class="clearfix">
    <div class="container">
        <div class="brand">
            <a href="/" tabindex="-1"><span>Cash</span><span class="green">Register</span></a>
        </div>
        <div class="buttons d-print-none">
            <form class="form" method="POST" action="/logout" autocomplete="off">
                <button type="submit" class="btn btn-link" tabindex="-2">
                    <fmt:message key="${Keys.LOGOUT}" bundle="${msg}"/> (${user.username})
                </button>
            </form>
        </div>
    </div>
</header>
