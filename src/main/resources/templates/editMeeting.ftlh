<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Meeting</title>
    <link rel="stylesheet" href="/css/editMeetingStyles.css">
</head>
<body>
<nav style="padding: 1rem; background: #f8f9fa; margin-bottom: 2rem;">
    <a onclick="window.location.href='/users/chats'">Chat</a> |
    <a href="/meetingSite">← Zurück zum Hauptmenü</a> |
    <a href="/meetings">Alle Meetings</a> |
    <a href="/meetings/${meeting.id}">Meeting Ansicht</a> |
    <a href="/logout">Logout</a>
</nav>
<div class="headerContainer">
    <h1>Meeting</h1>
    <h2>Neuer Plan</h2>
</div>
<div class="mainContainer">
    <form action="/meetings/${meeting.id}" method="post">
        <label for="nameId">Name:</label>
        <input type="text" id="nameId" name="name" placeholder="Name" value="${meeting.name}">

        <label for="ortId">Ort:</label>
        <input type="text" id="ortId" name="ort" placeholder="Ort" value="${meeting.ort}">

        <label for="dateId">Datum:</label>
        <input
                type="date"
                id="dateId"
                name="date"
                value="${meeting.date}"
                min="${.now?string('yyyy-MM-dd')}"
                max="2025-12-31" />
        <#--<input type="text" id="dateId" name="date" placeholder="Datum" value="${meeting.date}">-->

        <label for="timeId">Uhr:</label>
        <input type="time" id="timeId" name="time" placeholder="Time" value="${meeting.time}">

        <label for="sportartId">Sportart:</label>
        <input type="text" id="sportartId" name="sportArt" placeholder="Sportart" value="${meeting.sportArt}">

        <label for="indorOutdoorId">Indoor oder Outdoor:</label>
        <select id="indorOutdoorId" name="inOrOut">
            <option value="indoor">Indoor</option>
            <option value="outdoor">Outdoor</option>
        </select>

        <label for="descriptionId">Beschreibung:</label>
        <textarea id="descriptionId" name="description" placeholder="Beschreibung">${meeting.description}</textarea>
        <input type="hidden" name="_method" value="PATCH">
        <button id="saveId" name="save" type="submit">Speichern</button>
    </form>
</div>
</body>
</html>