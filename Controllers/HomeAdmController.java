package Controllers;
/*import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Classes.Flight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class HomeAdmController {

    @FXML
    private AnchorPane root;
    @FXML
    private VBox home_side_menu;
    @FXML
    private Button buttonlist, buttonFlights, buttonLogin, buttonReport;
    @FXML
    private TableView<Flight> FlightsTable;
    @FXML
    private TableColumn<Flight, String> TableColumn, TableColumn2;
    @FXML
    private TableColumn<Flight, String> stateOrigin;

    @FXML
    private TableColumn<Flight, String> aeroportOrigin;

    @FXML
    private TableColumn<Flight, String> dateOrigin;

    @FXML
    private TableColumn<Flight, String> timeOrigin;
    @FXML
    private TableColumn<Flight, String> stateDestination;

    @FXML
    private TableColumn<Flight, String> aeroportDestination;

    @FXML
    private TableColumn<Flight, String> dateDestination;

    @FXML
    private TableColumn<Flight, String> timeDestination;

    @FXML
    private TableColumn<Flight, String> TableColunmseats;
    @FXML
    private TextField textfield;

    private ObservableList<Flight> observablelistFlights;

    private ArrayList<Flight> flights = new ArrayList<Flight>();


    public void _initialize() throws IOException
    {
        loadViewFlights();
    }
   
    @FXML
    void buttonReport(ActionEvent event) throws IOException 
    {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("../Screens/generatereport.fxml")));
        Parent root = loader.load();

        String css = this.getClass().getResource("../StyleSheets/style.css").toExternalForm();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(css); 

        GenerateReportController controller = loader.getController();
        controller._initialize();

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        Image image = new Image("imagens\\logo.png");
        stage.getIcons().add(image);
        stage.show(); 
    }

    @FXML
    void buttonFlights(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("../Screens/admflights.fxml")));
        Parent root = loader.load();

        String css = this.getClass().getResource("../StyleSheets/style.css").toExternalForm();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(css);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        AdmFlightsController controller = loader.getController();
        controller._initialize();

        stage.setScene(scene);
        stage.centerOnScreen();
        Image image = new Image("imagens\\logo.png");
        stage.getIcons().add(image);
        stage.show();
    }

    @FXML
    void buttonLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load((getClass().getResource("../Screens/login.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
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

    public void loadViewFlights() throws IOException {
        readFlightsFile();
        observablelistFlights = FXCollections.observableArrayList(flights);

        TableColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn2.setCellValueFactory(new PropertyValueFactory<>("model"));
        stateOrigin.setCellValueFactory(new PropertyValueFactory<>("originState"));
        aeroportOrigin.setCellValueFactory(new PropertyValueFactory<>("originAirport"));
        dateOrigin.setCellValueFactory(new PropertyValueFactory<>("departureDate"));
        timeOrigin.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        stateDestination.setCellValueFactory(new PropertyValueFactory<>("destinationState"));
        aeroportDestination.setCellValueFactory(new PropertyValueFactory<>("destinationAirport"));
        dateDestination.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));
        timeDestination.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        TableColunmseats.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        FlightsTable.setItems(observablelistFlights);

        FilteredList<Flight> filteredFlightsList = new FilteredList<>(observablelistFlights, b -> true);
        textfield.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredFlightsList.setPredicate(flight -> {
                if(newValue.isEmpty() || newValue.isEmpty() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(flight.getID().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if(flight.getModel().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if(flight.getOriginAirport().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if(flight.getDestinationAirport().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if(flight.getDepartureDate().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if(flight.getArrivalDate().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                }  else if(flight.getArrivalTime().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if(flight.getDepartureTime().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if(flight.getOriginState().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if(flight.getDestinationState().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else{
                    return false;
                }
                
            });
        });

        SortedList<Flight> sortedData = new SortedList<Flight>(filteredFlightsList);
        sortedData.comparatorProperty().bind(FlightsTable.comparatorProperty());
        FlightsTable.setItems(sortedData);
    }
}
