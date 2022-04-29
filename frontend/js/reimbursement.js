function submitReimbursement() {
	const amount = document.getElementById("amount");
	const description = document.getElementById("discription");
	const select = document.getElementById("reimbursementType");
	const reimbursementTypeId = select.options[select.selectedIndex].value;

	const reimbursement = {
		amount: amount.value,
		description: description.value,
		reimbursementTypeId: reimbursementTypeId
	};

	console.log(reimbursement);

	fetch("http://localhost:8080/api/reimbursements", {
		method: "POST",
		headers: {
			Authorization: `Bearer ${localStorage.getItem("token")}`,
			"Content-Type": "application/json"
		},
		body: JSON.stringify(reimbursement)
	})
		.then(response => {
			if (response.status >= 200 && response.status < 300) {
				alert("Reimbursement submitted");
				cleanForm();
				return response.json();
			} else {
				alert("Error submitting reimbursement");
			}
		})
		.then(data => console.log(data))
		.catch(err => console.log(err));
}

function cleanForm() {
	document.getElementById("amount").value = "";
	document.getElementById("discription").value = "";
	document.getElementById("reimbursementType").value = "";
}
