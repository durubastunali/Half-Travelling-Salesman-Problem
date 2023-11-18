//Aksanur Konuk 150120049, Duru Baştunalı 150120075, Berat Demirhan 150119866
//Route Class

import java.io.IOException;
import java.util.ArrayList;

public class Route {
    static double totalDistance = 0; //Total distance traveled
    static ArrayList<City> usedHalf = new ArrayList<>(), unusedHalf = new ArrayList<>();

    public static void shortestPath(City centerCity, int cityCount, ArrayList<City> cities) throws IOException {
        City nextCity = findNearest(centerCity, cities); //Find the nearest city to the central city
        nextCity.setUsed(true); //Set the city as used
        usedHalf.add(centerCity);
        usedHalf.add(nextCity);
        for (int i = 3; i <= (cityCount + 0.5) / 2; i++) { //Create a path with n/2 cities
            //Find the nearest city to the path with referenced to the central city
            nextCity = findNearestCentered(nextCity, cities, centerCity);
            usedHalf.add(nextCity);
            nextCity.setUsed(true);
        } totalDistance += findDistance(centerCity, usedHalf.get(usedHalf.size()-1));
        for (City city : cities) { //Store all the unused cities in an array list for a later use
            if (!city.used)
                unusedHalf.add(city);
        } findMinRoute(); //First optimization
        Heuristic.heuristicAlgorithm(); //Second optimization
    }

    //Find the nearest neighbor to the city
    public static City findNearest(City city, ArrayList<City> cities) {
        City nearest = null;
        int distance = 2147483647, newDistance = 0;
        for (City neighbor : cities) {
            newDistance = findDistance(city, neighbor);
            if (newDistance < distance && (city.id != neighbor.id)) {
                distance = newDistance;
                nearest = neighbor;
            }
        } totalDistance += distance;
        return nearest;
    }

    //Find the nearest neighbor to the city with referenced to the central city
    public static City findNearestCentered(City city, ArrayList<City> cities, City centerCity) {
        City nearest = null;
        int distance = 2147483647, newDistance = 0, centerDistance = 0, addPath = 0;
        for (City neighbor : cities) {
            centerDistance = findDistance(centerCity, city);
            newDistance = findDistance(city, neighbor);
            if (centerDistance + newDistance < distance && neighbor.id != city.id && neighbor.id != centerCity.id && !neighbor.used) {
                distance = newDistance + centerDistance;
                addPath = newDistance;
                nearest = neighbor;
            }
        } totalDistance += addPath;
        return nearest;
    }

    //Find the minimum route by changing the cities in the calculated path
    public static void findMinRoute(){
        for(int i= 0; i < usedHalf.size()-1; i++){
            for (int j = i +1; j < usedHalf.size(); j++){
                City currCity = usedHalf.get(i);
                City CityToBeChanged = usedHalf.get(j);
                if(j == i+1)
                    compare2Path(currCity, CityToBeChanged, i, j);
                else if(i == 0 && j == usedHalf.size()-1)
                    compareBeginWithLast(currCity, CityToBeChanged, i, j);
                else
                    compare4Path(currCity, CityToBeChanged, i, j);
            }
        }
    }
    public  static void compareBeginWithLast(City city, City cityToBeChanged, int i, int j){
        City middleCity1 = usedHalf.get((i + 1 + usedHalf.size()) %usedHalf.size());
        City middleCity2 = usedHalf.get((j - 1 + usedHalf.size()) %usedHalf.size());
        double currDistance = findDistance(city, middleCity1) + findDistance(middleCity2, cityToBeChanged) ;
        double distanceToBeChanged = findDistance(cityToBeChanged, middleCity1) + findDistance(middleCity2, city);
        if(distanceToBeChanged < currDistance)
            swapCities(i,j, currDistance, distanceToBeChanged);
    }

    public static void compare2Path(City city, City cityToBeChanged, int i, int j){
        City city1 = usedHalf.get((i - 1 + usedHalf.size()) %usedHalf.size());
        City city2 = usedHalf.get((j + 1 + usedHalf.size()) %usedHalf.size());
        double currDistance = findDistance(city1, city) + findDistance(cityToBeChanged, city2) ;
        double distanceToBeChanged = findDistance(city1, cityToBeChanged) + findDistance(city, city2);
        if(distanceToBeChanged < currDistance)
            swapCities(i,j, currDistance, distanceToBeChanged);
    }

    public static void compare4Path(City city, City cityToBeChanged, int i, int j){
        City city1 = usedHalf.get((i - 1 + usedHalf.size()) %usedHalf.size());
        City city2 = usedHalf.get((i + 1 + usedHalf.size()) %usedHalf.size());
        City city3 = usedHalf.get((j - 1 + usedHalf.size()) %usedHalf.size());
        City city4 = usedHalf.get((j + 1 + usedHalf.size()) %usedHalf.size());
        double currDistance = findDistance(city1, city) + findDistance(city, city2) + findDistance(city3, cityToBeChanged) + findDistance(cityToBeChanged, city4);
        double distanceToBeChanged = findDistance(city1, cityToBeChanged) + findDistance(cityToBeChanged, city2) + findDistance(city3, city) + findDistance(city, city4);
        if(distanceToBeChanged < currDistance)
            swapCities(i,j, currDistance, distanceToBeChanged);
    }

    public static int findDistance(City city1, City city2){
        return (int)Math.round(Math.sqrt(Math.pow(city1.x - city2.x, 2) + Math.pow(city1.y - city2.y, 2)));
    }

    public static void swapCities(int i, int j, double currDistance, double distanceToBeChanged){ //j değiştirilen
        totalDistance = totalDistance - currDistance + distanceToBeChanged;
        City temp = usedHalf.get(j);
        usedHalf.set(j, usedHalf.get(i));
        usedHalf.set(i, temp);
    }
    public static ArrayList<City> getUsedHalf() {
        return usedHalf;
    }

    public static ArrayList<City> getUnusedHalf() {
        return unusedHalf;
    }

    public static double getTotalDistance() {
        return totalDistance;
    }
}