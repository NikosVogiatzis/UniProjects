package users;

import accommodations.HotelRooms;
import accommodations.PrivateAccommodation;
import accommodations.reservervations.Date;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Η κλάση Admins υλοποιεί τον τύπο χρήστη του διαχειριστή. Κληρονομεί απο την {@link Users}
 * τα βασικά χαρακτηριστικά του προφίλ του και εκτελεί τις παρακάτω λειτουργίες: <p>
 * Προβολή όλων των χρηστών: {@link #ViewAllUsers()} ,
 * Προβολή όλων των ενεργών κρατήσεων για ξενοδοχειακά δωμάτια: {@link #viewAllReservationsOfRooms()} )} ,
 * Προβολή όλων των ενεργών κρατήσεων για ιδιωτικά καταλύματα: {@link #viewAllReservationsOfPrivateAccommodation()} ()} )} ,
 * Αποστολή μηνύματος προς τους άλλους χρήστες: {@link #setMessages(String)} ,
 */
public class Admins extends Users
{
    /**
     * Default Constructor
     *
     * @param username Το όνομα χρήστη
     * @param password Ο κωδικός χρήστη
     * @param role     Ρόλος χρήστη (Πελάτης/Πάροχος/Διαχειριστής)
     * @param gender   Γένος του διαχειριστή
     */

    public Admins(String username, String password, String role, String gender)
    {
        super(
                username,
                password,
                role,
                gender
        );
    }

    /**
     * Μέθοδος που δίνει τη δυνατότητα στους διαχειριστές να δούνε όλους τους
     * χρήστες που υπάρχουν μέσα στην εφαρμογή.
     *
     * @return Λίστα με όλους τους χρήστες που υπάρχουν στην εφαρμογή
     */
    public ArrayList<Users> ViewAllUsers() {
        ArrayList<Users> allUsers = new ArrayList<>();

        allUsers.addAll(getAllAdmins());


        allUsers.addAll(getAllProviders());

        allUsers.addAll(getAllCustomers());

        return allUsers;
    }



    /**
     * Μέθοδος που επιτρέπει στον διαχειριστή να δει όλες τις ενεργές κρατήσεις
     * για όλα τα καταλύματα
     */
    public HashMap<HotelRooms, ArrayList<Date>> viewAllReservationsOfRooms()
    {
        HashMap<HotelRooms, ArrayList<Date>> allReservations = new HashMap<>();
        ArrayList<Date> allReservedDates;

        for (HotelRooms room : getAccommodations().getRooms())
        {
            if (room.getUserReservations().size() != 0)
            {
                allReservedDates = new ArrayList<>();
                for (Date date : room.getUserReservations())
                {
                    allReservedDates.add(date);
                    allReservations.put(room, allReservedDates);
                }
            }
        }

        return allReservations;
    }

    public HashMap<PrivateAccommodation, ArrayList<Date>> viewAllReservationsOfPrivateAccommodation()
    {
        HashMap<PrivateAccommodation, ArrayList<Date>> allReservations = new HashMap<>();
        ArrayList<Date> allReservedDates;

        for (PrivateAccommodation airbnb : getAccommodations().getAirbnb())
        {
            if (airbnb.getUserReservations().size() != 0)
            {
                allReservedDates = new ArrayList<>();
                for (Date date : airbnb.getUserReservations())
                {
                    allReservedDates.add(date);
                    allReservations.put(airbnb, allReservedDates);
                }
            }
        }

        return allReservations;
    }


    /**
     * Μέθοδος αποστολής μηνύματος προς χρήστες και των τριών τύπων
     *
     * @param username Όνομα χρήστη που θα αποσταλεί το μήνυμα
     * @param message  Συμβολοσειρά με το προς αποστολή μήνυμα
     */
    public boolean SendMessage(String username, String message)
    {
        int flag = 0;
        boolean msgSent = true;
        for (Providers index : getAllProviders())
        {
            if (username.equals(index.getUsername())) {
                flag++;
                index.setMessages("From: " + this.getUsername() + "\n" + "To: " + username + "\n\t" + message);
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("providers.bin"))) {
                    out.writeObject(this.getAllProviders());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        for (Customers index : getAllCustomers())
        {
            if (username.equals(index.getUsername())) {
                flag++;
                index.setMessages("From: " + this.getUsername() + "\n" + "To: " + username + "\n\t" + message);
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("customers.bin"))) {
                    out.writeObject(this.getAllCustomers());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for (Admins index : getAllAdmins())
        {
            if (username.equals(index.getUsername())) {
                flag++;
                index.setMessages("From: " + this.getUsername() + "\n" + "To: " + username + "\n\t" + message);
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("admins.bin"))) {
                    out.writeObject(this.getAllAdmins());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (flag == 0)
            msgSent = false;

        return msgSent;
    }

}
