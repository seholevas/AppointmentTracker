/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import connections.DBConnection;
import exception.EmptyException;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import models.Address;
import models.Appointment;
import models.City;
import models.Country;
import models.Customer;
import models.User;
import statics.CurrentUser;

/**
 * FXML Controller class
 *
 * @author Steven
 */
public class AppointmentsController implements Initializable {

    @FXML
    private ComboBox<String> comboboxStartTime;
    @FXML
    private ComboBox<String> comboboxEndTime;
    @FXML
    private ComboBox<String> comboboxType;
    
    @FXML
    private ComboBox<User> comboboxConsultantName;
    
    @FXML
    private ComboBox<Customer> comboboxCustomerName;
    @FXML
    private TextField txtCustomerID;
    @FXML
    private TextField txtContact;
    @FXML
    private TextField txtLocation;
    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtDescription;
   
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;

    @FXML
    private DatePicker datepickerPicked;
            
            
    @FXML
    private TableView<Appointment> tableAppointments;
    @FXML
    private TableColumn<Appointment, String> columnStart;
    @FXML
    private TableColumn<Appointment, String> columnEnd;
    @FXML
    private TableColumn<Appointment, String> columnDate;
    @FXML
    private TableColumn<Appointment, String> columnLocation;
    @FXML
    private TableColumn<Appointment, String> columnTitle;
    @FXML
    private TableColumn<Appointment, String> columnDescription;
    
    private static final String fxmlPath = "/view/AppointmentsView.fxml";
    
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private ObservableList<String> meetingType = FXCollections.observableArrayList("STATUS UPDATE MEETING","DECISICION-MAKING MEETING","INNOVATION MEETING");    
    private ObservableList<String> availableTimes = FXCollections.observableArrayList();
    private ObservableList<Customer> customerNames = FXCollections.observableArrayList();
    private ObservableList<User> userNames = FXCollections.observableArrayList();
    
    private boolean newAppointment = false;
    
