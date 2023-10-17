package users;

import accommodations.Accommodations;

import java.io.*;
import java.util.ArrayList;

/**
 * Στη συγκεκριμένη κλάση αρχικοποιόυνται οι χρήστες και αποθηκέυονται ανάλογα με τον τύπο τους σε μία απο τις
 * τρείς Λίστες {@link ArrayList<Admins> allAdmins}, {@link ArrayList<Providers> allProviders}, {@link ArrayList<Admins> allCustomers)}. Οι λίστες αυτές είναι του αντίστοιχου τύπο({@link users.Users}
 * ,{@link users.Providers}, {@link users.Customers}) και τύπου static ώστε ανεξάρτητα από το αντικείμενο {@link users.Users}( ή των υποκλάσεων του) που χρησιμοποιούμε
 * οι τιμές να είναι παντού ίδιες.
 * Τα πεδία είναι τα κοινά στοιχεία που έχουν και οι τρεις χρήστες
 * <p>
 * Υλοποιείται:
 * η σύνδεση του χρήστη στην εφαρμογή -> {@link #Login(String, String, Role)},
 * η εγγραφή χρήστη στην εφαρμογή -> {@link #Register(String, String, String, String, String)}
 *
 * </p>
 */

public class Users implements Serializable {
    private static ArrayList<Admins> allAdmins;
    private static ArrayList<Providers> allProviders;
    private static ArrayList<Customers> allCustomers;

    private static Accommodations accommodations;

    private String messages;
    private final Role role;
    private final String username;
    private final String password;
    private final String gender;

    /**
     * Default constructor
     * Αρχικοποιεί τις τιμές των πεδίων σε null και δημιουργεί τις κενές λίστες.
     * Στο τέλος καλείται η {@link #DefaultUsers()}, γιά την δημιουργία των default
     * χρηστών
     */
    public Users()
    {
        accommodations = new Accommodations();
        allAdmins = new ArrayList<>();
        allProviders = new ArrayList<>();
        allCustomers = new ArrayList<>();

        this.messages = null;
        this.username = null;
        this.password = null;
        this.role = null;
        this.gender = null;

        DefaultUsers();
    }

    /**
     * Constructor που χρησιμοποιείται για την δημιουργία αντικειμένων τύπου {@link Admins}, {@link Providers}, {@link Customers}
     *
     * @param username Όνομα χρήστη
     * @param password Κωδικός χρήστη
     * @param role     Τύπος χρήστη
     * @param Gender   Φύλο χρήστη
     */
    public Users(String username, String password, String role, String Gender)
    {
        this.username = username;
        this.password = password;
        this.role = RoleVerifier(role);
        this.gender = Gender;
    }

    /**
     * Μέθοδος που μετατρέπει τον ρόλο/τύπο που έχει εισάγει ο χρήστης σε έναν από τους
     * διαθέσιμους ρόλους που περιέχει η απαρίθμηση (enumeration) Role.
     *
     * @param role Τύπος χρήστη σε συμβολοσειρά (Admin/Provider/Customer).
     * @return Τύπος χρήστη ({@link Admins}/{@link Providers}/{@link Customers}) μεταφρασμένο σε απαρίθμηση (enumeration).
     */
    public Role RoleVerifier(String role)
    {
        return switch (role.toUpperCase())
                {
                    case "ADMIN" -> Role.ADMIN;
                    case "PROVIDER" -> Role.PROVIDER;
                    case "CUSTOMER" -> Role.CUSTOMER;
                    default -> Role.NOROLE;
                };
    }

    public ArrayList<Providers> getAllProviders()
    {
        return allProviders;
    }

    public ArrayList<Customers> getAllCustomers()
    {
        return allCustomers;
    }

    public ArrayList<Admins> getAllAdmins()
    {
        return allAdmins;
    }

    public String getGender()
    {
        return this.gender;
    }

    public String getPassword()
    {
        return this.password;
    }

    public Role getRole()
    {
        return this.role;
    }

    public String getUsername()
    {
        return this.username;
    }

    public Accommodations getAccommodations()
    {
        return accommodations;
    }

    public String getMessages() {
        if (messages == null) {
            return "You have no messages!";
        }
        return messages;
    }

    /**
     * Η μέθοδος αυτή επιστρέφει τη συμβολοσειρά των μηνυμάτων
     * Χρησιμοποιείται όταν ένας χρήστης θέλει να δει αν έχει εισερχόμενα μηνύματα
     *
     * @param messages Συμβολοσειρά με τα μηνύματα
     */
    public void setMessages(String messages)
    {
        this.messages = messages;
    }


