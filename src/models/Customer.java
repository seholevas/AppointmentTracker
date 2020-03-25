/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

public class Customer 
{
    private String customerID;
    private String customerName;
    private String primaryAddress;
    private String active;
    
    private String phone;
    
    private Address address;
    
    public Customer(String _customerID, String _customerName)
    {
        customerID = _customerID;
        customerName = _customerName;
    }
    
    public Customer(String _customerID, String _customerName, String _primaryAddress, String _active, String _phone)
    {
        customerID = _customerID;
        customerName = _customerName;
        primaryAddress = _primaryAddress;
        active = _active;
        phone = _phone;
    }
    
    public Customer(String _customerID, String _customerName, String _active, Address _address)
    {
        customerID = _customerID;
        customerName = _customerName;
        active = _active;
        address = _address;
        phone = _address.getPhoneNumber();
        primaryAddress = _address.getPrimaryAddress();
    }
    
    
    
    public void setAddress(Address _address)
    {
        address = _address;
    }
    
    public Address getAddress()
    {
        return address;
    }
    
    
    /**
     * @return the customerID
     */
    public String getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID the customerID to set
     */
    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
     /**
     * @return the primaryAddress
     */
    public String getPrimaryAddress() {
        return primaryAddress;
    }

    /**
     * @param primaryAddress the primaryAddress to set
     */
    public void setPrimaryAddress(String primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    /**
     * @return the active
     */
    public String getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(String active) {
        this.active = active;
    }
    
     /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    @Override
    public String toString() 
    {
        return customerName;
    }


}
