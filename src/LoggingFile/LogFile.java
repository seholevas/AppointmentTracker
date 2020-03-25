package LoggingFile;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Steven
 */
public class LogFile 
{
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static FileHandler fileHandler;
    
    public static void build() throws IOException
    {
        
         fileHandler = new FileHandler("LoginLogFile%g%u.txt", 10000, 22, true);
         fileHandler.setFormatter(new SimpleFormatter());
         Logger log = Logger.getLogger("");
         log.setUseParentHandlers(false);
         log.addHandler(fileHandler);
         log.setLevel(Level.INFO);
         SimpleFormatter formatter = new SimpleFormatter();  
         fileHandler.setFormatter(formatter);  
    }
}
