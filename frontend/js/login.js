const token = localStorage.getItem("token");
if (token) {
	location.href = "./index.html";
}

async function submitForm() {
	/*
         Build the object we will transfer in our API call. Grab the values from
         the input elements above.
         */
	let credentials = {
		username: document.getElementById("username").value,
		password: document.getElementById("password").value
	};

	let response = await loginRequest(credentials);

	if (response.status == 200) {
		let json = await response.json();

		let token = json.token;
		let userId = json.id;
		localStorage.setItem("userId", userId);
		localStorage.setItem("token", token);

		//navigate the window to the landing page
		window.location.href = "./index.html";
	} else {
		alert("Unable to log in! Check username and password!");
	}
}
