package lab11.graphs;

import java.util.LinkedList;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int initPos;
    private int destPos;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        // Add more variables here!
        this.initPos = maze.xyTo1D(sourceX, sourceY);
        this.destPos = maze.xyTo1D(targetX, targetY);
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        Queue<Integer> explorerQueue = new LinkedList<>();
        explorerQueue.add(initPos);
        distTo[initPos] = 0;
        edgeTo[initPos] = initPos;

        while (!explorerQueue.isEmpty()) {
            int poped = explorerQueue.remove();
            if (poped == destPos) {
                break;
            }
            marked[poped] = true;
            announce();

            for (int m: maze.adj(poped)) {
                if (!marked[m]) {
                    marked[m] = true;
                    explorerQueue.add(m);
                    if (distTo[m] > distTo[poped] + 1) {
                        distTo[m] = distTo[poped] + 1;
                        edgeTo[m] = poped;
                        announce();
                    }
                }
            }
        }
    }


    @Override
    public void solve() {
        bfs();
    }
}

