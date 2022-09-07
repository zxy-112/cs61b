package lab11.graphs;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int[] parentOf;
    private boolean cycleFound = false;
    private int itemInCycle;

    public MazeCycles(Maze m) {
        super(m);
        parentOf = new int[maze.V()];
        parentOf[0] = 0;
    }

    private void dfs(int m) {
        if (cycleFound) {
            return;
        }
        marked[m] = true;
        announce();
        for (int neighbor: maze.adj(m)) {
            if (marked[neighbor]) {
                if (parentOf[m] == neighbor) {
                    continue;
                } else {
                    cycleFound = true;
                    parentOf[neighbor] = m;
                    itemInCycle = neighbor;
                    return;
                }
            }
            parentOf[neighbor] = m;
            dfs(neighbor);
        }
    }

    @Override
    public void solve() {
        bfs(0);
        if (cycleFound) {
            int curItem = itemInCycle;
            while (parentOf[curItem] != itemInCycle) {
                edgeTo[curItem] = parentOf[curItem];
                curItem = parentOf[curItem];
            }
            edgeTo[curItem] = parentOf[curItem];
            announce();
        }
    }

    // Helper methods go here

    private void bfs(int m) {
        int currentVertex;
        Queue<Integer> explorerQueue = new LinkedList<>();
        explorerQueue.add(m);
        while (!explorerQueue.isEmpty() && !cycleFound) {
            currentVertex = explorerQueue.remove();
            for (int neighbor: maze.adj(currentVertex)) {
                if (marked[neighbor]) {
                    if (parentOf[currentVertex] != neighbor) {
                        cycleFound = true;
                        itemInCycle = neighbor;
                        changeDirection(neighbor);
                        parentOf[neighbor] = currentVertex;
                        return;
                    }
                } else {
                    marked[neighbor] = true;
                    announce();
                    parentOf[neighbor] = currentVertex;
                    explorerQueue.add(neighbor);
                }
            }
        }
    }

    private void changeDirection(int m) {
        int current = m;
        int currentParent = parentOf[current];
        int currentPParent = parentOf[currentParent];
        while (current != 0) {
            parentOf[currentParent] = current;
            current = currentParent;
            currentParent = currentPParent;
            currentPParent = parentOf[currentPParent];
        }
    }
}

