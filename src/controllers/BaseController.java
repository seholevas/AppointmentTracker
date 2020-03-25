/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import models.User;
import statics.CurrentUser;



/**
 *
 * @author Steven
 */
public class BaseController<T> implements Initializable
{
    
    @FXML
    Button btnExit;
    
    Button btnCustomers;
    
    @FXML
    Button btnAppointments;
    
    @FXML
    Button btnCalendar;
    
    @FXML
    Button btnSchedule;
    
    @FXML
    Button btnBrowse;
    
    @FXML
    private BorderPane myPane;
    
    @FXML
    private Button btnCostomers;
    
    private Parent root;
    
    private final static String fxmlPath = "/view/BaseView.fxml";
    

    static String getFXMLPath()
    {
        return fxmlPath;
    }
    
    @FXML
    public void actionExitButtonPressed(ActionEvent e) throws IOException
    {
        exitProgram();
    }
    
    @FXML
    public void actionCustomersButtonPressed(ActionEvent e) throws IOException
    {
        load(CustomerController.getFXMLPath());
        myPane.setCenter(root);
    }
    @FXML
    public void actionAppointmentsButtonPressed(ActionEvent e) throws IOException
    {
        load(AppointmentsController.getFXMLPath());
        myPane.setCenter(root);
    }
    
    @FXML
    public void actionCalendarButtonPressed(ActionEvent e) throws IOException
    {
        load(CalendarController.getFXMLPath());
        myPane.setCenter(root);
    }
    
    @FXML
    public void actionScheduleButtonPressed(ActionEvent e) throws IOException
    {
        load(ReportsController.getFXMLPath());
        myPane.setCenter(root);
    }
    
    @FXML
    public void actionBrowseButtonPressed(ActionEvent e) throws IOException
    {
        exitProgram();
    }
    
    private void exitProgram()
    {
        System.exit(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {       
        try 
        {
            load(WelcomeController.getFXMLPath());
                 
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(BaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
        myPane.setCenter(root);
    }
    
    public void load(String fxmlFilePath) throws IOException
    {
        setRoot(fxmlFilePath);
    }
    
    private void setRoot(String fxmlFilePath) throws IOException
    {
        root = FXMLLoader.load(getClass().getResource(fxmlFilePath));
    }
}
