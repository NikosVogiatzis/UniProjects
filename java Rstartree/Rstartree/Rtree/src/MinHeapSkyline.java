import java.util.Arrays;

/**
 * Η κλάση αυτή υλοποιεί το MinHeap αλλά χρησιμοποιείται μόνο κατά την υλοποίηση του αλγορίθμου skyline
 */
public class MinHeapSkyline {
    //πίνακας από κόμβους που περιέχει όλα τα στοιχεία που ανήκουν στη σωρό
    private SkylineNode[] Heap;
    private int size;
    private int maximum;

    public MinHeapSkyline(int max_size)
    {
        this.maximum = max_size;
        this.size = 0;
        Heap = new SkylineNode [max_size+1];
        SkylineNode node = new SkylineNode(new TreeNode(Double.MIN_VALUE,Double.MIN_VALUE,Double.MIN_VALUE,Double.MIN_VALUE));
        Heap[0] = node;
    }

    /**
     * Μέθοδος που επιστρέφει το που βρίσκεται ο γονιός
     * @param node κόμβος του οποίου ψάχνω τον γονιό
     */
    private int find_parent(int node)
    {
        return node / 2;
    }

    /**
     * Μέθοδος που επιστρέφει το που βρίσκεται το αριστερό παιδί
     * @param node κόμβος του οποίου ψάχνω το αριστερό παιδί
     */
    private int find_left_child(int node)
    {
        return (2 * node);
    }

    /**
     * Μέθοδος που επιστρέφει το που βρίσκεται το δεξί παιδί
     * @param node κόμβος του οποίου ψάχνω το δεξί παιδί
     */
    private int find_right_child(int node)
    {
        return (2 * node) + 1;
    }

    /**
     * Μέθοδος που επιστρέφει true αν ο κόμβος είναι φύλλο ή false αν δεν είναι
     * @param node κόμβος για το οποίο θέλω να μάθω αν είναι φύλλο στο δέντρο μου
     */
    private boolean isLeaf(int node) {
        return find_left_child(node) >= size;
    }

    /**
     * Μέθοδος που ελέγχει αν το μέγεθος της σωρός είναι μηδενικό
     * Δηλαδή ελέγχει αν η σωρός είναι άδεια
     */
    public boolean isEmpty(){
        return size == 0;
    }

    /**
     * Μέθοδος που αντιστρέφει δύο κόμβους node1 και node2 στη σωρό
     */
    private void swap(int node1, int node2)
    {
        SkylineNode temp;
        temp = Heap[node1];
        Heap[node1] = Heap[node2];
        Heap[node2] = temp;
    }

    /**
     * Μέθοδος που εφαρμόζει τον αλγόριθμο MinHeap σε ένα υποδέντρο
     * Με τη μέθοδο αυτή έχουμε ως αποτέλεσμα το δεξί και αριστερό υποδέντρο να είναι heaped και το μόνο που μένει είναι να φτιάξουμε τη ρίζα
     */
    private void apply_minHeap_for_skyline(int node)
    {
        if (!isLeaf(node)) {
            if (Heap[node].distance > Heap[find_left_child(node)].distance
                    || Heap[node].distance > Heap[find_right_child(node)].distance) {
                if (Heap[find_left_child(node)].distance < Heap[find_right_child(node)].distance) {
                    swap(node, find_left_child(node));
                    apply_minHeap_for_skyline(find_left_child(node));
                }
                else {
                    swap(node, find_right_child(node));
                    apply_minHeap_for_skyline(find_right_child(node));
                }
            }
        }
    }

    /**
     * Μέθοδος που προσθέτει έναν καινούργιο κόμβο στη σωρό
     * @param new_node κόμβος που θα προστεθεί στη σωρό
     */
    public void insert_to_minHeap_for_skyline(SkylineNode new_node)
    {
        if (size >= maximum) {
            return;
        }
        //αν έχει χώρο η σωρός τότε γίνεται αύξηση του πλήθους των στοιχείων της σωρού κατά 1
        //και αναπροσάρμοσε τη σειρά των στοιχείων μέσα σε αυτή
        Heap[++size] = new_node;
        int temp = size;
        while (Heap[temp].distance < Heap[find_parent(temp)].distance) {
            swap(temp, find_parent(temp));
            temp = find_parent(temp);
        }

    }


    /**
     * Μέθοδος που αφαιρεί και επιστρέφει τον κόμβο που βρίσκεται πιο ψηλά στη σωρό
     */
    public SkylineNode remove_for_skyline()
    {
        SkylineNode popped = Heap[1];
        Heap[1] = Heap[size--];
        apply_minHeap_for_skyline(1);
        return popped;
    }



    /**
     * Μέθοδος που κάνει override τη συνάρτηση toString για να μπορούμε να εμφανίσουμε πιο εύκολα στην οθόνη
     * τα στοιχεία που βρίσκονται μέσα στη minHeap
     */
    @Override
    public String toString() {
        return "HeapSkyline{" + "Heap=" + Arrays.toString(Heap) +
                '}';
    }
}

