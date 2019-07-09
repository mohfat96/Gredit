package gui.sample;


import core.DataManager;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class LineWithHandles extends Line implements Element {


    private  Bounds bounds;
    Circle resizeHandleW = new Circle(5, Color.WHITE);
    Circle resizeHandleE = new Circle(5, Color.WHITE);
    boolean added = false;
    static ToggleButton moveBtn;
    static AnchorPane sidePane;
    private Image image;
    private Tab tab;



    public LineWithHandles(double startX, double startY, double endX, double endY , Bounds bounds,Image image) {
        super(startX, startY, endX, endY);
        createHandles();
        this.bounds = bounds;
        // bind to top left corner of Rectangle
        this.image=image;
    }

    public void showHandles() {
        if (!added) {
            ((AnchorPane) super.getParent()).getChildren().add(resizeHandleE);
            ((AnchorPane) super.getParent()).getChildren().add(resizeHandleW);
            added = true;
        } else {
            resizeHandleW.setVisible(true);
            resizeHandleE.setVisible(true);
        }

    }

    public void hideHandels() {
        resizeHandleW.setVisible(false);
        resizeHandleE.setVisible(false);
    }

    public Circle getResizeHandleW() {
        return resizeHandleW;
    }

    public Circle getResizeHandleE() {
        return resizeHandleE;
    }

    @Override
    public void select() {
        this.setId("focused");

    }

    @Override
    public void unselect() {
        this.setId("");
    }

    public void delete(ArrayList<Element> arrayList) {
        arrayList.remove(this);
        ((AnchorPane) this.getParent()).getChildren().remove(resizeHandleE);
        ((AnchorPane) this.getParent()).getChildren().remove(resizeHandleW);
        ((AnchorPane) this.getParent()).getChildren().remove(this);
        DataManager.getInstance().remove(this);
    }

    @Override
    public void setColor(Paint color) {
        this.setStroke(color);
    }

    @Override
    public Node get() {
        return this;
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


    private void createHandles() {

        resizeHandleW.setStroke(new Color(0, 0, 0, 1));
        resizeHandleE.setStroke(new Color(0, 0, 0, 1));
        resizeHandleW.setStrokeWidth(1);
        resizeHandleE.setStrokeWidth(1);
        resizeHandleW.centerXProperty().bind(super.startXProperty());
        resizeHandleW.centerYProperty().bind(super.startYProperty());
        resizeHandleE.centerXProperty().bind(super.endXProperty());
        resizeHandleE.centerYProperty().bind(super.endYProperty());


        resizeHandleE.setOnMouseEntered(
                event -> {
                    if (moveBtn.isSelected()) {
                        resizeHandleE.setCursor(Cursor.E_RESIZE);
                    }
                }
        );
        resizeHandleW.setOnMouseEntered(
                event -> {
                    if (moveBtn.isSelected()) {
                        resizeHandleW.setCursor(Cursor.W_RESIZE);
                    }
                }
        );

        resizeHandleE.setOnMouseDragged(event -> {
            if (moveBtn.isSelected()) {
                Delta end = new Delta();
                if (event.getX() < bounds.getMaxX()){
                    if(event.getX() < bounds.getMinX()){
                        end.x = bounds.getMinX();
                        this.setEndX(end.x);
                    }
                    else {
                        end.x = event.getX();
                        end.y = event.getY();
                        this.setEndX(end.x);
                    }
                }
                else {
                    end.x = bounds.getMaxX();
                    this.setEndX(end.x);
                }

                event.consume();
            }
        });

        resizeHandleW.setOnMouseDragged(event -> {

            if (moveBtn.isSelected()) {
                Delta start = new Delta();
                if (event.getX()< bounds.getMaxX()){
                    if(event.getX() < bounds.getMinX()){
                        start.x = bounds.getMinX();
                        this.setStartX(start.x);
                    }
                    else {
                        start.x = event.getX();
                        this.setStartX(start.x);
                    }
                }
                else {
                    start.x = bounds.getMaxX();
                    this.setStartX(start.x);
                }

                event.consume();
            }

        });


    }

    public static void setMoveBtn(ToggleButton btn, AnchorPane sidepane) {
        moveBtn = btn;
        sidePane = sidepane;
    }
}
