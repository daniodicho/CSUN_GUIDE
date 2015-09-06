package googlemaps.example.com.csun_guide;

import java.util.LinkedList;

public class Point {

    double cost;
    int id;
    String name;
    //   String initial;
    double latitude;
    double longitude;
    int[] adjacentids;
    LinkedList<Point> adjacents;
    Point PathPredecessor;
    double DistanceToSource;
    double DistanceToGoal;

    public Point(int ID, String name, double latitude, double longitude,   int[] ids) {
        this.longitude = longitude;
        this.id = ID;
        this.latitude = latitude;
        adjacents = new LinkedList<Point>();
        adjacentids = ids;
        this.name = name;
    }

    public Point(double lat, double lon){
        this.longitude = lon;
        this.latitude = lat;
    }

    public int getID() {
        return id;
    }

    public void setID(int ID) {
        this.id = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    //  public String getInitial(){
    //    return this.initial;
    //}
    //public void setInitial(String in){
    //  this.initial=in;
    //}

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void addAdjacents(Point p){
        this.adjacents.add(p);
    }

    public String row(){
        String adjs="";
        if(!adjacents.isEmpty()){
            int i=0;
            while(i<adjacents.size())
            {
                adjs += adjacents.get(i).name+" ";
                i++;
            }
        }
        return "ID: "+Integer.toString(this.getID())+"\n"+"Name: "+this.getName()+"\nLatitude: "+this.latitude+"\nLongitude: "+this.longitude+"\nAdjacents: "+adjs;
    }

    public double distance(double lat2, double lon2){
        return  Math.sqrt(Math.pow(getLatitude() - lat2, 2) + Math.pow(getLongitude() - lon2, 2));
    }

    public double distance(Point p2){
        return Math.sqrt(Math.pow(getLatitude() - p2.getLatitude(), 2) + Math.pow(getLongitude() - p2.getLongitude(), 2));
    }

    public double DistanceFrom(Point p){
        return distance(p)*0.000008998;
    }

    public boolean isAdjacent(Point p){
        for(int i=0;i<adjacents.size();i++){
            if(p.id==adjacents.get(i).id){
                return true;
            }
        }
        return false;
    }
}