public class NBody {

    public static double readRadius(String fileName) {
        In in = new In(fileName);
        in.readInt();
        return in.readDouble();
    }

    public static Planet[] readPlanets(String fileName) {
        In in = new In(fileName);
        int num = in.readInt();
        in.readDouble();
        Planet[] planets = new Planet[num];
        for (int i=0; i<num; i++) {
            planets[i] = new Planet(in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readDouble(), in.readString());
        }
        return planets;
    }

    public static void main(String[] args) {
        double T = Double.parseDouble(args[0]);
        double dt = Double.parseDouble(args[1]);
        String filename = args[2];

        double radius = NBody.readRadius(filename);
        Planet[] planets = NBody.readPlanets(filename);

        StdDraw.setScale(-radius, radius);
        StdDraw.picture(0, 0, "./images/starfield.jpg", 2*radius, 2*radius);
        for (Planet planet: planets) {
            planet.draw();
        }

        StdDraw.enableDoubleBuffering();
        double t = 0;
        while(t < T) {
            double[] xForces = new double[planets.length];
            double[] yForces = new double[planets.length];
            for (int i=0; i<planets.length; i++) {
                xForces[i] = planets[i].calcNetForceExertedByX(planets);
                yForces[i] = planets[i].calcNetForceExertedByY(planets);
            }
            for (int i=0; i<planets.length; i++) {
                planets[i].update(dt, xForces[i], yForces[i]);
            }
            StdDraw.picture(0, 0, "./images/starfield.jpg", 2*radius, 2*radius);
            for (Planet planet: planets) {
                planet.draw();
            }
            StdDraw.show();
            StdDraw.pause(10);
            t = t + dt;
        }
        StdOut.printf("%d\n", planets.length);
        StdOut.printf("%.2e\n", radius);
        for (int i = 0; i < planets.length; i++) {
        StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
                  planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
                  planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
}
    }
}
