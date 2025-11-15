<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>FitamCampus - Login</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="/css/login.css">
</head>
<body>
<div class="loginContainer">
    <h1 class="login_title">Create Account</h1>
    <p class="login_subtitle">Join FitamCampus today</p>

    <form class="loginFormula" action="/setup-actor" method="post">
        <div class="input-group">
            <input type="text" name="username" class="username" placeholder="Username" required>
        </div>
        <div class="input-group">
            <input type="email" name="email" class="password" placeholder="Email Address" required>
        </div>
        <div class="input-group">
            <input type="password" name="password" class="password" placeholder="Password" required>
        </div>
        <button type="submit" class="login-btn">Create Account</button>
    </form>
    
    <div class="auth-link">
        <p>Already have an account?</p>
        <a href="/login" class="nav-button secondary">Sign In Instead</a>
    </div>
</div>
</body>
</html>