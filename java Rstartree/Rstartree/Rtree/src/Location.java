import static java.lang.Math.abs;

/**
 * Η κλάση αυτή αναπαριστά μια τοποθεσία με lat (x) και lon (y) στο osm αρχείο
 */
public class Location implements Comparable<Location>{
    private long location_id;
    private double lat, lon;
    private double distance;

    /**
     * Κατασκευαστής της κλάσης Location με 3 ορίσματα
     * @param location_id το id του κάθε node στο αρχείο .osm
     * @param lat η παράμετρος x του σημείου
     * @param lon η παράμετρος y του σημείου
     */
    public Location(long location_id, double lat, double lon) {
        this.location_id = location_id;
        this.lat = lat;
        this.lon = lon;
        distance = Double.MAX_VALUE;
    }

    public void setDistance(double new_distance){
        distance=new_distance;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public long getLocation_id() {
        return location_id;
    }

    /**
     * Υπολογισμός της απόστασης Manhattan μεταξύ 2 τοποθεσιών
     * @param lat η παράμετρος x του ξένου σημείου
     * @param lon η παράμετρος y του ξένου σημείου
     */
    public double find_manhattan_distance_between_two_points(double lat, double lon) {
        distance = abs((lat - this.lat)) + abs(lon - this.lon);
        return distance;
    }

    /**
     * Getter για Distance
     * @return Μήνυμα λάθους και τερματισμός αν η τιμή της απόστασης είναι αυτή της αρχικοποίησης
     *          διαφορετικά την πραγματική απόσταση
     */
    public double getDistance() {
        if (distance == Double.MAX_VALUE) {
            System.out.println("Error with distance value");
            System.exit(-1);
        }
        return distance;
    }

    /**
     * Κάνουμε override τη συνάρτηση toString
     * Συνάρτηση που μετατρέπει τα στοιχεία της τοποθεσίας σε συμβολοσειρά έτσι ώστε να μπορεί να εκτυπωθεί στην οθόνη
     */
    @Override
    public String toString() {
        return "id:" + location_id + "   latitude= " + lat + "   longitude= " + lon+ "  distance= "+distance;
    }

    /**
     * Κάνουμε override τη συνάρτηση compareTo
     * Συνάρτηση που συγκρίνει 2 τοποθεσίες μεταξύ τους
     * @param second_location η δεύτερη τοποθεσία
     * @return :
     * 1 αν το distance_πρώτης > distance_δεύτερης
     *-1 αλλιώς,
     * 0 same distances
     */
    @Override
    public int compareTo(Location second_location) {
        if (distance > second_location.getDistance()) {
            return 1;
        } else if (distance < second_location.getDistance()) {
            return -1;
        }
        return 0;
    }
}
