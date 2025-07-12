# Arbitrary Precision Arithmetic Library in Java

This project implements **arbitrary-precision integer** (`AInteger`) and **floating-point** (`AFloat`) **arithmetic operations in Java**. It supports basic arithmetic operations (addition, subtraction, multiplication, and division) on numbers of any length and precision, bypassing the limitations of Java's built-in numeric types, using digit-by-digit operations on integer arrays. The tool provides a command-line interface (`MyInfArith`) for performing operations like addition, subtraction, multiplication, and comparison.

## Build the project using docker:
sudo docker build -t myinfarith .
## Run the project in myinfarith:
sudo docker run myinfarith

---

## Project Structure

```
.
├── build/                # Compiled class files
├── src/                  # Java source files (AInteger.java, AFloat.java)
├── Makefile              # Optional build automation
├── MyInfArith.class      # Compiled main class
├── MyInfArith.java       # Main Java file
├── README.md             # Project documentation
├── build.xml             # Ant build configuration
└── runproj.py            # Python script (optional)
```

---

### Compilation

Open a terminal in the project directory:

```bash
javac -d build src/arbitraryarithmetic/AInteger.java src/arbitraryarithmetic/AFloat.java MyInfArith.java
```

This will compile the Java classes into the `build` folder.

---

### Running the Program

```bash
java -cp build MyInfArith <int|float> <add|sub|mul|div> <number1> <number2>
```
---

## Notes

* The `AInteger` and `AFloat` classes implement custom logic for high-precision arithmetic, using regular algorithms such as long division.
* This project demonstrates fundamental algorithms for operations beyond standard data type limits.

---
