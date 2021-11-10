/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2017  Filippo Ranza
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package view;

import controller.BallController;
import controller.DebugPanelController;
import model.WallModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/***
 * DebugConsoleView Class extends JDialog and implements WindowListener
 * to generate the GameBoard(The Actual Game on the GameFrameModel).
 */
public class DebugConsoleView extends JDialog implements WindowListener{
    //The title of the Debug Console Window
    private static final String TITLE = "Dialog Console";


    private JFrame owner;
    private DebugPanelController debugPanel;
    private GameBoardView gameBoard;
    private WallModel wall;


    /**
     *DebugConsoleView Constructor:
     * Takes in the parameter owner , wall and gameboard
     * @param owner
     * @param wall
     * @param gameBoard
     */
    public DebugConsoleView(JFrame owner, WallModel wall, GameBoardView gameBoard){

        this.wall = wall;
        this.owner = owner;
        this.gameBoard = gameBoard;
        //Calling the initialize method
        initialize();

        // debugPanel is an object of the DebugPanelController Method
        debugPanel = new DebugPanelController(wall);
        //Add the Panel , and keep the Border Layout as Center
        this.add(debugPanel,BorderLayout.CENTER);
        this.pack();
    }

    /**
     * initialize Method:
     * Initializes the DebugConsole
     */
    private void initialize(){
        this.setModal(true);
        this.setTitle(TITLE);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.addWindowListener(this);
        this.setFocusable(true);
    }


    /**
     * setLocation Method:
     * Set the Coordinates of the Debug Console
     */
    private void setLocation(){
        int x = ((owner.getWidth() - this.getWidth()) / 2) + owner.getX();
        int y = ((owner.getHeight() - this.getHeight()) / 2) + owner.getY();
        this.setLocation(x,y);
    }


    @Override
    public void windowOpened(WindowEvent windowEvent) {

    }

    @Override
    public void windowClosing(WindowEvent windowEvent) {
        gameBoard.repaint();
    }

    @Override
    public void windowClosed(WindowEvent windowEvent) {

    }

    @Override
    public void windowIconified(WindowEvent windowEvent) {

    }

    @Override
    public void windowDeiconified(WindowEvent windowEvent) {

    }

    /**
     * windowActivated Method:
     * When the DebugConsole is called , set the speed of the ball
     * @param windowEvent
     */
    @Override
    public void windowActivated(WindowEvent windowEvent) {
        setLocation();
        BallController b = wall.ball;
        debugPanel.setValues(b.getSpeedX(),b.getSpeedY());
    }

    @Override
    public void windowDeactivated(WindowEvent windowEvent) {

    }
}