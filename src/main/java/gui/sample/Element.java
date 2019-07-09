package gui.sample;


import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;

import javafx.scene.control.Tab;
import javafx.scene.paint.Paint;
import javafx.scene.image.Image;
import java.awt.*;
import java.util.ArrayList;

public interface Element {

    public void select();

    public void unselect();

    public void delete(ArrayList<Element> arrayList);

    public double getTranslateX();

    public double getTranslateY();

    public void setLayoutX(double x);

    public void setLayoutY(double y);

    public double getLayoutX();

    public double getLayoutY();

    public void setTranslateX(double x);

    public void setTranslateY(double y);

    public void setCursor(Cursor cursor);

    public void setColor(Paint color);

    public Node get();

    public Bounds getBoundsInParent();

    public Image getImage();

    public void setImage(Image image);

    void setTab(Tab tab);
    Tab getTab();

}
