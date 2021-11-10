package view;

import model.GameFrameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

/***
 * HomeMenuView Class extends JComponent and implements MouseListener and MouseMotionListener
 * to generate the HomeMenu of the Game
 */
public class HomeMenuView extends JComponent implements MouseListener, MouseMotionListener {
    //Static Variables for the strings
    private static final String GREETINGS = "Welcome to:";
    private static final String GAME_TITLE = "Brick Destroy";
    private static final String CREDITS = "Version 0.1";
    private static final String START_TEXT = "Start";
    private static final String MENU_TEXT = "Exit";
    //Static Variables to Specify the Colors in the Home Menu
    private static final Color BG_COLOR = Color.GREEN.darker();
    private static final Color BORDER_COLOR = new Color(200,8,21); //Venetian Red
    private static final Color DASH_BORDER_COLOR = new  Color(255, 216, 0);//school bus yellow
    private static final Color TEXT_COLOR = new Color(16, 52, 166);//egyptian blue
    private static final Color CLICKED_BUTTON_COLOR = BG_COLOR.brighter();
    private static final Color CLICKED_TEXT = Color.WHITE;
    private static final int BORDER_SIZE = 10;
    private static final float[] DASHES = {20,20};//the red dashes on the border

    //The Menu Board
    private Rectangle menuFace;
    //The Start Button
    private Rectangle startButton;
    //The Exit Button
    private Rectangle menuButton;

