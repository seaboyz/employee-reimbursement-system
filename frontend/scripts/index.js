"use strict";

const activityElements = document.querySelectorAll(".activity");

async function init() {
	const data = await fetch("./data.json").then(response => response.json());

	document.querySelectorAll(".user li").forEach(_ =>
		_.addEventListener("click", e => {
			document
				.querySelectorAll(".user li")
				.forEach(_ => _.classList.remove("selected"));

			e.target.classList.add("selected");

			const timeframe = e.target.textContent.toLowerCase();

			data
				.map(_ => ({
					activity: _.title.toLowerCase(),
					..._.timeframes[timeframe],
					timeframe
				}))
				.forEach(_ => {
					const activity = _.activity.replace(" ", "-");
					const current = _.current;
					const previous = _.previous;
					let timeframe;
					switch (_.timeframe) {
						case "daily":
							timeframe = "Day";
							break;
						case "monthly":
							timeframe = "Month";
							break;
						case "weekly":
							timeframe = "Week";
							break;

						default:
							timeframe = "";
							break;
					}
					document.querySelector(
						`.${activity} .hours`
					).textContent = `${current}$0.00`;
					document.querySelector(
						`.${activity} .last`
					).textContent = `Last ${timeframe} - ${previous}$0.00`;
				});
		})
	);
}

let token = localStorage.getItem("token");

function logout() {
	//We remove the authToken from localStorage
	localStorage.clear();
	//We then redirect the user to the login page
	window.location.href = "./login.html";
}

async function displayUser() {
	let response = await getUserRequest();
	let json = await response.json();

	let paragraph = document.getElementById("pageContent");
	paragraph.innerHTML += `
					<h1>Welcome, ${json.username}!</h1>
					<p>You are logged in!</p>
					<p>Email: ${json.email}</p>
					<p>First Name: ${json.firstname}</p>
					<p>Last Name: ${json.lastname}</p>
					<hr />
					<button onclick="logout()">Logout</button>
				`;
}

window.onload = init;
