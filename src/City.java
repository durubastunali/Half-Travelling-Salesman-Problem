//Aksanur Konuk 150120049, Duru Baştunalı 150120075, Berat Demirhan 150119866
//City class

public class City {
    int id; //Index of the city
    double x, y; //x and y values of the city
    boolean used; //Variable that indicates whether the city has been used in the path or not

    public City(int id, double x, double y){
        this.id = id;
        this.x = x;
        this.y = y;
        this.used = false;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
