package files.util;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * This Util-Class represents a Line read out from the input-files.
 *
 * @author Simon Raffeck
 * @version 1.0
 * @since 25.04.18
 */
public class Line implements Convertable {
    public Coordinate end;
    public Coordinate start;
    private double width=5.0;

    public Coordinate getEnd() {
        return end;
    }

    public void setEnd(Coordinate end) {
        this.end = end;
    }

    public Coordinate getStart() {
        return start;
    }

    public void setStart(Coordinate start) {
        this.start = start;
    }

    public Coordinate getCoordinate(){
        Coordinate coord = new Coordinate();
        coord.setX((start.getX()+end.getX())/2);
        coord.setY((start.getY()+end.getY())/2);
        return coord;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Line line = (Line) o;
        return Double.compare(line.width, width) == 0 &&
                Objects.equals(end, line.end) &&
                Objects.equals(start, line.start);
    }

    @Override
    public int hashCode() {

        return Objects.hash(end, start, width);
    }

    @Override
    public byte[] toBytes() {
        byte[] endByte = this.getEnd().toBytes();
        byte[] startByte = this.getStart().toBytes();
        byte[] width = new byte[8];
        ByteBuffer.wrap(width).putDouble(getWidth());
        byte[] result = Convertable.concat(endByte,startByte);
        return result;
    }

    @Override
    public String toString() {
        return "Line{" +
                "end=" + end +
                ", start=" + start +
                ", width=" + width +
                '}';
    }
}
