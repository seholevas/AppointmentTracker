/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package statics;

import models.User;

/**
 *
 * @author Steven
 */
public class CurrentUser 
{
    private static User user;
    
    public static User getUser()
    {
        return user;
    }
    
    public static void setUser(User _user)
    {
        user = _user;
    }
    
}
