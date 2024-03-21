package Classes;
//import java.util.ArrayList;

import java.util.ArrayList;

public class Flight 
{
    private String ID;
    private String model;
    private String originAirport;
    private String originState;
    private String destinationAirport;
    private String destinationState;
    private String departureDate;
    private String departureTime;
    private String arrivalDate;
    private String arrivalTime;
    private double regularPrice;
    private double emergencyPrice;
    private double vipPrice;
    private double milesPercentage;
    private String capacity;
    public ArrayList<String> CPF_name;
    private ArrayList<Seat> seats = new ArrayList<Seat>();

    public Flight(String ID, String model, String originAirport, String originState, 
                  String destinationAirport, String destinationState, String departureDate, 
                  String departureTime, String arrivalDate, String arrivalTime, double regularPrice, 
                  double emergencyPrice, double vipPrice, double milesPercentage){
        this.ID = ID;
        this.model = model;
        this.originAirport = originAirport;
        this.destinationAirport = destinationAirport;
        this.originState = originState;
        this.destinationState = destinationState;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
        this.regularPrice = regularPrice;
        this.emergencyPrice = emergencyPrice;
        this.vipPrice = vipPrice;
        this.milesPercentage = milesPercentage;
        createSeats();
        this.CPF_name = cpfsAndNames();
    }

    public Flight(String ID, String model, String originAirport, String originState, 
                  String destinationAirport, String destinationState, String departureDate, 
                  String departureTime, String arrivalDate, String arrivalTime, double regularPrice, 
                  double emergencyPrice, double vipPrice, double milesPercentage, ArrayList<Boolean> seatsStatus,
                  ArrayList<String> CPF_name)
    {
        this.ID = ID;
        this.model = model;
        this.originAirport = originAirport;
        this.destinationAirport = destinationAirport;
        this.originState = originState;
        this.destinationState = destinationState;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
        this.regularPrice = regularPrice;
        this.emergencyPrice = emergencyPrice;
        this.vipPrice = vipPrice;
        this.milesPercentage = milesPercentage;
        this.CPF_name = CPF_name;
        setSeats();
        this.capacity = calculateCapacity(seatsStatus);
    }

    private ArrayList<String> cpfsAndNames() {
        String costumer;
        ArrayList<String> cpf_name = new ArrayList<String>();
        for (Seat seat : seats) 
        {
            costumer = seat.getCpf() + "," + seat.getName();
            cpf_name.add(costumer);
        }

        return cpf_name;
    }

    private String calculateCapacity(ArrayList<Boolean> seatsStatus){
        int i = 0;
        int passengers = 0;
        for (Seat seat : seats) 
        {
            seat.setisEmpty(seatsStatus.get(i));
            if(!seatsStatus.get(i))
                passengers++;
            i++;
        }
        return passengers + "/" + i;
    }

    private void createSeats()
    {
        if(model.equals("ATR 72-600 (54)"))
            createAtrSeats();
        if(model.equals("Embraer 195 (54)"))
            createEmbraerSeats();
    }

    private void createAtrSeats()
    {
        for (int i = 0; i < 18; i++) // 0 - 17
        {
            seats.add(new Seat("Vip", vipPrice));
        }
        for (int i = 0; i < 12; i++) // 18 - 29
        {
            seats.add(new Seat("Emergency", emergencyPrice));
        }
        for (int i = 0; i < 24; i++) // 30 = 53
        {
            seats.add(new Seat("Regular", regularPrice));
        }
    }

    private void createEmbraerSeats()
    {
        for (int i = 0; i < 18; i++) 
        {
            seats.add(new Seat("Vip", vipPrice));
        }
        for (int i = 0; i < 12; i++) 
        {
            seats.add(new Seat("Emergency", emergencyPrice));
        }
        for (int i = 0; i < 24; i++) 
        {
            seats.add(new Seat("Regular", regularPrice));
        }
    }   

    private void setSeats()
    {
        if(model.equals("ATR 72-600 (54)"))
            setAtrSeats();
        if(model.equals("Embraer 195 (54)"))
            setEmbraerSeats();
    }

