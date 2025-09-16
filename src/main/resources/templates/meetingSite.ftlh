<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Meeting</title>
    <link rel="stylesheet" href="/css/stylesMeetingSite.css">
</head>
<body>
<div class="headerContainer">
    <h1 class="meetingHeader">Meeting Planer</h1>
    <nav style="margin-top: 10px;">
        <a href="/meetings/all" style="margin-right: 15px; color: #2b90d9; text-decoration: none;">📅 Alle Meetings</a>
        <a href="/mastodon" style="margin-right: 15px; color: #2b90d9; text-decoration: none;">🐘 Mastodon</a>
        <a href="/activitypub" style="margin-right: 15px; color: #1a73e8; text-decoration: none;">🌐 ActivityPub</a>
        <a href="/activitypub/post" style="margin-right: 15px; color: #1a73e8; text-decoration: none;">📝 Posten</a>
        <a href="/activitypub/activities" style="color: #1a73e8; text-decoration: none;">📋 Activities</a>
    </nav>
</div>
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