package users;

import accommodations.HotelRooms;
import accommodations.PrivateAccommodation;
import accommodations.reservervations.Date;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Η κλάση Customers υλοποιεί τον τύπο χρήστη του πελάτη.
 * Λειτουργίες: <p>
 * <p>
 * Κράτηση καταλύματος: {@link #TabBookAccommodation(int, String, String, Customers)},
 * Αναζήτηση καταλύματος με κριτήρια: για ξενοδοχειακα δωμάτια -> {@link #TabSearchRoom(String, int, ArrayList, List)},
 * για ιδιωτικά καταλύματα -> {@link #TabSearchAccommodation(String, int, ArrayList, List)}
 * ακύρωση κράτησης: {@link #CancelReservation(int)}
 */
public class Customers extends Users {

    private boolean activated;

    /**
     * Ορίζει, με βάση τις παραμέτρους του κατασκευαστή, αρχικές
     * τιμές στο προφίλ του πελάτη που καλείται να δημιουργηθεί.
     *
     * @param username (Μοναδικό) Όνομα χρήστη.
     * @param password Κωδικός χρήστη.
     * @param role     Ρόλος χρήστη.
     * @param gender   Γένος χρήστη.
     */
    public Customers(String username, String password, String role, String gender)
    {
        super(username, password, role, gender);
        this.activated = false;
    }

    /**
     * Μέθοδος ακύρωση κράτησης.
     * @param reservationID Αναγνωριστικός αριθμός κράτησης
     * @return true/false ανάλογα με το αν η κράτηση ακυρώθηκε επιτυχώς ή όχι
     */
    public boolean CancelReservation(int reservationID) {

        if (this.getAccommodations().CancelReservationPrivateAccommodation(reservationID, this)) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("privateAccommodation.bin"))) {
                out.writeObject(this.getAccommodations().getAirbnb());
            } catch (IOException err) {
                err.printStackTrace();
            }

            return true;
        }
        if (this.getAccommodations().CancelReservationHotelRoom(reservationID, this)) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hotelRooms.bin"))) {
                out.writeObject(this.getAccommodations().getRooms());
            } catch (IOException err) {
                err.printStackTrace();
            }
            return true;
        }

        return false;

    }


    /**
     * Μέθοδος που επιτρέπει την αναζήτηση ιδιωτικού καταλύματος με βάση τα κριτήρια που δέχεται η μέθοδος
     * ως παραμέτρους. Καλείται η μέθοδος {@link accommodations.Accommodations#SearchHotelRooms(String, int, ArrayList, List)}
     * Και η λίστα που επιστρέφει επιστρέφεται και απο αυτήν την συνάρτηση.
     * @param address Διεύθυνση ξενοδοχείου. Υπορεωτικό για την αναζήτηση δωματίου
     * @param capacity Χωρητικότητα ατόμων
     * @param ranges Λίστα με το εύρος τετραγωνικών/τιμής
     * @param characteristics Λίστα με τα χαρακτηριστικά
     * @return Λίστα με όλα τα δωμάτια που βρέθηκαν με τα δοθέντα χαρακτηριστικά
     *        null αν δεν βρεθούν δωμάτια.
     */
    public ArrayList<HotelRooms> TabSearchRoom(
            String address, int capacity,
            ArrayList<ArrayList<Integer>> ranges, List<String> characteristics) {

        ArrayList<HotelRooms> allHotelRoomsFound = this.getAccommodations().SearchHotelRooms(
                address, capacity,
                ranges, characteristics);
        if (allHotelRoomsFound != null) {
            if (allHotelRoomsFound.size() == 0) {
                return null;
            }
        }
        return allHotelRoomsFound;
    }

    /**
     * Μέθοδος που επιτρέπει την αναζήτηση ιδιωτικού καταλύματος με βάση τα κριτήρια που δέχεται η μέθοδος
     * ως παραμέτρους. Καλείται η μέθοδος {@link accommodations.Accommodations#SearchPrivateAccommodations(String, int, ArrayList, List)}
     * Και η λίστα που επιστρέφει επιστρέφεται και απο αυτήν την συνάρτηση.
     *
     * @param address         Διεύθυνση ιδιωτικού καταλύματος. Υποχρεωτικό πεδίο για την αναζήτηση
     * @param capacity        Χωρητικότητα δωματίου
     * @param ranges          Λίστα με το εύρος αναζήτησης τετραγωνικών και τιμής
     * @param characteristics Λίστα με τα χαρακτηριστικά
     * @return Λίστα με όλα τα καταλύματα που βρέθηκαν με τα δοθέντα χαρακτηριστικά
     * null αν δεν βρεθούν καταλύματα με τα συγκεκριμένα χαρακτηριστικά
     */
    public ArrayList<PrivateAccommodation> TabSearchAccommodation(
            String address, int capacity,
            ArrayList<ArrayList<Integer>> ranges, List<String> characteristics) {

        ArrayList<PrivateAccommodation> allAccommodationsFound = this.getAccommodations().SearchPrivateAccommodations(
                address, capacity,
                ranges, characteristics);
        if (allAccommodationsFound != null) {
            if (allAccommodationsFound.size() == 0) {
                return null;
            }
        }
        return allAccommodationsFound;
    }


    /**
     * Μέθοδος κράτησης καταλύματος.
     * Η κράτηση πραγματοποιείται με την επιτυχή καταχώρηση του αναγνωριστικού αριθμού και σε περίπτωση που για
     * τις επιθυμητές ημερομηνίες δεν έχει κάνει κάποιος άλλος πελάτης κράτηση στο κατάλυμα αυτό
     * @param ID Αναγνωριστικός αριθμός δωματίου για το οποίο ο χρήστης επιθυμεί να κάνει κράτηση
     * @param startDate Ημερομηνία "από"
     * @param toDate Ημερομηνία "εως"
     * @param customer Πελάτης που επιθυμεί να κάνει κράτηση
     * @return true/false Ανάλογα με το αν έγινε επιτυχημένη κράτηση ή όχι
     */
    public boolean TabBookAccommodation(int ID, String startDate, String toDate, Customers customer/*int temp*/) {

        Date tempDate = new Date();
        int preferredRoomPosition = this.getAccommodations().FindRoom(ID);
        if (preferredRoomPosition != -1) {
            HotelRooms preferredRoom = this.getAccommodations().getRooms().get(preferredRoomPosition);
            return preferredRoom.Reserve(customer, tempDate.intermediateDates(tempDate.dateGenerator(startDate), tempDate.dateGenerator(toDate)));


        }

        int preferredAccommodationPosition = this.getAccommodations().FindAccommodation(ID);
        if (preferredAccommodationPosition != -1) {
            PrivateAccommodation preferredAccommodation = this.getAccommodations().getAirbnb().get(preferredAccommodationPosition);
            return preferredAccommodation.Reserve(customer, tempDate.intermediateDates(tempDate.dateGenerator(startDate), tempDate.dateGenerator(toDate)));
        }
        return false;
    }

    public void activate() {
        this.activated = true;
    }
}
