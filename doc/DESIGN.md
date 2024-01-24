# Breakout Design
## Divyansh Jain


## Design Goals

The primary design goals of the Breakout game include providing an engaging gaming experience with multiple levels, diverse block types, and dynamic features such as power-ups. The game aims to be modular, allowing for easy expansion and customization.

## High-Level Design

The high-level design involves a modular structure with distinct classes for different game elements. The main components include the paddle, blocks, ball, power-ups, and various game screens. JavaFX is utilized for the graphical user interface, providing an interactive and visually appealing gaming environment.

## Assumptions or Simplifications

The game assumes a straightforward file-based configuration for defining block layouts in each level. Additionally, it assumes that the game will be played on a standard window with keyboard input for control.

## Changes from the Plan

From the Project plan, things that I was not able
to implement included :

     - Block Idea #2 - When an exploding block is destroyed, it also destroys (or damages, in the case of Multi-Hit Blocks) neighboring blocks.
     - Power Up Idea Idea #1 - When the player gets an Extra Ball power-up, a new ball is added to the level. The old ball is not affected. While there is at least two balls on the screen and a ball contacts the edge of the window, the ball disappears but the player does not lose a life.
     - Power up Idea #3 - After the player gets a Lasers power-up, when the player presses the space bar, a laser fires from the paddle. When the laser contacts a block, it damages/destroys the block. 
     - Cheat key Idea #5 - Dropping a power-up immediatiely
     - Cheat key Idea #6 - Changing the direction of the ball as if it bounced off something

This was primarily because of time constraints.

## How to Add New Levels

To add new levels to the Breakout game:

- Create Configuration File:

Generate a new text file representing the block layout for the new level.
Use numeric values to represent different block types, specifying their arrangement.
Update ConfigParser:

- Modify the ConfigParser class to correctly parse the new configuration file.

Ensure that the createBlock method accommodates any new block types.

- Adjust Game Logic:

If the new level introduces unique game mechanics or block behaviors, adapt the game logic accordingly.


Extend the loadNextLevel method in the Main class to handle loading the new level.
Increment the currentLevel variable and update the levelText accordingly.

- Testing:

Thoroughly test the new level to ensure proper functionality and balance.
Verify that the overall progression from one level to another is smooth.
