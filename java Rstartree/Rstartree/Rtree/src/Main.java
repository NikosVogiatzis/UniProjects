import java.util.ArrayList;

public class Main {

    private static Tree Rtree = new Tree(new TreeNode(Double.MAX_VALUE, Double.MIN_VALUE, Double.MAX_VALUE, Double.MIN_VALUE));

    //Δεδομένα στοιχείου που θα κάνουμε εισαγωγή διαγραφή αλλά και των queries
    private static final int k = 5;
    private static final double range = 10;
    private static final long id = 35836525;
    private static final double lat = 38.444235;
    private static final double lon = 23.853263;
    /**
     * Η μέθοδος αυτή είναι υπεύθυνη για τη δημιουργία του R*tree
     * @param fileManager Αντικείμενο της κλάσης FileManager που μας επιτρέπει να προσπελάσουμε μεθόδους της
     * @return Το R*tree που διαβάζεται από το indexfile
     */
    private static void MakeIndex(FileManager fileManager)  {
        Rtree = fileManager.read_tree_from_indexfile();
    }

    private static void InsertNewLocation(ArrayList<Location> locations, Location newLocation) {
        System.out.println("\n---------------->\tΕισαγωγή νέου στοιχείου\t<---------------------- \n\t" + newLocation);
        long start = System.currentTimeMillis();
        int size = locations.size();
        FileManager osm = new FileManager();
        osm.add_location(newLocation, locations);
        Data data2 = new Data();
        locations = data2.read_datafile();
        if(size !=locations.size())
            System.out.println("Η νέα τοποθεσία προστέθηκε επιτυχώς!");
        else
            System.out.println("Πρόβλημα στην προσθήκη της νέας τοποθεσίας!");
        long finish = System.currentTimeMillis() - start;
        System.out.println("Ο χρόνος που χρειάστηκε: " + finish + "ms");
        System.out.println("-------------------------------------------------------------------\n");

    }

    public static void DeleteLocation(ArrayList<Location> locations, Location newLocation,  long id) {
        System.out.println("\n---------------->\tΔιαγραφή του στοιχείου\t<---------------------- \n\t" + newLocation);

        long start = System.currentTimeMillis();
        int size = locations.size();
        Rtree.delete_from_tree(id,locations);
        if(size != locations.size())
            System.out.println("Η τοποθεσία διαγράφηκε επιτυχώς!");
        else
            System.out.println("Πρόβλημα κατά τη διαγραφή της τοποθεσίας!");
        long finish = System.currentTimeMillis() - start;
        System.out.println("Ο Χρόνος που χρειάστηκε: " + finish + "ms");
        System.out.println("-------------------------------------------------------------------\n");

    }

    public static void RangeQueryWithoutIndex(double lat, double lon, ArrayList<Location> locations)  {
        System.out.println("\n---------------->\tΕρώτημα περιοχής χωρίς χρήση καταλόγου!\t<---------------------- \n\t" + "range: " + range + " lat: " + lat + " lon: " + lon );

        long start = System.currentTimeMillis();
        ArrayList<Location> locations_in_range = QueriesWithoutIndex.range_query_without_index(new Location(-1, lat, lon), range, locations);
//         Εμφάνιση Αποτελεσμάτων
        for (Location neighbor : locations_in_range)
            System.out.println(neighbor.toString());
        long finish = System.currentTimeMillis() - start;
        System.out.println("Ο χρόνος που χρειάστηκε το ερώτημα: " + finish + "ms");

    }

    public static void RangeQueryWithIndex(QueriesWithIndex qwi, double lat, double lon) {
        System.out.println("\n---------------->\tΕρώτημα περιοχής με χρήση καταλόγου!\t<---------------------- \n\t\t" + "range: " + range + " lat: " + lat + " lon: " + lon );
        long start= System.currentTimeMillis();
        ArrayList<Location> locations_in_range = qwi.range_query_with_index(new Point(lat, lon), range);
        long finish = System.currentTimeMillis() - start;
//         Εμφάνιση αποτελεσμάτων
        for (Location neighbor : locations_in_range)
            System.out.println(neighbor.toString());
        System.out.println("Ο χρόνος που χρειάστηκε το ερώτημα: " + finish + "ms");
    }

