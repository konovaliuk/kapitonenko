<header class="clearfix">
    <div class="container">
        <div class="brand"><span>Cash</span><span class="green">Register</span></div>
        <div class="buttons">
            <form class="form" method="POST" action="/logout" autocomplete="off">
                <button type="submit" class="btn btn-link">
                    <fmt:message key="${Keys.LOGOUT}" bundle="${msg}"/> (${user.username})
                </button>
            </form>
        </div>
    </div>
</header>
