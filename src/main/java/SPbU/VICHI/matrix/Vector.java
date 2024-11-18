package SPbU.VICHI.matrix;

public class Vector extends AbstractMatrix {
    public Vector(int size) {
        super(size, 1);
    }


    public int getSize () {
        return this.getRowCount();
    }
    public double getElement (int row) {
        return this.getElement(row,0);
    }


    public void setElement(int row, double value) {
        elements[row][0] = value;
    }

    public double dotProduct(Vector other) {
        if (this.getSize() != other.getSize()) {
            throw new IllegalArgumentException("Vectors must have the same size for dot product.");
        }
        double sum = 0;
        for (int i = 0; i < this.getSize(); i++) {
            sum += this.getElement(i) * other.getElement(i);
        }
        return sum;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getSize(); i++) {
            sb.append(String.format("%.2f", getElement(i)));
            sb.append('\n');
        }
        return sb.toString();
    }

}
