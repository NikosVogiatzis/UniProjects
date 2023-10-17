package GUI;

import accommodations.Accommodations;
import accommodations.HotelRooms;
import accommodations.PrivateAccommodation;
import accommodations.reservervations.Date;
import photo_characteristicsDisplay.Display;
import users.Customers;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;


public class Customer extends JDialog {
    private JPanel Panel;
    private JTabbedPane mainTabbedPane;
    private JPanel WelcomePanel;
    private JLabel HelloMSG;
    private JButton viewAccommodationsButton;
    private JLabel accommodationsMSG;
    private JLabel reserveMSG;
    private JButton reserveButton;
    private JButton cancelReservationButton;
    private JLabel cancelReservationMSG;
    private JButton viewReservationsButton;
    private JLabel viewReservationsMSG;
    private JLabel messagesMSG;
    private JButton messagesButton;
    private JTabbedPane AccommodationsPane;
    private JPanel HotelRoomsPanel;
    private JScrollPane HotelRoomsScroll;
    private JScrollPane PrivateAccommodationsScroll;
    private JTable HotelRoomsTable;
    private JTable PrivateAccommodationsTable;
    private JButton logOutButton;
    private JPanel ReservePanel;
    private JPanel MyReservationsPanel;
    private JScrollPane MyReservationsScroll;
    private JTable MyReservationsTable;
    private JRadioButton roomRadioButton;
    private JRadioButton privateRadioButton;
    private JCheckBox addressCheckBox;
    private JTextField adressTextField;
    private JCheckBox sizeCheckBox;
    private JTextField SquareMetresTextField;
    private JCheckBox priceCheckBox;
    private JTextField PriceTextField;
    private JCheckBox capacityCheckBox;
    private JTextField CapacityTextField;
    private JLabel chooselabel;
    private JButton searchButton;
    private JPanel CheckBoxesPanel;
    private JComboBox TypeOfAccommodationCombo;
    private JButton searchButton1;
    private JLabel FoundAccommodationsMSG;
    private JScrollPane HotelRoomsFound;
    private JScrollPane PrivateAccommodationsFound;
    private JTable FoundRoomsTable;
    private JTable FoundPrivateTable;
    private JTextField preferedCharacteristicsText;
    private JCheckBox PreferedCharacteristicsBox;
    private JPanel RoomFoundPanel;
    private JPanel PrivateFoundPanel;
    private JButton clearButton;
    private JButton accommodationsButton;
    private JButton reserveSearchButton;
    private JTextField fromDateReserve;
    private JTextField toDateReserve;
    private JTextField idReserve;
    private JButton ReserveButton;
    private JButton myReservationsButton;
    private JTextField cancelID;
    private JButton cancelButton;


    private String setSquareText = "";
    private String setPriceText = "";
    private String setCapacityText = "";
    private String setAddressText = "";


    private String setCharacteristics = "";

