package SPbU.VICHI.seven.second;


import SPbU.VICHI.matrix.AbstractMatrix;
import SPbU.VICHI.matrix.SquareMatrix;

public class LUPDecomposition {
    private AbstractMatrix L;
    private AbstractMatrix U;
    private AbstractMatrix P;
    private int permutationSign = 1; // Для учета перестановок (определитель).

    public LUPDecomposition(AbstractMatrix matrix) {
        decompose(matrix);
    }

    private void decompose(AbstractMatrix matrix) {
        int n = matrix.getRowCount();
        if (n != matrix.getColCount()) {
            throw new IllegalArgumentException("Matrix must be square for LUP decomposition.");
        }

        L = new AbstractMatrix(n, n); // Нижнетреугольная
        U = matrix.clone();            // Верхнетреугольная (на основе входной)
        P = SquareMatrix.identity(n); // Изначально единичная матрица

        for (int i = 0; i < n; i++) {
            // Найдем ведущий элемент
            int pivotRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(U.getElement(k, i)) > Math.abs(U.getElement(pivotRow, i))) {
                    pivotRow = k;
                }
            }

            // Перестановка строк, если нужно
            if (pivotRow != i) {
                U.swapRows(i, pivotRow);
                P.swapRows(i, pivotRow);
                permutationSign *= -1; // Инверсия знака определителя при перестановке строк
            }

            // Обработка элементов
            for (int k = i + 1; k < n; k++) {
                double factor = U.getElement(k, i) / U.getElement(i, i);
                L.setElement(k, i, factor);
                for (int j = i; j < n; j++) {
                    U.setElement(k, j, U.getElement(k, j) - factor * U.getElement(i, j));
                }
            }
        }

        // Установка единиц на диагональ матрицы L
        for (int i = 0; i < n; i++) {
            L.setElement(i, i, 1.0);
        }
    }

    public AbstractMatrix getL() {
        return L;
    }

    public AbstractMatrix getU() {
        return U;
    }

    public AbstractMatrix getP() {
        return P;
    }

    public int getPermutationSign() {
        return permutationSign;
    }
}
