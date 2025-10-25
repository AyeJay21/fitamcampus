<!doctype html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FitamCampus - Login</title>
    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/css/login.css">
</head>
<body>
<div class="loginContainer">
    <h1 class="login_title">Willkommen zurück!</h1>
    <p class="login_subtitle">Melde dich bei FitamCampus an</p>

    <#if error??>
        <div class="error-message">
            ${error}
        </div>
    </#if>
    
    <#if message??>
        <div class="success-message">
            ${message}
        </div>
    </#if>

    <form action="/login" method="post" class="loginFormula">
        <div class="input-group">
            <input class="username" type="email" name="email" placeholder="your@email.com" required>
        </div>
        <div class="input-group">
            <input class="username" type="password" name="password" placeholder="password" required>
        </div>
        <button type="submit" class="login-btn">SIGN IN</button>
    </form>
    
    <div class="auth-link">
        <p>No account yet?</p>
        <a href="/register" class="nav-button primary">REGISTER</a>
    </div>
</div>
<div style="position:fixed;bottom:10px;right:10px;z-index:9999;">
    <a href="/followerRequest" style="background:#4fa3ff;color:#fff;padding:10px 18px;border-radius:8px;text-decoration:none;font-weight:bold;box-shadow:0 2px 8px rgba(0,0,0,0.1);">Follower-Anfragen</a>
</div>
</body>
</html>