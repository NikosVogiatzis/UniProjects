package accommodations.reservervations;

import users.Customers;

import java.util.ArrayList;

public interface Reserve
{
    /**
     * Μέθοδος δημιουργίας κράτησης καταλύματος για μια συγκεκριμένη μέρα.
     *
     * @param customer Τύπος χρήστη. Γίνεται χρήση
     *                 για να καθοριστεί ο χρήστης
     *                 που έχει προβεί προς κράτηση
     *                 κάποιου καταλύματος.
     * @param date     Ημερομηνία κρατήσεως.
     * @return Επιστρέφετε true σε περίπτωση που η
     * κράτηση είναι επιτυχής (δηλαδή το συγκεκριμένο
     * κατάλυμα είναι σε διαθεσιμότητα για τη συγκεκριμένη
     * μέρα), διαφορετικά false.
     */
    public boolean Reserve(Customers customer, Date date);

    /**
     * Μέθοδος δημιουργίας κράτησης καταλύματος για μια συγκεκριμένη περίοδο.
     *
     * @param customer Τύπος χρήστη. Γίνεται χρήση
     *                 για να καθοριστεί ο χρήστης
     *                 που έχει προβεί προς κράτηση
     *                 κάποιου καταλύματος.
     * @param dates    Ημερομηνίες κρατήσεως.
     * @return Επιστρέφετε true σε περίπτωση που η
     * κράτηση είναι επιτυχής (δηλαδή το συγκεκριμένο
     * κατάλυμα είναι σε διαθεσιμότητα για τη συγκεκριμένη
     * περίοδο), διαφορετικά false.
     */
    public boolean Reserve(Customers customer, ArrayList<Date> dates);

    /**
     * Επιστρέφονται όλες οι ημερομηνίες κρατήσεων ενός
     * συγκεκριμένου χρήστη πελάτη για συγκεκριμένο
     * τύπου καταλύματος.
     *
     * @return Λίστα με όλες τις ημερομηνίες που
     * υπάρχουν κρατήσεις του χρήστη πελάτη.
     */
    public ArrayList<Date> getUserReservations(Customers customer);

    public ArrayList<Date> getUserReservations();

    /**
     * Επιστρέφονται όλες οι ενεργές κρατήσεις ενός
     * συγκεκριμένου τύπου καταλύματος.
     *
     * @return Λίστα με όλες τις ενεργές κρατήσεις.
     */
    public ArrayList<Reservation> getReservations();
}
