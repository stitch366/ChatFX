package chatfx;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 *
 * @author Sydney
 */
public class FormPane extends Pane{
    private Pane p;
    private TextField[] feilds = new TextField[3];
    private boolean serverFrm;
    private final FormPane me = this;
    public FormPane(boolean serverForm){
        this.p = new Pane();
        this.serverFrm = serverForm;
        LabelandTXT("Screen Name: ", 0, 20, 20, 110);
        if(serverForm){
            LabelandTXT("Port No: ", 1, 20, 60, 110);
        }
        else{
            LabelandTXT("Server IP: ", 1, 20, 60, 110);
            LabelandTXT("Port No: ", 2, 20, 100, 110);
        }
        
        setPrefSize(500, 600);
        Button btn = new Button();
        btn.setText("Go");
        btn.setLayoutX(20);
        btn.setLayoutY(140);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String n = feilds[0].getText();
                String ip;
                String port;
                if(serverFrm){
                    ip = "127.0.0.1";
                    port = feilds[1].getText();
                }
                else{
                    ip = feilds[1].getText();
                    port = feilds[2].getText();
                }
                if( !(ip.equals("")) && !(n.equals("")) && !(port.equals(""))){
                    me.startChat(n, ip, port);
                }
            }
        });
        this.p.getChildren().add(btn);
        p.setLayoutX(100);
        p.setLayoutY(200);
        getChildren().add(this.p);
    }
    
    private void startChat(String name, String ip, String port){
        ChatPane p;
        try {
            if(serverFrm){
                ((ChatScene)getScene()).setServer(new ChatServer(Integer.parseInt(port)));
                ((ChatScene)getScene()).startServer();
            }
            ((ChatScene)getScene()).setChat(new ChatPane(name, ip, port));
        
        } catch (IOException ex) {
            Logger.getLogger(FormPane.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private void LabelandTXT(String label, int i,int x, int y, int x2){
        Label l = new Label(label);
        TextField f = new TextField();
        feilds[i]=f;
        l.setLayoutX(x);
        l.setLayoutY(y);
        f.setLayoutX(x2);
        f.setLayoutY(y);
        this.p.getChildren().add(l);
        this.p.getChildren().add(f);
    }
}
