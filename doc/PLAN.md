# Breakout Plan
### Name : Divyansh Jain


## Interesting Breakout Variants

 * *Centipong* 

I think this variant of Breakout is extremely
interesting because the bricks are not static. 
This aspect introduces a new layer of 
complexity and fun to the game, since the player
not only has to keep tabs on where the ball is,
but also the movement of the moving bricks or centipedes. 
I think this adds difficulty and challenge which can be fun,
and one has to factor in different angles at which one should
hit the ball to make impact with the centipedes. Additionally, the
centipedes are also cute to watch and make the game a little more appealing and animate.

 * *Devilish*

Devilish seems to be a very cool variant of Breakout. By adding
a scrolling and flying feature, it makes the game more interactive
and avoids boredom by repetition, in my opinion. Different
passages, different worlds and the idea of flying combines the 
concept of Breakout with almost a fantasy world of sorts. The idea
of flying also requires a shorter reaction time which makes the game more 
challenging and fun.

* *Vortex*

Vortex, by having a periphery where you have to hit the ball
from all directions, engages the player more. Rather
than the conventional Breakout where you have to hit the ball
from the bottom, hitting the ball from all directions requires the player
to have more attention and keep track of more things. Seems like a 
fun and challenging variant!


## Paddle Ideas

 * Idea #1 - *Ball bouncing differently depending on where ball hits paddle*

This makes the game more fun since you have to account
for where the ball is hitting the paddle. Looking at
the variants mentioned previously, it makes sense that the 
middle third cause the ball to bounce 
normally, the left and right thirds 
cause the ball to bounce back in the 
direction it came.

 * Idea #2 - *warping from one side of the screen to the other when it reaches the edge*

I have played the game as a kid and 
this feature makes it more interesting since
exiting from one side of the screen makes the
game more challenging because the sides 
can be very apart.


## Block Ideas

 * Idea #1 - *When a power-up block is destroyed, the player receives a power-up. 
This power-up is dropped to be collected/caught by the player*


 * Idea #2 - *When an exploding block is destroyed, it also destroys (or damages, in the case of Multi-Hit Blocks) neighboring blocks.*


 * Idea #3 - *When the ball comes into 
contact with a multi-hit block, 
the block’s health is decremented. 
If the health reaches zero, the block 
is destroyed. For instance, if 
the block has 3 health, after it is 
hit three times, it is destroyed.*


* Idea #4 - *Special blocks that instantly return the 
ball to the paddle* 



## Power-up Ideas

 * Idea #1 - *When the player gets an Extra Ball power-up, a new ball is added to the level. The old ball is not affected. While there is at least two balls on the screen and a ball contacts the edge of the window, the ball disappears but the player does not lose a life.*


 * Idea #2 - *When the player gets a Paddle Extension power-up, the width of the paddle is expanded to make it easier to block the ball.*


 * Idea #3 - *After the player gets a Lasers power-up, when the player presses the space bar, a laser fires from the paddle. When the laser contacts a block, it damages/destroys the block.*


 * Idea #4 - *Slows down the ball making it easier to hit it*


## Cheat Key Ideas

 * Idea #1 - *When the player presses the L key, their current life total should be incremented by 1.*


 * Idea #2 - *When the player presses the R key, the ball and paddle should be reset to their starting positions.*


 * Idea #3 - *When the player presses any key 1-9, the current level should be cleared and the game should load the level corresponding to that key (or the highest one that exists).*


 * Idea #4 - *When the player presses the S key, the level should be cleared and the starting splash screen loaded as if the game had just started.*


 * Idea #5 - *Dropping a power-up immediatiely*


 * Idea #6 - *Changing the direction of the ball as if it bounced off something*

## Level Descriptions

 * Idea #1 - *Classic Breakout*


   Block Configuration: 
   
   1 0 0 0 0 0

   0 2 0 0 0 0

   0 0 5 0 0 0

   0 0 0 3 0 0

   0 0 0 0 4 0


   
Variations: Basic Breakout gameplay, introducing players to core mechanics. Traditional paddle, standard ball behavior.

 * Idea #2 - *More blocks and blocks that require multiple hits*

Block Configuration:

0 0 5 5 0 0

0 5 5 5 5 0

0 5 5 5 5 0

5 5 5 5 5 5

5 4 5 5 4 5

5 4 5 5 4 5

5 5 5 5 5 5

5 5 5 5 5 5

5 2 2 2 2 5

5 5 5 5 5 5

1 1 1 1 1 1

1 1 1 1 1 1

1 1 1 1 1 1

0 3 0 0 3 0

0 3 0 0 3 0

0 3 0 0 3 0

0 3 0 0 3 0

1 1 0 0 1 1

Variations: More blocks requiring multiple hits by the ball, standard paddle
and standard speed of block. Simply harder block configuration

 * Idea #3 - * Smaller paddle and faster ball*

Block Configuration:

0 2 3 3 2 0

0 2 2 2 2 0

0 2 2 2 2 0

0 0 2 2 0 0

0 0 2 2 0 0

0 0 0 0 0 0

5 1 5 1 5 1

1 5 1 5 1 5

5 1 5 1 5 1

0 0 0 0 0 0

Variation: Smaller paddle, faster ball. More blocks
requiring multiple hits than level 1, but lesser than level 2.


## Class Ideas

### Idea #1 - *GameController Class*

Methods:

- start_game: Initiates the game, sets up initial configurations.
- end_game: Handles game over conditions, displays end-game information.
- load_level: Loads a specific game level, setting up block configurations.
- update_score: Updates the player's score based on game events.

### Idea #2 - *Ball Class*

Methods:

- move: Controls the movement of the ball on the screen.
- bounce: Manages the ball's bouncing behavior off walls, paddles, and blocks.
- destroy_block: Handles the destruction of blocks upon collision with the ball.

### Idea #3 - *Paddle Class*

Methods:

- move: Allows the player to move the paddle left and right.
- apply_ability: Implements the chosen ability for the paddle (e.g., directional bounce, screen warping).
- catch_ball: Enables the paddle to catch the ball temporarily.
- release_ball: Releases the caught ball when triggered.

### Idea #4 - *Block Class*

Methods: 

- receive_hit: Manages the block's response when hit by the ball.
- destroy: Handles the destruction of the block.
- drop_power_up: Initiates the dropping of power-ups when certain blocks are destroyed.

