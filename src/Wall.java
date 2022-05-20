public class Wall {
    public  double width ;
    public  double height;

    public Wall(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double[] x_range() {
        return new double[]{0.0, this.width};
    }

    public double[] y_range() {
        return new double[]{0.0, this.height};
    }
}
