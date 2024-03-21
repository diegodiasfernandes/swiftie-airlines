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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SignupController 
{
    @FXML
    private TextField CPFTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button signUpButton;
    @FXML
    private TextField NameTextField;
    @FXML
    private Text nameAlert;
    @FXML
    private Text passwordAlert;
    @FXML
    private Text CPFAlert;
    @FXML
    private Button switchToLoginButton;

    private ArrayList<Costumer> costumers = new ArrayList<Costumer>();

    @FXML
    private void signup(ActionEvent event) throws IOException 
    {
        boolean validAccount = true;
        readCostumersFile();

        String name = NameTextField.getText();
        if(name.equals("") || name.contains(";"))
        {
            validAccount = false;
            nameAlert.setText("Invalid name! Must have at least one character and not have ' ; '");
        }
        else
        {
            nameAlert.setText(" ");
        }

        String CPF = CPFTextField.getText();    
        if(CPF.equals("") || CPF.contains(";") || invalidCPF(CPF))
        {
            validAccount = false;
            CPFAlert.setText("Invalid CPF! Must have only numbers. If you wrote only numbers, this CPF already exist.");
        }    
        else
        {
            CPFAlert.setText(" ");
        }

        String password = passwordTextField.getText();
        if(password.equals("") || password.contains(";"))
        {
            validAccount = false;
            passwordAlert.setText("Invalid password! Must have at least one character and not have ' ; '");
        }
        else
        {
            passwordAlert.setText(" ");
        }

        if(validAccount)
        {
            Costumer user = new Costumer(name, CPF, password);
            costumers.add(user);
            FileWriter fw = new FileWriter("src/txt_files/costumers.txt", true);
            fw.append(user.toTxt() + "\n");
            fw.close();

            switchToLogin(event);
        }
    }

    @FXML
    void switchToLogIn(ActionEvent event) throws IOException 
    {
        switchToLogin(event);
    }

    private void switchToLogin(ActionEvent event) throws IOException
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
            costumers.add(new Costumer(data[0], data[1], data[2]));
        }
        bf.close();
    }

    private boolean invalidCPF(String CPF)
    {
        for (Costumer costumer : costumers) 
        {
            if(costumer.getCPF().equals(CPF))
            {
                return true;
            }    
        }

        try{
            Integer.parseInt(CPF);
        } catch(Exception e){
            return false;
        }

        return false;
    }
}
