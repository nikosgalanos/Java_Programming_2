public class LinkedList <E> {
    /** Node's value. */
    private E value;
    /** Next node. */
    private LinkedList <E> next;

    /**
     * Construct a list with a single element.
     * @param v The value of the list's first element.
     */
    LinkedList(final E v) {
        value = v;
        next = null;
    }

    /**
     * Add an element to the list.
     * @param v The value of the element to add.
     * @return A list with element v added to it.
     */
    public final LinkedList <E> add(final E v) {
        LinkedList <E> n = new LinkedList <E>(v);
        n.next = this;
        return n;
    }

    /**
     * Obtain a string representation of the list.
     * @return The list as a series of elements connected with arrows.
     */
    public final String toString() {
        String me = value.toString();

        /* Recursive implementation */
        if (next == null) {
            return me;
        } else {
            return me + " -> " + next;
        }
    }

    /** A simple test harness */
    static public void main(String args[]) {
        LinkedList <Integer> intList = new LinkedList <Integer>(0);

        intList = intList.add(1);
        intList = intList.add(18);
        intList = intList.add(45);

        for (int i = 0; i < 5; i++)
            intList = intList.add(i * 10);
        System.out.println(intList);
    }
}
