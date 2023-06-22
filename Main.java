import java.util.Arrays;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    List<Matrix> matrixes = Tests.create3x3MatrixesDeterminant();

    matrixes.forEach(matrix -> {
      try {
        System.out.println("最初の行列 = " + matrix.toString());

        long c1 = System.currentTimeMillis();
        double determinant = matrix.toTriangleAndDeterminant();
        long c2 = System.currentTimeMillis();

        System.out.println("上三角行列 = " + matrix.toString());
        System.out.println("行列式 = " + determinant);
        System.out.println("経過時間 = " + (c2 - c1) + "(milis)");
        System.out.println();
      } catch (RuntimeException e) {
        System.err.println(e.getMessage());
        System.err.println();
      }
    });

    List<Equiation> equiations = Equiation.create3x3Equiations();

    equiations.forEach(equiation -> {
      try {
        System.out.println("与式 = " + equiation.toString());
        long c1 = System.currentTimeMillis();
        equiation.toDiagonal();
        long c2 = System.currentTimeMillis();
        System.out.println("解答後 = " + equiation.toString());
        System.out.println("経過時間 = " + (c2 - c1) + "(milis)");
        System.out.println();
      } catch (RuntimeException e) {
        System.err.println(e.getMessage());
        System.err.println(equiation.toString());
        System.err.println();
      }
    });

    List<Matrix> matrixes2 = Tests.create3x3MatrixesEigenValues();

    matrixes2.forEach(matrix -> {
      try {
        System.out.println("与えられた行列 = " + matrix.toString());

        long c1 = System.currentTimeMillis();
        double[][] eigenvalue = matrix.qrMethod(50);
        double[][] eigenVectors = new double[matrix.getSize()][];

        for (int i = 0; i < eigenvalue.length; i++) {
          double eigen = eigenvalue[i][i];
          eigenVectors[i] = matrix.getEigenVector(50, eigen, 1e-14);
        }
        long c2 = System.currentTimeMillis();

        for (int i = 0; i < eigenvalue.length; i++) {
          double eigen = eigenvalue[i][i];
          System.out.println("(固有値 = " + eigen + ", 固有ベクトル = " + Arrays.toString(eigenVectors[i]) + ")");
        }

        System.out.println("経過時間 = " + (c2 - c1) + "(milis)");

        System.out.println();
      } catch (RuntimeException e) {
        e.printStackTrace();
        System.err.println(e.getMessage());
        System.err.println();
      }
    });
  }
}