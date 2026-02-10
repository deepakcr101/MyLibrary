
---

# What is System Testing? 

**System Testing** is a level of testing that validates a complete and fully integrated software product. Its purpose is to evaluate the system's compliance with its specified requirements. Unlike unit or integration testing, which are done from a developer's perspective (white-box testing), system testing is almost always a form of **black-box testing**.

*   **Black-Box Testing:** The tester does not know or care about the internal structure, code, or logic of the system. They focus solely on the **inputs and outputs**. The system is treated as an opaque "black box." The goal is to see if, given a certain input (like an API call or a user clicking a button), the system produces the expected output (the correct JSON response, a change in the UI, a record in the database).

*   **The "System" under test:** This is not just a few classes. It is the **entire, deployed application**, running in an environment that is as close to the final production environment as possible. It includes the web server, the application code, the database, and any other external services it depends on.

**The Primary Goal:** To find defects that only manifest when the *entire system* is running together. These are often non-functional requirements or emergent behaviors that are impossible to find at lower levels of testing.

**In our project, how is it different from the `@SpringBootTest` integration test?**
Our `SecurityIntegrationTest` is a *developer-level system test*. It runs the full application stack but is still executed from within the IDE or Maven build process. A true, formal System Test is typically performed by a dedicated QA (Quality Assurance) team on a deployed application in a staging environment.

However, **our `SecurityIntegrationTest` is the closest we have to a System Test**. It simulates an external actor (the `TestRestTemplate`) interacting with the complete, running system via its public API interface, and it verifies the behavior against a real, running database. It is testing the system as a whole from the outside in.

---

### System Testing: A Detailed Study Note

System tests are not a single type of test but a category that includes many different testing activities.

#### Must-Know Types of System Testing

1.  **Functional Testing:**
    *   **Goal:** To verify that the features and functionality of the application work according to the business requirements. This is the most common type.
    *   **Example for Our Project:**
        *   Create a test plan document.
        *   **Test Case 1:** Authenticate as an Admin. Send a `POST` request to `/api/books`. Verify the response is `201 Created` and the new book is in the database.
        *   **Test Case 2:** Authenticate as a User. Send a `POST` request to `/api/books`. Verify the response is `403 Forbidden`.
        *   **Test Case 3:** Authenticate as a User. Send a `GET` request to `/api/books`. Verify the response is `200 OK` and contains a list of books.
    *   **How we achieved this:** Our `SecurityIntegrationTest` performs exactly these functional tests, just in an automated way.

2.  **Performance Testing:**
    *   **Goal:** To evaluate how the system performs in terms of responsiveness and stability under a particular workload.
    *   **Sub-Types:**
        *   **Load Testing:** Simulates the expected number of concurrent users to see how the system behaves. (e.g., "Can the system handle 100 users per second viewing books?").
        *   **Stress Testing:** Pushes the system beyond its expected limits to find its breaking point and see how it recovers. (e.g., "At what point does the database connection pool get exhausted?").
    *   **Tools:** JMeter, Gatling, k6.
    *   **How to apply to our project:** You could use JMeter to send 500 concurrent `GET /api/books` requests and measure the average response time and error rate.

3.  **Security Testing:**
    *   **Goal:** To uncover vulnerabilities in the system and ensure that its data and resources are protected from malicious attacks.
    *   **Example for Our Project:**
        *   **Penetration Testing:** An ethical hacker tries to bypass our security. Can they perform an SQL/Cypher injection through the `authorName` field? Can they access the `/api/books` POST endpoint without any authentication at all?
        *   **Vulnerability Scanning:** Use automated tools (like OWASP ZAP) to scan for common vulnerabilities.
    *   **How we partially achieved this:** Our `SecurityIntegrationTest` performs a basic form of security testing by confirming that a `ROLE_USER` cannot access an admin-only endpoint. Formal security testing is a much deeper specialty.

4.  **Usability Testing:**
    *   **Goal:** To evaluate how easy and intuitive the system is for a real end-user. This is primarily for systems with a user interface (UI).
    *   **Example for Our Project:** If we built a web frontend, we would sit a user down, ask them to "add a new book," and watch where they struggle or get confused.

5.  **Recovery Testing:**
    *   **Goal:** To verify how well the system recovers from crashes, hardware failures, or other catastrophic problems.
    *   **Example for Our Project:** While the application is running, kill the Neo4j Docker container (`docker stop my-neo4j-db`). What happens? Does the application crash? Does it start returning `500 Internal Server Error`? Now, restart the container (`docker start my-neo4j-db`). Does the application automatically reconnect and start working again? A resilient system, like those Ciena builds, should recover gracefully.

---

### Other General Testing Stuffs You Should Know

1.  **The Testing Pyramid (Revisited):** It's the most important guiding principle. Don't try to cover everything with slow E2E/System tests. Build a massive base of fast unit tests, a solid layer of integration tests, and a small, targeted set of system tests for critical user journeys.

2.  **White-Box vs. Black-Box Testing:**
    *   **White-Box:** You know the internal code structure. Unit tests are almost always white-box.
    *   **Black-Box:** You have no knowledge of the internals. System and E2E tests are black-box. Integration tests can be a mix (often called "Gray-Box").

3.  **Test-Driven Development (TDD):**
    *   The "Red-Green-Refactor" cycle.
    1.  **Red:** Write a failing test for a feature that doesn't exist yet.
    2.  **Green:** Write the absolute minimum amount of code required to make the test pass.
    3.  **Refactor:** Clean up and improve the code you just wrote, knowing that your test will protect you from breaking it.
    *   This is a discipline that leads to higher quality, better-designed, and fully tested code from the start.

4.  **Behavior-Driven Development (BDD):**
    *   An extension of TDD that focuses on writing tests in a natural, human-readable language. It uses a `Given-When-Then` format.
    *   **Example:**
        *   **Given** I am an authenticated user with the "ADMIN" role
        *   **When** I send a POST request to "/api/books" with a valid book
        *   **Then** the system should respond with a "201 Created" status code
    *   This is what our `SecurityIntegrationTest` is actually doing, just written in Java code. BDD frameworks like Cucumber help bridge the gap between technical testers and non-technical business stakeholders.

5.  **Continuous Integration / Continuous Deployment (CI/CD):**
    *   This is where testing delivers its ultimate value. A CI/CD pipeline (using tools like Jenkins, GitLab CI, GitHub Actions) automatically runs your entire test suite every time a developer pushes new code.
    *   If any test fails, the build is marked as "broken," and the code is prevented from being deployed. This creates a safety net that ensures that bad code never reaches production. Your `mvn clean install` command is the heart of what a CI server would run.