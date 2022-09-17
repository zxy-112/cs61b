package creatures;

import huglife.*;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class Clorus extends Creature {
    /**
     * Creates a creature with the name N. The intention is that this
     * name should be shared between all creatures of the same type.
     *
     */
    public Clorus(double e) {
        super("clorus");
        this.energy = e;
    }

    public Clorus() {
        this(1);
    }

    @Override
    public void move() {
        energy -= 0.03;
    }

    @Override
    public void attack(Creature c) {
        energy += c.energy();
    }

    @Override
    public Creature replicate() {
        Clorus res = new Clorus(energy / 2);
        energy = energy / 2;
        return res;
    }

    @Override
    public void stay() {
        energy -= 0.01;
    }

    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        List<Direction> plips = getNeighborsOfType(neighbors, "plip");
        if (empties.size() == 0) {
            return new Action(Action.ActionType.STAY);
        } else if (!plips.isEmpty()) {
            Direction attackDirection = HugLifeUtils.randomEntry(plips);
            return new Action(Action.ActionType.ATTACK, attackDirection);
        } else if (energy > 1) {
            Direction replicateDirection = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.REPLICATE, replicateDirection);
        } else {
            Direction moveDirection = HugLifeUtils.randomEntry(empties);
            return new Action(Action.ActionType.MOVE, moveDirection);
        }
    }

    @Override
    public Color color() {
        return new Color(34, 0, 231);
    }
}
