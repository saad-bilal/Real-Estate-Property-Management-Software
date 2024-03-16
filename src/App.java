import Menus.UserLogin;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        UserLogin login = new UserLogin(primaryStage);
        login.initializeComponents();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
