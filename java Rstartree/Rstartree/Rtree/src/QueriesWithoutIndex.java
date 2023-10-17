import java.util.ArrayList;
import java.util.Collections;

/**
 * Η κλάση αυτή υλοποιεί ερωτήματα περιοχής και κοντινότερων γειτόνων χωρίς index
 */
public class QueriesWithoutIndex {

    public QueriesWithoutIndex(){}
    /**
     * Συνάρτηση που υλοποιεί σειριακά το ερώτημα κοντινότερων γειτόνων
     * Επιστρέφει τους k κοντινότερους γείτονες από το στοιχείο middle
     * @param locations όλοι οι πιθανοί γείτονες
     * @param middle τοποθεσία για την οποία ψάχνουμε τους κοντινότερους γείτονες
     * @param k αριθμός επιθυμητών γειτόνων
     */
    public ArrayList<Location> knn_without_index(ArrayList<Location> locations, Location middle, int k) {
        ArrayList<Location> k_neighbors = new ArrayList<>();
        for (Location location : locations)
            location.find_manhattan_distance_between_two_points(middle.getLat(), middle.getLon());
        Collections.sort(locations);
        for (int i = 0; i < k; i++) {
            k_neighbors.add(locations.get(i));
            Point k_distance = new Point(locations.get(i).getLat(), locations.get(i).getLon());
            k_distance.setDistance_point(k_distance.find_distance_from_point(new Point(middle.getLat(), middle.getLon())));
        }
        return k_neighbors;
    }

    /**
     * Συνάρτηση που υλοποιεί σειριακά το ερώτημα περιοχής
     * Επιστρέφει τους γείτονες που βρίσκονται μέσα στον "κύκλο" γύρω από το στοιχείο middle
     * @param middle η τοποθεσία από την οποία μετράμε την ακτίνα
     * @param radius η ακτίνα εντός της οποίας ψάχνουμε τις τοποθεσίες
     * @param locations όλες οι τοποθεσίες
     */
    public static ArrayList<Location> range_query_without_index(Location middle, double radius, ArrayList<Location> locations) {
        ArrayList<Location> locations_in_range = new ArrayList<>();
        for (Location location : locations) {
            if (location.find_manhattan_distance_between_two_points(middle.getLat(), middle.getLon()) <= radius)
                locations_in_range.add(location);
        }
        return locations_in_range;
    }
}
