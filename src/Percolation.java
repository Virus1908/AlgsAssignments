import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF unionFind;
    private final WeightedQuickUnionUF onlyStartUnionFind;
    private final boolean[] siteStatus;
    private int openSites;
    private final int size;

    public Percolation(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be at least 1");
        }
        this.size = size;
        openSites = 0;
        unionFind = new WeightedQuickUnionUF(size * size + 2);
        onlyStartUnionFind = new WeightedQuickUnionUF(size * size + 1);
        siteStatus = new boolean[size * size];
        for (int i = 0; i < size; i++) {
            unionFind.union(size * size, mapRowAndColumnToIndex(1, i + 1));
            onlyStartUnionFind.union(size * size, mapRowAndColumnToIndex(1, i + 1));
            unionFind.union(size * size + 1, mapRowAndColumnToIndex(size, i + 1));
        }
    }

    private int mapRowAndColumnToIndex(int row, int col) {
        return (row - 1) * size + (col - 1);
    }

    public void open(int row, int col) {
        validate(row, col);
        if (isOpen(row, col)) {
            return;
        }
        int siteIndex = mapRowAndColumnToIndex(row, col);
        if (row > 1 && isOpen(row - 1, col)) {
            unionFind.union(siteIndex, mapRowAndColumnToIndex(row - 1, col));
            onlyStartUnionFind.union(siteIndex, mapRowAndColumnToIndex(row - 1, col));
        }
        if (col > 1 && isOpen(row, col - 1)) {
            unionFind.union(siteIndex, mapRowAndColumnToIndex(row, col - 1));
            onlyStartUnionFind.union(siteIndex, mapRowAndColumnToIndex(row, col - 1));
        }
        if (row < size && isOpen(row + 1, col)) {
            unionFind.union(siteIndex, mapRowAndColumnToIndex(row + 1, col));
            onlyStartUnionFind.union(siteIndex, mapRowAndColumnToIndex(row + 1, col));
        }
        if (col < size && isOpen(row, col + 1)) {
            unionFind.union(siteIndex, mapRowAndColumnToIndex(row, col + 1));
            onlyStartUnionFind.union(siteIndex, mapRowAndColumnToIndex(row, col + 1));
        }
        openSites++;
        siteStatus[siteIndex] = true;
    }

    private void validate(int row, int col) {
        validate(row);
        validate(col);
    }

    private void validate(int single) {
        if (single < 1 || single > size)
            throw new IllegalArgumentException("wrong row or column");
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);
        return siteStatus[mapRowAndColumnToIndex(row, col)];
    }

    public boolean isFull(int row, int col) {
        validate(row, col);
        return isOpen(row, col) && onlyStartUnionFind.connected(size * size, mapRowAndColumnToIndex(row, col));
    }

    public int numberOfOpenSites() {
        return openSites;
    }

    public boolean percolates() {
        if (size == 1) {
            return siteStatus[0];
        }
        return unionFind.connected(size * size, size * size + 1);
    }

}