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
import models.City;
import models.Country;
import models.Customer;
import models.User;

/**
 * FXML Controller class
 *
 * @author Steven
 */
public class CalendarController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    private static final String fxmlPath = "/view/CalendarView.fxml";
    
    public static String getFXMLPath()
    {
        return fxmlPath;
    }
    @FXML
    private TableView<Appointment> tableWeek;
    @FXML
    private TableColumn<Appointment, String> columnDate1;
    @FXML
    private TableColumn<Appointment, String> columnStartTime1;
    @FXML
    private TableColumn<Appointment, String> columnEndTime1;
    @FXML
    private TableColumn<Appointment, String> columnID1;
    @FXML
    private TableColumn<Appointment, String> columnCustomer1;
    @FXML
    private TableColumn<Appointment, String> columnLocation1;
    @FXML
    private TableColumn<Appointment, String> columnType1;
    @FXML
    private TableColumn<Appointment, String> columnDescription1;
    @FXML
    private TableView<Appointment> tableMonth;
    @FXML
    private TableColumn<Appointment, String> columnDate2;
    @FXML
    private TableColumn<Appointment, String> columnStartTime2;
    @FXML
    private TableColumn<Appointment, String> columnEndTime2;
    @FXML
    private TableColumn<Appointment, String> columnID2;
    @FXML
    private TableColumn<Appointment, String> columnCustomer2;
    @FXML
    private TableColumn<Appointment, String> columnLocation2;
    @FXML
    private TableColumn<Appointment, String> columnType2;
    @FXML
    private TableColumn<Appointment, String> columnDescription2;
    
    private ObservableList<Appointment> monthappointments = FXCollections.observableArrayList();
    private ObservableList<Appointment> weekappointments = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            populateTable();
        } catch (SQLException ex) {
            Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
     private void addAppointmentsToList() throws SQLException
    {
        PreparedStatement prepStatement = DBConnection.getConnection().prepareStatement(
        "SELECT * " +
        "FROM appointment, customer, user " +
        "WHERE appointment.start <= CURRENT_DATE + INTERVAL 1 MONTH AND appointment.start > CURRENT_DATE AND appointment.customerId = customer.customerId AND appointment.userId = user.userId " +
        "ORDER BY appointment.start");
        
        //result set to execute statement
        ResultSet resultSet = prepStatement.executeQuery();    
        
        //creates a new appointment and adds them to the list
        while(resultSet.next())
        {
            
//            Country country = new Country(resultSet.getInt("country.countryId")+"", resultSet.getString("country.country"));
//            City city = new City(resultSet.getInt("city.cityId")+"", resultSet.getString("city.city"), country);
//            Address address = new Address(resultSet.getInt("address.addressId")+"", resultSet.getString("address.address"), resultSet.getString("address.address2"), city, resultSet.getString("address.postalCode"), resultSet.getString("address.phone"));
            User user = new User(resultSet.getInt("user.userId")+"", resultSet.getString("user.userName"), resultSet.getString("user.password"));
            Customer customer = new Customer(resultSet.getInt("customer.customerId")+"", resultSet.getString("customer.customerName"), resultSet.getString("customer.active"), new Address());
            monthappointments.add(new Appointment(resultSet.getString("appointment.appointmentId"), customer, user, resultSet.getString("appointment.title"), resultSet.getString("appointment.description"), resultSet.getString("appointment.location"), resultSet.getString("appointment.contact"), resultSet.getString("appointment.type"), resultSet.getString("appointment.url"), getLocalDateTime(resultSet.getString("appointment.start")), getLocalDateTime(resultSet.getString("appointment.end"))));
            
       }
        
        prepStatement = DBConnection.getConnection().prepareStatement(
        "SELECT * " +
        "FROM appointment, customer, user " +
        "WHERE appointment.start <= CURRENT_DATE + INTERVAL 7 DAY AND appointment.start > CURRENT_DATE AND appointment.customerId = customer.customerId AND appointment.userId = user.userId " +
        "ORDER BY appointment.start");
        
        //result set to execute statement
        resultSet = prepStatement.executeQuery(); 
//        i= 0;
        //creates a new appointment and adds them to the list
        while(resultSet.next())
        {
    
           
            User user = new User(resultSet.getInt("user.userId")+"", resultSet.getString("user.userName"), resultSet.getString("user.password"));
            Customer customer = new Customer(resultSet.getInt("customer.customerId")+"", resultSet.getString("customer.customerName"), resultSet.getString("customer.active"), new Address());
            weekappointments.add(new Appointment(resultSet.getString("appointment.appointmentId"), customer, user, resultSet.getString("appointment.title"), resultSet.getString("appointment.description"), resultSet.getString("appointment.location"), resultSet.getString("appointment.contact"), resultSet.getString("appointment.type"), resultSet.getString("appointment.url"), getLocalDateTime(resultSet.getString("appointment.start")), getLocalDateTime(resultSet.getString("appointment.end"))));
        }
    }
    
    private void populate()
    {
        setCellValueFactories();
    }
    
    private void populateTable() throws SQLException
    {
        clearList();
        addAppointmentsToList();
        setCellValueFactories();
       tableWeek.setItems(weekappointments);
       tableMonth.setItems(monthappointments);
       
    }
    
    private void clearList() 
    {
        monthappointments.clear();
        weekappointments.clear();
    }
    
    
    private void setCellValueFactories()
    {
        columnDate1.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnStartTime1.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        columnEndTime1.setCellValueFactory(new PropertyValueFactory("endTime"));
        columnID1.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        columnCustomer1.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        columnLocation1.setCellValueFactory(new PropertyValueFactory<>("location"));
        columnType1.setCellValueFactory(new PropertyValueFactory<>("type"));
        columnDescription1.setCellValueFactory(new PropertyValueFactory<>("description"));
        
        
        columnDate2.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnStartTime2.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        columnEndTime2.setCellValueFactory(new PropertyValueFactory("endTime"));
        columnID2.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        columnCustomer2.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        columnLocation2.setCellValueFactory(new PropertyValueFactory<>("location"));
        columnType2.setCellValueFactory(new PropertyValueFactory<>("type"));
        columnDescription2.setCellValueFactory(new PropertyValueFactory<>("description"));
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
