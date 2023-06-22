import java.util.List;

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
}
