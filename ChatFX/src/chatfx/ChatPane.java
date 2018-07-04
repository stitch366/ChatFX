package chatfx;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

/**
 *
 * @author Sydney
 */
public class ChatPane extends VBox{
    private ListView<String> msgs = new ListView<String>();
    private TextArea txt = new TextArea();
    private String name;
    private Socket socket;
    private Listener listen;
    private boolean active = true;
    private ObjectInputStream from;
    private ObjectOutputStream to;
    public ChatPane(String name, String ip, String port) throws IOException{
        ObservableList<String> init = FXCollections.observableArrayList();
        msgs.setItems(init);
        this.name = name;
        this.socket = new Socket(ip, Integer.parseInt(port));
        this.from = new ObjectInputStream(socket.getInputStream());
        this.to = new ObjectOutputStream(socket.getOutputStream());
        this.listen = new Listener();
        Label label = new Label("Enter Message: ");
        msgs.setPrefSize(500, 500);
        txt.setPrefSize(500, 100);
        txt.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                String msg = txt.getText();
                txt.setText("");
                try {
                    send(msg);
                } catch (IOException ex) {
                    Logger.getLogger(ChatPane.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        getChildren().add(msgs);
        getChildren().add(label);
        getChildren().add(txt);
        setPrefSize(500, 600);
        listen.setName("Client-Listener");
        listen.start();
        this.send(this.name);
        
    }
    public void end(){
        this.active = false;
        try {
            this.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ChatPane.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void send(String msg) throws IOException{
        to.writeObject(msg);
    }
    public void recive() throws IOException, ClassNotFoundException{
        String str = (String)from.readObject();
        Platform.runLater(new AddMsg(str));
    }
    public class AddMsg extends Task<Void>{
        private String str;
        public AddMsg(String s){
            this.str = s;
        }
        @Override public Void call() {
           msgs.getItems().add(str);
           return null;
        }
    }
    public class Listener extends Thread{
        public Listener(){
        }
        @Override
        public void run(){
            while(active){
                try {
                    recive();
                } catch (Exception ex) {
                    if(active){
                        active = false;
                        System.out.println("Server has Disconnected");
                    }
                } 
            }
        }
    }
}
