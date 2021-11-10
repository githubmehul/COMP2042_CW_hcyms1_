package game;

import model.GameFrameModel;

import java.awt.*;

/***
 * Main Method calls the GameFrameModel Class's initialize method
 */
public class Main {

    public static void main(String[] args){

        EventQueue.invokeLater(() -> new GameFrameModel().initialize());
    }

}