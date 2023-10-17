/**
 * Αυτή η κλάση αναπαριστά έναν κόμβο στον αλγόριθμο κορυφογραμμής
 */
public class SkylineNode {
    public TreeNode node;
    public Point point;
    public double distance;

    /**
     * Κατασκευαστής με όρισμα έναν κόμβο
     * μέσα σε αυτόν υπολογίζεται και η απόσταση που απέχει ο κόμβος από το σημείο (0,0)
     * @param node ο κόμβος που θέλουμε να βάλουμε στην κορυφογραμμή
     */
    public SkylineNode(TreeNode node)
    {
        this.node = node;
        point = null;
        this.distance = node.getMBR().getMin_x() + node.getMBR().getMin_y();
    }

    /**
     * Κατασκευαστής με όρισμα ένα σημείο
     * μέσα σε αυτόν υπολογίζεται και η απόσταση που απέχει το σημείο από το (0,0)
     * @param point ο κόμβος που θέλουμε να βάλουμε στην κορυφογραμμή
     */
    public SkylineNode(Point point)
    {
        node = null;
        this.point = point;
        distance = point.getLat() + point.getLon();
    }

    /**
     * Getter για την τιμή του x από ένα σημείο ή κόμβο
     * αν είναι σημείο παίρνουμε την τιμή lat
     * αν είναι κόμβος παίρνουμε την κάτω αριστερή τιμή του x από το ορθογώνιο mbr που σχηματίζεται
     */
    public double getX()
    {
        if(node == null)
            return point.getLat();
        return node.getMBR().getMin_x();

    }

    /**
     * Getter για την τιμή του y από ένα σημείο ή κόμβο
     * αν είναι σημείο παίρνουμε την τιμή lon
     * αν είναι κόμβος παίρνουμε την κάτω αριστερή τιμή του y από το ορθογώνιο που σχηματίζεται
     */
    public double getY()
    {
        if(node == null)
            return point.getLon();
        return node.getMBR().getMin_y();

    }

    /**
     * Μέθοδος που κάνει override τη συνάρτηση toString
     * @return το blockid και slotid ενός σημείου
     * χρησιμοποιείται για να εμφανίζονται στην οθόνη τα σημεία αυτά που
     * ανήκουν στην κορυφογραμμή και θέλουμε να ξέρουμε τα χαρακτηριστικά τους
     */
    @Override
    public String toString() {
        return " point : BlockID=" +  point.getBlock_id() + " slotID="+ point.getSlot_id() ;
    }
}