    //initializes controller    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        try 
        {
            populateTable();
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null,"ERROR: EXCEPTION, PLEASE CHECK YOUR CODE.");
            Logger.getLogger(AppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void add() throws SQLException, IOException
    {   
        checkForInvalidCustomerData(comboboxCustomerName.getValue());
        PreparedStatement prepStatement = null;
        Integer  appointmentId = randomNumberGenerator();
        Integer url = randomNumberGenerator();
       
        prepStatement = DBConnection.getConnection().prepareStatement("INSERT INTO appointment (appointmentId, customerId, userId, title, description, location, contact, type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)");
        
        prepStatement.setString(1, ""+appointmentId);
        prepStatement.setString(2, comboboxCustomerName.getValue().getCustomerID());        
        prepStatement.setString(3, comboboxConsultantName.getValue().getID());
        prepStatement.setString(4, txtTitle.getText());
        prepStatement.setString(5, txtDescription.getText());
        prepStatement.setString(6, txtLocation.getText());
        prepStatement.setString(7, txtContact.getText());
        prepStatement.setString(8, comboboxType.getValue());
        prepStatement.setString(9, url.toString());
        prepStatement.setString(10, getUTCDateTime(datepickerPicked.getValue().getYear(), datepickerPicked.getValue().getMonthValue(), datepickerPicked.getValue().getDayOfMonth(), Integer.parseInt(comboboxStartTime.getValue().substring(0, 2)), Integer.parseInt(comboboxStartTime.getValue().substring(3, 5))));
        prepStatement.setString(11, getUTCDateTime(datepickerPicked.getValue().getYear(), datepickerPicked.getValue().getMonthValue(), datepickerPicked.getValue().getDayOfMonth(),Integer.parseInt(comboboxEndTime.getValue().substring(0, 2)), Integer.parseInt(comboboxEndTime.getValue().substring(3, 5))));       
        prepStatement.setString(12, CurrentUser.getUser().getUsername());
        prepStatement.setString(13, CurrentUser.getUser().getUsername());
        
        prepStatement.execute();
        JOptionPane.showMessageDialog(null, String.format("CUSTOMER APPOINTMENT FOR %s HAS BEEN SUCESSFULLY ADDED", comboboxCustomerName.getValue().getCustomerName()));
    }
    
    private void addAppointmentsToList() throws SQLException
    {
        PreparedStatement prepStatement = DBConnection.getConnection().prepareStatement(
        "SELECT * " + 
        "FROM appointment, customer, user, address, city, country " +
        "WHERE appointment.customerId = customer.customerId AND appointment.userId = user.userId AND appointment.appointmentId = appointment.appointmentId AND address.cityId = city.cityId AND city.countryId = country.countryId and customer.addressId = address.addressId " +
        "ORDER BY appointment.start");
        
        //result set to execute statement
        ResultSet resultSet = prepStatement.executeQuery();    
        
        //creates a new customer and adds them to the list
        while(resultSet.next())
        {
            User user = new User(resultSet.getInt("user.userId")+"", resultSet.getString("user.userName"), resultSet.getString("user.password"));
            Country country = new Country(resultSet.getString("country.countryId"), resultSet.getString("country.country"));
            City city = new City(resultSet.getString("city.cityId"), resultSet.getString("city.city"), country);
            Address address = new Address(resultSet.getString("address.addressId"), resultSet.getString("address.address"), resultSet.getString("address.address2"), city, resultSet.getString("address.postalCode"), resultSet.getString("address.phone"));
            Customer customer = new Customer(resultSet.getInt("customer.customerId")+"", resultSet.getString("customer.customerName"), resultSet.getString("customer.active"), address);
            
            appointments.add(new Appointment(resultSet.getString("appointment.appointmentId"), customer, user, resultSet.getString("appointment.title"), resultSet.getString("appointment.description"), resultSet.getString("appointment.location"), resultSet.getString("appointment.contact"), resultSet.getString("appointment.type"), resultSet.getString("appointment.url"), getLocalDateTime(resultSet.getString("appointment.start")), getLocalDateTime(resultSet.getString("appointment.end"))));
        }
    }
    @FXML
    private void actionSaveButtonPressed(ActionEvent event) throws SQLException, IOException 
    {
        int choice;
        checkForEmptyInput();
        
       
        //gets the start time of attempted appt
        LocalDateTime start = LocalDateTime.of(datepickerPicked.getValue().getYear(), datepickerPicked.getValue().getMonth(), datepickerPicked.getValue().getDayOfMonth(), Integer.parseInt(comboboxStartTime.getValue().substring(0, 2)), Integer.parseInt(comboboxStartTime.getValue().substring(3,5)));
      
        //gets the end time of attempted appt
        LocalDateTime end = LocalDateTime.of(datepickerPicked.getValue().getYear(), datepickerPicked.getValue().getMonth(), datepickerPicked.getValue().getDayOfMonth(), Integer.parseInt(comboboxEndTime.getValue().substring(0, 2)), Integer.parseInt(comboboxEndTime.getValue().substring(3,5)));
        //clients time zone
        
        //formats ZonedDateTime
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        ZonedDateTime zdtStart = getCustomersZoneDateTime(start);
//        ZonedDateTime zdtEnd = getCustomersZoneDateTime(end);
//        String strStart = getLocalDateTime(zdtStart.format(format));
//        String strEnd = getLocalDateTime(zdtEnd.format(format));
        
        
        validateTimes(start, end);
        
        
        if(newAppointment)
        {
            choice = JOptionPane.showConfirmDialog(null, "ARE YOU SURE YOU WANT TO ADD THIS APPOINTMENT?");
      
            if( choice == 0)
            {
                add();
            }
        }
        else
        {
            
            choice = JOptionPane.showConfirmDialog(null, "ARE YOU SURE YOU WANT TO UPDATE THIS APPOINTMENT?");

            if( choice == 0)
               update();
        }
        
        if(choice == 0)
        {
            disable();
            populateTable();
            clear();
        }
    }

    @FXML
    private void actionCancelButtonPressed(ActionEvent event) 
    {  
        if((JOptionPane.showConfirmDialog(null, "ARE YOU SURE YOU WANT TO CANCEL?")) == 0)
        {
            disable();
            clear();
        }
    }

    @FXML
    private void actionEditButtonPressed(ActionEvent event) throws SQLException 
    {
        Appointment appointment = tableAppointments.getSelectionModel().getSelectedItem();
        newAppointment = false;
        
        if(appointment != null)
        {
            populateFields(appointment);
            fillComboBoxes();
            enable();
        }
        else
        {
            JOptionPane.showMessageDialog(null, "PLEASE SELECT AN APPOINTMENT TO UPDATE");
        }
    }

    @FXML
    private void actionAddButtonPressed(ActionEvent event) throws SQLException 
    {
        newAppointment = true;
        fillComboBoxes();
        enable();
    }

    @FXML
    private void actionDeleteButtonPressed(ActionEvent event) throws SQLException 
    {
        Appointment chosenAppointment = tableAppointments.getSelectionModel().getSelectedItem();
        if(chosenAppointment != null)
        {
            if(JOptionPane.showConfirmDialog(null, "ARE YOU SURE YOU WANT TO DELETE THE HIGHLIGHTED APPOINTMENT?") == 0)
            {
                delete(chosenAppointment);
                populateTable();
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null,"PLEASE SELECT AN APPOINTMENT TO DELETE.");
        }
        
    }
    
    private void checkForEmptyInput() throws EmptyException
    {   
        checkInputIsNotEmpty(txtContact.getText());
        checkInputIsNotEmpty(txtLocation.getText());
        checkInputIsNotEmpty(txtTitle.getText());
        checkInputIsNotEmpty(txtDescription.getText());
        
        checkComboBoxIsNotEmpty(comboboxType);
        checkComboBoxIsNotEmpty(comboboxStartTime);
        checkComboBoxIsNotEmpty(comboboxEndTime);
        checkComboBoxIsNotEmpty(comboboxCustomerName);
        checkComboBoxIsNotEmpty(comboboxConsultantName);
        
        checkDatePickerIsNotEmpty(datepickerPicked);
    }
    
    private void checkInputIsNotEmpty(String value) throws EmptyException
    {
        if(value.isEmpty())
        {
            JOptionPane.showMessageDialog(null,"PLEASE MAKE SURE ALL FIELDS ARE FILLED IN BEFORE SUBMITTING.");
            throw new EmptyException("Error in AppointmentsController.checkInputIsNotEmpty(String)");
        }
    }
    
    private void checkComboBoxIsNotEmpty(ComboBox _comboBox)
    {
        if(_comboBox.getValue() == null)
        {
            JOptionPane.showMessageDialog(null,"PLEASE MAKE SURE ALL COMBOBOXES ARE FILLED IN BEFORE SUBMITTING");
            throw new EmptyException("Error in AppointmentsController.checkComboBoxIsNotEmpty(ComboBox<String>)");
        }
    }
    
    private void checkDatePickerIsNotEmpty(DatePicker _datePicker)
    {
        if(_datePicker.getValue() == null)
        {
            JOptionPane.showMessageDialog(null,"PLEASE MAKE SURE YOU HAVE PICKED A DATE BEFORE SUBMITTING");
            throw new EmptyException("Error in AppointmentsController.checkDatePickerIsNotEmpty(DatePicker)");
        }
    }
    
      private void clear()
    {
        emptyComboBoxes();
        datepickerPicked.getEditor().clear();
        
        
        txtCustomerID.clear();
        txtContact.clear();
        txtLocation.clear();
        txtTitle.clear();
        txtDescription.clear();
    }
      
    private void clearList() 
    {
        appointments.clear();
    }

    private void delete(Appointment _appointment) throws SQLException
    {
        PreparedStatement prepStatement;
        prepStatement = DBConnection.getConnection().prepareStatement("DELETE FROM appointment WHERE appointmentId = ?");
        prepStatement.setString(1, _appointment.getAppointmentID());
        prepStatement.executeUpdate();
        
        JOptionPane.showMessageDialog(null,"SELECTED APPOINTMENT HAS BEEN DELETED");
    }
    
    private void disable()
    {
        comboboxType.setDisable(true);
        comboboxStartTime.setDisable(true);
        comboboxEndTime.setDisable(true);
        comboboxCustomerName.setDisable(true);
        comboboxConsultantName.setDisable(true);
        
        datepickerPicked.setDisable(true);
        
        
        txtContact.setEditable(false);
        txtLocation.setEditable(false);
        txtTitle.setEditable(false);
        txtDescription.setEditable(false);
        txtDescription.setEditable(false);
        
        btnSave.setDisable(true);
        btnCancel.setDisable(true);
        
        btnDelete.setDisable(false);
        btnEdit.setDisable(false);
        btnAdd.setDisable(false);
    }
    
    private void enable()
    {
        
        datepickerPicked.setDisable(false);
        
        comboboxType.setDisable(false);
        comboboxStartTime.setDisable(false);
        comboboxEndTime.setDisable(false);
        comboboxCustomerName.setDisable(false);
        comboboxConsultantName.setDisable(false);
        
        txtContact.setEditable(true);
        txtLocation.setEditable(true);
        txtTitle.setEditable(true);
        txtDescription.setEditable(true);
        
        btnDelete.setDisable(true);
        btnEdit.setDisable(true);
        btnAdd.setDisable(true);
        btnSave.setDisable(false);
        btnCancel.setDisable(false);
    }
    
    public static String getFXMLPath()
    {
        return fxmlPath;
    }
       
    private void fillStartTimeComboBox()
    {
        //formats LocalDateTime
        DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:mm");
        //gets the time now
        LocalDateTime ldt = LocalDateTime.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), LocalDate.now().getDayOfMonth(), 0, 0);
      
