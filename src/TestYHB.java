public class TestYHB {
    public static void main(String[] args) {
        QuadTree quadTree = new QuadTree(new Rectangle(0, 0, 10, 10));
        Cell cell0 = new Cell();
        quadTree.insert(cell0);
        System.out.println(Cell.cells);
        Cell cell1 = new Cell();
        quadTree.insert(cell1);
        System.out.println(Cell.cells);

    }
}
