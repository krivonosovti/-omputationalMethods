package SPbU.VICHI.matrix;

public class AbstractMatrix implements Matrix {

    protected double[][] elements;

    public AbstractMatrix(int rows, int cols) {
        this.elements = new double[rows][cols];
    }

    @Override
    public double getElement(int row, int col) {
        return elements[row][col];
    }

    @Override
    public void setElement(int row, int col, double value) {
        elements[row][col] = value;
    }
    @Override
    public AbstractMatrix getRow(int rowIndex) {
        int m = this.getColCount();
        int n = this.getRowCount();
        if (rowIndex < 0 || rowIndex >= n) {
            throw new IndexOutOfBoundsException("Row index out of bounds");
        }
        AbstractMatrix rowMatrix = new AbstractMatrix(1, m);
        for (int j = 0; j < m; j++) {
            rowMatrix.setElement(0, j, getElement(rowIndex, j));
        }
        return rowMatrix;
    }

    @Override
    public AbstractMatrix getCol(int colIndex) {
        int m = this.getColCount();
        int n = this.getRowCount();
        if (colIndex < 0 || colIndex >= m) {
            throw new IndexOutOfBoundsException("Column index out of bounds");
        }
        AbstractMatrix colMatrix = new AbstractMatrix(n, 1);
        for (int i = 0; i < n; i++) {
            colMatrix.setElement(i, 0, getElement(i, colIndex));
        }
        return colMatrix;
    }
    @Override
    public int getRowCount() {
        return elements.length;
    }

    @Override
    public int getColCount() {
        return elements[0].length;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColCount(); j++) {
                sb.append(String.format("%8.2f", getElement(i, j))).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean isNull() {
        int checker = 0;
        int n =  getRowCount();
        int m =  getColCount();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (getElement(i,j) == 0.0) {
                    checker++;
                }
            }
        }
        if (checker == n*m)
            return true;
        return false;
    }

    public AbstractMatrix transpose() {
        int rows = getColCount(); // Новое количество строк после транспонирования
        int cols = getRowCount(); // Новое количество колонок после транспонирования

        // Создание новой матрицы размером MxN, а не квадратной
        AbstractMatrix transposed = new AbstractMatrix(rows, cols) {};

        // Заполнение транспонированной матрицы
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed.setElement(i, j, this.getElement(j, i));
            }
        }

        return transposed;
    }

    public AbstractMatrix add(AbstractMatrix other) {
        if (other.getRowCount() != this.getRowCount() || other.getColCount() != this.getColCount()) {
            throw new IllegalArgumentException("Matrix dimensions do not match");
        }
        AbstractMatrix result = new AbstractMatrix(getRowCount(), getColCount());
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColCount(); j++) {
                result.setElement(i, j, this.getElement(i, j) + other.getElement(i, j));
            }
        }
        return result;
    }

    public AbstractMatrix multiply(double c) {
        int cols = getColCount();
        int rows = getRowCount();
        AbstractMatrix result = new AbstractMatrix(getRowCount(), getColCount());
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.setElement(i,j, this.getElement(i,j) * c);
            }
        }
        return result;
    }

    public AbstractMatrix multiply(AbstractMatrix other) {
        if (this.getColCount() != other.getRowCount()) {
            throw new IllegalArgumentException("Matrix dimensions do not match for multiplication. Required: NxM * MxK.");
        }

        int n = this.getRowCount(); // Число строк первой матрицы (N)
        int m = this.getColCount(); // Число колонок первой матрицы (и строк второй) (M)
        int k = other.getColCount(); // Число колонок второй матрицы (K)

        // Итоговая матрица размерности NxK
        AbstractMatrix result = new AbstractMatrix(n, k) {};

        // Умножение матриц
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < k; j++) {
                double sum = 0;
                for (int p = 0; p < m; p++) {
                    sum += this.getElement(i, p) * other.getElement(p, j);
                }
                result.setElement(i, j, sum);
            }
        }
        return result;
    }

    public double normVector(int n) throws IllegalArgumentException {
        if (this.isNull())
            throw new IllegalArgumentException("vector is null");
        if (this.getColCount() == 1 &&  this.getRowCount() >= 1) {
            double sum = 0;
            for (int i = 0; i < getRowCount(); i++) {
                sum += Math.pow(Math.abs(getElement(i,0)), n);
            }
            return Math.pow(sum, 1 / n);
        } else if (this.getColCount() >= 1 &&  this.getRowCount() == 1) {
            double sum = 0;
            for (int i = 0; i < getColCount(); i++) {
                sum += Math.pow(Math.abs(getElement(0,i)), n);
            }
            return Math.pow(sum, 1 / n);
        } else throw new IllegalArgumentException("expected vector");
    }
    public double normVector(String s) throws IllegalArgumentException {
        if (this.isNull())
            throw new IllegalArgumentException("vector is null");

        if (s != "inf")
            throw new IllegalArgumentException("expected 'inf'");

        if (this.getColCount() == 1 &&  this.getRowCount() >= 1) {
            double max = 0;
            double x;
            for (int i = 0; i < getRowCount(); i++) {
                x = Math.abs(getElement(i,0));
                if (max < x)
                    max = x;
            }
            return max;
        } else if (this.getColCount() >= 1 &&  this.getRowCount() == 1) {
            double max = 0;
            double x;
            for (int i = 0; i < getColCount(); i++) {
                x = Math.abs(getElement(0, i));
                if (max < x)
                    max = x;
            }
            return max;
        } else throw new IllegalArgumentException("expected vector");
    }

    public double normMatrix(int n) throws IllegalArgumentException {
        if (isNull())
            throw new IllegalArgumentException("matrix is null");

        if (getColCount() == 1 || getRowCount() == 1)
            return normVector(n);
        double max = 0;
        double cur;
        AbstractMatrix x;

        for (int i = 0; i < this.getRowCount(); i++) {
            x = this.getRow(i);
            cur = (this.multiply(x.transpose())).normVector(n);
            cur /= x.normVector(n);
            if (max < cur)
                max = cur;
        }
        return max;
    }

    public double normMatrix(String s) throws IllegalArgumentException {
        if (s!="inf")
            throw new IllegalArgumentException("expected 'inf");
        if (isNull())
            throw new IllegalArgumentException("matrix is null");

        if (getColCount() == 1 || getRowCount() == 1)
            return normVector(s);

        double max = 0;
        double cur;
        AbstractMatrix x;

        for (int i = 0; i < this.getRowCount(); i++) {
            x = this.getRow(i);
            cur = (this.multiply(x.transpose())).normVector(s);
            cur /= x.normVector(s);
            if (max < cur)
                max = cur;
        }
        return max;
    }

    public SquareMatrix getInverse() throws IllegalArgumentException {
        if (getColCount() == getRowCount()) {
            SquareMatrix result = new SquareMatrix(getColCount());
            for (int i = 0; i < getRowCount(); i++) {
                for (int j = 0; j < getRowCount(); j++) {
                    result.setElement(i, j, getElement(i, j));
                }
            }
            return result.getInverse();
        }
        else {
            throw new ArithmeticException("Matrix isn't square");
        }
    }

    public double getCond(int p) {
        if (getColCount() == getRowCount()) {
            return normMatrix(p) * (this.getInverse()).normMatrix(p);
        }
        else {
            throw new ArithmeticException("Matrix cannot be inverted.");
        }
    }
}
