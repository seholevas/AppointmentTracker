/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Steven
 */
public class Country 
{   
    private String countryID;
    private String countryName;
    
    public Country()
    {
        
    }
    
    public Country(String passedCountryID, String passedCountryName)
    {
        countryID = passedCountryID;
        countryName = passedCountryName;   
    }
    
    /**
     * @return the countryID
     */
    public String getCountryID() 
    {
        return countryID;
    }

    /**
     * @param countryID the countryID to set
     */
    public void setCountryID(String countryID) 
    {
        this.countryID = countryID;
    }

    /**
     * @return the countryName
     */
    public String getCountryName() 
    {
        return countryName;
    }

    /**
     * @param countryName the countryName to set
     */
    public void setCountryName(String countryName) 
    {
        this.countryName = countryName;
    }
    
    @Override
    public String toString() 
    {
        return countryName;
    }
}
