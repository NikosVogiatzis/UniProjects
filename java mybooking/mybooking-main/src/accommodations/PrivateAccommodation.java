package accommodations;

import accommodations.reservervations.Date;
import accommodations.reservervations.Reservation;
import accommodations.reservervations.Reserve;
import users.Customers;

import java.util.ArrayList;
import java.util.List;

/**
 * Η κλάση PrivateAccommodation υλοποιεί τα ιδιωτικά καταλύματα (Airbnb, House , Maisonette).
 * Κληρονομεί απο την {@link Accommodations} τα βασικά χαρακτηριστικά και προσθέτει επιπλέον
 * την επωνυμία του καταλύματος που ταυτίζεται με την επωνυμία του παρόχου που έχει καταχωρήσει το κατάλυμα,
 * τον αναγνωριστικό αριθμό, τη διεύθυνση, τον τύπο του καταλύματος, την χωρητικότητα ατόμων, τη λίστα με
 * τα ειδικά χαρακτηριστικά του καταλύματος
 */
public class PrivateAccommodation extends Accommodations implements Reserve {
    private final String companyName;
    private final int id;
    private final String address; //Δεν αφορά συγκεκριμένη οδό αλλά πόλη

    protected ArrayList<Reservation> reservedDates = new ArrayList<>();
    private String type;


    /**
     * Ορίζει, με βάση τις παραμέτρους του κατασκευαστή, αρχικές
     * τιμές στο κατάλυμα που καλείται να δημιουργηθεί.
     *
     * @param squareMetres    Τετραγωνικά μέτρα δωματίου.
     * @param price           Τιμή δωματίου (σε ευρώ).
     * @param type            Τύπος καταλύματος.
     * @param address         Διεύθυνση καταλύματος.
     * @param companyName     Διεύθυνση καταλύματος.
     * @param id              Αναγνωριστικό καταλύματος (Δίνεται από το σύστημα).
     * @param capacity        Χωρητικότητα ατόμων στο κατάλυμα
     * @param characteristics Λίστα με τα ειδικά χαρακτηριστικά του καταλύματος
     */
    public PrivateAccommodation(int squareMetres, int price, String type,
                                String address, String companyName, int id, int capacity,
                                List<String> characteristics, String imagePath) {
        super(squareMetres, price, capacity, characteristics, imagePath);
        this.type = type;
        this.address = address;
        this.companyName = companyName;
        this.id = id;

    }

    public String getCompanyName() { return companyName; }

    public String getAddress() { return address; }


    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public int getId() { return id; }

    @Override
    public boolean Reserve(Customers customer, Date date)
    {
        if (this.reservedDates.contains(date))
            return false;

        ArrayList<Date> temp = new ArrayList<>();
        temp.add(date);

        this.reservedDates.add(new Reservation(customer, temp, reservationsIdentifierManager()));
        return true;
    }

    @Override
    public boolean Reserve(Customers customer, ArrayList<Date> dates) {
        if (dates != null) {
            for (Date wantedDate : dates) {
                for (Date reservedDates : this.getUserReservations()) {
                    if (reservedDates.getDay() == wantedDate.getDay()
                            && reservedDates.getMonth() == wantedDate.getMonth()
                            && reservedDates.getYear() == wantedDate.getYear()) {
                        return false;
                    }
                }
            }

            this.reservedDates.add(new Reservation(customer, dates, reservationsIdentifierManager()));
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Date> getUserReservations() {
        ArrayList<Date> dates = new ArrayList<>();
        for (Reservation ignored : reservedDates) {
            dates.addAll(ignored.getReservationPeriod());
        }

        return dates;
    }

    @Override
    public ArrayList<Date> getUserReservations(Customers customer) {
        ArrayList<Date> dates = new ArrayList<>();
        for (Reservation ignored : reservedDates) {
            if (customer.getUsername().equals(ignored.getCustomer().getUsername())) {
                dates.addAll(ignored.getReservationPeriod());

            }
        }

        return dates;
    }

    @Override
    public ArrayList<Reservation> getReservations() {
        return reservedDates;
    }

}
