const reimbursementHtml = (id, amount, discription) => `
	<div data-id=${id} class="reimbursement">
		<p class="total">$${amount}</p>
		<p class="discription">${discription}</p>
		<p style="display: flex; gap: 5px">
			<span style="cursor: pointer;" id="yes" onclick="approve(event)">✅</span>
			<span style="cursor: pointer;" onclick="deny(event)" id="no">❌</span>
		</p>
</div>
`;
const URL = "http://localhost:8080/api/reimbursements";

init();

function init() {
	verifyToken();

	displayUser();

	displayReimbursements();
}

function verifyToken() {
	let token = localStorage.getItem("token");

	if (!token) {
		window.location.href = "./login.html";
	}
}

function filterReimburseStatus() {
	const statusString = document
		.querySelector(".selected")
		.innerText.toLowerCase();

	if (statusString === "pending") {
		return 1;
	}
	if (statusString === "approved") {
		return 2;
	}
	if (statusString === "denied") {
		return 3;
	}
}

async function getReimbursementsByStatus(status) {
	const token = localStorage.getItem("token");
	const response = await fetch(`${URL}?status=${status}`, {
		method: "GET",
		headers: {
			Accept: "*/*",
			Authorization: `Bearer ${token}`
		}
	});

	if (response.status >= 400) {
		alert("Error loading reimbursements");
		return;
	}
	const reimbursements = await response.json();
	let html = "";
	const food = reimbursements.filter(
		reimbursement => reimbursement.reimbursementTypeId === 4
	);
	const lodging = reimbursements.filter(
		reimbursement => reimbursement.reimbursementTypeId === 5
	);
	const travel = reimbursements.filter(
		reimbursement => reimbursement.reimbursementTypeId === 1
	);
	const other = reimbursements.filter(
		reimbursement => reimbursement.reimbursementTypeId === 6
	);
	const training = reimbursements.filter(
		reimbursement => reimbursement.reimbursementTypeId === 7
	);
	return {
		food,
		lodging,
		travel,
		training,
		other
	};
}

function logout() {
	//We remove the authToken from localStorage
	localStorage.clear();
	//We then redirect the user to the login page
	window.location.href = "./login.html";
}

async function displayUser() {
	let response = await getUserRequest();
	let user = await response.json();

	if (user.username === "admin") {
		document.querySelector("#username").innerText = user.username;
		// remove plus sign
		document
			.querySelectorAll(".plus-sign")
			.forEach(element => (element.style.display = "none"));
	} else {
		document.querySelector("#username").innerText =
			user.firstname + " " + user.lastname;
	}
}

async function displayReimbursements() {
	let status = filterReimburseStatus();
	let { food, training, travel, other, lodging } =
		await getReimbursementsByStatus(status);

	let html = "";
	food.forEach(reimbursement => {
		html += reimbursementHtml(
			reimbursement.id,
			reimbursement.amount,
			reimbursement.description
		);
	});
	document.querySelector(".food .reimbursementList").innerHTML = html;
	html = "";
	lodging.forEach(reimbursement => {
		html += reimbursementHtml(
			reimbursement.id,
			reimbursement.amount,
			reimbursement.description
		);
	});
	document.querySelector(".lodging .reimbursementList").innerHTML = html;
	html = "";
	travel.forEach(reimbursement => {
		html += reimbursementHtml(
			reimbursement.id,
			reimbursement.amount,
			reimbursement.description
		);
	});
	document.querySelector(".travel .reimbursementList").innerHTML = html;
	html = "";
	training.forEach(reimbursement => {
		html += reimbursementHtml(
			reimbursement.id,
			reimbursement.amount,
			reimbursement.description
		);
	});
	document.querySelector(".training .reimbursementList").innerHTML = html;

	html = "";
	other.forEach(reimbursement => {
		html += reimbursementHtml(
			reimbursement.id,
			reimbursement.amount,
			reimbursement.description
		);
	});
	document.querySelector(".other .reimbursementList").innerHTML = html;
}

async function approve(event) {
	let token = localStorage.getItem("token");
	let id = event.target.parentElement.parentElement.dataset.id;
	let response = await fetch(`${URL}/${id}?status=2`, {
		method: "PUT",
		headers: {
			Accept: "*/*",
			Authorization: `Bearer ${token}`,
			"Content-Type": "application/json"
		},
		body: JSON.stringify({
			status: 2
		})
	});

	if (response.status >= 400) {
		alert("Error approving reimbursement");
		return;
	}
	displayReimbursements();
}

async function deny(event) {
	let token = localStorage.getItem("token");
	let id = event.target.parentElement.parentElement.dataset.id;
	let response = await fetch(`${URL}/${id}?status=2`, {
		method: "PUT",
		headers: {
			Accept: "*/*",
			Authorization: `Bearer ${token}`,
			"Content-Type": "application/json"
		},
		body: JSON.stringify({
			status: 3
		})
	});
	if (response.status >= 400) {
		alert("Error denying reimbursement");
		return;
	}
	displayReimbursements();
}

function changeStatus(event) {
	console.log(event.target.innerText);
	document.querySelector(".selected").classList.remove("selected");
	event.target.classList.add("selected");

	displayReimbursements();
}
