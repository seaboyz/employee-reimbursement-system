# Think about if it is a team object, and how to break the project into features, then branch the object base on features.

### Console interaction using the scanner class as user interface.

- scanner
- input logic
- output

### Rest api handle http request

- login (POST)
  - /api/user/login
- register (POST)
  - /api/user
- profile (GET/PUT)
  - /api/user/profile

### Login flow
@desc Auth user & get token
@route Post /api/user/login
@body username/email password
@access public
<img src="./login-flowchart.svg" width=400>

