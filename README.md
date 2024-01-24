# Breakout Game
## Divyansh Jain



This project implements the game of Breakout with multiple levels.

### Timeline

 * Start Date: 1/14/2024

 * Finish Date: 1/24/2024

 * Hours Spent: 20



### Attributions

 * Resources used for learning (including AI assistance):

    - javatpoint.com
    - Chat GPT
    - Official Documentation
    - Youtube tutorials
    - Lecture slides




### Running the Program

- Main class: Main class in the breakout package.


- Data files needed: 

  - Configuration files for each level (e.g., level1, level2, etc.).

  - Key/Mouse inputs:

  - Arrow keys for paddle movement

  - Space key to start the game and release the ball
  - 'L' key to increment lives
  - 'R' key to reset ball and paddle positions
  - 'F' key to increase ball speed
  - Number keys (1-9) to load specific levels
  - 'S' key to reset the game to the splash screen
  - 'P' key to slow down the ball


- Cheat keys
  - 'L': Increment lives
  - 'R': Reset ball and paddle positions
  - 'F': Increase ball speed
  - Number keys (1-9): Load specific levels
  - 'S': Reset the game to the splash screen
  - 'P': Slow down ball

   
### Notes/Assumptions

- Assumptions or Simplifications:

The game assumes a standard Breakout game environment with paddle, ball, and blocks.
Configuration files for each level are assumed to be in a specific format.
Known Bugs: No known bugs reported.

- Features implemented:

Classic Breakout game mechanics.

Multiple levels with varying block configurations.

Power-ups and special blocks.

Dynamic paddle and ball interactions.

- Features unimplemented: 

From the Project plan, things that I was not able 
to implement included :

     - Block Idea #2 - When an exploding block is destroyed, it also destroys (or damages, in the case of Multi-Hit Blocks) neighboring blocks.
     - Power Up Idea Idea #1 - When the player gets an Extra Ball power-up, a new ball is added to the level. The old ball is not affected. While there is at least two balls on the screen and a ball contacts the edge of the window, the ball disappears but the player does not lose a life.
     - Power up Idea #3 - After the player gets a Lasers power-up, when the player presses the space bar, a laser fires from the paddle. When the laser contacts a block, it damages/destroys the block. 
     - Cheat key Idea #5 - Dropping a power-up immediatiely
     - Cheat key Idea #6 - Changing the direction of the ball as if it bounced off something

This was primarily because of time constraints.

- Noteworthy Features:

Cheat keys for testing and debugging.

Configuration files for defining different levels.

Different types of blocks: multihit, custom special blocks
that return ball to paddle



### Assignment Impressions


- Thoroughly enjoyed implementing JavaFX
- First massive individual coding assignment
- Not the best code design, but good functionality
and ended up learning a lot!