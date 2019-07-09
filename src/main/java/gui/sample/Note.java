package gui.sample;

import core.DataManager;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;

import javafx.scene.shape.Rectangle;
import model.CoreUtils;
import model.NoteName;
import model.NotePosition;

import java.util.ArrayList;

public class Note extends Rectangle implements Element {

    private static double width = 15;
    private static double height = 15;
    private Image image;
    private Tab tab;
    private Paint color;

    public Note(double centerX, double centerY, double radius, Paint fill, Image image) {
        super(centerX , centerY, width, height);
        super.setFill(fill);
        super.setArcHeight(3);
        super.setArcWidth(3);
        this.image=image;

        color = fill;
    }

    public void delete(ArrayList<Element> arrayList) {
        ((AnchorPane) super.getParent()).getChildren().remove(this);
        DataManager.getInstance().remove(this);
    }

    @Override
    public void setColor(Paint color) {
        this.color = color;
        this.setFill(color);
    }

    @Override
    public Node get() {
        return this;
    }

    public void select() {
        this.setId("focused");

    }

    public void colorizeTemporarily(Paint color){
        setFill(color);
    }

    public void undoTemporaryColorization(){
        setFill(this.color);
    }


    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public void setImage(Image image) {
        this.image=image;
    }

    @Override
    public void setTab(Tab tab) {
        this.tab=tab;
    }

    @Override
    public Tab getTab() {
        return this.tab;
    }

    public void unselect() {
        this.setId("");
    }

    public static double getNoteWidth() {
        return width;
    }

    public static double getNoteHeight() {
        return height;
    }

    public static void setNoteWidth(double width) {
        Note.width = width;
    } //method to set width from user (not implemented yet)


    public static void setNoteHeight(double height) { //method to set width from user (not implemented yet)
        Note.height = height;
    }

    public NoteName getName(){
        return CoreUtils.computeNoteName(this);
    }

    public NotePosition getNotePosition(){
        return CoreUtils.computeNotePosition(this);
    }


}
