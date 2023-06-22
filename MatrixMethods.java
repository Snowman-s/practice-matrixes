public class MatrixMethods {
  private MatrixMethods() {

  }

  public static double[][] identity(int size) {
    double[][] ret = new double[size][size];

    for (int i = 0; i < size; i++) {
      ret[i][i] = 1.0;
    }

    return ret;
  }

  public static void add(int size, double[] aModified, double bFactor, double b[]) {
    for (int i = 0; i < size; i++) {
      aModified[i] += bFactor * b[i];
    }
  }

  public static void norm(int size, double[] aModified) {
    double norm = 0;
    for (int i = 0; i < size; i++) {
      norm += aModified[i] * aModified[i];
    }
    norm = Math.sqrt(norm);
    for (int i = 0; i < size; i++) {
      aModified[i] /= norm;
    }
  }

  public static double innerProduct(int size, double[] a, double[] b) {
    double ret = 0;

    for (int i = 0; i < size; i++) {
      ret += a[i] * b[i];
    }

    return ret;
  }

  public static double[][] dot(int size, double[][] a, double[][] b) {
    double[][] ret = new double[size][size];
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        double sum = 0;
        for (int col2 = 0; col2 < size; col2++) {
          sum += a[row][col2] * b[col2][col];
        }
        ret[row][col] = sum;
      }
    }

    return ret;
  }

  public static double[][] t(int size, double[][] a) {
    double[][] ret = new double[size][size];
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        ret[col][row] = a[row][col];
      }
    }

    return ret;
  }

  public static double[][] copy(int size, double[][] a) {
    double[][] ret = new double[size][size];
    for (int row = 0; row < size; row++) {
      for (int col = 0; col < size; col++) {
        ret[row][col] = a[row][col];
      }
    }

    return ret;
  }
}
