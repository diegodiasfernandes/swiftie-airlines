package Controllers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Classes.Costumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController 
{

    @FXML
    private TextField CPFTextField, passwordTextField;
    @FXML
    private Button loginButton, switchToSignUpButton;
    @FXML
    private Text loginAlert;

    private ArrayList<Costumer> costumers = new ArrayList<Costumer>();
    private Costumer user;

    @FXML
    void login(ActionEvent event) throws IOException 
    {
        boolean existingAccount = false;
        readCostumersFile();

        Costumer c = null;
        for (Costumer costumer : costumers) 
        {
            if(CPFTextField.getText().equals(costumer.getCPF()) && passwordTextField.getText().equals(costumer.getPassword()))
            {
                existingAccount = true;
                c = costumer;
                user = costumer;
                break;
            }    
        }

        if(existingAccount || CPFTextField.getText().equals("admin"))
        {
            if(CPFTextField.getText().equals("admin"))
            {
                switchToAdminMenu(event);                
            }
            else
            {
                switchToCostumerMenu(event, c);
            }
        }
        else
            loginAlert.setText("Your CPF, password, or both are wrong.");
    }

    @FXML
    void switchToSignUp(ActionEvent event) throws IOException 
    {
        Parent root = FXMLLoader.load((getClass().getResource("../Screens/signup.fxml")));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();        
        Image image = new Image("imagens\\logo.png");
        stage.getIcons().add(image);
        stage.show();
    }

    private void switchToCostumerMenu(ActionEvent event, Costumer c) throws IOException
    {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("../Screens/buyhometickets.fxml")));
        Parent root = loader.load();

        String css = this.getClass().getResource("../StyleSheets/style.css").toExternalForm();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(css); 

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        BuyHomeTicketsController controller = loader.getController();
        controller._initialize(user);

        stage.setScene(scene);
        stage.centerOnScreen();        
        Image image = new Image("imagens\\logo.png");
        stage.getIcons().add(image);
        stage.show();        
    }

    private void switchToAdminMenu(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("../Screens/homeadm.fxml")));
        Parent root = loader.load();

        String css = this.getClass().getResource("../StyleSheets/style.css").toExternalForm();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(css); 

        HomeAdmController controller = loader.getController();
        controller._initialize();

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();        
        Image image = new Image("imagens\\logo.png");
        stage.getIcons().add(image);
        stage.show();   
    }

    private void readCostumersFile() throws IOException
    {
        new File("src/txt_files").mkdirs();
        File f = new File("src/txt_files/costumers.txt");
        if(!f.exists())
            f.createNewFile();
        BufferedReader bf = new BufferedReader(new FileReader("src/txt_files/costumers.txt"));

        String line; 
        while ((line = bf.readLine()) != null)
        {
            String[] data = line.split(";");
            String[] flightIDsStrings = data[4].split(","); 
            ArrayList<String> flightIDs = new ArrayList<String>();
            for (String id : flightIDsStrings) 
            {
                flightIDs.add(id);
            }
            costumers.add(new Costumer(data[0], data[1], data[2], Double.parseDouble(data[3]), flightIDs));
        }
        bf.close();
    }
}
