package Classes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
 
public class App extends Application 
{ 
    public static void main(String[] args) 
    {
        launch(args);
    }

    public void start (Stage stage) throws Exception 
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Screens/login.fxml"));
        Parent root = fxmlLoader.load();
        Scene screen = new Scene(root);

        stage.setTitle("Swifties Airlines");
        stage.setScene(screen);
        Image image = new Image("imagens\\logo.png");
        stage.getIcons().add(image);
        stage.show();
    }
}