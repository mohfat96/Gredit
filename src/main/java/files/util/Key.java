package files.util;

import java.util.Objects;

/**
 * This Util-Class represents a Key read out from the input-files.
 *
 * @author Simon Raffeck
 * @version 1.0
 * @since 25.04.18
 */
public class Key implements Convertable {
    public Coordinate coordinate;
    public String type;

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key = (Key) o;
        return Objects.equals(coordinate, key.coordinate);
    }

    @Override
    public int hashCode() {

        return Objects.hash(coordinate);
    }

    @Override
    public byte[] toBytes() {
        return this.getCoordinate().toBytes();
    }


    @Override
    public String toString() {
        return "Key{" +
                "coordinate=" + coordinate +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
