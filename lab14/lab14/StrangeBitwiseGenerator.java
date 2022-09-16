package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {

    private int period;
    private int state;
    private double interval;
    public StrangeBitwiseGenerator(int period) {
        this.period = period;
        this.state = 0;
        interval = 2. / (period - 1);
    }

    public double next() {
        state = (state + 1);
        int res = (state & (state >>> 3)) % period;
        return normalize(res);
    }

    private double normalize(int number) {
        return -1. + number * interval;
    }
}