    public Customer(Customers customer, Login login) {
        setTitle("[Customer] " + customer.getUsername());

        setContentPane(Panel);
        setModal(true);


        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // call onCancel() on ESCAPE
        Panel.registerKeyboardAction(e -> System.exit(0), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        ///Πρόσθεση των προσωπικών στοιχείων του πελάτη στο welcome Tab
        HelloMSG.setText("Hello " + customer.getUsername());
        accommodationsMSG.setText("There are " + customer.getAccommodations().getNumberOfAccommodations()
                + " accommodations you can view here!");
        /*
               Απενεργοποίηση των Panel που περιέχουν τα καταλύματα που βρέθηκαν
               στο Tab Search Accommodation, γιατί θέλουμε να εμφανίζονται μόνο
               αν βρέθηκαν καταλύματα αφού έγινε η αναζήτηση
         */
        RoomFoundPanel.setEnabled(false);
        RoomFoundPanel.setVisible(false);
        PrivateFoundPanel.setEnabled(false);
        PrivateFoundPanel.setVisible(false);


        /*
            Υλοποίηση των ActionListeners για τα κουμπία που βρίσκονται στο welcome Tab.
            με το πάτημα του μεταφερόμαστε στο αντίστοιχο Tab
         */
        viewAccommodationsButton.addActionListener(e -> mainTabbedPane.setSelectedIndex(1));
        searchButton1.addActionListener(e -> mainTabbedPane.setSelectedIndex(5));
        viewReservationsButton.addActionListener(e -> mainTabbedPane.setSelectedIndex(3));
        reserveButton.addActionListener(e -> mainTabbedPane.setSelectedIndex(2));
        messagesButton.addActionListener(e -> JOptionPane.showMessageDialog(null, customer.getMessages()));
        myReservationsButton.addActionListener(e -> mainTabbedPane.setSelectedIndex(3));
        cancelReservationButton.addActionListener(e -> mainTabbedPane.setSelectedIndex(4));
        accommodationsButton.addActionListener(e -> mainTabbedPane.setSelectedIndex(1));
        reserveButton.addActionListener(e -> mainTabbedPane.setSelectedIndex(4));
        reserveSearchButton.addActionListener(e -> mainTabbedPane.setSelectedIndex(5));

        //Δημιουργία πίνακα με όλα τα δωμάτια
        createHotelRoomsTable();
        //Δημιουργία πίνακα με όλα τα ιδιωτικά καταλύματα
        createPrivateAccommodationTable();
        //Δημιουργία πίνακα με τις ενεργές κρατήσεις του χρήστη
        createAllReservationsTable();
        /*
            MouseListeners για τους πίνακες που περιέχουν καταλύματα
            ώστε να βλέπουμε τα ειδικά χαρακτηριστικά τους
         */
        roomTableMouseListener(customer, HotelRoomsTable);
        privateAccommodationTableMouseListener(customer, PrivateAccommodationsTable);
        roomTableMouseListener(customer, FoundRoomsTable);
        privateAccommodationTableMouseListener(customer, FoundPrivateTable);

        logOut(login);

        /*
            Απενεργοποίηση του μηνύματος εύρεσης καταλύματος στο SearchAccommodation Tab,
            γιατί εμφανίζονται διαφορετικά μηνύματα ανάλογα με την αναζήτηση
         */
        FoundAccommodationsMSG.setVisible(false);



        /*
            ItemListener για το sizeCheckBox.Αν είναι επιλεγμένο
            θέτει στη μεταβλητή setSquareText την τιμή του πεδίου SquareMetresField
            και απενεργοποιεί το παραπάνω πεδίο. Διαφορετικά θέτει το κενό.
         */
        sizeCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1)
                    SquareMetresTextField.setEnabled(false);
                else SquareMetresTextField.setEnabled(true);
                setSquareText = ((e.getStateChange() == 1 ? SquareMetresTextField.getText() : ""));
            }
        });

        /*
            ItemListener για το priceCheckBox.Αν είναι επιλεγμένο
            θέτει στη μεταβλητή setPriceText την τιμή του πεδίου PriceTextField
            και απενεργοποιεί το παραπάνω πεδίο. Διαφορετικά θέτει το κενό.
         */
        priceCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1)
                    PriceTextField.setEnabled(false);
                else PriceTextField.setEnabled(true);
                setPriceText = ((e.getStateChange() == 1 ? PriceTextField.getText() : ""));
            }
        });

        /*
            ItemListener για το addressCheckBox.Αν είναι επιλεγμένο
            θέτει στη μεταβλητή setAddressText την τιμή του πεδίου adressTextField
            και απενεργοποιεί το παραπάνω πεδίο. Διαφορετικά θέτει το κενό.
         */
        addressCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1)
                    adressTextField.setEnabled(false);
                else adressTextField.setEnabled(true);
                setAddressText = ((e.getStateChange() == 1 ? adressTextField.getText() : ""));
            }
        });

        /*
            ItemListener για το PreferedCharacteristicsBox.Αν είναι επιλεγμένο
            θέτει στη μεταβλητή setCharacteristics την τιμή του πεδίου preferedCharacteristicsText
            και απενεργοποιεί το παραπάνω πεδίο. Διαφορετικά θέτει το κενό.
         */
        PreferedCharacteristicsBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    preferedCharacteristicsText.setEnabled(false);
                } else
                    preferedCharacteristicsText.setEnabled(true);
                setCharacteristics = ((e.getStateChange() == 1 ? preferedCharacteristicsText.getText() : ""));


            }
        });

        /*
            ItemListener για το capacityCheckBox.Αν είναι επιλεγμένο
            θέτει στη μεταβλητή setCapacityText την τιμή του πεδίου CapacityTextField
            και απενεργοποιεί το παραπάνω πεδίο. Διαφορετικά θέτει το κενό.
         */
        capacityCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1)
                    CapacityTextField.setEnabled(false);
                else
                    CapacityTextField.setEnabled(true);
                setCapacityText = ((e.getStateChange() == 1 ? CapacityTextField.getText() : ""));
            }
        });

        /*
            ActionListener του κουμπιού searchButton στο Tab SearchAccommodation.
            Για την αναζήτηση καταλύματος είναι υποχρετική η συμπλήρωση διεύθυνσης.
            Αν ο πελάτης επιλέξει αναζήτηση δωματίου,καλείται η συνάρτηση TabSearchRoom
            της κλάσης Customers, από την οποία επιστρέφεται η λίστα δωματίων που βρέθηκαν
            με τα δοθέντα κριτήρια σε συνδυασμό. Έπειτα εισάγονται αυτά τα δωμάτια στον πίνακα FoundRoomsTable,
            και ο πίνακας γίνεται ορατός. Αντίστοιχα αν ο πελάτης επιλέξει αναζήτηση καταλύματος επιστρέφεται η λίστα
            ιδιωτικών καταλυμάτων που βρέθηκαν με βάση τα κριτήρια, μέσω της TabSearchAccommodation της κλάσης Customers
            και εισάγεται η λίστα στον πίνακα FoundPrivateTable που στη συνέχεια γίνεται ορατός. Εμφανίζονται αντίστοιχα
            μηνύματα για το αν βρέθηκαν ή όχι καταλύματα μετά την αναζήτηση τους. Αν οι τιμές των πεδίων είναι λάθος (πχ γραμμα
            στο πεδίο για τα τετραγωνικά ) τότε εμφανίζεται μήνυμα λάθους και προτρέπει τον χρήστη να δει πάνω απο τα πεδία
            μέσω των ToolTipText το έγκυρο format


         */
        searchButton.addActionListener(e -> {
            int error = 0;
            //Λίστα με το εύρος αναζήτησης τετραγωνικών.Στο πρώτο κελί αποθηκεύεται η τιμή "από" και στο δεύτερο η τιμή "εως"
            ArrayList<Integer> preferredSquareness = new ArrayList<>();
            preferredSquareness.add(0);
            preferredSquareness.add(0);
            //Λίστα με το εύρος αναζήτησης τιμής.Στο πρώτο κελί αποθηκεύεται η τιμή "από" και στο δεύτερο η τιμή "εως"

            ArrayList<Integer> preferredPrice = new ArrayList<>();
            preferredPrice.add(0);
            preferredPrice.add(0);
            //Λίστα με τα εύρη τετραγωνικών και τιμής.Στην πρώτη θέση αποθηκεύεται η λίστα με το εύρος αναζήτησης τετραγωνικών
            //και στο δεύτερο κελί η λίστα με το εύρος αναζήτησης τιμής.Αρχικοποιείται με τιμές [ [0,0] ,[0,0] ]
            ArrayList<ArrayList<Integer>> ranges = new ArrayList<>();

            ArrayList<Integer> squareDump = new ArrayList<>();
            squareDump.add(0);
            squareDump.add(0);

            ArrayList<Integer> priceDump = new ArrayList<>();
            priceDump.add(0);
            priceDump.add(0);

            ranges.add(squareDump);
            ranges.add(priceDump);

            int preferredCapacity = -1;
            if (!setSquareText.equals("")) {
                String[] splitSizeText = setSquareText.split("-");
                if (splitSizeText.length != 2) {
                    error++;
                } else {
                    Scanner dump = new Scanner(splitSizeText[0]);
                    Scanner dump1 = new Scanner(splitSizeText[1]);
                    if (dump.hasNextInt() && dump1.hasNextInt()) {
                        preferredSquareness = new ArrayList<>();
                        preferredSquareness.add(Integer.parseInt(splitSizeText[0]));
                        preferredSquareness.add(Integer.parseInt(splitSizeText[1]));
                    } else
                        error++;
                }
            }

            if (!setPriceText.equals("")) {
                String[] splitPriceText = setPriceText.split("-");
                if (splitPriceText.length != 2)
                    error++;
                else {
                    Scanner dump = new Scanner(splitPriceText[0]);
                    Scanner dump1 = new Scanner(splitPriceText[1]);
                    if (dump.hasNextInt() && dump1.hasNextInt()) {
                        preferredPrice = new ArrayList<>();
                        preferredPrice.add(Integer.parseInt(splitPriceText[0]));
                        preferredPrice.add(Integer.parseInt(splitPriceText[1]));
                    } else
                        error++;
                }
            }

            ranges = new ArrayList<>();
            ranges.add(preferredSquareness);
            ranges.add(preferredPrice);
            Scanner dump = new Scanner(CapacityTextField.getText());
            if (!setCapacityText.equals("") && dump.hasNextInt())
                preferredCapacity = dump.nextInt();


            List<String> characteristics = null;
            if (!setCharacteristics.equals("")) {
                String[] temp = null;
                temp = setCharacteristics.split("/");
                characteristics = Arrays.asList(temp);
            }

            if (TypeOfAccommodationCombo.getSelectedIndex() == 0) {
                PrivateFoundPanel.setVisible(false);
                PrivateFoundPanel.setEnabled(false);
                ArrayList<HotelRooms> roomsFound = customer.TabSearchRoom(setAddressText, preferredCapacity,
                        ranges, characteristics);
                if (error == 0) {
                    if (roomsFound != null) {
                        FoundAccommodationsMSG.setText("Accommodations found!");
                        FoundAccommodationsMSG.setVisible(true);
                        RoomFoundPanel.setEnabled(true);
                        RoomFoundPanel.setVisible(true);
                        CreateFoundRoomsTable();
                        AddFoundRoomsToTable(roomsFound);

                    } else {
                        FoundAccommodationsMSG.setText("No Accommodations found!");
                        FoundAccommodationsMSG.setVisible(true);
                        RoomFoundPanel.setEnabled(false);
                        RoomFoundPanel.setVisible(false);
                    }
                }

            } else {
                RoomFoundPanel.setEnabled(false);
                RoomFoundPanel.setVisible(false);
                ArrayList<PrivateAccommodation> accommodationsFound = customer.TabSearchAccommodation(
                        setAddressText, preferredCapacity,
                        ranges, characteristics);
                if (error == 0) {
                    if (accommodationsFound != null) {
                        FoundAccommodationsMSG.setText("Accommodations found!");
                        FoundAccommodationsMSG.setVisible(true);
                        PrivateFoundPanel.setEnabled(true);
                        PrivateFoundPanel.setVisible(true);
                        CreateFoundPrivateTable();
                        AddPrivateFoundToTable(accommodationsFound);
                        FoundPrivateTable.setVisible(true);
                    } else {
                        RoomFoundPanel.setVisible(false);
                        PrivateFoundPanel.setVisible(false);
                        FoundAccommodationsMSG.setText("No Accommodations found!");
                        FoundAccommodationsMSG.setVisible(true);
                    }
                }

            }
            if (error != 0) {
                JOptionPane.showMessageDialog(null, "An error has occurred. \n Move your cursor onto fields" +
                        "to see the proper input format");
                FoundAccommodationsMSG.setText("Fill at least address's field!");
            }
        });

        /*
            ActionListener για το κουμπί cancelButton. Υλοποιείται η ακύρωση κράτησης. Ζητείται από το πεδίο cancelID, ο
            αναγνωριστικός αριθμός της κράτησης (ΟΧΙ του καταλύματος). Αν η κράτηση με αυτόν τον αριθμό αντιστοιχεί σε
            κράτηση του ίδιου πελάτη. Τότε η κράτηση ακυρώνεται, διαγράφεται από τον πίνακα που εμφανίζει τις ενεργές
            κρατήσεις και εμφανίζεται αντίστοιχο μήνυμα. Αν δεν συμπληρωθεί σωστά ο αναγνωριστικός αριθμός ή αν αυτός δεν αντιστοιχεί
            σε κράτηση του συγκεκριμένου πελάτη εμφανίζεται μήνυμα λάθους.
         */
        cancelButton.addActionListener(e -> {
            Scanner dump = new Scanner(cancelID.getText());
            if (!cancelID.getText().equals("") && dump.hasNextInt()) {
                if (customer.CancelReservation(dump.nextInt())) {
                    JOptionPane.showMessageDialog(null, "You have successfully cancelled your reservation!");
                    int rowCount = MyReservationsTable.getModel().getRowCount();
                    for (int i = rowCount - 1; i >= 0; i--) {
                        if (MyReservationsTable.getValueAt(i, 7).equals("" + cancelID.getText()))
                            ((DefaultTableModel) MyReservationsTable.getModel()).removeRow(i);
                    }
                } else
                    JOptionPane.showMessageDialog(null, "Could not cancel the reservation! \n" +
                            "You don't own a reservation with ID " + cancelID.getText());
            } else
                JOptionPane.showMessageDialog(null, "Make sure you have given the reservation's ID correctly");
            cancelID.setText("");
        });

        /*
         * ActionListener για το κουμπί clearButton στην καρτέλα αναζήτησης καταλύματος.
         * Όταν αυτό πατηθεί καθαρίζονται οι τιμές απ' όλα τα πεδία, κάνει uncheck τα checkboxes και
         * κρύβει τον πίνακα με τα καταλύματα που ενδεχομένως βρέθηκαν σε προηγούμενη αναζήτηση
         */
        clearButton.addActionListener(e -> {
            addressCheckBox.setSelected(false);
            sizeCheckBox.setSelected(false);
            priceCheckBox.setSelected(false);
            capacityCheckBox.setSelected(false);
            PreferedCharacteristicsBox.setSelected(false);
            adressTextField.setText("");
            SquareMetresTextField.setText("");
            PriceTextField.setText("");
            CapacityTextField.setText("");
            preferedCharacteristicsText.setText("");
            RoomFoundPanel.setVisible(false);
            PrivateFoundPanel.setVisible(false);
            FoundAccommodationsMSG.setVisible(false);
        });

        /*
         * Υλοποιήση του ActionListener για το κουμπί ReserveButton.
         * Ο χρήστης πρέπει να εισάγει τον αναγνωριστικό αριθμό του καταλύματος που επιθυμεί να κάνει κράτηση
         * και τις ημερομηνίες κράτησης.Καλείται η συνάρτηση TabBookAccommodation της κλάσης Customers και αν επιστραφεί
         * true, οπότε έγινε η κράτηση επιτυχώς, τότε διαγράφονται όλα τα στοιχεία απο τον πίνακα με τις ενεργές κρατήσεις
         * και εισάγονται εκ νέου οι κρατήσεις συμπεριλαμβανομένης και της τρέχουσας. Εμφανίζεται μήνυμα επιτυχημένης κράτησης.
         * Αν δεν γίνει επιτυχημένη κράτηση, δηλαδή ή είναι κρατημένο το κατάλυμα για τις επιθυμητές ημερομηνίες, ή δώθηκαν
         * λάθος ημερομηνίες (πχ από 4/5/2022 ως 2/5/2022) ή δεν συμπληρώθηκαν σωστά τα πεδία, εμφανίζεται μήνυμα λάθους.
         */
        ReserveButton.addActionListener(e -> {
            if (!idReserve.getText().equals("") && !fromDateReserve.getText().equals("")
                    && !toDateReserve.getText().equals("")) {
                Scanner dump = new Scanner(idReserve.getText());
                if (dump.hasNextInt()) {
                    int ID = dump.nextInt();
                    if (customer.TabBookAccommodation(ID, fromDateReserve.getText(), toDateReserve.getText(), customer)) {
                        int position = customer.getAccommodations().FindRoom(ID);
                        int position1 = customer.getAccommodations().FindAccommodation(ID);
                        if (position != -1 && position1 == -1) {
                            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hotelRooms.bin"))) {
                                out.writeObject(customer.getAccommodations().getRooms());
                            } catch (IOException err) {
                                err.printStackTrace();
                            }
                            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("accommodationsIdentifier.bin"))) {
                                out.writeObject(customer.getAccommodations().identifierManager());
                            } catch (IOException err) {
                                err.printStackTrace();
                            }

                            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("imageIdentifier.bin"))) {
                                out.writeObject(customer.getAccommodations().getImageIdentifier());
                            } catch (IOException err) {
                                err.printStackTrace();
                            }

                            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("reservationsID.bin"))) {
                                out.writeObject(customer.getAccommodations().reservationsIdentifierManager());
                            } catch (IOException err) {
                                err.printStackTrace();
                            }
                            //Καθαρίζει ο πίνακας και εισέρχονται οι νέες κρατήσεις του πελάτη έπειτα από την νέα
                            int rowCount = MyReservationsTable.getModel().getRowCount();
                            for (int i = rowCount - 1; i >= 0; i--) {
                                ((DefaultTableModel) MyReservationsTable.getModel()).removeRow(i);
                            }
                            AddReservationsToTable(
                                    customer.getAccommodations().UserPrivateReservations(customer),
                                    customer.getAccommodations().UserHotelReservations(customer), customer);
                            JOptionPane.showMessageDialog(null, "The accommodation was booked successfully!");
                        }

                        if (position1 != -1 && position == -1) {
                            //Καθαρίζει ο πίνακας και εισέρχονται οι νέες κρατήσεις του πελάτη έπειτα από την νέα
                            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("privateAccommodation.bin"))) {
                                out.writeObject(customer.getAccommodations().getAirbnb());
                            } catch (IOException err) {
                                err.printStackTrace();
                            }
                            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("accommodationsIdentifier.bin"))) {
                                out.writeObject(customer.getAccommodations().identifierManager());
                            } catch (IOException err) {
                                err.printStackTrace();
                            }

                            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("imageIdentifier.bin"))) {
                                out.writeObject(customer.getAccommodations().getImageIdentifier());
                            } catch (IOException err) {
                                err.printStackTrace();
                            }

                            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("reservationsID.bin"))) {
                                out.writeObject(customer.getAccommodations().reservationsIdentifierManager());
                            } catch (IOException err) {
                                err.printStackTrace();
                            }
                            DefaultTableModel temp = (DefaultTableModel) MyReservationsTable.getModel();
                            int rowCount = temp.getRowCount();
                            for (int i = rowCount - 1; i >= 0; i--) {
                                temp.removeRow(i);
                            }
                            AddReservationsToTable(
                                    customer.getAccommodations().UserPrivateReservations(customer),
                                    customer.getAccommodations().UserHotelReservations(customer), customer);
                            JOptionPane.showMessageDialog(null, "The accommodation was booked successfully!");

                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Could not reserve room for your" +
                                "preferred dates.\n It seems like someone else is staying there these days!"
                                + "Or move your cursor to see if you have typed your preferred dates correct!");
                    }
                } else
                    JOptionPane.showMessageDialog(null, "Make sure you fill the ID correctly!");
            } else
                JOptionPane.showMessageDialog(null, "Make sure you complete all required fields!");
            idReserve.setText("");
            fromDateReserve.setText("");
            toDateReserve.setText("");
        });
    }

    /**
     * Στη μέθοδο αυτή υλοποιείται ο {@link ActionListener} για το κουμπί {@link #logOutButton}
     * όπου κατά το πάτημα του αποσυνδέεται ο πελάτης από την εφαρμογή
     */
    public void logOut(Login login) {
        logOutButton.addActionListener(e -> {
            this.dispose();
            login.dispose();
        });
    }

    /**
     * Η μέθοδος αυτή δημιουργεί τον πίνακα με τις ενεργές κρατήσεις του πελάτη
     * στο tab {@link #MyReservationsTable}
     */
    private void createAllReservationsTable() {
        String[][] data = {};

        MyReservationsTable.setModel(new DefaultTableModel(
                data,
                new String[]{"Accommodation's ID", "Type/Room Number", "Price",
                        "Square Metres", "Company's name", "Address", "Reserved Dates", "Reservation's ID"}
        ));
    }

    /**
     * Η μέθοδος αυτή προσθέτει όλες τις ενεργές κρατήσεις του πελάτη
     * στον πίνακα {@link #MyReservationsTable}
     *
     * @param allReservationsPrivate HashMap με τα ιδιωτικά καταλύματα και τις ημερομηνίες κράτησης του πελάτη
     * @param allReservationsRooms   HashMap με τα ξενοδοχειακά δωμάτια και τις ημερομηνίες κράτησης του πελάτη
     * @param customer               Ο πελάτης για τον οποίο θέλουμε να εισάγουμε τις κρατήσεις του
     */
    public void AddReservationsToTable(
            HashMap<PrivateAccommodation, ArrayList<Date>> allReservationsPrivate,
            HashMap<HotelRooms, ArrayList<Date>> allReservationsRooms,
            Customers customer
    ) {
        DefaultTableModel model = (DefaultTableModel) MyReservationsTable.getModel();
        if (allReservationsPrivate != null) {
            allReservationsPrivate.forEach((key, value) ->
            {
                for (Date date : value) {
                    int i = 0;
                    for (int x = 0; x < key.getReservations().size(); x++) {
                        if (key.getReservations().get(x).getCustomer().getUsername().equals(customer.getUsername())
                                && key.getReservations().get(x).getReservationPeriod().contains(date)) {
                            i = key.getReservations().get(x).getId();
                            break;
                        }
                    }
                    model.addRow(new Object[]{
                            key.getId(),
                            "Type: " + key.getType(),
                            key.getPrice() + "€",
                            key.getSquareMetres() + "m²",
                            "\uD83D\uDCBC " + key.getCompanyName(),
                            "\uD83D\uDCCD " + key.getAddress(),
                            "\uD83D\uDCC6 " + date.toString(),
                            "" + i
                    });
                }
            });
        }

        if (allReservationsRooms != null) {
            allReservationsRooms.forEach((key, value) ->
            {

                for (Date date : value) {
                    int i = 0;
                    for (int x = 0; x < key.getReservations().size(); x++) {
                        if (key.getReservations().get(x).getCustomer().getUsername().equals(customer.getUsername())
                                && key.getReservations().get(x).getReservationPeriod().contains(date)) {
                            i = key.getReservations().get(x).getId();
                            break;
                        }
                    }
                    model.addRow(new Object[]{
                            key.getId(),
                            "Room: " + key.getRoomNumber(),
                            key.getPrice() + "€",
                            key.getSquareMetres() + "m²",
                            "\uD83D\uDCBC " + key.getHotelName(),
                            "\uD83D\uDCCD " + key.getAddress(),
                            "\uD83D\uDCC6 " + date.toString(),
                            "" + i
                    });
                }
            });
        }

    }

    /**
     * Η μέθοδος αυτή δημιουργεί τον πίνακα {@link #FoundRoomsTable}
     * στον οποίο αργότερα θα εισαχθούν τα δωμάτια που βρέθηκαν κατά
     * την αναζήτηση δωματίου με κριτήρια
     */
    public void CreateFoundRoomsTable() {
        Object[][] data = {};
        FoundRoomsTable.setModel(new DefaultTableModel(
                data,
                new Object[]{"Hotel name", "Location", "Room's number", "Floor",
                        "Square metres", "Price", "ID", "Capacity", "Characteristics"}

        ));
    }

    /**
     * Η μέθοδος αυτή δημιουργεί τον πίνακα {@link #FoundRoomsTable}
     * στον οποίο αργότερα θα εισαχθούν τα ιδιωτικά καταλύματα που βρέθηκαν κατά
     * την αναζήτηση ιδιωτικού καταλύματος με κριτήρια
     */
    public void CreateFoundPrivateTable() {
        Object[][] data = {};
        FoundPrivateTable.setModel(new DefaultTableModel(
                data,
                new Object[]{"Company's name", "Type", "Location", "Square metres", "price", "Capacity", "Characteristics", "ID"}
        ));
    }

    /**
     * Η μέθοδος αυτή δημιουργεί τον πίνακα στον οποίο
     * θα προσθεθούν όλα τα δωμάτια ξενοδοχείων
     */
    public void createHotelRoomsTable() {
        Object[][] data = {};
        HotelRoomsTable.setModel(new DefaultTableModel(
                data,
                new Object[]{"Hotel name", "Location", "Room's number", "Floor",
                        "Square metres", "Price", "ID", "Capacity", "Characteristics"}

        ));
    }

    /**
     * Η μέθοδος αυτή δημιουργεί τον πίνακα {@link #PrivateAccommodationsTable}
     * στον οποίο αργότερα θα εισαχθούν όλα τα ιδιωτικά καταλύματα που υπάρχουν στην εφαρμογή
     */
    public void createPrivateAccommodationTable() {

        Object[][] data = {};
        PrivateAccommodationsTable.setModel(new DefaultTableModel(
                data,
                new Object[]{"Company's name", "Type", "Location", "Square metres", "price", "Capacity", "Characteristics", "ID"}
        ));
    }

    /**
     * Η μέθοδος αυτή προσθέτει τα δωμλατια που βρέθηκαν κατά την αναζήτηση δωματίου στον
     * πίνακα {@link #FoundRoomsTable}
     *
     * @param rooms Λίστα με όλα τα δωμάτια που βρέθηκαν κατά την αναζήτηση του χρήστη
     */
    public void AddFoundRoomsToTable(ArrayList<HotelRooms> rooms) {
        DefaultTableModel model = (DefaultTableModel) FoundRoomsTable.getModel();
        for (HotelRooms room : rooms) {
            model.addRow(new Object[]{
                    "\uD83D\uDCBC " + room.getHotelName(),
                    "\uD83D\uDCCD " + room.getAddress(),
                    room.getRoomNumber(),
                    room.getFloor(),
                    room.getSquareMetres() + "m²",
                    room.getPrice() + "€",
                    "" + room.getId(),
                    room.getCapacity(),
                    "Click Here!"
            });
        }
    }

    /**
     * Η μέθοδος αυτή εισάγει όλα τα δωμάτια που υπάρχουν στην εφαρμογή στον
     * πίνκακα {@link #HotelRoomsTable}
     *
     * @param rooms Λίστα τύπου {@link HotelRooms} με όλα τα δωμάτια που υπάρχουν στην εφαρμογή
     */
    public void AddHotelRoomsToTable(ArrayList<HotelRooms> rooms) {
        DefaultTableModel model = (DefaultTableModel) HotelRoomsTable.getModel();
        for (HotelRooms room : rooms) {
            model.addRow(new Object[]{
                    "\uD83D\uDCBC " + room.getHotelName(),
                    "\uD83D\uDCCD " + room.getAddress(),
                    room.getRoomNumber(),
                    room.getFloor(),
                    room.getSquareMetres() + "m²",
                    room.getPrice() + "€",
                    "" + room.getId(),
                    room.getCapacity(),
                    "Click Here!"
            });
        }
    }

    /**
     * Η μέθοδος αυτή εισάγει στον πίνακα {@link #FoundPrivateTable}
     * όλα τα ιδιωτικά καταλύματα που βρέθηκαν κατά την αναζήτηση ιδιωτικού καταλύματος με κριτήρια
     *
     * @param accommodations Λίστα τύπου {@link PrivateAccommodation} με
     *                       τα ιδιωτικά καταλύματα που βρέθηκαν
     */
    public void AddPrivateFoundToTable(ArrayList<PrivateAccommodation> accommodations) {
        DefaultTableModel model = (DefaultTableModel) FoundPrivateTable.getModel();
        for (PrivateAccommodation accommodation : accommodations) {
            model.addRow(new Object[]{
                    "\uD83D\uDCBC " + accommodation.getCompanyName(),
                    "Type: " + accommodation.getType(),
                    "\uD83D\uDCCD " + accommodation.getAddress(),
                    accommodation.getSquareMetres() + "m²",
                    accommodation.getPrice() + "€",
                    accommodation.getCapacity(),
                    "Click here!",
                    "" + accommodation.getId()
            });
        }
    }

    /**
     * Η μέθοδος αυτή προσθέτει όλα τα ιδιωτικά καταλύματα στον πίνακα
     * {@link #PrivateAccommodationsTable}
     *
     * @param accommodations Λίστα τύπου {@link PrivateAccommodation} με όλα τα
     *                       ιδιωτικά καταλύματα που υπάρχουν στην εφαρμογή
     */
    public void AddPrivateAccommodationsToTable(ArrayList<PrivateAccommodation> accommodations) {
        DefaultTableModel model = (DefaultTableModel) PrivateAccommodationsTable.getModel();
        for (PrivateAccommodation accommodation : accommodations) {
            model.addRow(new Object[]{
                    "\uD83D\uDCBC " + accommodation.getCompanyName(),
                    "Type: " + accommodation.getType(),
                    "\uD83D\uDCCD " + accommodation.getAddress(),
                    accommodation.getSquareMetres() + "m²",
                    accommodation.getPrice() + "€",
                    accommodation.getCapacity(),
                    "Click here!",
                    "" + accommodation.getId()
            });
        }
    }

    /**
     * Η μέθοδος αυτή υλοποιεί {@link MouseListener} για τους πίνακες που αφορούν τα ιδιωτικά καταλύματα.
     * Αυτόν που τα περιέχει όλα και αυτόν που προστίθενται καταλύματα κατά την αναζήτησή τους με κριτήρια.
     * Στο κλικ του ποντικού στην στήλη που αποθηκεύει
     * τα ειδικά χαρακτηριστικά του ιδιωτικού καταλύματος, εμφανίζει με την βοήθεια της
     * κλάσης {@link Display}την Λίστα με τα χαρακτηριστικά
     * του καταλύματος {@link Accommodations#getCharacteristics()} και την φωτογραφία
     * του καταλύματος με όνομα {@link Accommodations#getImageName()}
     *
     * @param customer Ο χρήστης που είναι συνδεδεμένος στην εφαρμογή
     * @param table    Ο πίνακας που αποθηκεύει ιδιωτικά καταλύματα.Χρησιμοποιούνται οι πίνακες,
     *                 {@link #PrivateAccommodationsTable} και {@link #FoundPrivateTable}
     */
    public void privateAccommodationTableMouseListener(Customers customer, JTable table) {
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col == 6) {
                    int rowSelected = table.getSelectedRow();
                    int accommodationFound = customer.getAccommodations().FindAccommodation(Integer.parseInt(table.getValueAt(rowSelected, 7).toString()));
                    Display temp = new Display(customer.getAccommodations().getAirbnb().get(accommodationFound).getCharacteristics(),
                            customer.getAccommodations().getAirbnb().get(accommodationFound).getImageName());
                    temp.pack();
                    temp.setVisible(true);

                }
            }
        });
    }

    /**
     * Η μέθοδος αυτή υλοποιεί {@link MouseListener} για τους πίνακες που αφορούν τα ξενοδοχειακά δωμάτια.
     * Αυτόν που τα περιέχει όλα και αυτόν που προστίθενται δωμάτια κατά την αναζήτησή τους με κριτήρια.
     * Στο κλικ του ποντικού στην στήλη που αποθηκεύει
     * τα ειδικά χαρακτηριστικά του δωματίου, εμφανίζει με την βοήθεια της
     * κλάσης {@link Display}την Λίστα με τα χαρακτηριστικά
     * του καταλύματος {@link Accommodations#getCharacteristics()} και την φωτογραφία
     * του καταλύματος με όνομα {@link Accommodations#getImageName()}
     *
     * @param customer Ο χρήστης που είναι συνδεδεμένος στην εφαρμογή
     * @param table    Ο πίνακας που αποθηκεύει ξενοδοχειακά δωμάτια.Χρησιμοποιούνται οι πίνακες,
     *                 {@link #HotelRoomsTable} και {@link #FoundRoomsTable}
     */
    public void roomTableMouseListener(Customers customer, JTable table) {
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int col = table.columnAtPoint(evt.getPoint());
                if (row >= 0 && col == 8) {
                    int rowSelected = table.getSelectedRow();
                    int roomFound = customer.getAccommodations().FindRoom(Integer.parseInt(table.getValueAt(rowSelected, 6).toString()));
                    Display temp = new Display(customer.getAccommodations().getRooms().get(roomFound).getCharacteristics(),
                            customer.getAccommodations().getRooms().get(roomFound).getImageName());
                    temp.pack();
                    temp.setVisible(true);

                }
            }
        });
    }
}

