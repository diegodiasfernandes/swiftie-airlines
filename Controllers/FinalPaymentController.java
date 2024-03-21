package Controllers;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import Classes.Costumer;
import Classes.Flight;
import Classes.Seat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FinalPaymentController 
{

    @FXML
    private Button button1, button11, button111, button111111, button1111121, cardButton, milesButton,
                    cancelButton;
    @FXML
    private VBox home_side_menu;
    @FXML
    private TextField seat3TextField, seat2TextField, cpf3TextField, cpf2TextField, seat1TextField, cpf1TextField;
    @FXML
    private AnchorPane root;
    @FXML
    private Text seat1Text, seat2Text, seat3Text, seatType1Text, seatType2Text, seatType3Text, totalPriceText,
                 destinationAirportText, originAirportText, myMilesText;
    @FXML
    private Label milesValueLabel;

    private Costumer user;
    private Flight flight;
    private String id;
    private ArrayList<Flight> flights = new ArrayList<Flight>();
    private ArrayList<Costumer> costumers = new ArrayList<Costumer>();

    public void _initialize(Costumer user, String id, Flight flight, String totalMoney, String milesValue, String seatSelected1,
                            String seatSelected2, String seatSelected3, String seatType1, String seatType2, String seatType3) throws IOException
    {
        readCostumersFile();
        this.id = id;
        this.user = user;
        myMilesText.setText(user.getMiles()+"");
        readFlightsFile();
        this.flight = flight;
        seat1Text.setText(seatSelected1);
        seat2Text.setText(seatSelected2);
        seat3Text.setText(seatSelected3);
        seatType1Text.setText(seatType1);
        seatType2Text.setText(seatType2);
        seatType3Text.setText(seatType3);
        totalPriceText.setText(totalMoney);
        milesValueLabel.setText(milesValue);
        String airport[] = flight.getDestinationAirportFull().split(",");
        destinationAirportText.setText(airport[0]);
        String airport2[] = flight.getOriginAirportFull().split(",");
        originAirportText.setText(airport2[0]);
        if(seatSelected3.equals(""))
        {
            seat3Text.setText("Not Selected");
            seat3TextField.setEditable(false);
            seat3TextField.setPromptText("No Selected Seat");
            cpf3TextField.setEditable(false);
            cpf3TextField.setPromptText("No Selected Seat");
        }
        if(seatSelected2.equals(""))
        {
            seat2Text.setText("Not Selected");
            seat2TextField.setEditable(false);
            seat2TextField.setPromptText("No Selected Seat");
            cpf2TextField.setEditable(false);
            cpf2TextField.setPromptText("No Selected Seat");
        }
    }

    @FXML
    void cardPayment(ActionEvent event) throws IOException 
    {
        if(seat1TextField.getText().equals("") || cpf1TextField.getText().equals(""))
        {
            errorAlert("Passenger Error", "Name or CPF are blank!", "You must provide a name and a CPF for every seat!");
            return;
        }
        if(seat2TextField.editableProperty().getValue() && (seat2TextField.getText().equals("") || cpf2TextField.getText().equals("")))
        {
            errorAlert("Passenger Error", "Name or CPF are blank!", "You must provide a name and a CPF for every seat!");
            return;
        }
        if(seat3TextField.editableProperty().getValue() && (seat3TextField.getText().equals("") || cpf3TextField.getText().equals("")))
        {
            errorAlert("Passenger Error", "Name or CPF are blank!", "You must provide a name and a CPF for every seat!");
            return;
        }

        FileWriter fw = new FileWriter("src/txt_files/flights.txt", false);
        fw.append("");
        fw.close();

        int i = 0;
        for (Seat seat : flight.getSeats()) 
        {
            if(seat.getisSelected() && i == 0)
            {
                seat.setisSelected(false);
                String cpf_costumer[] = {cpf1TextField.getText(), seat1TextField.getText()};
                seat.setCpf(cpf_costumer[0]);
                seat.setName(cpf_costumer[1]);
                seat.setisEmpty(false);
                i++;
            }
            else if(seat.getisSelected() && i == 1)
            {
                seat.setisSelected(false);
                String cpf_costumer[] = {cpf2TextField.getText(), seat2TextField.getText()};
                seat.setCostumer(cpf_costumer);
                seat.setisEmpty(false);
                i++;
            }
            else if(seat.getisSelected() && i == 2)
            {
                seat.setisSelected(false);
                String cpf_costumer[] = {cpf3TextField.getText(), seat3TextField.getText()};
                seat.setCostumer(cpf_costumer);
                seat.setisEmpty(false);
            }
        }

        FileWriter fw2 = new FileWriter("src/txt_files/flights.txt", true);
        for (int j = 0; j < flights.size(); j++) 
        {
            if(flight.getID().equals(flights.get(j).getID()))
            {
                fw2.append(flight.toTxt() + "\n");
                flights.set(j, flight);                  
            }
            else
            {
                fw2.append(flights.get(j).toTxt() + "\n");
            } 
        }
        fw2.close();

        if(!user.getFlightIDs().contains(id))
            user.getFlightIDs().add(id);
        user.setMiles(user.getMiles() + Double.parseDouble(milesValueLabel.getText()));

        FileWriter fw3 = new FileWriter("src/txt_files/costumers.txt", false);
        fw3.append("");
        fw3.close();

        FileWriter fw4 = new FileWriter("src/txt_files/costumers.txt", true);
        for (int j = 0; j < costumers.size(); j++) 
        {
            if(user.getCPF().equals(costumers.get(j).getCPF()))
            {
                costumers.set(j, user);
                fw4.append(user.toTxt() + "\n");                
            }
            else
            {
                fw4.append(costumers.get(j).toTxt() + "\n");
            } 
        }
        fw4.close();

        FileWriter fw5 = new FileWriter("src/txt_files/costumersflightsstatus.txt", true);
        fw5.append(user.getCPF() + ";" + id + ";" + "false" + "\n");
        fw5.close();

        warningAlert("Completed Purchase", "You have successfully bought passages!", "Check 'My Travels' to see your flight or 'Check-in' to fly!");

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
    void milesPayment(ActionEvent event) throws IOException
    {
        if(seat1TextField.getText().equals("") || cpf1TextField.getText().equals(""))
        {
            errorAlert("Passenger Error", "Name or CPF are blank!", "You must provide a name and a CPF for every seat!");
            return;
        }
        if(seat2TextField.editableProperty().getValue() && (seat2TextField.getText().equals("") || cpf2TextField.getText().equals("")))
        {
            errorAlert("Passenger Error", "Name or CPF are blank!", "You must provide a name and a CPF for every seat!");
            return;
        }
        if(seat3TextField.editableProperty().getValue() && (seat3TextField.getText().equals("") || cpf3TextField.getText().equals("")))
        {
            errorAlert("Passenger Error", "Name or CPF are blank!", "You must provide a name and a CPF for every seat!");
            return;
        }
        if(user.getMiles() < Double.parseDouble(totalPriceText.getText()))
        {
            errorAlert("Miles Error", "You don't have enough miles to pay!", "Buy using your card to gain miles!");
            return;
        }

        user.setMiles(user.getMiles()-Double.parseDouble(totalPriceText.getText()));

        FileWriter fw = new FileWriter("src/txt_files/flights.txt", false);
        fw.append("");
        fw.close();

        int i = 0;
        for (Seat seat : flight.getSeats()) 
        {
            if(seat.getisSelected() && i == 0)
            {
                seat.setisSelected(false);
                String cpf_costumer[] = {cpf1TextField.getText(), seat1TextField.getText()};
                seat.setCpf(cpf_costumer[0]);
                seat.setName(cpf_costumer[1]);
                seat.setisEmpty(false);
                i++;
            }
            else if(seat.getisSelected() && i == 1)
            {
                seat.setisSelected(false);
                String cpf_costumer[] = {cpf2TextField.getText(), seat2TextField.getText()};
                seat.setCostumer(cpf_costumer);
                seat.setisEmpty(false);
                i++;
            }
            else if(seat.getisSelected() && i == 2)
            {
                seat.setisSelected(false);
                String cpf_costumer[] = {cpf3TextField.getText(), seat3TextField.getText()};
                seat.setCostumer(cpf_costumer);
                seat.setisEmpty(false);
            }
        }

        FileWriter fw2 = new FileWriter("src/txt_files/flights.txt", true);
        for (int j = 0; j < flights.size(); j++) 
        {
            if(flight.getID().equals(flights.get(j).getID()))
            {
                flights.set(j, flight);
                fw2.append(flight.toTxt() + "\n");                
            }
            else
            {
                fw2.append(flights.get(j).toTxt() + "\n");
            } 
        }
        fw2.close();

        if(!user.getFlightIDs().contains(id))
            user.getFlightIDs().add(id);
        user.setMiles(user.getMiles() + Double.parseDouble(milesValueLabel.getText()));

        FileWriter fw3 = new FileWriter("src/txt_files/costumers.txt", false);
        fw3.append("");
        fw3.close();

        FileWriter fw4 = new FileWriter("src/txt_files/costumers.txt", true);
        for (int j = 0; j < costumers.size(); j++) 
        {
            if(user.getCPF().equals(costumers.get(j).getCPF()))
            {
                costumers.set(j, user);
                fw4.append(user.toTxt() + "\n");                
            }
            else
            {
                fw4.append(costumers.get(j).toTxt() + "\n");
            } 
        }
        fw4.close();

        FileWriter fw5 = new FileWriter("src/txt_files/costumersflightsstatus.txt", true);
        fw5.append(user.getCPF() + ";" + id + ";" + "false" + "\n");
        fw5.close();

        warningAlert("Completed Purchase", "You have successfully bought passages!", "Check 'My Travels' to see your flight or 'Check-in' to fly!");

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
    void cancelPayment(ActionEvent event) throws IOException
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

    private void warningAlert(String title, String header, String content)
    {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);

        Image image = new Image("imagens/ok icon2.png");
        ImageView imageView = new ImageView(image);
        alert.setGraphic(imageView);

		alert.showAndWait();
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
