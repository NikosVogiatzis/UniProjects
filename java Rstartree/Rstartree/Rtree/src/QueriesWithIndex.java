import java.util.ArrayList;
import java.util.Stack;

/**
 * Η κλάση αυτή υλοποιεί τα ερωτήματα περιοχής και k κοντινότερων γειτόνων με index
 */
public class QueriesWithIndex {

    private TreeNode root; // Ρίζα του R*tree

    public QueriesWithIndex(TreeNode root) {
        this.root = root;
    }

    /**
     * Συνάρτηση που υλοποιεί τα ερωτήματα περιοχής με τη βοήθεια του indexfile
     * Επιστρέφει τους γείτονες που βρίσκονται μέσα στον "κύκλο" γύρω από το στοιχείο mid_point
     * Χρησιμοποιεί μια στοίβα (για τον αλγόριθμο DFS) όπου σπρώχνουμε έναν κόμβο εάν είναι μια πιθανή επιλογή να κρατήσουμε γείτονες
     * και μια ArrayList από Points όπου βάζουμε τους γείτονες
     * @param mid_point σημείο από το οποίο ψάχνουμε τους γείτονες
     * @param radius η ακτίνα μέσα στην οποία ψάχνουμε τα σημεία
     */
    public ArrayList<Location> range_query_with_index(Point mid_point, double radius) {
        //ArrayList με τις τοποθεσίες που επιστρέφονται
        ArrayList<Location> range_results=new ArrayList<>();
        //ArrayList με τα στοιχεία που βρίσκονται μέσα στο radius
        ArrayList<Point> points = new ArrayList<>();
        //διαβάζει από το datafile
        Data data=new Data();
        //αν η απόσταση της ρίζα από το σημείο mid_point είναι μικρότερο από την τιμή της ακτίνας
        //τότε βάζουμε τη ρίζα στη στοίβα
        if (root.getMBR().distance_between_point_and_MBR(mid_point) <= radius) {
            Stack<TreeNode> DFS = new Stack<>();
            DFS.push(root);
            //όσο η στοίβα δεν είναι άδεια
            while (!DFS.empty()) {
                //κάνουμε pop τον πρώτο κόμβο της στοίβας
                TreeNode node = DFS.pop();
                for (int i = 0; i < node.getChildren().size(); i++) {
                    //αν ο κόμβος που βγήκε από τη στοίβα είναι φύλλο τότε
                    //ελέγχουμε αν τα σημεία που ανήκουν μέσα στον κόμβο βρίσκονται εντός της ακτίνας radius
                    //αν ισχύει αυτό τότε προσθέτουμε τα σημεία αυτά στο points
                    if (node.getChildren().get(i).isLeaf()) {
                        for (int k = 0; k < node.getChildren().get(i).getPoints().size(); k++) {
                            if (node.getChildren().get(i).getPoints().get(k).find_distance_from_point(mid_point) <= radius) {
                                points.add(node.getChildren().get(i).getPoints().get(k));
                            }
                        }
                    }
                    //αν ο κόμβος που βγήκε από τη στοίβα δεν είναι φύλλο τότε
                    //ελέγχουμε αν τα mbr των παιδιών του είναι εντός της ακτίνας radius
                    //αν ισχύει η συνθήκη τότε βάζουμε τα παιδιά του κόμβου στη στοίβα
                    else {
                        if (node.getChildren().get(i).getMBR().distance_between_point_and_MBR(mid_point) <= radius) {
                            DFS.push(node.getChildren().get(i));
                        }
                    }
                }
            }
        }
        //αποθηκεύουμε τα δεκτά σημεία στη range_results
        for (Point point : points) {
            Location loc = (data.get_block_loc(point.getBlock_id(), point.getSlot_id()));
            loc.find_manhattan_distance_between_two_points(mid_point.getLat(), mid_point.getLon());
            range_results.add(loc);
        }
        return range_results;
    }

    /**
     * Συνάρτηση που υλοποιεί το ερώτημα k κοντινότερων γειτόνων με τη βοήθεια του indexfile
     * Επιστρέφει τους k κοντινότερους γείτονες από το σημείο middle
     * Στη minHeap περιέχονται οι κόμβοι που μπορεί να χρειαστεί να ψάξουμε για τους πιθανούς γείτονες
     * Στην κορυφή του minHeap αποθηκεύουμε την περιοχή που είναι πιο κοντά στο σημείο middle
     * Στη maxHeap περιέχονται οι k κοντινότεροι γείτονες
     * στην κορυφή της στοίβας αποθηκεύουμε την περιοχή που ενώ ανήκει στους k κοντινότερους γείτονες βρίσκεται πιο μακριά από την middle
     * @param middle σημείο από το οποίο ψάχνουμε τους γείτονες
     * @param k αριθμός από κοντινότερους γείτονες
     * */
    public ArrayList<Location> knn_with_index(Point middle, int k) {
        ArrayList<Point> neighbors = new ArrayList<>();
        Data dt=new Data(); //In this class there is a method to read a specific register from DataFile
        MinHeap minHeap = new MinHeap(100);
        MaxHeap maxHeap = new MaxHeap(k);
        for (TreeNode node : root.getChildren()) {
            node.setDistance_from_point(node.getMBR().distance_between_point_and_MBR(middle));
            minHeap.insert_to_minHeap(node);
        }
        while (!minHeap.isEmpty()) {
            TreeNode tempNode = minHeap.remove();
            for (TreeNode node : tempNode.getChildren()) {
                //System.out.println(maxHeap.getSize());
                node.setDistance_from_point(node.getMBR().distance_between_point_and_MBR(middle));
                if (!node.isLeaf() && (maxHeap.getSize() < k)) {
                    minHeap.insert_to_minHeap(node);
                } else if (!node.isLeaf()) {                   // If maxHeap  is full we already have k neigborngs but we must the nearest
                    if (maxHeap.getMax().getDistance_point() >= node.getDistance_from_point()) {
                        minHeap.insert_to_minHeap(node);
                    }
                } else {
                    ArrayList<Point> temp = node.getPoints();
                    for (Point point : temp) {
                        point.setDistance_point(point.find_distance_from_point(middle));
                        maxHeap.insert_to_maxHeap(point);// We ensured that if maxHeap is empty we replace the max with the new one only if the new one is smaller. The condition is in Insert method.
                    }
                }
            }
        }
        for (int i = 0; i < k; i++) {
            neighbors.add(maxHeap.extractMax());
        }
        ArrayList<Location> locations=new ArrayList<>();
        for (Point neighbor : neighbors) {
            Location loc = dt.get_block_loc(neighbor.getBlock_id(), neighbor.getSlot_id());
            loc.setDistance(neighbor.getDistance_point());
            locations.add(loc);
        }
        return locations;
    }
}
