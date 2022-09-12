import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        long begin = g.closest(stlon, stlat);
        long end = g.closest(destlon, destlat);

        Set<Long> marked = new HashSet<>();
        Map<Long, Long> edgeTo = new HashMap<>();
        Map<Long, Double> distanceTo = new HashMap<>();
        PriorityQueue<Long> searchQueue = new PriorityQueue<>(new DistanceComparator(g, end, distanceTo));
        edgeTo.put(begin, begin);
        distanceTo.put(begin, 0.);
        searchQueue.add(begin);
        long current;
        labelA:
        while (!searchQueue.isEmpty()) {
            current = searchQueue.remove();
            marked.add(current);
            for (long neighbor: g.adjacent(current)) {
                if (marked.contains(neighbor)) {
                    continue;
                }
                double newDistance = distanceTo.get(current) + g.distance(current, neighbor);
                if (distanceTo.containsKey(neighbor)) {
                    if (newDistance < distanceTo.get(neighbor)) {
                        distanceTo.put(neighbor, newDistance);
                        edgeTo.put(neighbor, current);
                        searchQueue.add(neighbor);
                    }
                } else {
                    distanceTo.put(neighbor, newDistance);
                    edgeTo.put(neighbor, current);
                    searchQueue.add(neighbor);
                }
                if (neighbor == end) {
                    break labelA;
                }
            }
        }

        List<Long> res = new ArrayList<>();
        if (edgeTo.get(end) == null) {
            return res;
        }
        current = end;
        while (current != begin) {
            res.add(0, current);
            current = edgeTo.get(current);
        }
        res.add(0, current);
        return res;
    }

    static class DistanceComparator implements Comparator<Long> {

        GraphDB g;
        long end;
        Map<Long, Double> distanceTo;
        DistanceComparator(GraphDB g, long target, Map<Long, Double> distanceTo) {
            this.g = g;
            this.end = target;
            this.distanceTo = distanceTo;
        }
        @Override
        public int compare(Long o1, Long o2) {
            double distance1 = g.distance(o1, end) + distanceTo.get(o1);
            double distance2 = g.distance(o2, end) + distanceTo.get(o2);
            return Double.compare(distance1, distance2);
        }
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> res = new ArrayList<>();
        if (route.size() > 1) {
            long v1 = route.get(0);
            for (int m = 0; m < route.size() - 1; m += 1) {
                long v2 = route.get(m);
                long v3 = route.get(m+1);
                NavigationDirection temp = new NavigationDirection();
                temp.direction = direction(g, v1, v2, v3);
                v1 = v2;
                temp.distance = g.distance(v2, v3);
                temp.way = g.idNodeMap.get(v2).neighbors.get(v3).extraInfo.get("name");
                if (temp.way == null) {
                    temp.way = NavigationDirection.UNKNOWN_ROAD;
                }
                res.add(temp);
            }
        }
        return res;
    }

    static int direction(GraphDB g, long v1, long v2, long v3) {
        if (v1 == v2) {
            return NavigationDirection.STRAIGHT;
        }
        double dx1 = g.lon(v2) - g.lon(v1);
        double dy1 = g.lat(v2) - g.lat(v1);
        double dx2 = g.lon(v3) - g.lon(v2);
        double dy2 = g.lat(v3) - g.lat(v2);
        double angle1 = Math.atan(dy1 / dx1);
        double angle2 = Math.atan(dy2 / dx2);
        double dAngle = angle2 - angle1;
        if (dAngle <= 60 && dAngle > 10) {
            return NavigationDirection.SLIGHT_LEFT;
        } else if (dAngle <= 10 && dAngle >= - 10) {
            return NavigationDirection.STRAIGHT;
        } else if (dAngle >= - 60 && dAngle < -10) {
            return NavigationDirection.SLIGHT_RIGHT;
        } else if (dAngle > 60 && dAngle <= 120) {
            return NavigationDirection.LEFT;
        } else if (dAngle > 120) {
            return NavigationDirection.SHARP_LEFT;
        } else if (dAngle < -60 && dAngle >= -120) {
            return NavigationDirection.RIGHT;
        }
        return NavigationDirection.SHARP_RIGHT;
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
