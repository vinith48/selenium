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

- Java 21+
- Maven 3.9+
- Chrome or Edge installed

## Running on a remote Selenium server

This project can run either locally or against a browser host in another machine/container.

## Dockerized JDK 21 test runner

This repo includes a `Dockerfile` that packages Maven + JDK 21 so the suite can run in a container while the browser stays on a separate Selenium host.

### Build the runner image

```bash
docker build -t selenium-suite-runner .
```

### Run the full suite against a remote Selenium server

```bash
docker run --rm \
  -e SELENIUM_REMOTE_URL=http://host.docker.internal:4444/wd/hub \
  selenium-suite-runner
```

### Run a single test class

```bash
docker run --rm \
  -e SELENIUM_REMOTE_URL=http://host.docker.internal:4444/wd/hub \
  -e TEST_CLASS=SimpleSeleniumTests \
  selenium-suite-runner
```

### Supported environment variables

- `SELENIUM_REMOTE_URL` - required when the browser runs on another server/container
- `HEADLESS` - defaults to `true`
- `BROWSER` - defaults to `chrome`
- `TEST_CLASS` - optional Maven Surefire class filter

## Docker Compose

The repo also includes `docker-compose.yml` so you can start a Selenium Chrome container and run this JDK 21 test runner against it.

### Start only the Selenium server

```bash
docker compose up -d selenium
```

Useful endpoints:

- Selenium status/UI: `http://localhost:4444`
- Optional browser view over VNC: `http://localhost:7900/?autoconnect=1&resize=scale`

### Run the suite through Compose

```bash
docker compose --profile runner up --build --abort-on-container-exit test-runner
```

### Run one test class through Compose

```bash
TEST_CLASS=SimpleSeleniumTests docker compose --profile runner up --build --abort-on-container-exit test-runner
```

### Clean up containers

```bash
docker compose down
```

### Remote mode

Set one of these before running the tests:

- `SELENIUM_REMOTE_URL` environment variable
- or `-DremoteUrl=http://your-selenium-host:4444/wd/hub` on the Maven command line

When `remoteUrl` is set, `BaseTest` uses `RemoteWebDriver` instead of local browser binaries.

### Example: Selenium Grid / standalone container

If your remote host runs Selenium Grid or standalone Chrome, the API can call the suite like this:

```bash
mvn -Dheadless=true -DremoteUrl=http://your-host:4444/wd/hub test
```

### Example API flow

1. UI sends a request to your API.
2. API publishes a job or directly triggers the Maven command on the remote runner.
3. Remote runner executes `mvn test` with `SELENIUM_REMOTE_URL` configured.

### Notes on free hosting

For a free always-on machine, the most practical option is an always-free cloud VM (for example Oracle Cloud Free Tier) or a free CI runner.

If you use a containerized Selenium image, make sure the API points to the remote WebDriver endpoint exposed by that host.

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

Run against a remote Selenium server:

```bash
mvn -DremoteUrl=http://localhost:4444/wd/hub test
```

Run only this class:

```bash
mvn -Dtest=SimpleSeleniumTests test
```

## Notes

- The tests target a public Selenium demo page: `https://www.selenium.dev/selenium/web/web-form.html`
- If your corporate network blocks external downloads, preinstall browser drivers and disable WebDriverManager usage.
- For remote execution, the browser runs on the remote host; this repo only sends WebDriver commands.
