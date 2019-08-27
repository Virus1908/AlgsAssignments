import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private static final int BORDER_ENERGY = 1000;
    private Picture picture;

    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.picture = new Picture(picture);
    }

    public Picture picture() {
        return new Picture(picture);
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public double energy(int col, int row) {
        validateColumnIndex(col);
        validateRowIndex(row);
        if (col == 0 || row == 0 || col == width() - 1 || row == height() - 1) {
            return BORDER_ENERGY;
        }
        return Math.sqrt(delta(picture.get(col - 1, row), picture.get(col + 1, row)) + delta(picture.get(col, row - 1), picture.get(col, row + 1)));
    }

    private double delta(Color colorPrev, Color colorNext) {
        return delta(colorPrev.getRed(), colorNext.getRed()) + delta(colorPrev.getGreen(), colorNext.getGreen()) + delta(colorPrev.getBlue(), colorNext.getBlue());
    }

    private double delta(int valuePrev, int valueNext) {
        return Math.pow(valueNext - valuePrev, 2);
    }

    private void validateRowIndex(int row) {
        if (row < 0 || row >= height())
            throw new IllegalArgumentException();
    }

    private void validateColumnIndex(int col) {
        if (col < 0 || col >= width())
            throw new IllegalArgumentException();
    }

    public int[] findHorizontalSeam() {
        if (width() == 1) {
            return new int[]{0};
        }
        int[][] rowFrom = new int[width() - 2][height()];
        double[][] sumEnergy = new double[width() - 1][height()];
        for (int row = 0; row < height(); row++) {
            sumEnergy[0][row] = BORDER_ENERGY;
        }
        CoordinateEnergyPair rowFromTemp;
        for (int col = 1; col < width() - 1; col++) {
            for (int row = 0; row < height(); row++) {
                rowFromTemp = findBestRow(row, col, sumEnergy);
                rowFrom[col - 1][row] = rowFromTemp.coordinate;
                sumEnergy[col][row] = rowFromTemp.energy + energy(col, row);
            }
        }
        int endRow = 0;
        for (int i = 1; i < height(); i++) {
            if (sumEnergy[width() - 2][i] < sumEnergy[width() - 2][endRow]) {
                endRow = i;
            }
        }
        return buildHorizontalSeamFromRow(rowFrom, endRow);
    }

    private CoordinateEnergyPair findBestRow(int row, int col, double[][] sumEnergy) {
        double fromTopEnergy = row != 0 ? sumEnergy[col - 1][row - 1] : Integer.MAX_VALUE;
        double fromCenterEnergy = sumEnergy[col - 1][row];
        double fromBottomEnergy = (row != height() - 1) ? sumEnergy[col - 1][row + 1] : Integer.MAX_VALUE;
        if (fromTopEnergy < fromCenterEnergy) {
            if (fromTopEnergy < fromBottomEnergy) return new CoordinateEnergyPair(row - 1, fromTopEnergy);
            else return new CoordinateEnergyPair(row + 1, fromBottomEnergy);
        } else {
            if (fromBottomEnergy < fromCenterEnergy) {
                return new CoordinateEnergyPair(row + 1, fromBottomEnergy);
            } else {
                return new CoordinateEnergyPair(row, fromCenterEnergy);
            }
        }
    }

    private int[] buildHorizontalSeamFromRow(int[][] rowFrom, int endRow) {
        int[] seam = new int[width()];
        seam[width() - 1] = endRow;
        seam[width() - 2] = endRow;
        for (int i = width() - 3; i > 0; i--) {
            seam[i] = rowFrom[i][seam[i + 1]];
        }
        seam[0] = seam[1];
        return seam;
    }

    public int[] findVerticalSeam() {
        if (height() == 1) {
            return new int[]{0};
        }
        int[][] colFrom = new int[width()][height() - 2];
        double[][] sumEnergy = new double[width()][height() - 1];
        for (int col = 0; col < width(); col++) {
            sumEnergy[col][0] = BORDER_ENERGY;
        }
        CoordinateEnergyPair colFromTemp;
        for (int row = 1; row < height() - 1; row++) {
            for (int col = 0; col < width(); col++) {
                colFromTemp = findBestCol(row, col, sumEnergy);
                colFrom[col][row - 1] = colFromTemp.coordinate;
                sumEnergy[col][row] = colFromTemp.energy + energy(col, row);
            }
        }
        int endCol = 0;
        for (int i = 1; i < width(); i++) {
            if (sumEnergy[i][height() - 2] < sumEnergy[endCol][height() - 2]) {
                endCol = i;
            }
        }
        return buildVerticalSeamFromCol(colFrom, endCol);
    }

    private CoordinateEnergyPair findBestCol(int row, int col, double[][] sumEnergy) {
        double fromLeftEnergy = col != 0 ? sumEnergy[col - 1][row - 1] : Integer.MAX_VALUE;
        double fromCenterEnergy = sumEnergy[col][row - 1];
        double fromRightEnergy = (col != width() - 1) ? sumEnergy[col + 1][row - 1] : Integer.MAX_VALUE;
        if (fromLeftEnergy < fromCenterEnergy) {
            if (fromLeftEnergy < fromRightEnergy) return new CoordinateEnergyPair(col - 1, fromLeftEnergy);
            else return new CoordinateEnergyPair(col + 1, fromRightEnergy);
        } else {
            if (fromRightEnergy < fromCenterEnergy) {
                return new CoordinateEnergyPair(col + 1, fromRightEnergy);
            } else {
                return new CoordinateEnergyPair(col, fromCenterEnergy);
            }
        }
    }

    private int[] buildVerticalSeamFromCol(int[][] colFrom, int endCol) {
        int[] seam = new int[height()];
        seam[height() - 1] = endCol;
        seam[height() - 2] = endCol;
        for (int i = height() - 3; i > 0; i--) {
            seam[i] = colFrom[seam[i + 1]][i];
        }
        seam[0] = seam[1];
        return seam;
    }

    public void removeHorizontalSeam(int[] seam) {
        validateSeam(seam, width(), height());
        Picture newPicture = new Picture(width(), height() - 1);
        for (int col = 0; col < seam.length; col++) {
            int rowToRemove = seam[col];
            for (int row = 0; row < rowToRemove; row++) {
                newPicture.set(col, row, picture.get(col, row));
            }
            for (int row = rowToRemove; row < height() - 1; row++) {
                newPicture.set(col, row, picture.get(col, row + 1));
            }
        }
        picture = newPicture;
    }

    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam, height(), width());
        Picture newPicture = new Picture(width() - 1, height());
        for (int row = 0; row < seam.length; row++) {
            int colToRemove = seam[row];
            for (int col = 0; col < colToRemove; col++) {
                newPicture.set(col, row, picture.get(col, row));
            }
            for (int col = colToRemove; col < width() - 1; col++) {
                newPicture.set(col, row, picture.get(col + 1, row));
            }
        }
        picture = newPicture;

    }

    private void validateSeam(int[] seam, int length, int bound) {
        if (seam == null || seam.length != length || bound <= 1) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length; i++) {
            if (i != 0 && Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException();
            } else if (seam[i] < 0 || seam[i] >= bound) {
                throw new IllegalArgumentException();
            }
        }
    }

    private static class CoordinateEnergyPair {
        private final int coordinate;
        private final double energy;

        private CoordinateEnergyPair(int coordinate, double energy) {
            this.coordinate = coordinate;
            this.energy = energy;
        }
    }

}
