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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BuyHomeTicketsController 
{
    @FXML
    private Button button1, button11, checkinButton, button111111, button1111121, button112, button1121, confirmButton, selectSeatsButton;
    @FXML
    private VBox home_side_menu;
    @FXML
    private AnchorPane root;
    @FXML
    private TextField originTextField, destinationTextField, dateTextField;
    @FXML
    private TableView<Flight> FlightsTable;
    @FXML
    private TableColumn<Flight, String> IDColumn, OriginColumn, DestinationColumn, DateColumn, timeColumn;

    @FXML
    private Text myMilesText;

    private ArrayList<Flight> flights = new ArrayList<Flight>();
    private String id = "";
    private Costumer user;

    public void _initialize(Costumer user) throws IOException
    {
        this.user = user;
        myMilesText.setText(Math.round(user.getMiles())+"");

        readFlightsFile();

        ObservableList<Flight> tableFlightsObservableList = FXCollections.observableArrayList(flights);
        IDColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("ID"));
        OriginColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("originAirportFull"));
        DestinationColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("destinationAirportFull"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("departureDate"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("departureTime"));

        FlightsTable.setItems(tableFlightsObservableList);

        FilteredList<Flight> originFilteredFlightsList = new FilteredList<>(tableFlightsObservableList, b -> true);
        originTextField.textProperty().addListener((observable, oldValue, newValue) -> {
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
                } else{
                    return false;
                }
            });
        });

        SortedList<Flight> originSortedData = new SortedList<Flight>(originFilteredFlightsList);
        originSortedData.comparatorProperty().bind(FlightsTable.comparatorProperty());
        FlightsTable.setItems(originSortedData);
    }

    public void _initialize(Costumer user, String originAirport, String destinationAirport) throws IOException
    {
        this.user = user;
        myMilesText.setText(Math.round(user.getMiles())+"");
        originTextField.setText(originAirport);
        destinationTextField.setText(destinationAirport);

        readFlightsFile();

        ObservableList<Flight> tableFlightsObservableList = FXCollections.observableArrayList(flights);
        IDColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("ID"));
        OriginColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("originAirportFull"));
        DestinationColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("destinationAirportFull"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("departureDate"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("departureTime"));
        FlightsTable.setItems(tableFlightsObservableList);

        ArrayList<Flight> fs = new ArrayList<Flight>();
        for (int i = 0; i < FlightsTable.getItems().size(); i++) 
        {
            if(DestinationColumn.getCellData(i).toLowerCase().contains(destinationAirport.toLowerCase()) && OriginColumn.getCellData(i).toLowerCase().contains(originAirport.toLowerCase()))
                fs.add(flights.get(getFlightIndex(IDColumn.getCellData(i))));
        }

        ObservableList<Flight> tableFlightsObservableList2 = FXCollections.observableArrayList(fs);
        FlightsTable.setItems(tableFlightsObservableList2);
    }

    @FXML
    void selectFlight(MouseEvent event)
    {
        int index = FlightsTable.getSelectionModel().getSelectedIndex();
        id = IDColumn.getCellData(index);
    }

    @FXML
    void buyTicketsLoader(ActionEvent event) throws IOException 
    {
        if(id == null)
        {
            errorAlert("ID Error", "No Flight Selected!", "Select a flight from the table!");
            return;
        }
        if(id.equals(""))
        {
            errorAlert("ID Error", "No Flight Selected!", "Select a flight from the table!");
            return; 
        }

        String model = "";
        for (Flight flight : flights) 
        {
            if(flight.getID().equals(id))
                model = flight.getModel();
        }

        if(model.equals("ATR 72-600 (54)"))
        {
            FXMLLoader loader = new FXMLLoader((getClass().getResource("../Screens/buytickets.fxml")));
            Parent root = loader.load();

            String css = this.getClass().getResource("../StyleSheets/style.css").toExternalForm();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(css); 

            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            BuyTicketsController controller = loader.getController();
            controller._initialize(this.user, id);

            stage.setScene(scene);
            stage.centerOnScreen();
            Image image = new Image("imagens\\logo.png");
            stage.getIcons().add(image);
            stage.show();             
        }
        else
        {
            FXMLLoader loader = new FXMLLoader((getClass().getResource("../Screens/buytickets2.fxml")));
            Parent root = loader.load();

            String css = this.getClass().getResource("../StyleSheets/style.css").toExternalForm();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(css); 

            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

            BuyTicketsController controller = loader.getController();
            controller._initialize(this.user, id);

            stage.setScene(scene);
            stage.centerOnScreen();
            Image image = new Image("imagens\\logo.png");
            stage.getIcons().add(image);
            stage.show();  
        }
     
    }

    @FXML
    void searchDestination()
    {
        ArrayList<Flight> fs = new ArrayList<Flight>();
        String destination = destinationTextField.getText();
        String origin = originTextField.getText();
        String date = dateTextField.getText();
        if(destination.equals("") && date.equals(""))
        {
            ObservableList<Flight> tableFlightsObservableList = FXCollections.observableArrayList(flights);
            FilteredList<Flight> originFilteredFlightsList = new FilteredList<>(tableFlightsObservableList, b -> true);
            originTextField.textProperty().addListener((observable, oldValue, newValue) -> {
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
                    } else{
                        return false;
                    }
                });
            });

            SortedList<Flight> originSortedData = new SortedList<Flight>(originFilteredFlightsList);
            originSortedData.comparatorProperty().bind(FlightsTable.comparatorProperty());
            FlightsTable.setItems(originSortedData);
            return;
        }

        for (int i = 0; i < FlightsTable.getItems().size(); i++) 
        {
            if(DestinationColumn.getCellData(i).toLowerCase().contains(destination.toLowerCase()) && OriginColumn.getCellData(i).toLowerCase().contains(origin.toLowerCase()) && DateColumn.getCellData(i).contains(date))
                fs.add(flights.get(getFlightIndex(IDColumn.getCellData(i))));
        }
        ObservableList<Flight> tableFlightsObservableList = FXCollections.observableArrayList(fs);
        FlightsTable.setItems(tableFlightsObservableList);    
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

    private int getFlightIndex(String id)
    {
        int i = 0;
        for (Flight flight : flights) 
        {
            if(id.equals(flight.getID()))
                return i;
            i++;
        }
        return -1;
    }

    private void errorAlert(String title, String header, String content)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();   
    }
}