//Aksanur Konuk 150120049, Duru Baştunalı 150120075, Berat Demirhan 150119866
//HTS (Half Traveling Salesman) Class

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class HTS {
    public static ArrayList<City> cities = new ArrayList<>(); //All cities
    public static City centerCity; //The city that is closest to the densest point (central city)
    public static double xMean = 0, yMean = 0; //x means and y mean

    public static void main (String[] args) throws IOException {
        System.out.println("Enter the input file name: "); //Get the input file name from the user
        Scanner scanFile = new Scanner(System.in);
        String fileName = scanFile.nextLine();

        File inputFile = new File(fileName); //Get the input file based on the given name
        Scanner input = new Scanner(inputFile), cityInput = new Scanner(inputFile);
        int order, cityCount = 0;
        //Determine how many cities are there in the input file. This action is done beforehand, because while reading
        //the input, we also calculate the x and y mean. Therefore, we need the number of the cities.
        for (; cityInput.hasNextLine(); cityInput.nextLine())
            cityCount++;

        double x, y;
        while (input.hasNextLine()) { //Read from the input file
            order = input.nextInt();
            x = input.nextDouble();
            y = input.nextDouble();
            xMean += x / cityCount; //Calculate the x mean
            yMean += y / cityCount; //Calculate the y mean
            cities.add(new City(order, x, y)); //Generate the city object and add it to the array list
        } centerCity = findPoint(cityCount); //Find the central city
        Route.shortestPath(centerCity, cityCount,cities); //Find the initial-default path
        scanFile.close();
        input.close();
    }

    //These two methods are used to calculate the densest point, and later on trying to find the
    //central city based on among all cities, which one is the closest one to the found point.
    //In this method, an imported class "Multivariate Normal Distribution" is used. (Details in report)
    public static City findPoint(int n){
        double[] mean = {xMean,yMean};
        double[][] covariance = calculationCovariance(n);
        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(mean, covariance);
        City maxDensitCity = null;
        double density = 0;
        for (City city : cities) {
            double[] currPoint = {city.x, city.y};
            double currDensity = distribution.density(currPoint);
            if (currDensity > density) {
                density = currDensity;
                maxDensitCity = city;
            }
        } maxDensitCity.setUsed(true);
        return maxDensitCity;
    }

    public static double[][] calculationCovariance(int n){
        double sxy = 0, sxx = 0, syy = 0;
        for (City city : cities) {
            double x = city.x, y = city.y;
            sxy += (x - xMean) * (y - yMean) / n - 1;
            sxx += Math.pow((x - xMean), 2) / n - 1;
            syy += Math.pow((y - yMean), 2) / n - 1;
        } return new double[][]{{sxx,sxy},{sxy,syy}};
    }
}
