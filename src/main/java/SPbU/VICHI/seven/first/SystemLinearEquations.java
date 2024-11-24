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

    public static GaussResult solve(AbstractMatrix matrix, AbstractMatrix b, PivotType pivotType) {
        int n = matrix.getRowCount();
        int operationCount = 0;
        double determinant = 1;

        if (n != matrix.getColCount()) {
            throw new IllegalArgumentException("Matrix must be square to solve the system.");
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

        // Прямой ход метода Гаусса с выбором ведущего элемента
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

            // Перестановка строк и/или столбцов для приведения ведущего элемента в позицию (i, i)
            if (pivotRow != i) {
                determinant *= -1; // Инверсия знака определителя при перестановке строк
                for (int j = 0; j <= n; j++) {
                    double temp = augmented.getElement(i, j);
                    augmented.setElement(i, j, augmented.getElement(pivotRow, j));
                    augmented.setElement(pivotRow, j, temp);
                    operationCount++; // Операция перестановки
                }
            }

            if (pivotType == PivotType.FULL && pivotCol != i) {
                determinant *= -1; // Инверсия знака определителя при перестановке столбцов
                for (int row = 0; row < n; row++) {
                    double temp = augmented.getElement(row, i);
                    augmented.setElement(row, i, augmented.getElement(row, pivotCol));
                    augmented.setElement(row, pivotCol, temp);
                }
            }

//            /**
//             * //            Это условие проверяет, не слишком ли мал ведущий элемент на текущем шаге алгоритма Гаусса.
//             * //            Пороговое значение 1e-10 используется, чтобы учесть возможные ошибки округления при
//             * //            вычислениях с плавающей точкой.
//            */
//            if (Math.abs(augmented.getElement(i, i)) < 1e-10) {
//                throw new ArithmeticException("System of equations has no unique solution.");
//            }


            determinant *= augmented.getElement(i, i); // Умножение на диагональный элемент

            double diagElement = augmented.getElement(i, i);
            for (int j = 0; j <= n; j++) {
                augmented.setElement(i, j, augmented.getElement(i, j) / diagElement);
                operationCount++;
            }

            for (int k = i + 1; k < n; k++) {
                double factor = augmented.getElement(k, i);
                for (int j = 0; j <= n; j++) {
                    augmented.setElement(k, j, augmented.getElement(k, j) - factor * augmented.getElement(i, j));
                    operationCount++;
                }
            }
        }

        Vector solution = new Vector(n);
        for (int i = n - 1; i >= 0; i--) {
            double sum = augmented.getElement(i, n);
            for (int j = i + 1; j < n; j++) {
                sum -= augmented.getElement(i, j) * solution.getElement(j, 0);
                operationCount++;
            }
            solution.setElement(i, 0, sum);
        }

        return new GaussResult(solution, operationCount, determinant);
    }
}
