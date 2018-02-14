/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notifcationexamples;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import taskers.*;

/**
 * FXML Controller class
 *
 * @author dalemusser
 */
public class NotificationsUIController implements Initializable, Notifiable {

    @FXML
    private TextArea textArea;
    
    @FXML
    private Button task1Button;
    
    @FXML
    private Button task2Button;
    
    @FXML
    private Button task3Button;
    
    private Task1 task1;
    private Task2 task2;
    private Task3 task3;
    
    private boolean task1Running = false;
    private boolean task2Running = false;
    private boolean task3Running = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void start(Stage stage) {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if (task1 != null) task1.end();
                if (task2 != null) task2.end();
                if (task3 != null) task3.end();
            }
        });
    }
    
    @FXML
    public void startTask1(ActionEvent event) {
        System.out.println("start task 1");
        if (task1 == null) {
            task1 = new Task1(2147483647, 1000000);
            task1.setNotificationTarget(this);
            task1.start();
            stateChanged("task1");
            
        } else {
            task1.end();
            stateChanged("task1");
            task1 = null;
        }
    }
    
    @Override
    public void notify(String message) {
        if (message.equals("Task1 done.")) {
            task1 = null;
            stateChanged("task1");
        }
        textArea.appendText(message + "\n");
    }
    
    @FXML
    public void startTask2(ActionEvent event) {
        System.out.println("start task 2");
        if (task2 == null) {
            task2 = new Task2(2147483647, 1000000);
            task2.setOnNotification((String message) -> {
                textArea.appendText(message + "\n");
                try {
                        if(!task2.isAlive()){
                            stateChanged("task2");
                            task2 = null;
                        }
                    }
                    catch(NullPointerException e){
                        
                    }
            });
            
            task2.start();
            stateChanged("task2");
            
        } else {
            task2.end();
            stateChanged("task2");
            task2 = null;
        }      
    }
    
    @FXML
    public void startTask3(ActionEvent event) {
        System.out.println("start task 3");
        if (task3 == null) {
            task3 = new Task3(2147483647, 1000000);
            // this uses a property change listener to get messages
            task3.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    textArea.appendText((String)evt.getNewValue() + "\n");
                    try {
                        if(!task3.isAlive()){
                            stateChanged("task3");
                            task3 = null;
                        }
                    }
                    catch(NullPointerException e){
                        
                    }
                }
            });
            
            task3.start();
            stateChanged("task3");
            
        } else {
            task3.end();
            stateChanged("task3");
            task3 = null;
        }
    } 
    
    public void stateChanged(String task){
        switch(task){
            case "task1":
                if(task1Running == false){
                    task1Running = true;
                    task1Button.setText("Stop Task 1");
                } else {
                    task1Running = false;
                    task1Button.setText("Start Task 1");
                }
                break;
            case "task2":
                if(task2Running == false){
                    task2Running = true;
                    task2Button.setText("Stop Task 2");
                } else {
                    task2Running = false;
                    task2Button.setText("Start Task 2");
                }
                break; 
            case "task3":
                if(task3Running == false){
                    task3Running = true;
                    task3Button.setText("Stop Task 3");
                } else {
                    task3Running = false;
                    task3Button.setText("Start Task 3");
                }
                break;
        }
    }
    
   
}
