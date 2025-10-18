<!doctype html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chat</title>
    <style>
        body { margin: 0; padding: 1rem; font-family: sans-serif; }
        .follower p { margin: 0.25rem 0; }


    </style>
</head>
<body>
<h1>Chat</h1>

<div class="mainContainer">
    <div class="follower">
        <h3 class="username">${username}</h3>
        <div id="followerList">Lade Follower...</div>
    </div>
    <div class="chat">
        <#if chatMessages??>
        <#list chatMessages as chatMessage>
            <p>${chatMessage.text}</p>
        </#list>
        </#if>
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
                // display a readable label if available
                p.textContent = follower.username || follower.followerUrl || follower.id || "Unbekannt";
                p.classList.add('follower-item');
                // store the receiver (actor URL or identifier)
                const receiver = follower.followerUrl || follower.id || follower.username;
                p.dataset.receiver = receiver;
                p.style.cursor = 'pointer';
                p.addEventListener('click', () => {
                    // navigate to the chat view and include the receiver as query param
                    const url = `/users/${encodeURIComponent(username)}/chats?receiver=${encodeURIComponent(receiver)}`;
                    window.location.href = url;
                });
                listDiv.appendChild(p);
            });
        })
        .catch(err => {
            listDiv.textContent = "Fehler: " + err.message;
        });
    // The page will be reloaded when the user clicks a follower and the server
    // will render the chat view with the optional ?receiver=... query parameter.
</script>
</body>
</html>
