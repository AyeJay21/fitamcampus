<!doctype html>
<html lang="de">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chat</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: 'Segoe UI', Arial, sans-serif;
            background: #f4f6fb;
        }

        .mainContainer {
            display: flex;
            flex-direction: row;
            gap: 2rem;
            max-width: 900px;
            margin: 2rem auto;
            background: #fff;
            border-radius: 16px;
            box-shadow: 0 4px 24px rgba(0, 0, 0, 0.08);
            padding: 2rem;
        }

        .followerContainer {
            min-width: 220px;
            border-right: 1px solid #e0e4ea;
            padding-right: 1rem;
        }

        .followerContainer h3 {
            margin-bottom: 1rem;
            font-size: 1.1rem;
            color: #3a3a3a;
        }

        #followerList {
            display: flex;
            flex-direction: column;
            gap: 0.5rem;
        }

        #followerList p {
            margin: 0;
            padding: 0.5rem 1rem;
            border-radius: 8px;
            background: #e9eef7;
            color: #2a2a2a;
            cursor: pointer;
            transition: background 0.2s;
        }

        #followerList p:hover {
            background: #d0d8e8;
        }

        .chatContainer {
            flex: 1;
            display: flex;
            flex-direction: column;
            min-width: 0;
        }

        #chatTitle {
            margin-bottom: 1rem;
            font-size: 1.1rem;
            color: #3a3a3a;
        }

        #messageList {
            flex: 1;
            overflow-y: auto;
            background: #f7f9fc;
            border-radius: 12px;
            padding: 1rem;
            min-height: 300px;
            max-height: 400px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.03);
            display: flex;
            flex-direction: column;
            gap: 0.5rem;
        }

        .message {
            max-width: 70%;
            padding: 0.7rem 1rem;
            border-radius: 16px;
            background: #e9eef7;
            color: #222;
            box-shadow: 0 1px 4px rgba(0, 0, 0, 0.04);
            word-break: break-word;
            position: relative;
            align-self: flex-start;
            margin-bottom: 0.2rem;
            font-size: 1rem;
            animation: fadeIn 0.3s;
        }

        .message.own {
            background: #cbe7ff;
            align-self: flex-end;
        }

        .message small {
            display: block;
            color: #888;
            font-size: 0.85em;
            margin-top: 0.3em;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        @media (max-width: 700px) {
            .mainContainer {
                flex-direction: column;
                padding: 1rem;
            }

            .chatContainer {
                margin-top: 1rem;
            }
        }
    </style>
</head>
<body>
<h1>Chat</h1>


<div class="mainContainer">
    <div class="followerContainer">
        <h3 class="username" id="currentUser">${username}</h3>
        <div id="followerList">Lade Follower...</div>
    </div>
    <div class="chatContainer">
        <h3 id="chatTitle">Wähle einen Kontakt</h3>
        <div id="messageList" class="no-chat">Keine Nachrichten</div>
        <form id="chatForm" style="display:none; margin-top:1rem; display:flex; gap:0.5rem;">
            <input id="chatInput" type="text" placeholder="Nachricht schreiben..." autocomplete="off"
                   style="flex:1; padding:0.7rem 1rem; border-radius:12px; border:1px solid #cbe7ff; font-size:1rem;"/>
            <button id="sendBtn" type="submit"
                    style="padding:0.7rem 1.2rem; border-radius:12px; background:#4fa3ff; color:#fff; border:none; font-size:1rem; cursor:pointer;">
                Senden
            </button>
        </form>
    </div>
</div>

<script>
    function followerUrlToReceiver(url) {
        if (typeof url === 'string' && url.includes('@')) {
            const [name, domain] = url.split('@');
            return "https://" + domain + "/users/" + name;
        }
        return url;
    }

    function urlToHandle(url) {
        if (url.startsWith('https://') && url.includes('/users/')) {
            const [domain, name] = url.replace('https://', '').split('/users/');
            return name + '@' + domain.replace('/', '');
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
        chatTitle.textContent = 'Chat mit ' + displayName;
        messageList.textContent = 'Lade Nachrichten...';
        const chatForm = document.getElementById('chatForm');
        chatForm.style.display = 'flex';
        chatForm.onsubmit = function (e) {
            e.preventDefault();
            const input = document.getElementById('chatInput');
            const text = input.value.trim();
            if (!text) return;
            const sendNoteRequest = {
                fromUser: username,
                targetHandle: urlToHandle(receiverUrl),
                message: text
            };
            console.log("FrontEnd fromUser: " + sendNoteRequest.fromUser + " targetHandle: " + sendNoteRequest.targetHandle + " message: " + sendNoteRequest.message)
            fetch('/activitypub/send-note', {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({sendNoteRequest})
            })
                .then(res => {
                    if (!res.ok) throw new Error('Fehler beim Senden');
                    input.value = '';
                    loadMessages(username, receiverUrl, displayName);
                })
                .catch(() => alert('Fehler beim Senden der Nachricht.'));
        };
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
                    // Zeit formatieren: z.B. 19.10.2025 12:44
                    let date = new Date(msg.timeStamp);
                    let formatted = date.getDate().toString().padStart(2, '0') + '.' +
                        (date.getMonth() + 1).toString().padStart(2, '0') + '.' +
                        date.getFullYear() + ' ' +
                        date.getHours().toString().padStart(2, '0') + ':' +
                        date.getMinutes().toString().padStart(2, '0');
                    let isOwn = (msg.sender == username);
                    div.className = 'message' + (isOwn ? ' own' : '');
                    div.innerHTML = '<b>' + (isOwn ? 'Du' : msg.sender) + ':</b> ' + msg.text + ' <small>' + formatted + '</small>';
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
