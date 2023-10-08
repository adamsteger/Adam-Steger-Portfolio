# Gate Guard

###
Gate Guard is a token-based authentication application for gated communities. It allows members in the community to manage electronic passes for their guests efficiently, and it automatically sends their guests an email with a QR code that opens the gate to their community. Check out the wiki for more information about Gate Guard.

### My Contributions
My work was done primarily in the backend of this applcation.  I am responsible for most of the code in the `gate-guard/src/main/java/com/capstone/lifesabit/gateguard` directory, outside of the login system and authomatic email functionality.  I led database design for this project, helped design HTTP requests/responses to the frontend, and SQL queries to the database for all of the middleware of the applcation.

### First-time setup instructions
1. Ensure that you have `npm` and `maven` installed
2. Clone the repository, and `cd` into the `gate-guard` directory.
3. Run `mvn package`
4. `cd` into the frontend directory and run `npm install`

### Build instructions
- To build backend changes, simply run `mvn spring-boot:run` in the `gate-guard` directory again.
  - If you are already running `mvn spring-boot:run`, kill the existing process and start it again.
- To build frontend changes, leave the npm daemon running. It will automatically re-compile and re-deploy after you make changes.

### Run instructions
- Backend: Run `mvn spring-boot:run` in the `gate-guard` directory
- Frontend: Run `npm run start` in the `frontend` directory
- The website can be accessed at `localhost:3000`

### Connect to the Virtual Linux Server Enviroment via SSH
- In a terminal do the command: ssh lab@146.190.222.5
- Enter the password: tooR1Root.
- This will ssh you into the virtual enviroment.

## Testing
### Unit Tests
- To run the unit tests, clone the repository and `cd` into the `gate-guard` directory
- Ensure that you have a postgresql server running on port 5432, with a user named `postgres` with the password `password1234` who has access to a database name `GateGuard`.
- Type `mvn clean test`
- The test files are located in the `gate-guard/src/test` folder and are called `GateGuardApplicationTest.java` and `SessionTests.java`

### Behavioral Tests

- To run the behavioral tests, clone the repository and `cd` into the `frontend` directory
- Type the command `npm run test`
- The test file is located in the `gate-guard/frontend/src/components/_tests_` folder and is called `pass.test.js`
