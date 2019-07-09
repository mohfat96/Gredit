package model;

import core.DataManager;
import gui.sample.Element;
import gui.sample.LineWithHandles;
import gui.sample.Note;
import gui.sample.NoteKey;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CoreUtils {

    /**
     * Compute the value of note relative to the key position
     *
     * @param note Representation of a note
     * @return Computed value of a note
     */
    public static NoteName computeNoteName(Note note) {
        NoteKey key = getCorrectKey(note);

        if (key == null)
            throw new IllegalArgumentException("Could not read the correct key!");

        int distance = -key.getNotePosition().ordinal() + note.getNotePosition().ordinal();
        NoteName result = key.getNoteName();

        for (int i = 0; i < Math.abs(distance); i++) {
            if (distance < 0)
                result = result.getPrevious();
            else result = result.getNext();
        }

        return result;
    }

    public static NotePosition computeNotePosition(Element element) {
        if (!(element instanceof Note) && !(element instanceof NoteKey))
            throw new IllegalArgumentException("Element must be an instance of Note or NoteKey!");

        List<LineWithHandles> lines = getCorrectLines(element);
        if (lines == null || lines.size() != 4)
            throw new IllegalArgumentException("There must be only 4 lines to correctly identify a note!");

        lines = lines.stream().filter(line -> line.getBoundsInParent().getMinY()
                < element.getBoundsInParent().getMaxY()).collect(Collectors.toList());

        NotePosition result;
        double epsilon = element.getBoundsInParent().getMinY()
                + (element.getBoundsInParent().getMaxY() - element.getBoundsInParent().getMinY())
                / (element instanceof Note ? 2.0 : 2.0);
        //We need to know whether a note is on a line or between two lines. We do this by
        // checking whether a certain part of the note is still above one line (-> on a line) or not
        // (-> between two lines).
        if (lines.isEmpty()) {
            //Possible positions for note: Just Top
            result = NotePosition.Top;

        } else if (lines.size() == 1) {
            //Possible positions: OnLineFour, BetweenThreeFour
            if (lines.get(0).getBoundsInParent().getMaxY() > epsilon || (lines.get(0).intersects(element.getBoundsInParent())))
                result = NotePosition.OnLineFour;
            else result = NotePosition.BetweenThreeFour;

        } else if (lines.size() == 2) {
            //Possible positions: OnLineThree, BetweenTwoThree
            if (lines.get(1).getBoundsInParent().getMaxY() > epsilon || ( lines.get(1).intersects(element.getBoundsInParent())))
                result = NotePosition.OnLineThree;
            else result = NotePosition.BetweenTwoThree;

        } else if (lines.size() == 3) {
            //Possible position: OnLineTwo, Between OneTwo
            if (lines.get(2).getBoundsInParent().getMaxY() > epsilon || ( lines.get(2).intersects(element.getBoundsInParent())))
                result = NotePosition.OnLineTwo;
            else result = NotePosition.BetweenOneTwo;

        } else {
            //Possible positions: OnLineOne, Bottom
            if (lines.get(3).getBoundsInParent().getMaxY() > epsilon || (lines.get(3).intersects(element.getBoundsInParent())))
                result = NotePosition.OnLineOne;
            else result = NotePosition.Bottom;
        }

        return result;
    }

    /**
     * This method return a list of LinesWithHandles which are all in the same tab as a given Element. It is needed to
     * determine the position of a NoteKey and additional to that the value of a Note.
     * The returned list is sorted by their y-value.
     *
     * @param element Element containing the tab variable
     * @return List of LinesWithHandles, ordered by their y-value
     */
    private static List<LineWithHandles> getCorrectLines(Element element) {
        List<Element> lines = DataManager.getInstance().getLines();
        List<LineWithHandles> result = new ArrayList<>();
        for (Element e : lines) {
            LineWithHandles line = (LineWithHandles) e;
            if (line.getTab().equals(element.getTab()))
                result.add(line);
        }
        result.sort(Comparator.comparingDouble(line -> line.getBoundsInParent().getMinY()));
        return result;
    }

    /**
     * This method returns the NoteKey instance which is in the same tab as a given Element. It is needed to determine
     * note values according to a key value and position.
     *
     * @param element Element
     * @return The needed NoteKey
     */
    private static NoteKey getCorrectKey(Element element) {
        List<Element> keys = DataManager.getInstance().getKeys();
        for (Element e : keys) {
            if (e.getTab().equals(element.getTab()))
                return (NoteKey) e;
        }
        return null;
    }

    /**
     * Compute the new coordinates for a note with value newName and move the given Note instance to
     * this position.
     *
     * @param note    A Note instance which need to be corrected/moved.
     * @param newName The new value the given Note instance will get.
     */
    public static void correctNotePosition(Note note, NoteName newName) {
        NoteKey key = getCorrectKey(note);
        if (key == null)
            return;

        List<LineWithHandles> lines = getCorrectLines(note);
        double epsilon = -0.2;

        //Compute the new NotePosition a note with name newName must be placed
        int distance = NoteName.getDistance(key.getNoteName(), newName);
        int index = key.getNotePosition().ordinal() + distance;
        if (index >= NotePosition.values().length || index < 0)
            index = key.getNotePosition().ordinal() + distance - 7;
        NotePosition pos = NotePosition.values()[index];

        //Move note to the new position by changing just its y-coordinate
        if (pos.equals(NotePosition.Top)) {
            note.setY(lines.get(0).getBoundsInParent().getMinY() - note.getHeight() - epsilon);
        } else if (pos.equals(NotePosition.OnLineFour)) {
            note.setY(lines.get(0).getBoundsInParent().getMinY() - note.getHeight() / 2 - epsilon);
        } else if (pos.equals(NotePosition.BetweenThreeFour)) {
            double middle = (lines.get(0).getBoundsInParent().getMinY() + lines.get(1).getBoundsInParent().getMinY()) / 2;
            note.setY(middle - note.getHeight() / 2 - epsilon);
        } else if (pos.equals(NotePosition.OnLineThree)) {
            note.setY(lines.get(1).getBoundsInParent().getMinY() - note.getHeight() / 2 - epsilon);
        } else if (pos.equals(NotePosition.BetweenTwoThree)) {
            double middle = (lines.get(1).getBoundsInParent().getMinY() + lines.get(2).getBoundsInParent().getMinY()) / 2;
            note.setY(middle - note.getHeight() / 2 - epsilon);
        } else if (pos.equals(NotePosition.OnLineTwo)) {
            note.setY(lines.get(2).getBoundsInParent().getMinY() - note.getHeight() / 2 - epsilon);
        } else if (pos.equals(NotePosition.BetweenOneTwo)) {
            double middle = (lines.get(2).getBoundsInParent().getMinY() + lines.get(3).getBoundsInParent().getMinY()) / 2;
            note.setY(middle - note.getHeight() / 2 - epsilon);
        } else if (pos.equals(NotePosition.OnLineOne)) {
            note.setY(lines.get(3).getBoundsInParent().getMinY() - note.getHeight() / 2 - epsilon);
        } else if (pos.equals(NotePosition.Bottom)) {
            note.setY(lines.get(3).getBoundsInParent().getMinY() + note.getHeight() + epsilon);
        }
    }
}
