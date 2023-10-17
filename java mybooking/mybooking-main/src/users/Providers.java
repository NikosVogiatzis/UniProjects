package users;

import accommodations.Accommodations;
import accommodations.HotelRooms;
import accommodations.PrivateAccommodation;
import accommodations.reservervations.Date;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;


/**
 * Η κλάση {@link users.Providers} υλοποιεί τον τύπο χρήστη του παρόχου
 * Εισάγει καταλύματα στην εφαρμογή {@link #AddHotelRoom(String, String, String, String, String, String, List, String)}
 * , {@link #AddPrivateAccommodation(String, String, String, String, String, List, String)}
 * επεξεργάζεται καταλύματα {@link #EditRoom(String, String, String, String, String, String, HotelRooms)},
 * {@link #EditAccommodation(String, String, String, String, String, PrivateAccommodation)},
 * διαγράφει καταλύματα: {@link #DeleteAccommodation(String)}
 * και βρίσκει τις ενεργές κρατήσεις για τα καταλύματα του παρόχου: Για τα δωμάτια -> {@link #RoomReservations()},
 * για τα ιδιωτικά καταλύματα {@link #PrivateReservations()}.
 *
 * @author Νίκος Βογιατζής
 * @version 9/1/2022
 */
public class Providers extends Users
{
    private final String providersBrandName;
    private boolean activated;

    /**
     * Κατασκευαστής που δημιουργεί νέο χρήστη τύπου {@link Providers}
     *
     * @param username    (Μοναδικό) Όνομα παρόχου.
     * @param password    Κωδικός χρήστη.
     * @param role        Ρόλος χρήστη.
     * @param Gender      Γένος χρήστη.
     * @param companyName (Μοναδική) Επωνυμία παρόχου (χρησιμοποιείται για την ταύτιση των καταλυμάτων που έχει παραχωρήσει με τον ίδιο)
     */
    public Providers(String username, String password, String role, String Gender, String companyName) {
        super(username, password, role, Gender);
        this.providersBrandName = companyName;
        this.activated = false;
    }

    /**
     * Getter της τιμής providersBrandName
     *
     * @return Επιστρέφει την επωνυμία του παρόχου
     */
    public String getProvidersBrandName() {
        return providersBrandName;
    }


    /**
     * Μέθοδος που αποφασίζει για το αν ένας συγκεκριμένος πάροχος έχει δωμάτια ξενοδοχείων στην κατοχή του.
     * <p>
     *
     * @param name Το όνομα του ξενοδοχείου
     * @return True αν ο πάροχος έχει στην κατοχή του δωμάτια ξενοδοχείων/ false αλλιώς
     */

