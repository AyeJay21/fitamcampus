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
    <div class="followerContainer">
        <h3 class="username" id="currentUser"></h3>
        <div id="followerList">Lade Follower...</div>
    </div>

    <div class="chatContainer">
        <h3 id="chatTitle">Wähle einen Kontakt</h3>
        <div id="messageList" class="no-chat">Keine Nachrichten</div>
    </div>
</div>

<script>
    const username = document.body.dataset.username || "testuser";
    let selectedReceiver = null;

    document.getElementById("currentUser").textContent = username;

    // Follower laden
    fetch(`/users/${username}/followers`)
        .then(res => res.json())
        .then(followers => {
            const listDiv = document.getElementById("followerList");
            listDiv.innerHTML = "";

            if (followers.length === 0) {
                listDiv.textContent = "Keine Follower gefunden.";
                return;
            }

            followers.forEach(follower => {
                const p = document.createElement("p");
                p.textContent = follower.followerUrl;
                p.dataset.followerUrl = follower.followerUrl;
                p.classList.add('follower');

                p.addEventListener("click", () => {
                    document.querySelectorAll(".follower").forEach(el => el.classList.remove("active"));
                    p.classList.add("active");
                    selectedReceiver = follower.followerUrl;
                    loadChat(username, selectedReceiver);
                });

                listDiv.appendChild(p);
            });
        })
        .catch(err => {
            console.error("Fehler beim Laden der Follower:", err);
        });

    // Chat-Nachrichten laden
    function loadChat(sender, receiver) {
        document.getElementById("chatTitle").textContent = `Chat mit ${receiver}`;

        fetch(`/users/${sender}/chats?receiver=${encodeURIComponent(receiver)}`)
            .then(res => res.json())
            .then(messages => {
                const messageList = document.getElementById("messageList");
                messageList.innerHTML = "";

                if (messages.length === 0) {
                    messageList.className = "no-chat";
                    messageList.textContent = "Keine Nachrichten mit diesem Kontakt";
                    return;
                }

                messageList.className = "";
                messages.forEach(msg => {
                    const msgDiv = document.createElement("div");
                    msgDiv.classList.add("message");
                    msgDiv.classList.add(msg.sender === username ? "sent" : "received");

                    const date = new Date(msg.timeStamp).toLocaleString('de-DE');
                    msgDiv.innerHTML = `
                            <div class="message-sender">\${msg.sender}</div>
                            <div>\${msg.text}</div>
                            <div class="message-time">\${date}</div>
                        `;

                    messageList.appendChild(msgDiv);
                });

                // Scroll zum Ende
                messageList.scrollTop = messageList.scrollHeight;
            })
            .catch(err => {
                console.error("Fehler beim Laden des Chats:", err);
            });
    }
</script>
</body>
</html>
