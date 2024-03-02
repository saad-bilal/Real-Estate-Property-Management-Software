import javafx.application.Application;
import javafx.stage.Stage;

// The main application class that extends JavaFX Application
public class App extends Application {

    // The start method is the main entry point for all JavaFX applications
    @Override
    public void start(Stage primaryStage) {
        // Create a new UserLogin object
        UserLogin login = new UserLogin(primaryStage);
        // Initialize the components of the UserLogin object
        login.initializeComponents();
    }

    // The main method that launches the JavaFX application
    public static void main(String[] args) {
        launch(args);
    }
}