    public static void KnnWithoutIndex(QueriesWithoutIndex sKNN, int k , ArrayList<Location> locations) {
        System.out.println("\n------------------->\tΕρώτημα εύρεσης k κοντινότερων γειτόνων χωρίς κατάλογο για k: " + k + " lat: " + lat + " lon: " + lon + "\t<----------------");
        Location middle = new Location(-1, lat, lon);
        long start = System.currentTimeMillis();
        ArrayList<Location> distances = sKNN.knn_without_index(locations, middle, k);
        for (Location neighbor : distances)
            System.out.println(neighbor.toString());
        long finish = System.currentTimeMillis() - start;
        System.out.println("Ο χρόνος που χρειάστηκε το ερώτημα: " + finish + "ms");
    }

    public static void KnnWithIndex(QueriesWithIndex qwi)  {
        System.out.println("\n------------------->\tΕρώτημα εύρεσης k κοντινότερων γειτόνων με κατάλογο για k: " + k + " lat: " + lat + " lon: " + lon + "\t<----------------");
        Point point = new Point(lat, lon);
        long start = System.currentTimeMillis();
        ArrayList<Location> result_list = qwi.knn_with_index(point, k);
        for (Location neighbor : result_list)
            System.out.println(neighbor.toString());
        long finish = System.currentTimeMillis() - start;
        System.out.println("Ο χρόνος που χρειάστηκε το ερώτημα: " + finish + "ms");
    }

    public static void Skyline() {

        System.out.println("\n--------------------------->\tΕρώτημα κορυφογραμμής!\t<---------------------------");
        long start = System.currentTimeMillis();
        Skyline skl = new Skyline();
        ArrayList<SkylineNode> results = skl.calculateSkyline(Rtree);
        for (SkylineNode node : results)
            System.out.println(node);
        long finish = System.currentTimeMillis() - start;
        System.out.println("Ο χρόνος που χρειάστηκε το ερώτημα: " + finish + "ms");
    }

    public static void BottomUp() throws Exception {
        System.out.println("\n---------------------->\tΥλοποίηση μαζικής κατασκευής δέντρου ΒottomUp\t<------------------");
        long start= System.currentTimeMillis();
        BottomUp bottomUp = new BottomUp();
        bottomUp.execute_bottom_up();
        long finish= System.currentTimeMillis() - start ;
        System.out.println("Ο χρόνος που χρειάστηκε το ερώτημα: " + finish + "ms");
    }
    public static void main(String[] args) throws Exception {

        Location newLocation = new Location(id, lat, lon);

        FileManager file_mngr = new FileManager();
        Data data = new Data();

        ArrayList<Location> locations;
        QueriesWithoutIndex sKNN = new QueriesWithoutIndex();

        file_mngr.create_datafile(); //OSM management
        locations = data.read_datafile(); //Εισαγωγές στο datafile.txt

        MakeIndex(file_mngr); // Κλήση συνάρτησης δημιουργίας του καταλόγου


        InsertNewLocation(locations, newLocation); // Κλήση της μεθόδου εισαγωγής νέου στοιχείου

        DeleteLocation(locations, newLocation, id); // Κλήση της μεθόδου διαγραφής στοιχείου

        RangeQueryWithoutIndex(lat, lon, locations); // Κλήση της μεθόδου για ερωτήματα περιοχής χωρίς κατάλογο

      QueriesWithIndex qwi = new QueriesWithIndex(Rtree.getRoot());

        RangeQueryWithIndex(qwi, lat, lon); // Κλήση της μεθόδου για ερωτήματα περιοχής με κατάλογο

        KnnWithoutIndex(sKNN, k, locations); // Κλήση της μεθόδου εύρεσης Κ-κοντινότερων γειτόνων χωρίς κατάλογο

        KnnWithIndex(qwi); // Κλήση της μεθόδου εύρεσης Κ-κοντινότερων γειτόνων με κατάλογο

        Skyline(); // Κλήση μεθόδου για Skyline

        BottomUp(); // Κλήση μεθόδου για BottomUp
    }

}





