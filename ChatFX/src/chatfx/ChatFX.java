/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatfx;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Sydney
 */
public class ChatFX extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        ChatScene scene = new ChatScene();
        primaryStage.setTitle("ChatFX");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> {
            try {
                scene.end();
            } catch (IOException ex) {
                Logger.getLogger(ChatFX.class.getName()).log(Level.SEVERE, null, ex);
            }
      });
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
