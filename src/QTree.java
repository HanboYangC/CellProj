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
        public ArrayList<Node> son = new ArrayList<>(Arrays.asList(ne,nw,se,sw));
        // leaf
        private ArrayList<Cell> cells;
        // TODO : = null as node; = list as leaf (no longer than 4)

        // given rectangle
        public Node(Rectangle rectangle) {
            this.divided = false;
            this.boundary = rectangle;
            this.cells = new ArrayList<>();
        }

        // given rectangle and cells
        public Node(Rectangle rectangle, ArrayList<Cell> cells) {
            this(rectangle);
            for (Cell c : cells) {
                insert(c);
            }
        }


        //  cells would not overlap in the beginning
        public boolean insert(Cell cell) {
            if (cell.check_if_overlapped(wall)) {
                return false;
            }
            if (!this.isContain(cell)) {
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
        public boolean isContain(Cell cell) {
            return this.boundary.isContainCell(cell);
        }

        // given a range, find the cells which in this node in the range
        public ArrayList<Cell> cellInRange(Rectangle range) {
            ArrayList<Cell> foundCell = new ArrayList<>();
            if (!this.boundary.isOverlap(range)) {
                return foundCell;
            }

            if (!this.divided) {
                for (int i = 0; i < this.cells.size(); i++) {
                    Cell cell = this.cells.get(i);
                    if (range.isContainCell(cell)) {
                        foundCell.add(cell);
                    }
                }
            } else {
                foundCell.addAll(this.ne.cellInRange(range));
                foundCell.addAll(this.se.cellInRange(range));
                foundCell.addAll(this.nw.cellInRange(range));
                foundCell.addAll(this.sw.cellInRange(range));
            }
            return foundCell;
        }

    }

    public static ArrayList<Cell> cellOverlap(ArrayList<Cell> cells, Cell cell) {
        ArrayList<Cell> res = new ArrayList<>();
        for (Cell tmp : cells) {
            if (tmp.check_if_overlapped(tmp, cell))
                res.add(tmp);
        }
        return res;
    }

    // construct function 1
    QTree(Rectangle rectangle) {
        wall = new Wall(rectangle.h, rectangle.w);
        root = new Node(rectangle);
    }

    // construct function 2
    QTree(Rectangle rectangle, ArrayList<Cell> cellArrayList) {
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
    public void CellShouldChange(Cell cell){
        if(cell.node.isContain(cell)){
            return;
        }

        // remove cell from its original node
        cell.node.cells.remove(cell);

        Node n = cell.node;
        Node rightNode;
        boolean find = false;

        while (!find){
            for(Node node : n.brother){
                if(node.isContain(cell)){
                    find = true;
                    break;
                }
            }
            n = n.father;
        }
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

    public boolean move(Node node) {
        if(node==null)
            return false;
        if (node.divided) {
            move(node.ne);
            move(node.nw);
            move(node.se);
            move(node.sw);
        } else {
            for (Cell cell : node.cells) {
                Rectangle collisionArea = new Rectangle(cell.x, cell.y, (cell.radius + 1 / 15) * 2, (cell.radius + 1 / 15) * 2);
                ArrayList<Cell> collision = this.root.cellInRange(collisionArea);
                cell.move();
                if (cellOverlap(collision, cell).size() != 0) {
                    switch (cell.color) {
                        case RED:
                            double maxBackY=0;
                            for (Cell collided : collision) {
                                double idleDistance=cell.radius+collided.radius;
                                double deltax=cell.x-collided.x;
                                double deltay=collided.y-cell.y;//>0
                                double idledy=Math.sqrt(idleDistance * idleDistance - deltax * deltax);
                                double backY=deltay-idledy;//<0
                                if(backY<maxBackY)
                                    maxBackY=backY;
                            }
                            cell.move(cell.x,cell.y+maxBackY);
                        case GREEN:
                            double maxForwardY=0;
                            for (Cell collided : collision) {
                                double idleDistance=cell.radius+collided.radius;
                                double deltax=cell.x-collided.x;
                                double deltay=cell.y-collided.y;//>0
                                double idledy=Math.sqrt(idleDistance * idleDistance - deltax * deltax);
                                double forwardY=idledy-deltay;//>0
                                if(forwardY>maxForwardY)
                                    maxForwardY=forwardY;
                            }
                            cell.move(cell.x,cell.y+maxForwardY);
                        case BLUE:
                            //move(this.x -= 1.0 / 15.0, this.y);
                            double maxForwardX=0;
                            for (Cell collided : collision) {
                                double idleDistance=cell.radius+collided.radius;
                                double deltax=cell.x-collided.x;//>0
                                double deltay=cell.y-collided.y;
                                double idledx=Math.sqrt(idleDistance * idleDistance - deltay * deltay);
                                double forwardX=idledx-deltax;//>0
                                if(forwardX>maxForwardX)
                                    maxForwardX=forwardX;
                            }
                            cell.move(cell.x+maxForwardX,cell.y);
                        case YELLOW:
                            //move(this.x += 1.0 / 15.0, this.y);
                            double maxBackX=0;
                            for (Cell collided : collision) {
                                double idleDistance=cell.radius+collided.radius;
                                double deltax=collided.x-cell.x;//>0
                                double deltay=cell.y-collided.y;
                                double idledx=Math.sqrt(idleDistance * idleDistance - deltay * deltay);
                                double backX=deltax-idledx;//<0
                                if(backX<maxBackX)
                                    maxBackX=backX;
                            }
                            cell.move(cell.x+maxBackX,cell.y);
                    }
                }
            }
        }
        return true;
    }
    public void simple_test_output(){
        ArrayList<Cell> cells_visited = dfs(root);
        for (Cell tmp: cells)
            System.out.println(Arrays.toString(tmp.position));
    }

    public void detect_and_set_color(Cell cell, Node root) {
        ArrayList<Cell> detected_cells = root.cellInRange(cell.perception_rectangle);
        Cell.Color[] colors_changed = cell.count_detected_cells(detected_cells);
        cell.setColor(colors_changed);
    }

    public void color_test_output() {
        ArrayList<Cell> cells_visited = dfs(root);
        for (Cell tmp : cells) {
            System.out.println(tmp.color);
            detect_and_set_color(tmp, root);
            System.out.println(tmp.color);
        }
        System.out.println("----------");
        for (Cell tmp:cells){
            detect_and_set_color(tmp, root);
            System.out.println(tmp.color);
        }
        System.out.println("----------");
        for (Cell tmp:cells){
            Cell.queryID(4).change_color(Cell.Color.YELLOW);
            detect_and_set_color(tmp, root);
            System.out.println(tmp.color);
        }
        System.out.println("----------");
    }
}
