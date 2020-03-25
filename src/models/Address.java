package models;


public class Address 
{
    private String addressID;
    private String primaryAddress;
    private String secondaryAddress;
    private City city;
    private String postalCode;
    private String phoneNumber;
    
    public Address()
    {
        
    }
    
    public Address(String passedAddressID, String passedPrimaryAddress, String passedSecondaryAddress, City passedCity, String passedPostalCode, String passedPhoneNumber)
    {
        addressID = passedAddressID;
        primaryAddress = passedPrimaryAddress;
        secondaryAddress = passedSecondaryAddress;
        city = passedCity;
        postalCode = passedPostalCode;
        phoneNumber = passedPhoneNumber;
    }
    
    /**
     * @return the addressID
     */
    public String getAddressID() {
        return addressID;
    }

    /**
     * @param addressID the addressID to set
     */
    public void setAddressID(String addressID) {
        this.addressID = addressID;
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
     * @return the secondaryAddress
     */
    public String getSecondaryAddress() {
        return secondaryAddress;
    }

    /**
     * @param secondaryAddress the secondaryAddress to set
     */
    public void setSecondaryAddress(String secondaryAddress) {
        this.secondaryAddress = secondaryAddress;
    }

    /**
     * @return the city
     */
    public City getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(City city) {
        this.city = city;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    
    @Override
    public String toString() 
    {
        return primaryAddress;
    }
    
}
