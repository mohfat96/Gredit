package files.util;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * This Class represents one stave of a notesheet and thus also one tab and one Image
 * @author Simon Raffeck
 * @version 1.0
 * @since 25.04.18
 */
public class Stave {
    public List<Note> notes;
    public List<Line> lines;
    public List<Key> keys;
    public String path;
    private Image image;

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public void setKeys(List<Key> keys) {
        this.keys = keys;
    }

    public List<Note> getNotes() {
        return new ArrayList<>(notes);
    }

    public List<Line> getLines() {
        return new ArrayList<>(lines);
    }

    public List<Key> getKeys() {
        return new ArrayList<>(keys);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        this.image=new Image("file:"+path);
    }
    public void setImage(){
        this.image=new Image("file:"+path);
    }

    /**
     * Returns the image used to read out the stave, this also identifies the stave
     * @return The Image used to construct the stave
     */
    public Image getImage() {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stave stave = (Stave) o;
        return Objects.equals(notes, stave.notes) &&
                Objects.equals(lines, stave.lines) &&
                Objects.equals(keys, stave.keys) &&
                Objects.equals(path, stave.path) &&
                Objects.equals(image, stave.image);
    }

    @Override
    public int hashCode() {

        return Objects.hash(notes, lines, keys, path, image);
    }
    public byte[] byteValue(){
        ArrayList<byte[]> bytes= new ArrayList<>();
        for (Note note:notes
             ) {
            bytes.add(note.toBytes());
        }
        for (Line line : lines
                ) {
            bytes.add(line.toBytes());
        }
        for (Key key: keys
                ) {
            bytes.add(key.toBytes());
        }
        bytes.add(path.getBytes());
        byte[] result = new byte[0];
        for (byte[] byt: bytes
             ) {
            result = Convertable.concat(result,byt);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Stave{" +
                "notes=" + notes +
                ", lines=" + lines +
                ", keys=" + keys +
                ", path='" + path + '\'' +
                ", image=" + image +
                '}';
    }

    public void clear(){
        this.notes.clear();
        this.lines.clear();
        this.keys.clear();
    }

    public boolean isEmpty(){
        if (this.notes.isEmpty() && this.lines.isEmpty() && this.keys.isEmpty()){
            return true;
        }
        return false;
    }
}
