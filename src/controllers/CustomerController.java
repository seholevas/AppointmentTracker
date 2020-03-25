/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import connections.DBConnection;
import exception.EmptyException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import models.Address;
import models.City;
import models.Country;
import models.Customer;
import statics.CurrentUser;

/**
 * FXML Controller class
 *
 * @author Steven
 */
public class CustomerController implements Initializable 
{
    
    @FXML
    private ToggleGroup rbtngroupCities;
    @FXML
    private RadioButton radiobtnPhoenix;
    @FXML
    private RadioButton radiobtnNewYork;
    @FXML
    private RadioButton radiobtnLondon;
    
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPhoneNumber;
    @FXML
    private TextField txtPrimaryAddress;
    @FXML
    private TextField txtSecondaryAddress;

    @FXML
    private Button btnEdit;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    
    @FXML
    private TableView<Customer> tableCustomers;
    @FXML
    private TableColumn<Customer, String> columnID;
    @FXML
    private TableColumn<Customer, String> columnName;
    @FXML
    private TableColumn<Customer, String> columnPhoneNumber;
    @FXML
    private TableColumn<Customer, String> columnAddress;
    
    private ObservableList<Customer> olistCustomer = FXCollections.observableArrayList();
    
    private final static String fxmlPath = "/view/CustomerView.fxml";
    @FXML
    private TextField txtZipCode;
    
    private boolean newCustomer;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        try 
        {
            populateTable();
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(null,"ERROR: SQL-EXCEPTION, PLEASE CHECK YOUR SQL");
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        disable();
        setRadioButtonData();
    }

    //if btnEdit button is pressed, then the fields will populate with the selected row from the table and become enabled 
    @FXML
    private void actionEditButtonPressed(ActionEvent event) throws SQLException 
    {
        Customer chosenCustomer = tableCustomers.getSelectionModel().getSelectedItem();
        newCustomer = false;
        
        if(chosenCustomer != null)
        {
            enable();
            populateFields(chosenCustomer);   
        }
        else
        {
            JOptionPane.showMessageDialog(null, "ERROR: PLEASE SELECT A CUSTOMER TO UPDATE");
        }
    }

    //when btnAdd button is pressed it sets up the process for adding a completely new customer
    @FXML
    private void actionAddButtonPressed(ActionEvent event) 
    {
        newCustomer = true;
        clear();
        enable();
    }
    
    //when btnDelete button is pressed it deletes the customer that was selected in the table
    @FXML
    private void actionDeleteButtonPressed(ActionEvent event) throws SQLException 
    {
        Customer chosenCustomer = tableCustomers.getSelectionModel().getSelectedItem();
        if(chosenCustomer != null)
        {
            if(JOptionPane.showConfirmDialog(null, "ARE YOU SURE YOU WANT TO DELETE THE SELECTED CUSTOMER?") == 0)
            {
                delete(chosenCustomer);
                populateTable();
            }
        }
        else
        {
            JOptionPane.showMessageDialog(null, "ERROR: PLEASE SELECT A CUSTOMER TO UPDATE");
        }
    }
    
    //depending on whether it is a new customer or not, when the btnSave button is pressed. This will either update or add a new customer and then refresh and populate the table
    @FXML
    private void actionSaveButtonPressed(ActionEvent event) throws SQLException 
    {
        int choice;
        validate();
        if(newCustomer)
        {
            if((choice = JOptionPane.showConfirmDialog(null, "ARE YOU SURE YOU WANT TO ADD THIS CUSTOMER?")) == 0)
                add();
            
        }
        else
        {
            if((choice = JOptionPane.showConfirmDialog(null, "ARE YOU SURE YOU WANT TO UPDATE THIS CUSTOMER?")) == 0)
                update();
        }
        
        if(choice == 0)
        {
            populateTable();
            clear();
            disable();
        }
    }

    //if the cancel button is pressed it cancel the add or update
    @FXML
    private void actionCancelButtonPressed(ActionEvent event)
    {
       if(JOptionPane.showConfirmDialog(null, "ARE YOU SURE YOU WANT TO CANCEL?") == 0)
       {
            disable();
            clear();
       }
    }
    
    
    //returns the file path to the FXML
    public static String getFXMLPath()
    {
        return fxmlPath;
    }
    
    //enables all the fields
    private void enable()
    {
        txtName.setEditable(true);
        txtPhoneNumber.setEditable(true);
        txtPrimaryAddress.setEditable(true);
        txtSecondaryAddress.setEditable(true);
        txtZipCode.setEditable(true);
        btnSave.setDisable(false);
        btnCancel.setDisable(false);
        btnDelete.setDisable(true);
        btnAdd.setDisable(true);
        btnEdit.setDisable(true);
        tableCustomers.setDisable(true);
        
        //lambda expression: used to enable and set each radiobutton in the group, allows me to do it in one line rather than 5 lines.
        rbtngroupCities.getToggles().forEach(toggle -> 
        {
            Node node = (Node) toggle ;
            node.setDisable(false);
        });
        
        
    }
    
