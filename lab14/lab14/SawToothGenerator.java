package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {

    private int period;
    private int state;
    private double[] value;
    public SawToothGenerator(int period) {
        this.period = period;
        this.state = 0;
        value = new double[period];

        double init = -1;
        double interval = 2. / (period - 1);
        for (int m = 0; m < period; m++) {
            value[m] = init;
            init += interval;
        }
    }

    public double next() {
        double res = normalize(state);
        state = (state + 1) % period;
        return res;
    }

    private double normalize(int number) {
        return value[number];
    }
}
