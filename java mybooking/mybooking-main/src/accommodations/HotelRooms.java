package accommodations;

import accommodations.reservervations.Date;
import accommodations.reservervations.Reservation;
import accommodations.reservervations.Reserve;
import users.Customers;

import java.util.ArrayList;
import java.util.List;

/**
 * Η κλάση HotelRooms υλοποιεί τα ξενοδοχειακά δωμάτια. Κληρονομεί απο την
 * {@link Accommodations} τα βασικά χαρακτηριστικά της και προσθέτει επιπλέον χαρακτηριστικά
 * όπως το όνομα του ξενοδοχείου ,τη διεύθυνση του ξενοδοχείου (Πόλη ΟΧΙ ειδική διεύθυνση), αριθμό δωματίου , όροφο
 * ,χωρητικότητα ατόμων, λίστα με τα ειδικά χαρακτηριστικά
 * και τον ειδικό αναγνωριστικό αριθμό
 *
 */
public class HotelRooms extends Accommodations implements Reserve
{
    private final String hotelName;
    private final String address; //Δεν αφορά συγκεκριμένη οδό αλλά πόλη
    protected ArrayList<Reservation> reservedDates = new ArrayList<>();
    private final int id;
    private int roomNumber;
    private int floor;

    /**
     * Ορίζει, με βάση τις παραμέτρους του κατασκευαστή, αρχικές
     * τιμές στο δωμάτιο που καλείται να δημιουργηθεί.
     *
     * @param roomNumber   Αριθμός δωματίου.
     * @param squareMetres Τετραγωνικά μέτρα δωματίου.
     * @param price        Τιμή δωματίου (σε ευρώ).
     * @param hotelName    Όνομα ξενοδοχείου.
     * @param address      Διεύθυνση ξενοδοχείου.
     * @param floor        Όροφος δωματίου.
     * @param id           Αναγνωριστικό δωματίου (Δίνεται από το σύστημα).
     * @param capacity     Χωρητικότητα ατόμων στο δωμάτιο
     * @param characteristics Λίστα με τα ειδικά χαρακτηριστικά του δωματίου
     */
    public HotelRooms(int roomNumber, int squareMetres, int price,
                      String hotelName, String address, int floor, int id,
                      int capacity, List<String> characteristics, String imageName) {
        super(squareMetres, price, capacity, characteristics, imageName);

        this.hotelName = hotelName;
        this.address = address;

        this.floor = floor;
        this.roomNumber = roomNumber;
        this.id = id;
    }


    public String getHotelName() { return hotelName; }

    public int getId() { return id; }

    public int getFloor() { return floor; }

    public void setFloor(int floor) { this.floor = floor; }

    public String getAddress() { return address; }

    public int getRoomNumber()  { return roomNumber; }

    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }

    @Override
    public boolean Reserve(Customers customer, Date date) {
        if (this.reservedDates.contains(date))
            return false;

        ArrayList<Date> temp = new ArrayList<>();
        temp.add(date);
        int x = reservationsIdentifierManager();

        this.reservedDates.add(new Reservation(customer, temp, x));
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
    public ArrayList<Date> getUserReservations(Customers customer) {
        ArrayList<Date> dates = new ArrayList<>();
        for (Reservation ignored : reservedDates) {
            if (ignored.getCustomer().getUsername().equals(customer.getUsername())) {
                dates.addAll(ignored.getReservationPeriod());
            }
        }

        return dates;
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
    public ArrayList<Reservation> getReservations()
    {
        return reservedDates;
    }

}





