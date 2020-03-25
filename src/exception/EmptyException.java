/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exception;

/**
 *
 * @author Steven
 */
public class EmptyException extends RuntimeException
{
    public EmptyException(String message)
    {
        super(message);
    }
}
