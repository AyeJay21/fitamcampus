<!DOCTYPE html>
<html lang="de">
<head>
	<meta charset="UTF-8">
	<title>Follower-Anfragen</title>
	<style>
		body {
			font-family: 'Segoe UI', Arial, sans-serif;
			background: #f4f6fb;
			margin: 0;
			padding: 0;
		}
		.container {
			max-width: 500px;
			margin: 40px auto;
			background: #fff;
			border-radius: 12px;
			box-shadow: 0 4px 16px rgba(0,0,0,0.08);
			padding: 2rem;
		}
		h2 {
			margin-top: 0;
			color: #4fa3ff;
		}
		.input-row {
			display: flex;
			gap: 0.5rem;
			margin-bottom: 1.5rem;
		}
		.input-row input {
			flex: 1;
			padding: 0.7rem 1rem;
			border-radius: 8px;
			border: 1px solid #cbe7ff;
			font-size: 1rem;
		}
		.input-row button {
			padding: 0.7rem 1.2rem;
			border-radius: 8px;
			background: #4fa3ff;
			color: #fff;
			border: none;
			font-size: 1rem;
			cursor: pointer;
		}
		.requests {
			margin-top: 2rem;
		}
		.request-item {
			background: #e9eef7;
			border-radius: 8px;
			padding: 0.7rem 1rem;
			margin-bottom: 0.7rem;
			display: flex;
			align-items: center;
			justify-content: space-between;
		}
		.request-item button {
			background: #4fa3ff;
			color: #fff;
			border: none;
			border-radius: 6px;
			padding: 0.4rem 1rem;
			cursor: pointer;
		}
	</style>
</head>
<body>
<div class="container">
	<h2>Follower-Anfrage senden</h2>
	<form class="input-row" id="sendFollowForm" autocomplete="off">
		<input type="text" id="followUserInput" placeholder="User@domain.tld" required />
		<button type="submit">Anfrage senden</button>
	</form>

	<div class="requests">
		<h2>Eingehende Anfragen</h2>
		<div id="incomingRequests">

		</div>
	</div>
</div>

<script>
document.getElementById('sendFollowForm').onsubmit = function(e) {
	e.preventDefault();
	const user = document.getElementById('followUserInput').value.trim();
	if (!user) return;
	alert('Würde Anfrage an ' + user + ' senden (Endpoint folgt)');
	document.getElementById('followUserInput').value = '';
};

const incomingDiv = document.getElementById('incomingRequests');
const username = (document.getElementById('currentUser') && document.getElementById('currentUser').textContent) || '';

const userForRequest = username;

function loadFollowRequests() {
	console.log("UserForRequest " + userForRequest)
	incomingDiv.innerHTML = 'Lade Anfragen...';
	fetch(`/users/`+  userForRequest + `/followRequests`)
		.then(res => res.json())
		.then(requests => {
			if (!Array.isArray(requests) || requests.length === 0) {
				incomingDiv.innerHTML = '<em>Keine offenen Anfragen.</em>';
				return;
			}
			incomingDiv.innerHTML = '';
			requests.forEach(req => {
				const div = document.createElement('div');
				div.className = 'request-item';
				div.innerHTML = `<span>` + req.username + `</span><span>` + req.followerUrl + `</span>`;
				incomingDiv.appendChild(div);
			});
		})
		.catch(() => {
			incomingDiv.innerHTML = '<em>Fehler beim Laden der Anfragen.</em>';
		});
}

loadFollowRequests();
</script>
</body>
</html>
