import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Tests {
  private Tests() {

  }

  public static List<Matrix> create3x3MatrixesDeterminant() {
    return List.of(
        // 問17 (1)
        new Matrix(new double[][] {
            new double[] { -1, 2, 3 },
            new double[] { 4, 0, 2 },
            new double[] { -2, 3, -4 }
        }),
        // だって行列が正則じゃないので
        new Matrix(new double[][] {
            new double[] { 1, 2, 3 },
            new double[] { 4, 5, 6 },
            new double[] { 7, 8, 9 }
        }),
        // 入れ替えなきゃ解けないよ
        new Matrix(new double[][] {
            new double[] { 1, 2, 3 },
            new double[] { 4, 8, 6 },
            new double[] { 7, 8, 9 }
        }),
        // 2回入れ替えなきゃ解けないよ
        new Matrix(new double[][] {
            new double[] { 0, 0, 3 },
            new double[] { 1, 2, 3 },
            new double[] { 7, 8, 9 }
        }));
  }

  public static List<Equation> create3x3Equations() {
    return List.of(
        // p67 問22 (1)
        new Equation(new double[][] {
            new double[] { 2, 3, 1 },
            new double[] { 1, 2, 3 },
            new double[] { 3, 1, 2 }
        }, new double[] { 9, 6, 8 }),
        // p74 演習8 (1)
        new Equation(new double[][] {
            new double[] { 1, 3, -2 },
            new double[] { 1, -8, 8 },
            new double[] { 3, -2, 4 }
        }, new double[] { 0, 0, 0 }),
        // p74 演習8 (2)
        new Equation(new double[][] {
            new double[] { 1, 3, -2 },
            new double[] { 2, -3, 1 },
            new double[] { 3, -2, 2 }
        }, new double[] { 0, 0, 0 }));
  }

  public static List<Matrix> create3x3MatrixesEigenValues() {
    return List.of(
        // p135 問4 (2)
        new Matrix(new double[][] {
            new double[] { 1, 2, 3 },
            new double[] { 0, 1, -3 },
            new double[] { 0, -3, 1 },
        }),
        // p135 問4 (3)
        new Matrix(new double[][] {
            new double[] { 2, 0, 0 },
            new double[] { 0, -1, 0 },
            new double[] { 0, 0, 3 },
        }));
  }

  public static List<MatrixAndAnswer<Double>> createNMatrixesDeterminant(List<Integer> ns) {
    List<MatrixAndAnswer<Double>> ret = new ArrayList<>();
    for (int nsIndex = 0; nsIndex < ns.size(); nsIndex++) {
      int n = ns.get(nsIndex);

      if (n <= 1) {
        throw new RuntimeException("n must be >= 2");
      }

      // 行列の作成
      var matrixData = new double[n][n];
      for (int col = 0; col < n; col++) {
        for (int row = 0; row < col; row++) {
          matrixData[col][row] = ThreadLocalRandom.current().nextDouble();
        }
        matrixData[col][col] = col % 2 == 0 ? 2 : 1 / 2.0;
      }

      // 行列式の生成
      ret.add(new MatrixAndAnswer<>(new Matrix(matrixData), n % 2 == 0 ? 1.0 : 2.0));
    }

    return ret;
  }

  public static List<EquationAndAnswer> createNEquations(List<Integer> ns) {
    List<EquationAndAnswer> ret = new ArrayList<>();
    for (int nsIndex = 0; nsIndex < ns.size(); nsIndex++) {
      int n = ns.get(nsIndex);

      if (n <= 1) {
        throw new RuntimeException("n must be >= 2");
      }

      // 行列の作成
      var matrixData = new double[n][n];
      for (int col = 0; col < n; col++) {
        for (int row = 0; row < col; row++) {
          matrixData[col][row] = ThreadLocalRandom.current().nextDouble();
        }
        matrixData[col][col] = col % 2 == 0 ? 2 : 1 / 2.0;
      }

      double[] ans = new double[n];
      for (int i = 0; i < n; i++) {
        ans[i] = i + 1;
      }

      double[] rightSide = new double[n];

      for (int col = 0; col < n; col++) {
        rightSide[col] = MatrixMethods.innerProduct(n, matrixData[col], ans);
      }

      // 行列式の生成
      ret.add(new EquationAndAnswer(new Equation(matrixData, rightSide), ans));
    }

    return ret;
  }

  public static List<MatrixAndAnswer<List<Eigenvalues>>> createNMatrixesEigenvalue(List<Integer> ns) {
    List<MatrixAndAnswer<List<Eigenvalues>>> ret = new ArrayList<>();
    for (int nsIndex = 0; nsIndex < ns.size(); nsIndex++) {
      int n = ns.get(nsIndex);

      if (n <= 1) {
        throw new RuntimeException("n must be >= 2");
      }

      // 行列λの作成
      var lambda = new double[n][n];
      for (int col = 0; col < n; col++) {
        lambda[col][col] = col + 1;
      }
      // 行列Pの作成
      var p = new double[n][n];
      for (int col = 0; col < n; col++)
        for (int row = 0; row < n; row++) {
          p[col][row] = Math.min(col + 1, row + 1);
        }
      // 行列P^(-1)の作成
      var pInverse = new double[n][n];
      for (int col = 0; col < n; col++) {
        pInverse[col][col] = col == n - 1 ? 1 : 2;
        if (col + 1 < n) {
          pInverse[col][col + 1] = -1.0;
        }
        if (0 <= col - 1) {
          pInverse[col][col - 1] = -1.0;
        }
      }

      var matrixData = MatrixMethods.dot(n, p, lambda);
      matrixData = MatrixMethods.dot(n, matrixData, pInverse);

      // 固有値・固有ベクトルの作成 - QR法は絶対値の大きい順に固有値が出てくる
      List<Eigenvalues> data = new ArrayList<>();
      for (int col = n - 1; col >= 0; col--) {
        double[] vector = new double[n];
        for (int row = 0; row < n; row++) {
          vector[row] = Math.min(col + 1, row + 1);
        }
        MatrixMethods.norm(n, vector);
        data.add(new Eigenvalues(col + 1, vector));
      }
      ret.add(new MatrixAndAnswer<>(new Matrix(matrixData), data));
    }

    return ret;
  }

  public record MatrixAndAnswer<A>(Matrix matrix, A answer) {
  }

  public record EquationAndAnswer(Equation equation, double[] answer) {
  }

  public record Eigenvalues(double value, double[] vector) {
  }
}
