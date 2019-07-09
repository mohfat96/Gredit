package gui.sample;

import core.DataManager;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public class Selection implements Iterable<Element>{

   private ArrayList<Element> selected = new ArrayList<>();
   private double maxX = Double.MIN_VALUE;
   private double maxY = Double.MIN_VALUE;
   private double minX = Double.MAX_VALUE;
   private double minY = Double.MAX_VALUE;



   public void add(Element element){
       selected.add(element);
       maxX = Double.max(maxX,element.getBoundsInParent().getMaxX());
       maxY = Double.max(maxY,element.getBoundsInParent().getMaxY());
       minX = Double.min(minX,element.getBoundsInParent().getMinX());
       minY = Double.min(minY , element.getBoundsInParent().getMinY());
       }

   public void remove(Element element){
       selected.remove(element);
       DataManager.getInstance().remove(element);
       if (selected.isEmpty()){
           clear();
       }else {
           double elementMaxX = element.getBoundsInParent().getMaxX();
           double elementMaxY = element.getBoundsInParent().getMaxY();
           double elementMinX = element.getBoundsInParent().getMinX();
           double elementMinY = element.getBoundsInParent().getMinX();

           if (maxX == elementMaxX||
                   minX == elementMinX || maxY == elementMaxY || minY == elementMinY){
               refreshValues();
           }


       }
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public void refreshValues() {
         maxX = Double.MIN_VALUE;
         maxY = Double.MIN_VALUE;
         minX = Double.MAX_VALUE;
         minY = Double.MAX_VALUE;

       for (Element element : selected){
           maxX = Double.max(maxX,element.getBoundsInParent().getMaxX());
           maxY = Double.max(maxY,element.getBoundsInParent().getMaxY());
           minX = Double.min(minX,element.getBoundsInParent().getMinX());
           minY = Double.min(minY , element.getBoundsInParent().getMinY());
       }
    }

    public void clear(){
       selected.clear();
       maxX = Double.MIN_VALUE;
       maxY = Double.MIN_VALUE;
       minX = Double.MAX_VALUE;
       minY = Double.MAX_VALUE;
   }

   public boolean isEmpty(){
       return selected.isEmpty();
   }

   public boolean contains(Element element){
       return selected.contains(element);
   }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    @Override
    public Iterator iterator() {
        return selected.iterator();
    }

    @Override
    public void forEach(Consumer action) {
       selected.forEach(action);
    }

    @Override
    public Spliterator spliterator() {
        return selected.spliterator();
    }

}
