package chatfx;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.Scene;

/**
 *
 * @author Sydney
 */
public class ChatScene extends Scene{
    private ChatServer server;
    private ChatPane chat;
    
    public ChatScene(){
        super(new LaunchSelectionPane(),500,600);
    }
    public void showForm(boolean isserver){
        this.setRoot(new FormPane(isserver));
    }
    public ChatServer getServer() {
        return server;
    }
    public void startServer(){
        server.start();
    }
    public void setServer(ChatServer server) {
        this.server = server;
    }
    public ChatPane getChat() {
        return chat;
    }

    public void setChat(ChatPane chat) {
        this.chat = chat;
        this.setRoot(this.chat);
    }
    public void end() throws IOException{
        if(this.chat != null){
            this.chat.end();
        }
        if(this.server != null){
            this.server.end();
        }
    }
    
}
