package files.util;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * This class is a Util-class representing the coordinates of Elements within the gui.
 *
 * @author Simon Raffeck
 * @version 1.0
 * @since 25.04.18
 */
public class Coordinate implements Convertable {
    public double x;
    public double y;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "("+getX()+" | "+getY()+")";
    }

    @Override
    public byte[] toBytes() {
        byte[] xBytes = new byte[8];
        ByteBuffer.wrap(xBytes).putDouble(getX());
        byte[] yBytes = new byte[8];
        ByteBuffer.wrap(yBytes).putDouble(getY());
        byte[] result = Convertable.concat(xBytes,yBytes);
        return result;
    }

}
