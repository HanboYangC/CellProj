public class Wall {
    public static double width ;
    public static double height;

    public Wall(double width, double height) {
        Wall.width = width;
        Wall.height = height;
    }

    public double[] x_range() {
        return new double[]{0.0, Wall.width};
    }

    public double[] y_range() {
        return new double[]{0.0, Wall.height};
    }
}
