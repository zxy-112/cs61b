package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {

    private int period;
    private double factor;
    private int state;
    private double interval;
    public AcceleratingSawToothGenerator(int period, double factor) {
        this.period = period;
        this.factor = factor;
        this.state = 0;
        this.interval = 2. / (period - 1);
    }

    public double next() {
        double res = normalize(state);
        if (state + 1 == period) {
            state = 0;
            period = (int) (period * factor);
            interval = 2. / (period - 1);
        } else {
            state += 1;
        }
        return res;
    }

    private double normalize(int number) {
        return -1 + number * interval;
    }
}
