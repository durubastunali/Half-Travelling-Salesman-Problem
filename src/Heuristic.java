//Aksanur Konuk 150120049, Duru Baştunalı 150120075, Berat Demirhan 150119866
//Heuristic Class

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Heuristic {
    static ArrayList<City> usedHalf = Route.getUsedHalf(), unusedHalf = Route.getUnusedHalf();
    static int preDistance = (int) Route.getTotalDistance(), usedSize = usedHalf.size(), unusedSize = unusedHalf.size(), curDistance = 0;

    //2-Opt Heuristic Algorithm
    public static void heuristicAlgorithm() throws IOException {
        for (int i = 0; i < usedSize - 1; i++){
            City city1 = usedHalf.get(i); //City that's used in the path at index i
            City city2 = usedHalf.get(i+1); //City that's used in the path at index i+1
            for (int j = 0; j < unusedSize - 1; j++){
                City tempCity1 = unusedHalf.get(j); //Unused city in array list at index j
                City tempCity2 = unusedHalf.get(j + 1); //Unused city in array list at index j+1
                //Swap the used cities with unused cities
                if (swapCities( i, j, city1, city2, tempCity1, tempCity2) || swapCities( i, j, city1, city2, tempCity2,tempCity1)){
                    i = 0;
                    break;
                }
            }
        } writeResults(); //Write the final path and its index in order with the total distance traveled on output
    }

    public static boolean swapCities( int i,int j,City city1, City city2, City tempFirst, City tempSecond) {
        ArrayList<City> temp = new ArrayList<>(usedHalf); //Create a temp array list to adjust the path
        temp.set(i, tempFirst);
        temp.set(i + 1, tempSecond);
        curDistance = calculatePath(temp); //Find the new length of the updated path
        if (curDistance < preDistance) { //If a shorter path is found:
            usedHalf = (ArrayList<City>) temp.clone(); //If the updated find is shorter, update the used half array list
            unusedHalf.set(j, city1);
            unusedHalf.set(j + 1, city2);
            preDistance = curDistance;
            return true;
        } return false;
    }

    //Calculate the path by finding the distance between cities at indexes i and i+1, and eventually 0 and final city
    public static int calculatePath(ArrayList<City> cities) {
        int path = 0;
        for (int i = 0; i < cities.size() - 1; i++) {
            path += findDistance(cities.get(i), cities.get(i + 1));
        } path += findDistance(cities.get(0), cities.get(cities.size() - 1));
        return path;
    }

    //Finding the distance between two cities with the given analytical formula
    public static int findDistance(City city1, City city2){
        return (int)Math.round(Math.sqrt(Math.pow(city1.x - city2.x, 2) + Math.pow(city1.y - city2.y, 2)));
    }

    //Writing the final path on the output file
    public static void writeResults() throws IOException {
        FileWriter output = new FileWriter("output.txt");
        output.write(preDistance + "\n");
        for (City city : usedHalf)
            output.write(city.id + "\n");
        output.close();
    }
}