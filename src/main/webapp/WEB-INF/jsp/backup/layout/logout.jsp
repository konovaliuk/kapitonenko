<div class="header header-primary">
<div class="logo pull-left">
    <span>Cash</span><span class="green">Register</span>
</div>
<div class="button-group pull-right">
    <form id="logout-form" class="form" method="POST" action="/logout" autocomplete="off">
        <button type="submit" class="btn btn-link dark-bg">
            <fmt:message key="${Messages.LOGOUT}" bundle="${msg}"/> (${user.username})
        </button>
    </form>
</div>
</div>
