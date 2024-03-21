package Controllers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Classes.Costumer;
import Classes.Flight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomeCheckinController 
{
    @FXML
    private VBox home_side_menu;
    @FXML
    private AnchorPane root;
    @FXML
    private Button buyTicketsButton, button11, button111, button111111, button1111121, 
            loadCheckinButton, logoutButton;

    @FXML
    private TableView<Flight> FlightsTable;
    @FXML
    private TableColumn<Flight, String> DateColumn;
    @FXML
    private TableColumn<Flight, String> DestinationColumn;
    @FXML
    private TableColumn<Flight, String> IDColumn;
    @FXML
    private TableColumn<Flight, String> OriginColumn;
    @FXML
    private TableColumn<Flight, String> timeColumn;

    private ArrayList<Flight> flights = new ArrayList<Flight>();
    private String id = "";
    private String originAirport = "";
    private String destinationAirport = "";
    private Costumer user;
    private ArrayList<String> alreadyCheckedInIDs = new ArrayList<String>();
    private ArrayList<String> statusArrayList = new ArrayList<String>();

    public void _initialize(Costumer user) throws IOException
    {
        this.user = user;
        readFlightsFile();
        readFlightStatus();
        

        ArrayList<Flight> userFlights = new ArrayList<Flight>();
        for (Flight flight : flights) 
        {
            if(user.getFlightIDs().contains(flight.getID()) && !alreadyCheckedInIDs.contains(flight.getID()))
                userFlights.add(flight);
        }

        ObservableList<Flight> tableFlightsObservableList = FXCollections.observableArrayList(userFlights);
        IDColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("ID"));
        OriginColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("originAirportFull"));
        DestinationColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("destinationAirportFull"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("departureDate"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("departureTime"));

        FlightsTable.setItems(tableFlightsObservableList);
    }

    @FXML
    void checkin(ActionEvent event) throws IOException
    {
        if(id == null)
        {
            errorAlert("ID Error", "No Flight Selected!", "Select a flight from the table!");
            return;
        }
        if(id.equals(""))
        {
            errorAlert("ID error", "No Flight Has been Selected", "Please select a flight from the table!");
            return;
        }

        FileWriter fw = new FileWriter("src/txt_files/costumersflightsstatus.txt", false);
        fw.append("");
        fw.close();

        FileWriter fw2 = new FileWriter("src/txt_files/costumersflightsstatus.txt", true);
        for (String string : statusArrayList) 
        {
            String[] data = string.split(";");
            if(user.getCPF().equals(data[0]) && id.equals(data[1]))
            {
                fw2.append(data[0] + ";" + data[1] + ";" + "true" + "\n");
                alreadyCheckedInIDs.add(id);
            }
            else
            {
                fw2.append(string + "\n");
            }
        }
        fw2.close();

        ArrayList<Flight> userFlights = new ArrayList<Flight>();
        for (Flight flight : flights) 
        {
            if(user.getFlightIDs().contains(flight.getID()) && !alreadyCheckedInIDs.contains(flight.getID()))
                userFlights.add(flight);
        }
        ObservableList<Flight> tableFlightsObservableList = FXCollections.observableArrayList(userFlights);
        FlightsTable.setItems(tableFlightsObservableList);

        if(confirmationAlert("Fly Back", "Do you want to buy a return passage?", "Click Yes to check if there are passages to your location."))
        {
            FXMLLoader loader = new FXMLLoader((getClass().getResource("../Screens/buyhometickets.fxml")));
            Parent root = loader.load();

            String css = this.getClass().getResource("../StyleSheets/style.css").toExternalForm();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(css); 

            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            BuyHomeTicketsController controller = loader.getController();
            controller._initialize(user, destinationAirport, originAirport);

            stage.setScene(scene);
            stage.centerOnScreen();
            Image image = new Image("imagens\\logo.png");
            stage.getIcons().add(image);
            stage.show();   
        }
    }    
    
    @FXML
    void selectFlight(MouseEvent event)
    {
        int index = FlightsTable.getSelectionModel().getSelectedIndex();
        id = IDColumn.getCellData(index);
        originAirport = OriginColumn.getCellData(index);
        destinationAirport = DestinationColumn.getCellData(index);
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
    void myTravelsLoader(ActionEvent event) throws IOException {
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
    void configurationsLoader(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("../Screens/configuration.fxml")));
        Parent root = loader.load();

        String css = this.getClass().getResource("../StyleSheets/style.css").toExternalForm();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(css);

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        ConfigurationController controller = loader.getController();
        controller._initialize(user);

        stage.setScene(scene);
        stage.centerOnScreen();
        Image image = new Image("imagens\\logo.png");
        stage.getIcons().add(image);
        stage.show();
    }

    @FXML
    void logout(ActionEvent event) throws IOException
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

    private void readFlightStatus() throws IOException
    {
        new File("src/txt_files").mkdirs();
        File f = new File("src/txt_files/costumersflightsstatus.txt");
        if(!f.exists())
            f.createNewFile();
        BufferedReader bf = new BufferedReader(new FileReader("src/txt_files/costumersflightsstatus.txt"));

        String line; 
        while ((line = bf.readLine()) != null)
        {
            String[] data = line.split(";");
            if(user.getCPF().equals(data[0]))
            {
                if(data[2].equals("true"))
                    alreadyCheckedInIDs.add(data[1]);
            }    
            statusArrayList.add(line);
        }
        bf.close();
    }

    private void readFlightsFile() throws IOException
    {
        new File("src/txt_files").mkdirs();
        File f = new File("src/txt_files/flights.txt");
        if(!f.exists())
            f.createNewFile();
            
        BufferedReader bf = new BufferedReader(new FileReader("src/txt_files/flights.txt"));
        String line;

        while((line = bf.readLine()) != null)
        {
            String[] dados = line.split(";");

            String[] seatsStatusString = dados[14].split(","); 
            ArrayList<Boolean> seatsStatus = new ArrayList<Boolean>();
            for (String status : seatsStatusString) 
            {
                seatsStatus.add(Boolean.parseBoolean(status));
            }

            String[] costumersString = dados[15].split("=");
            ArrayList<String> cpf_name = new ArrayList<String>();
            for (String costumer : costumersString) 
            {
                cpf_name.add(costumer);
            }

            flights.add(new Flight(dados[0], dados[1], dados[2], dados[3], dados[4], dados[5], dados[6], 
                                   dados[7], dados[8], dados[9], Double.parseDouble(dados[10]), 
                                   Double.parseDouble(dados[11]), Double.parseDouble(dados[12]), 
                                   Double.parseDouble(dados[13]), seatsStatus, cpf_name));
        }

        bf.close();
    }
}
