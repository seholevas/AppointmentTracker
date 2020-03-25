package app;

import LoggingFile.LogFile;
import connections.DBConnection;
import java.io.IOException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class app extends Application {
    
    
    /**
     * @param primaryStage the command line arguments
     * @throws java.io.IOException
     * @return void
     * 
     * starts the program by setting up the primaryStage as the LoginView
     */
    @Override
    public void start(Stage primaryStage) throws IOException 
    {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/LoginView.fxml"));
            Parent loginScene = loader.load();            
            Scene scene = new Scene(loginScene);
            primaryStage.setScene(scene);
            primaryStage.show();
    }

    /**
     * @param args the command line arguments
     * @throws java.sql.SQLException
     * 
     * Connects to the database and launches the program. After program is done, it then closes the connection.
     */
    public static void main(String[] args) throws SQLException, Exception 
    {
        DBConnection.makeConnection();
        LogFile.build();
        launch(args);
        DBConnection.closeConnection();
    }
    
}
