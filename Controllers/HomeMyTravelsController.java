package Controllers;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Classes.Costumer;
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


public class HomeMyTravelsController  {

    @FXML
    private AnchorPane root;
    @FXML
    private VBox home_side_menu;
    @FXML
    private Button buyTicketsButton;
    @FXML
    private Button button11;
    @FXML
    private Button checkinButton;
    @FXML
    private Button acessoriesButton;
    @FXML
    private Button configurationButton;
    @FXML
    private Button logoutButton;
    @FXML
    private TextField textfield;
    @FXML
    private Button buttonbusca;
    @FXML
    private TableView<Flight> myTravelsTable;
    @FXML
    private TableColumn<Flight, String> originColumn;
    @FXML
    private TableColumn<Flight, String> destinationColumn;
    @FXML
    private TableColumn<Flight, String> dateColumn;
    @FXML
    private TableColumn<Flight, String> timeColumn;

    private ArrayList<Flight> flights = new ArrayList<Flight>();
    private Costumer user;

    public void _initialize(Costumer user) throws IOException
    {
        this.user = user;
        readFlightsFile();

        ArrayList<Flight> userFlights = new ArrayList<Flight>();
        for (Flight flight : flights)
        {
            if(user.getFlightIDs().contains(flight.getID()))
                userFlights.add(flight);
        }

        ObservableList<Flight> tableFlightsObservableList = FXCollections.observableArrayList(userFlights);
        originColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("originAirportFull"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("destinationAirportFull"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("departureDate"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("departureTime"));

        myTravelsTable.setItems(tableFlightsObservableList);

        FilteredList<Flight> originFilteredFlightsList = new FilteredList<>(tableFlightsObservableList, b -> true);
        textfield.textProperty().addListener((observable, oldValue, newValue) -> {
            originFilteredFlightsList.setPredicate(flight -> {
                if(newValue.isEmpty() || newValue.isEmpty() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if(flight.getDepartureTime().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if(flight.getOriginAirportFull().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if(flight.getDepartureDate().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if(flight.getDestinationAirportFull().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if(flight.getArrivalDate().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else{
                    return false;
                }
            });
        });

        SortedList<Flight> originSortedData = new SortedList<Flight>(originFilteredFlightsList);
        originSortedData.comparatorProperty().bind(myTravelsTable.comparatorProperty());
        myTravelsTable.setItems(originSortedData);
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
    void buyTicketsLoader(ActionEvent event) throws IOException {
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
