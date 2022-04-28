const reimbursementHtml = (amount, discription, submitDate) => `
	<div class="reimbursement">
		<p class="total">${amount}</p>
		<p class="discription">${discription}</p>
		<p>${submitDate}</p>
</div>
`;

const getStatus = () => {
	const statusString = document
		.querySelector(".status.selected")
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
};

const getReimbursementsByStatus = async status => {
	const response = await fetch(
		`http://localhost:8080/reimbursements?status=${status}`,
		{
			method: "GET",
			headers: {
				Authorizaiton: `Bearer ${token}`
			}
		}
	);

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
};

function logout() {
	//We remove the authToken from localStorage
	localStorage.clear();
	//We then redirect the user to the login page
	window.location.href = "./login.html";
}

async function displayUser() {
	let response = await getUserRequest();
	let user = await response.json();

	document.querySelector("#username").innerText =
		user.firstname + " " + user.lastname;
}

async function displayReimbursements() {
	let status = getStatus();
	let { food, training, travel, other, lodging } =
		await getReimbursementsByStatus(status);

	let html = "";
	food.forEach(reimbursement => {
		html += reimbursementHtml(
			reimbursement.amount,
			reimbursement.description,
			reimbursement.submitDate
		);
	});
	document.querySelector(".food").innerHTML = html;
	html = "";
	lodging.forEach(reimbursement => {
		html += reimbursementHtml(
			reimbursement.amount,
			reimbursement.description,
			reimbursement.submitDate
		);
	});
	document.querySelector(".lodging").innerHTML = html;
	html = "";
	travel.forEach(reimbursement => {
		html += reimbursementHtml(
			reimbursement.amount,
			reimbursement.description,
			reimbursement.submitDate
		);
	});
	document.querySelector(".travel").innerHTML = html;
	html = "";
	training.forEach(reimbursement => {
		html += reimbursementHtml(
			reimbursement.amount,
			reimbursement.description,
			reimbursement.submitDate
		);
	});
	document.querySelector(".training").innerHTML = html;

	html = "";
	other.forEach(reimbursement => {
		html += reimbursementHtml(
			reimbursement.amount,
			reimbursement.description,
			reimbursement.submitDate
		);
	});
	document.querySelector(".other").innerHTML = html;
}

let token = localStorage.getItem("token");

if (!token) {
	window.location.href = "./login.html";
}

displayUser();
displayReimbursements();