    /**
     * Η μέθοδος αυτή επιτρέπει τη σύνδεση του χρήστη στην εφαρμογή.Ανάλογα με την μεταβλητή givenRole
     * το πρόγραμμα ανατρέχει στην αντίστοιχη Λίστα που έχουμε αποθηκεύσει χρήστες ίδιου τύπου. Έπειτα ελέγχει
     * αν ταυτίζονται το όνομα(given_username) και ο κωδικός (given_password) με κάποιον από τους υπάρχοντες χρήστες.
     *
     * @param Login_username Όνομα χρήστη
     * @param Login_password Κωδικός χρήστη
     * @param givenRole      Τύπος/ρόλος χρήστη
     * @return -1 Αν το Login είναι ανεπιτυχές. Την θέση στη λίστα που υπάρχει ο χρήστης αν συνδεθεί επιτυχώς
     */
    public int Login(String Login_username, String Login_password, Role givenRole)
    {
        int i = 0;
        switch (givenRole)
        {

            case ADMIN:
                for (Admins index : allAdmins)
                {
                    if (index.getUsername().equals(Login_username) && index.getPassword().equals(Login_password) && index.getRole().equals(givenRole))
                    {
                        return i;
                    }
                    i++;
                }
            case PROVIDER:
                i = 0;
                for (Providers index : allProviders)
                {
                    if (index.getUsername().equals(Login_username) && index.getPassword().equals(Login_password) && index.getRole().equals(givenRole))
                    {
                        return i;
                    }
                    i++;
                }
            case CUSTOMER:
                i = 0;
                for (Customers index : allCustomers)
                {
                    if (index.getUsername().equals(Login_username) && index.getPassword().equals(Login_password) && index.getRole().equals(givenRole))
                    {
                        return i;
                    }
                    i++;
                }
            default:
                return -1;
        }

    }

    public boolean Register(String username, String password, String role, String gender, String compName)
    {
        boolean usrExists = false;

        switch (RoleVerifier(role))
        {
            case PROVIDER -> {
                for (Providers provider : allProviders)
                {
                    if (provider.getUsername().equals(username))
                    {
                        usrExists = true;
                        break;
                    }
                }

                if (!usrExists) {
                    allProviders.add(new Providers(username, password, Role.PROVIDER.toString(), gender, compName));
                    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("providers.bin"))) {
                        out.writeObject(allProviders);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            case CUSTOMER -> {
                for (Customers customer : allCustomers)
                {
                    if (customer.getUsername().equals(username))
                    {
                        usrExists = true;
                        break;
                    }
                }

                if (!usrExists) {
                    allCustomers.add(new Customers(username, password, Role.CUSTOMER.toString(), gender));
                    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("customers.bin"))) {
                        out.writeObject(allCustomers);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return usrExists;
    }

    /**
     * Μέθοδος αυτή δημιουργεί τους default χρήστες κι έπειτα τους εισάγει στην αντίστοιχη λίστα
     */
    public void DefaultUsers() {
//        Admins admin = new Admins("Nikos", "Nikos12345", "Admin", "male");
//        allAdmins.add(admin);

//        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("admins.bin")))
//        {
//            out.writeObject(allAdmins);
//        } catch(IOException e)
//        {
//            e.printStackTrace();
//        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("admins.bin"))) {
            allAdmins = (ArrayList<Admins>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


//        Providers provider = new Providers("Dimitris", "Dimitris12345", "Provider", "male", "Lucy");
//        Providers provider1 = new Providers("Giorgos", "Giorgos12345", "Provider", "male", "Vogiatzis");
//        Providers provider2 = new Providers("Kostas", "Kostas12345", "Provider", "male", "Titania");
//        Providers provider3 = new Providers("Takis", "Takis12345", "Provider", "male", "Papadopoulos");
//
//        provider.activate();
//        provider1.activate();
//        provider2.activate();
//        provider3.activate();
//
//        allProviders.add(provider);
//        allProviders.add(provider1);
//        allProviders.add(provider2);
//        allProviders.add(provider3);
//
//        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("providers.bin")))
//        {
//            out.writeObject(allProviders);
//        } catch(IOException e)
//        {
//            e.printStackTrace();
//        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("providers.bin"))) {
            allProviders = (ArrayList<Providers>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
//        Customers customer = new Customers("Maria", "Maria12345", "Customer", "female");
//        Customers customer1 = new Customers("Makis", "Makis12345", "Customer", "male");
//
//        allCustomers.add(customer);
//        allCustomers.add(customer1);
//
//        customer.activate();
//        customer1.activate();
//        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("customers.bin")))
//        {
//            out.writeObject(allCustomers);
//        } catch(IOException e)
//        {
//            e.printStackTrace();
//        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("customers.bin"))) {
            allCustomers = (ArrayList<Customers>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Βασικοί (διαθέσιμοι) τύποι χρηστών.
     */
    enum Role
    {
        ADMIN,
        PROVIDER,
        CUSTOMER,
        NOROLE
    }

}


