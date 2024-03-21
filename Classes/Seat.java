package Classes;
public class Seat
{
    private String name;
    private String cpf;
    private double price;
    private String seatType;
    private boolean isEmpty;
    private boolean isSelected;
    private int index;

    public Seat(String seatType, double price)
    {
        this.seatType = seatType;
        this.name = "0";
        this.cpf = "0";
        this.isEmpty = true;
        this.isSelected = false;
        this.price = price;
    }

    public Seat(String seatType, double price, String cpf, String name)
    {
        this.seatType = seatType;
        this.name = name;
        this.cpf = cpf;
        this.isEmpty = true;
        this.isSelected = false;
        this.price = price;
        this.index = 0;
    }
















    public void setIndex(int index)
    {
        this.index = index;
    }
    public String getIndex()
    {
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
        
        String result = "";
        int i = 1;
        int j = 0;
        for (int k = 0; k < index; k++) 
        {
            result = letters[j] + i;
            
            i++;
            if(i == 7)
            {
                i = 1;
                j++;
            }
        }

        return result;
    }
    public double getPrice() {
        return this.price;
    }public String getSeatType() {
        return seatType;
    }
    public void setCostumer(String[] cpf_name) {
        this.cpf = cpf_name[0];
        this.name = cpf_name[1];
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }
    public void setisEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }
    public boolean getisEmpty() {
        return this.isEmpty;
    }
    public boolean getisSelected()
    {
        return isSelected;
    }
    public void setisSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    public String getRegularColor()
    {
        if(seatType.equals("Regular"))
            return "-fx-background-color: #DA975E; -fx-border-vidth: 2px 2px 2px 2px; -fx-border-radius: 50px; -fx-background-radius: 50px; -fx-border-style:solid; -fx-border-color:#ffffff;";
        if(seatType.equals("Vip"))
            return "-fx-background-color: #7F63D8; -fx-border-vidth: 2px 2px 2px 2px; -fx-border-radius: 50px; -fx-background-radius: 50px; -fx-border-style:solid; -fx-border-color:#ffffff;";
        return "-fx-background-color: #193E80; -fx-border-vidth: 2px 2px 2px 2px; -fx-border-radius: 50px; -fx-background-radius: 50px; -fx-border-style:solid; -fx-border-color:#ffffff;";
    }
    public String getCpf() 
    {
        if(cpf.equals("0"))
            return "Empty";
        return cpf;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public String getName() 
    {
        if(name.equals("0")) 
            return "Empty";
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
}