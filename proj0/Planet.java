public class Planet {

    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    public static final double G = 6.67e-11;

    public Planet(double xxPos, double yyPos, double xxVel, double yyVel, double mass, String imgFileName) {
        this.xxPos = xxPos;
        this.yyPos = yyPos;
        this.xxVel = xxVel;
        this.yyVel = yyVel;
        this.mass = mass;
        this.imgFileName = imgFileName;
    }

    public Planet(Planet p) {
        this.xxPos = p.xxPos;
        this.yyPos = p.yyPos;
        this.xxVel = p.xxVel;
        this.yyVel = p.yyVel;
        this.mass = p.mass;
        this.imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p) {
        double dx = p.xxPos - this.xxPos;
        double dy = p.yyPos - this.yyPos;
        double rSqare = dx * dx + dy * dy;
        return Math.sqrt(rSqare);
    }

    public double calcForceExertedBy(Planet p) {
        double distance = this.calcDistance(p);
        return Planet.G * this.mass * p.mass / distance / distance;
    }

    public double calcForceExertedByX(Planet p) {
        double dx = p.xxPos - this.xxPos;
        return this.calcForceExertedBy(p) * dx / this.calcDistance(p);
    }

    public double calcForceExertedByY(Planet p) {
        double dy = p.yyPos - this.yyPos;
        return this.calcForceExertedBy(p) * dy / this.calcDistance(p);
    }

    public double calcNetForceExertedByX(Planet[] planets) {
        double res = 0;
        for (Planet planet: planets) {
            if (planet.equals(this)) {
                continue;
            }
            res = res + this.calcForceExertedByX(planet);
        }
        return res;
    }

    public double calcNetForceExertedByY(Planet[] planets) {
        double res = 0;
        for (Planet planet: planets) {
            if (planet.equals(this)) {
                continue;
            }
            res = res + this.calcForceExertedByY(planet);
        }
        return res;
    }

    public void update(double dt, double forceX, double forceY) {
        double accX = forceX / this.mass;
        double accY = forceY / this.mass;
        this.xxVel = this.xxVel + dt * accX;
        this.yyVel = this.yyVel + dt * accY;
        this.xxPos = this.xxPos + this.xxVel * dt;
        this.yyPos = this.yyPos + this.yyVel * dt;
    }

    public void draw() {
        StdDraw.picture(this.xxPos, this.yyPos, "./images/"+this.imgFileName);
    }
}
