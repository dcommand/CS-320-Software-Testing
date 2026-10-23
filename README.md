# CS 320 Software Testing Automation and Quality Assurance

This portfolio contains the contact service and JUnit tests from Project One and the summary and reflections report from Project Two. The projects demonstrate translating requirements into testable behavior, checking invalid inputs, and evaluating the strengths and limits of automated tests.

## Portfolio artifacts

- [Contact.java](src/main/java/contact/Contact.java): Contact object with an immutable ID and validated name, phone, and address fields.
- [ContactService.java](src/main/java/contact/ContactService.java): In-memory storage supporting unique contact IDs, deletion, and updates to permitted fields.
- [ContactTest.java](src/test/java/contact/ContactTest.java): Tests for field validation, boundary values, ID immutability, and permitted updates.
- [ContactServiceTest.java](src/test/java/contact/ContactServiceTest.java): Tests for adding, deleting, updating, duplicate rejection, and preservation of unrelated data.
- [Project Two summary and reflections report](reports/CS320_Project_Two_Report.docx): Analysis of testing approaches, coverage, efficiency, caution, bias, and professional discipline.

## How can I ensure that my code program or software is functional and secure

I begin with explicit requirements and use tests to verify both intended behavior and rejected inputs. In the contact service, I test maximum field lengths, nulls, malformed phone numbers, duplicate IDs, and updates that must leave unrelated data unchanged. The combined Project One suite passed 121 test invocations and achieved 100% line and branch coverage across its six application classes. The two contact application classes included here also achieved 100% line and branch coverage. Coverage shows which code executed; it does not prove correctness or security.

For security, validation is one layer. A deployed application also needs appropriate authentication, authorization, safe handling of personal information, dependency review, and tests of misuse scenarios. This assignment uses in-memory storage and has no authentication or UI, so I would not present its unit-test results as evidence that a production system is secure. I would add security and integration checks as those capabilities are introduced.

## How do I interpret user needs and incorporate them into a program

I translate user needs into specific acceptance criteria before deciding how to implement them. For example, the contact requirements distinguish an ID that cannot change from names, phone numbers, and addresses that can. I reflect that distinction in the class interface and test it explicitly. I also verify exact limits and invalid values rather than assuming that successful object creation satisfies every requirement. When wording is ambiguous, I seek clarification rather than silently adding restrictions. Keeping expected results tied to the requirements helps prevent the implementation and its tests from sharing the same incorrect assumption.

## How do I approach designing software

I separate responsibilities and keep the design proportional to the problem. Contact owns its field validation, while ContactService manages storage and operations by ID. A shared validation helper reduces duplicated length checks. I design tests alongside these responsibilities and check how they interact, including what remains stored after an operation fails. Small, focused components make changes easier to understand and test. As a system grows, I would review new dependencies and risks, add integration tests, and retain regression checks during refactoring instead of treating passing tests as permission to stop reviewing the design.

## Test environment and scope

The Java files declare the `contact` package and are organized under matching package directories. Tests use JUnit Jupiter, including parameterized-test support. The original verification compiled the application and test files targeting Java 17, used JUnit 5.10.2 and JaCoCo 0.8.14, and executed on OpenJDK 25.0.3. Only the contact portion of Project One is included here, as required by the portfolio assignment; the 121-test figure describes the complete three-service project.

## AI acknowledgment

OpenAI Codex assisted with implementation, unit tests, report drafting and revision, and preparation of this portfolio README. Anthropic Claude assisted with reviewing the Project Two report. The report discusses why AI-generated implementation and tests can share blind spots and why requirements-based review remains necessary.
