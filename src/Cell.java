import java.util.ArrayList;

public class Cell {
    enum Color {
        RED, GREEN, BLUE, YELLOW;
    }

    public static int num = 0;  // total number of cells
    public static ArrayList<Cell> cells = new ArrayList<Cell>();
    public static double maxRadius = 0;
    public static double step = 1.0 / 0.15;  // default move step

    public static void release() {
        num = 0;
        cells.clear();
    }

    // Variables & its default value
    public int ID = num;  //  default: from 0 to num-1
    //    public Color color = Color.RED;

    // Node :
    public QTree.Node node;

    public Color color;
    public Color[] perception_colors;
    public ArrayList<Cell> perception_cells;
    public double radius = 0.0;
    public double x = 0.0;
    public double y = 0.0;
    public double perception_range = 0.1;  // perception range
    //public double[] position = {this.x, this.y};  // position = (x, y)
    public boolean hit_others = false; // move : stop if hits other cells
    public boolean hit_wall = false;
    public Rectangle perception_rectangle;

    public Cell() {

        if (this.radius > maxRadius)
            maxRadius = this.radius;
        cells.add(this);
        Cell.num++;
    }  // default instantiate

    public Cell(double radius, int identity, Color color) {
        //this.position = position;
        this.ID = identity;
        this.color = color;
        this.radius = radius;
        if (this.radius > maxRadius)
            maxRadius = this.radius;
        cells.add(this);
        Cell.num++;
    }

    // instantiate using standard input (pdf P5)
    public Cell(double x, double y, double radius, double perception_range, char c) {
        this.x = x;
        this.y = y;
        //this.position = new double[]{this.x, this.y};
        this.radius = radius;
        if (QuadTree.cellOverlap(Cell.cells, this).size() != 0)
            return;
        this.perception_range = perception_range;
        this.perception_rectangle = new Rectangle(x, y, 2 * perception_range, 2 * perception_range);

        switch (c) {
            case 'r':
                this.color = Color.RED;
                break;
            case 'g':
                this.color = Color.GREEN;
                break;
            case 'b':
                this.color = Color.BLUE;
                break;
            case 'y':
                this.color = Color.YELLOW;
        }
//        this.ID = num;
        if (this.radius > maxRadius) {
            maxRadius = this.radius;
        }

        cells.add(this);
        Cell.num++;
    }


//    Check if overlapped, with wall or another cell

    public boolean check_if_overlapped(Wall wall) {
        if (this.x < this.radius || this.y < this.radius || this.x > wall.width - this.radius || this.y > wall.height - this.radius)
            return true;
        return false;
    }  //Check the cell and the wall, true for overlapped

    public boolean check_if_overlapped(Cell b) {
        double pow2dist = Math.sqrt(Math.pow((this.y - b.y), 2) + Math.pow((this.x - b.x), 2));
        double pow2body = this.radius + b.radius;
        this.hit_others = pow2body > pow2dist;
        return pow2body > pow2dist;
    }  //Check the cell a and the cell b, true for overlapped

//    Move the cell at every step

    public void move(double x, double y) {
        this.x = x;
        this.y = y;
        //this.position = new double[]{x, y};
    }

    public void move(double[] position) {
        //this.position = position;
        this.x = position[0];
        this.y = position[1];
    }

    public boolean move() {
        // when hit others, stop
        /*if (hit_others || hit_wall) {
            return false;
        }*/
        switch (this.color) {
            case RED:
                move(this.x, this.y += 0.0667);
                break;
            case GREEN:
                move(this.x, this.y -= 0.0667);
                break;
            case BLUE:
                move(this.x -= 0.0667, this.y);
                break;
            case YELLOW:
                move(this.x += 0.0667, this.y);
                break;
        }
        return true;
    }

    // Find and add up all the cells in the perception range
    public int[] perception() {
        int red = 0;
        int green = 0;
        int blue = 0;
        int yellow = 0;

        //

        return new int[]{red, green, blue, yellow};
    }

    public void change_color(Color color_changed) {
        this.color = color_changed;
    }

    public char getColorChar() {
        switch (this.color) {
            case RED:
                return 'r';
            case GREEN:
                return 'g';
            case BLUE:
                return 'b';
            case YELLOW:
                return 'y';
        }
        System.out.println("Invalid color");
        return '#';
    }

    // input : Color[] perception_color
    // color of cells in this.cell's perception range
    // not include the cell itself
    public void setColor(Color[] perception_color) {
        int[] count_color = new int[4]; // r,g,b,y
        for (Color col : perception_color) {
            switch (col) {
                case RED:
                    count_color[0] = count_color[0] + 1;
                    break;
                case GREEN:
                    count_color[1] = count_color[1] + 1;
                    break;
                case BLUE:
                    count_color[2] = count_color[2] + 1;
                    break;
                case YELLOW:
                    count_color[3] = count_color[3] + 1;
                    break;
            }
        }
        double[] prop_color = new double[4];
        for (int i = 0; i < prop_color.length; i++) {
            prop_color[i] = (double) count_color[i] / perception_color.length;
        }

        // 0-red; 1-green; 2-blue; 3-yellow
        switch (this.color) {
            case RED:
                if (count_color[0] >= 3 & prop_color[0] > 0.7) {
//                    this.color_before = Color.GREEN;
                    change_color(Color.GREEN);
                } else if (count_color[3] >= 1 & prop_color[3] < 0.1) {
//                    this.color_before = Color.YELLOW;
                    change_color(Color.YELLOW);
                }
                break;
            case GREEN:
                if (count_color[1] >= 3 & prop_color[1] > 0.7) {
//                    this.color_before = Color.BLUE;
                    change_color(Color.BLUE);
                } else if (count_color[0] >= 1 & prop_color[0] < 0.1) {
//                    this.color_before = Color.RED;
                    change_color(Color.RED);
                }
                break;
            case BLUE:
                if (count_color[2] >= 3 & prop_color[2] > 0.7) {
//                    this.color_before = Color.YELLOW;
                    change_color(Color.YELLOW);
                } else if (count_color[1] >= 1 & prop_color[1] < 0.1) {
//                    this.color_before = Color.GREEN;
                    change_color(Color.GREEN);
                }
                break;
            case YELLOW:
                if (count_color[3] >= 3 & prop_color[3] > 0.7) {
//                    this.color_before = Color.RED;
                    change_color(Color.RED);
                } else if (count_color[2] >= 1 & prop_color[2] < 0.1) {
//                    this.color_before = Color.BLUE;
                    change_color(Color.BLUE);
                }
                break;
        }
    }

    public void print_basic_info() {
        System.out.println("----------Cell Basic Info-----------");
        System.out.printf("Cell ID: %d\n", ID);
        System.out.printf("Position: %f, %f\n", x, y);
        System.out.printf("Color: %s\n", color.toString());
        System.out.printf("Perception: %f; Radius: %f\n", perception_range, radius);
        System.out.println("----------Cell Basic Info-----------");
    }

    public String standard_output() {
        return this.x + " " + this.y + " " + this.color.toString().substring(0, 1).toLowerCase();
    }

    public static Cell queryID(int num) {
        return cells.get(num);
    }

    public Color[] count_detected_cells(ArrayList<Cell> detected_cells) {
        Color[] colors = new Color[detected_cells.size()];
        for (int i = 0; i < detected_cells.size(); i++)
            colors[i] = detected_cells.get(i).color;
        return colors;
    }

}
