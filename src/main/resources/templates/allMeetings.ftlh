<!doctype html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Alle Meetings - FitamCampus</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background: #f8f9fa;
            color: #2c3e50;
            line-height: 1.6;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }

        /* Navigation */
        nav {
            padding: 15px 20px;
            background: white;
            margin-bottom: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            text-align: center;
        }

        nav a {
            color: #3498db;
            text-decoration: none;
            margin: 0 15px;
            font-weight: 500;
            transition: color 0.3s;
        }

        nav a:hover {
            color: #2980b9;
        }

        nav a[href="/logout"] {
            color: #e74c3c;
        }

        nav a[href="/logout"]:hover {
            color: #c0392b;
        }

        /* Page Title */
        .page-title {
            text-align: center;
            font-size: 2.2rem;
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 30px;
        }

        /* Meetings Container */
        .meetings-container {
            display: grid;
            gap: 20px;
            margin-bottom: 40px;
        }

        .meeting-card {
            background: white;
            border-radius: 12px;
            padding: 25px;
            box-shadow: 0 2px 15px rgba(0, 0, 0, 0.08);
            transition: transform 0.2s ease, box-shadow 0.2s ease;
            cursor: pointer;
        }

        .meeting-card:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 25px rgba(0, 0, 0, 0.12);
        }

        .meeting-name {
            font-size: 1.4rem;
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 15px;
        }

        .meeting-details {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
        }

        .detail-item {
            display: flex;
            flex-direction: column;
        }

        .detail-label {
            font-size: 0.8rem;
            color: #7f8c8d;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-bottom: 3px;
        }

        .detail-value {
            font-weight: 500;
            color: #2c3e50;
        }

        .no-meetings {
            text-align: center;
            color: #95a5a6;
            font-size: 1.2rem;
            padding: 60px 20px;
            background: white;
            border-radius: 12px;
            box-shadow: 0 2px 15px rgba(0, 0, 0, 0.08);
        }

        /* Social Form */
        .social-form {
            background: white;
            border-radius: 12px;
            padding: 25px;
            box-shadow: 0 2px 15px rgba(0, 0, 0, 0.08);
        }

        .form-title {
            font-size: 1.2rem;
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 20px;
        }

        .form-row {
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
            align-items: end;
        }

        .form-group {
            flex: 1;
            min-width: 150px;
        }

        .form-input, .form-select {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #ecf0f1;
            border-radius: 6px;
            font-size: 1rem;
            transition: border-color 0.2s;
        }

        .form-input:focus, .form-select:focus {
            outline: none;
            border-color: #3498db;
        }

        .form-btn {
            padding: 12px 25px;
            background: #3498db;
            color: white;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 500;
            transition: background 0.2s;
        }

        .form-btn:hover {
            background: #2980b9;
        }

        /* Delete Button Styling */
        .meeting-container {
            position: relative;
        }

        .delete-btn {
            position: absolute;
            top: 10px;
            right: 10px;
            background: #e74c3c;
            color: white;
            width: 30px;
            height: 30px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            font-size: 16px;
            z-index: 10;
            transition: background 0.2s;
        }

        .delete-btn:hover {
            background: #c0392b;
        }

        @media (max-width: 768px) {
            .container {
                padding: 15px;
            }

            .page-title {
                font-size: 1.8rem;
            }

            .meeting-details {
                grid-template-columns: 1fr;
            }

            .form-row {
                flex-direction: column;
            }

            .form-group {
                min-width: auto;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <nav>
    <a onclick="window.location.href='/users/chats'">Chat</a> |
    <a href="/meetingSite">Zurück zum Hauptmenü</a> |
    <a href="/meetings">Alle Meetings</a> |
    <a href="/logout">Logout</a>
    </nav>

    <h1 class="page-title">Alle Meetings</h1>

    <div class="meetings-container">
        <#if meetings?? && meetings?size gt 0>
            <#list meetings as meeting>
                <div class="meeting-container">
                    <div class="delete-btn" onclick="deleteMeeting('${meeting.id}')">X</div>
                    <div class="meeting-card" onclick="window.location.href='/meetings/${meeting.id}'">
                    <div class="meeting-name">${meeting.name}</div>
                    <div class="meeting-details">
                        <div class="detail-item">
                            <div class="detail-label">Ort</div>
                            <div class="detail-value">${meeting.ort}</div>
                        </div>
                        <div class="detail-item">
                            <div class="detail-label">Datum</div>
                            <div class="detail-value">${meeting.date}</div>
                        </div>
                        <div class="detail-item">
                            <div class="detail-label">Uhrzeit</div>
                            <div class="detail-value">${meeting.time}</div>
                        </div>
                            <#if weathers[meeting.id]??>
                                <#assign weather = weathers[meeting.id]>
<#--                                <div class="detail-item">-->
<#--                                    <div class="detail-label">Debug Wetter-Key</div>-->
<#--                                    <div class="detail-value">${meeting.id}</div>-->
<#--                                </div>-->
                            </#if>
                        </div>
                        <div class="detail-item">
                            <div class="detail-label">Beschreibungs</div>
                            <div class="detail-value">${meeting.description}</div>
                        </div>
<#--                        <div class="detail-item">-->
<#--                            <div class="detail-label">MeetingID</div>-->
<#--                            <div class="detail-value">${meeting.id}</div>-->
<#--                        </div>-->
                        <#if weathers[meeting.id]??>
                        <#assign weather = weathers[meeting.id]>
                        <#if weather??>
                        <div class="detail-item">
                            <div class="detail-label">Wetter</div>
                            <div class="detail-value">
                                Temperatur: ${weather.temperature}°C<br>
                                Typ: ${weather.weatherType}
                            </div>
                        </div>
                        </#if>
                        </#if>
                </div>
            </#list>
        <#else>
            <div class="no-meetings">
                Noch keine Meetings vorhanden.
                <a href="/meetingSite" style="color: #3498db; text-decoration: none;">Erstelle das erste Meeting!</a>
            </div>
        </#if>
    </div>
    <div>
    </div>

    <div class="social-form">
        <div class="form-title">ActivityPub Post - Meetings teilen</div>
        <form id="activityPubForm">
            <div class="form-row">
                <div class="form-group">
                    <label>Ziel-Handle:</label>
                    <input type="text" name="targetHandle" class="form-input targetHandle"
                           placeholder="username" required>
                </div>
            </div>

            <div class="form-group">
                <label>Nachricht:</label>
                <textarea name="message" class="form-input ownMessage" rows="3"
                          placeholder="Deine Nachricht..." required></textarea>
            </div>

            <div class="form-group">
                <label>Meetings auswählen:</label>
                <div class="meetings-selection">
                    <#if meetings?? && meetings?size gt 0>
                        <#list meetings as meeting>
                            <label class="meeting-checkbox">
                                <input type="checkbox" name="meetingIds" value="${meeting.id}">
                                <span class="checkmark"></span>
                                ${meeting.name}, ${meeting.sportArt} (${meeting.date}, ${meeting.time})
                            </label>
                        </#list>
                    <#else>
                        <p style="color: #999;">Keine Meetings verfügbar</p>
                    </#if>
                </div>
            </div>

            <button type="submit" class="form-btn">📤 ActivityPub Post senden</button>
        </form>

        <div id="result" class="result-message" style="display: none;"></div>
    </div>

    <style>
        .meetings-selection {
            max-height: 201px;
            overflow-y: auto;
            border: 2px solid #ecf0f1;
            border-radius: 6px;
            padding: 15px;
            background: #f9f9f9;
        }

        .meeting-checkbox {
            display: block;
            margin-bottom: 10px;
            cursor: pointer;
            padding: 8px;
            border-radius: 4px;
            transition: background 0.2s;
        }

        .meeting-checkbox:hover {
            background: #f0f0f0;
        }

        .meeting-checkbox input[type="checkbox"] {
            margin-right: 10px;
        }

        .result-message {
            margin-top: 20px;
            padding: 15px;
            border-radius: 6px;
            font-weight: 500;
        }

        .result-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .result-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        textarea.form-input {
            resize: vertical;
            min-height: 80px;
        }

        label {
            display: block;
            margin-bottom: 5px;
            font-weight: 500;
            color: #2c3e50;
        }
    </style>

    <script>
        document.getElementById('activityPubForm').addEventListener('submit', function (e) {
            e.preventDefault();

            const selectedMeetings = [];
            document.querySelectorAll('input[name="meetingIds"]:checked').forEach(box => {
                const label = box.parentElement;

                selectedMeetings.push(label.textContent.trim());
            })

            let message = document.querySelector('[name="message"]').value;
            if (selectedMeetings.length > 0) {
                message += "\n\nAusgewählte Meetings:\n" + selectedMeetings.map(m => "- " + m).join("\n");
            }
            console.log("Message:" +document.querySelector('[name="message"]').value + "TargetHandle: "+ document.querySelector('[name="targetHandle"]').value + "FromUser: " + "${currentUser.username}");
            const requestData = {
                message: message,
                targetHandle: document.querySelector('[name="targetHandle"]').value,
                fromUser: "${currentUser.username}",
            };
            console.log("Sending data" + requestData);
            fetch("/activitypub/send-private-message", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/activity+json, application/ld+json'
                },
                body: JSON.stringify(requestData)
            })
                .then(response => response.json())
                .then(data => {
                    console.log('Success:', data);
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        });
    </script>

    <script>
        function deleteMeeting(meetingId){
            fetch("/meetings/"+meetingId, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    if(response.ok){
                        location.reload()
                    }else{
                        alert('Fehler beim Löschen!');
                    }
                })
                .catch(error =>{
                    console.error('Error', error);
                    alert('Fehler beim Löschen!');
                })
        }
    </script>
</div>
<div style="position:fixed;bottom:10px;right:10px;z-index:9999;">
    <a href="/followerRequest/${currentUser.username}" style="background:#4fa3ff;color:#fff;padding:10px 18px;border-radius:8px;text-decoration:none;font-weight:bold;box-shadow:0 2px 8px rgba(0,0,0,0.1);">Follower-Anfragen</a>
</div>
</body>
</html>