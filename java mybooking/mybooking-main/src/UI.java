import GUI.Admin;
import GUI.Customer;
import GUI.Login;
import GUI.Provider;
import users.Users;

import java.util.Scanner;

/**
 * Η κλάση αυτή επιτρέπει την επικοινωνία του χρήστη της εφαρμογής με την ίδια την εφαρμογή.Δημιουργεί
 * ένα αντικείμενο τύπου {@link users.Users}(μέσα στο οποίο δημιουργούνται οι τρεις default
 * χρήστες: ({@link users.Admins}, {@link users.Providers}, {@link users.Customers}))
 * Μετά την δημιουργία των χρηστών, ο χρήστης πρέπει να κάνει log in με έναν λογαριασμό που ήδη υπάρχει
 * <p>
 * Μεταβλητές κλάσης: Μία τύπου {@link Scanner} ή οποία χρησιμοποιείται για την εισαγωγή δεδομένων από το πληκτρολόγιο
 */
public class UI
{
    Scanner inputs;

    /**
     * Default constructor
     */
    public UI()
    {
        this.inputs = new Scanner(System.in);
    }

    /**
     * Η κύρια συνάρτηση της εφαρμογής μέσω της οποίας επιτρέπεται η διεπαφή χρήστη-προγράμματος. Επιτρέπει τη
     * σύνδεση ενός χρήστη με την κλήση της {@link Users#Login(String, String, Users.Role)}. Έπειτα απο την επιτυχημένη σύνδεση του χρήστη
     * ανάλογα με τον ρόλο του χρήστη εμφανίζεται το αντοίστοιχο panel και αρχικοποιούνται τα στατικά δεδομένα μέσα σε αυτό
     * όπως οι πίνακες για τα καταλύματα,για τις κρατήσεις
     */


    public void StartApp()
    {
        boolean failLogin = false;
        Users Default = new Users();

        while (true)
        {
            Login login = new Login();
            login.setBase(Default);

            if (failLogin) login.setVisibleWrongCredentialsMsg();

            login.pack();
            login.setVisible(true);

            String role = login.getUserType();

            if (!role.equalsIgnoreCase("admin")
                    && !role.equalsIgnoreCase("customer")
                    && !role.equalsIgnoreCase("provider"))
                continue;

            String username = login.getUsername();
            String password = login.getPassword();

            int usersPosition = Default.Login(
                    username,
                    password,
                    Default.RoleVerifier(role));

            if (usersPosition == -1)
            {
                failLogin = true;
            } else
            {
                if (role.equalsIgnoreCase("admin")) {
                    //login.dispose();
                    AdminMenu(Default, usersPosition, login);
                }

                if (role.equalsIgnoreCase("provider")) {
                    // login.dispose();
                    ProvidersMenu(Default, usersPosition, login);
                }


                if (role.equalsIgnoreCase("customer")) {
                    //   login.dispose();
                    CustomersMenu(Default, usersPosition, login);
                }
            }

        }


    }

    /**
     * Εμφάνιση του μενού των διαχειριστών και επιλογή λειτουργίας προς εκτέλεση
     *
     * @param Default Αντικείμενο τύπου {@link Users}.Χρησιμοποιείται ώστε να βρεθεί στη λίστα των allAdmins
     *                ο διαχειριστής που συνδέθηκε
     * @param temp    Ακέραιος δείκτης που χρησιμοποιείται για την εύρεση της θέσης του διαχειριστή στη λίστα allAdmins
     */
    public void AdminMenu(Users Default, int temp, Login login) {
        Admin adminWindow = new Admin(Default, temp, login);
        adminWindow.addUsersToTable(Default.getAllAdmins().get(temp).ViewAllUsers());
        adminWindow.setHelloMsgTo(Default.getAllAdmins().get(temp).getUsername());
        adminWindow.AddHotelRoomsToTable(Default.getAccommodations().getRooms());
        adminWindow.AddPrivateAccommodationsToTable(Default.getAccommodations().getAirbnb());
        adminWindow.addReservationsToTable(
                Default.getAllAdmins().get(temp).viewAllReservationsOfPrivateAccommodation(),
                Default.getAllAdmins().get(temp).viewAllReservationsOfRooms()
        );

        adminWindow.pack();
        adminWindow.setEnabled(true);
        adminWindow.setVisible(true);

    }


    /**
     * Εμφάνιση του μενού των παρόχων και επιλογή λειτουργίας προς εκτέλεση
     *
     * @param Default Αντικείμενο τύπου {@link Users}.Χρησιμοποιείται ώστε να βρεθεί στη λίστα των allProviders
     *                ο πάροχος που συνδέθηκε
     * @param temp    Ακέραιος δείκτης που χρησιμοποιείται για την εύρεση της θέσης του παρόχου στη λίστα allProviders
     */
    public void ProvidersMenu(Users Default, int temp, Login login) {

        Provider provider = new Provider(Default.getAllProviders().get(temp), login);

        //Φτιάχνει τον πίνακα με τα δωμάτια ξενοδοχείου που έχει στην κατοχή του ο πάροχος που συνδέθηκε
        provider.addHotelRoomsToTable(Default.getAllProviders().get(temp));
        //Φτιάχνει τον πίνακα με τα ιδιωτικά καταλύματα που έχει στην κατοχή του ο πάροχος που συνδέθηκε
        provider.addPrivateAccommodationsToTable(Default.getAllProviders().get(temp));

        provider.addMyReservations(Default.getAllProviders().get(temp).PrivateReservations(),
                Default.getAllProviders().get(temp).RoomReservations());
        provider.pack();
        provider.setVisible(true);
    }

    /**
     * Εμφάνιση του μενού των πελατών και επιλογή λειτουργίας προς εκτέλεση
     *
     * @param Default Αντικείμενο τύπου {@link Users}.Χρησιμοποιείται ώστε να βρεθεί στη λίστα των allCustomers
     *                ο πελάτης που συνδέθηκε
     * @param temp    Ακέραιος δείκτης που χρησιμοποιείται για την εύρεση της θέσης του πελάτη στη λίστα allCustomers
     */
    public void CustomersMenu(Users Default, int temp, Login login) {
        Customer customerWindow = new Customer(Default.getAllCustomers().get(temp), login);
        customerWindow.AddHotelRoomsToTable(Default.getAccommodations().getRooms());
        customerWindow.AddPrivateAccommodationsToTable(Default.getAccommodations().getAirbnb());
        customerWindow.AddReservationsToTable(
                Default.getAllCustomers().get(temp).getAccommodations().UserPrivateReservations(Default.getAllCustomers().get(temp)),
                Default.getAllCustomers().get(temp).getAccommodations().UserHotelReservations(Default.getAllCustomers().get(temp)),
                Default.getAllCustomers().get(temp));
        customerWindow.pack();
        customerWindow.setVisible(true);
    }


}



