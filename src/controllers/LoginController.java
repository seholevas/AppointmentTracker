/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

//imports
import LoggingFile.LogFile;
import java.util.logging.Logger;
import connections.DBConnection;
import exception.EmptyException;
import exception.InvalidCredentialsException;
import models.User;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import statics.CurrentUser;

/**
 * FXML Controller class
 *
 * @author Steven
 */
public class LoginController implements Initializable
{
    
    //FXML Variables
    @FXML
    private Label errorMessage;
    
    @FXML
    private TextField txtUsername;
    
    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private Button btnLogin;
    
    @FXML
    private Button btnClose;
    
    //class variable
    private final static String fxmlPath = "/view/LoginView.fxml";
    private final static Logger LOGGER = Logger.getLogger(LogFile.class.getName());

    
    //returns fxml file path
    static String getFXMLPath()
    {
        return fxmlPath;
    }
    
    //exits program if btnExit button is pressed
    @FXML
    private void actionExitButtonPressed(ActionEvent e) throws IOException
    {
        exitProgram();
    }
    
    //attempts to login if btnLogin is pressed
    @FXML
    private void actionLoginButtonPressed(ActionEvent e) throws IOException, SQLException
    {   
        //local variable setup
        User user = new User();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        
        
        //attempts to check credentials, if it fails either by a field being empty or incorrect it will throw an exception in the user's language
        try
        {
            checkUsersCredentials(user, username, password);
        }
        
        //if at least one field is empty catch here
        catch(EmptyException ee)
        {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.login", Locale.getDefault());
            errorMessage.setText(resourceBundle.getString("empty"));
            return;
        }
        //if credentials don't  match catch here
        catch(InvalidCredentialsException ice)
        {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("languages.login", Locale.getDefault());
            errorMessage.setText(resourceBundle.getString("incorrect"));
            return;
        }
        
        //goes to next scene if everything is done correctly
        nextScene(e);
    }
    
    //checks that all the fields are full and then checks that the user exists in thre database
    private void checkUsersCredentials(User _user, String _username, String _password) throws EmptyException, InvalidCredentialsException, SQLException
    {
        checkInputIsNotEmpty(_username);
        checkInputIsNotEmpty(_password);
        checkUserExists(_user, _username, _password);
    }
    
    //checks if passed string is empty, and throws an exception if it is
    private void checkInputIsNotEmpty(String value) throws EmptyException
    {
        if(value.isEmpty())
        {
            throw new EmptyException("Error in LoginController.checkInputIsNotEmpty(UserModel, String, String)|");
        }
    }
     
    //checks if the user exists in the database, throws an exception if the user does not exist
    private void checkUserExists(User passedUser, String passedUsername, String passedPassword) throws InvalidCredentialsException, SQLException
    {
        //returns a user within the database, or null
        passedUser = getCredentials(passedUser, passedUsername, passedPassword);
        
        if(passedUser == null)
        {
               throw new InvalidCredentialsException("Error in LoginController.checkUserExists(UserModel, String, String)| ");
        }
        else
        {
            //sets the current user if found in database
            CurrentUser.setUser(passedUser);
            LOGGER.log(Level.INFO, "{0} logged in", CurrentUser.getUser().getUsername());
            checkForMeeting();
        }
    }
    
    //attempts to retrieve a user in the database, if there is no such user then it returns null.
    private User getCredentials(User passedUser, String passedUsername, String passedPassword) throws SQLException
    {
        
            // PreparedStatment to hold/prepare the SQL string value that will be passed to Database in order to select username and password.
            PreparedStatement prepStatement = DBConnection.getConnection().prepareStatement("SELECT * FROM user WHERE userName=? AND password=?");
            // set strings are to set the above prepareStatement() "?" values.
            prepStatement.setString(1, passedUsername);
            prepStatement.setString(2, passedPassword);
            //searches database.
            ResultSet resultSet = prepStatement.executeQuery();
                    
            User tempUser = passedUser;
            
            //if the resultSet is not pointing to the last row. Meaning, there is a user with the prepStatements username and password.
            if(resultSet.next())
            {
                tempUser.setUsername(resultSet.getString("userName"));
                tempUser.setPassword(resultSet.getString("password"));
                tempUser.setID(""+resultSet.getInt("userId"));
                
                return tempUser;
            }   
        return null;
    }

    //grabs next scene
    private void nextScene(ActionEvent e) throws IOException
    {
        //loads the path to the fxml file        
        FXMLLoader loader = new FXMLLoader();
        
        loader.setLocation(getClass().getResource(BaseController.getFXMLPath()));

        //sets the pane to what the loader is pointed to load
        Parent baseView =  loader.load();      
        
        //creates a scene that is the BaseView pane/fxml
        Scene nextScene = new Scene(baseView);
        
        
        //gets stage information
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        
        
        //sets the next scene on the stage
        window.setScene(nextScene);
        //shows next scene on the stage
        window.show();
    }
    
    //exits program
    private void exitProgram()
    {
        System.exit(0);
    }
    
    //sets the appropraite language for each key phrase
    private void setAppropriateLanguage()
    {
        ResourceBundle rb = ResourceBundle.getBundle("languages.login", Locale.getDefault());
        txtUsername.setPromptText(rb.getString("username"));
        txtPassword.setPromptText(rb.getString("password"));
        btnLogin.setText(rb.getString("login"));
        
    }
    
    //initializes the scene
    @Override
    public void initialize(URL location, ResourceBundle resources) 
    {
        setAppropriateLanguage();
    }
    
    public void checkForMeeting() throws SQLException
    {
        //formats LocalDateTime
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        //gets the time now
        ZonedDateTime zdt = ZonedDateTime.now();
        ZonedDateTime utc = zdt.withZoneSameInstant(ZoneId.of("UTC"));
        //String to localize timezone
        String dateTime = utc.format(format);
        PreparedStatement prepStatement = DBConnection.getConnection().prepareStatement(
        "SELECT * " +
        "FROM appointment, customer, user " +
        "WHERE appointment.customerId = customer.customerId AND appointment.userId = user.userId AND appointment.appointmentId = appointment.appointmentId AND user.userId = ? AND appointment.start <= ? + INTERVAL 15 MINUTE AND appointment.start >= ? - INTERVAL 15 MINUTE");
         
         prepStatement.setString(1, CurrentUser.getUser().getID());
         prepStatement.setString(2, dateTime);
         prepStatement.setString(3, dateTime);
         
         ResultSet resultSet = prepStatement.executeQuery();
         
         if(resultSet.next())
         {
             JOptionPane.showMessageDialog(null, String.format("ATTENTION %s: YOU HAVE AN APPOINTMENT IN 15 MINUTES OR LESS!",CurrentUser.getUser().getUsername()));
         }
    }
}
