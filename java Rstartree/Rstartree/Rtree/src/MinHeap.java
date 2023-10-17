
/**
 * Η κλάση αυτή υλοποιεί το MinHeap
 */
public class MinHeap {
    private final TreeNode[] MinHeap;
    private int size;
    private final int maximum_size;

    /**
     * Κατασκευαστής με 1 όρισμα
     * @param maximum η μέγιστη χωρητικότητα
     */
    public MinHeap(int maximum)
    {
        this.maximum_size = maximum;
        this.size = 0;
        MinHeap = new TreeNode[this.maximum_size + 1];
        TreeNode aNode = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
        aNode.setDistance_from_point(Double.MIN_VALUE);
        MinHeap[0] = aNode;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    /**
     * Μέθοδος που επιστρέφει που βρίσκεται ο γονιός
     * @param node κόμβος του οποίου ψάχνω τον γονιό
     */
    private int find_parent(int node) { 
        return node / 2;
    }

    /**
     * Μέθοδος που επιστρέφει το που βρίσκεται το αριστερό παιδί
     * @param node κόμβος του οποίου ψάχνω το αριστερό παιδί
     */
    private int find_left_child(int node) { 
        return (2 * node);
    }

    /**
     * Μέθοδος που επιστρέφει το που βρίσκεται το δεξί παιδί
     * @param node κόμβος του οποίου ψάχνω το δεξί παιδί
     */
    private int find_right_child(int node) {
        return (2 * node) + 1;
    }

    /**
     * Μέθοδος που επιστρέφει true αν ο κόμβος είναι φύλλο ή false αν δεν είναι
     * @param node ο κόμβος ελέγχου
     */
    private boolean isLeaf(int node) {
        return find_left_child(node) >= size;
    }

    /**
     * Μέθοδος που αντιστρέφει δύο κόμβους node1 και node2 στη σωρό
     */
    private void swap(int node1, int node2)
    {
        TreeNode temp_node;
        temp_node = MinHeap[node1];
        MinHeap[node1] = MinHeap[node2];
        MinHeap[node2] = temp_node;
    }

    /**
     * Μέθοδος που αφαιρεί και επιστρέφει τον κόμβο που βρίσκεται πιο ψηλά στη σωρό
     */
    public TreeNode remove()
    {
        TreeNode exit = MinHeap[1];
        MinHeap[1] = MinHeap[size--];
        apply_minHeap(1);
        return exit;
    }

    /**
     * Μέθοδος που εφαρμόζει τον αλγόριθμο MinHeap σε ένα υποδέντρο
     * Με τη μέθοδο αυτή έχουμε ως αποτέλεσμα το δεξί και αριστερό υποδέντρο να είναι heaped και το μόνο που μένει είναι να φτιάξουμε τη ρίζα
     */
    private void apply_minHeap(int node) {
        //αν ο κόμβος δεν είναι φύλλο και είναι μεγαλύτερο από οποιοδήποτε από τα παιδιά του
        if (!isLeaf(node)) {
            if (MinHeap[node].getDistance_from_point() > MinHeap[find_left_child(node)].getDistance_from_point()
                    || MinHeap[node].getDistance_from_point() > MinHeap[find_right_child(node)].getDistance_from_point()) {
                //αντάλλαξε το με το αριστερό παιδί και προσάρμοσε στη σωρό το αριστερό παιδί
                if (MinHeap[find_left_child(node)].getDistance_from_point() < MinHeap[find_right_child(node)].getDistance_from_point()) {
                    swap(node, find_left_child(node));
                    apply_minHeap(find_left_child(node));
                }
                //αντάλλαξε το με το δεξί παιδί και προσάρμοσε στη σωρό το δεξί παιδί
                else {
                    swap(node, find_right_child(node));
                    apply_minHeap(find_right_child(node));
                }
            }
        }
    }

    /**
     * Μέθοδος που προσθέτει έναν καινούργιο κόμβο στη σωρό
     * @param node κόμβος που θα προστεθεί στη σωρό
     */
    public void insert_to_minHeap(TreeNode node) {
        if (size >= maximum_size)
            return;
        MinHeap[++size] = node;
        int temp = size;
        while (MinHeap[temp].getDistance_from_point() < MinHeap[find_parent(temp)].getDistance_from_point()) {
            swap(temp, find_parent(temp));
            temp = find_parent(temp);
        }
    }

}
