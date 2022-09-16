import edu.princeton.cs.algs4.Picture;

import java.awt.*;

public class SeamCarver {

    private Picture picture;
    public SeamCarver(Picture picture) {
        this.picture = picture;
    }

    public Picture picture() {
        Picture res = new Picture(width(), height());
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                res.set(x, y, picture.get(x, y));
            }
        }
        return res;
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    /**
     *
     * @param x the col
     * @param y the row
     * @return the energy
     */
    public double energy(int x, int y) {
        Color colorLeft = picture.get((x - 1 + width()) % width(), y);
        Color colorRight = picture.get((x + 1) % width(), y);
        Color colorUp = picture.get(x, (y - 1 + height()) % height());
        Color colorDown = picture.get(x, (y + 1) % height());
        return diffEnergy(colorLeft, colorRight) + diffEnergy(colorUp, colorDown);
    }

    private double diffEnergy(Color c1, Color c2) {
        int r1 = c1.getRed();
        int g1 = c1.getGreen();
        int b1 = c1.getBlue();
        int r2 = c2.getRed();
        int g2 = c2.getGreen();
        int b2 = c2.getBlue();
        return Math.pow(r1 - r2, 2) + Math.pow((g1 - g2), 2) + Math.pow((b1 - b2), 2);
    }

    public int[] findVerticalSeam() {
        double[][] energy = new double[height()][width()];
        double[][] totalEnergy = new double[height()][width()];
        for (int m = 0; m < width(); m += 1) {
            for (int n = 0; n < height(); n += 1) {
                energy[n][m] = energy(m, n);
            }
        }
        System.arraycopy(energy[0], 0, totalEnergy[0], 0, energy[0].length);
        for (int row = 1; row < height(); row += 1) {
            for (int col = 0; col < width(); col += 1) {
                if (col == 0) {
                    double middle = totalEnergy[row - 1][col];
                    double right = totalEnergy[row - 1][col + 1];
                    totalEnergy[row][col] = energy[row][col] + Math.min(middle, right);
                } else if (col == width() - 1){
                    double left = totalEnergy[row - 1][col - 1];
                    double middle = totalEnergy[row - 1][col - 1];
                    totalEnergy[row][col] = energy[row][col] + Math.min(left, middle);
                } else {
                    double left = totalEnergy[row - 1][col - 1];
                    double middle = totalEnergy[row - 1][col];
                    double right = totalEnergy[row - 1][col + 1];
                    totalEnergy[row][col] = energy[row][col] + threeMin(left, middle, right);
                }
            }
        }
        int[] res = new int[height()];
        double minValue = Double.MAX_VALUE;
        for (int m = 0; m < width(); m += 1) {
            double curTotalEnergy = totalEnergy[totalEnergy.length-1][m];
            if (curTotalEnergy < minValue) {
                minValue = curTotalEnergy;
                res[height() - 1] = m;
            }
        }

        int currentRow = height() - 2;
        for (int m = 1; m < height(); m += 1) {
            int nextIndex = res[currentRow + 1];
            if (nextIndex == 0) {
                if (totalEnergy[currentRow][0] < totalEnergy[currentRow][1]) {
                    res[currentRow] = 0;
                } else {
                    res[currentRow] = 1;
                }
            } else if (nextIndex == width() - 1) {
                if (totalEnergy[currentRow][width() - 1] < totalEnergy[currentRow][width() - 2]) {
                    res[currentRow] = width() - 1;
                } else {
                    res[currentRow] = width() - 2;
                }
            } else {
                if (totalEnergy[currentRow][nextIndex - 1] < totalEnergy[currentRow][nextIndex]) {
                    if (totalEnergy[currentRow][nextIndex] < totalEnergy[currentRow][nextIndex + 1]) {
                        res[currentRow] = nextIndex - 1;
                    } else if (totalEnergy[currentRow][nextIndex - 1] < totalEnergy[currentRow][nextIndex + 1]) {
                        res[currentRow] = nextIndex - 1;
                    } else {
                        res[currentRow] = nextIndex + 1;
                    }
                } else {
                    if (totalEnergy[currentRow][nextIndex] < totalEnergy[currentRow][nextIndex + 1]) {
                        res[currentRow] = nextIndex;
                    } else {
                        res[currentRow] = nextIndex + 1;
                    }
                }
            }
            currentRow -= 1;
        }
        return res;
    }

    private static double threeMin(double d1, double d2, double d3) {
        if (d1 > d2) {
            return Math.min(d2, d3);
        } else {
            return Math.min(d1, d3);
        }
    }

    public int[] findHorizontalSeam() {
        transpose();
        int[] res = findVerticalSeam();
        transpose();
        return res;
    }

    private void transpose() {
        int newHeight = width();
        int newWidth = height();
        Picture newPic = new Picture(newWidth, newHeight);
        for (int y = 0; y < newHeight; y += 1) {
            for (int x = 0; x < newWidth; x += 1) {
                newPic.set(x, y, picture.get(y, x));
            }
        }
        picture = newPic;
    }

    public void removeHorizontalSeam(int[] seam) {
        picture = SeamRemover.removeHorizontalSeam(picture, seam);
    }

    public void removeVerticalSeam(int[] seam) {
        picture = SeamRemover.removeVerticalSeam(picture, seam);
    }

    public static void main(String[] args) {
        Picture p = new Picture("./images/10x12.png");
    }

}
