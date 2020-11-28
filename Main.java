import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static BufferedReader bufferedReader;

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            //Inputs
            System.out.println("Enter the starting city: ");
            String starting_city = scanner.nextLine();
            System.out.println("Enter enter the final city: ");
            String ending_city = scanner.nextLine();

            System.out.println("");


            LinkedList<String> attractions = new LinkedList<String>();
            while (true) {
                System.out.println("Please enter an attraction name: ");
                System.out.println("Type q to quit");
                String attraction = scanner.nextLine();
                if (attraction.equals("q")) {
                    break;
                } else {
                    attractions.add(attraction);
                }

            }

            List<String> trip = buildRoute(starting_city, ending_city, attractions);
            System.out.println("Route = " + trip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> buildRoute(String start, String end, List<String> attractions) throws IOException {
        Graph graph = new Graph();


        bufferedReader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/roads.csv"));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String[] data = line.split(",");
            graph.addCitiesByDistance(data[0], data[1], Integer.parseInt(data[2]));
        }
        bufferedReader.close();

        bufferedReader = new BufferedReader(new FileReader(System.getProperty("user.dir") + "/attractions.csv"));
        while ((line = bufferedReader.readLine()) != null) {
            String[] data = line.split(",");
            graph.addAttractionsToCity(data[1], data[0]);
        }
        bufferedReader.close();

        for (String place : attractions)
            graph.addAttractions(place);

        LinkedList<String> list = graph.route(start, end);

        return list;
    }
}
