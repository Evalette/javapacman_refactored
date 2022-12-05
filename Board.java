/* Drew Schuster */

import java.awt.*;
import javax.swing.JPanel;
import java.util.*;

/*This board class contains the player, ghosts, pellets, and most of the game logic.*/
public class Board extends JPanel {
    /* Initialize the images*/
    /* For NOT JAR file*/

    /* Initialize the player and ghosts */
    private Player player;

    private final LinkedList<Ghost> ghosts = new LinkedList<>();

    /* Timer is used for playing sound effects and animations */
    private long timer = System.currentTimeMillis();

    /* Dying is used to count frames in the dying animation.  If it's non-zero,
       pacman is in the process of dying */
    private int dying = 0;

    /* Score information */
    private Score currScore = new Score();
    private final Score highScore = new Score();

    /* if the high scores have been cleared, we have to update the top of the screen to reflect that */
    private boolean clearHighScores = false;

    private int numLives = 2;

    /*Contains the game map, passed to player and ghosts */
    private boolean[][] state;

    /* Contains the state of all pellets*/
    private boolean[][] pellets;

    /* Game dimensions */
    private final int gridSize;
    private final int max;

    /* State flags*/
    private boolean stopped;

    private final Screen titleScreen;
    private final Screen winScreen;
    private final Screen overScreen;
    private boolean demo = false;
    private int New;

    /* Used to call sound effects */
    private final GameSounds sounds;

    private int lastPelletEatenX = 0;
    private int lastPelletEatenY = 0;

    /* This is the font used for the menus */
    private final Font font = new Font("Monospaced", Font.BOLD, 12);

    /* Constructor initializes state flags etc.*/
    public Board(int New) {
        sounds = new GameSounds();
        highScore.initScoreFromFile();
        stopped = false;
        max = 400;
        gridSize = 20;
        this.New = New;

        titleScreen = new Screen("img/titleScreen.jpg", true);
        overScreen = new Screen("img/gameOver.jpg", false);
        winScreen = new Screen("img/winScreen.jpg", false);

        player = new Player(200, 300, "img/pacman.jpg");
        Ghost ghost1 = new Ghost(180, 180, "img/ghost11.jpg", "img/ghost10.jpg");
        Ghost ghost2 = new Ghost(200, 180, "img/ghost21.jpg", "img/ghost20.jpg");
        Ghost ghost3 = new Ghost(220, 180, "img/ghost31.jpg", "img/ghost30.jpg");
        Ghost ghost4 = new Ghost(220, 180, "img/ghost41.jpg", "img/ghost40.jpg");
        ghosts.add(ghost1);
        ghosts.add(ghost2);
        ghosts.add(ghost3);
        ghosts.add(ghost4);
    }

    /* Writes the new high score to a file and sets flag to update it on screen */
    public void updateScore(Score score) {
        score.updateScore();
        highScore.initScoreFromFile();
        clearHighScores = true;
    }

    /* Wipes the high scores file and sets flag to update it on screen */
    public void clearHighScores() {
        highScore.clear();
        clearHighScores = true;
    }

