/*Armin Hamrah
Mr. Kevin Morris
AP Computer Science A
15 September 2021*/
import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
/*Pawn Breakout*/
public class Breakout extends GraphicsProgram 
{
    // Width and height of application window in pixels
    public static final int APPLICATION_WIDTH = 400;
    public static final int APPLICATION_HEIGHT = 600;

    // Dimensions of game board in pixels (usually the same)
    private static final int WIDTH = APPLICATION_WIDTH;
    private static final int HEIGHT = APPLICATION_HEIGHT;

    // Dimensions of the paddle
    private static final int PADDLE_WIDTH = 60;
    private static final int PADDLE_HEIGHT = 10;

    // Offset of the paddle up from the bottom
    private static final int PADDLE_Y_OFFSET = 30;

    // Number of bricks per row
    private static final int NBRICKS_PER_ROW = 10;

    // Number of rows of bricks
    private static final int NBRICK_ROWS = 10;

    // Separation between bricks
    private static final int BRICK_SEP = 4;

    // Width of a brick
    private static final int BRICK_WIDTH =
        (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

    // Height of a brick
    private static final int BRICK_HEIGHT = 8;

    // Radius of the ball in pixels
    private static final int BALL_RADIUS = 10;

    // Offset of the top brick row from the top
    private static final int BRICK_Y_OFFSET = 70;

    // Number of "lives" (balls) before the player loses
    private static final int NUM_LIVES = 3;

    // Global variables declared here. You should feel free to add others as needed.
    GRect paddle;
    GOval ball;
    double vx;
    double vy;
    int lives = 3;
    GLabel liveCounter;
    GLabel tryAgainText = null;
    GRect tryAgainRect = null;
    GRect tryAgainBorder = null;
    GLabel playAgainText = null;
    GRect playAgainRect = null;
    GRect playAgainBorder = null;
    int row;
    int col;
    int numBricks = NBRICK_ROWS*NBRICKS_PER_ROW;
    AudioClip kmoSayingPawns = MediaTools.loadAudioClip("kmosayingpawns.au");

    // Run method
    public void run()
    {
        setupGame();
        animationLoop();
    }

    public void setupGame()
    {
        drawBricks();
        drawPaddle();
        drawBall();
        liveCounter = new GLabel("Lives Remaining: " + lives, APPLICATION_WIDTH/2-175, 50);
        liveCounter.setFont("Average-36-Bold");
        liveCounter.setColor(Color.red);
        add(liveCounter);
    }

    public void animationLoop()
    {
        while(true)
        {
            updateBall();
            pause(5);
            checkForCollisions();
        }
    }

    //0.1: Initialize Bricks
    public void drawBricks()
    {
        for (row=0; row<NBRICK_ROWS; row++)
        {
            for (col=0; col<NBRICKS_PER_ROW; col++)
            {
                GRect brick = new GRect(col*(BRICK_WIDTH+BRICK_SEP),
                        BRICK_Y_OFFSET+row*(BRICK_HEIGHT+BRICK_SEP),
                        BRICK_WIDTH, BRICK_HEIGHT);

                brick.setFilled(true);
                add(brick);
                if (row == 0 && col == 0)
                {
                    brick.setColor(new Color(255, 215, 0));
                }
                else if (row == 0 && col == 9)
                {
                    brick.setColor(new Color(255, 215, 0));
                }
                else if (row == 9 && col == 0)
                {
                    brick.setColor(new Color(255, 215, 0));
                }
                else if (row == 9 && col == 9)
                {
                    brick.setColor(new Color(255, 215, 0));
                }
                else if (col % 2 == 0 && row % 2 == 0)
                {
                    brick.setColor(Color.black);   
                }
                else if (row<1)
                {
                    brick.setColor(new Color(155 + col*10, 0, 0));
                }
                else if (row<2)
                {
                    brick.setColor(new Color(255 - col*10, 0, 0));
                }
                else if (row<3)
                {
                    brick.setColor(new Color (155+col*10, 65+col*10, 0));
                }
                else if (row<4)
                {
                    brick.setColor(new Color (255-col*10, 165-col*10, 0));
                }
                else if (row<5)
                {
                    brick.setColor(new Color(155+col*10, 155+col*10, 0));
                }
                else if (row<6)
                {
                    brick.setColor(new Color(255-col*10, 255-col*10, 0));
                }
                else if (row<7)
                {
                    brick.setColor(new Color(0, 155+col*10, 0));
                }
                else if (row<8)
                {
                    brick.setColor(new Color(0, 255-col*10, 0));
                }
                else if (row<9)
                {
                    brick.setColor(new Color(20, 80, 255-col*10));
                }
                else
                {
                    brick.setColor(new Color(20, 80, 155+col*10));
                }
            }
        }
    }

    //0.20 Initialize Paddle
    public void drawPaddle()
    {
        paddle = new GRect(APPLICATION_WIDTH/2-PADDLE_WIDTH/2, 
            APPLICATION_HEIGHT-PADDLE_Y_OFFSET, 
            PADDLE_WIDTH, PADDLE_HEIGHT);
        paddle.setFilled(true);
        add(paddle);
    }

    //0.21 Ensure paddle is on screen
    public void mouseMoved(MouseEvent event)
    {
        if (event.getX()-paddle.getWidth()/2 > 0 && event.getX()<APPLICATION_WIDTH-paddle.getWidth()/2)
        {
            paddle.setLocation(event.getX()-paddle.getWidth()/2, APPLICATION_HEIGHT-PADDLE_Y_OFFSET);
        }
    }

    //0.30: Initialize Ball
    public void drawBall()
    {
        ball = new GOval(APPLICATION_WIDTH/2-BALL_RADIUS, 
            APPLICATION_HEIGHT/2-BALL_RADIUS, 
            BALL_RADIUS*2, 
            BALL_RADIUS*2);
        ball.setFilled(true);
        add(ball);
        vx = 1.5+2*Math.random();
        vy = 1.5+2*Math.random();
    }

    //0.31: Enable ball to bounce off walls
    public void updateBall()
    {
        ball.move(vx, vy);
        if (ball.getX()>APPLICATION_WIDTH-2*BALL_RADIUS || ball.getX()<0)
        {
            vx = -vx;
        }
        if (ball.getY()>APPLICATION_HEIGHT-2*BALL_RADIUS || ball.getY()<0)
        {
            vy = -vy;
        }
    }

    //0.4: Checking for Collisions
    public void checkForCollisions()
    {
        GObject obj = getElementAt(ball.getX(), ball.getY());
        if (obj == null)
        {
            obj = getElementAt(ball.getX(), ball.getY()+2*BALL_RADIUS);
            if (obj == null)
            {
                obj =  getElementAt(ball.getX()+2*BALL_RADIUS, ball.getY());
                if (obj == null)
                {
                    obj = getElementAt(ball.getX()+2*BALL_RADIUS, ball.getY()+2*BALL_RADIUS);
                }
            }
        }
        if (obj != null)
        {
            if (obj == paddle)
            {
                vy = -vy*1.02;
                paddle.setSize(paddle.getWidth() * 0.99, paddle.getHeight());
                if(vy>0)
                {
                    vy = -vy;
                }
            }
            else if (obj == liveCounter)
            {
            }
            else //must be bricks
            {
                kmoSayingPawns.play();
                paddle.setSize(paddle.getWidth() * 0.99, paddle.getHeight());
                vy = -vy;
                remove(obj);
                numBricks = numBricks - 1;
                if (numBricks==0)
                {
                    youWin();

                    remove(ball);
                    ball.setLocation(0,0);
                    vx = 0;
                    vy = 0;
                    playAgain();
                    remove(liveCounter);
                    lives = 3;
                    numBricks = NBRICKS_PER_ROW * NBRICK_ROWS;
                }
            }
        }
        if (ball.getY()+2*BALL_RADIUS >= APPLICATION_HEIGHT)
        {
            lives--;
            liveCounter.setText("Lives Remaining: " + lives);
            if (lives>0)
            {
                ball.setLocation(APPLICATION_WIDTH/2-BALL_RADIUS, APPLICATION_HEIGHT/2-BALL_RADIUS);
                pause(500);
                vx = 1.5+2*Math.random();
                vy = vy*0.85;
                paddle.setSize(paddle.getWidth() * 1.25, paddle.getHeight());
            }
            else
            {
                remove(ball);
                ball.setLocation(0,0);
                vx = 0;
                vy = 0;
                gameOver();
                tryAgain();
                numBricks = NBRICKS_PER_ROW * NBRICK_ROWS;
            }
        }
    }

    public void gameOver()
    {
        GLabel gameOverText = new GLabel("Game over.", APPLICATION_WIDTH/2-98, APPLICATION_HEIGHT/2-18);
        gameOverText.setFont("Average-36");
        gameOverText.setColor(Color.black);

        GRect gameOverRect = new GRect(APPLICATION_WIDTH/2-100, APPLICATION_HEIGHT/2-50, 200, 37);
        gameOverRect.setFilled(true);
        gameOverRect.setColor(new Color(255, 215, 0));

        GRect gameOverBorder = new GRect(APPLICATION_WIDTH/2-100, APPLICATION_HEIGHT/2-50, 200, 37);
        gameOverBorder.setFilled(false);
        gameOverBorder.setColor(Color.black);

        add(gameOverRect);
        add(gameOverText);
        add(gameOverBorder);
    }

    public void tryAgain()
    {
        tryAgainText = new GLabel("Try Again?", APPLICATION_WIDTH/2-90, APPLICATION_HEIGHT/2+32);
        tryAgainText.setFont("Average-36");
        tryAgainText.setColor(Color.black);

        tryAgainRect = new GRect(APPLICATION_WIDTH/2-100, APPLICATION_HEIGHT/2, 200, 35);
        tryAgainRect.setFilled(true);
        tryAgainRect.setColor(new Color(255, 215, 0));

        tryAgainBorder = new GRect(APPLICATION_WIDTH/2-100, APPLICATION_HEIGHT/2, 200, 35);
        tryAgainBorder.setFilled(false);
        tryAgainBorder.setColor(Color.black);

        add(tryAgainRect);
        add(tryAgainText);
        add(tryAgainBorder);
    }

    public void playAgain()
    {
        playAgainText = new GLabel("Play Again?", APPLICATION_WIDTH/2-90, APPLICATION_HEIGHT/2+32);
        playAgainText.setFont("Average-34");
        playAgainText.setColor(Color.black);

        playAgainRect = new GRect(APPLICATION_WIDTH/2-100, APPLICATION_HEIGHT/2, 200, 35);
        playAgainRect.setFilled(true);
        playAgainRect.setColor(new Color(255, 215, 0));

        playAgainBorder = new GRect(APPLICATION_WIDTH/2-100, APPLICATION_HEIGHT/2, 200, 35);
        playAgainBorder.setFilled(false);
        playAgainBorder.setColor(Color.black);

        add(playAgainText);
        add(playAgainRect);
        add(playAgainBorder);
        playAgainRect.sendToBack();
    }

    public void youWin()
    {
        GLabel youWinText = new GLabel("Congratulations! You Win!", APPLICATION_WIDTH/2-95, APPLICATION_HEIGHT/2-18);
        youWinText.setFont("Average-14-Bold");
        youWinText.setColor(Color.black);

        GRect youWinRect = new GRect(APPLICATION_WIDTH/2-100, APPLICATION_HEIGHT/2-50, 200, 37);
        youWinRect.setFilled(true);
        youWinRect.setColor(new Color(255, 215, 0));

        GRect youWinBorder = new GRect(APPLICATION_WIDTH/2-100, APPLICATION_HEIGHT/2-50, 200, 37);
        youWinBorder.setFilled(false);
        youWinBorder.setColor(Color.black);

        add(youWinText);
        add(youWinRect);
        add(youWinBorder);
        youWinRect.sendToBack();
    }

    public void mouseClicked(MouseEvent event)
    {
        if (tryAgainRect != null && (getElementAt(event.getX(), event.getY()) == tryAgainRect|| 
            getElementAt(event.getX(), event.getY()) == tryAgainText || 
            getElementAt(event.getX(), event.getY()) == tryAgainBorder))
        {
            removeAll(); 
            lives = 3;
            setupGame();
        }
        else if (playAgainRect != null && (getElementAt(event.getX(), event.getY()) == playAgainRect|| 
            getElementAt(event.getX(), event.getY()) == playAgainText || 
            getElementAt(event.getX(), event.getY()) == playAgainBorder))
        {
            removeAll();
            lives = 3;
            setupGame();
        }
    }
}