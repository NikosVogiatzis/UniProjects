import java.util.Comparator;
import java.io.Serializable;

/**
 * Η κλάση αυτή αναπαριστά ένα σημείο σε έναν δισδιάστατο χώρο
 */
public class Point implements Serializable {
    private final double lat;
    private final double lon;
    private int block_id;
    private int slot_id;
    private double distance;

    /**
     * Κατασκευαστής της κλάσης Point με 4 ορίσματα
     * @param lat η παράμετρος x του σημείου
     * @param lon η παράμετρος y του σημείου
     * @param blockid το block του datafile στο οποίο είναι αποθηκευμένο το σημείο
     * @param slotid το slot του blockid του datafile στο οποίο έχε αποθηκευτεί το σημείο
     */
    public Point(double lat, double lon , int blockid, int slotid){
        this.lat=lat;
        this.lon=lon;
        this.block_id =blockid;
        this.slot_id =slotid;

    }

    /**
     * Κατασκευαστής της κλάσης Point με 2 ορίσματα
     * @param lat η παράμετρος x του σημείου
     * @param lon η παράμετρος y του σημείου
     */
    public Point(double lat, double lon){
        this.lat=lat;
        this.lon=lon;
    }

    public void setDistance_point(double distance) {
        this.distance = distance;
    }
    public int getBlock_id(){
        return block_id;
    }

    public int getSlot_id(){
        return slot_id;
    }

    public double getLon(){
        return lon;
    }
    public double getLat(){
        return lat;
    }
    public double getDistance_point() {
        return distance;
    }

    /**
     * Η μέθοδος αυτή βρίσκει τη Manhattan απόσταση μεταξύ δύο σημείων
     * @param point το 2ο σημείο σύγκρισης
     */
    public double find_distance_from_point(Point point){
        return Math.abs(lat-point.getLat())+Math.abs(lon-point.getLon());
    }

    /**
     * Μέθοδος που συγκρίνει lat τιμές δύο σημείων
     */
    static class PointsComparatorLat implements Comparator<Point> {
        @Override
        public int compare(Point point1, Point point2) {
            return Double.compare(point2.getLat(), point1.getLat());
        }
    }

    /**
     * Συνάρτηση που συγκρίνει lon τιμές δύο σημείων
     */
    static class PointsComparatorLon implements Comparator<Point> {
        @Override
        public int compare(Point point1, Point point2) {
            return Double.compare(point2.getLon(), point1.getLon());
        }
    }



}
