package models;

public class City 
{
    
    private String cityID;
//    private int countryID;
    private String cityName;
    
    private Country country;
    
    public City()
    {
        
    }
    
    public City(String passedCityID, String passedCityName)
    {
        cityName = passedCityName;
        cityID = passedCityID;
    }
    
    public City(String passedCityID, String passedCityName, Country _country)
    {
        cityName = passedCityName;
        cityID = passedCityID;
        country = _country;
    }
    
     /**
     * @return the cityID
     */
    

    public Country getCountry()
    {
        return country;
    }
    
    public String getCityID() 
    {
        return cityID;
    }
    

    /**
     * @param cityID the cityID to set
     */
    public void setCityID(String cityID) 
    {
        this.cityID = cityID;
    }

    /**
     * @return the cityName
     */
    public String getCityName() 
    {
        return cityName;
    }

    /**
     * @param cityName the cityName to set
     */
    public void setCityName(String cityName) 
    {
        this.cityName = cityName;
    }
    
    @Override
    public String toString() 
    {
        return cityName;
    }
}
