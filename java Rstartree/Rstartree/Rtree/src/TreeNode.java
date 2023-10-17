import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Αυτή η κλάση απεικονίζει έναν κόμβο στο R*tree
 */
public class TreeNode implements Serializable {
    private final ArrayList<TreeNode> children;
    private ArrayList<Point> points;
    private final Rectangle MBR;
    private double distance_from_point = Double.MAX_VALUE;

    private static final int max = 5;


    /**
     * Constructor
     * @param min_x : η μικρότερη τιμή στον άξονα x
     * @param max_x : η μεγαλύτερη τιμή στον άξονα x
     * @param min_y : η μικρότερη τιμή στον άξονα y
     * @param max_y : η μεγαλύτερη τιμή στον άξονα y
     */
    public TreeNode(double min_x, double max_x, double min_y, double max_y){
        MBR = new Rectangle(min_x,max_x,min_y,max_y);
        children = new ArrayList<>(max);
        points = new ArrayList<>(max);
    }
    public void setPoints(ArrayList<Point> new_points){
        points.clear();
        points=new_points;
    }
    public void setDistance_from_point(double other_distance){
        distance_from_point =other_distance;
    }

    public double getDistance_from_point(){
        return distance_from_point;
    }

    public ArrayList<Point> getPoints(){
        return points;
    }


    public ArrayList<TreeNode> getChildren(){
        return children;
    }
    public Rectangle getMBR(){
        return MBR;
    }

    /**
     * Μέθοδος που επιστρέφει αν ένας κόμβος είναι φύλλο ή όχι
     * ελέγχοντας αν ο κόμβος των παιδιών είναι κενός
     */
    public boolean isLeaf(){
        return children.isEmpty() ;
    }

    /**
     * Μέθοδος που προσθέτει έναν καινούργιο κόμβο ως παιδί αν ο γονιός δεν είναι φύλλο
     * @param new_node Ο κόμβος προς προσθήκη
     */
    public void add_new_child_node(TreeNode new_node){
        children.add(new_node);
        MBR.setNewDimensions(new_node.getMBR().getAllValues().get(0),new_node.getMBR().getAllValues().get(1),new_node.getMBR().getAllValues().get(2),new_node.getMBR().getAllValues().get(3));
    }

    /**
     * Μέθοδος που προσθέτει ένα σημείο ως παιδί αν ο γονιός είναι επίσης φύλλο
     * @param new_point το νέο σημείο
     */
    public void add_new_point(Point new_point){
        points.add(new_point);
        MBR.setNewDimensions(new_point.getLat(),new_point.getLon());
    }

    /**
     * Συνάρτηση που προσθέτει καινούργια παιδιά σε έναν κόμβο
     * @param points λίστα από σημεία που θέλουμε να προσθέσουμε
     */
    public void add_children_nodes(ArrayList<Point> points){
        this.points = points;
    }

    /**
     * Συνάρτηση που συγκρίνει δύο κόμβους σύμφωνα με τις τιμές των ορθογωνίων τους
     * πρώτα γίνεται ταξινόμηση ως προς τη μικρότερη τιμή του x και μετά ως προς τη μεγαλύτερη τιμή του x
     */
    static class CompareLat implements Comparator<TreeNode> {
        @Override
        public int compare(TreeNode node1, TreeNode node2) {
            if(node1.getMBR().getAllValues().get(0) > node2.getMBR().getAllValues().get(0))
                return -1;
            else if(node1.getMBR().getAllValues().get(0) < node2.getMBR().getAllValues().get(0))
                return 1;
            else if(node1.getMBR().getAllValues().get(0).equals(node2.getMBR().getAllValues().get(0)))
                return node2.getMBR().getAllValues().get(1).compareTo(node1.getMBR().getAllValues().get(1));
            return 0;

        }
    }


    /**
     * Συνάρτηση που συγκρίνει δύο κόμβους σύμφωνα με τις τιμές των ορθογωνίων τους
     * πρώτα γίνεται ταξινόμηση ως προς τη μικρότερη τιμή του y και μετά ως προς τη μεγαλύτερη τιμή του y
     */
    static class CompareLon implements Comparator<TreeNode>{
        @Override
        public int compare(TreeNode node1, TreeNode node2) {
            if (node1.getMBR().getAllValues().get(2) > node2.getMBR().getAllValues().get(2))
                return -1;
            else if (node1.getMBR().getAllValues().get(2) < node2.getMBR().getAllValues().get(2))
                return 1;
            else if (node1.getMBR().getAllValues().get(2).equals(node2.getMBR().getAllValues().get(2)))
                return node2.getMBR().getAllValues().get(3).compareTo(node1.getMBR().getAllValues().get(3));
            return 0;
        }
    }
}