    //disables all the fields
    private void disable()
    {
        txtName.setEditable(false);
        txtPhoneNumber.setEditable(false);
        txtPrimaryAddress.setEditable(false);
        txtSecondaryAddress.setEditable(false);
        txtZipCode.setEditable(false);
        btnSave.setDisable(true);
        btnCancel.setDisable(true);
        
        btnDelete.setDisable(false);
        btnAdd.setDisable(false);
        btnEdit.setDisable(false);
        
        tableCustomers.setDisable(false);
        
        //lambda expression: used to disable and set each radiobutton in the group. Allows me to do it in 1 line opposed to 5 lines.
        rbtngroupCities.getToggles().forEach(toggle -> 
        {
            Node node = (Node) toggle ;
            node.setDisable(true);
        });
    }
    
    //clears all the fields
    private void clear()
    {
        txtName.clear();
        txtPhoneNumber.clear();
        txtPrimaryAddress.clear();
        txtSecondaryAddress.clear();
        txtZipCode.clear();

    }
    
    //populates the table
    private void populateTable() throws SQLException
    {
        clearList();
        addCustomersToList();
        //sets Cell Values
        setCellValueFactories();
        
    
        
        tableCustomers.setItems(olistCustomer);
        
    }
    
    //clears the list of customers
    private void clearList()
    {
        olistCustomer.clear();
    }
    
    //adds a customer from the database to the list
    private void addCustomersToList() throws SQLException
    {
        PreparedStatement prepStatement = DBConnection.getConnection().prepareStatement(
        "SELECT customer.*, address.*, city.*, country.* " +
        "FROM customer, address, city, country " +
        "WHERE customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId " +
        "ORDER BY customer.customerName");
        
        
        //result set to execute statement
        ResultSet resultSet = prepStatement.executeQuery();        
        
        //creates a new customer and adds them to the list
        while(resultSet.next())
        {
            Country country = new Country(resultSet.getString("country.countryId"), resultSet.getString("country.country"));
            City city = new City(resultSet.getString("city.cityId"), resultSet.getString("city.city"), country);
            Address address = new Address(resultSet.getString("address.addressId"), resultSet.getString("address.address"), resultSet.getString("address.address2"), city, resultSet.getString("address.postalCode"), resultSet.getString("address.phone"));
            olistCustomer.add(new Customer(resultSet.getString("customer.customerId"), resultSet.getString("customer.customerName"), resultSet.getString("customer.active"), address));
        }
    }
    
