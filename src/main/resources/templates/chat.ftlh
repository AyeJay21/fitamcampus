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
                    // Redirect to the chat page with the followerUrl as query parameter 'receiver'
                    const targetUrl = `/users/${username}/chats?receiver=${follower.followerUrl}`;
                    window.location.href = targetUrl;
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
