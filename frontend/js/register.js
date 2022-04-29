const email = document.querySelector("#email");
const username = document.querySelector("#username");
const password = document.querySelector("#password");
const confirmPassword = document.querySelector("#confirmPassword");
const firstname = document.querySelector("#firstname");
const lastname = document.querySelector("#lastname");
const registerBtn = document.querySelector("button");

registerBtn.addEventListener("click", async () => {
	if (!validateInputs()) {
		return;
	}
	const userInfo = {
		email: email.value,
		username: username.value,
		password: password.value,
		firstname: firstname.value,
		lastname: lastname.value
	};

	try {
		const response = await registerRequest(userInfo);
		if (response.status >= 200 && response.status < 300) {
			alert("Registration successful!");
			window.location.href = "./login.html";
		} else {
			alert("Registration failed!");
		}
	} catch (error) {
		console.log(error);
	}
});

function validateInputs() {
	if (password.value !== confirmPassword.value) {
		alert("Passwords do not match!");
		return false;
	}

	if (password.value.length < 6) {
		alert("Password must be at least 6 characters!");
		return false;
	}

	if (!validateEmail(email.value)) {
		alert("Invalid email!");
		return false;
	}
	if (username.value.length < 6) {
		alert("Username must be at least 6 characters!");
		return false;
	}

	if (firstname.value.length < 2) {
		alert("First name must be at least 2 characters!");
		return false;
	}

	if (lastname.value.length < 2) {
		alert("Last name must be at least 2 characters!");
		return;
	}
	return true;
}

function validateEmail(email) {
	let re =
		/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	return re.test(String(email).toLowerCase());
}
