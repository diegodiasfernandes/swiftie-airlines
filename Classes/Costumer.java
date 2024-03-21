package Classes;
import java.util.ArrayList;

public class Costumer 
{
    private String name;
    private String CPF;
    private String password;
    private double miles;
    private ArrayList<String> flightIDs = new ArrayList<String>();

    public Costumer(String name, String CPF, String password)
    {
        this.CPF = CPF;
        this.name = name;
        this.password = password;
        this.miles = 0;
        this.flightIDs = null;
    }

    public Costumer(String name, String CPF, String password, double miles, ArrayList<String> flightIDs)
    {
        this.CPF = CPF;
        this.name = name;
        this.password = password;
        this.miles = miles;    
        this.flightIDs = flightIDs;
    }

    public String toTxt()
    {
        if(flightIDs == null)
        {
            return name+";"+ CPF+";"+ password+";"+ miles+";"+ "null";
        }

        String ids = "";
        for (String string : flightIDs) 
        {
            ids += string + ",";
        }

        return name+";"+ CPF+";"+ password+";"+ miles+";"+ ids;
    }

    public String getCPF() {
        return CPF;
    }
    public void setCPF(String cPF) {
        CPF = cPF;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public double getMiles() {
        return miles;
    }
    public void setMiles(double miles) {
        this.miles = miles;
    }
    public ArrayList<String> getFlightIDs() {
        return flightIDs;
    }public void setFlightIDs(ArrayList<String> flightIDs) {
        this.flightIDs = flightIDs;
    }
}
