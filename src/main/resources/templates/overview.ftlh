<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Meeting Overview</title>
    <link rel="stylesheet" href="/css/overviewMeetingStyles.css">
</head>
<body>
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
    
    <form action="/mastodon/post" method="post" style="margin-top: 10px;">
        <input type="hidden" name="content" value="🏃‍♂️ Neues Meeting: ${meeting.name} am ${meeting.date} um ${meeting.time} in ${meeting.ort}! Sportart: ${meeting.sportArt} #FitamCampus #Sport">
        <button type="submit" style="background-color: #2b90d9; color: white; border: none; padding: 8px 16px; border-radius: 4px; cursor: pointer;">📤 Auf Mastodon teilen</button>
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