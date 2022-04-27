#### User Stories

##### 1. As a guest, I can register for a new account
  GET @http://localhost:8080/api/users
  Basic Authentication

`let headersList = {`
 `"Accept": "*/*",`
 `"User-Agent": "Thunder Client (https://www.thunderclient.com)",`
 `"Authorization": "Basic dGVzdDoxMjM0NTY="`
`}`

`fetch("http://localhost:8080/api/users", { `
  `method: "GET",`
  `headers: headersList`
`}).then(function(response) {`
  `return response.text();`
`}).then(function(data) {`
 ` console.log(data);`
`})`