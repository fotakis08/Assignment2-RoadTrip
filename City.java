import java.util.ArrayList;

public class City {
    public String name;
    public ArrayList<String> attractions;

    // Constructor
    public City(String name)
    {
        this.name = name;
        this.attractions = new ArrayList<>();
    }
}