    //sets the cell values for the columns in table
    private void setCellValueFactories()
    {
        columnID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        columnPhoneNumber.setCellValueFactory(new PropertyValueFactory("phone"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("primaryAddress"));
    }
    
    //checks if input is not empty
    private void checkInputIsNotEmpty(String value) throws EmptyException
    {
        if(value.isEmpty())
        {
            JOptionPane.showMessageDialog(null, String.format("ERROR: MAKE SURE ALL FIELDS ARE FILLED IN"));
            throw new EmptyException("Error in LoginController.checkInputIsNotEmpty(UserModel, String, String)|");
        }
    }
    
    //validates that each field is not empty
    private void validate() throws EmptyException
    {   
        checkInputIsNotEmpty(txtName.getText());
        checkInputIsNotEmpty(txtPhoneNumber.getText());
        checkInputIsNotEmpty(txtPrimaryAddress.getText());
        checkInputIsNotEmpty(txtSecondaryAddress.getText());
        checkInputIsNotEmpty(txtZipCode.getText());
    }
    
    //deletes a customer from the database
    private void delete(Customer _customer) throws SQLException
    {       
        PreparedStatement prepStatement = null;
        prepStatement = DBConnection.getConnection().prepareStatement("DELETE FROM appointment WHERE appointment.customerId = ?");
        prepStatement.setString(1, _customer.getCustomerID());
        prepStatement.executeUpdate();
        
         prepStatement = DBConnection.getConnection().prepareStatement("DELETE FROM customer WHERE customer.customerId = ?");
        prepStatement.setString(1, _customer.getCustomerID());
        prepStatement.executeUpdate();
        
        prepStatement = DBConnection.getConnection().prepareStatement("DELETE FROM address WHERE address.addressId = ?");
        prepStatement.setString(1, _customer.getAddress().getAddressID());
        prepStatement.executeUpdate();
        JOptionPane.showMessageDialog(null, String.format("%s HAS BEEN SUCESSFULLY DELETED", _customer.getCustomerName()));
    }
    
    //adds a customer to the database by adding to the customer and address tables
    private void add() throws SQLException
    {
        PreparedStatement prepStatement = null;
        Integer customerId = randomNumberGenerator();
        Integer addressId = randomNumberGenerator();
        
        prepStatement = DBConnection.getConnection().prepareStatement("INSERT INTO address (addressId, address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)");
        
        prepStatement.setInt(1, addressId);
        prepStatement.setString(2, txtPrimaryAddress.getText());
        prepStatement.setString(3, txtSecondaryAddress.getText());
        prepStatement.setInt(4, (int) rbtngroupCities.getSelectedToggle().getProperties().get("cityId"));
        prepStatement.setString(5, txtZipCode.getText());
        prepStatement.setString(6, txtPhoneNumber.getText());
        prepStatement.setString(7, CurrentUser.getUser().getUsername());
        prepStatement.setString(8, CurrentUser.getUser().getUsername());
        
        prepStatement.executeUpdate();
        
        prepStatement = DBConnection.getConnection().prepareStatement("INSERT INTO customer (customerId, customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) VALUES (?,?,?,?,CURRENT_TIMESTAMP,?,CURRENT_TIMESTAMP,?)");
        prepStatement.setInt(1, customerId);
        prepStatement.setString(2, txtName.getText());
        prepStatement.setInt(3, addressId);
        prepStatement.setInt(4, new Random().nextInt(2));
        prepStatement.setString(5, CurrentUser.getUser().getUsername());
        prepStatement.setString(6, CurrentUser.getUser().getUsername());
        
        prepStatement.executeUpdate();
        JOptionPane.showMessageDialog(null, String.format("%s HAS BEEN SUCESSFULLY ADDED", txtName.getText()));
    }
    
    //updates a customer that is already in the database
    private void update() throws SQLException
    {
        Customer chosenCustomer = tableCustomers.getSelectionModel().getSelectedItem();
        PreparedStatement prepStatement = null;
        
        prepStatement = DBConnection.getConnection().prepareStatement("UPDATE address SET address=?, address2=?, cityId=?, postalCode=?, phone=?, lastUpdate=CURRENT_TIMESTAMP, lastUpdateBy=? WHERE addressId =?");
        prepStatement.setString(1, txtPrimaryAddress.getText());
        prepStatement.setString(2, txtSecondaryAddress.getText());
        prepStatement.setInt(3,(int) rbtngroupCities.getSelectedToggle().getProperties().get("cityId"));
        prepStatement.setString(4, txtZipCode.getText());
        prepStatement.setString(5, txtPhoneNumber.getText());
        prepStatement.setString(6, CurrentUser.getUser().getUsername());
        prepStatement.setString(7, chosenCustomer.getAddress().getAddressID());
        prepStatement.executeUpdate();
        
        prepStatement = DBConnection.getConnection().prepareStatement("UPDATE customer SET customerName=?, lastUpdate=CURRENT_TIMESTAMP, lastUpdateBy=? WHERE customerId=?");
        prepStatement.setString(1, txtName.getText());
        prepStatement.setString(2, CurrentUser.getUser().getUsername());
        prepStatement.setString(3, chosenCustomer.getCustomerID());
        prepStatement.executeUpdate();
        JOptionPane.showMessageDialog(null, String.format("%s HAS BEEN SUCESSFULLY UPDATED", txtName.getText()));
    }
    
    //sets data within radio buttons so it is easily accessible for later calls
    private void setRadioButtonData()
    {
        radiobtnPhoenix.getProperties().put("cityId", 1);
        radiobtnNewYork.getProperties().put("cityId", 2);
        radiobtnLondon.getProperties().put("cityId", 3);
        
        radiobtnPhoenix.getProperties().put("countryId", 1);
        radiobtnNewYork.getProperties().put("countryId", 1);
        radiobtnLondon.getProperties().put("countryId", 2);
        
        radiobtnPhoenix.getProperties().put("city", "Phoenix");
        radiobtnNewYork.getProperties().put("city", "New York");
        radiobtnLondon.getProperties().put("city", "London");
        
        
    }
    
    //populates all the fields
    private void populateFields(Customer _customer)
    {
        txtName.setText(_customer.getCustomerName());
        txtPhoneNumber.setText(_customer.getPhone());
        txtPrimaryAddress.setText(_customer.getAddress().getPrimaryAddress());
        txtSecondaryAddress.setText(_customer.getAddress().getSecondaryAddress());
        txtZipCode.setText(_customer.getAddress().getPostalCode());
        toggleRadioButtons(_customer);
       
       
    }
    
    //randomly generates a number
    private int randomNumberGenerator()
    {
        Random rng = new Random();
        return rng.nextInt(999999);
    }
    
    //toggles between radiobuttons
    private void toggleRadioButtons(Customer _customer)
    {
        switch(_customer.getAddress().getCity().getCityID())
       {
           case "1":
           {
               rbtngroupCities.selectToggle(radiobtnPhoenix);
               break;
           }
           case "2":
           {
               rbtngroupCities.selectToggle(radiobtnNewYork);
               break;
           }
           case "3":
           {
               rbtngroupCities.selectToggle(radiobtnLondon);
               break;   
           }    
        }
    }
}