<!doctype html>
<html lang="en">
<head>
    <title>Meeting Overview</title>
    <link rel="stylesheet" href="/css/allMeetings.css">
</head>
<body>
<h1>All Meetings</h1>
<form action="/meetings" method="get">
    <#list meetings as meeting>
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
            </tr>
            </tbody>
        </table>
    </#list>
</form>
</body>
</html>