    /* Reset occurs on a new game*/
    public void reset() {
        numLives = 2;
        state = new boolean[20][20];
        pellets = new boolean[20][20];

        /* Clear state and pellets arrays */
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                state[i][j] = true;
                pellets[i][j] = true;
            }
        }

        /* Handle the weird spots with no pellets*/
        for (int i = 5; i < 14; i++) {
            for (int j = 5; j < 12; j++) {
                pellets[i][j] = false;
            }
        }
        pellets[9][7] = false;
        pellets[8][8] = false;
        pellets[9][8] = false;
        pellets[10][8] = false;
    }


    /* Function is called during drawing of the map.
       Whenever a portion of the map is covered up with a barrier,
       the map and pellets arrays are updated accordingly to note
       that those are invalid locations to travel or put pellets
    */
    public void updateMap(int x, int y, int width, int height) {
        for (int i = x / gridSize; i < x / gridSize + width / gridSize; i++) {
            for (int j = y / gridSize; j < y / gridSize + height / gridSize; j++) {
                state[i - 1][j - 1] = false;
                pellets[i - 1][j - 1] = false;
            }
        }
    }


    /* Draws the appropriate number of lives on the bottom left of the screen.
       Also draws the menu */
    public void drawLives(Graphics g) {
        g.setColor(Color.BLACK);

        /*Clear the bottom bar*/
        g.fillRect(0, max + 5, 600, gridSize);
        g.setColor(Color.YELLOW);
        for (int i = 0; i < numLives; i++) {
            /*Draw each life */
            g.fillOval(gridSize * (i + 1), max + 5, gridSize, gridSize);
        }
        /* Draw the menu items */
        g.setColor(Color.YELLOW);
        g.setFont(font);
        g.drawString("Reset", 100, max + 5 + gridSize);
        g.drawString("Clear High Scores", 180, max + 5 + gridSize);
        g.drawString("Exit", 350, max + 5 + gridSize);
    }


    /*  This function draws the board.  The pacman board is really complicated and can only feasibly be done
        manually.  Whenever I draw a wall, I call updateMap to invalidate those coordinates.  This way the pacman
        and ghosts know that they can't traverse this area */
    public void drawBoard(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 600, 600);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 420, 420);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 20, 600);
        g.fillRect(0, 0, 600, 20);
        g.setColor(Color.WHITE);
        g.drawRect(19, 19, 382, 382);
        g.setColor(Color.BLUE);

        fillRectAndUpdateMap(g, 40, 40, 60, 20);
        fillRectAndUpdateMap(g, 120, 40, 60, 20);
        fillRectAndUpdateMap(g, 200, 20, 20, 40);
        fillRectAndUpdateMap(g, 240, 40, 60, 20);
        fillRectAndUpdateMap(g, 320, 40, 60, 20);
        fillRectAndUpdateMap(g, 40, 80, 60, 20);
        fillRectAndUpdateMap(g, 160, 80, 100, 20);
        fillRectAndUpdateMap(g, 200, 80, 20, 60);
        fillRectAndUpdateMap(g, 320, 80, 60, 20);

        fillRectAndUpdateMap(g, 20, 120, 80, 60);
        fillRectAndUpdateMap(g, 320, 120, 80, 60);
        fillRectAndUpdateMap(g, 20, 200, 80, 60);
        fillRectAndUpdateMap(g, 320, 200, 80, 60);

        fillRectAndUpdateMap(g, 160, 160, 40, 20);
        fillRectAndUpdateMap(g, 220, 160, 40, 20);
        fillRectAndUpdateMap(g, 160, 180, 20, 20);
        fillRectAndUpdateMap(g, 160, 200, 100, 20);
        fillRectAndUpdateMap(g, 240, 180, 20, 20);
        g.setColor(Color.BLUE);


        fillRectAndUpdateMap(g, 120, 120, 60, 20);
        fillRectAndUpdateMap(g, 120, 80, 20, 100);
        fillRectAndUpdateMap(g, 280, 80, 20, 100);
        fillRectAndUpdateMap(g, 240, 120, 60, 20);

        fillRectAndUpdateMap(g, 280, 200, 20, 60);
        fillRectAndUpdateMap(g, 120, 200, 20, 60);
        fillRectAndUpdateMap(g, 160, 240, 100, 20);
        fillRectAndUpdateMap(g, 200, 260, 20, 40);

        fillRectAndUpdateMap(g, 120, 280, 60, 20);
        fillRectAndUpdateMap(g, 240, 280, 60, 20);

        fillRectAndUpdateMap(g, 40, 280, 60, 20);
        fillRectAndUpdateMap(g, 80, 280, 20, 60);
        fillRectAndUpdateMap(g, 320, 280, 60, 20);
        fillRectAndUpdateMap(g, 320, 280, 20, 60);

        fillRectAndUpdateMap(g, 20, 320, 40, 20);
        fillRectAndUpdateMap(g, 360, 320, 40, 20);
        fillRectAndUpdateMap(g, 160, 320, 100, 20);
        fillRectAndUpdateMap(g, 200, 320, 20, 60);

        fillRectAndUpdateMap(g, 40, 360, 140, 20);
        fillRectAndUpdateMap(g, 240, 360, 140, 20);
        fillRectAndUpdateMap(g, 280, 320, 20, 40);
        fillRectAndUpdateMap(g, 120, 320, 20, 60);
        drawLives(g);
    }

    private void fillRectAndUpdateMap(Graphics g, int x, int y, int width, int height) {
        g.fillRect(x, y, width, height);
        updateMap(x, y, width, height);
    }


    /* Draws the pellets on the screen */
    public void drawPellets(Graphics g) {
        g.setColor(Color.YELLOW);
        for (int i = 1; i < 20; i++) {
            for (int j = 1; j < 20; j++) {
                if (pellets[i - 1][j - 1])
                    g.fillOval(i * 20 + 8, j * 20 + 8, 4, 4);
            }
        }
    }

    /* This is the main function that draws one entire frame of the game */
    public void paint(Graphics g) {
    /* If we're playing the dying animation, don't update the entire screen.
       Just kill the pacman*/
        if (dying > 0) {
            /* Stop any pacman eating sounds */
            sounds.nomNomStop();

            /* Draw the pacman */
            player.drawPacMan(g);
            g.setColor(Color.BLACK);

            /* Kill the pacman */
            if (dying == 4)
                player.killPacMan(g, 20, 7);
            else if (dying == 3)
                player.killPacMan(g, 20, 14);
            else if (dying == 2 || dying == 1)
                player.killPacMan(g, 20, 20);
     
      /* Take .1 seconds on each frame of death, and then take 2 seconds
         for the final frame to allow for the sound effect to end */
            long currTime = System.currentTimeMillis();
            long temp;
            if (dying != 1)
                temp = 100;
            else
                temp = 2000;
            /* If it's time to draw a new death frame... */
            if (currTime - timer >= temp) {
                dying--;
                timer = currTime;
                /* If this was the last death frame...*/
                if (dying == 0) {
                    if (numLives == -1) {
                        /* Demo mode has infinite lives, just give it more lives*/
                        if (demo)
                            numLives = 2;
                        else {
                            /* Game over for player.  If relevant, update high score.  Set gameOver flag*/
                            if (currScore.biggerThan(highScore)) {
                                updateScore(currScore);
                            }
                            overScreen.setActive(true);
                        }
                    }
                }
            }
            return;
        }

        /* If this is the title screen, draw the title screen and return */
        if (titleScreen.isActive()) {
            titleScreen.drawImage(g);

            /* Stop any pacman eating sounds */
            sounds.nomNomStop();
            New = 1;
            return;
        }

        /* If this is the win screen, draw the win screen and return */
        else if (winScreen.isActive()) {
            winScreen.drawImage(g);
            New = 1;
            /* Stop any pacman eating sounds */
            sounds.nomNomStop();
            return;
        }

        /* If this is the game over screen, draw the game over screen and return */
        else if (overScreen.isActive()) {
            overScreen.drawImage(g);
            New = 1;
            /* Stop any pacman eating sounds */
            sounds.nomNomStop();
            return;
        }

        /* If I need to update the high scores, redraw the top menu bar */
        if (clearHighScores) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 600, 18);
            g.setColor(Color.YELLOW);
            g.setFont(font);
            clearHighScores = false;
            if (demo)
                g.drawString("DEMO MODE PRESS ANY KEY TO START A GAME\t High Score: " + highScore.getScore(), 20, 10);
            else
                g.drawString("Score: " + (currScore.getScore()) + "\t High Score: " + highScore.getScore(), 20, 10);
        }

        /* oops, is set to true when pacman has lost a life */
        boolean oops = false;

        /* Game initialization */
        if (New == 1) {
            reset();
            player = new Player(200, 300, "img/pacman.jpg");
            ghosts.set(0, new Ghost(180, 180, "img/ghost11.jpg", "img/ghost10.jpg"));
            ghosts.set(1, new Ghost(200, 180, "img/ghost21.jpg", "img/ghost20.jpg"));
            ghosts.set(2, new Ghost(220, 180, "img/ghost31.jpg", "img/ghost30.jpg"));
            ghosts.set(3, new Ghost(220, 180, "img/ghost41.jpg", "img/ghost40.jpg"));

            currScore = new Score();
            drawBoard(g);
            drawPellets(g);
            drawLives(g);
            /* Send the game map to player and all ghosts */
            player.updateState(state);
            /* Don't let the player go in the ghost box*/
            player.getState()[9][7] = false;
            for (Ghost ghost : ghosts) {
                ghost.updateState(state);
            }

            /* Draw the top menu bar*/
            g.setColor(Color.YELLOW);
            g.setFont(font);
            if (demo)
                g.drawString("DEMO MODE PRESS ANY KEY TO START A GAME\t High Score: " + highScore.getScore(), 20, 10);
            else
                g.drawString("Score: " + (currScore.getScore()) + "\t High Score: " + highScore.getScore(), 20, 10);
            New++;
        }
        /* Second frame of new game */
        else if (New == 2) {
            New++;
        }
        /* Third frame of new game */
        else if (New == 3) {
            New++;
            /* Play the newGame sound effect */
            sounds.newGame();
            timer = System.currentTimeMillis();
            return;
        }
        /* Fourth frame of new game */
        else if (New == 4) {
            /* Stay in this state until the sound effect is over */
            long currTime = System.currentTimeMillis();
            if (currTime - timer >= 5000) {
                New = 0;
            } else
                return;
        }

        /* Drawing optimization */
        player.copyArea(g);
        for (Ghost ghost :
                ghosts) {
            ghost.copyArea(g);
        }

        /* Detect collisions */
        if (player.collidesWith(ghosts)) {
            oops = true;
        }

        /* Kill the pacman */
        if (oops && !stopped) {
            /* 4 frames of death*/
            dying = 4;

            /* Play death sound effect */
            sounds.death();
            /* Stop any pacman eating sounds */
            sounds.nomNomStop();

            /*Decrement lives, update screen to reflect that.  And set appropriate flags and timers */
            numLives--;
            stopped = true;
            drawLives(g);
            timer = System.currentTimeMillis();
        }

        /* Delete the players and ghosts */
        g.setColor(Color.BLACK);
        player.delete(g);
        for (Ghost ghost :
                ghosts) {
            ghost.delete(g);
        }

        /* Eat pellets */
        if (pellets[player.getPelletX()][player.getPelletY()] && New != 2 && New != 3) {
            eatPellets(g);

            /* If this was the last pellet */
            if (player.getPelletsEaten() == 173) {
                /*Demo mode can't get a high score */
                if (!demo) {
                    if (currScore.biggerThan(highScore)) {
                        updateScore(currScore);
                    }
                    winScreen.setActive(true);
                } else {
                    titleScreen.setActive(true);
                }
                return;
            }
        }

        /* If we moved to a location without pellets, stop the sounds */
        else if ((player.getPelletX() != lastPelletEatenX || player.getPelletY() != lastPelletEatenY) || player.isStopped()) {
            /* Stop any pacman eating sounds */
            sounds.nomNomStop();
        }


        /* Replace pellets that have been run over by ghosts */
        for (Ghost ghost :
                ghosts) {
            if (pellets[ghost.getLastPelletX()][ghost.getLastPelletY()])
                ghost.fillPellet(g);
        }

        drawGhosts(g);

        /* Draw the pacman */
        if (player.getFrameCount() < 5) {
            /* Draw mouth closed */
            player.drawPacMan(g);
        } else {
            /* Draw mouth open in appropriate direction */
            if (player.getFrameCount() >= 10)
                player.setFrameCount(0);

            player.getCurrDirection().drawImage(g, player);
        }

        /* Draw the border around the game in case it was overwritten by ghost movement or something */
        g.setColor(Color.WHITE);
        g.drawRect(19, 19, 382, 382);

    }

    private void eatPellets(Graphics g) {
        lastPelletEatenX = player.getPelletX();
        lastPelletEatenY = player.getPelletY();

        /* Play eating sound */
        sounds.nomNom();

        /* Increment pellets eaten value to track for end game */
        player.eatPellet();

        /* Delete the pellet*/
        pellets[player.getPelletX()][player.getPelletY()] = false;

        /* Increment the score */
        currScore.add(50);

        /* Update the screen to reflect the new score */
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 600, 20);
        g.setColor(Color.YELLOW);
        g.setFont(font);
        if (demo)
            g.drawString("DEMO MODE PRESS ANY KEY TO START A GAME\t High Score: " + highScore.getScore(), 20, 10);
        else
            g.drawString("Score: " + (currScore.getScore()) + "\t High Score: " + highScore.getScore(), 20, 10);
    }

    private void drawGhosts(Graphics g) {

        /*Draw the ghosts */
        Ghost ghost1 = ghosts.get(0);
        if (ghost1.getFrameCount() < 5) {
            /* Draw first frame of ghosts */
            for (Ghost ghost :
                    ghosts) {
                ghost.drawLooksToTheRight(g);
            }
            ghost1.setFrameCount(ghost1.getFrameCount() + 1);
        } else {
            /* Draw second frame of ghosts */
            for (Ghost ghost :
                    ghosts) {
                ghost.drawLooksToTheLeft(g);
            }

            if (ghost1.getFrameCount() >= 10)
                ghost1.setFrameCount(0);
            else
                ghost1.setFrameCount(ghost1.getFrameCount() + 1);
        }
    }

    /* This repaintChangedArea function repaints only the parts of the screen that may have changed.
           Namely, the area around every player ghost and the menu bars
        */
    public void repaintChangedArea() {
        if (player.isTeleport()) {
            repaint(player.getLastX() - 20, player.getLastY() - 20, 80, 80);
            player.setTeleport(false);
        }
        repaint(0, 0, 600, 20);
        repaint(0, 420, 600, 40);
        repaint(player.getX() - 20, player.getY() - 20, 80, 80);
        for (Ghost ghost :
                ghosts) {
            repaint(ghost.getX() - 20, ghost.getY() - 20, 80, 80);
        }
    }

    void resetStartingPositionsAndOrientations() {
        /* Move all game elements back to starting positions and orientations */
        player.setCurrDirection(new Left());
        player.setDirection(new Left());
        player.setDesiredDirection(new Left());
        player.resetPosition();
        for (Ghost ghost :
                ghosts) {
            ghost.resetPosition();
        }
    }

    /* Also movePlayer the ghosts, and update the pellet states */
    void moveGhostAndUpdatePellet() {
        for (Ghost ghost :
                ghosts) {
            ghost.move();
            ghost.updatePellet();
        }
        player.updatePellet();
    }

    void choseMoveFunction() {
        if (demo) {
            player.demoMove();
        } else {
            player.move();
        }
    }

    void gotToTitleScreen() {
        winScreen.setActive(false);
        overScreen.setActive(false);
        titleScreen.setActive(true);
    }

    public Player getPlayer() {
        return player;
    }

    public int getDying() {
        return dying;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public Screen getTitleScreen() {
        return titleScreen;
    }

    public Screen getWinScreen() {
        return winScreen;
    }

    public Screen getOverScreen() {
        return overScreen;
    }

    public boolean isDemo() {
        return demo;
    }

    public void setDemo(boolean demo) {
        this.demo = demo;
    }

    public int getNew() {
        return New;
    }

    public void setNew(int aNew) {
        New = aNew;
    }

    public GameSounds getSounds() {
        return sounds;
    }

    @Override
    public Font getFont() {
        return font;
    }
}
