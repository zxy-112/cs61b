package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Solver {
    MinPQ<WorldState> minPQ;
    int totalEnqueued = 0;
    WorldState currentWorldState;
    Map<WorldState, WorldState> edgeTo = new HashMap<>();
    Set<WorldState> marked = new HashSet<>();
    Map<WorldState, Integer> distanceToGoal = new HashMap<>();
    Map<WorldState, Integer> movesTo = new HashMap<>();
    public Solver(WorldState initial) {
        edgeTo.put(initial, initial);
        movesTo.put(initial, 0);
        minPQ = new MinPQ<>(new StateComparator());
        minPQ.insert(initial);
        currentWorldState = initial;
        while (!minPQ.isEmpty() && !currentWorldState.isGoal()) {
            currentWorldState = minPQ.delMin();
            marked.add(currentWorldState);
            for (WorldState worldState: currentWorldState.neighbors()) {
                if (!marked.contains(worldState)) {
                    Integer previous = movesTo.get(worldState);
                    Integer current = movesTo.get(currentWorldState);
                    if (previous == null || previous > current) {
                        movesTo.put(worldState, current + 1);
                        edgeTo.put(worldState, currentWorldState);
                        minPQ.insert(worldState);
                        totalEnqueued += 1;
                    }
                }
            }
        }
    }

    class StateComparator implements Comparator<WorldState> {
        public int compare(WorldState w1, WorldState w2) {
            int distance1 = distanceToGoal(w1) + movesTo.get(w1);
            int distance2 = distanceToGoal(w2) + movesTo.get(w2);
            return Integer.compare(distance1, distance2);
        }
    }

    int distanceToGoal(WorldState worldState) {
        if (distanceToGoal.containsKey(worldState)) {
            return distanceToGoal.get(worldState);
        } else {
            int res = worldState.estimatedDistanceToGoal();
            distanceToGoal.put(worldState, res);
            return res;
        }
    }

    public int moves() {
        return movesTo.get(currentWorldState);
    }

    public Iterable<WorldState> solution() {
        List<WorldState> res = new LinkedList<>();
        solutionHelper(res, currentWorldState);
        return res;
    }

    void solutionHelper(List<WorldState> list, WorldState worldState) {
        if (edgeTo.get(worldState).equals(worldState)) {
            list.add(worldState);
        } else {
            solutionHelper(list, edgeTo.get(worldState));
            list.add(worldState);
        }
    }


}
