package gui.sample;

public class Container  {
    private Element element;

    public Element get() {

        return element;
    }

    public void set(Element lineWithHandles) {
        if (lineWithHandles == null){
            throw new IllegalArgumentException();
        }
       element = lineWithHandles;

    }

    public void clear(){
        element = null;
    }

    public boolean isEmpty(){
        return element == null;
    }
}
