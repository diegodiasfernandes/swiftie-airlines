package Controllers;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Classes.Costumer;
import Classes.Flight;
import Classes.Seat;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BuyTicketsController 
{
    @FXML
    private AnchorPane aviao, root;
    @FXML
    private Button button1, button11, button111, button111111, button1111121, selectButton, payButton,
                    cancelButton, addLuggageButton, addCarButton, addAccommodationButton;
    @FXML
    private VBox home_side_menu;  
    @FXML
    private Label milesValueLabel;
    @FXML
    private ComboBox<String> luggageAmountCombobox, luggageTypeCombobox, carModelsCombobox, roomTypeCombobox,
    hotelsCombobox, roomForCombobox;
    @FXML
    private TextField numberOfDaysTextField, accomodationDaysTextField;

    private ArrayList<String> amountList = new ArrayList<>(){{add("1"); add("2"); add("3"); add("4"); add("5");}};
    private ObservableList<String> luggageAmount = FXCollections.observableArrayList(amountList);
    private ArrayList<String> lugaggeTypesList = new ArrayList<>(){{add("Personal"); add("Hand"); add("Dispatched");}};
    private ObservableList<String> luggageTypes = FXCollections.observableArrayList(lugaggeTypesList);
    private ArrayList<String> carTypesList = new ArrayList<>(){{add("Honda Fit"); add("Prisma"); add("Renegade");}};
    private ObservableList<String> carTypes = FXCollections.observableArrayList(carTypesList);
    private ArrayList<String> roomTypeList = new ArrayList<>(){{add("Regular"); add("Presidential"); add("Balcony");}};
    private ObservableList<String> roomTypes = FXCollections.observableArrayList(roomTypeList);
    private ArrayList<String> hotelsList = new ArrayList<>(){{add("Ibis"); add("B&B");}};
    private ObservableList<String> hotels = FXCollections.observableArrayList(hotelsList);

    @FXML
    private Text seatSelected1Text, seatSelected2Text, seatSelected3Text, seatType1Text, ticketsPriceText,
                 seatType2Text, seatType3Text, totalMoneyText, vipValueText, emergencyValueText, regularValueText,
                 ticketsNumberText, lugaggeNumberText, accomodationNumberText, carsNumberText, carsPriceText,
                 accomodationPriceText, luggagePriceText, originStateText, destinationStateText, originAirportText,
                 destinationAirportText;  
    @FXML
    private Button a1Button, a2Button, a3Button, a4Button, a5Button, a6Button, 
            b1Button, b2Button, b3Button, b4Button, b5Button, b6Button, 
            c1Button, c2Button, c3Button, c4Button, c5Button, c6Button, 
            d1Button, d2Button, d3Button, d4Button, d5Button, d6Button, 
            e1Button, e2Button, e3Button, e4Button, e5Button, e6Button, 
            f1Button, f2Button, f3Button, f4Button, f5Button, f6Button, 
            g1Button, g2Button, g3Button, g4Button, g5Button, g6Button, 
            h1Button, h2Button, h3Button, h4Button, h5Button, h6Button, 
            i1Button, i2Button, i3Button, i4Button, i5Button, i6Button; 

    private Flight flight;
    private Costumer user;
    private String id;
    
    private int selectedSeats = 0;
    private String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};

    public void _initialize(Costumer user , String id) throws IOException
    {
        this.user = user;
        this.id = id;
        readSelectedFlightFile(id);
        setBoughtTickets();
        vipValueText.setText(flight.getVipPrice() + "");
        emergencyValueText.setText(flight.getEmergencyPrice() + "");
        regularValueText.setText(flight.getRegularPrice() + "");
        luggageAmountCombobox.setItems(luggageAmount);
        luggageTypeCombobox.setItems(luggageTypes);
        carModelsCombobox.setItems(carTypes);
        roomForCombobox.setItems(luggageAmount);
        roomTypeCombobox.setItems(roomTypes);
        hotelsCombobox.setItems(hotels);
    }

    private void readSelectedFlightFile(String id) throws IOException
    { 
        BufferedReader bf = new BufferedReader(new FileReader("src/txt_files/flights.txt"));
        String line;

        while((line = bf.readLine()) != null)
        {
            String[] dados = line.split(";");

            if(dados[0].equals(id))
            {
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

                flight = (new Flight(dados[0], dados[1], dados[2], dados[3], dados[4], dados[5], dados[6], 
                                    dados[7], dados[8], dados[9], Double.parseDouble(dados[10]), 
                                    Double.parseDouble(dados[11]), Double.parseDouble(dados[12]), 
                                    Double.parseDouble(dados[13]), seatsStatus, cpf_name));                
            }
        }

        bf.close();

        originStateText.setText(flight.getOriginState());
        destinationStateText.setText(flight.getDestinationState());
        String airport_state[] = flight.getOriginAirportFull().split(",");
        originAirportText.setText(airport_state[0]);
        String airport_state2[] = flight.getDestinationAirportFull().split(",");
        destinationAirportText.setText(airport_state2[0]);
    }

    private void setBoughtTickets()
    {
        String notEmptyColor = "-fx-background-color:#808080; -fx-border-vidth: 2px 2px 2px 2px; -fx-border-radius: 50px; -fx-background-radius: 50px; -fx-border-style:solid; -fx-border-color:#ffffff;";
        int i = 0;
        for (Seat seat : flight.getSeats()) 
        {
            if(!seat.getisEmpty() && i == 0)
                a1Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 1)
                a2Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 2)
                a3Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 3)
                a4Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 4)
                a5Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 5)
                a6Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 6)
                b1Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 7)
                b2Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 8)
                b3Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 9)
                b4Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 10)
                b5Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 11)
                b6Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 12)
                c1Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 13)
                c2Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 14)
                c3Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 15)
                c4Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 16)
                c5Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 17)
                c6Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 18)
                d1Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 19)
                d2Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 20)
                d3Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 21)
                d4Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 22)
                d5Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 23)
                d6Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 24)
                e1Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 25)
                e2Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 26)
                e3Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 27)
                e4Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 28)
                e5Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 29)
                e6Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 30)
                f1Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 31)
                f2Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 32)
                f3Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 33)
                f4Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 34)
                f5Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 35)
                f6Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 36)
                g1Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 37)
                g2Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 38)
                g3Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 39)
                g4Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 40)
                g5Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 41)
                g6Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 42)
                h1Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 43)
                h2Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 44)
                h3Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 45)
                h4Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 46)
                h5Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 47)
                h6Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 48)
                i1Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 49)
                i2Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 50)
                i3Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 51)
                i4Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 52)
                i5Button.setStyle(notEmptyColor);
            if(!seat.getisEmpty() && i == 53)
                i6Button.setStyle(notEmptyColor);

            i++;
        }
    }

    @FXML
    void selectSeats(ActionEvent event)
    {
        seatSelected1Text.setText("");
        seatSelected2Text.setText("");
        seatSelected3Text.setText("");
        seatType1Text.setText("");
        seatType2Text.setText("");
        seatType3Text.setText("");
        
        double seatsPrice = 0;
        int i = 1;
        int j = 0;
        int nextText = 0;
        for (Seat seat : flight.getSeats()) 
        {
            if(seat.getisSelected())
            {
                if(nextText == 0)
                {
                    seatSelected1Text.setText(letters[j] + i);
                    seatType1Text.setText(seat.getSeatType());
                }
                else if(nextText == 1)
                {
                    seatSelected2Text.setText(letters[j] + i);
                    seatType2Text.setText(seat.getSeatType());
                }
                else if(nextText == 2)
                {
                    seatSelected3Text.setText(letters[j] + i);
                    seatType3Text.setText(seat.getSeatType());
                }
                seatsPrice += seat.getPrice();
                nextText++;
            }
            i++;
            if(i == 7)
            {
                i = 1;
                j++;
            }
        }
        String money = seatsPrice + "";
        totalMoneyText.setText((Double.parseDouble(totalMoneyText.getText())+seatsPrice-Double.parseDouble(ticketsPriceText.getText())) + "");
        ticketsPriceText.setText(money);
        ticketsNumberText.setText(selectedSeats + "");
        double miles = seatsPrice*(flight.getMilesPercentage());
        milesValueLabel.setText(Math.round(miles) + "");
    }

    @FXML
    void erase(ActionEvent event)
    {
        lugaggeNumberText.setText("0");
        luggagePriceText.setText("0.0");
        carsNumberText.setText("0");
        carsPriceText.setText("0.0");
        accomodationNumberText.setText("0");
        accomodationPriceText.setText("0.0");
        totalMoneyText.setText(ticketsPriceText.getText());
    }

    @FXML
    boolean calculateLuggagePrice(ActionEvent event) 
    {
        if(luggageAmountCombobox.getSelectionModel().getSelectedItem() == null || luggageTypeCombobox.getSelectionModel().getSelectedItem() == null)
            return false;

        double luggagePrice = 0;
        if(luggageTypeCombobox.getSelectionModel().getSelectedItem().toString().equals("Personal"))
            luggagePrice = Double.parseDouble(luggageAmountCombobox.getSelectionModel().getSelectedItem()) * 200;
        else if(luggageTypeCombobox.getSelectionModel().getSelectedItem().toString().equals("Hand"))
            luggagePrice = Double.parseDouble(luggageAmountCombobox.getSelectionModel().getSelectedItem()) * 300;
        else
            luggagePrice = Double.parseDouble(luggageAmountCombobox.getSelectionModel().getSelectedItem()) * 500;
        

        lugaggeNumberText.setText(luggageAmountCombobox.getSelectionModel().getSelectedItem());
        totalMoneyText.setText((Double.parseDouble(totalMoneyText.getText())+luggagePrice-Double.parseDouble(luggagePriceText.getText())) + "");            
        luggagePriceText.setText(luggagePrice + "");
        
        return true;
    }

    @FXML
    boolean calculateCarsPrice(ActionEvent event) 
    {
        int numberOfDays = 0;
        try {
            numberOfDays = Integer.parseInt(numberOfDaysTextField.getText());
        } catch (Exception e) {
            return false;
        }

        if(carModelsCombobox.getSelectionModel().getSelectedItem() == null)
            return false;

        double carsPrice = 0;
        if(carModelsCombobox.getSelectionModel().getSelectedItem().toString().equals("Honda Fit"))
            carsPrice = numberOfDays * 100;
        else if(carModelsCombobox.getSelectionModel().getSelectedItem().toString().equals("Prisma"))
            carsPrice = numberOfDays * 150;
        else
            carsPrice = numberOfDays * 200;
        
        carsNumberText.setText(luggageAmountCombobox.getSelectionModel().getSelectedItem());
        totalMoneyText.setText((Double.parseDouble(totalMoneyText.getText())+carsPrice-Double.parseDouble(carsPriceText.getText())) + "");
        carsPriceText.setText(carsPrice + "");
        return true;
    }

    @FXML
    boolean calculateHotelPrice(ActionEvent event)
    {
        int numberOfDays = 0;
        try {
            numberOfDays = Integer.parseInt(accomodationDaysTextField.getText());
        } catch (Exception e) {
            return false;
        }

        if(hotelsCombobox.getSelectionModel().getSelectedItem() == null || roomTypeCombobox.getSelectionModel().getSelectedItem() == null || roomForCombobox.getSelectionModel().getSelectedItem() == null)
            return false;

        double hotelPrice = 0;

        if(hotelsCombobox.getSelectionModel().getSelectedItem().equals("Ibis"))
            hotelPrice = 10;
        else
            hotelPrice = 15;
        
        if(roomTypeCombobox.getSelectionModel().getSelectedItem().equals("Regular"))
            hotelPrice = hotelPrice * 20;
        else if(roomTypeCombobox.getSelectionModel().getSelectedItem().equals("Balcony"))
            hotelPrice = hotelPrice * 26;
        else
            hotelPrice = hotelPrice * 60;
        
        accomodationNumberText.setText(roomForCombobox.getSelectionModel().getSelectedItem());
        totalMoneyText.setText((Double.parseDouble(totalMoneyText.getText())+(Integer.parseInt(roomForCombobox.getSelectionModel().getSelectedItem()) * numberOfDays * hotelPrice)-Double.parseDouble(accomodationPriceText.getText())) + "");
        accomodationPriceText.setText((Integer.parseInt(roomForCombobox.getSelectionModel().getSelectedItem()) * numberOfDays * hotelPrice) + "");
        return false;
    }

    @FXML
    void finalPaymentLoader(ActionEvent event) throws IOException 
    {
        if(seatSelected1Text.getText().equals(""))
        {
            errorAlert("No Seats Error", "No Seats Selected", "You must select some seats before");
            return;
        }    

        FXMLLoader loader = new FXMLLoader((getClass().getResource("../Screens/finalpayment.fxml")));
        Parent root = loader.load();

        String css = this.getClass().getResource("../StyleSheets/style.css").toExternalForm();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(css); 

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        FinalPaymentController controller = loader.getController();
        controller._initialize(user, id, flight, totalMoneyText.getText(), milesValueLabel.getText(),
                                seatSelected1Text.getText(), seatSelected2Text.getText(), seatSelected3Text.getText(),
                                seatType1Text.getText(), seatType2Text.getText(), seatType3Text.getText());

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

    private String selectingColor = "-fx-background-color:#FFEB5B; -fx-border-vidth: 2px 2px 2px 2px; -fx-border-radius: 50px; -fx-background-radius: 50px; -fx-border-style:solid; -fx-border-color:#ffffff;";

    @FXML
    boolean buyA1(ActionEvent event) 
    {
        if(!flight.getSeats().get(0).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(0).getisSelected())
        {
            flight.getSeats().get(0).setisSelected(false);
            selectedSeats--;
            a1Button.setStyle(flight.getSeats().get(0).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(0).setisSelected(true);
        a1Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyA2(ActionEvent event) 
    {
        if(!flight.getSeats().get(1).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(1).getisSelected())
        {
            flight.getSeats().get(1).setisSelected(false);
            selectedSeats--;
            a2Button.setStyle(flight.getSeats().get(1).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(1).setisSelected(true);
        a2Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyA3(ActionEvent event) 
    {
        if(!flight.getSeats().get(2).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(2).getisSelected())
        {
            flight.getSeats().get(2).setisSelected(false);
            selectedSeats--;
            a3Button.setStyle(flight.getSeats().get(2).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(2).setisSelected(true);
        a3Button.setStyle(selectingColor);

        return true;
    }
    
    @FXML
    boolean buyA4(ActionEvent event) 
    {
        if(!flight.getSeats().get(3).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(3).getisSelected())
        {
            flight.getSeats().get(3).setisSelected(false);
            selectedSeats--;
            a4Button.setStyle(flight.getSeats().get(3).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(3).setisSelected(true);
        a4Button.setStyle(selectingColor);

        return true;
    
    }

    @FXML
    boolean buyA5(ActionEvent event) 
    {
        if(!flight.getSeats().get(4).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(4).getisSelected())
        {
            flight.getSeats().get(4).setisSelected(false);
            selectedSeats--;
            a5Button.setStyle(flight.getSeats().get(4).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(4).setisSelected(true);
        a5Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyA6(ActionEvent event) 
    {
        if(!flight.getSeats().get(5).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(5).getisSelected())
        {
            flight.getSeats().get(5).setisSelected(false);
            selectedSeats--;
            a6Button.setStyle(flight.getSeats().get(5).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(5).setisSelected(true);
        a6Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyB1(ActionEvent event) 
    {
        if(!flight.getSeats().get(6).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(6).getisSelected())
        {
            flight.getSeats().get(6).setisSelected(false);
            selectedSeats--;
            b1Button.setStyle(flight.getSeats().get(6).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(6).setisSelected(true);
        b1Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyB2(ActionEvent event) 
    {
        if(!flight.getSeats().get(7).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(7).getisSelected())
        {
            flight.getSeats().get(7).setisSelected(false);
            selectedSeats--;
            b2Button.setStyle(flight.getSeats().get(7).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(7).setisSelected(true);
        b2Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyB3(ActionEvent event) 
    {
        if(!flight.getSeats().get(8).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(8).getisSelected())
        {
            flight.getSeats().get(8).setisSelected(false);
            selectedSeats--;
            b3Button.setStyle(flight.getSeats().get(8).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(8).setisSelected(true);
        b3Button.setStyle(selectingColor);

        return true;  
    }

    @FXML
    boolean buyB4(ActionEvent event) 
    {
        if(!flight.getSeats().get(9).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(9).getisSelected())
        {
            flight.getSeats().get(9).setisSelected(false);
            selectedSeats--;
            b4Button.setStyle(flight.getSeats().get(9).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(9).setisSelected(true);
        b4Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyB5(ActionEvent event) 
    {
        if(!flight.getSeats().get(10).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(10).getisSelected())
        {
            flight.getSeats().get(10).setisSelected(false);
            selectedSeats--;
            b5Button.setStyle(flight.getSeats().get(10).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(10).setisSelected(true);
        b5Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyB6(ActionEvent event) 
    {   
        if(!flight.getSeats().get(11).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(11).getisSelected())
        {
            flight.getSeats().get(11).setisSelected(false);
            selectedSeats--;
            b6Button.setStyle(flight.getSeats().get(11).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(11).setisSelected(true);
        b6Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyC1(ActionEvent event) 
    {
        if(!flight.getSeats().get(12).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(12).getisSelected())
        {
            flight.getSeats().get(12).setisSelected(false);
            selectedSeats--;
            c1Button.setStyle(flight.getSeats().get(12).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(12).setisSelected(true);
        c1Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyC2(ActionEvent event) 
    {
        if(!flight.getSeats().get(13).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(13).getisSelected())
        {
            flight.getSeats().get(13).setisSelected(false);
            selectedSeats--;
            c2Button.setStyle(flight.getSeats().get(13).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(13).setisSelected(true);
        c2Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyC3(ActionEvent event) 
    {
        if(!flight.getSeats().get(14).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(14).getisSelected())
        {
            flight.getSeats().get(14).setisSelected(false);
            selectedSeats--;
            c3Button.setStyle(flight.getSeats().get(14).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(14).setisSelected(true);
        c3Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyC4(ActionEvent event) 
    {
        if(!flight.getSeats().get(15).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(15).getisSelected())
        {
            flight.getSeats().get(15).setisSelected(false);
            selectedSeats--;
            c4Button.setStyle(flight.getSeats().get(15).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(15).setisSelected(true);
        c4Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyC5(ActionEvent event) 
    {
        if(!flight.getSeats().get(16).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(16).getisSelected())
        {
            flight.getSeats().get(16).setisSelected(false);
            selectedSeats--;
            c5Button.setStyle(flight.getSeats().get(16).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(16).setisSelected(true);
        c5Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyC6(ActionEvent event) 
    {
        if(!flight.getSeats().get(17).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(17).getisSelected())
        {
            flight.getSeats().get(17).setisSelected(false);
            selectedSeats--;
            c6Button.setStyle(flight.getSeats().get(17).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(17).setisSelected(true);
        c6Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyD1(ActionEvent event) 
    {
        if(!flight.getSeats().get(18).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(18).getisSelected())
        {
            flight.getSeats().get(18).setisSelected(false);
            selectedSeats--;
            d1Button.setStyle(flight.getSeats().get(18).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(18).setisSelected(true);
        d1Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyD2(ActionEvent event) 
    {
        if(!flight.getSeats().get(19).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(19).getisSelected())
        {
            flight.getSeats().get(19).setisSelected(false);
            selectedSeats--;
            d2Button.setStyle(flight.getSeats().get(19).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(19).setisSelected(true);
        d2Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyD3(ActionEvent event) 
    {
        if(!flight.getSeats().get(20).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(20).getisSelected())
        {
            flight.getSeats().get(20).setisSelected(false);
            selectedSeats--;
            d3Button.setStyle(flight.getSeats().get(20).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(20).setisSelected(true);
        d3Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyD4(ActionEvent event) 
    {
        if(!flight.getSeats().get(21).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(21).getisSelected())
        {
            flight.getSeats().get(21).setisSelected(false);
            selectedSeats--;
            d4Button.setStyle(flight.getSeats().get(21).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(21).setisSelected(true);
        d4Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyD5(ActionEvent event) 
    {
        if(!flight.getSeats().get(22).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(22).getisSelected())
        {
            flight.getSeats().get(22).setisSelected(false);
            selectedSeats--;
            d5Button.setStyle(flight.getSeats().get(22).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(22).setisSelected(true);
        d5Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyD6(ActionEvent event) 
    {
        if(!flight.getSeats().get(23).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(23).getisSelected())
        {
            flight.getSeats().get(23).setisSelected(false);
            selectedSeats--;
            d6Button.setStyle(flight.getSeats().get(23).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(23).setisSelected(true);
        d6Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyE1(ActionEvent event) 
    {
        if(!flight.getSeats().get(24).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(24).getisSelected())
        {
            flight.getSeats().get(24).setisSelected(false);
            selectedSeats--;
            e1Button.setStyle(flight.getSeats().get(24).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(24).setisSelected(true);
        e1Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyE2(ActionEvent event) 
    {
        if(!flight.getSeats().get(25).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(25).getisSelected())
        {
            flight.getSeats().get(25).setisSelected(false);
            selectedSeats--;
            e2Button.setStyle(flight.getSeats().get(25).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(25).setisSelected(true);
        e2Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyE3(ActionEvent event) 
    {
        if(!flight.getSeats().get(26).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(26).getisSelected())
        {
            flight.getSeats().get(26).setisSelected(false);
            selectedSeats--;
            e3Button.setStyle(flight.getSeats().get(26).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(26).setisSelected(true);
        e3Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyE4(ActionEvent event) 
    {
        if(!flight.getSeats().get(27).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(27).getisSelected())
        {
            flight.getSeats().get(27).setisSelected(false);
            selectedSeats--;
            e4Button.setStyle(flight.getSeats().get(27).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(27).setisSelected(true);
        e4Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyE5(ActionEvent event) 
    {
        if(!flight.getSeats().get(28).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(28).getisSelected())
        {
            flight.getSeats().get(28).setisSelected(false);
            selectedSeats--;
            e5Button.setStyle(flight.getSeats().get(28).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(28).setisSelected(true);
        e5Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyE6(ActionEvent event) 
    {
        if(!flight.getSeats().get(29).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(29).getisSelected())
        {
            flight.getSeats().get(29).setisSelected(false);
            selectedSeats--;
            e6Button.setStyle(flight.getSeats().get(29).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(29).setisSelected(true);
        e6Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyF1(ActionEvent event) 
    {
        if(!flight.getSeats().get(30).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(30).getisSelected())
        {
            flight.getSeats().get(30).setisSelected(false);
            selectedSeats--;
            f1Button.setStyle(flight.getSeats().get(30).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(30).setisSelected(true);
        f1Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyF2(ActionEvent event) 
    {
        if(!flight.getSeats().get(31).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(31).getisSelected())
        {
            flight.getSeats().get(31).setisSelected(false);
            selectedSeats--;
            f2Button.setStyle(flight.getSeats().get(31).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(31).setisSelected(true);
        f2Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyF3(ActionEvent event) 
    {
        if(!flight.getSeats().get(32).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(32).getisSelected())
        {
            flight.getSeats().get(32).setisSelected(false);
            selectedSeats--;
            f3Button.setStyle(flight.getSeats().get(32).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(32).setisSelected(true);
        f3Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyF4(ActionEvent event) 
    {
        if(!flight.getSeats().get(33).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(33).getisSelected())
        {
            flight.getSeats().get(33).setisSelected(false);
            selectedSeats--;
            f4Button.setStyle(flight.getSeats().get(33).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(33).setisSelected(true);
        f4Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyF5(ActionEvent event) 
    {
        if(!flight.getSeats().get(34).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(34).getisSelected())
        {
            flight.getSeats().get(34).setisSelected(false);
            selectedSeats--;
            f5Button.setStyle(flight.getSeats().get(34).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(34).setisSelected(true);
        f5Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyF6(ActionEvent event) 
    {
        if(!flight.getSeats().get(35).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(35).getisSelected())
        {
            flight.getSeats().get(35).setisSelected(false);
            selectedSeats--;
            f6Button.setStyle(flight.getSeats().get(35).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(35).setisSelected(true);
        f6Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyG1(ActionEvent event) 
    {
        System.out.println(flight.getSeats().get(36).getSeatType());
        if(!flight.getSeats().get(36).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(36).getisSelected())
        {
            flight.getSeats().get(36).setisSelected(false);
            selectedSeats--;
            g1Button.setStyle(flight.getSeats().get(36).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(36).setisSelected(true);
        g1Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyG2(ActionEvent event) 
    {
        if(!flight.getSeats().get(37).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(37).getisSelected())
        {
            flight.getSeats().get(37).setisSelected(false);
            selectedSeats--;
            g2Button.setStyle(flight.getSeats().get(37).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(37).setisSelected(true);
        g2Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyG3(ActionEvent event) 
    {
        if(!flight.getSeats().get(38).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(38).getisSelected())
        {
            flight.getSeats().get(38).setisSelected(false);
            selectedSeats--;
            g3Button.setStyle(flight.getSeats().get(38).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(38).setisSelected(true);
        g3Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyG4(ActionEvent event) 
    {
        if(!flight.getSeats().get(39).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(39).getisSelected())
        {
            flight.getSeats().get(39).setisSelected(false);
            selectedSeats--;
            g4Button.setStyle(flight.getSeats().get(39).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(39).setisSelected(true);
        g4Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyG5(ActionEvent event) 
    {
        if(!flight.getSeats().get(40).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(40).getisSelected())
        {
            flight.getSeats().get(40).setisSelected(false);
            selectedSeats--;
            g5Button.setStyle(flight.getSeats().get(40).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(40).setisSelected(true);
        g5Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyG6(ActionEvent event) 
    {
        if(!flight.getSeats().get(41).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(41).getisSelected())
        {
            flight.getSeats().get(41).setisSelected(false);
            selectedSeats--;
            g6Button.setStyle(flight.getSeats().get(41).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(41).setisSelected(true);
        g6Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyH1(ActionEvent event) 
    {
        if(!flight.getSeats().get(42).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(42).getisSelected())
        {
            flight.getSeats().get(42).setisSelected(false);
            selectedSeats--;
            h1Button.setStyle(flight.getSeats().get(42).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(42).setisSelected(true);
        h1Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyH2(ActionEvent event) 
    {
        if(!flight.getSeats().get(43).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(43).getisSelected())
        {
            flight.getSeats().get(43).setisSelected(false);
            selectedSeats--;
            h2Button.setStyle(flight.getSeats().get(43).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(43).setisSelected(true);
        h2Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyH3(ActionEvent event) 
    {
        if(!flight.getSeats().get(44).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(44).getisSelected())
        {
            flight.getSeats().get(44).setisSelected(false);
            selectedSeats--;
            h3Button.setStyle(flight.getSeats().get(44).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(44).setisSelected(true);
        h3Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyH4(ActionEvent event) 
    {
        if(!flight.getSeats().get(45).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(45).getisSelected())
        {
            flight.getSeats().get(45).setisSelected(false);
            selectedSeats--;
            h4Button.setStyle(flight.getSeats().get(45).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(45).setisSelected(true);
        h4Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyH5(ActionEvent event) 
    {
        if(!flight.getSeats().get(46).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(46).getisSelected())
        {
            flight.getSeats().get(46).setisSelected(false);
            selectedSeats--;
            h5Button.setStyle(flight.getSeats().get(46).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(46).setisSelected(true);
        h5Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyH6(ActionEvent event) 
    {
        if(!flight.getSeats().get(47).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(47).getisSelected())
        {
            flight.getSeats().get(47).setisSelected(false);
            selectedSeats--;
            h6Button.setStyle(flight.getSeats().get(47).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(47).setisSelected(true);
        h6Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyI1(ActionEvent event) 
    {
        if(!flight.getSeats().get(48).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(48).getisSelected())
        {
            flight.getSeats().get(48).setisSelected(false);
            selectedSeats--;
            i1Button.setStyle(flight.getSeats().get(48).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(48).setisSelected(true);
        i1Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyI2(ActionEvent event) 
    {
        if(!flight.getSeats().get(49).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(49).getisSelected())
        {
            flight.getSeats().get(49).setisSelected(false);
            selectedSeats--;
            i2Button.setStyle(flight.getSeats().get(48).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(49).setisSelected(true);
        i2Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyI3(ActionEvent event) 
    {
        if(!flight.getSeats().get(50).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(50).getisSelected())
        {
            flight.getSeats().get(50).setisSelected(false);
            selectedSeats--;
            i3Button.setStyle(flight.getSeats().get(48).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(50).setisSelected(true);
        i3Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyI4(ActionEvent event) 
    {
        if(!flight.getSeats().get(51).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(51).getisSelected())
        {
            flight.getSeats().get(51).setisSelected(false);
            selectedSeats--;
            i4Button.setStyle(flight.getSeats().get(48).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(51).setisSelected(true);
        i4Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyI5(ActionEvent event) 
    {
        if(!flight.getSeats().get(52).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(52).getisSelected())
        {
            flight.getSeats().get(52).setisSelected(false);
            selectedSeats--;
            i5Button.setStyle(flight.getSeats().get(48).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(52).setisSelected(true);
        i5Button.setStyle(selectingColor);

        return true;
    }

    @FXML
    boolean buyI6(ActionEvent event) 
    {
        if(!flight.getSeats().get(53).getisEmpty())
        {
            errorAlert("Buy Error", "This seat has been already bought", "Choose another seat!");
            return false;
        }
        if(flight.getSeats().get(53).getisSelected())
        {
            flight.getSeats().get(53).setisSelected(false);
            selectedSeats--;
            i6Button.setStyle(flight.getSeats().get(48).getRegularColor());
            return false;
        }
        if(selectedSeats == 3)
        {
            errorAlert("Too Many Tickets", "You can only buy 3 tickets on the same flight", "To choose this seat you must remove one selected seat!");
            return false;
        }

        selectedSeats++;
        flight.getSeats().get(53).setisSelected(true);
        i6Button.setStyle(selectingColor);

        return true;
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
