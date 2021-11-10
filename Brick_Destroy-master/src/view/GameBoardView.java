package view;

import controller.BallController;
import controller.BrickController;
import model.PlayerModel;
import model.WallModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;

/***
 * GameBoardView Class extends JComponent and implements KeyListener , MouseListener and MouseMotionListener
 * to generate the GameBoard(The Actual Game on the GameFrameModel).
 */

public class GameBoardView extends JComponent implements KeyListener,MouseListener,MouseMotionListener {
    // Static Declaration of Variables
    private static final String CONTINUE = "Continue";
    private static final String RESTART = "Restart";
    private static final String EXIT = "Exit";
    private static final String PAUSE = "Pause Menu";
    private static final int TEXT_SIZE = 30;
    private static final Color MENU_COLOR = new Color(0,255,0);



    //Width and Height of the GameBoard
    private static final int DEF_WIDTH = 600;
    private static final int DEF_HEIGHT = 450;
    //Background Color of GameBoard
    private static final Color BG_COLOR = Color.WHITE;
    //Timer of the Game
    private Timer gameTimer;
    // wall object taking the parameter of area of wall , brick count , line count ,brick dimension , platform starting point
    private final WallModel wall;
    // Message during game
    public String message;

    private boolean showPauseMenu = false;
    // Font of Pause Menu
    private final Font menuFont;
    // The coordinates of the Pause Menu Button
    private Rectangle continueButtonRect;
    private Rectangle exitButtonRect;
    private Rectangle restartButtonRect;

    private int strLen = 0;
    //Object for DebugConsoleView
    private final DebugConsoleView debugConsole;

    /**
     * GameBoardView Constructor implements Gameboard characteristics
     * @param owner
     */

    public GameBoardView(JFrame owner){
        super();
        strLen = 0;
        showPauseMenu = false;
        menuFont = new Font("Monospaced",Font.PLAIN,TEXT_SIZE);
        this.initialize();
        message = "";
        wall = new WallModel(new Rectangle(0,0,DEF_WIDTH,DEF_HEIGHT),30,3,6/2,new Point(300,430));
        debugConsole = new DebugConsoleView(owner,wall,this);
        // Create the View of the GameBoard
        //initialize the first level
        wall.nextLevel();
        // Timer setting the delay between the initial delay and and event firing (the ball speed)
        gameTimer = new Timer(10,e ->{
            // Calls the move function in the WallModel Class
            wall.move();
            //Calls the findImpacts function in the WallModel Class
            wall.findImpacts();
            // Message Display for Brick Count and Ball Count
            message = String.format("Bricks: %d Balls %d",wall.getBrickCount(),wall.getBallCount());
            //If the Ball is Lost at the bottom
            if(wall.isBallLost()){
                //And If Ball Count is 0
                if(wall.ballEnd()){
                    //Reset the Wall
                    wall.wallReset();
                    //Display Message Game Over
                    message = "Game over!";
                }
                // If the Ball Is Lost , Reset Ball and the Player
                wall.ballReset();
                // Stop the Game Timer , till the Player Presses Spacebar
                gameTimer.stop();
            }
            //If the Wall Is Done
            else if(wall.isDone()){
                //If the Wall has another level
                if(wall.hasLevel()){
                    //Display Message
                    message = "Go to Next Level";
                    //Stop the GameTimer
                    gameTimer.stop();
                    //Reset the Number of Balls
                    wall.ballReset();
                    //Reset the Wall
                    wall.wallReset();
                    //Go to the Next Level
                    wall.nextLevel();
                }
                else{
                    //If the Player reaches end of game , Display this message
                    message = "ALL WALLS DESTROYED";
                    //Game Timer Stopped.
                    gameTimer.stop();
                }
            }
            //performs a request to erase and perform redraw of the component after a small delay in time.
            repaint();
        });

    }


