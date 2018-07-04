/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatfx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 *
 * @author Sydney
 */
public class LaunchSelectionPane extends Pane{
    private Pane p = new Pane();
    private Label question = new Label("Launch as a Chat Server?");
    private Button yes = new Button("Yes");
    private Button no = new Button("No");
    public LaunchSelectionPane(){
        postion(question, 20, 20);
        postion(yes, 40, 40);
        postion(no, 80, 40);
        postion(p, 170, 200);
        LaunchSelectionPane me = this;
        yes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                me.changeForm(true);
            }
        });
        no.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                me.changeForm(false);
            }
        });
        p.getChildren().add(question);
        p.getChildren().add(yes);
        p.getChildren().add(no);
        this.getChildren().add(p);
    }
    private void changeForm(boolean answer){
        ((ChatScene)getScene()).showForm(answer);
    }
    private void postion(Node n, double x, double y){
        n.setLayoutX(x);
        n.setLayoutY(y);
    }
}
