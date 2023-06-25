import java.util.Arrays;

/**
 * matrix
 */
public class Matrix {
  private double[][] data;
  private int size;

  public Matrix(double[][] data) {
    // コピーしなきゃいけないが目をつぶります。
    this.data = data;
    size = data.length;
  }

  /**
   * 破壊的に上三角にして行列式を返す
   */
  public double toTriangleAndDeterminant() {
    boolean swapped = this.swapped;

    for (int i = 0; i < size; i++) {
      int nonZeroHead = i;

      if (Double.compare(data[i][nonZeroHead], 0.0) == 0) {
        // すごく厄介な問題
        // まずこれ以降の行に0でない要素があるか求める。
        int found = -1;

        for (int searchNonZero = nonZeroHead + 1; searchNonZero < size; searchNonZero++) {
          if (Double.compare(data[searchNonZero][nonZeroHead], 0.0) != 0) {
            found = searchNonZero;
            break;
          }
        }

        // じゃあ行列式ないじゃん
        if (found == -1) {
          return 0;
        }

        double[] swapTmp = data[found];
        data[found] = data[i];
        data[i] = swapTmp;

        swapped = !swapped;
      }

      double headElm = data[i][nonZeroHead];

      for (int i2 = nonZeroHead + 1; i2 < size; i2++) {
        // i = 引く行
        // i2 = 引かれる行

        double i2headElm = data[i2][nonZeroHead];
        for (int c = nonZeroHead; c < size; c++) {
          data[i2][c] -= data[i][c] / headElm * i2headElm;
        }
      }
    }

    this.swapped = swapped;

    double ret = swapped ? -1.0 : 1.0;

    for (int i = 0; i < size; i++) {
      ret *= data[i][i];
    }

    return ret;
  }

  private boolean swapped = false;

  /**
   * 非破壊的でナイーブなQR法
   * グラム・シュミットの方法を使う
   * 
   * @param repeatTime 反復回数
   * 
   * @return 固有値列 - サイズはこの正方行列のサイズと同じ
   */
  public double[][] qrMethod(int repeatTime) {
    assert size >= 2;
    double[][] dataCopy = MatrixMethods.copy(size, data);
    for (int repeating = 0; repeating < repeatTime; repeating++) {
      // qr分解する
      double[][] q = new double[size][size];
      double[][] r = new double[size][size];

      // 行と列が逆になっているので注意
      double[][] u = new double[size][];

      for (int col = 0; col < size; col++) {
        // col 行目のベクトルをとる (aとなる)
        double[] nowSeeingVector = new double[size];
        for (int row = 0; row < size; row++) {
          nowSeeingVector[row] = dataCopy[row][col];
        }

        double[] v = new double[size];

        MatrixMethods.add(size, v, 1, nowSeeingVector);

        // シグマの計算
        for (int i = 0; i < col; i++) {
          double innerProduct = MatrixMethods.innerProduct(size, nowSeeingVector, u[i]);
          MatrixMethods.add(size, v, -innerProduct, u[i]);
        }

        MatrixMethods.norm(size, v);

        // これで v=uになった
        u[col] = v;
      }

      // 現在uはQの転置状態である
      r = MatrixMethods.dot(size, u, dataCopy);
      // で、Qを転置
      q = MatrixMethods.t(size, u);

      dataCopy = MatrixMethods.dot(size, r, q);
    }

    return dataCopy;
  }

  public double[] getEigenVector(int repeatTime, double eigenValue, double eps) {
    double[][] b = MatrixMethods.copy(size, data);
    for (int i = 0; i < size; i++) {
      b[i][i] -= eigenValue + eps;
    }

    // b(=A-λI)の逆行列をかけていくのだが、方程式を解いたほうが良い。 逆べき乗法
    double[] willBeEigenVector = new double[size];
    for (int i = 0; i < size; i++) {
      willBeEigenVector[i] = 1;
    }

    for (int repeating = 0; repeating < repeatTime; repeating++) {
      Equation equiation = new Equation(MatrixMethods.copy(size, b), willBeEigenVector);
      equiation.toDiagonal();
      willBeEigenVector = equiation.calcAns();

      MatrixMethods.norm(size, willBeEigenVector);
    }

    return willBeEigenVector;
  }

  public int getSize() {
    return size;
  }

  public String toString() {
    return Arrays.deepToString(data) + (swapped ? " (この行列は列が奇数回swapされています)" : "");
  }
}
