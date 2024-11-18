package SPbU.VICHI.matrix;

public class SquareMatrix extends AbstractMatrix {
    public SquareMatrix(int size) {
        super(size, size);
    }

    public SquareMatrix getInverse() {
        int n = getRowCount();
        AbstractMatrix augmented = new AbstractMatrix(n, 2 * n);

        // Создание расширенной матрицы [A|I]
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                augmented.setElement(i, j, this.getElement(i, j));
            }
            // Заполнение правой части единичной матрицей
            augmented.setElement(i, i + n, 1.0);
        }

        // Прямой ход метода Гаусса
        for (int i = 0; i < n; i++) {
            // Проверка, что главный элемент не равен нулю
            if (augmented.getElement(i, i) == 0) {
                throw new ArithmeticException("Matrix is singular and cannot be inverted.");
            }

            // Нормализация строки
            double diagElement = augmented.getElement(i, i);
            for (int j = 0; j < 2 * n; j++) {
                augmented.setElement(i, j, augmented.getElement(i, j) / diagElement);
            }

            // Обнуление элементов ниже и выше главной диагонали
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = augmented.getElement(k, i);
                    for (int j = 0; j < 2 * n; j++) {
                        augmented.setElement(k, j, augmented.getElement(k, j) - factor * augmented.getElement(i, j));
                    }
                }
            }
        }

        // Извлечение обратной матрицы из расширенной матрицы
        SquareMatrix inverse = new SquareMatrix(n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverse.setElement(i, j, augmented.getElement(i, j + n));
            }
        }

        return inverse;
    }

    public double getCond() {
        return normMatrix(1) * (this.getInverse()).normMatrix(1);
    }
}

