import java.util.ArrayList;

/**
 * Στην κλάση αυτή υλοποιούνται οι αλγόριθμοι αυτοί ώστε να χτιστεί το δέντρο και να τρέξουν τα ερωτήματα που θέλουμε
 */
public class Tree {
    private TreeNode root;
    private static final int max = 5;
    private static final int min = 2;
    private ArrayList<TreeNode> node_path;

    /**
     * Κατασκευαστής του δέντρου
     * @param root η ρίζα του δέντρου
     */
    public Tree(TreeNode root) {
        this.root = root;
    }


    /**
     * Getter για τη ρίζα
     */
    public TreeNode getRoot() {
        return root;
    }


    /**
     * Η μέθοδος αυτή υλοποιεί τον αλγόριθμο ChooseSubtree. Οι περιπτώσεις περιγράφονται αναλυτικά
     * και στην αναφορά αλλά υλοποιήθηκε βάσει του R*tree paper που αναρτήσατε στη σελίδα στο elearning
     * @param node κόμβος
     * @param new_point σημείο που θέλουμε να προσθέσουμε
     * @return έναν κόμβο στον οποίο θα προστεθεί το καινούργιο σημείο
     */
    public TreeNode ChooseSubtree(TreeNode node, Point new_point) {
        node_path.add(node);
        if (node.isLeaf()) {
            return node;
        } else {
            TreeNode child = null;
            double minimumExtend = Double.MAX_VALUE;
            for (int i = 0; i < node.getChildren().size(); i++) {
                double area = node.getChildren().get(i).getMBR().calculate_new_area(new_point.getLat(), new_point.getLon()) - node.getChildren().get(i).getMBR().getArea();
                if (area < minimumExtend) {
                    minimumExtend = area;
                    child = node.getChildren().get(i);

                }
            }
            return ChooseSubtree(child, new_point);
        }

    }
    /**
     * Συνάρτηση που χωρίζει έναν κόμβο σε δύο
     * Υλοποιήθηκε σύμφωνα με το paper του R*Tree που αναρτήθηκε στη σελίδα του μαθήματος
     * @param node ο κόμβος τον οποίο θέλουμε να χωρίσουμε σε δύο
     * @return μια ArrayList που περιέχει τους δύο νέους κόμβους
     */
    public ArrayList<TreeNode> ChooseSplitAxis(TreeNode node) {
        TreeNode Node1 = new TreeNode(0, 0, 0, 0);
        TreeNode Node2 = new TreeNode(0, 0, 0, 0);
        double perimeter_X = 0;
        double perimeter_Y = 0;
        double min_overlap = Double.MAX_VALUE;


        if (node.getChildren().size() == 0) {
            node.getPoints().sort(new Point.PointsComparatorLat());
            for (int k = 1; k < max - 2 * min + 2; k++) {
                TreeNode node1 = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                TreeNode node2 = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                int _k = 0;
                for (; _k < min - 1 + k; _k++) {
                    node1.add_new_point(node.getPoints().get(_k));
                }
                for (; _k < max + 1; _k++) {
                    node2.add_new_point(node.getPoints().get(_k));
                }
                double perx = node1.getMBR().getPerimeter() + node2.getMBR().getPerimeter();
                perimeter_X += perx;
            }

            node.getPoints().sort(new Point.PointsComparatorLon());
            for (int k = 1; k < max - 2 * min + 2; k++) {
                TreeNode node1 = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                TreeNode node2 = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                int _k = 0;
                //χωρίζουμε τα σημεία που ήταν στον αρχικό κόμβο
                //τοποθετούμε τα μισά στον πρώτο ΠΡΟΣΩΡΙΝΟ κόμβο
                for (; _k < min - 1 + k; _k++) {
                    node1.add_new_point(node.getPoints().get(_k));
                }
                //και τα άλλα μισά στον δεύτερο ΠΡΟΣΩΡΙΝΟ κόμβο
                for (; _k < max + 1; _k++) {
                    node2.add_new_point(node.getPoints().get(_k));
                }
                //άθροισμα των περιμέτρων των δύο ΠΡΟΣΩΡΙΝΩΝ κόμβων όταν ταξινομούμε σύμφωνα με το y
                double pery = node1.getMBR().getPerimeter() + node2.getMBR().getPerimeter();
                perimeter_Y += pery;
            }

            if (perimeter_X < perimeter_Y) {
                node.getPoints().sort(new Point.PointsComparatorLat());
                for (int k = 1; k < max - 2 * min + 2; k++) {
                    TreeNode node1 = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                    TreeNode node2 = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                    int _k = 0;
                    //χωρίζουμε πάλι τα σημεία στους δύο νέους ΠΡΟΣΩΡΙΝΟΥΣ κόμβους
                    for (; _k < min - 1 + k; _k++) {
                        node1.add_new_point(node.getPoints().get(_k));
                    }
                    for (; _k < max + 1; _k++) {
                        node2.add_new_point(node.getPoints().get(_k));
                    }
                    double overlapx = node1.getMBR().calculate_overlap(node2.getMBR());
                    if (overlapx < min_overlap) {
                        min_overlap = overlapx;
                        Node1 = node1;
                        Node2 = node2;
                    }
                    //αν το overlap που βρήκαμε τώρα είναι ίσο με το min_overlap τότε συγκρίνουμε
                    //α) το εμβαδό του ορθογωνίου που προκύπτει από την πρόσθεση των ορθογωνίων που σχηματίζουν οι δύο ΚΥΡΙΟΙ κόμβοι
                    //β) το εμβαδό του ορθογωνίου που προκύπτει από την πρόσθεση των ορθογωνίων που σχηματίζουν οι δύο ΠΡΟΣΩΡΙΝΟΙ κόμβοι
                    if(overlapx==min_overlap){
                        double area1= Node1.getMBR().getArea() + Node2.getMBR().getArea();
                        double area2= node1.getMBR().getArea() + node2.getMBR().getArea();
                        //αν το β) είναι μικρότερο τότε δίνουμε καινούργιες τιμές στους ΚΥΡΙΟΥΣ κόμβους
                        if(area2<area1){
                            Node1=node1;
                            Node2=node2;
                        }
                    }
                }
            }
            //αν η δεύτερη περίμετρος είναι μικρότερη
            else {
                // γίνεται πάλι ταξινόμηση των σημείων του κόμβου ως προς τον άξονα y (μικρότερο προς μεγαλύτερο)
                node.getPoints().sort(new Point.PointsComparatorLon());
                for (int k = 1; k < max - 2 * min + 2; k++) {
                    //αρχικοποίηση δύο ΠΡΟΣΩΡΙΝΩΝ κόμβων
                    TreeNode node1 = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                    TreeNode node2 = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                    int _k = 0;
                    for (; _k < min - 1 + k; _k++) {
                        node1.add_new_point(node.getPoints().get(_k));
                    }
                    for (; _k < max + 1; _k++) {
                        node2.add_new_point(node.getPoints().get(_k));
                    }
                    //όμως τώρα υπολογίζουμε το overlap που σχηματίζεται μεταξύ των ΠΡΟΣΩΡΙΝΩΝ κόμβων (όχι την περίμετρο)
                    //όταν ταξινομούμε ως προς y
                    double overlapy = node1.getMBR().calculate_overlap(node2.getMBR());
                    //κρατάμε το μικρότερο πιθανό overlap και δίνουμε τιμές στα στους δύο ΚΥΡΙΟΥΣ κόμβους
                    if (overlapy < min_overlap) {
                        min_overlap = overlapy;
                        Node1 = node1;
                        Node2 = node2;
                    }
                    //αν το overlap που βρήκαμε τώρα είναι ίσο με το min_overlap τότε συγκρίνουμε
                    //α) το εμβαδό του ορθογωνίου που προκύπτει από την πρόσθεση των ορθογωνίων που σχηματίζουν οι δύο ΚΥΡΙΟΙ κόμβοι
                    //β) το εμβαδό του ορθογωνίου που προκύπτει από την πρόσθεση των ορθογωνίων που σχηματίζουν οι δύο ΠΡΟΣΩΡΙΝΟΙ κόμβοι
                    if(overlapy==min_overlap){
                        double area1= Node1.getMBR().getArea() + Node2.getMBR().getArea();
                        double area2= node1.getMBR().getArea() + node2.getMBR().getArea();
                        //αν το β) είναι μικρότερο τότε δίνουμε καινούργιες τιμές στους ΚΥΡΙΟΥΣ κόμβους
                        if(area2<area1){
                            Node1=node1;
                            Node2=node2;
                        }
                    }
                }
            }
        }
        //αν ο κόμβος δεν είναι φύλλο
        else {
            //κάνε ταξινόμηση των κόμβων σύμφωνα με τις τιμές των ορθογωνίων τους ως προς τον άξονα x
            // (από την πιο μικρή τιμή προς την πιο μεγάλη)
            node.getChildren().sort(new TreeNode.CompareLat());
            for (int k = 1; k < max - 2 * min + 2; k++) {
                //αρχικοποίηση δύο ΠΡΟΣΩΡΙΝΩΝ κόμβων
                TreeNode node1 = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                TreeNode node2 = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                int _k = 0;
                for (; _k < min - 1 + k; _k++) {
                    node1.add_new_child_node(node.getChildren().get(_k));
                }
                for (; _k < max + 1; _k++) {
                    node2.add_new_child_node(node.getChildren().get(_k));
                }
                //άθροισμα των περιμέτρων των δύο ΠΡΟΣΩΡΙΝΩΝ κόμβων όταν ταξινομομούμε σύμφωνα με στο x
                double perX = node1.getMBR().getPerimeter() + node2.getMBR().getPerimeter();
                perimeter_X += perX;
            }
            //κάνε ταξινόμηση των κόμβων σύμφωνα με τις τιμές των ορθογωνίων τους ως προς τον άξονα y
            // (από την πιο μικρή τιμή προς την πιο μεγάλη)
            node.getChildren().sort(new TreeNode.CompareLon());
            for (int k = 1; k < max - 2 * min + 2; k++) {
                //αρχικοποίηση δύο ΠΡΟΣΩΡΙΝΩΝ κόμβων
                TreeNode node1 = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                TreeNode node2 = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                int _k = 0;
                for (; _k < min - 1 + k; _k++) {
                    node1.add_new_child_node(node.getChildren().get(_k));
                }
                for (; _k < max + 1; _k++) {
                    node2.add_new_child_node(node.getChildren().get(_k));
                }
                //άθροισμα των περιμέτρων των δύο ΠΡΟΣΩΡΙΝΩΝ κόμβων όταν ταξινομομούμε σύμφωνα με στο y
                double perY = node1.getMBR().getPerimeter() + node2.getMBR().getPerimeter();
                perimeter_Y += perY;
            }
            //πάλι διαλέγουμε τη μικρότερη τιμή περιμέτρων
            //αν η πρώτη περίμετρος είναι μικρότερο τότε
            if (perimeter_X < perimeter_Y) {
                //γίνεται πάλι ταξινόμηση των κόμβων σύμφωνα με τις τιμές των ορθογωνίων τους ως προς τον άξονα x
                node.getChildren().sort(new TreeNode.CompareLat());
                for (int k = 1; k < max - 2 * min + 2; k++) {
                    //αρχικοποίηση δύο ΠΡΟΣΩΡΙΝΩΝ κόμβων
                    TreeNode node1 = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                    TreeNode node2 = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                    int _k = 0;
                    for (; _k < min - 1 + k; _k++) {
                        node1.add_new_child_node(node.getChildren().get(_k));
                    }
                    for (; _k < max + 1; _k++) {
                        node2.add_new_child_node(node.getChildren().get(_k));
                    }
                    //υπολογίζουμε το overlap που σχηματίζεται μεταξύ των ΠΡΟΣΩΡΙΝΩΝ κόμβων (όχι την περίμετρο)
                    // όταν ταξινομούμε ως προς x
                    double overlapX = node1.getMBR().calculate_overlap(node2.getMBR());
                    //κρατάμε το μικρότερο πιθανό overlap και δίνουμε τιμές στα στους δύο ΚΥΡΙΟΥΣ κόμβους
                    if (overlapX < min_overlap) {
                        min_overlap = overlapX;
                        Node1 = node1;
                        Node2 = node2;
                    }
                    if(overlapX==min_overlap){
                        double area1= Node1.getMBR().getArea() + Node2.getMBR().getArea();
                        double area2= node1.getMBR().getArea() + node2.getMBR().getArea();
                        if(area2<area1){
                            Node1=node1;
                            Node2=node2;
                        }
                    }
                }
            }
            //αν η δεύτερη περίμετρος είναι μικρότερη
            else {
                //γίνεται πάλι ταξινόμηση των κόμβων σύμφωνα με τις τιμές των ορθογωνίων τους ως προς τον άξονα y
                node.getChildren().sort(new TreeNode.CompareLon());
                for (int k = 1; k < max - 2 * min + 2; k++) {
                    //αρχικοποίηση δύο ΠΡΟΣΩΡΙΝΩΝ κόμβων
                    TreeNode node1 = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                    TreeNode node2 = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                    int _k = 0;
                    for (; _k < min - 1 + k; _k++) {
                        node1.add_new_child_node(node.getChildren().get(_k));
                    }
                    for (; _k < max + 1; _k++) {
                        node2.add_new_child_node(node.getChildren().get(_k));
                    }
                    //υπολογισμός του overlap που σχηματίζεται μεταξύ των ΠΡΟΣΩΡΙΝΩΝ κόμβων (όχι την περίμετρο)
                    //όταν ταξινομούμε ως προς y
                    double overlapY = node1.getMBR().calculate_overlap(node2.getMBR());
                    //κρατάμε το μικρότερο πιθανό overlap και δίνουμε τιμές στα στους δύο ΚΥΡΙΟΥΣ κόμβους
                    if (overlapY < min_overlap) {
                        min_overlap = overlapY;
                        Node1 = node1;
                        Node2 = node2;
                    }
                    if(overlapY==min_overlap){
                        double area1= Node1.getMBR().getArea() + Node2.getMBR().getArea();
                        double area2= node1.getMBR().getArea() + node2.getMBR().getArea();
                        if(area2<area1){
                            Node1=node1;
                            Node2=node2;
                        }
                    }
                }
            }
        }

        ArrayList<TreeNode> splitted_node = new ArrayList<>(2);
        splitted_node.add(Node1);
        splitted_node.add(Node2);
        return splitted_node;
    }



