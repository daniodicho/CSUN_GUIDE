package googlemaps.example.com.csun_guide;

import java.util.ArrayList;
import java.util.LinkedList;


import java.util.LinkedList;

/**
 * Created by Dani on 7/5/2015.
 */
public class Table{
    Point points[];
    ArrayList<Point> points5 = new ArrayList<Point>();
    int Count =0;



    // reads all the data from a String array and stores it in a Table object
    Table(String[] s){
        // initialize linked list
        points = new Point[s.length];
        //Go through each line
        for(int i=0;i<s.length;i++) {

            //each line has values separated by tabs, store those values in an array
            String[] columns = s[i].split("\t");
            if(columns[0].length()>4)
                columns[0]= columns[0].substring(0, 4);

            // first item is the id
            int id = Integer.parseInt(columns[0]);

            //second item is the name
            String name = columns[1];

            //third and fourth are latitude and longitude
            float latitude = Float.parseFloat(columns[2]);
            float longitude = Float.parseFloat(columns[3]);

            //The next set of items are the adjacent point ids, store them in an array
            int[] adjIds = new int[columns.length-4];

            for(int j=4;j<columns.length;j++){
                if(isNumeric(columns[j])){
                    if(columns[j].length()>4)
                        columns[j]= columns[j].substring(0, 4);
                    adjIds[j-4] = Integer.parseInt(columns[j]);
                }
            }
            //add the point to the table
            this.add(new Point(id,name,latitude,longitude,adjIds));
        }
    }

    // adds a point to the table
    public void add(Point p){
        points[Count] =p;
        Count++;
    }

    public Point find(int index){


        for (int i=0;i<points.length ;i++){
            if(points[i].getID()== index)
                return points[i];
        }

        return new Point(0,"",0,0,new int[1]);
    }

    public Point findByName(String name, double lat,double lon){

        for (int i=0;i<points.length ;i++) {
            double min=Double.MAX_VALUE;
            int closest=0;
            if (points[i].getName().toLowerCase().equals(name.toLowerCase())) {
                while (points[i].getName().toLowerCase().equals(name.toLowerCase())) {
                    double distance = points[i].distance(lat,lon);
                    if (distance < min) {
                        min = distance;
                        closest = i;
                    }
                    i++;
                }
                return points[closest];
            }

        }
        return new Point(0,"",0,0,new int[1]);
    }

    public boolean checkValidPath(String name) {

        for (int i = 0; i < points.length; i++)
            if (points[i].getName().toLowerCase().equals(name.toLowerCase())) {
                return true;
            }

        return false;
    }

    public void setAllAdjacents(){

        // Go trhough all points in the table
        for (int i=0;i<points.length ;i++){
            //get a point
            Point p =points[i];
            //get the array that contains the ids of adjacent points
            int[] adj = p.adjacentids;

            for(int j=0;j<adj.length;j++){
                Point n = find(adj[j]);
                if (n.getLatitude()!=0)  // make sure it's not a null point
                {
                    if(!points[i].isAdjacent(n))
                        points[i].addAdjacents(n);
                    if(!n.isAdjacent(points[i]))
                        n.addAdjacents(points[i]);

                }


            }
        }
    }

    public double getAngle(Point a,Point b){
        if(a.latitude<b.latitude)
            return Math.toDegrees(Math.atan((b.longitude-a.longitude)/(b.latitude-a.latitude)));
        else
            return Math.toDegrees(Math.atan((a.longitude-b.longitude)/(a.latitude-b.latitude)))+180;
    }

    //Checks if a string is a number
    public static boolean isNumeric(String str){
        try{double d = Double.parseDouble(str);}
        catch(NumberFormatException nfe){return false;}
        return true;
    }

    public Point findClosest(Point p){
        double distance = Double.MAX_VALUE;
        Point closest=null;
        for(int i=0;i<points.length;i++){
            double currentDistance= p.distance(points[i]);
            if(currentDistance<distance)
            {
                closest=points[i];
                distance=currentDistance;
            }
        }
        return closest;
    }

    public ArrayList<Point> findClosest5(Point p){
        double distance = Double.MAX_VALUE;
        Point closest=null;
        for(int i=0;i<points.length;i++){
            double currentDistance= p.distance(points[i]);
            if(currentDistance<distance)
            {
                points5.add(points[i]);
                distance=currentDistance;
            }
        }
        return points5;
    }

}