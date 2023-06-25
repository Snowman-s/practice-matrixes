import java.util.Arrays;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    List<Matrix> matrixes = Tests.create3x3MatrixesDeterminant();

    matrixes.forEach(matrix -> {
      System.out.println("最初の行列 = " + matrix.toString());

      long c1 = System.currentTimeMillis();
      double determinant = matrix.toTriangleAndDeterminant();
      long c2 = System.currentTimeMillis();

      System.out.println("上三角行列 = " + matrix.toString());
      System.out.println("行列式 = " + determinant);
      System.out.println("経過時間 = " + (c2 - c1) + "(milis)");
      System.out.println();
    });

    List<Equation> equations = Tests.create3x3Equations();

    equations.forEach(equation -> {
      try {
        System.out.println("与式 = " + equation.toString());
        long c1 = System.currentTimeMillis();
        equation.toDiagonal();
        long c2 = System.currentTimeMillis();
        System.out.println("解答後 = " + equation.toString());
        System.out.println("経過時間 = " + (c2 - c1) + "(milis)");
        System.out.println();
      } catch (RuntimeException e) {
        System.err.println(e.getMessage());
        System.err.println(equation.toString());
        System.err.println();
      }
    });

    List<Matrix> matrixes2 = Tests.create3x3MatrixesEigenValues();

    matrixes2.forEach(matrix -> {
      try {
        System.out.println("与えられた行列 = " + matrix.toString());

        long c1 = System.currentTimeMillis();
        double[][] eigenvalue = matrix.qrMethod(30);
        double[][] eigenVectors = new double[matrix.getSize()][];

        for (int i = 0; i < eigenvalue.length; i++) {
          double eigen = eigenvalue[i][i];
          eigenVectors[i] = matrix.getEigenVector(20, eigen, 1e-14);
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

    var matrixAndAnswer = Tests.createNMatrixesDeterminant(List.of(4, 10, 100, 1000));
    matrixAndAnswer.forEach(
        element -> {
          var matrix = element.matrix();
          var collectDeterminant = element.answer();

          try {
            System.out.println("行列の次数 = " + matrix.getSize());

            long c1 = System.currentTimeMillis();
            double determinant = matrix.toTriangleAndDeterminant();
            long c2 = System.currentTimeMillis();

            System.out.println("行列式 = " + determinant);
            System.out.println("真値との差 = " + Math.abs(collectDeterminant - determinant));
            System.out.println("経過時間 = " + (c2 - c1) + "(milis)");
            System.out.println();
          } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            System.err.println();
          }
        });

    var equationAndAnswer = Tests.createNEquations(List.of(4, 10, 100, 1000));

    equationAndAnswer.forEach(record -> {
      Equation equation = record.equation();
      int n = record.answer().length;
      try {
        System.out.println("方程式、次数 = " + n);

        long c1 = System.currentTimeMillis();
        equation.toDiagonal();
        double[] ans = equation.calcAns();
        long c2 = System.currentTimeMillis();

        double errorMean = 0;
        for (int i = 0; i < n; i++) {
          errorMean += Math.abs(ans[i] - record.answer()[i]);
        }
        errorMean /= n;

        System.out.println("誤差平均 = " + errorMean);
        System.out.println("経過時間 = " + (c2 - c1) + "(milis)");
        System.out.println();
      } catch (RuntimeException e) {
        System.err.println(e.getMessage());
        System.err.println(equation.toString());
        System.err.println();
      }
    });

    var matrixesAndAnswer2 = Tests.createNMatrixesEigenvalue(List.of(4, 10, 100, 1000));

    matrixesAndAnswer2.forEach(element -> {
      var matrix = element.matrix();
      var collectEigenvalues = element.answer();

      try {
        System.out.println("行列の次数 = " + matrix.getSize());

        long c1 = System.currentTimeMillis();
        double[][] eigenvalue = matrix.qrMethod(300);
        double[][] eigenVectors = new double[matrix.getSize()][];

        for (int i = 0; i < eigenvalue.length; i++) {
          double eigen = eigenvalue[i][i];
          eigenVectors[i] = matrix.getEigenVector(20, eigen, 1e-14);
        }
        long c2 = System.currentTimeMillis();

        double eigenvaluesErrorsMean = 0;
        double eigenvectorsCosineMean = 0;
        for (int i = 0; i < eigenvalue.length; i++) {
          double eigen = eigenvalue[i][i];
          eigenvaluesErrorsMean += Math.abs(collectEigenvalues.get(i).value() - eigen);
          eigenvectorsCosineMean += Math
              .abs(MatrixMethods.innerProduct(matrix.getSize(), collectEigenvalues.get(i).vector(),
                  eigenVectors[i]));
        }
        eigenvaluesErrorsMean /= matrix.getSize();
        eigenvectorsCosineMean /= matrix.getSize();

        System.out
            .println("(固有値誤差平均 = " + eigenvaluesErrorsMean + ", 固有ベクトルコサイン類似度絶対値平均 = " + eigenvectorsCosineMean + ")");
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