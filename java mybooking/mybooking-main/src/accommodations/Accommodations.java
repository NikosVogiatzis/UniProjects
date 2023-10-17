package accommodations;

import accommodations.reservervations.Date;
import accommodations.reservervations.Reservation;
import users.Customers;
import users.Providers;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Η κλάση Accommodations υλοποιεί τα καταλύματα. Λειτουργεί ως πρόγονος κλάση για τις
 * {@link HotelRooms} και {@link PrivateAccommodation} και περιέχει βασικά χαρακτηριστικά
 * τους όπως τα τετραγωνικά μέτρα, την τιμή,την χωρητικότητα ατόμων,τη λίστα
 * με τα ειδικά χαρακτηριστικά του καταλύματος και το ειδικό αναγνωριστικό καταλύματος. Ακόμα περιέχει
 * δύο λίστες για την αποθήκευση όλων τον καταχωρημένων καταλυμάτων (δωματίων, ιδιωτικών καταλυμάτων)
 * <p>
 * Υλοποιεί: Αναζήτηση ιδιωτικού καταλύματος με κριτήρια -> {@link #SearchPrivateAccommodations(String, int, ArrayList, List)},
 * αναζήτηση ξενοδοχειακού δωματίου με κριτήρια -> {@link #SearchHotelRooms(String, int, ArrayList, List)},
 * ακύρωση κράτησης για ιδιωτικό κατάλυμα -> {@link #CancelReservationPrivateAccommodation(int, Customers)},
 * ακύρωση κράτησης για ξενοδοχειακό δωμάτιο -> {@link #CancelReservationHotelRoom(int, Customers)},
 * εύρεση και επιστροφή κρατήσεων σε ξενοδοχειακά δωμάτια για έναν συγκεκριμένο χρήστη -> {@link #UserHotelReservations(Customers)},
 * εύρεση και επιστροφή κρατήσεων σε ιδιωτικά καταλύματα για έναν συγκεκριμένο χρήστη -> {@link #UserPrivateReservations(Customers)},
 *
 * </p>
 */
public class Accommodations implements Serializable {
    private static ArrayList<HotelRooms> rooms;
    private static ArrayList<PrivateAccommodation> airbnb;
    private static int baseIdentifier;
    private static int reservationsBaseIdentifier;
    private static int imageIdentifier;
    private int squareMetres;
    private int price;
    private int capacity;
    private String imageName;
    private List<String> characteristics;


    public Accommodations() {
        rooms = new ArrayList<>();
        airbnb = new ArrayList<>();
        baseIdentifier = 1000;
        reservationsBaseIdentifier = 100;
        imageIdentifier = 0;
        this.squareMetres = 0;
        this.capacity = 0;
        characteristics = new ArrayList<>();
        CreateDefaultAccommodation();
    }

    public Accommodations(int squareMetres, int price, int capacity, List<String> characteristics, String imageName) {
        this.squareMetres = squareMetres;
        this.price = price;
        this.capacity = capacity;
        this.characteristics = characteristics;
        this.imageName = imageName;
    }


    public int getNumberOfAccommodations() {
        return rooms.size() + airbnb.size();
    }

    public List<String> getCharacteristics() {
        return characteristics;
    }

    public int getSquareMetres() {
        return squareMetres;
    }

    public static void setBaseIdentifier(int baseIdentifier) {
        Accommodations.baseIdentifier = baseIdentifier;
    }

    public static void setReservationsBaseIdentifier(int reservationsBaseIdentifier) {
        Accommodations.reservationsBaseIdentifier = reservationsBaseIdentifier;
    }

    public static void setImageIdentifier(int imageIdentifier) {
        Accommodations.imageIdentifier = imageIdentifier;
    }

    public void setSquareMetres(int squareMetres) {
        this.squareMetres = squareMetres;
    }

    public ArrayList<HotelRooms> getRooms() {
        return rooms;
    }

    public ArrayList<PrivateAccommodation> getAirbnb()
    {
        return airbnb;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCharacteristics(List<String> characteristics) {
        this.characteristics = characteristics;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getImageName() {
        return imageName;
    }

    /**
     * Η μέθοδος αυτή φιλτράρει τα ιδιωτικά καταλύματα και επιστρέφει αυτά με οποία τα κριτήρια αναζήτησης
     * που έδωσε ο χρήστης ταυτίζονται
     *
     * @param address         Διεύθυνση ξενοδοχείου .Υποχρεωτικό πεδίο για την αναζήτηση δωματίου
     * @param capacity        Χωρητικότητα
     * @param ranges          Λίστα με το εύρος τετραγωνικών/τιμής
     * @param characteristics Λίστα με τα χαρακτηριστικά του δωματίου
     * @return Λίστα με τα ιδιωτικά καταλύματα που βρέθηκαν βάσει των κριτηρίων που δώθηκαν
     * ως παράμετροι. null Αν το πεδίο της διεύθυνσης είναι κενό
     */
    public ArrayList<PrivateAccommodation> SearchPrivateAccommodations(String address, int capacity,
                                                                       ArrayList<ArrayList<Integer>> ranges,
                                                                       List<String> characteristics) {
        HashSet<PrivateAccommodation> filtered = new HashSet<>();

        for (PrivateAccommodation accommodation : airbnb) {

            if (accommodation.getAddress().equals(address) && capacity <= 0 && ranges.get(0).get(0) <= 0
                    && ranges.get(1).get(0) <= 0 && characteristics == null) {
                filtered.add(accommodation);
            }

            if (accommodation.getAddress().equalsIgnoreCase(address) && capacity > 0) {
                if (ranges.get(0).get(0) <= 0 && ranges.get(1).get(0) <= 0 && characteristics == null) {
                    if (capacity == accommodation.getCapacity())
                        filtered.add(accommodation);
                }

                if (ranges.get(0).get(0) > 0 && ranges.get(1).get(0) <= 0 && characteristics == null) {
                    if (capacity == accommodation.getCapacity() && accommodation.getSquareMetres() >= ranges.get(0).get(0)
                            && accommodation.getSquareMetres() <= ranges.get(0).get(1)) {
                        filtered.add(accommodation);
                    }
                }

                if (ranges.get(0).get(0) > 0 && ranges.get(1).get(0) > 0 && characteristics == null) {
                    if (capacity == accommodation.getCapacity() && accommodation.getSquareMetres() >= ranges.get(0).get(0)
                            && accommodation.getSquareMetres() <= ranges.get(0).get(1) &&
                            accommodation.getPrice() >= ranges.get(1).get(0) && accommodation.getPrice() <= ranges.get(1).get(1)) {
                        filtered.add(accommodation);
                    }
                }

                if (ranges.get(0).get(0) > 0 && ranges.get(1).get(0) > 0 && characteristics != null) {
                    if (capacity == accommodation.getCapacity() && accommodation.getSquareMetres() >= ranges.get(0).get(0)
                            && accommodation.getSquareMetres() <= ranges.get(0).get(1) &&
                            accommodation.getPrice() >= ranges.get(1).get(0) && accommodation.getPrice() <= ranges.get(1).get(1) &&
                            accommodation.ContainsAtLeastOneCharacteristicPrivate(characteristics)) {
                        filtered.add(accommodation);
                    }

                }

                if (ranges.get(0).get(0) > 0 && ranges.get(1).get(0) <= 0 && characteristics != null) {
                    if (accommodation.getCapacity() == capacity && accommodation.getSquareMetres() >= ranges.get(0).get(0)
                            && accommodation.getSquareMetres() <= ranges.get(1).get(0)
                            && accommodation.ContainsAtLeastOneCharacteristicPrivate(characteristics)) {
                        filtered.add(accommodation);
                    }
                }

                if (ranges.get(0).get(0) <= 0 && ranges.get(1).get(0) > 0 && characteristics == null) {
                    if (capacity == accommodation.getCapacity() && accommodation.getPrice() >= ranges.get(1).get(0)
                            && accommodation.getPrice() <= ranges.get(1).get(1)) {
                        filtered.add(accommodation);
                    }
                }
                if (ranges.get(0).get(0) <= 0 && ranges.get(1).get(0) > 0 && characteristics != null) {
                    if (capacity == accommodation.getCapacity() && accommodation.getPrice() >= ranges.get(1).get(0)
                            && accommodation.getPrice() <= ranges.get(1).get(1)
                            && accommodation.ContainsAtLeastOneCharacteristicPrivate(characteristics)) {
                        filtered.add(accommodation);
                    }
                }

                if (ranges.get(0).get(0) <= 0 && ranges.get(1).get(0) <= 0 && characteristics != null) {
                    if (capacity == accommodation.getCapacity() && accommodation.ContainsAtLeastOneCharacteristicPrivate(characteristics))
                        filtered.add(accommodation);
                }


            }

            if (accommodation.getAddress().equals(address) && capacity < 0) {
                if (ranges.get(0).get(0) > 0 && ranges.get(1).get(0) <= 0 && characteristics == null) {
                    if (accommodation.getSquareMetres() >= ranges.get(0).get(0)
                            && accommodation.getSquareMetres() <= ranges.get(0).get(1)) {
                        filtered.add(accommodation);
                    }
                }

                if (ranges.get(0).get(0) > 0 && ranges.get(1).get(0) > 0 && characteristics == null) {
                    if (accommodation.getSquareMetres() >= ranges.get(0).get(0)
                            && accommodation.getSquareMetres() <= ranges.get(0).get(1) &&
                            accommodation.getPrice() >= ranges.get(1).get(0) && accommodation.getPrice() <= ranges.get(1).get(1)) {
                        filtered.add(accommodation);
                    }
                }

                if (ranges.get(0).get(0) > 0 && ranges.get(1).get(0) > 0 && characteristics != null) {
                    if (accommodation.getSquareMetres() >= ranges.get(0).get(0)
                            && accommodation.getSquareMetres() <= ranges.get(0).get(1) &&
                            accommodation.getPrice() >= ranges.get(1).get(0) && accommodation.getPrice() <= ranges.get(1).get(1) &&
                            accommodation.ContainsAtLeastOneCharacteristicPrivate(characteristics)) {
                        filtered.add(accommodation);
                    }

                }

                if (ranges.get(0).get(0) > 0 && ranges.get(1).get(0) <= 0 && characteristics != null) {
                    if (accommodation.getSquareMetres() >= ranges.get(0).get(0)
                            && accommodation.getSquareMetres() <= ranges.get(1).get(0)
                            && accommodation.ContainsAtLeastOneCharacteristicPrivate(characteristics)) {
                        filtered.add(accommodation);
                    }
                }

                if (ranges.get(0).get(0) <= 0 && ranges.get(1).get(0) > 0 && characteristics == null) {
                    if (accommodation.getPrice() >= ranges.get(1).get(0)
                            && accommodation.getPrice() <= ranges.get(1).get(1)) {
                        filtered.add(accommodation);
                    }
                }
                if (ranges.get(0).get(0) <= 0 && ranges.get(1).get(0) > 0 && characteristics != null) {
                    if (accommodation.getPrice() >= ranges.get(1).get(0)
                            && accommodation.getPrice() <= ranges.get(1).get(1)
                            && accommodation.ContainsAtLeastOneCharacteristicPrivate(characteristics)) {
                        filtered.add(accommodation);
                    }
                }

                if (ranges.get(0).get(0) <= 0 && ranges.get(1).get(0) <= 0 && characteristics != null) {
                    if (accommodation.ContainsAtLeastOneCharacteristicPrivate(characteristics))
                        filtered.add(accommodation);
                }
            }
        }
        return new ArrayList<>(filtered);
    }

    /**
     * Μέθοδος που χρησιμοποιείται στην αναζήτηση δωματίου. Βρίσκει και επιστρέφει αν έστω ένα χαρακτηριστικό
     * από τη λίστα που δίνεται ως όρισμα περιέχεται σε κάποιο από τα δωμάτια των ξενοδοχείων
     *
     * @param characteristics Λίστα με τα χαρακτηριστικά του δωματίου για το οποίο ο χρήστης κάνει αναζήτηση
     * @return true/false ανάλογα με το αν περιέχεται ή όχι έστω ένα χαρακτηριστικό σε κάποιο δωμάτιο
     */
    public boolean ContainsAtLeastOneCharacteristicRoom(List<String> characteristics) {
        boolean contains = false;
        if (characteristics != null) {
            for (String has : characteristics) {
                for (String index : this.getCharacteristics()) {
                    if (index.equalsIgnoreCase(has)) {
                        contains = true;
                        break;
                    }
                }
            }
        }
        return contains;
    }

    /**
     * Μέθοδος που χρησιμοποιείται στην αναζήτηση ιδιωτικού καταλύματος. Βρίσκει και επιστρέφει αν έστω ένα χαρακτηριστικό
     * από τη λίστα που δίνεται ως όρισμα περιέχεται σε κάποιο από τα ιδιωτικά καταλύματα
     *
     * @param characteristics Λίστα με τα χαρακτηριστικά του ιδιωτικού καταλύματος για το οποίο ο χρήστης κάνει αναζήτηση
     * @return true/false ανάλογα με το αν περιέχεται ή όχι έστω ένα χαρακτηριστικό σε κάποιο ιδιωτικό κατάλυμα
     */
    public boolean ContainsAtLeastOneCharacteristicPrivate(List<String> characteristics) {
        boolean contains = false;
        if (characteristics != null) {
            for (String has : characteristics) {
                for (String index : this.getCharacteristics()) {
                    if (index.equalsIgnoreCase(has)) {
                        contains = true;
                        break;
                    }
                }
            }
        }
        return contains;
    }


    /**
     * Η μέθοδος αυτή φιλτράρει τα ξενοδοχειακά δωμάτια και επιστρέφει αυτά με οποία τα κριτήρια αναζήτησης
     * που έδωσε ο χρήστης ταυτίζονται
     * @param address Διεύθυνση ξενοδοχείου .Υποχρεωτικό πεδίο για την αναζήτηση δωματίου
     * @param capacity Χωρητικότητα
     * @param ranges Λίστα με το εύρος τετραγωνικών/τιμής
     * @param characteristics Λίστα με τα χαρακτηριστικά του δωματίου
     * @return Λίστα με τα δωμάτια που βρέθηκαν βάσει των κριτηρίων που δώθηκαν
     *         ως παράμετροι. null Αν το πεδίο της διεύθυνσης είναι κενό
     */
    public ArrayList<HotelRooms> SearchHotelRooms(String address, int capacity,
                                                  ArrayList<ArrayList<Integer>> ranges,
                                                  List<String> characteristics) {
        ArrayList<HotelRooms> filtered = new ArrayList<>();
        if (address.equals("")) {
            System.out.println("fsfd");
            return null;
        }
        for (HotelRooms room : rooms) {


            if (room.getAddress().equalsIgnoreCase(address) && capacity <= 0 && ranges.get(0).get(0) <= 0
                    && ranges.get(1).get(0) <= 0 && characteristics == null) {
                filtered.add(room);
            }

            if (room.getAddress().equals(address) && capacity > 0) {
                if (ranges.get(0).get(0) <= 0 && ranges.get(1).get(0) <= 0 && characteristics == null) {
                    if (capacity == room.getCapacity())
                        filtered.add(room);
                }
                if (ranges.get(0).get(0) > 0 && ranges.get(1).get(0) <= 0 && characteristics == null) {
                    if (capacity == room.getCapacity() && room.getSquareMetres() >= ranges.get(0).get(0)
                            && room.getSquareMetres() <= ranges.get(0).get(1)) {
                        filtered.add(room);
                    }
                }
                if (ranges.get(0).get(0) > 0 && ranges.get(1).get(0) > 0 && characteristics == null) {
                    if (capacity == room.getCapacity() && room.getSquareMetres() >= ranges.get(0).get(0)
                            && room.getSquareMetres() <= ranges.get(0).get(1) &&
                            room.getPrice() >= ranges.get(1).get(0) && room.getPrice() <= ranges.get(1).get(1)) {
                        filtered.add(room);
                    }
                }
                if (ranges.get(0).get(0) > 0 && ranges.get(1).get(0) > 0 && characteristics != null) {
                    if (capacity == room.getCapacity() && room.getSquareMetres() >= ranges.get(0).get(0)
                            && room.getSquareMetres() <= ranges.get(0).get(1) &&
                            room.getPrice() >= ranges.get(1).get(0) && room.getPrice() <= ranges.get(1).get(1) &&
                            room.ContainsAtLeastOneCharacteristicRoom(characteristics)) {
                        filtered.add(room);
                    }
                }
                if (ranges.get(0).get(0) > 0 && ranges.get(1).get(0) <= 0 && characteristics != null) {
                    if (room.getCapacity() == capacity && room.getSquareMetres() >= ranges.get(0).get(0)
                            && room.getSquareMetres() <= ranges.get(1).get(0)
                            && room.ContainsAtLeastOneCharacteristicRoom(characteristics)) {
                        filtered.add(room);
                    }
                }
                if (ranges.get(0).get(0) <= 0 && ranges.get(1).get(0) > 0 && characteristics == null) {
                    if (capacity == room.getCapacity() && room.getPrice() >= ranges.get(1).get(0)
                            && room.getPrice() <= ranges.get(1).get(1)) {
                        filtered.add(room);
                    }
                }
                if (ranges.get(0).get(0) <= 0 && ranges.get(1).get(0) > 0 && characteristics != null) {
                    if (capacity == room.getCapacity() && room.getPrice() >= ranges.get(1).get(0)
                            && room.getPrice() <= ranges.get(1).get(1)
                            && room.ContainsAtLeastOneCharacteristicRoom(characteristics)) {
                        filtered.add(room);
                    }
                }
                if (ranges.get(0).get(0) <= 0 && ranges.get(1).get(0) <= 0 && characteristics != null) {
                    if (capacity == room.getCapacity() && room.ContainsAtLeastOneCharacteristicRoom(characteristics))
                        filtered.add(room);
                }
            }
            if (room.getAddress().equals(address) && capacity < 0) {
                if (ranges.get(0).get(0) > 0 && ranges.get(1).get(0) <= 0 && characteristics == null) {
                    if (room.getSquareMetres() >= ranges.get(0).get(0)
                            && room.getSquareMetres() <= ranges.get(0).get(1)) {
                        filtered.add(room);
                    }
                }
                if (ranges.get(0).get(0) > 0 && ranges.get(1).get(0) > 0 && characteristics == null) {
                    if (room.getSquareMetres() >= ranges.get(0).get(0)
                            && room.getSquareMetres() <= ranges.get(0).get(1) &&
                            room.getPrice() >= ranges.get(1).get(0) && room.getPrice() <= ranges.get(1).get(1)) {
                        filtered.add(room);
                    }
                }
                if (ranges.get(0).get(0) > 0 && ranges.get(1).get(0) > 0 && characteristics != null) {
                    if (room.getSquareMetres() >= ranges.get(0).get(0)
                            && room.getSquareMetres() <= ranges.get(0).get(1) &&
                            room.getPrice() >= ranges.get(1).get(0) && room.getPrice() <= ranges.get(1).get(1) &&
                            room.ContainsAtLeastOneCharacteristicRoom(characteristics)) {
                        filtered.add(room);
                    }
                }
                if (ranges.get(0).get(0) > 0 && ranges.get(1).get(0) <= 0 && characteristics != null) {
                    if (room.getSquareMetres() >= ranges.get(0).get(0)
                            && room.getSquareMetres() <= ranges.get(1).get(0)
                            && room.ContainsAtLeastOneCharacteristicRoom(characteristics)) {
                        filtered.add(room);
                    }
                }
                if (ranges.get(0).get(0) <= 0 && ranges.get(1).get(0) > 0 && characteristics == null) {
                    if (room.getPrice() >= ranges.get(1).get(0)
                            && room.getPrice() <= ranges.get(1).get(1)) {
                        filtered.add(room);
                    }
                }
                if (ranges.get(0).get(0) <= 0 && ranges.get(1).get(0) > 0 && characteristics != null) {
                    if (room.getPrice() >= ranges.get(1).get(0)
                            && room.getPrice() <= ranges.get(1).get(1)
                            && room.ContainsAtLeastOneCharacteristicRoom(characteristics)) {
                        filtered.add(room);
                    }
                }
                if (ranges.get(0).get(0) <= 0 && ranges.get(1).get(0) <= 0 && characteristics != null) {
                    if (room.ContainsAtLeastOneCharacteristicRoom(characteristics))
                        filtered.add(room);
                }
            }
        }
        return new ArrayList<>(filtered);
    }

    /**
     * Αναζήτηση δωματίου με κριτήριο τον
     * αναγνωριστικό του αριθμό.
     *
     * @param id Αναγνωριστικό δωματίου.
     * @return Τη θέση (στη λίστα) του δωματίου
     * αν υπάρχει, διαφορετικά -1.
     */
    public int FindRoom(int id)
    {
        int positionFound = -1;

        int position = 0;
        for (HotelRooms room : this.getRooms())
        {
            if (room.getId() == id)
            {
                positionFound = position;
            }

            position++;
        }

        return positionFound;
    }

    /**
     * Αναζήτηση ιδιωτικού καταλύματος με κριτήριο
     * τον αναγνωριστικό του αριθμό.
     *
     * @param id Αναγνωριστικό καταλύματος.
     * @return Τη θέση (στη λίστα) του ιδιωτικού
     * καταλύματος αν υπάρχει, διαφορετικά -1.
     */
    public int FindAccommodation(int id)
    {
        int positionFound = -1;

        int position = 0;
        for (PrivateAccommodation accommodation : this.getAirbnb())
        {
            if (accommodation.getId() == id)
            {
                positionFound = position;
            }

            position++;
        }

        return positionFound;
    }

    /**
     * Μέθοδος ακύρωσης κράτησης του χρήστη τύπου πελάτη
     * ({@link Customers}) για ιδιωτικά καταλύματα.
     *
     * @param id       Αναγνωριστικό κράτησης η οποία
     *                 θα ακυρωθεί.
     * @param customer Τύπος χρήστη πελάτης: Γίνεται χρήση
     *                 για να προσεγγιστούν όλες (μόνο)
     *                 κρατήσεις του εκάστοτε χρήστη.
     * @return Αν ακυρωθεί η επιθυμητή κράτηση τότε επιστρέφετε
     * true, διαφορετικά false.
     */
    public boolean CancelReservationPrivateAccommodation(int id, Customers customer)
    {
        boolean isCanceled = false;
        ArrayList<Reservation> needsCancel = new ArrayList<>();
        for (PrivateAccommodation accomm : airbnb) {
            for (Reservation reservation : accomm.getReservations()) {
                if (reservation.getId() == id && reservation.getCustomer().getUsername().equals(customer.getUsername())) {
                    needsCancel.add(reservation);
                    isCanceled = true;
                }
            }
            if (needsCancel.size() != 0) {
                for (Reservation x : needsCancel) {
                    accomm.getReservations().remove(x);
                }
            }
        }
        return isCanceled;
    }

    /**
     * Μέθοδος ακύρωσης κράτησης του χρήστη τύπου πελάτη
     * ({@link Customers}) για δωμάτια ξενοδοχείων.
     *
     * @param id       Αναγνωριστικό κράτησης η οποία θα
     *                 ακυρωθεί.
     * @param customer Τύπος χρήστη πελάτης: Γίνεται χρήση
     *                 για να προσεγγιστούν όλες (μόνο)
     *                 κρατήσεις του εκάστοτε χρήστη.
     * @return Αν ακυρωθεί η επιθυμητή κράτηση τότε επιστρέφετε
     * true, διαφορετικά false.
     */
    public boolean CancelReservationHotelRoom(int id, Customers customer)
    {
        boolean isCanceled = false;
        ArrayList<Reservation> needsCancel = new ArrayList<>();
        for (HotelRooms room : rooms) {
            for (Reservation reservation : room.getReservations()) {
                if (reservation.getId() == id && reservation.getCustomer().getUsername().equals(customer.getUsername())) {
                    needsCancel.add(reservation);
                    isCanceled = true;
                }
            }
            if (needsCancel.size() != 0) {
                for (Reservation x : needsCancel) {
                    room.getReservations().remove(x);
                }
            }
        }
        return isCanceled;
    }


    /**
     * Η μέθοδος αυτή βρίσκει και επιστρέφει όλες τις κρατήσεις στα ξενοδοχειακά δωμάτια για έναν συγκεκριμένο χρήστη
     * @param customer Ο πελάτης για τον οποίο βρίσκονται και επιστρέφονται οι κρατήσεις ιδιωτικών καταλυμάτων
     * @return HashMap με όλα τα δωμάτια και τις ημερομηνίες κράτησης που του χρήστη customer
     */
    public HashMap<HotelRooms, ArrayList<Date>> UserHotelReservations(Customers customer) {
        boolean hasReservations = false;
        HashMap<HotelRooms, ArrayList<Date>> hotelRooms = new HashMap<>();
        ArrayList<Date> allReservedDates;
        for (HotelRooms ignored : rooms) {
            if (ignored.getReservations() != null) {
                for (Reservation reservation : ignored.getReservations()) {
                    allReservedDates = new ArrayList<>();
                    for (Date date : ignored.getUserReservations(customer)) {
                        allReservedDates.add(date);
                        hotelRooms.put(ignored, allReservedDates);
                        hasReservations = true;

                    }
                }
            }
        }
        if (hasReservations)
            return hotelRooms;
        return null;

    }

    /**
     * Η μέθοδος αυτή βρίσκει και επιστρέφει όλες τις κρατήσεις στα ιδιωτικά καταλύματα για έναν συγκεκριμένο χρήστη
     *
     * @param customer Ο πελάτης για τον οποίο βρίσκονται και επιστρέφονται οι κρατήσεις ιδιωτικών καταλυμάτων
     * @return HashMap με όλα τα ιδιωτικά καταλύματα και τις ημερομηνίες κράτησης που του χρήστη customer
     */
    public HashMap<PrivateAccommodation, ArrayList<Date>> UserPrivateReservations(Customers customer) {
        boolean hasReservations = false;
        HashMap<PrivateAccommodation, ArrayList<Date>> privateAccommodations = new HashMap<>();
        ArrayList<Date> allReservedDates;
        for (PrivateAccommodation ignored : airbnb) {
            for (Reservation reservation : ignored.getReservations()) {

                if (reservation.getCustomer().getUsername().equals(customer.getUsername())) {
                    allReservedDates = new ArrayList<>();
                    for (Date date : ignored.getUserReservations(customer)) {
                        allReservedDates.add(date);
                        privateAccommodations.put(ignored, allReservedDates);
                        hasReservations = true;
                    }
                }
            }
        }
        if (hasReservations)
            return privateAccommodations;
        return null;
    }


    /**
     * Αυξάνει κάθε φορά, ανεξάρτητα το είδος καταλύματος, κατά μια
     * μονάδα τη γενική μεταβλητή στην κλάση {@link Accommodations}
     * έτσι ώστε κάθε κατάλυμα που προστίθεται να έχει διαφορετικό
     * αναγνωριστικό.
     */
    public int identifierManager() {
        return baseIdentifier++;
    }

    public int reservationsIdentifierManager() {
        return reservationsBaseIdentifier++;
    }

    public int getImageIdentifier() {
        return imageIdentifier++;
    }

    /**
     * Μέθοδος που αρχικοποεί τα καταλύματα και τις κρατήσεις των καταλυμάτων
     */
    public void CreateDefaultAccommodation() {

        //------Δωμάτια ξενοδοχείου Lucy απο τον πάροχο "Dimitris"
//        ArrayList<String> characteristics = new ArrayList<>();
//        characteristics.add("City View");
//        characteristics.add("Wi-fi");
//        characteristics.add("3 meals");
//        HotelRooms lucy1 = new HotelRooms(125, 34, 42, "Lucy", "Kavala",
//                1, identifierManager(), 3, characteristics, "room" + getImageIdentifier() + ".png");
//
//        ArrayList<String> characteristics1 = new ArrayList<>();
//        characteristics1.add("Sea View");
//        characteristics1.add("Pool");
//        HotelRooms lucy2 = new HotelRooms(127, 48, 55, "Lucy", "Kavala", 1, identifierManager(),
//                5, characteristics1, "room" + getImageIdentifier() + ".png");
//
//        ArrayList<String> characteristics2 = new ArrayList<>();
//        characteristics2.add("Movie cinema");
//        characteristics2.add("Television");
//        characteristics2.add("2 Beds");
//        HotelRooms lucy3 = new HotelRooms(223, 55, 60, "Lucy",
//                "Kavala", 2, identifierManager(), 7, characteristics2, "room" + getImageIdentifier() + ".png");
//        this.getRooms().add(lucy1);
//        this.getRooms().add(lucy2);
//        this.getRooms().add(lucy3);
//
//
//        ArrayList<String> characteristics3 = new ArrayList<>();
//        characteristics3.add("3 meals");
//        characteristics3.add("Wi-Fi");
//        characteristics3.add("Parking");
//        HotelRooms titania1 = new HotelRooms(112, 20, 45, "Titania", "Athens", 1, identifierManager(),
//                2, characteristics3, "room" + getImageIdentifier() + ".png");
//
//        ArrayList<String> characteristics4 = new ArrayList<>();
//        characteristics4.add("One day trip to Acropolis museum");
//        characteristics4.add("Wi-Fi");
//        characteristics4.add("Breakfast");
//        HotelRooms titania2 = new HotelRooms(132, 25, 50, "Titania", "Athens",
//                1, identifierManager(), 2, characteristics4, "room" + getImageIdentifier() + ".png");
//
//        ArrayList<String> characteristics5 = new ArrayList<>();
//        characteristics5.add("Big balcony");
//        characteristics5.add("2 meals");
//        HotelRooms titania3 = new HotelRooms(245, 27, 50, "Titania", "Athens",
//                2, identifierManager(), 2, characteristics5, "room" + getImageIdentifier() + ".png");
//        this.getRooms().add(titania1);
//        this.getRooms().add(titania2);
//        this.getRooms().add(titania3);
//
//        lucy1.Reserve(new Customers("Maria", "Maria12345", "Customer", "female"), new Date(2, 6, 2032));
//        lucy1.Reserve(new Customers("Maria", "Maria12345", "Customer", "female"), new Date(3, 3, 2023));
//        lucy1.Reserve(new Customers("Maria", "Maria12345", "Customer", "female"), new Date(4, 3, 2030));
//        lucy1.Reserve(new Customers("Makis", "Makis12345", "Customer", "male"), new Date(5, 6, 2022));
//        lucy1.Reserve(new Customers("Makis", "Makis12345", "Customer", "male"), new Date(6, 6, 2022));
//        lucy2.Reserve(new Customers("Makis", "Makis12345", "Customer", "male"), new Date(6, 6, 2022));
//
//        //lucy5.Reserve(new Customers("Makis", "Makis12345", "Customer", "male"), new Date(8,3,2022));
//        //lucy7.Reserve(new Customers("Maria", "Maria12345", "Customer", "female"), new Date(4,8,2022));
//
//        //-------------------------------------------
//
//
//        //--------Ιδιωτικά καταλύματα από τον πάροχο "Giorgos" με την επωνυμία "Vogiatzis"
//        ArrayList<String> characteristics6 = new ArrayList<>();
//        characteristics6.add("2 floors");
//        characteristics6.add("Personal bar");
//        PrivateAccommodation Maisonette1 = new PrivateAccommodation(45, 600, "Airbnb", "Thessaloniki", "Vogiatzis",
//                identifierManager(), 2, characteristics6, "accommodation" + getImageIdentifier() + ".png");
//
//        ArrayList<String> characteristics7 = new ArrayList<>();
//        characteristics7.add("4 rooms");
//        characteristics7.add("2 floors");
//        characteristics7.add("City view");
//        ArrayList<String> characteristics8 = new ArrayList<>();
//        characteristics8.add("Big balcony");
//        characteristics8.add("Garden");
//        characteristics8.add("Interior parking");
//        PrivateAccommodation House1 = new PrivateAccommodation(98, 160, "Airbnb", "Kozani", "Vogiatzis",
//                identifierManager(), 5, characteristics8, "accommodation" + getImageIdentifier() + ".png");
//
//
//        PrivateAccommodation House2 = new PrivateAccommodation(65, 120, "Airbnb", "Kozani", "Vogiatzis",
//                identifierManager(), 6, characteristics7, "accommodation" + getImageIdentifier() + ".png");
//
//        this.getAirbnb().add(Maisonette1);
//        this.getAirbnb().add(House1);
//        this.getAirbnb().add(House2);
//        Maisonette1.Reserve(new Customers("Maria", "Maria12345", "Customer", "female"), new Date(6, 9, 2022));
//        Maisonette1.Reserve(new Customers("Maria", "Maria12345", "Customer", "female"), new Date(7, 9, 2022));
//        Maisonette1.Reserve(new Customers("Maria", "Maria12345", "Customer", "female"), new Date(8, 9, 2022));
//        House1.Reserve(new Customers("Makis", "Makis12345", "Customer", "male"), new Date(5, 6, 2022));
//        House1.Reserve(new Customers("Makis", "Makis12345", "Customer", "male"), new Date(6, 6, 2022));
//        House1.Reserve(new Customers("Makis", "Makis12345", "Customer", "male"), new Date(7, 6, 2022));
//        House1.Reserve(new Customers("Makis", "Makis12345", "Customer", "male"), new Date(8, 6, 2022));
//
//        titania1.Reserve(new Customers("Maria", "Maria12345", "Customer", "female"), new Date(4, 5, 2022));
//        titania2.Reserve(new Customers("Maria", "Maria12345", "Customer", "female"), new Date(1, 7, 2022));
//        titania3.Reserve(new Customers("Makis", "Makis12345", "Customer", "male"), new Date(9, 5, 2022));
//        titania2.Reserve(new Customers("Makis", "Makis12345", "Customer", "male"), new Date(11, 9, 2022));
//
//        // ----------------------------------------------------
//
//
//        //---------Ιδιωτικά καταλύματα στην Κρήτη από τον πάροχο "Takis"
//
//        ArrayList<String> characteristics9 = new ArrayList<>();
//        characteristics9.add("Sea view");
//        characteristics9.add("boat");
//        PrivateAccommodation Airbnb1 = new PrivateAccommodation(55, 50, "Airbnb", "Kriti", "Papadopoulos",
//                identifierManager(), 2, characteristics9, "accommodation" + getImageIdentifier() + ".png");
//
//        ArrayList<String> characteristics10 = new ArrayList<>();
//        characteristics10.add("2 floors");
//        characteristics10.add("Big living room");
//        characteristics10.add("Wifi");
//        PrivateAccommodation Airbnb2 = new PrivateAccommodation(60, 60, "Airbnb", "Kriti",
//                "Papadopoulos", identifierManager(), 2, characteristics10, "accommodation" + getImageIdentifier() + ".png");
//
//        ArrayList<String> characteristics11 = new ArrayList<>();
//        characteristics11.add("Big balcony");
//        characteristics11.add("Garden");
//        PrivateAccommodation Airbnb3 = new PrivateAccommodation(47, 48, "Airbnb", "Kriti", "Papadopoulos",
//                identifierManager(), 2, characteristics11, "accommodation" + getImageIdentifier() + ".png");
//
//        this.getAirbnb().add(Airbnb1);
//        this.getAirbnb().add(Airbnb2);
//        this.getAirbnb().add(Airbnb3);
//
//
//        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("accommodationsIdentifier.bin")))
//        {
//            out.writeObject(identifierManager());
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//
//        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("imageIdentifier.bin")))
//        {
//            out.writeObject(getImageIdentifier());
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//
//        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hotelRooms.bin")))
//        {
//            out.writeObject(this.getRooms());
//        } catch( IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("privateAccommodation.bin")))
//        {
//            out.writeObject(this.getAirbnb());
//        } catch( IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("reservationsID.bin")))
//        {
//            out.writeObject(reservationsIdentifierManager());
//        } catch (IOException e){
//            e.printStackTrace();
//        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("privateAccommodation.bin"))) {
            airbnb = (ArrayList<PrivateAccommodation>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("hotelRooms.bin"))) {
            rooms = (ArrayList<HotelRooms>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("accommodationsIdentifier.bin"))) {
            int x = (Integer) in.readObject();
            setBaseIdentifier(x);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("imageIdentifier.bin"))) {
            int x = (Integer) in.readObject();
            setImageIdentifier(x);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("reservationsID.bin"))) {
            int x = (Integer) in.readObject();
            setReservationsBaseIdentifier(x);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Η μέθοδος αυτή εισάγει την εικόνα που πήραμε από το path μέσω του JFileChooser
     * στο directory "uploads" όπου αποθηκεύονται όλες οι φωτογραφίες των default καταλυμάτων αλλά
     * και αυτών που εισάγονται αργότερα
     *
     * @param path             Όνομα διαδρομής
     * @param imageIdentifier1 Μεταβλητή που βοηθά στο να ξεχωρίσουμε τα ονόματα των φωτογραφιών μέσα στο directory
     */
    public void addImageToDirectory(String path, int imageIdentifier1, int type) {
        File file = new File(path);
        String path1 = "uploads/";
        File newDirectory = new File(path1);
        if (!newDirectory.exists())
            newDirectory.mkdirs();

        File sourceFile = null;
        File destinationFile = null;
        sourceFile = new File(file.getAbsolutePath());
        if (type == 0)
            destinationFile = new File(path1 + "room" + imageIdentifier1 + ".png");
        else
            destinationFile = new File(path1 + "accommodation" + imageIdentifier1 + ".png");

        try {
            Files.copy(sourceFile.toPath(), destinationFile.toPath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Η μέθοδος αυτή φιλτράρει τη λίστα με τα δωμάτια ξενοδοχείων και επιστρέφει αυτά
     * τα οποία ανήκουν στον πάροχο που περνιέται ως όρισμα.
     *
     * @param provider Ο πάροχος για τον οποίο τα δωμάτια θα βρεθούν και θα επιστρεφούν
     * @return Λίστα με τα ξενοδοχειακά δωμάτια που έχει στην κατοχή του ο πάροχος
     */
    public ArrayList<HotelRooms> getProvidersRooms(Providers provider) {
        ArrayList<HotelRooms> roomsToReturn = new ArrayList<>();
        for (HotelRooms room : rooms) {
            if (room.getHotelName().equals(provider.getProvidersBrandName())) {
                roomsToReturn.add(room);
            }
        }
        return roomsToReturn;
    }

    /**
     * Η μέθοδος αυτή φιλτράρει τη λίστα με τα ιδιωτικά καταλύματα και επιστρέφει αυτά
     * τα οποία ανήκουν στον πάροχο που περνιέται ως όρισμα.
     *
     * @param provider Ο πάροχος για τον οποίο τα καταλύματα θα βρεθούν και θα επιστρεφούν
     * @return Λίστα με τα ιδιωτικά καταλύματα που έχει στην κατοχή του ο πάροχος
     */
    public ArrayList<PrivateAccommodation> getProvidersPrivateAccommodations(Providers provider) {
        ArrayList<PrivateAccommodation> accommodationsToReturn = new ArrayList<>();
        for (PrivateAccommodation accommodation : airbnb) {
            if (accommodation.getCompanyName().equals(provider.getProvidersBrandName())) {
                accommodationsToReturn.add(accommodation);
            }
        }
        return accommodationsToReturn;
    }
}