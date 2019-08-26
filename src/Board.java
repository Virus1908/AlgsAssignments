import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int[][] tiles;

    public Board(int[][] tiles) {
        this.tiles = copyTiles(tiles);
    }

    private static int[][] copyTiles(int[][] tiles) {
        int[][] tilesCopy = new int[tiles.length][tiles.length];
        for (int i = 0; i < tiles.length; i++) {
            tilesCopy[i] = Arrays.copyOf(tiles[i], tiles.length);
        }
        return tilesCopy;
    }

    public String toString() {
        int n = tiles.length;
        StringBuilder sb = new StringBuilder();
        sb.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(" ").append(tiles[i][j]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public int dimension() {
        return tiles.length;
    }

    public int hamming() {
        int goalNumber = 1;
        int hamming = -1;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] != goalNumber) {
                    hamming++;
                }
                goalNumber++;
            }
        }
        return hamming;
    }

    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                manhattan += horizontalDistance(tiles[i][j], i) + verticalDistance(tiles[i][j], j);
            }
        }
        return manhattan;
    }

    private int horizontalDistance(int tile, int i) {
        if (tile == 0) {
            return 0;
        }
        int tileRow = (tile - 1) / tiles.length;
        return Math.abs(tileRow - i);
    }

    private int verticalDistance(int tile, int j) {
        if (tile == 0) {
            return 0;
        }
        int tileCol = (tile - 1) % tiles.length;
        return Math.abs(tileCol - j);
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public boolean equals(Object y) {
        if (y != null && y.getClass() == Board.class) {
            if (((Board) y).tiles.length == this.tiles.length) {
                for (int i = 0; i < tiles.length; i++) {
                    for (int j = 0; j < tiles.length; j++) {
                        if (((Board) y).tiles[i][j] != tiles[i][j]) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] == 0) {
                    if (i != 0) {
                        neighbors.add(createBoardWithExchange(i, j, i - 1, j));
                    }
                    if (i != tiles.length - 1) {
                        neighbors.add(createBoardWithExchange(i, j, i + 1, j));
                    }
                    if (j != 0) {
                        neighbors.add(createBoardWithExchange(i, j, i, j - 1));
                    }
                    if (j != tiles.length - 1) {
                        neighbors.add(createBoardWithExchange(i, j, i, j + 1));
                    }
                }
            }
        }
        return neighbors;
    }

    private Board createBoardWithExchange(int i, int j, int i1, int j1) {
        int[][] tilesCopy = copyTiles(tiles);
        tilesCopy[i][j] = tiles[i1][j1];
        tilesCopy[i1][j1] = tiles[i][j];
        return new Board(tilesCopy);
    }

    public Board twin() {
        int i1 = -1;
        int j1 = -1;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] != 0) {
                    if (i1 == -1) {
                        i1 = i;
                        j1 = j;
                    } else {
                        return createBoardWithExchange(i, j, i1, j1);
                    }
                }
            }
        }
        return null;
    }
}