    /**
     * Συνάρτηση που εισάγει ένα καινούργιο σημείο στο δέντρο
     * @param new_point σημείο που πρέπει να προσθέσουμε στο δέντρο
     */
    public void insert_in_Rtree(Point new_point) {
        node_path = new ArrayList<>();
        TreeNode node = ChooseSubtree(root, new_point);
        //αν το πλήθος των παιδιών του κόμβου είναι μικρότερο από τον μέγιστο αριθμό παιδιών που μπορεί να έχει ένας κόμβος
        //τότε προσθέτουμε σε αυτόν το σημείο αυτό και ανανεώνουμε το μονοπάτι
        if (node.getPoints().size() < max) {
            node.add_new_point(new_point);
            for (TreeNode treeNode : node_path) {
                treeNode.getMBR().setNewDimensions(new_point.getLat(), new_point.getLon());
            }
        }
        //αλλιώς αν το πλήθος των παιδιών ξεπερνάει τον μέγιστο αριθμό παιδιών που μπορεί να έχει ένας κόμβος
        //πρέπει να κάνουμε split
        else {
            node.add_new_point(new_point);
            //κάνουμε split μόνο τη ρίζα
            if (node_path.size() == 1) {
                ArrayList<TreeNode> splitted_node = ChooseSplitAxis(node);
                TreeNode newRoot = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                newRoot.add_new_child_node(splitted_node.get(0));
                newRoot.add_new_child_node(splitted_node.get(1));
                root = newRoot;
                for (TreeNode treeNode : node_path) {
                    treeNode.getMBR().setNewDimensions(new_point.getLat(), new_point.getLon());
                }
            }
            //κάνουμε split μεγαλύτερο μέρος του δέντρου
            else {
                for (int j = node_path.size() - 1; j >= 0; j--) {
                    //αν βρει φύλλο
                    if (node_path.get(j).isLeaf()) {
                        for (int l = 0; l < node_path.get(j - 1).getChildren().size(); l++) {
                            //γίνεται αναζήτηση και διαγραφή του παιδιού που θέλουμε να κάνουμε split
                            //και στη θέση αυτού θα βάλουμε τα δύο καινούργια "χωρισμένα" παιδιά
                            if (node_path.get(j - 1).getChildren().get(l) == node_path.get(j)) {
                                node_path.get(j - 1).getChildren().remove(l);
                                break;
                            }
                        }
                        //προσθήκη καινούργιου παιδιού
                        ArrayList<TreeNode> temp = ChooseSplitAxis(node_path.get(j));
                        node_path.get(j - 1).add_new_child_node(temp.get(0));
                        node_path.get(j - 1).add_new_child_node(temp.get(1));
                    }
                    //αν δεν είναι φύλλο
                    else {
                        //αν το πλήθος των παιδιών ξεπερνάει το όριο
                        if (node_path.get(j).getChildren().size() > max) {
                            //αν βρεις φύλλο
                            if (j - 1 >= 0) {
                                for (int l = 0; l < node_path.get(j - 1).getChildren().size(); l++) {
                                    //γίνεται αναζήτηση και διαγραφή του παιδιού που θέλουμε να κάνουμε split
                                    //και στη θέση αυτού θα βάλουμε τα δύο καινούργια "χωρισμένα" παιδιά
                                    if (node_path.get(j - 1).getChildren().get(l) == node_path.get(j)) {
                                        node_path.get(j - 1).getChildren().remove(l);
                                        break;
                                    }
                                }
                                //προσθήκη καινούργιου παιδιού
                                ArrayList<TreeNode> split = ChooseSplitAxis(node_path.get(j));
                                node_path.get(j - 1).add_new_child_node(split.get(0));
                                node_path.get(j - 1).add_new_child_node(split.get(1));
                            }
                            //τότε χρειαζόμαστε καινούργια ρίζα
                            else {
                                ArrayList<TreeNode> splitAxis = ChooseSplitAxis(node_path.get(j));
                                TreeNode newRoot = new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE);
                                newRoot.add_new_child_node(splitAxis.get(0));
                                newRoot.add_new_child_node(splitAxis.get(1));
                                root = newRoot;
                            }

                        }
                    }
                    for (TreeNode treeNode : node_path) {
                        treeNode.getMBR().setNewDimensions(new_point.getLat(), new_point.getLon());
                    }
                }
            }
        }
    }

    /**
     * Συνάρτηση που ελέγχει εάν ο κόμβος στον οποίο είμαστε είναι στο τελευταίο επίπεδο, δηλαδή έχει μέσα όλο φύλλα
     */
     public boolean isAllLeaf(ArrayList<TreeNode> nodes)  {
        for (TreeNode node: nodes ) {
            if(!node.isLeaf())
                return false;
        }
        return true;
    }


    /**
     * Συνάρτηση που διαγράφει μια τοποθεσία από το δέντρο
     * @param id το id του σημείου που θέλουμε να διαγράψουμε
     * @param locations οι τοποθεσίες όλων των σημείων
     */
    public void delete_from_tree(long id, ArrayList<Location> locations)
    {
        //κόμβος
        TreeNode parent = null;
        //λίστα με όλους τους κόμβους του δέντρου
        ArrayList<TreeNode> temptree = new ArrayList<>();
        temptree.add(getRoot());
        boolean flag = true;
        while (flag){
            for(TreeNode r : temptree) {
                if (isAllLeaf(temptree) || temptree.isEmpty())
                {
                    flag = false;
                    break;
                }
                //ελέγχω αν είναι φύλλο
                if (r.isLeaf()) {
                    //κρατάμε τον γονιό του κόμβου
                    parent = new TreeNode(r.getMBR().getMin_x(),r.getMBR().getMax_x(),r.getMBR().getMin_y(),r.getMBR().getMax_y());
                    Data data = new Data();
                    ArrayList<Point> point = new ArrayList<>(r.getPoints());
                    //παίρνουμε όλα τα στοιχεία που είναι στον κόμβο
                    // βρίσκουμε τις τοποθεσίες των σημείων του
                    for (Point p : point) {
                        Location lc = data.get_block_loc(p.getBlock_id(), p.getSlot_id());
                        long check = lc.getLocation_id();
                        //αν κάποιο από τα σημεία έχει ίδιο id με αυτό που δίνεται ως όρισμα τότε
                        if (check== id) {
                            flag = false;
                            //δημιουργία προσωρινού δέντρου και αφαίρεση από τη λίστα το σημείο αυτό αφαιρώντας την τοποθεσία του
                            ArrayList<Point> temp = parent.getPoints();
                            ArrayList<Location> tempLoc = new ArrayList<>(locations);
                            for (Location l : tempLoc) {
                                if (l.getLocation_id() == check) {
                                    locations.remove(l);
                                }
                            }
                            parent.setPoints(temp);
                            //έλεγχος πληρότητας:
                            //αν ο κόμβος είναι λιγότερο από το 50% γεμάτος με σημεία τότε
                            // θα πρέπει να γίνει η αναδιαμόρφωση στο δέντρο
                            if (parent.getPoints().size() <= 3) {
                                temp = parent.getPoints();
                                parent.setPoints(new ArrayList<>());
                                //διαγραφή των τοποθεσιών που έμειναν ορφανές
                                for (Point t : temp) {
                                    for (Location l : tempLoc) {
                                        if (l.getLocation_id() == data.get_block_loc(t.getBlock_id(), t.getSlot_id()).getLocation_id())
                                            locations.remove(l);
                                    }
                                    //εισαγωγή των ορφανών τοποθεσιών στο δέντρο
                                    FileManager lOSM = new FileManager();
                                    lOSM.add_location(data.get_block_loc(t.getBlock_id(), t.getSlot_id()), data.read_datafile());
                                }

                            }
                        }
                    }

                }
                parent = r;
            }
            temptree.remove(parent);
            assert parent != null;
            temptree.addAll(parent.getChildren());

        }
    }

}
