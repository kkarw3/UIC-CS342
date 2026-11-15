# CS 342 — Project 2: Three Card Poker (Two-Player Edition)

A Java 25 and JavaFX 25 desktop implementation of Two-Player Three Card Poker.

---

## How to Build and Run the Application

### Prerequisites
* **Java 25+**
* **Apache Maven 3.9+**

### Build and Run Commands

1. **Compile the project:**
   ```bash
   mvn clean compile
   ```
   
2. **Launch the App**
   ```bash
   mvn javafx:run
   ```

## Running Tests
This project uses JUnit Jupiter to verify different parts of the program (mainly game logic regarding hand detection & payout calculations, deck making/shuffling, etc.) To run the automated test, use the following command:
   ```bash
   mvn test
   ```
## Responsibilities of Major Software Components

This project's code is structured into different sections to ensure the separation between game logic, UI, and gameplay states.

### Main Folder (poker)
Despite Main.java being the starting point for the program, it only initializes the primary Stage and loads the Welcome screen. The rest of the screen navigator code is instead in SceneNavigator.java, where screen transitions and the CSS styles are handled.

### Model Folder (poker/model)
The Model folder contains all code pertaining to the game logic. This is where you'll find the object classes for the Cards, Deck, Player, as well as the underlying logic for Three Card Poker.

### Service Folder (poker/service)
The Service Folder contains all code pertaining to the different phases of the game. This is where you'll find the defined game phases, as well as the code for managing the phases of each round.

### Controller Folder (poker/controller)
The Controller Folder contains all code pertaining to the UI buttons and animations. This is where you'll find FXML definitions for buttons & code for animations and other visuals.

## Purpose of Each Screen

The Welcome Screen is the starting point for the program. From here, you can start a game, view the rules, or exit. There is also a theme toggle in the top-right of the screen.

The Gameplay Screen is where the game is played (obviously). Players place their bets, the cards are revealed, the players decide whether to play or fold, the dealer's cards are revealed, and the players either win or lose. The Gameplay Screen features a header at the top that tells the player what to do for each phase. There are also visible ranges for each bet and buttons are toggled on or off depending on the game phase.

The Results Screen shows the results of the round that was just played. It displays who won and who lost, as well as the round payout calculations for each player. There are also buttons to either start the next round or quit.

## Purpose of Each Animation

Between screens, I added a Fade transition to make each transition between screens much smoother and less jarring compared to just instantly loading the next screen.

I also added an animation where the cards slide in at the start of each round to show that new cards have been pulled from the deck.

After bets are placed, the player's card are animated to flip over and reveal, adding tension to the game as the players decide whether to play or fold. The dealer's card then flip over afterward as well.

On the results screen, The winner's box is highlighted and there's a brief pulsing animation around the box, meant to draw attention to who won the round.

## Part 1 Feedback & Resulting Changes

During Part 1, I got feedback saying it was unclear what buttons were toggleable during what phase. To fix this for Part 2, I made sure a button was grayed out when it wasn't time to click that button. I also made clickable buttons orange to show they were clickable.

The header at the top of the Gameplay Screen denoting what to do was also part of user feedback, as users were unclear on what to do during each phase.

## Design Reflection Questions

1. Why did you separate game logic from the FXML controllers?

I separated the game logic from the FXML controllers because it decouples things like the UI rendering and event handling from poker rules and payout calculations. Because of this, the controllers only read user inputs and display state, removing the need for duplicate rule code across screens.

2. What usability problems did other students identify?

During Part 1, other students noted the following:

- Unclear why their ante was invalid (there was no text denoting the range for bets)
- Distinguishing winning vs losing outcomes quickly without relying only on color changes.

3. Which wireframe revisions most improved the user experience?

For my wireframe, moving the results screen to a dedicated screen helped declutter the screen, making it easier to see the results of a round. I also made it so that both players are displayed on the same side of the screen, making it easier to distinguish the players and the dealer.

4. How do the animations help users understand game events?

The animations correspond to different phases of the game. The cards sliding in face-down at the start signify the start of the betting phase, the players' cards flipping over symbolize the transition from betting to player decision, and the dealer's cards flipping over symbolize the transition from player decision to the round being over. There is also a screen fade transition to signify a screen switch.

5. How could the interface be redesigned without changing the game rules?

Because all of the rules for Three Card Poker are in `poker/controller`, the interface is able to be easily modified without changing anything relating to the underlying game logic. Instead of displaying a separate results screen, you could probably change it to be a slide-up summary window, removing the need for a separate screen. You could also change the text fields for inputting wages to casino-style poker chips with designs on them depending on their value as an alternative way to place bets.

6. How does your architecture make the application easier to test and maintain?

Because of the way I organized my program, it made unit testing significantly easier to set up. Because the classes in `poker/model` do not have any dependencies with JavaFX, it makes writing tests much easier and running tests much faster. Additionally, if you wanted to modify something like Three Card Poker's rules, the only thing you'd need to change is `ThreeCardLogic.java` and nothing else. If you wanted to change something visually, the only thing you'd need to change is either `light.css` or `dark.css`.