package models;
public class User 
{
    private String id;
    
    private String username;
    
    private String password;
    
    public User()
    {
    }
    
    public User(String _userID, String _name) 
    {
        id = _userID;
        username = _name; 
    }
    
    public User(String passedID, String passedUsername, String passedPassword)
    {
        id = passedID;
        username = passedUsername;
        password = passedPassword;
    }

    
    
    public String getID()
    {
        return id;
    }
    
    public void setID(String value)
    {
        id = value;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String value)
    {
        username = value;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String value)
    {
        password = value;
    }
    
    @Override
    public String toString() 
    {
        return username;
    }
    
}
