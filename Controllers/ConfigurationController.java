package Controllers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Classes.Costumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ConfigurationController 
{        
    @FXML
    private AnchorPane root;
    @FXML
    private VBox home_side_menu;
    @FXML
    private Button button1, button11, button111, button111111, button1111121, buttonbusca, logoutButton;
    @FXML
    private TextField cpfTextField, nameTextField, passwordTextField;
    @FXML
    private Text nameText;

    private Costumer user;
    private ArrayList<Costumer> costumers = new ArrayList<Costumer>();

    public void _initialize(Costumer user) throws IOException
    {
        this.user = user;
        readCostumersFile();

        nameTextField.setText(user.getName());
        cpfTextField.setText(user.getCPF());
        nameText.setText(user.getName());
    }

    @FXML
    void saveEditions() throws IOException
    {
        if(cpfTextField.getText().equals("") || nameTextField.getText().equals("") || passwordTextField.getText().equals(""))
        {
            errorAlert("Invalid User Error", "There may be some invalid informations", "You must write something on every field and can't use ';");
            return;
        } 
        if(cpfTextField.getText().contains(";") || nameTextField.getText().contains(";") || passwordTextField.getText().contains(";"))
        {
            errorAlert("Invalid User Error", "There may be some invalid informations", "You must write something on every field and can't use ';");
            return;
        } 

        if(!confirmationAlert("Edit Confirmation", "Do you really want to edit your profile?", "This will change your password, be carefull"))
        {
            return;
        }

        Costumer c = new Costumer(nameTextField.getText(), cpfTextField.getText(), 
                                  passwordTextField.getText(), user.getMiles(), user.getFlightIDs());

        FileWriter fw = new FileWriter("src/txt_files/costumers.txt", false);
        fw.append("");
        fw.close();

        FileWriter fw2 = new FileWriter("src/txt_files/costumers.txt", true);
        for (int i = 0; i < costumers.size(); i++) 
        {
            if(costumers.get(i).getCPF().equals(user.getCPF()))
            {
                costumers.set(i, c);
                fw2.append(c.toTxt() + "\n");
            }
            else
            {
                fw2.append(costumers.get(i).toTxt() + "\n");
            }
        }
        fw2.close();

        user = c;
        nameText.setText(user.getName());
    }

    @FXML
    void buyTicketsLoader(ActionEvent event) throws IOException
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

    @FXML
    void homecheckinLoader(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("../Screens/homecheckin.fxml")));
        Parent root = loader.load();

        String css = this.getClass().getResource("../StyleSheets/style.css").toExternalForm();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(css); 

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        HomeCheckinController controller = loader.getController();

        controller._initialize(this.user);

        stage.setScene(scene);
        stage.centerOnScreen();
        Image image = new Image("imagens\\logo.png");
        stage.getIcons().add(image);
        stage.show();    
    }

    @FXML
    void accessoriesLoader(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("../Screens/accessories.fxml")));
        Parent root = loader.load();

        String css = this.getClass().getResource("../StyleSheets/style.css").toExternalForm();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(css); 

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        AccessoriesController controller = loader.getController();

        controller._initialize(this.user);

        stage.setScene(scene);
        stage.centerOnScreen();
        Image image = new Image("imagens\\logo.png");
        stage.getIcons().add(image);
        stage.show();    
    }

    @FXML
    void myTravelsLoader(ActionEvent event) throws IOException 
    {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("../Screens/HomeMyTravels.fxml")));
        Parent root = loader.load();

        String css = this.getClass().getResource("../StyleSheets/style.css").toExternalForm();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(css);

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        HomeMyTravelsController controller = loader.getController();
        controller._initialize(user);

        stage.setScene(scene);
        stage.centerOnScreen();
        Image image = new Image("imagens\\logo.png");
        stage.getIcons().add(image);
        stage.show();
    }

    @FXML
    private void logout(ActionEvent event) throws IOException
    {
        Parent root = FXMLLoader.load((getClass().getResource("../Screens/login.fxml")));
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        Image image = new Image("imagens\\logo.png");
        stage.getIcons().add(image);
        stage.show();
    }

    private void errorAlert(String title, String header, String content)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();   
    }

    private boolean confirmationAlert(String title, String header, String content)
    {
        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(AlertType.CONFIRMATION, "", yesButton, noButton);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);  
        
        if(alert.showAndWait().get() == yesButton)
        {
            return true;
        }
        return false;
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
