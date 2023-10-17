
/**
 * Η κλάση αυτή υλοποιεί το MaxHeap
 */
public class MaxHeap {
    //πίνακας από Points που περιέχει όλα τα στοιχεία που ανήκουν στη σωρό
    private final Point[] MaxHeap;
    //πλήθος σημείων που υπάρχουν στη σωρό
    private int size;
    //μέγιστη χωρητικότητα της σωρού
    private final int maximum;

    /**
     * Κατασκευαστής με 1 όρισμα
     * @param maximum_size η μέγιστη χωρητικότητα
     */
    public MaxHeap(int maximum_size) {
        this.maximum = maximum_size;
        this.size = 0;
        MaxHeap = new Point[this.maximum + 1];
        Point point =  new Point(Double.MIN_VALUE, Double.MIN_VALUE);
        point.setDistance_point(Double.MAX_VALUE);
        MaxHeap[0] = point;
    }
    public int getSize(){
        return size;
    }

    /**
     * Μέθοδος που επιστρέφει true αν ο κόμβος είναι φύλλο ή false αν δεν είναι
     * @param node κόμβος για το οποίο θέλω να μάθω αν είναι φύλλο στο δέντρο μου
     */
    private boolean isLeaf(int node) {
        return get_left_child(node) >= size;
    }

    /**
     * Μέθοδος που επιστρέφει το που βρίσκεται ο γονιός
     * @param node κόμβος του οποίου ψάχνω τον γονιό
     */
    private int get_parent(int node) { //parent
        return node / 2;
    }

    /**
     * Μέθοδος που επιστρέφει το που βρίσκεται το αριστερό παιδί
     * @param node κόμβος του οποίου ψάχνω το αριστερό παιδί
     */
    private int get_left_child(int node) { //leftChild
        return (2 * node);
    }

    /**
     * Μέθοδος που επιστρέφει το που βρίσκεται το δεξί παιδί
     * @param node κόμβος του οποίου ψάχνω το δεξί παιδί
     */
    private int get_right_child(int node) { //rightChild
        return (2 * node) + 1;
    }

    /**
     * Μέθοδος που αντιστρέφει δύο κόμβους node1 και node2 στη σωρό
     * node1, node2 είναι οι κόμβοι που θέλω να αλλάξω μεταξύ τους
     */
    private void swap(int node1, int node2) {
        Point temp;
        temp = MaxHeap[node1];
        MaxHeap[node1] = MaxHeap[node2];
        MaxHeap[node2] = temp;
    }
    public Point getMax(){
        return MaxHeap[1];
    }
    public Point extractMax() {
        Point popped = MaxHeap[1];
        MaxHeap[1] = MaxHeap[size];
        apply_maxHeap(1);
        size--;
        return popped;
    }

    /**
     * Μέθοδος που εφαρμόζει τον αλγόριθμο MaxHeap σε ένα υποδέντρο
     * Με τη μέθοδο αυτή έχουμε ως αποτέλεσμα το δεξί και αριστερό υποδέντρο του κόμβου node να είναι heaped και το μόνο που μένει είναι να φτιάξουμε τη ρίζα
     */
    private void apply_maxHeap(int node) {
        if (isLeaf(node))
            return;
        if (MaxHeap[node].getDistance_point() < MaxHeap[get_left_child(node)].getDistance_point() ||
                MaxHeap[node].getDistance_point() < MaxHeap[get_right_child(node)].getDistance_point()) {

            if (MaxHeap[get_left_child(node)].getDistance_point() > MaxHeap[get_right_child(node)].getDistance_point()) {
                swap(node, get_left_child(node));
                apply_maxHeap(get_left_child(node));
            } else {
                swap(node, get_right_child(node));
                apply_maxHeap(get_right_child(node));
            }
        }
    }

    /**
     * Μέθοδος που προσθέτει ένα καινούργιο στοιχείο στη σωρό
     */
    public void insert_to_maxHeap(Point new_point) {
        if (size == maximum){
            if(new_point.getDistance_point()<=getMax().getDistance_point())
                extractMax();
            else
                return ;
        }
        MaxHeap[++size] = new_point;
        int current = size;
        while (MaxHeap[current].getDistance_point() > MaxHeap[get_parent(current)].getDistance_point() ) {
            swap(current, get_parent(current));
            current = get_parent(current);
        }
    }

}