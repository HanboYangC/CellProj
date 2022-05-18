import java.util.ArrayList;

public class QuadTree {
    public static Wall wall = new Wall(600, 700);
    public static int capacity = 4;

    public boolean divided;
    private ArrayList<Cell> cells;
    public Rectangle boundary;
    private QuadTree ne;
    private QuadTree nw;
    private QuadTree se;
    private QuadTree sw;


    public QuadTree(Rectangle rectangle) {
        this.divided = false;
        this.cells = new ArrayList();
        this.boundary = rectangle;

    }

    public QuadTree(Rectangle rectangle, ArrayList<Cell> cells) {
        this(rectangle);
        this.cells = cells;
    }

    public boolean insert(Cell cell) {
        if (cell.check_if_overlapped(wall)) {
            return false;
        }
        if (!this.isContain(cell)) {
            return false;
        }
        if (this.cells.size() < capacity) {
            this.cells.add(cell);
            return true;
        }

        if (!this.divided && this.cells.size() >= QuadTree.capacity) {
            this.divide();
        } else if (this.divided) {
            if (this.ne.insert(cell)) {
                return true;
            } else if (this.se.insert(cell)) {
                return true;
            } else if (this.nw.insert(cell)) {
                return true;
            } else if (this.sw.insert(cell)) {
                return true;
            }
        } else {
            this.cells.add(cell);
        }

        return false;
    }

    public boolean divide() {
        if (this.divided)
            return false;
        if (this.cells.size() != QuadTree.capacity) {
            return false;
        }
        ArrayList<Cell> cells = this.cells;
        double centx = this.boundary.x;
        double centy = this.boundary.y;
        double w = this.boundary.w / 2;
        double h = this.boundary.h / 2;

        this.ne = new QuadTree(new Rectangle(centx + w / 2, centy + h / 2, w, h));
        this.se = new QuadTree(new Rectangle(centx + w / 2, centy - h / 2, w, h));
        this.nw = new QuadTree(new Rectangle(centx - w / 2, centy + h / 2, w, h));
        this.sw = new QuadTree(new Rectangle(centx - w / 2, centy - h / 2, w, h));

        this.divided = true;
        this.cells = null;
        for (Cell cell : cells) {
            this.insert(cell);
        }
        return true;
    }

    public boolean isContain(Cell cell) {
        if (cell.x >= this.boundary.x - this.boundary.w / 2
                && cell.x < this.boundary.x + this.boundary.w / 2
                && cell.y >= this.boundary.y - this.boundary.h / 2
                && cell.y < this.boundary.y + this.boundary.h / 2) {
            return true;
        }
        return false;
    }

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

    public ArrayList<Cell> cellOverlap(ArrayList<Cell> cells, Cell cell) {
        ArrayList<Cell> res = new ArrayList<>();
        for (Cell tmp : cells) {
            if (tmp.check_if_overlapped(tmp, cell))
                res.add(tmp);
        }
        return res;
    }

    public ArrayList<Cell> dfs(QuadTree root, Cell cell) {
        ArrayList<Cell> cells_res = new ArrayList<>();
        for (QuadTree quadtree = root; quadtree != null; ) {
            quadtree = quadtree.nw;
            cells_res.addAll(cellOverlap(quadtree.cells, cell));
            cells_res.addAll(dfs(quadtree, cell));

            quadtree = quadtree.ne;
            cells_res.addAll(cellOverlap(quadtree.cells, cell));
            cells_res.addAll(dfs(quadtree, cell));

            quadtree = quadtree.sw;
            cells_res.addAll(cellOverlap(quadtree.cells, cell));
            cells_res.addAll(dfs(quadtree, cell));

            quadtree = quadtree.se;
            cells_res.addAll(cellOverlap(quadtree.cells, cell));
            cells_res.addAll(dfs(quadtree, cell));
        }
        return cells_res;
    }

    public ArrayList<Cell> dfs(QuadTree root) {  //纯DFS遍历，返回遍历过的Cell
        ArrayList<Cell> cells_visited = new ArrayList<>();
        for (QuadTree quadtree = root; quadtree != null; ) {
            quadtree = quadtree.nw;
            cells_visited.addAll(quadtree.cells);
            dfs(quadtree);
            quadtree = quadtree.ne;
            cells_visited.addAll(quadtree.cells);
            dfs(quadtree);
            quadtree = quadtree.sw;
            cells_visited.addAll(quadtree.cells);
            dfs(quadtree);
            quadtree = quadtree.se;
            cells_visited.addAll(quadtree.cells);
            dfs(quadtree);
        }
        return cells_visited;
    }



}