    public boolean OwnsHotel(String name)
    {
        for(HotelRooms index : getAccommodations().getRooms())
        {
            if(index.getHotelName().equals(name))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Μέθοδος που αποφασίζει για το αν ένας συγκεκριμένος πάροχος έχει ιδιωτικά καταλύματα στην κατοχή του.
     * <p>
     * @param name Το όνομα της ιδιωτικής εταιρίας του παρόχου
     * @return True αν ο πάροχος έχει στην κατοχή του ιδιωτικά καταλύματα/ false αλλιώς
     */

    public boolean OwnsPrivateAccommodation(String name)
    {
        for(PrivateAccommodation index : getAccommodations().getAirbnb())
        {
            if(index.getCompanyName().equals(name))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Βοηθητική μέθοδος που αποφασίζει αν ο πάροχος έχει ήδη δωμάτιο με τον ίδιο αριθμό στην ίδια διεύθυνση
     *
     * @param address Διεύθυνση του ξενοδοχείου
     * @param roomNumber αριθμός δωματίου
     * @param name       Όνομα παρόχου ώστε να χρησιμοποιείται η μέθοδος μόνο για τα δωμάτια που έχει στην κατοχή του
     * @return true αν ο πάροχος δεν έχει στην κατοχή του δωμάτιο με τον ίδιο αριθμό δωματίου / false αλλιώς
     */
    public boolean UniqueRoomNumber(String address, int roomNumber, String name) {

        for (HotelRooms index : getAccommodations().getRooms()) {
            if (roomNumber == index.getRoomNumber() && index.getHotelName().equals(name) && address.equals(index.getAddress()))
                return false;
        }
        return true;
    }


    /**
     * Η μέθοδος αυτή προσθέτει ξενοδοχειακό δωμάτιο για τον πάροχο.
     * Αν όλες οι τιμές που δώθηκαν στα textiles είναι έγκυρες και ο πάροχος δεν έχει άλλο δωμάτιο
     * με τον ίδιο αριθμό δωματίου στη συγκεκριμένη διεύθυνση, τότε δημιουργείται νέο δωμάτιο,
     * εισάγεται στη λίστα {@link Accommodations#getRooms()} και επιστρέφεται το νέο δωμάτιο
     * @param roomNumber αριθμός δωματίου
     * @param floor όροφος δωματίου
     * @param squareMetres τετραγωνικά δωματίου
     * @param price τιμή δωματίου
     * @param Address διεύθυνση ξενοδοχείου
     * @param capacity χωρητικότητα ατόμων στο δωμάτιο
     * @param characteristics Λίστα με τα ειδικά χαρακτηριστικά του δωματίου
     * @return μεταβλητή τύπου {@link HotelRooms} που είναι το νέο δωμάτιο που προσθέθηκε
     *         αν δεν έγινε επιτυχής προσθήκη του νέου δωματίου επιστρέφεται null
     */

    public HotelRooms AddHotelRoom(String roomNumber, String floor, String squareMetres,
                                   String price, String Address, String capacity,
                                   List<String> characteristics, String path) {
        boolean types = false;
        Scanner dump;
        dump = new Scanner(roomNumber);
        if (dump.hasNextInt()) {
            dump = new Scanner(floor);
            if (dump.hasNextInt()) {
                dump = new Scanner(squareMetres);
                if (dump.hasNextInt()) {
                    dump = new Scanner(price);
                    if (dump.hasNextInt()) {
                        dump = new Scanner(capacity);
                        if (dump.hasNextInt()) {
                            if (!Address.equals(""))
                                if (!path.equals(""))
                                    types = true;
                        }
                    }
                }
            }
        }
        if (types) {
            if (UniqueRoomNumber(Address, Integer.parseInt(roomNumber), this.providersBrandName)) {
                int x = this.getAccommodations().getImageIdentifier();
                this.getAccommodations().addImageToDirectory(path, x, 0);
                HotelRooms newRoom = new HotelRooms(Integer.parseInt(roomNumber), Integer.parseInt(squareMetres), Integer.parseInt(price),
                        this.providersBrandName, Address, Integer.parseInt(floor), getAccommodations().identifierManager()
                        , Integer.parseInt(capacity), characteristics, "room" + x + ".png");

                System.out.println("sss: " + newRoom.getImageName());
                return newRoom;
            } else
                JOptionPane.showMessageDialog(null,
                        "You already own a room with the number: " + roomNumber);
        }

        return null;

    }

    /**
     * Η μέθοδος αυτή προσθέτει ιδιωτικό κατάλυμα για τον πάροχο.
     * Αν όλες οι τιμές που δώθηκαν στα textiles είναι έγκυρες
     * τότε δημιουργείται νέο ιδιωτικό κατάλυμα,
     * εισάγεται στη λίστα {@link Accommodations#getAirbnb()} και επιστρέφεται το νέο ιδιωτικό κατάλυμα
     *
     * @param address         Διεύθυνση ιδιωτικού καταλύματος
     * @param type            Τύπος καταλύματος
     * @param squareMetres    τετραγωνικά καταλύματος
     * @param price           τιμή καταλύματος
     * @param capacity        χωρητικότητα ατόμων για το κατάλυμα
     * @param characteristics Λίστα με τα ειδικά χαρακτηριστικά του ιδιωτικού καταλύματος
     * @return μεταβλητή τύπου {@link PrivateAccommodation} που είναι το νέο ιδιωτικό κατάλυμα που προσθέθηκε,
     * null αν δεν δημιουργηθεί και προσθεθεί επιτυχώς το ιδιωτικό κατάλυμα
     */
    public PrivateAccommodation AddPrivateAccommodation(String address, String type
            , String squareMetres, String price, String capacity
            , List<String> characteristics, String imagePath) {
        boolean types = false;
        Scanner dump;
        dump = new Scanner(squareMetres);
        if (dump.hasNextInt()) {
            dump = new Scanner(price);
            if (dump.hasNextInt()) {

                if (!address.equals("")) {
                    if (!type.equals("") && (type.equalsIgnoreCase("house") || type.equalsIgnoreCase("maisonette")
                            || type.equalsIgnoreCase("airbnb"))) {
                        dump = new Scanner(capacity);
                        if (dump.hasNextInt()) {
                            if (!imagePath.equals(""))
                                types = true;
                        }
                    }
                }
            }
        }
        if (types) {
            int x = this.getAccommodations().getImageIdentifier();
            this.getAccommodations().addImageToDirectory(imagePath, x, 1);
            return new PrivateAccommodation(Integer.parseInt(squareMetres), Integer.parseInt(price)
                    , type, address, this.providersBrandName, this.getAccommodations().identifierManager(),
                    Integer.parseInt(capacity), characteristics, "accommodation" + x + ".png");
        }

        return null;
    }

    /**
     * Μέθοδος που υπολογίζει τον συνολικό αριθμό καταλυμάτων που έχει στην κατοχή του ο πάροχος
     *
     * @param name Όνομα παρόχου που βοηθά στην ταύτιση των καταλυμάτων με αυτά που του ανήκουν
     * @return Επιστρέφει μία ακέραιη τιμή που δείχνει τον αριθμό των καταλυμάτων του παρόχου
     */
    public int numberOfAccommodations(String name) {
        int count = 0;
        for (HotelRooms index : getAccommodations().getRooms()) {
            if (index.getHotelName().equals(name))
                count++;
        }

        for (PrivateAccommodation index : getAccommodations().getAirbnb()) {
            if (index.getCompanyName().equals(name))
                count++;
        }

        return count;
    }

    /**
     * Η μέθοδος αυτή επιτρέπει την επεξεργασία δωματίου. Δημιουργείται αντίγραφο του δωματίου για το οποίο
     * ο πάροχος επιθυμεί να κάνει αλλαγές. Αν όλες οι τιμές που εισέρχονται ως παράμετροι είναι κενές επιστρέφεται
     * null αφού δεν υπάρχει καμία αλλαγή στο δωμάτιο. Διαφορετικά ελέγχεται η εγκυρότητα της τιμής του κάθε πεδίου και αν δεν είναι
     * κενό, οπότε και αλλάζουν οι αντίστοιχες τιμές στο δωμάτιο. Σε αυτήν την περίπτωση έχουμε επιτυχημένη επεξεργασία δωματίου
     * και επιστρέφεται το αντίγραφο που δημιουργήθηκε με τις αλλαγμένες τιμές
     * @param roomNumber αριθμός δωματίου
     * @param roomFloor όροφος δωματίου
     * @param squareMetres τετραγωνικά μέτρα
     * @param priceNight τιμή
     * @param capacity χωρητικότητα ατόμων
     * @param roomCharacteristics λίστα με τα χαρακτηριστικά του δωματίου
     * @param room Το δωμάτιο για το οποίο ο πάροχος επιθυμεί να κάνει αλλαγές
     * @return Το αντίγραφο του δωματίου με τις αλλαγές που εφαρμόστηκαν σε αυτό σε περίπτωση επιτυχημένης
     *          επεξεργασίας, null αν η επεξεργασία δεν είναι επιτυχημένη
     */
    public HotelRooms EditRoom(String roomNumber, String roomFloor, String squareMetres,
                               String priceNight, String capacity, String roomCharacteristics, HotelRooms room) {
        HotelRooms copyOfRoom = new HotelRooms(room.getRoomNumber(), room.getSquareMetres(), room.getPrice(), room.getHotelName(),
                room.getAddress(), room.getFloor(), room.getId(), room.getCapacity(), room.getCharacteristics(), room.getImageName());

        if (roomNumber.equals("") && roomFloor.equals("") && squareMetres.equals("") && priceNight.equals("") &&
                capacity.equals("") && roomCharacteristics.equals(""))
            return null;
        Scanner dump;
        dump = new Scanner(roomNumber);
        if (dump.hasNextInt()) {
            if (UniqueRoomNumber(room.getAddress(), Integer.parseInt(roomNumber), this.providersBrandName))
                copyOfRoom.setRoomNumber(dump.nextInt());
            else
                return null;
        }
        dump = new Scanner(roomNumber);
        if (!dump.hasNextInt() && !roomNumber.equals(""))
            return null;
        dump = new Scanner(roomFloor);
        if (dump.hasNextInt()) {
            copyOfRoom.setFloor(dump.nextInt());
        }
        dump = new Scanner(roomFloor);
        if (!dump.hasNextInt() && !roomFloor.equals(""))
            return null;
        dump = new Scanner(squareMetres);
        if (dump.hasNextInt()) {
            copyOfRoom.setSquareMetres(dump.nextInt());
        }
        dump = new Scanner(squareMetres);
        if (!dump.hasNextInt() && !squareMetres.equals(""))
            return null;

        dump = new Scanner(priceNight);
        if (dump.hasNextInt()) {
            copyOfRoom.setPrice(dump.nextInt());
        }
        dump = new Scanner(priceNight);
        if (!dump.hasNextInt() && !priceNight.equals(""))
            return null;
        dump = new Scanner(capacity);
        if (dump.hasNextInt()) {
            copyOfRoom.setCapacity(dump.nextInt());
        }
        dump = new Scanner(capacity);
        if (!dump.hasNextInt() && !priceNight.equals(""))
            return null;

        if (!roomCharacteristics.equals("")) {
            List<String> characteristics = new ArrayList<>();
            String[] temp = roomCharacteristics.split("/");
            characteristics.addAll(Arrays.asList(temp));
            characteristics.addAll(room.getCharacteristics());
            copyOfRoom.setCharacteristics(characteristics);
        }
        return copyOfRoom;

    }

    /**
     * Η μέθοδος αυτή επιτρέπει την επεξεργασία ιδιωτικού καταλύματος. Δημιουργείται αντίγραφο του καταλύματος για το οποίο
     * ο πάροχος επιθυμεί να κάνει αλλαγές. Αν όλες οι τιμές που εισέρχονται ως παράμετροι είναι κενές επιστρέφεται
     * null αφού δεν υπάρχει καμία αλλαγή στο κατάλυμα. Διαφορετικά ελέγχεται η εγκυρότητα της τιμής του κάθε πεδίου και αν δεν είναι
     * κενό, οπότε και αλλάζουν οι αντίστοιχες τιμές στο κατάλυμα. Σε αυτήν την περίπτωση έχουμε επιτυχημένη επεξεργασία καταλύματος
     * και επιστρέφεται το αντίγραφο που δημιουργήθηκε με τις αλλαγμένες τιμές
     *
     * @param type            Τύπος ιδιωτικού καταλύματος
     * @param squareMetres    τετραγωνικά ιδιωτικού καταλύματος
     * @param price           Τιμή
     * @param capacity        Χωρητικότητα ατόμων
     * @param characteristics Ειδικά χαρακτηριστικά
     * @param accommodation   Το ιδιωτικό κατάλυμα για το οποίο ο πάροχος επιθυμεί να κάνει αλλαγές
     * @return Το αντίγραφο του ιδιωτικού καταλύματος με τις αλλαγές που εφαρμόστηκαν σε αυτό σε περίπτωση επιτυχημένης
     * *          επεξεργασίας, null αν η επεξεργασία δεν είναι επιτυχημένη
     */
    public PrivateAccommodation EditAccommodation(String type, String squareMetres, String price, String capacity
            , String characteristics, PrivateAccommodation accommodation) {
        PrivateAccommodation copyOfAccommodation = new PrivateAccommodation(accommodation.getSquareMetres(), accommodation.getPrice(),
                accommodation.getType(), accommodation.getAddress(), accommodation.getCompanyName(), accommodation.getId(),
                accommodation.getCapacity(), accommodation.getCharacteristics(), accommodation.getImageName());
        if (type.equals("") && squareMetres.equals("") && price.equals("")
                && capacity.equals("") && characteristics.equals(""))
            return null;

        if (!type.equals("")) {
            if (!type.equalsIgnoreCase("maisonette") &&
                    !type.equalsIgnoreCase("airbnb") &&
                    !type.equalsIgnoreCase("house"))
                return null;
            else
                copyOfAccommodation.setType(type);
        }

        Scanner dump = new Scanner(squareMetres);
        if (dump.hasNextInt())
            copyOfAccommodation.setSquareMetres(dump.nextInt());
        dump = new Scanner(squareMetres);
        if (!squareMetres.equals("") && !dump.hasNextInt())
            return null;

        dump = new Scanner(price);
        if (dump.hasNextInt())
            copyOfAccommodation.setPrice(dump.nextInt());
        dump = new Scanner(price);
        if (!price.equals("") && !dump.hasNextInt())
            return null;

        dump = new Scanner(capacity);
        if (dump.hasNextInt())
            copyOfAccommodation.setCapacity(dump.nextInt());
        dump = new Scanner(capacity);
        if (!capacity.equals("") && !dump.hasNextInt())
            return null;


        if (!characteristics.equals("")) {
            List<String> characteristicsList = new ArrayList<>();
            String[] temp = characteristics.split("/");
            characteristicsList.addAll(Arrays.asList(temp));
            characteristicsList.addAll(accommodation.getCharacteristics());
            copyOfAccommodation.setCharacteristics(characteristicsList);
        }
        return copyOfAccommodation;
    }

    /**
     * Η μέθοδος αυτή επιτρέπει την διαγραφή καταλύματος. Δέχεται ως όρισμα τον ειδικό αναγνωριστικό αριθμό καταλύματος
     * και αν το κατάλυμα με αυτον τον αριθμό ανήκει στον πάροχο τότε διαγράφεται από την αντίστοιχη λίστα
     * @param ID Ο ειδικός αναγνωριστικός αριθμός του καταλύματος
     * @return true αν το κατάλυμα διαγράφηκε επιτυχώς
     */
    public boolean DeleteAccommodation(String ID) {
        int i = 0;

        if (OwnsHotel(this.providersBrandName)) {
            Iterator<HotelRooms> itr = getAccommodations().getRooms().iterator();
            while (itr.hasNext()) {
                HotelRooms room = itr.next();
                if (room.getId() == Integer.parseInt(ID) && room.getHotelName().equals(this.providersBrandName)) {
                    itr.remove();
                    i++;
                }
            }
        }

        if (i != 0) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hotelRooms.bin"))) {
                out.writeObject(getAccommodations().getRooms());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (OwnsPrivateAccommodation(this.providersBrandName)) {
            Iterator<PrivateAccommodation> it = getAccommodations().getAirbnb().iterator();
            while (it.hasNext()) {
                PrivateAccommodation accommodation = it.next();
                if (accommodation.getId() == Integer.parseInt(ID) && accommodation.getCompanyName().equals(this.providersBrandName)) {
                    it.remove();
                    i++;
                    System.out.println(i);
                }
            }
        }
        if (i != 0) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("privateAccommodation.bin"))) {
                out.writeObject(getAccommodations().getAirbnb());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return i > 0;
    }

    /**
     * Η μέθοδος αυτή βρίσκει και επιστρέφει όλες τις κρατήσεις για τα ξενοδοχειακά δωμάτια
     * που έχει στην κατοχή του ο πάροχος
     * @return HashMap με κλειδία δωμάτια και τιμές τις ημερομηνίες κράτησης τους
     */
    public HashMap<HotelRooms, ArrayList<Date>> RoomReservations() {

        HashMap<HotelRooms, ArrayList<Date>> allReservations = new HashMap<>();
        ArrayList<Date> allReservedDates;

        for (HotelRooms room : getAccommodations().getRooms()) {
            if (room.getHotelName().equals(this.getProvidersBrandName())) {
                if (room.getUserReservations().size() != 0) {
                    allReservedDates = new ArrayList<>();
                    for (Date date : room.getUserReservations()) {
                        allReservedDates.add(date);
                        allReservations.put(room, allReservedDates);
                    }
                }
            }
        }

        return allReservations;
    }

    /**
     * Η μέθοδος αυτή βρίσκει και επιστρέφει όλες τις κρατήσεις για ιδιωτικά καταλύματα
     * που έχει στην κατοχή του ο πάροχος
     *
     * @return HashMap με κλειδία ιδιωτικά καταλύματα και τιμές τις ημερομηνίες κράτησης τους
     */
    public HashMap<PrivateAccommodation, ArrayList<Date>> PrivateReservations() {

        HashMap<PrivateAccommodation, ArrayList<Date>> allReservations = new HashMap<>();
        ArrayList<Date> allReservedDates;

        for (PrivateAccommodation accommodation : getAccommodations().getAirbnb()) {
            if (accommodation.getCompanyName().equals(this.getProvidersBrandName())) {
                if (accommodation.getUserReservations().size() != 0) {
                    allReservedDates = new ArrayList<>();
                    for (Date date : accommodation.getUserReservations()) {
                        allReservedDates.add(date);
                        allReservations.put(accommodation, allReservedDates);
                    }
                }
            }
        }

        return allReservations;
    }

    /**
     * μέθοδος που ενεργοποιεί τον λογαριασμό του παρόχου
     */
    public void activate() {
        this.activated = true;
    }

    /**
     * @return true/false ανάλογα με το αν ο λογαριασμός του παρόχου
     *         είναι ενεργοποιημένος ή όχι
     */
    public boolean accountStatus() {
        return activated;
    }
}






