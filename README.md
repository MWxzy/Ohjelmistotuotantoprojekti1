# Temperature Converter

## 1. Assignment Description

This project implements a temperature conversion application with a graphical user interface. The application allows the user to enter a temperature value, select the original unit, select the target unit, and view the converted result.

The supported temperature units are:

- Celsius
- Fahrenheit
- Kelvin

The application also stores conversion records in a local H2 database.

### Main requirements addressed

- Implement a graphical user interface for temperature conversion.
- Support conversions between Celsius, Fahrenheit, and Kelvin.
- Validate numeric user input.
- Display the converted temperature with two decimal places.
- Store temperature conversion records in a database.
- Initialize the database automatically when the application starts.
- Provide automated unit tests for the conversion logic and database functionality.
- Provide Maven, Docker, and Jenkins-based build and verification support.

## 2. Technologies & Tools Used

### Programming languages

- Java 17
- HTML
- JavaScript
- CSS
- Groovy, for the Jenkins pipeline

### Libraries and frameworks

- JavaFX 21.0.2
    - `javafx-controls`
    - `javafx-fxml`
    - `javafx-graphics`
    - `javafx-base`
- H2 Database 2.2.224
- JUnit Jupiter 5.13.4
- JaCoCo 0.8.11

### Development and build tools

- Apache Maven 3.9 or newer
- Java Development Kit 17
- Docker
- Jenkins
- Git and GitHub
- IntelliJ IDEA project configuration

## 3. Design Approach & Implementation Method

### Application structure

The application is divided into three main areas:

1. Graphical user interface
2. Temperature conversion logic
3. Database access

The main application classes are located in `src/main/java`.

### Graphical user interface

The GUI is implemented using JavaFX. The application window contains:

- A text field for entering the temperature value
- A unit selector for the original temperature unit
- A unit selector for the target temperature unit
- A conversion button
- A label for displaying the result or an error message

The application starts from the `Launcher` class, which starts `TempConverterApp`.

The main JavaFX application initializes the database before displaying the user interface. The available temperature units are loaded from the database through `TemperatureUnitDAO`.

### Temperature conversion

The conversion logic is implemented in the `TemperatureConverter` class. The following conversions are supported:

- Fahrenheit to Celsius
- Celsius to Fahrenheit
- Kelvin to Celsius
- Detection of extreme Celsius temperatures

The GUI also supports conversion between all three units by first converting the input value to Celsius and then converting the Celsius value to the selected target unit.

Temperature values are displayed using two decimal places.

### Database design

The application uses an H2 file-based database stored in the `data` directory.

The database contains two tables:

#### `temperature_unit`

Stores the supported temperature units.

| Column | Description |
|---|---|
| `id` | Unique identifier |
| `name` | Name of the temperature unit |
| `symbol` | Symbol of the temperature unit |

The database is automatically populated with:

- Celsius
- Fahrenheit
- Kelvin

#### `temperature_record`

Stores completed temperature conversions.

| Column | Description |
|---|---|
| `id` | Unique identifier |
| `input_value` | Original temperature value |
| `from_unit_id` | Original temperature unit |
| `to_unit_id` | Target temperature unit |
| `result` | Converted temperature |
| `recorded_at` | Time of the conversion |

### Database access

Database operations are separated into DAO classes:

- `TemperatureUnitDAO` retrieves temperature unit names.
- `TempRecordDAO` saves conversion records.
- `DatabaseManager` creates the database connection and initializes the database tables.

Prepared statements are used when storing conversion records.

### Build and deployment approach

Maven is used to:

- Compile the Java source code
- Download project dependencies
- Run automated tests
- Generate code coverage reports
- Build an executable shaded JAR file

A Dockerfile is included to create a containerized version of the application. The Docker build uses a multi-stage build and packages the application together with its dependencies.

A Jenkins pipeline is also included. It performs the following stages:

1. Checkout
2. Build
3. Test
4. Code coverage
5. Publish test results
6. Publish coverage results
7. Build the Docker image
8. Verify the Docker image
9. Publish the image to Docker Hub

## 4. Testing & Quality Assurance Steps

### Automated tests

The project uses JUnit 5 for automated testing.

The tests cover the following functionality:

| Test area | Test scenarios | Expected result |
|---|---|---|
| Fahrenheit-to-Celsius conversion | `32°F`, `212°F`, and `-40°F` | Correct Celsius values are returned |
| Celsius-to-Fahrenheit conversion | `0°C`, `100°C`, and `-40°C` | Correct Fahrenheit values are returned |
| Kelvin-to-Celsius conversion | `300 K`, `273.15 K`, and `0 K` | Correct Celsius values are returned |
| Extreme temperature detection | Values at, below, and above the limits | Correctly identifies extreme temperatures |
| Database connection | Initialize the database and request a connection | A valid database connection is returned |
| Temperature unit DAO | Retrieve available temperature units | Three units are returned, including Celsius |

The temperature conversion tests use a tolerance of `0.001` to account for floating-point calculation differences.

### Manual testing

The graphical user interface can be tested manually using the following scenarios:

1. Enter a valid Celsius value and convert it to Fahrenheit.
2. Enter a valid Fahrenheit value and convert it to Celsius.
3. Enter a valid Kelvin value and convert it to Celsius.
4. Convert a temperature between all supported unit combinations.
5. Leave the input empty and click **Convert**.
6. Enter non-numeric input and click **Convert**.
7. Confirm that an error message is displayed for invalid input.
8. Confirm that valid conversions are displayed with two decimal places.
9. Confirm that a conversion creates a record in the H2 database.

### Build verification

The following Maven commands are used for quality assurance:

```bash
mvn clean package
mvn test
mvn jacoco:report
```

The Maven build also creates a shaded JAR containing the application and its dependencies.

## 5. How to Run

### Prerequisites

Install the following software:

- JDK 17 or newer
- Apache Maven 3.9 or newer
- Git

For the Docker option:

- Docker Desktop or Docker Engine

### Clone the repository

```bash
git clone https://github.com/MWxzy/Ohjelmistotuotantoprojekti1.git
cd Ohjelmistotuotantoprojekti1
```

### Run the automated tests

```bash
mvn test
```

### Build the application

```bash
mvn clean package
```

The generated JAR file is created in the `target` directory.

### Run the application with Maven

On Windows, run:

```bash
mvn javafx:run
```

The Maven configuration uses the Windows JavaFX platform by default.

On Linux, use:

```bash
mvn -Djavafx.platform=linux javafx:run
```

### Run the packaged JAR

After building the project, run:

```bash
java -jar target/OTP1_inclass1_assignment-1.0-SNAPSHOT.jar
```

### Build and run with Docker

Build the Docker image:

```bash
docker build -t temperature-converter-gui .
```

Run the container:

```bash
docker run --rm temperature-converter-gui
```

The Docker image uses Java 17 and installs the native libraries required by JavaFX.

## Project Structure

```text
.
├── data/
│   ├── tempconverter.mv.db
│   └── tempconverter.trace.db
├── docs/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── Launcher.java
│   │       ├── TempConverterApp.java
│   │       ├── TemperatureConverter.java
│   │       └── database/
│   │           ├── DatabaseManager.java
│   │           ├── TempRecordDAO.java
│   │           └── TemperatureUnitDAO.java
│   └── test/
│       └── java/
│           ├── TemperatureConverterTest.java
│           └── database/
│               ├── DatabaseManagerTest.java
│               └── TemperatureUnitDAOTest.java
├── Dockerfile
├── Jenkinsfile
├── pom.xml
└── README.md
```