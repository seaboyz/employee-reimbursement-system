let token = localStorage.getItem("token");

if (!token) {
	window.location.href = "./login.html";
}

function logout() {
	//We remove the authToken from localStorage
	localStorage.clear();
	//We then redirect the user to the login page
	window.location.href = "./login.html";
}

async function displayUser() {
	let response = await getUserRequest();
	let json = await response.json();
}
