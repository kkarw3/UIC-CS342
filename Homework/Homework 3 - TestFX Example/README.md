# CS 342 — TestFX and JUnit Maven Example

Companion to the **Testing JavaFX Applications with TestFX** lecture (slides 8–14).

## Requirements
- JDK 25, Maven 3.9+, graphical desktop session (TestFX needs a display).
- JavaFX 25.0.4, JUnit Jupiter 5.12.2, TestFX 4.0.18.

## Run
```bash
mvn clean test                           # unit + TestFX GUI tests (requires desktop)
mvn -Dtest=CounterTest,AmountValidatorTest test  # unit tests only
mvn -Dtest=CounterAppTest test           # slide 10–11 GUI tests
mvn -Dtest=CounterFxmlAppTest test       # slide 13 GUI tests
mvn -Dtest=AmountFormAppTest test        # slide 14 GUI tests
mvn javafx:run                          # launch the FXML counter
```

To run the programmatic counter or form, launch `CounterApp.main` or `AmountFormApp.main` from IntelliJ using the Maven-imported project and JDK 25.

## Structure
- `Counter` — pure Java model; `CounterTest` — fast unit tests.
- `CounterApp` — programmatic UI (slide 8); `CounterAppTest` — clicks/assertions (slides 9–11).
- `CounterFxmlApp`, `CounterController`, `CounterView.fxml` — FXML implementation (slides 12–13); `CounterFxmlAppTest` — GUI tests.
- `AmountFormApp`, `AmountValidator` — validation demo (slide 14); unit and GUI tests.



## Student exercises
1. Add a Decrement button and corresponding model unit test and TestFX test.
2. Disable Reset when the count is zero; test the disabled state.
3. Add an acceptance criterion for negative amounts and test it at both levels.
