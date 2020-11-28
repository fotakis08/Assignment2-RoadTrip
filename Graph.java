import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Graph {
    private ArrayList<City> cities;
    private ArrayList<ArrayList<Road>> edges;
    private LinkedList<Integer> attractions;

    // Constructor
    public Graph() {
        cities = new ArrayList<City>();
        edges = new ArrayList<ArrayList<Road>>();
        attractions = new LinkedList<Integer>();
    }

    public boolean cityExists(String name) {
        return (position(name) >= 0);
    }

    public int position(String city) {
        for (int j = 0; j < cities.size(); j++) {
            if (cities.get(j).name.equalsIgnoreCase(city))
                return j;
        }
        return -1;
    }

    public boolean addAttractionsToCity(String cityName, String attraction) {
        try {
            if (!cityExists(cityName)) {
                return false;
            }

            int cityIndex = position(cityName);
            cities.get(cityIndex).attractions.add(attraction);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addAttractions(String attraction) {
        for (int j = 0; j < cities.size(); j++) {
            City city = cities.get(j);
            if (city.attractions.contains(attraction)) {
                attractions.add(j);
                return true;
            }
        }
        return false;
    }

    public void addCitiesByDistance(String cityOne, String cityTwo, int distance) {
        if (!cityExists(cityOne)) {
            // add city if it doesnt exist,
            City v1 = new City(cityOne);
            cities.add(v1);
            edges.add(new ArrayList<Road>());
        }

        int tempCity = position(cityOne);
        ArrayList<Road> roads = this.edges.get(tempCity);

        if (!cityExists(cityTwo)) {
            City city = new City(cityTwo);
            cities.add(city);
            this.edges.add(new ArrayList<Road>());
        }
        int tempCity2 = position(cityTwo);
        ArrayList<Road> roadArrayList = this.edges.get(tempCity2);

        // create two new roads
        Road road = new Road(tempCity, tempCity2, distance);
        // and of the second city
        Road road1 = new Road(tempCity2, tempCity, distance);

        // add the new roads to the lists of roads of each city
        roads.add(road);
        roadArrayList.add(road1);
    }

    public LinkedList<String> route(String start, String end) {
        int source = position(start), target = position(end);
        boolean isPlaces = !attractions.isEmpty();
        if (!isPlaces)
            attractions.add(source);
        List<LinkedList<Integer>> linkedLists = Utils.permutations(attractions);
        int attrNo = attractions.size();

        if (isPlaces) {
            attractions.add(source);
        }

        attractions.add(target);

        int sources = attrNo;
        int temp = attrNo + 1;
        if (!isPlaces) {
            sources = 0;
            temp = 1;
        }

        ArrayList<ArrayList<Integer>> distances = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> previousNodes = new ArrayList<ArrayList<Integer>>();
        for (int i = 0; i < attractions.size(); i++) {
            int attraction = attractions.get(i);

            ArrayList<Integer> tempDistances = new ArrayList<Integer>();
            ArrayList<Integer> previous = new ArrayList<Integer>();
            findShortestPath(tempDistances, previous, attraction);
            distances.add(tempDistances);
            previousNodes.add(previous);
        }

        int dist = -1, npMin = -1;
        for (int i = 0; i < linkedLists.size(); i++) {
            LinkedList<Integer> perm = linkedLists.get(i);

            int current = 0;

            // the first path, from source to the first vertex in the permutation
            int intEnd = perm.get(0);
            current += distances.get(sources).get(intEnd);
            List<Integer> fillPath = Utils.fillPath(previousNodes.get(sources), source, intEnd);

            for (int j = 0; j < perm.size() - 1; j++) {
                int integer = perm.get(j), attraction2 = perm.get(j + 1);
                int indexOf = attractions.indexOf(integer);

                current += distances.get(indexOf).get(attraction2);
                List<Integer> path = Utils.fillPath(previousNodes.get(indexOf), integer, attraction2);
            }

            intEnd = perm.get(perm.size() - 1);
            current += distances.get(temp).get(intEnd);
            List<Integer> pathT = Utils.fillPath(previousNodes.get(temp), target, intEnd);
            Collections.reverse(pathT);

            if (i == 0 || dist > current) {
                dist = current;
                npMin = i;
            }

        }

        LinkedList<String> shortPath = new LinkedList<String>();
        LinkedList<Integer> permMin = linkedLists.get(npMin);
        permMin.addFirst(source);
        permMin.addLast(target);

        for (int k = 0; k < permMin.size() - 1; k++) {
            int attraction1 = permMin.get(k), attraction2 = permMin.get(k + 1);
            int attraction1i = attractions.indexOf(attraction1), attraction2i = attractions.indexOf(attraction2);
            List<Integer> path = Utils.fillPath(previousNodes.get(attraction1i), attraction1, attraction2);

            for (int J = 0; J < path.size() - 1; J++)
                shortPath.addLast(cities.get(path.get(J)).name);
        }
        shortPath.addLast(cities.get(target).name);

        return shortPath;
    }

    public void findShortestPath(ArrayList<Integer> tempDistances, ArrayList<Integer> previous, int attraction) {
        ArrayList<Integer> integers = new ArrayList<Integer>();

        int m, minimum, currentDistance;

        // loops
        int i, j, k, l;

        for (i = 0; i < cities.size(); i++) {
            tempDistances.add(-1);
            previous.add(-1);
            integers.add(i);
        }
        tempDistances.set(attraction, 0);

        while (!integers.isEmpty()) {
            k = minimum = -1;
            for (j = 0; j < integers.size(); j++) {
                i = integers.get(j);
                currentDistance = tempDistances.get(i);
                if (currentDistance >= 0 && (k < 0 || currentDistance < minimum)) {
                    k = j;
                    minimum = currentDistance;
                }
            }

            l = integers.get(k);
            integers.remove(k);


            for (j = 0; j < edges.get(l).size(); j++) {
                i = edges.get(l).get(j).city2;

                if (integers.contains(i)) {
                    m = tempDistances.get(l) + edges.get(l).get(j).distance;

                    if (tempDistances.get(i) < 0 || m < tempDistances.get(i)) {
                        tempDistances.set(i, m);
                        previous.set(i, l);
                    }
                }
            }
        }
    }
}
