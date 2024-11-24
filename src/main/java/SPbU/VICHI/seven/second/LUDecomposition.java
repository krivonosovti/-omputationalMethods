package SPbU.VICHI.seven.second;
import SPbU.VICHI.matrix.SquareMatrix;

public class LUDecomposition {

    private final SquareMatrix L;
    private final SquareMatrix U;

    public LUDecomposition(SquareMatrix matrix) {
        if (matrix.getRowCount() != matrix.getColCount()) {
            throw new IllegalArgumentException("LU decomposition requires a square matrix.");
        }

        int n = matrix.getRowCount();
        L = new SquareMatrix(n);
        U = new SquareMatrix(n);

        performLU(matrix);
    }

    private void performLU(SquareMatrix matrix) {
        int n = matrix.getRowCount();

        for (int i = 0; i < n; i++) {
            // Заполняем верхнюю треугольную матрицу U
            for (int j = i; j < n; j++) {
                double sum = 0;
                for (int k = 0; k < i; k++) {
                    sum += L.getElement(i, k) * U.getElement(k, j);
                }
                U.setElement(i, j, matrix.getElement(i, j) - sum);
            }

            // Заполняем нижнюю треугольную матрицу L
            for (int j = i; j < n; j++) {
                if (i == j) {
                    L.setElement(i, i, 1); // Диагональные элементы L равны 1
                } else {
                    double sum = 0;
                    for (int k = 0; k < i; k++) {
                        sum += L.getElement(j, k) * U.getElement(k, i);
                    }
                    if (Math.abs(U.getElement(i, i)) < 1e-10) {
                        throw new ArithmeticException("Matrix is singular and cannot be decomposed.");
                    }
                    L.setElement(j, i, (matrix.getElement(j, i) - sum) / U.getElement(i, i));
                }
            }
        }
    }

    public SquareMatrix getL() {
        return L;
    }

    public SquareMatrix getU() {
        return U;
    }
}
