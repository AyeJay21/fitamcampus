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
        <h3 class="username" id="currentUser">Laden...</h3>
        <div id="followerList">Lade Follower...</div>
    </div>

    <div class="chatContainer">
        <h3 id="chatTitle">Wähle einen Kontakt</h3>
        <div id="messageList" class="no-chat">Keine Nachrichten</div>
    </div>
</div>

<script>
    // Username aus URL extrahieren: /users/{username}/chats
    const pathParts = window.location.pathname.split('/');
    const username = pathParts[2];

    console.log("Current username:", username);

    document.getElementById("currentUser").textContent = username;

    // Follower laden
    function loadFollowers() {
        fetch(`/users/${username}/followers`)
            .then(res => res.json())
            .then(followers => {
                console.log("Followers:", followers);
                const listDiv = document.getElementById("followerList");
                listDiv.innerHTML = "";

                if (!followers || followers.length === 0) {
                    listDiv.textContent = "Keine Follower gefunden.";
                    return;
                }

                followers.forEach(follower => {
                    const p = document.createElement("p");
                    p.textContent = follower.followerUrl;
                    p.classList.add('follower');

                    p.onclick = function() {
                        document.querySelectorAll(".follower").forEach(el => el.classList.remove("active"));
                        this.classList.add("active");
                        loadChat(username, follower.followerUrl);
                    };

                    listDiv.appendChild(p);
                });
            })
            .catch(err => {
                console.error("Fehler beim Laden der Follower:", err);
                document.getElementById("followerList").textContent = "Fehler beim Laden: " + err.message;
            });
    }

    // Chat-Nachrichten laden
    function loadChat(sender, receiver) {
        console.log("Loading chat between", sender, "and", receiver);
        document.getElementById("chatTitle").textContent = "Chat mit " + receiver;

        fetch(`/users/${sender}/chats?receiver=${encodeURIComponent(receiver)}`)
            .then(res => res.json())
            .then(messages => {
                console.log("Messages:", messages);
                const messageList = document.getElementById("messageList");
                messageList.innerHTML = "";

                if (!messages || messages.length === 0) {
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
                    msgDiv.innerHTML = msg.sender + ": " + msg.text + " (" + date + ")";

                    messageList.appendChild(msgDiv);
                });

                messageList.scrollTop = messageList.scrollHeight;
            })
            .catch(err => {
                console.error("Fehler beim Laden des Chats:", err);
                document.getElementById("messageList").textContent = "Fehler: " + err.message;
            });
    }

    // Follower beim Laden fetchen
    loadFollowers();
</script>
</body>
</html>
