package gui.sample;




import core.DataManager;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import model.CoreUtils;
import model.NoteName;
import model.NotePosition;

import java.util.ArrayList;
import java.util.List;

public class NoteKey implements Element {

    private final javafx.scene.shape.Shape shape;
    private static double width = 15;
    private static double height = 15;

    private static char type = 'c';
    private double x;
    private double y;
    private Image image;
    private Tab tab;
    private String configType;

    private final static ArrayList<NoteKey> allNoteKeys = new ArrayList<>();

    public NoteKey(double x, double y, Paint color, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
        switch (type) {
            case 'c': {
                javafx.scene.shape.Rectangle rect1 = new Rectangle(x, y, width, height);
                rect1.setArcWidth(3);
                rect1.setArcHeight(3);
                javafx.scene.shape.Rectangle rect2 = new Rectangle(x, y + 25, width, height);
                rect2.setArcWidth(3);
                rect2.setArcHeight(3);
                Line line = new Line(rect1.getX() + 1, rect1.getY() + rect1.getHeight(), rect2.getX() + 1, rect2.getY());
                line.setStrokeWidth(2);
                Shape shape1 = Shape.union(rect1, rect2);
                shape = Shape.union(shape1, line);
                shape.setFill(color);
                shape.setStyle("class:Notekey");
                this.configType="c";
                break;
            }
            case 'f': {
                javafx.scene.shape.Rectangle rect1 = new javafx.scene.shape.Rectangle(x, y, width, height);
                rect1.setArcWidth(3);
                rect1.setArcHeight(3);
                javafx.scene.shape.Rectangle rect2 = new javafx.scene.shape.Rectangle(x, y + 2 * height, width, height);
                rect2.setArcWidth(3);
                rect2.setArcHeight(3);
                javafx.scene.shape.Rectangle rect3 = new javafx.scene.shape.Rectangle(x - width, y + height, width, height);
                rect3.setArcHeight(3);
                rect3.setArcWidth(3);
                Shape shape1 = Shape.union(rect1, rect2);
                shape = Shape.union(shape1, rect3);
                shape.setFill(color);
                shape.setStyle("class:Notekey");
                this.configType="f";
                break;
            }
            default:
                shape = null;

        }

    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public static double getWidth() {
        return width;
    }

    public static void setWidth(double width) {
        NoteKey.width = width;
    }

    public static double getHeight() {
        return height;
    }

    public static void setHight(double hight) {
        NoteKey.height = hight;
    }

    public static char getType() {
        return type;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public static void setType(char type) {
        NoteKey.type = type;
    }

    @Override
    public void select() {
        shape.setId("focused");
    }

    @Override
    public void unselect() {
        shape.setId("");
    }

    @Override
    public void delete(ArrayList<Element> arrayList) {
        AnchorPane pane = (AnchorPane) shape.getParent();
        pane.getChildren().remove(shape);
        allNoteKeys.remove(this);
        DataManager.getInstance().remove(this);
    }
    @Override
    public void setTab(Tab tab) {
        this.tab=tab;
    }

    @Override
    public Tab getTab() {
        return this.tab;
    }

    @Override
    public double getTranslateX() {
        return shape.getTranslateX();
    }

    @Override
    public double getTranslateY() {
        return shape.getTranslateY();
    }

    @Override
    public void setLayoutX(double x) {
        shape.setLayoutX(x);
    }

    @Override
    public void setLayoutY(double y) {
        shape.setLayoutY(y);
    }

    @Override
    public double getLayoutX() {
        return shape.getLayoutX();
    }

    @Override
    public double getLayoutY() {
        return shape.getLayoutY();
    }

    @Override
    public void setTranslateX(double x) {
        shape.setTranslateX(x);
    }

    @Override
    public void setTranslateY(double y) {
        shape.setTranslateY(y);
    }

    @Override
    public void setCursor(Cursor cursor) {
        shape.setCursor(cursor);
    }

    @Override
    public void setColor(Paint color) {
        shape.setFill(color);
    }



    public void addToParent(AnchorPane parent){
        parent.getChildren().add(shape);
        allNoteKeys.add(this);
    }

    @Override
    public Shape get() {
        return shape;
    }

    @Override
    public Bounds getBoundsInParent() {
        return shape.getBoundsInParent();
    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public void setImage(Image image) {
        this.image=image;
    }

    public static NoteKey getNoteKey(Shape node)
    {
        for (NoteKey noteKey : allNoteKeys){
            if (noteKey.get().equals(node)){
                return noteKey;
            }
        }
        return null;
    }


    //Newly added methods for note value determination

    public NotePosition getNotePosition() {
        return CoreUtils.computeNotePosition(this);
    }

    public NoteName getNoteName() {
        if (type == 'c')
            return NoteName.C;
        else
            return NoteName.F;
    }
}