        //formats and then adds it to the list and then adds 30 minutes
        
        boolean toggle = true;
        do
        {
        
        
             
            availableTimes.add(ldt.format(format));
            ldt = ldt.plusMinutes(30);
            
            if((ldt.getHour() == 0 && ldt.getMinute() == 0))
                toggle = false;
        }
        while(toggle);
        comboboxStartTime.setItems(availableTimes);
        
    }
       
    private void fillEndTimeComboBox()
    {
        comboboxEndTime.setItems(availableTimes);
      
    }
    
    private void fillTypeComboBox()
    {
        comboboxType.setItems(meetingType);
    }
    
    private void fillCustomerNameComboBox() throws SQLException
    {
        
        // PreparedStatment to hold/prepare the SQL string value that will be passed to Database.
        PreparedStatement prepStatement = DBConnection.getConnection().prepareStatement("SELECT * FROM customer, address, city, country WHERE address.addressId = customer.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId");
        ResultSet resultSet = prepStatement.executeQuery();
        customerNames.clear();
        while(resultSet.next())
        {
            Country country = new Country(resultSet.getString("country.countryId"), resultSet.getString("country.country"));
            City city = new City(resultSet.getString("city.cityId"), resultSet.getString("city.city"), country);
            Address address = new Address(resultSet.getString("address.addressId"), resultSet.getString("address.address"), resultSet.getString("address.address2"), city, resultSet.getString("address.postalCode"), resultSet.getString("address.phone"));
            customerNames.add(new Customer(resultSet.getString("customer.customerId"), resultSet.getString("customer.customerName"), resultSet.getString("customer.active"), address));
//            customerNames.add(new Customer(resultSet.getString("customerId"), resultSet.getString("customerName")));
        }
        
        comboboxCustomerName.setItems(customerNames);
        
    }
    
    private void fillConsultantNameComboBox() throws SQLException
    {
        // PreparedStatment to hold/prepare the SQL string value that will be passed to Database.
        PreparedStatement prepStatement = DBConnection.getConnection().prepareStatement("SELECT * FROM user");
        ResultSet resultSet = prepStatement.executeQuery();
        userNames.clear();
        
        while(resultSet.next())
        {
            userNames.add(new User(resultSet.getString("userId"), resultSet.getString("userName")));
        }
        
        comboboxConsultantName.setItems(userNames);
    }
    
    private void fillComboBoxes() throws SQLException
    {
        fillStartTimeComboBox();
        fillEndTimeComboBox();
        fillTypeComboBox();
        fillCustomerNameComboBox();
        fillConsultantNameComboBox();
    }
    
    private void emptyComboBoxes()
    {
        comboboxType.setItems(null);
        comboboxStartTime.setItems(null);
        comboboxEndTime.setItems(null);
        comboboxCustomerName.setItems(null);
        comboboxConsultantName.setItems(null);
    }
    
    private void populateFields(Appointment _appointment)
    {
        
        datepickerPicked.setValue(LocalDate.parse(_appointment.getDate()));
        comboboxType.setValue(_appointment.getType());
        comboboxStartTime.setValue(_appointment.getStartTime());
        comboboxEndTime.setValue(_appointment.getEndTime());
        comboboxCustomerName.setValue(_appointment.getClient());
        comboboxConsultantName.setValue(_appointment.getRepresentative());
        
        txtCustomerID.setText(_appointment.getClient().getCustomerID());
        txtContact.setText(_appointment.getContact());
        txtLocation.setText(_appointment.getLocation());
        txtTitle.setText(_appointment.getTitle());
        txtDescription.setText(_appointment.getDescription());
        txtCustomerID.setText(_appointment.getClient().getCustomerID());
    }
    
    private void populateTable() throws SQLException
    {
        clearList();
        addAppointmentsToList();
        setCellValueFactories();
       tableAppointments.setItems(appointments);
    }
        
    private int randomNumberGenerator()
    {
        Random rng = new Random();
        return rng.nextInt(999999);
    }
    
    //sets the cell values for the columns in table
    private void setCellValueFactories()
    {
        columnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnStart.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        columnEnd.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
    }
    //updates the values in the sql from the fields in the GUI
    private void update() throws SQLException, IOException
    {
        checkForInvalidCustomerData(comboboxCustomerName.getValue());
        
        Appointment chosenAppointment = tableAppointments.getSelectionModel().getSelectedItem();
        PreparedStatement prepStatement = null;
        
        prepStatement = DBConnection.getConnection().prepareStatement("UPDATE appointment SET customerId=?, userId=?, title=?, description=?, location=?, contact=?, type=?, start=?, end=?, lastUpdate=CURRENT_TIMESTAMP, lastUpdateBy=? WHERE appointmentId =?");
        prepStatement.setInt(1, Integer.parseInt(comboboxCustomerName.getValue().getCustomerID()));
        prepStatement.setInt(2, Integer.parseInt(comboboxConsultantName.getValue().getID()));
        prepStatement.setString(3, txtTitle.getText());
        prepStatement.setString(4, txtDescription.getText());
        prepStatement.setString(5, txtLocation.getText());
        prepStatement.setString(6, txtContact.getText());
        prepStatement.setString(7, comboboxType.getValue());
        prepStatement.setString(8, getUTCDateTime(datepickerPicked.getValue().getYear(), datepickerPicked.getValue().getMonthValue(), datepickerPicked.getValue().getDayOfMonth(), Integer.parseInt(comboboxStartTime.getValue().substring(0, 2)), Integer.parseInt(comboboxStartTime.getValue().substring(3, 5))));
        prepStatement.setString(9, getUTCDateTime(datepickerPicked.getValue().getYear(), datepickerPicked.getValue().getMonthValue(), datepickerPicked.getValue().getDayOfMonth(),Integer.parseInt(comboboxEndTime.getValue().substring(0, 2)), Integer.parseInt(comboboxEndTime.getValue().substring(3, 5))));   
        prepStatement.setString(10, CurrentUser.getUser().getUsername());
        prepStatement.setString(11, chosenAppointment.getAppointmentID());
        prepStatement.executeUpdate();
        
        
        
        JOptionPane.showMessageDialog(null, String.format("CUSTOMER APPOINTMENT FOR %s HAS BEEN SUCESSFULLY UPDATED", comboboxCustomerName.getValue().getCustomerName()));
    }
    
    //returns the propper string formated date and time based on the zone date and time
    private String getUTCDateTime(Integer _year, Integer _month, Integer _day, Integer _hour, Integer _minute) throws SQLException
    {
        //formats LocalDateTime
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //gets the time now
        //year, month, day of month, hour, minute
        LocalDateTime ldt = LocalDateTime.of(_year, _month, _day, _hour, _minute);
        
        //the zoned time of the customer
        ZonedDateTime customersZoneDateTime = getCustomersZoneDateTime(ldt);
        
        //the standard UTC time
        ZonedDateTime utcZonedDateTime = customersZoneDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        
        System.out.println(customersZoneDateTime.format(format));
        System.out.println(utcZonedDateTime.format(format));
        
        
        //String to localize timezone
        return utcZonedDateTime.format(format);
        //String to localize timezone
        
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
    
//    private ZonedDateTime getUsersZoneDateTime(LocalDateTime ldt)
//    {
//       ZonedDateTime now = ZonedDateTime.now();
//       now = ldt.withZoneSameInstant(ZoneId.of("UTC"));
//       
//       
//       return null; 
//    }
    
    //gets the Zone Date Time of the customer
    public ZonedDateTime getCustomersZoneDateTime(LocalDateTime ldt) throws SQLException
    {
        String cityID = comboboxCustomerName.getValue().getAddress().getCity().getCityID();
        String countryID = comboboxCustomerName.getValue().getAddress().getCity().getCountry().getCountryID();
        
        if(cityID.equals("1") && countryID.equals("1"))
        {
//            System.out.println("PHX");
            return ZonedDateTime.of(ldt.toLocalDate(), ldt.toLocalTime(),ZoneId.of("America/Phoenix"));
        }
        else if(cityID.equals("2") && countryID.equals("1"))
        {
//            System.out.println("NY");
            return ZonedDateTime.of(ldt.toLocalDate(), ldt.toLocalTime(),ZoneId.of("America/New_York"));
        }
        else
        {
//            System.out.println("LON");
            return ZonedDateTime.of(ldt.toLocalDate(), ldt.toLocalTime(),ZoneId.of("Europe/London"));
        }
    }
    
    //validates the times
    private void validateTimes(LocalDateTime _start, LocalDateTime _end) throws IOException, SQLException
    {
//        Integer year1 = Integer.parseInt(_start.substring(0, 4));
//        Integer month1 = Integer.parseInt(_start.substring(5, 7));
//        Integer day1 = Integer.parseInt(_start.substring(8, 10));
//        Integer hour1 = Integer.parseInt(_start.substring(11, 13));
//        Integer minute1 = Integer.parseInt(_start.substring(14, 16));
//        
//        Integer year2 = Integer.parseInt(_end.substring(0, 4));
//        Integer month2 = Integer.parseInt(_end.substring(5, 7));
//        Integer day2 = Integer.parseInt(_end.substring(8, 10));
//        Integer hour2 = Integer.parseInt(_end.substring(11, 13));
//        Integer minute2 = Integer.parseInt(_end.substring(14, 16));
        
//        LocalDateTime ldtStart = LocalDateTime.of(year1, month1, day1, hour1, minute1);
//        LocalDateTime ldtEnd = LocalDateTime.of(year2, month2, day2, hour2, minute2);
        
        checkStartTimeIsBeforeEndTime(_start, _end);
        checkBusinessHours(_start, _end);
        checkForOverlappingTimes(_start, _end);
    }
    
    private void checkBusinessHours(LocalDateTime _start, LocalDateTime _end) throws IOException
    {
      
            
//            System.out.println(!_start.isBefore(LocalDateTime.of(_start.getYear(), _start.getMonth(), _start.getDayOfMonth(), 9, 0)));
//            System.out.println(!_start.isAfter(LocalDateTime.of(_start.getYear(), _start.getMonth(), _start.getDayOfMonth(), 17, 0)));
//            System.out.println(!_end.isBefore(LocalDateTime.of(_end.getYear(), _end.getMonth(), _end.getDayOfMonth(), 9, 0)));
//            System.out.println(!_end.isAfter(LocalDateTime.of(_end.getYear(), _end.getMonth(), _end.getDayOfMonth(), 17, 0)));
//            
            if(!_start.isBefore(LocalDateTime.of(_start.getYear(), _start.getMonth(), _start.getDayOfMonth(), 9, 0)) && !_start.isAfter(LocalDateTime.of(_start.getYear(), _start.getMonth(), _start.getDayOfMonth(), 17, 0)) && !_end.isBefore(LocalDateTime.of(_end.getYear(), _end.getMonth(), _end.getDayOfMonth(), 9, 0)) && !_end.isAfter(LocalDateTime.of(_end.getYear(), _end.getMonth(), _end.getDayOfMonth(), 17, 0))){}
            else 
            {
                JOptionPane.showMessageDialog(null, "ERROR: PLEASE MAKE SURE YOUR START AND END TIMES ARE WITHIN OUR BUSINESS HOURS (09:00 - 17:00 MILITARY TIME)");
                throw new IOException();
            }
    }
    
    private void checkStartTimeIsBeforeEndTime(LocalDateTime _start, LocalDateTime _end) throws IOException
    {
        if(!_start.isBefore(_end))
        {
            JOptionPane.showMessageDialog(null, "ERROR: PLEASE MAKE SURE YOUR START TIME IS BEFORE YOUR END TIME");
            throw new IOException();
        }
    }
    
    private void checkForOverlappingTimes(LocalDateTime _start, LocalDateTime _end) throws SQLException, IOException
    {
        //formats LocalDateTime
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
         PreparedStatement prepStatement = DBConnection.getConnection().prepareStatement(
        "SELECT appointment.* FROM appointment " + 
         "WHERE (? BETWEEN appointment.start AND appointment.end OR ? BETWEEN appointment.start AND appointment.end OR ? < appointment.start AND ? > appointment.end) AND (appointment.userId = ?)");
         
        prepStatement.setString(1, _start.format(format));
        prepStatement.setString(2, _end.format(format));
        prepStatement.setString(3, _start.format(format));
        prepStatement.setString(4, _end.format(format));
        prepStatement.setString(5, comboboxConsultantName.getValue().getID());
        ResultSet resultSet = prepStatement.executeQuery();
        
        if(resultSet.next())
        {
            JOptionPane.showMessageDialog(null, "ERROR: THERE IS ALREADY AN APPOINTMENT WITHIN THE START AND END TIMES YOU HAVE CHOSEN, PLEASE CHOOSE AGAIN");
            throw new IOException();
        }
        
        
    }
    
    private void checkForInvalidCustomerData(Customer _customer) throws IOException
    {
        try 
        {
            PreparedStatement prepStatement = DBConnection.getConnection().prepareStatement(
                    "SELECT * FROM customer "
                    + "WHERE customer.customerId = ? AND customer.customerName = ?");
            
            prepStatement.setString(1, _customer.getCustomerID());
            prepStatement.setString(2, _customer.getCustomerName());
            
            ResultSet resultSet = prepStatement.executeQuery();
            
            if(!resultSet.next())
            {
                JOptionPane.showMessageDialog(null, "ERROR: NON-EXISTANT OR INVALID CUSTOMER INFORMATION");
                throw new IOException();
            }
        }
        catch (SQLException ex) 
        {
            Logger.getLogger(AppointmentsController.class.getName()).log(Level.SEVERE, null, ex);
        }

         
    }
}
