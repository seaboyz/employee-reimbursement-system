const email = document.querySelector("#email");
const password = document.querySelector("#password");
const confirmPassword = document.querySelector("#confirmPassword");
const firstname = document.querySelector("#firstname");
const lastname = document.querySelector("#lastname");
const registerBtn = document.querySelector("button");

registerBtn.addEventListener("click", async () => {
	validateInputs();
  const userInfo = {
    email: email.value,
    password: password.value,
    firstname: firstname.value,
    lastname: lastname.value
    
  }
});

function validateInputs() {
	if (password.value !== confirmPassword.value) {
		alert("Passwords do not match!");
		return;
	}

	if (password.value.length < 6) {
		alert("Password must be at least 6 characters!");
		return;
	}

	if (!validateEmail(email.value)) {
		alert("Invalid email!");
		return;
	}

	if (firstname.value.length < 2) {
		alert("First name must be at least 2 characters!");
		return;
	}

	if (lastname.value.length < 2) {
		alert("Last name must be at least 2 characters!");
		return;
	}
}

function validateEmail(email) {
	let re =
		/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	return re.test(String(email).toLowerCase());
}