    /**
     * initialize Method:
     * Method to initialize the KeyListener , Mouselistener , Focusable , etc
     */
    private void initialize(){
        this.setPreferredSize(new Dimension(DEF_WIDTH,DEF_HEIGHT));
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    /**
     * Paint method implements Graphics , inherited from JComponent
     * , it is part of the draw system of the GUI.
     * It's invoked from Java Swing Framework to ask for a Component to draw itself on the screen
     * @param g
     */
    public void paint(Graphics g){
        // Create Object g2d
        Graphics2D g2d = (Graphics2D) g;
        //Setting the Background Color
        clear(g2d);
        //Setting the color of the Message
        g2d.setColor(Color.BLUE);
        g2d.drawString(message,250,225);
        //Calling the drawBall Method , with the parameters of the ball object and g2d
        drawBall(wall.ball,g2d);
        // If the brick is not broken , then call the method drawBrick
        for(BrickController b : wall.bricks) {
            if(!b.isBroken())
                drawBrick(b,g2d);
        }
        //Calling the drawPlayer method to draw the player
        drawPlayer(wall.player,g2d);
        //If the showPauseMenu is true , draw the drawMenu
        if(showPauseMenu)
            drawPauseMenu(g2d);
        //Binding various component implementations and synchronizes them.
        Toolkit.getDefaultToolkit().sync();
    }
    /**
     * clear method:
     * Uses the Graphics2D Object to paint the Background Color of the GameBoard
     * @param g2d
     */

    private void clear(Graphics2D g2d){
        Color tmp = g2d.getColor();
        g2d.setColor(BG_COLOR);
        g2d.fillRect(0,0,getWidth(),getHeight());
        g2d.setColor(tmp);
    }

    /**
     * drawBrick method:
     * Uses the Graphics2D Object to paint the Brick of the GameBoard
     * @param g2d
     */
    private void drawBrick(BrickController brick, Graphics2D g2d){
        Color tmp = g2d.getColor();
        // Calling the getInnerColor of the BrickController Class to get the Inner Color of the Brick
        // And the inner color of it's abstract implementation
        g2d.setColor(brick.getInnerColor());
        // Calls the getBrick Method of the BrickController Class , which captures the Dimension and
        //Size of the Brick in the abstract implementations.
        g2d.fill(brick.getBrick());
        //Calls the getBorderColor Method of the BrickController Class , whcih captures the
        //the Border Color of the Brick by the Abstract Implementations.
        g2d.setColor(brick.getBorderColor());
        // By Taking all parameters , it then paints the brick
        g2d.draw(brick.getBrick());
        g2d.setColor(tmp);
    }


    /**
     * drawBall Method:
     * Uses the Graphics2D Object to Paint the Ball of the GameBoard
     * @param ball
     * @param g2d
     */
    private void drawBall(BallController ball, Graphics2D g2d){
        Color tmp = g2d.getColor();
        // s takes the ball's coordinates to get it's structure
        Shape s = ball.getBallFace();
        //sets the inner color of the ball by calling the getInnerColor method which returns the inner color.
        g2d.setColor(ball.getInnerColor());
        // fill the ball shape with that color
        g2d.fill(s);
        //sets the border color of the ball by calling the getBorderColor method which returns the border color
        g2d.setColor(ball.getBorderColor());
        //draws the border color in the balls face shape s
        g2d.draw(s);

        g2d.setColor(tmp);
    }

    /**
     * drawPlayer Method:
     * To Draw the PLatform Player in the Game
     * @param p
     * @param g2d
     */
    private void drawPlayer(PlayerModel p, Graphics2D g2d){
        Color tmp = g2d.getColor();
        // s takes the ball's coordinates to get it's structure
        Shape s = p.getPlayerFace();
        //sets the inner color of the platform by calling the InnerColor attribute of the PlayerModel Class.
        g2d.setColor(PlayerModel.INNER_COLOR);
        g2d.fill(s);
        //sets the border color of the platform by calling the BorderColor attribute of the PlayerModel Class.
        g2d.setColor(PlayerModel.BORDER_COLOR);
        g2d.draw(s);

        g2d.setColor(tmp);
    }


    /**
     * drawMenu Method:
     * Method that contains the function call to ObscureGameBoard and drawPauseMenu
     * @param g2d
     */
    private void drawMenu(Graphics2D g2d){
        obscureGameBoard(g2d);
        drawPauseMenu(g2d);
    }
    /**
     *obscureGameBoard Method:
     * To Paint the Look of the GameBoard when the Pause Menu is called
     * @param g2d
     */
    private void obscureGameBoard(Graphics2D g2d){

        Composite tmp = g2d.getComposite();
        Color tmpColor = g2d.getColor();
        //Setting the Alpha
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.55f);
        g2d.setComposite(ac);
        //Setting GameBoard Color when Pause Menu is Called
        g2d.setColor(Color.BLACK);
        //Setting the GameBoard Shape when pause Menu is Called
        g2d.fillRect(0,0,DEF_WIDTH,DEF_HEIGHT);
        g2d.setComposite(tmp);
        g2d.setColor(tmpColor);
    }

