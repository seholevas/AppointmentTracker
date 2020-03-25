/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import statics.CurrentUser;

/**
 * FXML Controller class
 *
 * @author Steven
 */
public class WelcomeController implements Initializable {

    //FXML variables
    @FXML
    private Label lblUser;
    @FXML
    private Label lblUserID;
    
    
    //class variable
    private static String fxmlPath ="/view/WelcomeView.fxml";
    /**
     * Initializes the controller class, Also sets up the welcome screen in order to welcome the specific user who signed in.
     * 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        lblUser.setText(CurrentUser.getUser().getUsername().toUpperCase());
        String id = CurrentUser.getUser().getID().toString();
        lblUserID.setText(id.toString());
    }   
    
    
    /**
     * @return the fxmlPath
     */
    public static String getFXMLPath() 
    {
        return fxmlPath;
    }
}
