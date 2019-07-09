package model;

public enum NoteName {
    C, D, E, F, G, A, B;

    /**
     * Method required for the search function. Tries to translate a string representing a note value to an
     * equivalent NoteName instance.
     *
     * @param s Note value in form of a String
     * @return Equivalent NoteName instance.
     */
    public static NoteName getNoteName(String s) {
        if (s == null || s.isEmpty())
            throw new IllegalArgumentException("String may not be null!");

        s = s.toUpperCase();

        NoteName result = null;
        if (s.equals("C"))
            result = C;
        else if (s.equals("D"))
            result = D;
        else if (s.equals("E"))
            result = E;
        else if (s.equals("F"))
            result = F;
        else if (s.equals("G"))
            result = G;
        else if (s.equals("A"))
            result = A;
        else if (s.equals("B"))
            result = B;
        else
            throw new IllegalArgumentException(s + " is not a valid note name!");
        return result;
    }

    /**
     * This method returns the next note following this instance in a C Major scale.
     * If the instance is a note not within a C Major scale, it will be ignored.
     *
     * @return Next note in a C Major scale
     */
    public NoteName getNext() {
        return NoteName.values()[(this.ordinal() + 1) % NoteName.values().length];
    }

    /**
     * This method returns the previous note before this instance in a C Major scale.
     * If the instance is a note not within a C Major scale, it will be ignored.
     *
     * @return Previous note in a C Major scale.
     */
    public NoteName getPrevious() {
        return NoteName.values()[(this.ordinal() - 1 + NoteName.values().length) % NoteName.values().length];
    }

    public static int getDistance(NoteName name1, NoteName name2) {
        int result = 0;
        NoteName current = name2;
        while (!name1.equals(current)) {
            if ((name1.ordinal() - name2.ordinal() + NoteName.values().length) % NoteName.values().length
                    <= (name2.ordinal()-name1.ordinal()+ NoteName.values().length) % NoteName.values().length){
                current = current.getNext();
                result--;
            }
            else {
                current = current.getPrevious();
                result++;
            }
        }
        return result;
    }
}
