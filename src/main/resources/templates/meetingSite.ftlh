<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Meeting</title>
    <link rel="stylesheet" href="/css/stylesMeetingSite.css">
</head>
<body>
<nav class="navbar">
    <div class="navbar-left">
        <a onclick="window.location.href='/users/chats'">Chat</a> |
        <a href="/meetings" class="nav-link">🏠 Alle Meetings</a>
        <a href="/meetingSite" class="nav-link">➕ Neues Meeting</a>
    </div>
    <div class="navbar-right">
        <#if currentUser??>
            <span class="nav-user">👤 ${currentUser.username}</span>
            <a href="/logout" class="nav-link logout">Logout</a>
        </#if>
    </div>
</nav>
<style>
    .navbar {
        display: flex;
        justify-content: space-between;
        align-items: center;
        background: #fff;
        padding: 18px 32px;
        box-shadow: 0 2px 10px rgba(0,0,0,0.07);
        border-radius: 10px;
        margin: 24px auto 32px auto;
        max-width: 900px;
    }
    .navbar-left, .navbar-right {
        display: flex;
        align-items: center;
        gap: 18px;
    }
    .nav-link {
        color: #3498db;
        text-decoration: none;
        font-weight: 500;
        font-size: 1.08rem;
        padding: 6px 14px;
        border-radius: 6px;
        transition: background 0.2s, color 0.2s;
    }
    .nav-link:hover {
        background: #f0f4fa;
        color: #217dbb;
    }
    .nav-link.logout {
        color: #e74c3c;
        background: #fbeeea;
    }
    .nav-link.logout:hover {
        background: #f8d7da;
        color: #c0392b;
    }
    .nav-user {
        font-size: 1.08rem;
        color: #2c3e50;
        font-weight: 500;
        margin-right: 8px;
    }
</style>
<div class="mainContainer">
    <form action="/meetings" method="post">
        <label for="nameId">Name:</label>
        <input type="text" id="nameId" name="name" placeholder="Name">

        <label for="ortId">Ort:</label>
        <input type="text" id="ortId" name="ort" placeholder="Ort">

        <label for="dateId">Datum:</label>
<#--        <input type="text" id="dateId" name="date" placeholder="Datum">-->
        <input
                type="date"
                id="dateId"
                name="date"
                value="${.now?string('yyyy-MM-dd')}"
                min="${.now?string('yyyy-MM-dd')}"
                max="2025-12-31" />

        <label for="timeId">Uhrzeit:</label>
        <input type="time" id="timeId" name="time" placeholder="Uhrzeit">

        <label for="sportartId">Sportart:</label>
        <input type="text" id="sportartId" name="sportArt" placeholder="Sportart">

        <label for="indorOutdoorId">Indoor oder Outdoor:</label>
        <select id="indorOutdoorId" name="inOrOut">
            <option value="indoor">Indoor</option>
            <option value="outdoor">Outdoor</option>
        </select>

        <label for="descriptionId">Beschreibung:</label>
        <textarea id="descriptionId" name="description" placeholder="Beschreibung"></textarea>

        <input type="submit" value="Submit">
    </form>
</div>
</body>
</html>