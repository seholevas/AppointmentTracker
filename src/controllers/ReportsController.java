/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import connections.DBConnection;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Address;
import models.Appointment;
import models.Customer;
import models.User;
import statics.CurrentUser;

/**
 * FXML Controller class
 *
 * @author Steven
 */
public class ReportsController implements Initializable 
{

    
    @FXML
    private TableView<Appointment> tableAppointmentsByMonth;
    @FXML
    private TableColumn<Appointment, String> columnMonth1;
    @FXML
    private TableColumn<Appointment, String> columnType1;
    @FXML
    private TableColumn<Appointment, Integer> columnNumber1;
    @FXML
    private TableView<Appointment> tableAppointmentsByLocation;
    @FXML
    private TableColumn<Appointment, String> columnLocation2;
    @FXML
    private TableColumn<Appointment, Integer> columnNumber2;
    @FXML
    private TableView<Appointment> tableConsultantsAppointments;
    @FXML
    private TableColumn<Appointment, String> columnDate3;
    @FXML
    private TableColumn<Appointment, String> columnStart3;
    @FXML
    private TableColumn<Appointment, String> columnEnd3;
    @FXML
    private TableColumn<Appointment, String> columnCustomer3;
    @FXML
    private TableColumn<Appointment, String> columnType3;
    
    ObservableList<Appointment> list1 = FXCollections.observableArrayList();
    ObservableList<Appointment> list2 = FXCollections.observableArrayList();
    ObservableList<Appointment> list3 = FXCollections.observableArrayList();
    
    
    
    private static final String fxmlPath = "/view/ReportsView.fxml";
    
