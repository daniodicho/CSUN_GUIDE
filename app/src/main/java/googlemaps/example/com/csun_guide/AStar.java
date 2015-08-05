package googlemaps.example.com.csun_guide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;


public class AStar {
	

	public static LinkedList<Point> aStarPathFinding(Point source, Point destination)
    {
        LinkedList<Point> open = new LinkedList<Point>();
        LinkedList<Point> closed = new LinkedList<Point>();
        LinkedList<Point> path = new LinkedList<Point>();

        // Grab the source Node
        Point current = source;

        // No cost at the start
        current.cost = 0;

        open.add(current);

        // Sorting implementation of the open set
        // Sort with respect to cost, lowest cost nodes
        // are prioritized
        open = sortByCost(open);

        // Loop through until the open set is empty
        while(open!=null&&!open.isEmpty()){
        
            current = open.get(0);
            open.remove(0);
        
            // If the current node is the destination node,
            // Path is complete, break out of loop
            if ((current.latitude == destination.latitude)&&(current.longitude == destination.longitude))
            {
                break;
            }
        
            // Add the current node to the set and change is Navigability
            closed.add(current);
        
            // Go through all the current nodes adjacency list
        	for(int i=0;i<current.adjacents.size();i++)
            {
        		Point adjacent = current.adjacents.get(i);
                // if the adjacent node has not been processed, process the node
                if (!hasPoint(open,adjacent) && !hasPoint(closed,adjacent))
                {
                    // Set the adjacents previous node to the current node
                    adjacent.PathPredecessor = current;

                    // calculate the distance of the adjacent node from the source
                    adjacent.DistanceToSource = current.DistanceToSource +
                        Point.distance(current, adjacent);

                    // Calculate the heuristic distance of the adjacent node to the goal
                    adjacent.DistanceToGoal =
                    	Point.distance(current, adjacent) + Point.distance(current, destination);

                    // Set the total cost of the adjacent node
                    adjacent.cost = adjacent.DistanceToSource + adjacent.DistanceToGoal;

                    // Add the node the the open set, and change its Navigability
                    open.add(adjacent);
                }
            }        
        open = sortByCost(open);
    }
    
    while(Point.distance(current, source)!=0){
    	path.add(current);
    	current=current.PathPredecessor;
    }
    path.add(source);
    path = reverse(path);
    
    return path;
}
	
        public static LinkedList<Point> sortByCost(LinkedList<Point> p){
        	for(int i=0;i<p.size();i++){
        		for(int j=0; j<p.size()-1;j++){
        			if(p.get(j).cost>p.get(j+1).cost){
        				Point temp = p.get(j);
        				p.remove(j);
        				p.add(j+1, temp);
        			}
        		}
        	}
        	return p;
        }
        
        public static LinkedList<Point> reverse(LinkedList<Point> p){
        	LinkedList<Point> newList = new LinkedList<Point>();
        	while(!p.isEmpty()){
        		newList.add(p.getLast());
        		p.removeLast();
        	}
        	return newList;
        }
        
        static boolean hasPoint(LinkedList<Point> list,Point p){
        	for(int i=0;i<list.size();i++){
        		if(list.get(i).id==p.id){
        			return true;
        		}
        	}
        	return false;
        }
        
}