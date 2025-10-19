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

</div>

<script>
    // Username wird dynamisch aus dem DOM gelesen
    const listDiv = document.getElementById("followerList");

    fetch(`/users/${username}/followers`)
        .then(res => {
            if (!res.ok) throw new Error("Fehler beim Laden der Follower");
            return res.json();
        })
        .then(followers => {

            if (followers.length === 0) {
                listDiv.textContent = "Keine Follower gefunden.";
                return;
            }
            followers.forEach(follower => {
                const p = document.createElement("p");

                p.textContent = follower.followerUrl;
                p.dataset.followerUrl = follower.followerUrl;
                p.classList.add('follower');
                listDiv.appendChild(p);
            });
        })
        .catch(err => {
            listDiv.textContent = "Fehler: " + err.message;
        });

function followerUrlToReceiver(url) {
    if (typeof url === 'string' && url.includes('@')) {
        const [name, domain] = url.split('@');
    return "https://" + domain + "/users/" + name;
    }
    return url;
}

const currentUser = document.getElementById('currentUser');
const followerList = document.getElementById('followerList');
const chatTitle = document.getElementById('chatTitle');
const messageList = document.getElementById('messageList');

const username = currentUser.textContent === 'Laden...' ? null : currentUser.textContent;
if (username) {
    loadFollowers(username);
}

function loadFollowers(username) {
    followerList.textContent = 'Lade Follower...';
    fetch(`/users/${username}/followers`)
        .then(res => res.json())
        .then(data => {
            let followers = data.orderedItems || data;
            if (!Array.isArray(followers)) {
                followerList.textContent = 'Keine Follower gefunden.';
                return;
            }
            followerList.innerHTML = '';
            followers.forEach(f => {
                let display = f.followerUrl || f;
                let receiverUrl = followerUrlToReceiver(display);
                let el = document.createElement('p');
                el.textContent = display;
                el.style.cursor = 'pointer';
                el.onclick = () => {
                    loadMessages(username, receiverUrl, display);
                };
                followerList.appendChild(el);
            });
        })
        .catch(() => {
            followerList.textContent = 'Fehler beim Laden der Follower.';
        });
}

function loadMessages(username, receiverUrl, displayName) {
    chatTitle.textContent = `Chat mit displayName` + displayName;
    messageList.textContent = 'Lade Nachrichten...';
    fetch(`/users/${username}/chats?receiver=` + encodeURIComponent(receiverUrl))
        .then(res => res.json())
        .then(msgs => {
            if (!Array.isArray(msgs) || msgs.length === 0) {
                messageList.textContent = 'Keine Nachrichten.';
                return;
            }
            messageList.innerHTML = '';
            msgs.forEach(msg => {
                let div = document.createElement('div');
                div.className = 'message';
                div.innerHTML = "<b>" + (msg.sender == username ? "Du" : msg.sender) + ":</b> " + msg.text + " <br><small>" + msg.timeStamp + "</small>";
                messageList.appendChild(div);
            });
        })
        .catch(() => {
            messageList.textContent = 'Fehler beim Laden der Nachrichten.';
        });
}
</script>

</body>
</html>
