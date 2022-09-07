package lab11.graphs;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(v) - maze.toX(t)) + Math.abs(maze.toY(v) - maze.toY(t));
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        PriorityQueue<Integer> explorerQueue = new PriorityQueue<>(new MazeAStarPath.distanceComparator());
        explorerQueue.add(s);
        int current;
        while (!explorerQueue.isEmpty() && !targetFound) {
            current = explorerQueue.remove();
            if (marked[current]) {
                continue;
            }
            marked[current] = true;
            announce();
            for (int neighbor: maze.adj(current)) {
                if (marked[neighbor]) {
                    continue;
                }
                if (neighbor == t) {
                    edgeTo[neighbor] = current;
                    announce();
                    return;
                }
                if (distTo[current] + 1 < distTo[neighbor]) {
                    edgeTo[neighbor] = current;
                    distTo[neighbor] = distTo[current] + 1;
                    explorerQueue.add(neighbor);
                    announce();
                }
            }
        }
    }

    class distanceComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return Integer.compare(distTo[o1] + h(o1), distTo[o2] + h(o2));
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