    /**
     * drawPauseMenu Method:
     * To draw the Pause Menu , after obscuring the GameBoard
     * @param g2d
     */
    private void drawPauseMenu(Graphics2D g2d){
        //Getting the methods getFont and getColor
        Font tmpFont = g2d.getFont();
        Color tmpColor = g2d.getColor();
        // Setting the Font and Color of the Menu Screen
        g2d.setFont(menuFont);
        g2d.setColor(MENU_COLOR);

        if(strLen == 0){
            FontRenderContext frc = g2d.getFontRenderContext();
            strLen = menuFont.getStringBounds(PAUSE,frc).getBounds().width;
        }

        int x = (this.getWidth() - strLen) / 2;
        int y = this.getHeight() / 10;

        g2d.drawString(PAUSE,x,y);

        x = this.getWidth() / 8;
        y = this.getHeight() / 4;

        //Setting the Location and Drawing the continueButtonRect
        if(continueButtonRect == null){
            FontRenderContext frc = g2d.getFontRenderContext();
            continueButtonRect = menuFont.getStringBounds(CONTINUE,frc).getBounds();
            continueButtonRect.setLocation(x,y-continueButtonRect.height);
        }
        g2d.drawString(CONTINUE,x,y);

        y *= 2;
        //Setting the Location and Drawing the restartButtonRect
        if(restartButtonRect == null){
            restartButtonRect = (Rectangle) continueButtonRect.clone();
            restartButtonRect.setLocation(x,y-restartButtonRect.height);
        }

        g2d.drawString(RESTART,x,y);

        y *= 3.0/2;
        //Setting the Location and Drawing the exitButtonRect
        if(exitButtonRect == null){
            exitButtonRect = (Rectangle) continueButtonRect.clone();
            exitButtonRect.setLocation(x,y-exitButtonRect.height);
        }

        g2d.drawString(EXIT,x,y);



        g2d.setFont(tmpFont);
        g2d.setColor(tmpColor);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
    }
    /**
     * keyPressed Method:
     * To assign the Key of the KeyBoard to the corresponding action.
     * @override
     * @param keyEvent
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch(keyEvent.getKeyCode()){
            // A to move left
            case KeyEvent.VK_A:
                wall.player.moveLeft();
                break;
            //D to move right
            case KeyEvent.VK_D:
                wall.player.movRight();
                break;
            //Escape Key to call Pause Menu
            case KeyEvent.VK_ESCAPE:
                showPauseMenu = !showPauseMenu;
                repaint();
                gameTimer.stop();
                break;
            //Space Bar to Pause The Game
            case KeyEvent.VK_SPACE:
                if(!showPauseMenu)
                    if(gameTimer.isRunning())
                        gameTimer.stop();
                    else
                        gameTimer.start();
                break;
            //F1 to see the debugConsole
            case KeyEvent.VK_F1:
                if(keyEvent.isAltDown() && keyEvent.isShiftDown())
                    debugConsole.setVisible(true);
            default:
                wall.player.stop();
        }
    }

    /**
     * keyReleased Method:
     * To mention the Function when the key is released
     * @param keyEvent
     */
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        //Move amount will be 0
        wall.player.stop();
    }

    /**
     * mouseClicked Method:
     * To mention the function corresponding to the click of the mouse
     * @param mouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        //the p object gets the mouse point
        Point p = mouseEvent.getPoint();
        if(!showPauseMenu)
            return;
        //if clicked the continueButtonRect
        if(continueButtonRect.contains(p)){
            //remove the pause menu and repaint
            showPauseMenu = false;
            repaint();
        }
        //if clicked the restartButtonrect
        else if(restartButtonRect.contains(p)){
            //show the message
            message = "Restarting Game...";
            //restart the ball
            wall.ballReset();
            //restart the wall
            wall.wallReset();
            //remove the pause menu
            showPauseMenu = false;
            repaint();
        }
        //if the exitButtonRect is clicked
        else if(exitButtonRect.contains(p)){
            //exit the system
            System.exit(0);
        }

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {

    }

    /**
     * mouseMoved method:
     * To mention the cursor shape when it's hovered
     * @param mouseEvent
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        //get the mouse point
        Point p = mouseEvent.getPoint();
        //if the exitButtonRect is not clicked and the Pause Menu is shown
        if(exitButtonRect != null && showPauseMenu) {
            // if the cursor is on any of the Rect in the Pause Menu
            if (exitButtonRect.contains(p) || continueButtonRect.contains(p) || restartButtonRect.contains(p))
                //Change it to a hand cursor
                this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            else
                this.setCursor(Cursor.getDefaultCursor());
        }
        else{
            //else keep it as a default cursor
            this.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * onLostFocus Method:
     * To mention the functionality when the Window has lost focus
     */
    public void onLostFocus(){
        //Stop the game timer
        gameTimer.stop();
        //Print this message
        message = "Focus Lost";
        repaint();
    }

}