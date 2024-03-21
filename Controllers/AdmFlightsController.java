package Controllers;
//import org.controlsfx.control.textfield.AutoCompletionBinding;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AdmFlightsController 
{
    @FXML
    private DatePicker DepartureDateDatePicker;
    @FXML
    private AnchorPane blocoCad, blocoCad1, RootAnchorPane;   
    @FXML
    private VBox home_side_menu;   
    @FXML
    private Text airplaneModelAlert, ticketPriceAlert, milesAlert, originLocationAlert, destinationLocationAlert,
                 originAirportAlert, destinationAirportAlert, departureDateAlert,
                 departureTimeAlert, arrivalTimeAlert, IDAlert, flightScheduleText;
    @FXML
    private TextField EmergencyPriceTextField, IDTextField, MilesValueTextField, NormalPriceTextField, 
                      VipPriceTextField, flightDurationTextField, departureTimeTextField, keywordTextField;                
    @FXML
    private Button AddFlightButton, DeleteFlightButton, EddFlightButton, MenuFlightListButton, 
                   MenuReportButton, MenuFlightButton;
    @FXML
    private ComboBox<String> AirplaneModelCombobox, originRegionCombobox, originStateCombobox, originAirportCombobox,
                             destinationRegionCombobox, destinationStateCombobox, destinationAirportCombobox;                 
    @FXML
    private TableView<Flight> FlightsTable;
    @FXML
    private TableColumn<Flight, String> IDColumn;
    @FXML
    private TableColumn<Flight, String> modelColumn;
    @FXML
    private TableColumn<Flight, String> DateColumn;
    @FXML
    private TableColumn<Flight, String> DestinationColumn;
    @FXML
    private TableColumn<Flight, String> OriginColumn;

    private ArrayList<Flight> flights = new ArrayList<Flight>();

    private ArrayList<String> airplaneModelsList = new ArrayList<>(){{add("ATR 72-600 (54)"); add("Embraer 195 (54)");}};
    private ObservableList<String> airplaneModels = FXCollections.observableArrayList(airplaneModelsList);

    private ArrayList<String> regionsList = new ArrayList<String>(){{add("Norte");add("Nordeste");add("Centro-Oeste"); add("Sudeste");add("Sul");}};
    private ObservableList<String> regionsOptions = FXCollections.observableArrayList(regionsList);
    private ArrayList<String> statesList = new ArrayList<String>();
    private ArrayList<String> airportsList = new ArrayList<String>();

    private String arrivalDate;
    private String arrivalTime;

    private final Locale myLocale = Locale.getDefault();
    public void _initialize() throws IOException
    {
        readFlightsFile();
        AirplaneModelCombobox.setItems(airplaneModels);

        readLocationsFile();
        originRegionCombobox.setItems(regionsOptions);
        destinationRegionCombobox.setItems(regionsOptions);
        originStateCombobox.setItems(FXCollections.observableArrayList(statesList));
        destinationStateCombobox.setItems(FXCollections.observableArrayList(statesList));
        originAirportCombobox.setItems(FXCollections.observableArrayList(airportsList));
        destinationAirportCombobox.setItems(FXCollections.observableArrayList(airportsList));

        DepartureDateDatePicker.setValue(LocalDate.now());
        DepartureDateDatePicker.setOnShowing(e-> Locale.setDefault(Locale.Category.FORMAT,Locale.ENGLISH));
        DepartureDateDatePicker.setOnHiding(e-> Locale.setDefault(Locale.Category.FORMAT,myLocale));
        DepartureDateDatePicker.setOnAction(e-> Locale.setDefault(Locale.Category.FORMAT,myLocale));
        DepartureDateDatePicker.setDayCellFactory(picker -> new DateCell() 
        {
            public void updateItem(LocalDate date, boolean empty) 
            {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0 );
            }
        });

        ObservableList<Flight> tableFlightsObservableList = FXCollections.observableArrayList(flights);
        IDColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("ID"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("model"));
        OriginColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("originAirport"));
        DestinationColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("destinationAirport"));
        DateColumn.setCellValueFactory(new PropertyValueFactory<Flight, String>("departureDate"));

        FlightsTable.setItems(tableFlightsObservableList);

        FilteredList<Flight> filteredFlightsList = new FilteredList<>(tableFlightsObservableList, b -> true);
        keywordTextField.textProperty().addListener((observable, oldValue, newValue) -> {
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
                } else{
                    return false;
                }
            });
        });

        SortedList<Flight> sortedData = new SortedList<Flight>(filteredFlightsList);
        sortedData.comparatorProperty().bind(FlightsTable.comparatorProperty());
        FlightsTable.setItems(sortedData);
    }

    @FXML
    void selectFlight(MouseEvent event)
    {
        int index = FlightsTable.getSelectionModel().getSelectedIndex();
        if(index <= -1)
        {
            return;
        } 
        
        String id = IDColumn.getCellData(index);
        IDTextField.setText(id);
        NormalPriceTextField.setText(flights.get(index).getRegularPrice() + "");
        EmergencyPriceTextField.setText(flights.get(index).getEmergencyPrice() + "");
        VipPriceTextField.setText(flights.get(index).getVipPrice() + "");
        MilesValueTextField.setText(flights.get(index).getMilesPercentage() + "");
        originStateCombobox.setValue(flights.get(index).getOriginState());
        setOriginAirportCombobox(flights.get(index).getOriginState(), flights.get(index).getOriginAirportFull());
        destinationStateCombobox.setValue(flights.get(index).getDestinationState());
        setDestinationAirportCombobox(flights.get(index).getDestinationState(), flights.get(index).getDestinationAirportFull());
        AirplaneModelCombobox.setValue(modelColumn.getCellData(index));

        DepartureDateDatePicker.setValue(LocalDate.parse(flights.get(index).getDepartureDate()));
        departureTimeTextField.setText(flights.get(index).getDepartureTime());

        String departureDateAndTime = DepartureDateDatePicker.getValue().toString() + "T" + departureTimeTextField.getText() + ":00Z";
        String arrivalDateAndTime = flights.get(index).getArrivalDate() + "T" + flights.get(index).getArrivalTime() + "Z";
        String duration = Duration.between(Instant.parse(departureDateAndTime), 
                                        Instant.parse(arrivalDateAndTime)).toString();
        if(duration.length() == 4)
        {
            duration += "00";
        }
        flightDurationTextField.setText(duration.charAt(2) + ":" + duration.charAt(4) + duration.charAt(5));
    }

    @FXML
    void addFlight(ActionEvent event) throws IOException 
    {
        if(validInputs())
        {
            if(!notFoundID(IDTextField.getText()))
            {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("ID Error");
                alert.setHeaderText("ID Already In Use");
                alert.setContentText("To add a flight you must provide a non existing Flight ID");
                alert.showAndWait();
            }
            else
            {
                ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                Alert alert = new Alert(AlertType.CONFIRMATION, "", yesButton, noButton);
                alert.setTitle("Register Confirmation");
                alert.setHeaderText("You are about to add a Flight!");
                alert.setContentText("Are you sure? This will be visible to costumers:");
                
                if(alert.showAndWait().get() == yesButton)
                {
                    cadFlight();
                }
            }             
        }
    }

    private void cadFlight() throws IOException
    {
        Flight f = new Flight(IDTextField.getText(), AirplaneModelCombobox.getSelectionModel().getSelectedItem(), 
                               originAirportCombobox.getSelectionModel().getSelectedItem(), originStateCombobox.getSelectionModel().getSelectedItem(), 
                               destinationAirportCombobox.getSelectionModel().getSelectedItem(), destinationStateCombobox.getSelectionModel().getSelectedItem(), 
                               DepartureDateDatePicker.getValue().toString(), departureTimeTextField.getText(), arrivalDate, arrivalTime, Double.parseDouble(NormalPriceTextField.getText()), 
                               Double.parseDouble(EmergencyPriceTextField.getText()), Double.parseDouble(VipPriceTextField.getText()), Double.parseDouble(MilesValueTextField.getText()));
        flights.add(f);
        FileWriter fw = new FileWriter("src/txt_files/flights.txt", true);
        fw.append(f.toTxt() + "\n");
        fw.close();

        ObservableList<Flight> tableFlightsObservableList = FXCollections.observableArrayList(flights);
        FlightsTable.setItems(tableFlightsObservableList);
    }

    @FXML
    void deleteFlight(ActionEvent event) throws IOException 
    {
        if(notFoundID(IDTextField.getText()))
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("ID Error");
            alert.setHeaderText("ID Not Found");
            alert.setContentText("To delete flight informations you must provide an existing Flight ID");
            alert.showAndWait();
        }
        else
        {
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            Alert alert = new Alert(AlertType.CONFIRMATION, "", yesButton, noButton);
            alert.setTitle("Delete Confirmation");
            alert.setHeaderText("You are about to DELETE a Flight!");
            alert.setContentText("Are you sure? This will delete all informations of the Flight:");
            
            if(alert.showAndWait().get() == yesButton)
            {
                deleteFlight();
            }
        }             
    }

    private void deleteFlight() throws IOException
    {
        FileWriter fw = new FileWriter("src/txt_files/flights.txt", false);
        fw.append("");
        fw.close();

        for (int i = 0; i < flights.size(); i++) 
        {
            if(IDTextField.getText().equals(flights.get(i).getID()))
                flights.remove(i);
        }

        FileWriter fw2 = new FileWriter("src/txt_files/flights.txt", true);
        for (int i = 0; i < flights.size(); i++) 
        {
            if(!IDTextField.getText().equals(flights.get(i).getID()))
                fw2.append(flights.get(i).toTxt() + "\n");
        }
        fw2.close();

        ObservableList<Flight> tableFlightsObservableList = FXCollections.observableArrayList(flights);
        FlightsTable.setItems(tableFlightsObservableList);
    }

    @FXML
    void editFlight(ActionEvent event) throws IOException 
    {
        if(validInputs())
        {
            if(notFoundID(IDTextField.getText()))
            {
                errorAlert("ID Error", "ID Not Found", "To edit flight informations you must provide an existing Flight ID");
            }
            else if(!flights.get(getFlightIndex(IDTextField.getText())).isEmpty() && !flights.get(getFlightIndex(IDTextField.getText())).getModel().equals(AirplaneModelCombobox.getSelectionModel().getSelectedItem()))
            {
                errorAlert("Airplane Model Error", "There are already costumers who bought tickets here!", "You must delete the flight if you really want to edit this.");
            }
            else
            {
                boolean choice = confirmationAlert("Edit Confirmation", "You are about to change one or more informations!", "Are you sure? This will erase previous informations:");
                if(choice)
                    editFlight();
            }             
        }
    }

    private void editFlight() throws IOException
    {
        FileWriter fw = new FileWriter("src/txt_files/flights.txt", false);
        fw.append("");
        fw.close();

        Flight f = new Flight(IDTextField.getText(), AirplaneModelCombobox.getSelectionModel().getSelectedItem(), 
                               originAirportCombobox.getSelectionModel().getSelectedItem(), originStateCombobox.getSelectionModel().getSelectedItem(), 
                               destinationAirportCombobox.getSelectionModel().getSelectedItem(), destinationStateCombobox.getSelectionModel().getSelectedItem(), 
                               DepartureDateDatePicker.getValue().toString(), departureTimeTextField.getText(), arrivalDate, arrivalTime, Double.parseDouble(NormalPriceTextField.getText()), 
                               Double.parseDouble(EmergencyPriceTextField.getText()), Double.parseDouble(VipPriceTextField.getText()), Double.parseDouble(MilesValueTextField.getText()),
                               flights.get(getFlightIndex(IDTextField.getText())).getSeatsStatus(),
                               flights.get(getFlightIndex(IDTextField.getText())).CPF_name);

        FileWriter fw2 = new FileWriter("src/txt_files/flights.txt", true);
        for (int i = 0; i < flights.size(); i++) 
        {
            if(IDTextField.getText().equals(flights.get(i).getID()))
            {   
                fw2.append(f.toTxt() + "\n"); 
                flights.set(i, f);           
            }
            else
            {
                fw2.append(flights.get(i).toTxt() + "\n");
            } 
        }
        fw2.close();

        ObservableList<Flight> tableFlightsObservableList = FXCollections.observableArrayList(flights);
        FlightsTable.setItems(tableFlightsObservableList);
    }

    @FXML
    void setDestinationAirportCombobox(ActionEvent event) 
    {
        String destinationState = destinationStateCombobox.getSelectionModel().getSelectedItem();

        if(destinationState != null)
        {
            ArrayList<String> validRegion = new ArrayList<String>();
            if(destinationState.contains("AC") || destinationState.contains("AM") || destinationState.contains("AP") || destinationState.contains("PA") || destinationState.contains("RO") || destinationState.contains("RR") || destinationState.contains("TO"))
            {
                validRegion.add("Norte");
            }
            else if(destinationState.contains("AL") || destinationState.contains("BA") || destinationState.contains("CE") || destinationState.contains("MA") || destinationState.contains("PB") || destinationState.contains("PE") || destinationState.contains("PI") || destinationState.contains("RN") || destinationState.contains("SE"))
            {
                validRegion.add("Nordeste");
            }
            else if(destinationState.contains("GO") || destinationState.contains("MT") || destinationState.contains("MS") || destinationState.contains("DF"))
            {
                validRegion.add("Centro-Oeste");
            }
            else if(destinationState.contains("SP") || destinationState.contains("RJ") || destinationState.contains("MG") || destinationState.contains("ES"))
            {
                validRegion.add("Sudeste");
            }
            else if(destinationState.contains("PR") || destinationState.contains("SC") || destinationState.contains("RS"))
            {
                validRegion.add("Sul");
            }
            

            ArrayList<String> validAirports = new ArrayList<String>();
            for (String airport : airportsList) 
            {
                if(airport.contains(destinationState))
                    validAirports.add(airport);
            }

            destinationRegionCombobox.setValue(validRegion.get(0));

            destinationAirportCombobox.setItems(FXCollections.observableArrayList(validAirports));
            destinationAirportCombobox.setValue(validAirports.get(0));            
        }
    }

    private void setDestinationAirportCombobox(String state, String airportDestination) 
    {
        String destinationState = state;

        if(destinationState != null)
        {
            ArrayList<String> validRegion = new ArrayList<String>();
            if(destinationState.contains("AC") || destinationState.contains("AM") || destinationState.contains("AP") || destinationState.contains("PA") || destinationState.contains("RO") || destinationState.contains("RR") || destinationState.contains("TO"))
            {
                validRegion.add("Norte");
            }
            else if(destinationState.contains("AL") || destinationState.contains("BA") || destinationState.contains("CE") || destinationState.contains("MA") || destinationState.contains("PB") || destinationState.contains("PE") || destinationState.contains("PI") || destinationState.contains("RN") || destinationState.contains("SE"))
            {
                validRegion.add("Nordeste");
            }
            else if(destinationState.contains("GO") || destinationState.contains("MT") || destinationState.contains("MS") || destinationState.contains("DF"))
            {
                validRegion.add("Centro-Oeste");
            }
            else if(destinationState.contains("SP") || destinationState.contains("RJ") || destinationState.contains("MG") || destinationState.contains("ES"))
            {
                validRegion.add("Sudeste");
            }
            else if(destinationState.contains("PR") || destinationState.contains("SC") || destinationState.contains("RS"))
            {
                validRegion.add("Sul");
            }
            

            ArrayList<String> validAirports = new ArrayList<String>();
            for (String airport : airportsList) 
            {
                if(airport.contains(destinationState))
                    validAirports.add(airport);
            }

            destinationRegionCombobox.setValue(validRegion.get(0));

            destinationAirportCombobox.setItems(FXCollections.observableArrayList(validAirports));
            destinationAirportCombobox.setValue(airportDestination);            
        }
    }

    @FXML
    void setDestinationStateCombobox(ActionEvent event) 
    {
        String destinationRegion = destinationRegionCombobox.getSelectionModel().getSelectedItem();
        ArrayList<String> validStates = new ArrayList<String>();

        if(destinationRegion != null)
        {
            for (String state : statesList) 
            {
                if(destinationRegion.equals("Norte"))
                {
                    if(state.contains("AC") || state.contains("AM") || state.contains("AP") || state.contains("PA") || state.contains("RO") || state.contains("RR") || state.contains("TO"))
                        validStates.add(state);
                }
                else if(destinationRegion.equals("Nordeste"))
                {
                    if(state.contains("AL") || state.contains("BA") || state.contains("CE") || state.contains("MA") || state.contains("PB") || state.contains("PE") || state.contains("PI") || state.contains("RN") || state.contains("SE"))
                        validStates.add(state);
                }
                else if(destinationRegion.equals("Centro-Oeste"))
                {
                    if(state.contains("GO") || state.contains("MT") || state.contains("MS") || state.contains("DF"))
                        validStates.add(state);
                }
                else if(destinationRegion.equals("Sudeste"))
                {
                    if(state.contains("SP") || state.contains("RJ") || state.contains("MG") || state.contains("ES"))
                        validStates.add(state);
                }
                else if(destinationRegion.equals("Sul"))
                {
                    if(state.contains("PR") || state.contains("SC") || state.contains("RS"))
                        validStates.add(state);
                }
            }

            ArrayList<String> validAirports = new ArrayList<String>();
            for (String airport : airportsList) 
            {
                if(airport.contains(validStates.get(0)))
                    validAirports.add(airport);
            }

            destinationStateCombobox.setItems(FXCollections.observableArrayList(validStates));
            destinationStateCombobox.setValue(validStates.get(0));
            destinationAirportCombobox.setItems(FXCollections.observableArrayList(validAirports));
            destinationAirportCombobox.setValue(validAirports.get(0));            
        }
    }

    @FXML
    void setOriginAirportCombobox(ActionEvent event) 
    {
        String originState = originStateCombobox.getSelectionModel().getSelectedItem();

        if(originState != null)
        {
            ArrayList<String> validRegion = new ArrayList<String>();
            if(originState.contains("AC") || originState.contains("AM") || originState.contains("AP") || originState.contains("PA") || originState.contains("RO") || originState.contains("RR") || originState.contains("TO"))
            {
                validRegion.add("Norte");
            }
            else if(originState.contains("AL") || originState.contains("BA") || originState.contains("CE") || originState.contains("MA") || originState.contains("PB") || originState.contains("PE") || originState.contains("PI") || originState.contains("RN") || originState.contains("SE"))
            {
                validRegion.add("Nordeste");
            }
            else if(originState.contains("GO") || originState.contains("MT") || originState.contains("MS") || originState.contains("DF"))
            {
                validRegion.add("Centro-Oeste");
            }
            else if(originState.contains("SP") || originState.contains("RJ") || originState.contains("MG") || originState.contains("ES"))
            {
                validRegion.add("Sudeste");
            }
            else if(originState.contains("PR") || originState.contains("SC") || originState.contains("RS"))
            {
                validRegion.add("Sul");
            }
            

            ArrayList<String> validAirports = new ArrayList<String>();
            for (String airport : airportsList) 
            {
                if(airport.contains(originState))
                    validAirports.add(airport);
            }

            originRegionCombobox.setValue(validRegion.get(0));

            originAirportCombobox.setItems(FXCollections.observableArrayList(validAirports));
            originAirportCombobox.setValue(validAirports.get(0));            
        }
    }

    @FXML
    void setOriginStateCombobox(ActionEvent event) 
    {
        String originRegion = originRegionCombobox.getSelectionModel().getSelectedItem();
    
        if(originRegion != null)
        {
            ArrayList<String> validStates = new ArrayList<String>();
            for (String state : statesList) 
            {
                if(originRegion.equals("Norte"))
                {
                    if(state.contains("AC") || state.contains("AM") || state.contains("AP") || state.contains("PA") || state.contains("RO") || state.contains("RR") || state.contains("TO"))
                        validStates.add(state);
                }
                else if(originRegion.equals("Nordeste"))
                {
                    if(state.contains("AL") || state.contains("BA") || state.contains("CE") || state.contains("MA") || state.contains("PB") || state.contains("PE") || state.contains("PI") || state.contains("RN") || state.contains("SE"))
                        validStates.add(state);
                }
                else if(originRegion.equals("Centro-Oeste"))
                {
                    if(state.contains("GO") || state.contains("MT") || state.contains("MS") || state.contains("DF"))
                        validStates.add(state);
                }
                else if(originRegion.equals("Sudeste"))
                {
                    if(state.contains("SP") || state.contains("RJ") || state.contains("MG") || state.contains("ES"))
                        validStates.add(state);
                }
                else if(originRegion.equals("Sul"))
                {
                    if(state.contains("PR") || state.contains("SC") || state.contains("RS"))
                        validStates.add(state);
                }
            }

            ArrayList<String> validAirports = new ArrayList<String>();
            for (String airport : airportsList) 
            {
                if(airport.contains(validStates.get(0)))
                    validAirports.add(airport);
            }

            originStateCombobox.setItems(FXCollections.observableArrayList(validStates));
            originStateCombobox.setValue(validStates.get(0));

            originAirportCombobox.setItems(FXCollections.observableArrayList(validAirports));
            originAirportCombobox.setValue(validAirports.get(0));            
        }
    }

    private void setOriginAirportCombobox(String state, String airportOrigin) 
    {
        String originState = state;

        if(originState != null)
        {
            ArrayList<String> validRegion = new ArrayList<String>();
            if(originState.contains("AC") || originState.contains("AM") || originState.contains("AP") || originState.contains("PA") || originState.contains("RO") || originState.contains("RR") || originState.contains("TO"))
            {
                validRegion.add("Norte");
            }
            else if(originState.contains("AL") || originState.contains("BA") || originState.contains("CE") || originState.contains("MA") || originState.contains("PB") || originState.contains("PE") || originState.contains("PI") || originState.contains("RN") || originState.contains("SE"))
            {
                validRegion.add("Nordeste");
            }
            else if(originState.contains("GO") || originState.contains("MT") || originState.contains("MS") || originState.contains("DF"))
            {
                validRegion.add("Centro-Oeste");
            }
            else if(originState.contains("SP") || originState.contains("RJ") || originState.contains("MG") || originState.contains("ES"))
            {
                validRegion.add("Sudeste");
            }
            else if(originState.contains("PR") || originState.contains("SC") || originState.contains("RS"))
            {
                validRegion.add("Sul");
            }
            

            ArrayList<String> validAirports = new ArrayList<String>();
            for (String airport : airportsList) 
            {
                if(airport.contains(originState))
                    validAirports.add(airport);
            }

            originRegionCombobox.setValue(validRegion.get(0));

            originAirportCombobox.setItems(FXCollections.observableArrayList(validAirports));
            originAirportCombobox.setValue(airportOrigin);            
        }
    }

    @FXML
    void flightListLoader(ActionEvent event) throws IOException 
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

    @FXML
    void generateReportLoader(ActionEvent event) throws IOException
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

    private boolean validInputs()
    {
        boolean isValid = true;

        if(invalidDouble(NormalPriceTextField.getText()) || invalidDouble(EmergencyPriceTextField.getText()) || invalidDouble(VipPriceTextField.getText()))
        {
            ticketPriceAlert.setText("* invalid value");
            return false;
        }
        else
            ticketPriceAlert.setText(" ");

        if(AirplaneModelCombobox.getSelectionModel().getSelectedItem() == null)
        {
            airplaneModelAlert.setText("* select one model");
            return false;
        }
        else
            airplaneModelAlert.setText(" ");

        if(invalidDouble(MilesValueTextField.getText()))
        {
            milesAlert.setText("* invalid value");
            return false;    
        }
        else if(Double.parseDouble(MilesValueTextField.getText()) >= 1)
        {
            milesAlert.setText("* invalid range [0 < m < 1]");
            return false;                 
        }
        else
            milesAlert.setText(" ");

        if(originRegionCombobox.getSelectionModel().getSelectedItem() == null || originStateCombobox.getSelectionModel().getSelectedItem() == null)
        {
            originLocationAlert.setText("* select the location");
            return false;
        }
        else
            originLocationAlert.setText(" ");

        if(destinationRegionCombobox.getSelectionModel().getSelectedItem() == null || destinationStateCombobox.getSelectionModel().getSelectedItem() == null)
        {
            destinationLocationAlert.setText("* select the location");
            return false;
        }
        else
            destinationLocationAlert.setText(" ");

        if(destinationAirportCombobox.getSelectionModel().getSelectedItem() == null || destinationAirportCombobox.getSelectionModel().getSelectedItem() == null)
        {
            destinationAirportAlert.setText("* select one airport");
            return false;
        }
        else
            destinationAirportAlert.setText(" ");
        
        if(originAirportCombobox.getSelectionModel().getSelectedItem() == null || originAirportCombobox.getSelectionModel().getSelectedItem() == null)
        {
            originAirportAlert.setText("* select one airport");
            return false;
        }
        else
            originAirportAlert.setText(" ");

        if(DepartureDateDatePicker.getValue() == null)
        {
            departureDateAlert.setText("* invalid date");
            return false; 
        }
        else
            departureDateAlert.setText(" ");
        
        if(departureTimeTextField.getText().equals("") || !departureTimeTextField.getText().contains(":") || departureTimeTextField.getText().length() != 5)
        {
            departureTimeAlert.setText("\n * invalid time");
            return false;  
        }
        else
        {
            String[] time = departureTimeTextField.getText().split(":");
            if(invalidDouble(time[0]) || invalidDouble(time[1]))
            {
                departureTimeAlert.setText("\n * invalid time");
                return false;  
            }
            else if(Integer.parseInt(time[0]) >= 60 || Integer.parseInt(time[1]) >= 60)
            {
                departureTimeAlert.setText("\n * invalid duration");
                return false; 
            }
            else
            {
                departureTimeAlert.setText(" ");
                isValid = validDepartureTime();                
            }
        }

        if(flightDurationTextField.getText().equals("") || !flightDurationTextField.getText().contains(":"))
        {
            arrivalTimeAlert.setText("* invalid duration");
            return false;  
        }
        else
        {
            String[] time = flightDurationTextField.getText().split(":");
            if(invalidDouble(time[0]) || invalidDouble(time[1]) || time[0].length() > 2 || time[1].length() != 2)
            {
                arrivalTimeAlert.setText("* invalid duration");
                return false;  
            }
            else if(Integer.parseInt(time[0]) >= 60 || Integer.parseInt(time[1]) >= 60)
            {
                arrivalTimeAlert.setText("* invalid duration");
                return false; 
            }
            else
            {
                arrivalTimeAlert.setText(" ");
                if(validDepartureTime())
                    return calculateArrivalTime(Integer.parseInt(time[0]), Integer.parseInt(time[1]));                
            }
        }

        if(IDTextField.getText().contains(";") || IDTextField.getText().equals(""))
        {
            IDAlert.setText("* invalid ID");
            return false;  
        }
        else
            IDAlert.setText(" ");

        return isValid;
    }

    private boolean validDepartureTime()
    {
        boolean isValid = true;
        String departureDateAndTime = DepartureDateDatePicker.getValue().toString() + "T" + departureTimeTextField.getText() + ":00Z";
        Instant departureInstant;
        try {
            departureInstant = Instant.parse(departureDateAndTime);
        } catch (Exception e) {
            return false;
        }
        
        Instant now = Instant.now().minusSeconds(3*3600);

        if(departureInstant.minusSeconds(5*3600).isBefore(now))
        {
            departureTimeAlert.setText("* it must be at least \n  5h after now");
            isValid = false;
        }

        return isValid;
    }

    private boolean calculateArrivalTime(int hours, int minutes)
    {
        boolean isValid = true;

        String departureDateAndTime = DepartureDateDatePicker.getValue().toString() + "T" + departureTimeTextField.getText() + ":00Z";
        Instant departureInstant = Instant.parse(departureDateAndTime);
        int flightLenght = ((hours*60*60)+(minutes*60));
        Instant arrivalInstant = Instant.parse(departureDateAndTime).plusSeconds(flightLenght);

        String[] s = arrivalInstant.toString().split("T");
        arrivalDate = s[0];
        String[] ss = s[1].split("Z");
        arrivalTime = ss[0];

        if(flightLenght < 10*60)
        {
            arrivalTimeAlert.setText("* invalid duration");
            isValid = false;
        }
        else
        {
            flightScheduleText.setText("Departure -> arrival\n" + "Date: " +
                departureInstant.toString().replace('-', '/').replace("T", " Time: ").replace('Z', ' ') + " ->\n" +
                "Date: " + arrivalInstant.toString().replace('-', '/').replace("T", " Time: ").replace('Z', ' '));
        }
            
        return isValid;
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
 
    private void readLocationsFile() throws IOException 
    {
        BufferedReader airportsBF = new BufferedReader(new FileReader("src/txt_files/airports.txt"));
        String line;
        while((line = airportsBF.readLine()) != null)
        {
            String[] airport_state = line.split(",");
            airportsList.add(line);
            if(!statesList.contains(airport_state[1]))
            {
                statesList.add(airport_state[1]);
            }
        }   
        airportsBF.close();    
    }

    private boolean notFoundID(String id)
    {
        if(id.contains(";"))
            return true;

        for (Flight flight : flights) 
        {
            if(flight.getID().equals(id))
            {
                return false;
            }
        }
        return true;
    }

    private boolean invalidDouble(String s)
    {
        try {
            Double.parseDouble(s);
        } catch (Exception e) {
            return true;
        }
        return false;
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
}

