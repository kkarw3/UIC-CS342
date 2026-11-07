Krrish Karwal

NetID: kkarw3

UIN: 676475701

Homework 2 README Reflection Questions

1. What code is different between constructing the interface programmatically and defining it with FXML?

Constructing the programmatic interface required creating Java classes and manually creating
the interface using different functions and methods. In contrast, the FXML interface is constructed
using XML, the structure of which is mostly composed of nested UI elements.

2. What responsibilities belong in the FXML controller, and what responsibilities should stay out of it?

The FXML controller should only handle UI interaction logic (i.e. when a user clicks
a button or dropdown menu) as well as updating the UI elements. Because of this, it should not
have any logic pertaining to data fetching, API calls, or calculation logic.

3. Why is a lambda expression convenient for a short JavaFX event handler?

A lambda expression is significantly smaller than a conventional function or class.
Because of this, it allows for the condensation of a function/class into a single line
without sacrificing any of the function/class's components.

4. How does an event handler connect a user action to a change in the JavaFX interface?

An event handler acts as a bridge between the user and the program itself. When the user
interacts with a UI element in the program, it is sent to the program and "intercepted" by
an event handler, which determines what UI element was clicked on and executes its associated
code block. In the case of the interface button, clicking the other interface option toggles the UI
to that interface.

5. Which graphical representation did you choose, and what does its visual scale mean?

I chose to represent the cities' temperatures using a bar chart. For each city, their temperature
is displayed alongside each other to show a clearer difference in temperatures between the cities.

6. Which version—programmatic or FXML—would be easier for your team to maintain as the interface
grows? Explain.

To me, the FXML-based version would be way easier to maintain as the interface evolves.
Since FXML is almost entirely an external display method, it allows itself to remain separate
from other components of the program. It also just looks cleaner than the programmatic interface
from a coding perspective, since most of the programmatic interface is composed of several nested
Java functions and classes, making it annoying to edit.
