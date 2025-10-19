<!doctype html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chat</title>
    <style>
        body {
            margin: 0;
            padding: 1rem;
            font-family: sans-serif;
        }

        .follower p {
            margin: 0.25rem 0;
        }


    </style>
</head>
<body>
<h1>Chat</h1>

<div class="mainContainer">
    <div class="follower">
        <h3 class="username">${username}</h3>
        <div id="followerList" onclick="window.location.href='/users/${username}/chats'">Lade Follower...</div>
    </div>

    <div id="chatContainer">
        <p>Wähle einen Follower, um den Chat zu laden.</p>
    </div>

</div>

<script>
    const username = "${username}";
    const listDiv = document.getElementById("followerList");

    fetch(`/users/${username}/followers`)
        .then(res => {
            if (!res.ok) throw new Error("Fehler beim Laden");
            return res.json();
        })
        .then(followers => {
            listDiv.innerHTML = "";
            if (followers.length === 0) {
                listDiv.textContent = "Keine Follower gefunden.";
                return;
            }
            followers.forEach(follower => {
                const p = document.createElement("p");

                p.textContent = follower.followerUrl;
                p.classList.add('follower');
                p.addEventListener("click", () => {
                    // Fetch messages via the JSON endpoint and render into #chatContainer
                    const url = `/users/${encodeURIComponent(username)}/chats/messages?receiver=${encodeURIComponent(follower.followerUrl)}`;
                    const chatContainer = document.getElementById('chatContainer');
                    chatContainer.innerHTML = '<p>Lade Nachrichten...</p>';
                    fetch(url)
                        .then(res => {
                            if (!res.ok) throw new Error('Fehler beim Laden der Nachrichten');
                            return res.json();
                        })
                        .then(messages => {
                            chatContainer.innerHTML = '';
                            if (!messages || messages.length === 0) {
                                chatContainer.textContent = 'Keine Nachrichten.';
                                return;
                            }
                            messages.forEach(m => {
                                const div = document.createElement('div');
                                // Passe die Felder an deine Message-Klasse an
                                const sender = m.sender || m.from || '';
                                const text = m.text || m.message || m.content || '';
                                const time = m.timeStamp || m.time || '';
                                div.textContent = `${sender}: ${text}` + (time ? ` (${time})` : '');
                                chatContainer.appendChild(div);
                            });
                        })
                        .catch(err => {
                            chatContainer.textContent = 'Fehler: ' + err.message;
                        });
                });
                listDiv.appendChild(p);
            });
        })
        .catch(err => {
            listDiv.textContent = "Fehler: " + err.message;
        });
</script>
</body>
</html>
