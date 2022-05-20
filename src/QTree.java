import javax.crypto.spec.PSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class QTree {
    public Wall wall;
    public Node root = null;
    public ArrayList<Cell> cells = new ArrayList<Cell>();
//    public HashMap<Cell,Node> cn_link = new HashMap<>();
//    public HashMap<Node, ArrayList<Cell>> nc_link = new HashMap<>();
//    public ArrayList<Node> leaves = new ArrayList<>();

    class Node {
        public Rectangle boundary;
        public boolean divided; // if divided ? node : leaf
        final static private int capacity = 4;
        // relationship with other nodes
        public Node father = null; // null if it is the root
        public ArrayList<Node> brother = null; // null if it is the root

        // node
        public Node ne;
        public Node nw;
        public Node se;
        public Node sw;
        public ArrayList<Node> son = new ArrayList<>(Arrays.asList(ne, nw, se, sw));
        // leaf
        public ArrayList<Cell> cells;
        // TODO : = null as node; = list as leaf (no longer than 4)

        // name : only for test
//        public String name;

        // given rectangle
        public Node(Rectangle rectangle) {
            this.divided = false;
            this.boundary = rectangle;
            this.cells = new ArrayList<>();

//            // only for test
//            set_name();
        }

        // given rectangle and cells
        public Node(Rectangle rectangle, ArrayList<Cell> cells) {
            this(rectangle);
            for (Cell c : cells) {
                insert(c);
            }

        }

        // only for test
//        public void set_name(){
//            if(this.father == null){
//                this.name = "root";
//                return;
//            }
//
//            if(this == this.father.ne){
//                this.name = this.father.name + "_ne";
//            }else if(this == this.father.nw){
//                this.name = this.father.name + "_nw";
//            }else if(this == this.father.se){
//                this.name = this.father.name + "_se";
//            }else {
//                this.name = this.father.name + "_sw";
//            }
//
//        }

//        public void print(){
//            System.out.println(this.name);
//        }

        public Rectangle getBoundary(){
            return this.boundary;
        }

        //  cells would not overlap in the beginning
        public boolean insert(Cell cell) {
            if (cell.check_if_overlapped(wall)) {
                return false;
            }
            if (!this.isContain(cell, false)) {
                return false;
            }
            if (!this.divided && this.cells.size() >= capacity) {
                this.divide();
            }

            if (this.divided) {
                if (this.ne.insert(cell)) {
                    return true;
                } else if (this.se.insert(cell)) {
                    return true;
                } else if (this.nw.insert(cell)) {
                    return true;
                } else if (this.sw.insert(cell)) {
                    return true;
                }
            } else { // this is not divided and have enough space
                this.cells.add(cell);
                cell.node = this; // link cell and node
            }

            return false;
        }

        public boolean divide() {
            if (this.divided)
                return false;
            if (this.cells.size() != capacity) {
                return false;
            }
            ArrayList<Cell> cells = this.cells;
            double centx = this.boundary.x;
            double centy = this.boundary.y;
            double w = this.boundary.w / 2;
            double h = this.boundary.h / 2;

            this.ne = new Node(new Rectangle(centx + w / 2, centy + h / 2, w, h));
            this.se = new Node(new Rectangle(centx + w / 2, centy - h / 2, w, h));
            this.nw = new Node(new Rectangle(centx - w / 2, centy + h / 2, w, h));
            this.sw = new Node(new Rectangle(centx - w / 2, centy - h / 2, w, h));

            this.ne.father = this;
            this.se.father = this;
            this.nw.father = this;
            this.sw.father = this;

            this.ne.brother = new ArrayList<>((Arrays.asList(this.se,this.nw,this.sw)));
            this.se.brother = new ArrayList<>((Arrays.asList(this.ne,this.nw,this.sw)));
            this.nw.brother = new ArrayList<>((Arrays.asList(this.se,this.ne,this.sw)));
            this.sw.brother = new ArrayList<>((Arrays.asList(this.se,this.nw,this.ne)));

            this.son = new ArrayList<>(Arrays.asList(this.ne,this.nw,this.se,this.sw));

            this.divided = true;
            this.cells = null;
            for (Cell cell : cells) {
                this.insert(cell);
            }
            return true;
        }

        // whether a certain cell is contained in this node
        public boolean isContain(Cell cell, boolean critical) {
            return this.boundary.isContainCell(cell, critical);
        }

        // given a range, find the cells which in this node in the range
        public ArrayList<Cell> cellInRange(Rectangle range, boolean critical) {
            ArrayList<Cell> foundCell = new ArrayList<>();
            if (!this.boundary.isOverlap(range)) {
                return foundCell;
            }

            if (!this.divided) {
                for (int i = 0; i < this.cells.size(); i++) {
                    Cell cell = this.cells.get(i);
                    if (range.isContainCell(cell, critical)) {
                        foundCell.add(cell);
                    }
                }
            } else {
                foundCell.addAll(this.ne.cellInRange(range, critical));
                foundCell.addAll(this.se.cellInRange(range, critical));
                foundCell.addAll(this.nw.cellInRange(range, critical));
                foundCell.addAll(this.sw.cellInRange(range, critical));
            }
            return foundCell;
        }
    }

    public static ArrayList<Cell> cellOverlap(ArrayList<Cell> cells, Cell cell) {
        ArrayList<Cell> res = new ArrayList<>();
        for (Cell tmp : cells) {
            if (tmp.check_if_overlapped(cell))
                res.add(tmp);
        }
        return res;
    }

    // construct function 1
    QTree(Rectangle rectangle) {
        Cell.release();
        wall = new Wall( rectangle.w,rectangle.h);
        root = new Node(rectangle);
    }

    // construct function 2
    QTree(Rectangle rectangle, ArrayList<Cell> cellArrayList) {
        Cell.release();
        wall = new Wall(rectangle.h, rectangle.w);
        root = new Node(rectangle, cellArrayList);
        cells = cellArrayList; // actually the number of cells would not change
    }

    // tree's insert
    public void insert(Cell cell) {
        cells.add(cell);
        this.root.insert(cell);
    }

    // for a certain cell, change its place on tree
    public void cellShouldChange(Cell cell) {
//        System.out.println(cell.ID);
        if (cell.node.isContain(cell, false)) {
            return;
        }

        Node n = cell.node; // for upper search

//        if(n.brother==null){
//            System.out.println("this brother is null");
//            System.out.println(cell.ID);
//            return;
//        }
//        System.out.println(n.brother);
        Node rightNode = null; // for lower search
        boolean find = false;

        // find the right node (upwards)
        if(cell.ID==6)
            System.out.println('m');
        System.out.println(cell.ID);

        // find the right node (upwards)
        while (!find) {
            for (Node node : n.brother) {
                if (node.isContain(cell, false)) {
                    find = true;
                    rightNode = node;
                    break;
                }
            }
            n = n.father;
        }

        // find the exact node (downwards)
        while (rightNode.divided) {
            for (Node node : rightNode.son) {
                if (node.isContain(cell, false)) {
                    rightNode = node;
                    break;
                }
            }
        }

        // now that the right node is found
        // 1. remove cell from its original node
        cell.node.cells.remove(cell);
        // 2. add the cell into the right node
        rightNode.insert(cell);


    }


    // other functions like dfs
    public ArrayList<Cell> dfs() {
        return dfs(this.root);
    }

    private ArrayList<Cell> dfs(Node node) {  //纯DFS遍历，返回遍历过的Cell
        ArrayList<Cell> cells_visited = new ArrayList<>();
        if (node.divided) {
            cells_visited.addAll(dfs(node.ne));
            cells_visited.addAll(dfs(node.nw));
            cells_visited.addAll(dfs(node.se));
            cells_visited.addAll(dfs(node.sw));
        } else
            cells_visited.addAll(node.cells);

        return cells_visited;
    }

    public boolean move(Cell cell) {
        Rectangle collisionArea ;
        switch (cell.color){
            case RED :
                collisionArea=new Rectangle(cell.x,cell.y+(0.667+Cell.maxRadius+cell.radius)/2,4*Cell.maxRadius,0.667+Cell.maxRadius+cell.radius);
                break;
            case GREEN:
                collisionArea=new Rectangle(cell.x,cell.y-(0.667+Cell.maxRadius+cell.radius)/2,4*Cell.maxRadius,0.667+Cell.maxRadius+cell.radius);
                break;
            case BLUE:
                collisionArea=new Rectangle(cell.x-(0.667+Cell.maxRadius+cell.radius)/2,cell.y,0.667+Cell.maxRadius+cell.radius,4*Cell.maxRadius);
                break;
            case YELLOW:
                collisionArea=new Rectangle(cell.x+(0.667+Cell.maxRadius+cell.radius)/2,cell.y,0.667+Cell.maxRadius+cell.radius,4*Cell.maxRadius);
                break;
            default:
                collisionArea=null;

        }
        //= new Rectangle(cell.x, cell.y,
        //        (cell.radius + 1 / 15.0 + Cell.maxRadius) * 2, (cell.radius + 1 / 15.0 + Cell.maxRadius) * 2);
        ArrayList<Cell> collision = this.root.cellInRange(collisionArea, true);
        collision.remove(cell);
        cell.move();

        if (cellOverlap(collision, cell).size() != 0) {
            switch (cell.color) {
                case RED:
                    double maxBackY = 0;
                    for (Cell collided : collision) {
                        double idleDistance = cell.radius + collided.radius;
                        double deltax = cell.x - collided.x;
                        double deltay = collided.y - cell.y;//>0
                        double idledy = Math.sqrt(idleDistance * idleDistance - deltax * deltax);
                        double backY = deltay - idledy;//<0
                        if (backY < maxBackY)
                            maxBackY = backY;
                    }
                    maxBackY = (double) Math.round(maxBackY * 10000) / 10000;
                    cell.move(cell.x, (double) Math.round(cell.y * 10000) / 10000 + maxBackY);
                    break;
                case GREEN:
                    double maxForwardY = 0;
                    for (Cell collided : collision) {
                        double idleDistance = cell.radius + collided.radius;
                        double deltax = cell.x - collided.x;
                        double deltay = cell.y - collided.y;//>0
                        double idledy = Math.sqrt(idleDistance * idleDistance - deltax * deltax);
                        double forwardY = idledy - deltay;//>0
                        if (forwardY > maxForwardY)
                            maxForwardY = forwardY;
                    }
                    maxForwardY = (double) Math.round(maxForwardY * 10000) / 10000;
                    cell.move(cell.x, (double) Math.round(cell.y * 10000) / 10000 + maxForwardY);
                    break;
                case BLUE:
                    //move(this.x -= 1.0 / 15.0, this.y);
                    double maxForwardX = 0;
                    for (Cell collided : collision) {
                        double idleDistance = cell.radius + collided.radius;
                        double deltax = cell.x - collided.x;//>0
                        double deltay = cell.y - collided.y;
                        double idledx = Math.sqrt(idleDistance * idleDistance - deltay * deltay);
                        double forwardX = idledx - deltax;//>0
                        if (forwardX > maxForwardX)
                            maxForwardX = forwardX;
                    }
                    maxForwardX = (double) Math.round(maxForwardX * 10000) / 10000;
                    cell.move((double) Math.round(cell.x * 10000) / 10000 + maxForwardX, cell.y);
                    break;
                case YELLOW:
                    //move(this.x += 1.0 / 15.0, this.y);
                    double maxBackX = 0;
                    for (Cell collided : collision) {
                        double idleDistance = cell.radius + collided.radius;
                        double deltax = collided.x - cell.x;//>0
                        double deltay = cell.y - collided.y;
                        double idledx = Math.sqrt(idleDistance * idleDistance - deltay * deltay);
                        double backX = deltax - idledx;//<0
                        if (backX < maxBackX)
                            maxBackX = backX;
                    }
                    maxBackX = (double) Math.round(maxBackX * 10000) / 10000;
                    cell.move((double) Math.round(cell.x * 10000) / 10000 + maxBackX, cell.y);
                    break;
            }
        }

        if(cell.check_if_overlapped(this.wall)){
            switch (cell.color){
                case RED :
                    double backY=this.wall.height-(cell.y+cell.radius);
                    cell.move(cell.x,cell.y+backY);
                    break;
                case GREEN:
                    double forwardY=-(cell.y-cell.radius);
                    cell.move(cell.x,cell.y+forwardY);
                    break;
                case BLUE:
                    double forwardX=0-(cell.x-cell.radius);
                    cell.move(cell.x+forwardX,cell.y);
                    break;
                case YELLOW:
                    double backX=this.wall.width-(cell.x+cell.radius);
                    cell.move(cell.x+backX,cell.y);
                    break;
            }
        }

        return true;
    }

    public void moveOneStep() {
        for (Cell cell : this.cells) {
            move(cell);
            cellShouldChange(cell);
        }
        detect_and_set_color(root);
    }

    public void simple_test_output() {
        ArrayList<Cell> cells_visited = dfs(root);
        for (Cell tmp : cells)
            System.out.println(tmp.x + "," + tmp.y);
    }

//    public void detect_and_set_color(Cell cell, Node root) {
//        ArrayList<Cell> detected_cells = root.cellInRange(cell.perception_rectangle, false);
//        Cell.Color[] colors_changed = cell.count_detected_cells(detected_cells);
//        cell.setColor(colors_changed);
//    }

    public void detect_and_set_color(Node root){
        for (Cell cell : cells) {
            cell.perception_cells = root.cellInRange(cell.perception_rectangle, false);
            cell.perception_cells.remove(cell);
            cell.perception_colors = cell.count_detected_cells(cell.perception_cells);
//            if (cell.perception_colors.length>=7&&cell.perception_colors[0]!=cell.perception_colors[4]&&cell.perception_colors[4]!= cell.perception_colors[7])
//                System.out.println(cell.perception_colors);
        }
        for (Cell cell : cells){
            cell.setColor(cell.perception_colors);
        }
    }

    public void color_test_output() {
        for (Cell tmp : cells) {
            System.out.println(tmp.color);
        }
        detect_and_set_color(root);
        for (Cell tmp : cells) {
            System.out.println(tmp.color);
        }
//        ArrayList<Cell> cells_visited = dfs(root);
//        for (Cell tmp : cells) {
//            System.out.println(tmp.color);
//            detect_and_set_color(tmp, root);
//            System.out.println(tmp.color);
//        }
//        System.out.println("----------");
//        for (Cell tmp : cells) {
//            detect_and_set_color(tmp, root);
//            System.out.println(tmp.color);
//        }
//        System.out.println("----------");
//        for (Cell tmp : cells) {
//            Cell.queryID(4).change_color(Cell.Color.YELLOW);
//            detect_and_set_color(tmp, root);
//        }
//        for (Cell tmp : cells) {
//            System.out.println(tmp.color);
//        }
//        System.out.println("----------");
    }

    public void move_test_output(){
        //move(root);
        detect_and_set_color(root);

        for (Cell tmp: cells){
            tmp.print_basic_info();
        }
    }

    public static void main(String[] args) {
        QTree qTree = new QTree(new Rectangle(0, 0, 30, 30));
        qTree.insert(new Cell(0, 0, 1, 5, 'r'));
        qTree.insert(new Cell(0, 2, 1, 5, 'b'));
        qTree.insert(new Cell(-2, 2, 1, 5, 'b'));
        qTree.insert(new Cell(-2, 0, 1, 5, 'b'));


        for (Cell cell : qTree.cells) {
            System.out.println(cell.x + "," + cell.y);
        }
        qTree.moveOneStep();
        System.out.println("----------------------");
        for (Cell cell : qTree.cells) {
            System.out.println(cell.x + "," + cell.y);
        }
    }

}