    public static String getFXMLPath()
    {
        return fxmlPath;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try 
        {
            populateTable();
        }
        
        catch (SQLException ex) 
        {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void populateTable() throws SQLException
    {
        clearLists();
        addAppointmentsToLists();
        setCellValueFactories();
       tableAppointmentsByMonth.setItems(list1);
       tableAppointmentsByLocation.setItems(list2);
       tableConsultantsAppointments.setItems(list3);
    }
    
    private void setCellValueFactories()
    {
        columnMonth1.setCellValueFactory(new PropertyValueFactory<>("month"));
        columnType1.setCellValueFactory(new PropertyValueFactory<>("type"));
        columnNumber1.setCellValueFactory(new PropertyValueFactory<>("number"));
        
        columnLocation2.setCellValueFactory(new PropertyValueFactory<>("location"));
        columnNumber2.setCellValueFactory(new PropertyValueFactory<>("number"));
        
        
        columnDate3.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnStart3.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        columnEnd3.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        columnCustomer3.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        columnType3.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    private void addAppointmentsToLists() throws SQLException 
    {     
        
           
        
           fillList1();
           PreparedStatement prepStatement;
           
        prepStatement = DBConnection.getConnection().prepareStatement(
        "SELECT appointment.location AS \"Location\", appointment.type AS  \"Type\", COUNT(*) AS \"Number\" " +
        "FROM appointment, customer, user " +
        "WHERE appointment.customerId = customer.customerId AND appointment.userId = user.userId AND appointment.appointmentId = appointment.appointmentId " + 
        "GROUP BY appointment.location");
        
        //result set to execute statement
        ResultSet resultSet = prepStatement.executeQuery();  
        while(resultSet.next())
        {
            list2.add(new Appointment(resultSet.getString("Location"), resultSet.getString("Type"), Integer.parseInt(resultSet.getString("Number"))));
        }
        
        
        
        
        
        
        prepStatement = DBConnection.getConnection().prepareStatement(
        "SELECT * " +
        "FROM appointment, customer, user " +
        "WHERE appointment.customerId = customer.customerId AND appointment.userId = user.userId AND appointment.appointmentId = appointment.appointmentId AND user.userId = ? " +
        "ORDER BY appointment.start");
         
         prepStatement.setString(1, CurrentUser.getUser().getID());
        
        //result set to execute statement
        resultSet = prepStatement.executeQuery();    
        
        //creates a new appt and adds them to the list
        while(resultSet.next())
        {
            User user = new User(resultSet.getInt("user.userId")+"", resultSet.getString("user.userName"), resultSet.getString("user.password"));
            Customer customer = new Customer(resultSet.getInt("customer.customerId")+"", resultSet.getString("customer.customerName"), resultSet.getString("customer.active"), new Address());
            list3.add(new Appointment(resultSet.getString("appointment.appointmentId"), customer, user, resultSet.getString("appointment.title"), resultSet.getString("appointment.description"), resultSet.getString("appointment.location"), resultSet.getString("appointment.contact"), resultSet.getString("appointment.type"), resultSet.getString("appointment.url"), getLocalDateTime(resultSet.getString("appointment.start")), getLocalDateTime(resultSet.getString("appointment.end"))));
        }
        
        
        
        
        
    }
    private void fillList1() throws SQLException
    {
        addToList1("DECISICION-MAKING MEETING", "2019-01-10", "JANUARY", "01");
        addToList1("INNOVATION MEETING", "2019-01-10", "JANUARY", "01");
        addToList1("STATUS UPDATE MEETING", "2019-01-10", "JANUARY", "01");
        addToList1("DECISICION-MAKING MEETING", "2019-02-10", "FEBRUARY", "02");
        addToList1("INNOVATION MEETING", "2019-02-10", "FEBRUARY", "02");
        addToList1("STATUS UPDATE MEETING", "2019-02-10", "FEBRUARY", "02");
        addToList1("DECISICION-MAKING MEETING", "2019-03-10", "MARCH", "03");
        addToList1("INNOVATION MEETING", "2019-03-10", "MARCH", "03");
        addToList1("STATUS UPDATE MEETING", "2019-03-10", "MARCH", "03");
        addToList1("DECISICION-MAKING MEETING", "2019-04-10", "APRIL", "04");
        addToList1("INNOVATION MEETING", "2019-04-10", "APRIL", "04");
        addToList1("STATUS UPDATE MEETING", "2019-04-10", "APRIL", "04");
        addToList1("DECISICION-MAKING MEETING", "2019-05-10", "MAY", "05");
        addToList1("INNOVATION MEETING", "2019-05-10", "MAY", "05");
        addToList1("STATUS UPDATE MEETING", "2019-05-10", "MAY", "05");
        addToList1("DECISICION-MAKING MEETING", "2019-06-10", "JUNE", "06");
        addToList1("INNOVATION MEETING", "2019-06-10", "JUNE", "06");
        addToList1("STATUS UPDATE MEETING", "2019-06-10", "JUNE", "06");
        addToList1("DECISICION-MAKING MEETING", "2019-07-10", "JULY", "07");
        addToList1("INNOVATION MEETING", "2019-07-10", "JULY", "07");
        addToList1("STATUS UPDATE MEETING", "2019-07-10", "JULY", "07");
        addToList1("DECISICION-MAKING MEETING", "2019-08-10", "AUGUST", "08");
        addToList1("INNOVATION MEETING", "2019-08-10", "AUGUST", "08");
        addToList1("STATUS UPDATE MEETING", "2019-08-10", "AUGUST", "08");
        addToList1("DECISICION-MAKING MEETING", "2019-09-10", "SEPTEMBER", "09");
        addToList1("INNOVATION MEETING", "2019-09-10", "SEPTEMBER", "09");
        addToList1("STATUS UPDATE MEETING", "2019-09-10", "SEPTEMBER", "09");
        addToList1("DECISICION-MAKING MEETING", "2019-10-10", "OCTOBER", "10");
        addToList1("INNOVATION MEETING", "2019-10-10", "OCTOBER", "10");
        addToList1("STATUS UPDATE MEETING", "2019-10-10", "OCTOBER", "10");
        addToList1("DECISICION-MAKING MEETING", "2019-11-10", "NOVEMBER", "11");
        addToList1("INNOVATION MEETING", "2019-11-10", "NOVEMBER", "11");
        addToList1("STATUS UPDATE MEETING", "2019-11-10", "NOVEMBER", "11");
        addToList1("DECISICION-MAKING MEETING", "2019-12-10", "DECEMBER", "12");
        addToList1("INNOVATION MEETING", "2019-12-10", "DECEMBER", "12");
        addToList1("STATUS UPDATE MEETING", "2019-12-10", "DECEMBER", "12");
        
    }
    private void addToList1(String _meetingType, String _exampleDate, String _monthName,String _numOfMonth) throws SQLException
    {
        int i = 0;
        PreparedStatement prepStatement = DBConnection.getConnection().prepareStatement(
        "SELECT appointment.start " +
        "FROM appointment, customer, user " +
        "WHERE appointment.customerId = customer.customerId AND appointment.userId = user.userId AND appointment.appointmentId = appointment.appointmentId AND appointment.type = ? AND MONTHNAME(appointment.start)=?");
        
        prepStatement.setString(1, _meetingType);
        prepStatement.setString(2, _monthName);
        
        
        //result set to execute statement
        ResultSet resultSet = prepStatement.executeQuery();
        
        //creates a new customer and adds them to the list
        while(resultSet.next())
        {  
            i++;
//            System.out.println(_monthName+": "+ _meetingType + " i:"+ i +" ");
        }
        if(i >= 1 )
        {
            list1.add(new Appointment(i, _monthName, _meetingType));
        }
        
    }
    private void clearLists() {
        list1.clear();
        list2.clear();
        list3.clear();
    }
    
    private String getLocalDateTime(String _sqlDateTime)
    {
        //formats LocalDateTime
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        //variables for date and time
        Integer year = Integer.parseInt(_sqlDateTime.substring(0, 4));
        Integer month = Integer.parseInt(_sqlDateTime.substring(5, 7));
        Integer day = Integer.parseInt(_sqlDateTime.substring(8, 10));
        Integer hour = Integer.parseInt(_sqlDateTime.substring(11, 13));
        Integer minute = Integer.parseInt(_sqlDateTime.substring(14, 16));
        
        LocalDateTime ldt = LocalDateTime.of(year,month, day, hour, minute);
        ZonedDateTime utcZonedDateTime = ZonedDateTime.of(ldt, ZoneId.of("UTC"));
        ZonedDateTime usersZonedDateTime = utcZonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        
        
        return usersZonedDateTime.format(format);
    }
    
}
