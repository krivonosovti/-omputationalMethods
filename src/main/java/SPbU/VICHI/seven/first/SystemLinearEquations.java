package SPbU.VICHI.seven.first;

import SPbU.VICHI.matrix.AbstractMatrix;
import SPbU.VICHI.matrix.Vector;

public class SystemLinearEquations {
    public static class GaussResult {
        public Vector solution;
        public int operationCount;
        public double determinant;

        public GaussResult(Vector solution, int operationCount, double determinant) {
            this.solution = solution;
            this.operationCount = operationCount;
            this.determinant = determinant;
        }
    }
    public enum PivotType {
        ROW,       // Выбор по строке
        COLUMN,    // Выбор по столбцу
        FULL       // Полный выбор (по строке и столбцу)
    }

    public static AbstractMatrix forwardElimination(AbstractMatrix matrix, AbstractMatrix b, PivotType pivotType) {
        int n = matrix.getRowCount();
        if (n != matrix.getColCount()) {
            throw new IllegalArgumentException("Matrix must be square for forward elimination.");
        }
        if (b.getRowCount() != n || b.getColCount() != 1) {
            throw new IllegalArgumentException("Vector b must have dimensions Nx1.");
        }

        // Создаем расширенную матрицу [A|b]
        AbstractMatrix augmented = new AbstractMatrix(n, n + 1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                augmented.setElement(i, j, matrix.getElement(i, j));
            }
            augmented.setElement(i, n, b.getElement(i, 0));
        }

        // Прямой ход метода Гаусса
        for (int i = 0; i < n; i++) {
            int pivotRow = i;
            int pivotCol = i;

            // Стратегия выбора ведущего элемента
            switch (pivotType) {
                case ROW:
                    for (int k = i + 1; k < n; k++) {
                        if (Math.abs(augmented.getElement(k, i)) > Math.abs(augmented.getElement(pivotRow, i))) {
                            pivotRow = k;
                        }
                    }
                    break;

                case COLUMN:
                    for (int k = i + 1; k < n; k++) {
                        if (Math.abs(augmented.getElement(i, k)) > Math.abs(augmented.getElement(i, pivotCol))) {
                            pivotCol = k;
                        }
                    }
                    break;

                case FULL:
                    for (int row = i; row < n; row++) {
                        for (int col = i; col < n; col++) {
                            if (Math.abs(augmented.getElement(row, col)) > Math.abs(augmented.getElement(pivotRow, pivotCol))) {
                                pivotRow = row;
                                pivotCol = col;
                            }
                        }
                    }
                    break;
            }

            // Перестановка строк и/или столбцов
            if (pivotRow != i) {
                for (int j = 0; j <= n; j++) {
                    double temp = augmented.getElement(i, j);
                    augmented.setElement(i, j, augmented.getElement(pivotRow, j));
                    augmented.setElement(pivotRow, j, temp);
                }
            }

            if (pivotType == PivotType.FULL && pivotCol != i) {
                for (int row = 0; row < n; row++) {
                    double temp = augmented.getElement(row, i);
                    augmented.setElement(row, i, augmented.getElement(row, pivotCol));
                    augmented.setElement(row, pivotCol, temp);
                }
            }

            // Нормализация строки
            double diagElement = augmented.getElement(i, i);
            for (int j = 0; j <= n; j++) {
                augmented.setElement(i, j, augmented.getElement(i, j) / diagElement);
            }

            // Обнуление элементов ниже ведущего
            for (int k = i + 1; k < n; k++) {
                double factor = augmented.getElement(k, i);
                for (int j = 0; j <= n; j++) {
                    augmented.setElement(k, j, augmented.getElement(k, j) - factor * augmented.getElement(i, j));
                }
            }
        }

        return augmented;
    }

    public static Vector backSubstitution(AbstractMatrix augmented) {
        int n = augmented.getRowCount();
        Vector solution = new Vector(n);

        for (int i = n - 1; i >= 0; i--) {
            double sum = augmented.getElement(i, n);
            for (int j = i + 1; j < n; j++) {
                sum -= augmented.getElement(i, j) * solution.getElement(j, 0);
            }
            solution.setElement(i, 0, sum);
        }

        return solution;
    }

    public static GaussResult solve(AbstractMatrix matrix, AbstractMatrix b, PivotType pivotType) {
        AbstractMatrix augmented = forwardElimination(matrix, b, pivotType);
        Vector solution = backSubstitution(augmented);

        double determinant = 1;
        for (int i = 0; i < matrix.getRowCount(); i++) {
            determinant *= augmented.getElement(i, i);
        }

        return new GaussResult(solution, 0, determinant); // Replace 0 with operation count if needed
    }
}
