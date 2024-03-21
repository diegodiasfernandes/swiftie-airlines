package Controllers;
// import java.awt.event.ActionEvent;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;

import Classes.Flight;
import Classes.Seat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TableView;

public class ReportController  
{
    @FXML
    private AnchorPane root;
    @FXML
    private VBox home_side_menu;
    @FXML
    private Button listButton;
    @FXML
    private Button flightsButton, finishButton;
    @FXML
    private Button logoutbutton;
    @FXML
    private AnchorPane blocoCad;
    @FXML
    private TextField textfield;
    @FXML
    private Button buttonbusca;
    @FXML
    private TableView<Seat> tableView;
    @FXML
    private TableColumn<Seat,String> nameColumn;
    @FXML
    private TableColumn<Seat,String> cpfColunm;
    @FXML
    private TableColumn<Seat,String> priceColunm;
    @FXML
    private TableColumn<Seat,String> seatTypeColunm;
    @FXML
    private TableColumn<Seat,String> seatColumn;
    @FXML
    private Text textTotalPrice;

    private ArrayList<Seat> seats = new ArrayList<Seat>();


    public void _initialize(Flight flight) throws IOException 
    {
        int i = 1;
        double totalPrice = 0;
        for (Seat seat : flight.getSeats()) 
        {
            seat.setIndex(i);
            i++;
            seats.add(seat);
            if(!seat.getCpf().equals("Empty"))
                totalPrice += seat.getPrice();
        }
        textTotalPrice.setText(totalPrice+"");
        loadViewFlights();
    }

    public void loadViewFlights() throws IOException 
    {
        ObservableList<Seat> observablelistSeat = FXCollections.observableArrayList(seats);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cpfColunm.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        priceColunm.setCellValueFactory(new PropertyValueFactory<>("price"));
        seatTypeColunm.setCellValueFactory(new PropertyValueFactory<>("seatType"));
        seatColumn.setCellValueFactory(new PropertyValueFactory<>("index"));

        tableView.setItems(observablelistSeat);
    }

    @FXML
    void flightListLoader(ActionEvent event) throws IOException 
    {
        FXMLLoader loader = new FXMLLoader((getClass().getResource("../Screens/admflights.fxml")));
        Parent root = loader.load();

        String css = this.getClass().getResource("../StyleSheets/style.css").toExternalForm();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(css); 

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        AdmFlightsController controller = loader.getController();
        controller._initialize();

        stage.setScene(scene);
        stage.centerOnScreen();        
        Image image = new Image("imagens\\logo.png");
        stage.getIcons().add(image);
        stage.show(); 
    }

    @FXML
    void finishReport(ActionEvent event) throws IOException 
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
    void flightsButton(ActionEvent event) throws IOException 
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
    void logoutbutton(ActionEvent event) throws IOException 
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

}
