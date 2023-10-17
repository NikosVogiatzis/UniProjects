import java.util.ArrayList;
import java.util.Comparator;

/**
 * Η κλάση αυτή υλοποιεί την bottom_up (μαζική) κατασκευή του R*tree
 */
public class BottomUp {
    private ArrayList<Point> points;
    private final int upper_limit = 5;

    public BottomUp()
    {
        points = new ArrayList<>();
    }

    /**
     * Συνάρτηση που παίρνει όλα τα σημεία και τα ταξινομεί και μετά τα χωρίζει σε ομάδες των 5 σημείων (upper_limit)
     * τις βάζει σε κουτάκια
     */
    public void execute_bottom_up() throws Exception {
        FileManager file_mngr = new FileManager();
        points = file_mngr.parse_points();
        points.sort(Comparator.comparing(Point::getLat));
        ArrayList<Point> block = new ArrayList<>();
        ArrayList <TreeNode> rsn = new ArrayList<>();
        double min_x, max_x, min_y, max_y;
        for (int i = 0; i< points.size(); i++)
        {
            block.add(points.get(i));
            //αν το πλήθος των σημείων φτάσει τo upper_limit τότε
            //σταματάει και πηγαίνει και βρίσκει την πάνω δεξιά και κάτω αριστερή γωνία
            if(i % upper_limit == 0) {
                //μικρότερο σημείο στον άξονα x (το παίρνουμε από το σημείο που μπήκε πρώτο στη λίστα)
                min_x = block.get(0).getLat();
                //μεγαλύτερο σημείο στον άξονα x (το παίρνουμε από το σημείο που μπήκε τελευταίο στη λίστα)
                max_x = block.get(block.size()-1).getLat();
                //πρέπει να βρούμε το min max
                min_y = block.stream().min(Comparator.comparing(Point::getLon)).get().getLon();
                max_y = block.stream().max(Comparator.comparing(Point::getLon)).get().getLon();
                //προσθήκη του κόμβου στο δέντρο
                rsn.add(new TreeNode(min_x,max_x,min_y,max_y));
                rsn.get(rsn.size()-1).add_children_nodes(block);
                //αδειάζει το μπλοκ για να μπουν τα επόμενα 5 σημεία
                block = new ArrayList<>();
            }
        }
        //σταματάει μόλις γεμίσει το συγκεκριμένο επίπεδο στο οποίο βρισκόμασταν
        int count = rsn.size();
        ArrayList<TreeNode> temp = new ArrayList<>(rsn);
        //υπολογίζεται το ταβάνι της διαίρεσης μεταξύ του μεγέθους του δέντρου
        count = (int)Math.ceil((double)count/ upper_limit);
        for (int i = 0; i < count; i++) {
            //σε περίπτωση που υπάρχουν λιγότερο από 5 στοιχεία στο μπλοκ θα τρέχει
            int test = Math.min(temp.size(),5);
            double y = Double.MAX_VALUE,yy = Double.MIN_VALUE;
            for (int j = 0; j <test; j++) {
                if(temp.get(j).getMBR().getMin_y() < y)
                    y = temp.get(j).getMBR().getMin_y();
                if(temp.get(j).getMBR().getMax_y()> yy)
                    yy = temp.get(j).getMBR().getMax_y();
            }
            rsn.add(new TreeNode(temp.get(0).getMBR().getMin_x(),temp.get(Math.min(temp.size()-1,4)).getMBR().getMax_x(),y,yy));
            for (int j = 0; j <test; j++) {
                rsn.get(rsn.size()-1).add_new_child_node(temp.get(j));
            }
            temp.subList(0, test).clear();

        }
        //χτίσιμο ολόκληρου δέντρου
        while (count > 1) 
        {
            temp = new ArrayList<>();
            int test = Math.min(count, 5);
            count = (int)Math.ceil((double)count/ upper_limit);
            for (int i = 0; i < count; i++) {

                temp = new ArrayList<>();
                for (int k = 0; k <test ; k++) {
                    temp.add(rsn.get(rsn.size() -1 -k));
                }
                //υπολογισμός μεγαλύτερου και μικρότερου y
                double y = Double.MAX_VALUE, yy = Double.MIN_VALUE;
                for (TreeNode treeNode : temp) {
                    if (treeNode.getMBR().getMin_y() < y)
                        y = treeNode.getMBR().getMin_y();
                    if (treeNode.getMBR().getMax_y() > yy)
                        yy = treeNode.getMBR().getMax_y();
                }
                rsn.add(new TreeNode(temp.get(0).getMBR().getMin_x(), temp.get(temp.size() - 1).getMBR().getMax_x(), y, yy));

                for (TreeNode treeNode : temp) rsn.get(rsn.size() - 1).add_new_child_node(treeNode);

            }

        }
        //υπολογισμός μεγαλύτερου και μικρότερου y
        double y = Double.MAX_VALUE, yy = Double.MIN_VALUE;
        for (TreeNode r_star_Node : temp) {
            if (r_star_Node.getMBR().getMin_y() < y)
                y = r_star_Node.getMBR().getMin_y();
            if (r_star_Node.getMBR().getMax_y() > yy)
                yy = r_star_Node.getMBR().getMax_y();
        }
        //προσθήκη στη ρίζα
        TreeNode root =new TreeNode(temp.get(0).getMBR().getMin_x(),temp.get(temp.size()-1).getMBR().getMax_x(),y,yy);
        for (TreeNode treeNode : temp) {
            root.add_new_child_node(treeNode);
        }
    }

}