    private void setAtrSeats()
    {
        int j = 0;
        for (int i = 0; i < 18; i++) // 0 - 17
        {
            String cpf_name[] = CPF_name.get(j).split(",");
            seats.add(new Seat("Vip", vipPrice, cpf_name[0], cpf_name[1]));
            j++;
        }
        for (int i = 0; i < 12; i++) // 18 - 29
        {
            String cpf_name[] = CPF_name.get(j).split(",");
            seats.add(new Seat("Emergency", emergencyPrice, cpf_name[0], cpf_name[1]));
            j++;
        }
        for (int i = 0; i < 24; i++) // 30 = 53
        {
            String cpf_name[] = CPF_name.get(j).split(",");
            seats.add(new Seat("Regular", regularPrice, cpf_name[0], cpf_name[1]));
            j++;
        }
    }

    private void setEmbraerSeats()
    {
        int j = 0;
        for (int i = 0; i < 18; i++) // 0 - 17
        {
            String cpf_name[] = CPF_name.get(j).split(",");
            seats.add(new Seat("Vip", vipPrice, cpf_name[0], cpf_name[1]));
            j++;
        }
        for (int i = 0; i < 12; i++) // 18 - 29
        {
            String cpf_name[] = CPF_name.get(j).split(",");
            seats.add(new Seat("Emergency", emergencyPrice, cpf_name[0], cpf_name[1]));
            j++;
        }
        for (int i = 0; i < 24; i++) // 30 = 53
        {
            String cpf_name[] = CPF_name.get(j).split(",");
            seats.add(new Seat("Regular", regularPrice, cpf_name[0], cpf_name[1]));
            j++;
        }
    }   

    public String toTxt()
    {
        String seatsStatus = "";
        for (Seat seat : seats) 
        {
            seatsStatus += seat.getisEmpty() + ",";
        }

        String costumers = "";
        for (Seat seat : seats) 
        {
            costumers += seat.getCpf() + "," + seat.getName() + "=";
        }

        return ID+";"+ model+";"+ originAirport+";"+ originState+";"+ destinationAirport+";"+ 
               destinationState+";"+ departureDate+";"+ departureTime+";"+ arrivalDate+";"+
               arrivalTime+";"+ regularPrice+";"+ emergencyPrice+";"+ vipPrice+";"+ milesPercentage+";"+ 
               seatsStatus+";" + costumers;
    }

    public boolean isEmpty()
    {
        for (Seat seat : seats) 
        {
            if(!seat.getisEmpty())
                return false;
        }
        return true;
    }

    public String getDestinationAirport() 
    {
        String[] airport_name = this.destinationAirport.split("/");
        String[] airport_city = airport_name[0].split("de");
        String[] no_comma = airport_city[1].split(",");
        return no_comma[0];
    }
    public String getDestinationAirportFull() 
    {
        return this.destinationAirport;
    }
    public void setdestinationAirport(String destinationAirport) 
    {
        this.destinationAirport = destinationAirport;
    }
    public String getArrivalDate() 
    {
        return arrivalDate;
    }
    public void setarrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }
    public String getDestinationState() {
        return destinationState;
    }
    public void setDestinationState(String destinationState) {
        this.destinationState = destinationState;
    }
    public String getArrivalTime() {
        return arrivalTime;
    }
    public void setarrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    public String getDepartureDate() {
        return departureDate;
    }
    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }
    public String getDepartureTime() {
        return departureTime;
    }
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }
    public String getID() {
        return ID;
    }
    public void setID(String iD) 
    {
        ID = iD;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getOriginAirport() 
    {
        String[] airport_name = this.originAirport.split("/");
        String[] airport_city = airport_name[0].split("de");
        String[] no_comma = airport_city[1].split(",");
        return no_comma[0];
    }
    public String getOriginAirportFull() 
    {
        return this.originAirport;
    }
    public void setOriginAirport(String originAirport) {
        this.originAirport = originAirport;
    }
    public String getOriginState() {
        return originState;
    }
    public void setOriginState(String originState) {
        this.originState = originState;
    }
    public ArrayList<Seat> getSeats() {
        return seats;
    }
    public void setSeats(ArrayList<Seat> seats) {
        this.seats = seats;
    }
    public double getVipPrice(){
        return this.vipPrice;
    }
    public double getEmergencyPrice(){
        return this.emergencyPrice;
    }
    public double getRegularPrice(){
        return this.regularPrice;
    }
    public double getMilesPercentage(){
        return this.milesPercentage;
    }
    public ArrayList<Boolean> getSeatsStatus()
    {
        ArrayList<Boolean> seatsStatus = new ArrayList<Boolean>();
        for (Seat seat : seats) 
        {
            seatsStatus.add(seat.getisEmpty());
        }

        return seatsStatus;
    }
    public String getCapacity(){
        return this.capacity;
    }
}
