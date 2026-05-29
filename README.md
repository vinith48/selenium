# Selenium Simple Test Project

A minimal Java + Selenium project with a couple of basic UI test cases.

## What is included

- JUnit 5 test framework
- Selenium WebDriver
- WebDriverManager (auto-manages browser drivers)
- 2 simple tests against Selenium's demo web form page

## Project structure

- `pom.xml` - Maven dependencies and test plugin setup
- `src/test/java/com/example/selenium/BaseTest.java` - browser setup/teardown
- `src/test/java/com/example/selenium/SimpleSeleniumTests.java` - sample test cases

## Prerequisites

- Java 17+
- Maven 3.9+
- Chrome or Edge installed

## Run tests

Headless Chrome (default):

```bash
mvn test
```

Run with visible browser window:

```bash
mvn -Dheadless=false test
```

Run on Edge:

```bash
mvn -Dbrowser=edge test
```

Run only this class:

```bash
mvn -Dtest=SimpleSeleniumTests test
```

## Notes

- The tests target a public Selenium demo page: `https://www.selenium.dev/selenium/web/web-form.html`
- If your corporate network blocks external downloads, preinstall browser drivers and disable WebDriverManager usage.

