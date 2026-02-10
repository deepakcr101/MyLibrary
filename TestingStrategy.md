# Project Testing Strategy

This project employs a multi-layered testing strategy, adhering to the principles of the "Testing Pyramid." This ensures that we have fast, focused tests for individual units of logic and more comprehensive, slower tests for verifying that all the pieces work together correctly.


## Level 1: Unit Tests (The Base)

These are the fastest and most numerous tests. They focus on a single class or component in complete isolation.



* **Example: LibraryServiceTest.java**

  * Purpose: To verify the business logic within the LibraryService class without involving any external dependencies like the database or the web layer.

  * Key Techniques Used:

    * Mockito: We use Mockito.mock() to create "fake" versions of the BookRepository and AuthorRepository.

    * Dependency Injection: The mocked repositories are manually injected into the LibraryService instance being tested.

    * Behavior Verification: We use when(...).thenReturn(...) to define how the mocked repositories should behave. For example, when(authorRepository.findByName(...)) is told to return an Optional.empty() to test the "create new author" logic.

    * Assertions: We use assertEquals to check if the output is correct and verify(...) to ensure that the service interacted with its dependencies as expected (e.g., ensuring authorRepository.save() was never called when an author already existed).




## Level 2: Integration Tests (The Middle)

These tests verify that different layers of the application can communicate and function together. We have two types of integration tests in this project.


1.  **Sliced Integration Tests (@WebMvcTest)**

This type of test focuses only on the web layer.


* Example: LibraryControllerTest.java

    * Purpose: To test the LibraryController, including URL mappings, request/response serialization (JSON), and status codes, without starting the entire application or connecting to a database.

* Key Techniques Used:

    * @WebMvcTest(LibraryController.class): This Spring Boot annotation loads only the beans necessary for the web layer (like ObjectMapper, DispatcherServlet, etc.) and the specified controller.

    * @MockBean: We use this to inject a mock version of the LibraryService. This is crucial because we are only testing the controller's responsibility: to receive an HTTP request, call the correct service method, and return the result. We already tested the service's logic in the unit tests.

    * MockMvc: This powerful tool allows us to perform mock HTTP requests (.perform(get(...))) and make assertions on the HTTP response (.andExpect(status().isOk()), .andExpect(jsonPath(...))).



2.  **Full Integration Tests (@SpringBootTest)**

This is the most comprehensive type of test, verifying the application from end to end.



* Example: SecurityIntegrationTest.java

* Purpose: To test the full application stack, from receiving an HTTP request through the security filter chain, controller, service, and down to the actual database. This is where we verify that our ROLE_ADMIN and ROLE_USER security rules are correctly enforced.

* Key Techniques Used:

    * @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT): This annotation boots up the entire Spring application context and deploys it on a real, randomly-assigned port.

    * TestRestTemplate: A special web client provided by Spring for integration tests. It is automatically configured to make real HTTP calls to the test application running on the random port.

    * Real Dependencies: Unlike the other tests, this test requires the Neo4j Docker container to be running. It makes real database calls via the DataSeeder and the test methods themselves. This is why it was failing with Connection refused errors before the environment was correctly set up.

    * Assertions: We check the ResponseEntity for the correct HTTP status codes (HttpStatus.CREATED, HttpStatus.FORBIDDEN) to confirm our security rules are working.



### Summary of Test Classes

* **Test Class	Annotation Used	Purpose	What's Mocked?**
* LibraryServiceTest	(None - plain JUnit)	Unit Test the service's business logic.	BookRepository, AuthorRepository
* LibraryControllerTest	@WebMvcTest	Slice Test the web layer (controller, JSON).	LibraryService
* SecurityIntegrationTest	@SpringBootTest	Full Integration Test of the entire application, including security.	Nothing. Uses real HTTP and a real database.