import java.util.Arrays;
import java.util.List;

public class Equiation {
  private double[][] data;
  private double[] rightSide;
  private int size;

  public Equiation(double[][] data, double[] rightSide) {
    // コピーしなきゃいけないが目をつぶります。
    this.data = data;
    this.rightSide = rightSide;
    size = data.length;
  }

  public static List<Equiation> create3x3Equiations() {
    return List.of(
        // p67 問22 (1)
        new Equiation(new double[][] {
            new double[] { 2, 3, 1 },
            new double[] { 1, 2, 3 },
            new double[] { 3, 1, 2 }
        }, new double[] { 9, 6, 8 }),
        // p74 演習8 (1)
        new Equiation(new double[][] {
            new double[] { 1, 3, -2 },
            new double[] { 1, -8, 8 },
            new double[] { 3, -2, 4 }
        }, new double[] { 0, 0, 0 }),
        // p74 演習8 (2)
        new Equiation(new double[][] {
            new double[] { 1, 3, -2 },
            new double[] { 2, -3, 1 },
            new double[] { 3, -2, 2 }
        }, new double[] { 0, 0, 0 }),
        // 同じ式がある場合
        new Equiation(new double[][] {
            new double[] { 1, 2, 3 },
            new double[] { 1, 2, 3 },
            new double[] { 1, 2, 4 }
        }, new double[] { 1, 1, 0 }));
  }

  public void toDiagonal() {
    if (isDiagonal)
      return;

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
          throw new RuntimeException("一つに定まった解がありません");
        }

        double[] swapTmp = data[found];
        data[found] = data[i];
        data[i] = swapTmp;

        double swapTmp2 = rightSide[found];
        rightSide[found] = rightSide[i];
        rightSide[i] = swapTmp2;
      }

      double headElm = data[i][nonZeroHead];
      // 先頭を1に
      for (int c = nonZeroHead; c < size; c++) {
        data[i][c] /= headElm;
      }
      rightSide[i] /= headElm;

      for (int i2 = i + 1; i2 < size; i2++) {
        // i = 引く行
        // i2 = 引かれる行

        double i2headElm = data[i2][nonZeroHead];
        for (int c = nonZeroHead; c < size; c++) {
          data[i2][c] -= data[i][c] * i2headElm;
        }
        rightSide[i2] -= rightSide[i] * i2headElm;
      }
    }

    // これで上三角
    // 対角行列にもしよう
    // 対角線上の要素は既に1だったよね
    for (int i = 0; i < size; i++) {
      for (int i2 = i + 1; i2 < size; i2++) {
        // i = 引「かれる」行
        // i2 = 引「く」行

        double headElm = data[i][i2];

        for (int c = i2; c < size; c++) {
          data[i][c] -= data[i2][c] * headElm;
        }
        rightSide[i] -= rightSide[i2] * headElm;
      }
    }

    isDiagonal = true;
  }

  private boolean isDiagonal = false;

  public double[] calcAns() {
    if (!isDiagonal) {
      throw new RuntimeException();
    }

    // コピー？知らない。
    return rightSide;
  }

  public String toString() {
    return "left=" + Arrays.deepToString(data) + "\nright=" + Arrays.toString(rightSide);
  }
}
