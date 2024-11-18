package SPbU.VICHI.matrix;

public interface Matrix {
    double getElement(int row, int col);
    void setElement(int row, int col, double value);
    int getRowCount();
    int getColCount();
    AbstractMatrix getRow(int rowIndex);
    AbstractMatrix getCol(int colIndex);
    SquareMatrix getInverse();
    boolean isNull();

}