    //Statics Stroke Attribute
    private BasicStroke borderStoke = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND,0,DASHES,0);
    private BasicStroke borderStoke_noDashes = new BasicStroke(BORDER_SIZE,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);

    //Font of the Text in the Home Menu
    private Font greetingsFont = new Font("Noto Mono",Font.PLAIN,25);
    private Font gameTitleFont = new Font("Noto Mono",Font.BOLD,40);
    private Font creditsFont = new Font("Monospaced",Font.PLAIN,10);
    private Font buttonFont;

    //The JFrame Owner
    private GameFrameModel owner;

    //Boolean to mark when the start is clicked
    private boolean startClicked;
    //Boolean to mark when the exit is clicked
    private boolean menuClicked;


    /**
     * HomeMenuView CConstructor:
     * Implements the HomeMenuView Characteristics
     * @param owner
     * @param area
     */
    public HomeMenuView(GameFrameModel owner, Dimension area){
        this.setFocusable(true);
        this.requestFocusInWindow();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.owner = owner;
        //Define the Menu Area
        menuFace = new Rectangle(new Point(0,0),area);
        this.setPreferredSize(area);
        //Define the Dimension of the Button
        Dimension btnDim = new Dimension(area.width / 3, area.height / 12);
        //Associate Dimension with the Start Button
        startButton = new Rectangle(btnDim);
        //Associate Dimension with the Exit Button
        menuButton = new Rectangle(btnDim);
        //Font of the Button
        buttonFont = new Font("Monospaced",Font.PLAIN,startButton.height-2);
    }


    /**
     * paint Method:
     * To paint the output the Home Menu
     * @param g
     */
    public void paint(Graphics g){
        drawMenu((Graphics2D)g);
    }


    /**
     * drawMenu Method:
     * To draw the Home Menu Contents
     * @param g2d
     */
    public void drawMenu(Graphics2D g2d){
        //Calling the drawContainer method to create the container
        drawContainer(g2d);
        Color prevColor = g2d.getColor();
        Font prevFont = g2d.getFont();
        //x and y coordinates of the contents of the menu
        double x = menuFace.getX();
        double y = menuFace.getY();
        g2d.translate(x,y);

        //Method Call to Draw the Text
        drawText(g2d);
        //Method Call to Draw the Button
        drawButton(g2d);
        g2d.translate(-x,-y);
        g2d.setFont(prevFont);
        g2d.setColor(prevColor);
    }

    /**
     * drawContainer Method:
     * To implement the container characteristics
     * @param g2d
     */
    private void drawContainer(Graphics2D g2d){
        Color prev = g2d.getColor();
        // Set the Menu to the Background Color
        g2d.setColor(BG_COLOR);
        //Fill the Menu Area with the Color
        g2d.fill(menuFace);
        //Fill the MenuFace with the Border Dashes
        Stroke tmp = g2d.getStroke();
        g2d.setStroke(borderStoke_noDashes);
        g2d.setColor(DASH_BORDER_COLOR);
        g2d.draw(menuFace);
        //Fill the MenuFace with the Border Color
        g2d.setStroke(borderStoke);
        g2d.setColor(BORDER_COLOR);
        g2d.draw(menuFace);

        g2d.setStroke(tmp);
        g2d.setColor(prev);
    }

    /**
     * drawText Method:
     *Implements the Greetings , GameTitle , and various text at the home menu
     * @param g2d
     */
    private void drawText(Graphics2D g2d){

        g2d.setColor(TEXT_COLOR);

        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D greetingsRect = greetingsFont.getStringBounds(GREETINGS,frc);
        Rectangle2D gameTitleRect = gameTitleFont.getStringBounds(GAME_TITLE,frc);
        Rectangle2D creditsRect = creditsFont.getStringBounds(CREDITS,frc);

        int sX,sY;

        sX = (int)(menuFace.getWidth() - greetingsRect.getWidth()) / 2;
        sY = (int)(menuFace.getHeight() / 4);

        g2d.setFont(greetingsFont);
        g2d.drawString(GREETINGS,sX,sY);

        sX = (int)(menuFace.getWidth() - gameTitleRect.getWidth()) / 2;
        sY += (int) gameTitleRect.getHeight() * 1.1;//add 10% of String height between the two strings

        g2d.setFont(gameTitleFont);
        g2d.drawString(GAME_TITLE,sX,sY);

        sX = (int)(menuFace.getWidth() - creditsRect.getWidth()) / 2;
        sY += (int) creditsRect.getHeight() * 1.1;

        g2d.setFont(creditsFont);
        g2d.drawString(CREDITS,sX,sY);


    }

    /**
     * drawButton Method:
     * Implements the Button for the startButton and Exit Button
     * @param g2d
     */
    private void drawButton(Graphics2D g2d){

        FontRenderContext frc = g2d.getFontRenderContext();

        Rectangle2D txtRect = buttonFont.getStringBounds(START_TEXT,frc);
        Rectangle2D mTxtRect = buttonFont.getStringBounds(MENU_TEXT,frc);

        g2d.setFont(buttonFont);

        int x = (menuFace.width - startButton.width) / 2;
        int y =(int) ((menuFace.height - startButton.height) * 0.8);

        startButton.setLocation(x,y);

        x = (int)(startButton.getWidth() - txtRect.getWidth()) / 2;
        y = (int)(startButton.getHeight() - txtRect.getHeight()) / 2;

        x += startButton.x;
        y += startButton.y + (startButton.height * 0.9);




        if(startClicked){
            Color tmp = g2d.getColor();
            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(startButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(START_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.draw(startButton);
            g2d.drawString(START_TEXT,x,y);
        }

        x = startButton.x;
        y = startButton.y;

        y *= 1.2;

        menuButton.setLocation(x,y);




        x = (int)(menuButton.getWidth() - mTxtRect.getWidth()) / 2;
        y = (int)(menuButton.getHeight() - mTxtRect.getHeight()) / 2;

        x += menuButton.x;
        y += menuButton.y + (startButton.height * 0.9);

        if(menuClicked){
            Color tmp = g2d.getColor();

            g2d.setColor(CLICKED_BUTTON_COLOR);
            g2d.draw(menuButton);
            g2d.setColor(CLICKED_TEXT);
            g2d.drawString(MENU_TEXT,x,y);
            g2d.setColor(tmp);
        }
        else{
            g2d.draw(menuButton);
            g2d.drawString(MENU_TEXT,x,y);
        }

    }

    /**
     * mouseClicked Method:
     * To mention the function corresponding to the click of the mouse in the homemenu
     * @param mouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(startButton.contains(p)){
            owner.enableGameBoard();

        }
        else if(menuButton.contains(p)){
            System.out.println("Goodbye " + System.getProperty("user.name"));
            System.exit(0);
        }
    }

    /**mousePressed Method:
     * Implements the Functionality when the buttons are pressed but not released
     * @param mouseEvent
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(startButton.contains(p)){
            startClicked = true;
            repaint(startButton.x,startButton.y,startButton.width+1,startButton.height+1);

        }
        else if(menuButton.contains(p)){
            menuClicked = true;
            repaint(menuButton.x,menuButton.y,menuButton.width+1,menuButton.height+1);
        }
    }

    /**
     * mouseReleased Method:
     * Implements the functionality when the mouse is released from the button
     * @param mouseEvent
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        if(startClicked ){
            startClicked = false;
            repaint(startButton.x,startButton.y,startButton.width+1,startButton.height+1);
        }
        else if(menuClicked){
            menuClicked = false;
            repaint(menuButton.x,menuButton.y,menuButton.width+1,menuButton.height+1);
        }
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
     * mouseMoved Method:
     * Implements the functionality when the cursor hovers on the button
     * @param mouseEvent
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Point p = mouseEvent.getPoint();
        if(startButton.contains(p) || menuButton.contains(p))
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        else
            this.setCursor(Cursor.getDefaultCursor());

    }
}