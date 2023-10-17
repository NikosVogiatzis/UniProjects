package accommodations.reservervations;

import users.Customers;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Η κλάση Reservation υλοποιεί ένα δοχείο που περιέχει
 * τον πελάτη που κάνει κράτηση και τις ημερομηνίες της
 * κράτησης για ένα συγκεκριμένο κατάλυμα.
 *
 * @author Ολγκέρ Χότζα
 * @version 4/12/2021
 */

public class Reservation implements Serializable {
    private int id;
    private Customers customer;
    private ArrayList<Date> reservationPeriod;

    /**
     * Προκαθορίζει το ιδιωτικό αντικείμενο και την
     * ιδιωτική λίστα να μη δείχνουν σε κάποια θέση μνήμης.
     */
    public Reservation() {
        this.id = 0;
        this.customer = null;
        this.reservationPeriod = null;
    }

    /**
     * Καθορίζει τις τιμές της κλάσης με τύπους που δέχεται
     * ως παραμέτρους
     *
     * @param customer Αντικείμενο τύπου Customers.
     * @param date     Αντικείμενο τύπου Date.
     */
    public Reservation(Customers customer, ArrayList<Date> date, int id) {
        this.id = id;
        this.customer = customer;
        this.reservationPeriod = date;
    }

    public Reservation getReservation() {
        return this;
    }

    public int getId() {
        return id;
    }

    /**
     * Επιστρέφει αντικείμενο τύπου Customers που είναι
     * ορισμένο στην τωρινή κλάση.
     *
     * @return Αντικείμενο τύπου Customers.
     */
    public Customers getCustomer() {
        return customer;
    }

    /**
     * Ορίζει το αντικείμενο τύπου Customers της κλάσης
     *  με ένα άλλο ίδιου τύπου αντικείμενο που υπάρχει
     *  ως όρισμα.
     * @param customer Αντικείμενο τύπου Customers
     */
    public void setCustomer(Customers customer)
    {
        this.customer = customer;
    }

    /**
     * Επιστρέφει μια λίστα που περιέχει όλες τις αναγραφόμενες
     * ημερομηνίες κρατήσεων ενός καταλύματος.
     *
     * @return Λίστα με όλες τις ημερομηνίες κρατήσεων.
     */
    public ArrayList<Date> getReservationPeriod()
    {
        return reservationPeriod;
    }

}
