<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Meeting Overview</title>
    <link rel="stylesheet" href="/css/overviewMeetingStyles.css">
</head>
<body>
<nav style="padding: 15px 20px; background: white; margin-bottom: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); text-align: center;">
    <a onclick="window.location.href='/users/chats'">Chat</a> |
    <a href="/meetingSite" style="color: #3498db; text-decoration: none; margin: 0 15px; font-weight: 500; transition: color 0.3s;">← Zurück zum Hauptmenü</a> |
    <a href="/meetings" style="color: #3498db; text-decoration: none; margin: 0 15px; font-weight: 500; transition: color 0.3s;">Alle Meetings</a> |
    <a href="/logout" style="color: #e74c3c; text-decoration: none; margin: 0 15px; font-weight: 500; transition: color 0.3s;">Logout</a>
</nav>
<style>
nav a:hover {
    color: #2980b9 !important;
}
nav a[href="/logout"]:hover {
    color: #c0392b !important;
}
</style>
<div class="headerContainer">
    <h1>Meeting Overview</h1>
</div>
<div>
    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Ort</th>
            <th>Datum</th>
            <th>Uhrzeit</th>
            <th>Sportart</th>
            <th>Indoor/Outdoor</th>
            <th>Beschreibung</th>
            <th>Wetter</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>${meeting.name}</td>
            <td>${meeting.ort}</td>
            <td>${meeting.date}</td>
            <td>${meeting.time}</td>
            <td>${meeting.sportArt}</td>
            <td>${meeting.inOrOut}</td>
            <td>${meeting.description}</td>
            <#if meeting.inOrOut == "outdoor">
                <td>${weathers.temperature}˚C ${weathers.weatherType}</td>
            </#if>
        </tr>
        </tbody>
    </table>
</div>

<div>
    <form action="/meetings/${meeting.id}/edit" method="get">
        <button id="editId" name="edit" type="submit">Editieren</button>
    </form>
    <form action="/meetings/${meeting.id}" method="post">
        <input type="hidden" name="_method" value="delete">
        <button id="deleteId" name="delete" type="submit">Löschen</button>
    </form>
</div>

<div>
    <h2>Kommentare</h2>
    <form action="/meetings/${meeting.id}/comments" method="post">
        <input type="text" id="usernameId" name="name" placeholder="Name">
        <input type="text" id="commentarId" name="kommentar" placeholder="Kommentar">
        <input type="submit" class="kommentar" value="Kommentieren">
    </form>
    <table>
        <#if comments??>
            <thead>
            <tr>
                <th>Name</th>
                <th>Kommentar</th>
            </tr>
            </thead>
            <tbody>
            <#list comments as comment>
                <tr>
                    <td>${comment.name?string}</td>
                    <td>${comment.kommentar?string}</td>
                </tr>
            </#list>
            </tbody>
        </#if>
    </table>
</div>
</body>
</html>