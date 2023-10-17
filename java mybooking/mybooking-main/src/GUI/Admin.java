package GUI;

import accommodations.Accommodations;
import accommodations.HotelRooms;
import accommodations.PrivateAccommodation;
import accommodations.reservervations.Date;
import photo_characteristicsDisplay.Display;
import users.Admins;
import users.Providers;
import users.Users;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Admin extends JDialog
{
    private final Users base;
    private final int position;
    private JPanel panel;
    private JTable allUsersTable;
    private JLabel helloMsg;
    private JTable allReservationsTable;
    private JTextField toUserMsgField;
    private JTextArea messageArea;
    private JButton toUserMsgBtn;
    private JLabel msgFail;
    private JLabel msgSuccess;
    private JLabel helloMsgDesc;
    private JTextField fieldUserToActivate;
    private JButton activateBtn;
    private JLabel msgActUsrNotExist;
    private JLabel msgActUsrExist;
    private JTable tableInactiveProviders;
    private JButton LogOut1;
    private JTabbedPane tabbedPane;
    private JButton allUsersButton;
    private JButton activateButton;
    private JButton sendMessageButton;
    private JButton reservationsButton;
    private JTabbedPane tabbedPane1;
    private JScrollPane HotelRoomsPane;
    private JScrollPane PrivateAccommodationsPane;
    private JTable HotelRoomsTable;
    private JTable PrivateAccomodationsTable;
    private JButton accommodationsButton;


    public Admin(Users base, int position, Login login)
    {
        setTitle("[Admin] " + base.getAllAdmins().get(position).getUsername());

        setContentPane(panel);
        setModal(true);

        tabbedPane.setEnabled(true);
        this.base = base;
        this.position = position;

        //ActionListeners για τα κουμπιά που βρίσκονται στο welcomeTab
        sendMessageButton.addActionListener(e -> tabbedPane.setSelectedIndex(4));
        activateButton.addActionListener(e -> tabbedPane.setSelectedIndex(5));
        allUsersButton.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        reservationsButton.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        accommodationsButton.addActionListener(e -> tabbedPane.setSelectedIndex(3));

        //Καλούνται οι μέθοδοι που δημιουργούν τους πίνακες: (Χρηστων/καταλυμάτων/κρατήσεων/ΑνενεργώνΠαρόχων)
        createAllUsersTable();
        createAllReservationsTable();
        createTableInactiveProviders();
        createHotelRoomsTable();

        createPrivateAccommodationTable();
        privateAccommodationTableMouseListener(base.getAllAdmins().get(position));
        roomTableMouseListener(base.getAllAdmins().get(position));

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });

        // call onCancel() on ESCAPE
        panel.registerKeyboardAction(e -> System.exit(0),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        // send msg to user on click
        toUserMsgBtn.addActionListener(e -> sendMessage());
        activateBtn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                activateUser(base.getAllAdmins().get(position));
            }
        });

        logOut(login);

    }

    /**
     * Η μέθοδος αυτή δημιουργεί τον πίνακα στον οποίο
     * θα προσθεθούν όλα τα δωμάτια ξενοδοχείων
     */
    public void createHotelRoomsTable()
    {
        Object[][] data = {};
        HotelRoomsTable.setModel(new DefaultTableModel(
                data,
                new Object[]{"Hotel name", "Location", "Room's number", "Floor",
                        "Square metres", "Price", "ID", "Capacity", "Characteristics"}

        ));
    }

    /**
     * Η μέθοδος αυτή εισάγει όλα τα δωμάτια που υπάρχουν στην εφαρμογή στον
     * πίνκακα {@link #HotelRoomsTable}
     *
     * @param rooms Λίστα τύπου {@link HotelRooms} με όλα τα δωμάτια που υπάρχουν στην εφαρμογή
     */
    public void AddHotelRoomsToTable(ArrayList<HotelRooms> rooms)
    {
        DefaultTableModel model = (DefaultTableModel) HotelRoomsTable.getModel();
        for (HotelRooms room : rooms)
        {
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
     * Η μέθοδος αυτή δημιουργεί τον πίνακα στον οποίο θα
     * προσθεθούν όλα τα ιδιωτικά καταλύματα
     */
    public void createPrivateAccommodationTable()
    {
        Object[][] data = {};
        PrivateAccomodationsTable.setModel(new DefaultTableModel(
                data,
                new Object[]{"Company's name", "Type", "Location", "Square metres", "price", "Capacity", "Characteristics", "ID"}
        ));
    }

    /**
     * Η μέθοδος αυτή εισάγει στον πίνακα {@link #PrivateAccomodationsTable}
     * όλα τα ιδιωτικά καταλύματα που υπάρχουν στην εφαρμογή
     *
     * @param accommodations Λίστα τύπου {@link PrivateAccommodation} με όλα
     *                       τα ιδιωτικά καταλύματα
     */
    public void AddPrivateAccommodationsToTable(ArrayList<PrivateAccommodation> accommodations)
    {
        DefaultTableModel model = (DefaultTableModel) PrivateAccomodationsTable.getModel();
        for (PrivateAccommodation accommodation : accommodations)
        {
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
     * {@link ActionListener} για το κουμπί LogOut που αποσυνδέει τον διαχειριστή απο την εφαρμογή
     *
     * @param login Το frame Login που συνδέθηκε ο διαχειριστής στην εφαρμογή (Κλείνει κατα την αποσύνδεση και ανοίγει νέο)
     */
    public void logOut(Login login) {
        LogOut1.addActionListener(e ->
        {
            this.dispose();
            login.dispose();
        });
    }

    /**
     * Η μέθοδος αυτή δημιουργεί τον πίνακα στον οποίο
     * αργότερα θα εισαχθούν όλοι οι χρήστες της εφαρμογής
     */
    private void createAllUsersTable() {
        String[][] data = {};

        allUsersTable.setModel(new DefaultTableModel(
                data,
                new String[]{"", "", "", ""}
        ));
    }

    /**
     * Η μέθοδος αυτή δημιουργεί τον πίνακα στον οποίο αργότερα
     * θα εισαχθούν όλες οι ενεργές κρατήσεις για τα καταλύματα
     * της εφαρμογής
     */
    private void createAllReservationsTable() {
        String[][] data = {};

        allReservationsTable.setModel(new DefaultTableModel(
                data,
                new String[]{"", "", "", "", "", "", ""}
        ));
    }

    /**
     * Η μέθοδος αυτή εισάγει όλους τους χρήστες που υπάρχουν στην εφαρμογή στον πίνακα {@link #allUsersTable}
     *
     * @param users Λίστα με όλους τους χρήστες της πλατφόρμας
     */
    public void addUsersToTable(ArrayList<Users> users) {
        DefaultTableModel model = (DefaultTableModel) allUsersTable.getModel();

        for (Users user : users) {
            model.addRow(new Object[]{
                    user.getRole(),
                    user.getUsername(),
                    user.getGender(),
                    user.getPassword(),
            });
        }
    }

    /**
     * Στη μέθοδο αυτή εισάγονται στον πίνακα {@link #allReservationsTable} όλες οι ενεργές κρατήσεις δωματίων
     * και ιδιωτικών καταλυμάτων
     *
     * @param allReservationsPrivate HashMap με τα ιδιωτικά καταλύματα και τις ημερομηνίες κράτησης τους
     * @param allReservationsRooms   HashMap με τα δωμάτια ξενοδοχείων και τις ημερομηνίες κράτησης τους
     */
    public void addReservationsToTable(
            HashMap<PrivateAccommodation, ArrayList<Date>> allReservationsPrivate,
            HashMap<HotelRooms, ArrayList<Date>> allReservationsRooms
    ) {
        DefaultTableModel model = (DefaultTableModel) allReservationsTable.getModel();
        allReservationsPrivate.forEach((key, value) ->
        {
            for (Date date : value) {
                model.addRow(new Object[]{
                        "ID " + key.getId(),
                        key.getType(),
                        key.getPrice() + "€",
                        key.getSquareMetres() + "m²",
                        "\uD83D\uDCBC " + key.getCompanyName(),
                        "\uD83D\uDCCD " + key.getAddress(),
                        "\uD83D\uDCC6 " + date.toString()
                });
            }
        });

        allReservationsRooms.forEach((key, value) ->
        {
            for (Date date : value) {
                model.addRow(new Object[]{
                        "ID " + key.getId(),
                        "Floor " + key.getFloor(),
                        key.getPrice() + "€",
                        key.getSquareMetres() + "m²",
                        "\uD83D\uDCBC " + key.getHotelName(),
                        "\uD83D\uDCCD " + key.getAddress(),
                        "\uD83D\uDCC6 " + date.toString()
                });
            }
        });
    }

    /**
     * Η μέθοδος αυτή δημιουργεί τον πίνακα με τους ανενεργούς λογαριασμούς παρόχων
     */
    private void createTableInactiveProviders() {
        String[][] data = {};

        tableInactiveProviders.setModel(new DefaultTableModel(
                data,
                new String[]{"Username", "Gender", "Password"}
        ));

        addInactiveProvidersToTable();
    }

    /**
     * Η μέθοδος αυτή προσθέτει όλους τους λογαριασμούς παρόχων που δεν έχουν εγκριθεί ακόμα από τον διαχειριστή
     * στον πίνακα {@link #tableInactiveProviders}
     */
    public void addInactiveProvidersToTable() {
        DefaultTableModel model = (DefaultTableModel) tableInactiveProviders.getModel();

        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }

        for (Providers user : base.getAllProviders()) {
            if (user.accountStatus())
                continue;

            model.addRow(new Object[]{
                    user.getUsername(),
                    user.getGender(),
                    user.getPassword(),
            });
        }
    }

    /**
     * Η μέθοδος αυτή συμπληρώνει το μήνυμα που καλωσορίζει τον διαχειριστή στην πλατφόρμα
     *
     * @param name Username διαχειριστή που είναι συνδεδεμένος στην εφαρμογή
     */
    public void setHelloMsgTo(String name) {
        int allUsers = base.getAllAdmins().size() + base.getAllProviders().size() + base.getAllCustomers().size();
        int allAcc = base.getAccommodations().getRooms().size() + base.getAccommodations().getAirbnb().size();
        this.helloMsg.setText("Hello " + name + ", how are you doing today.");
        this.helloMsgDesc.setText("It looks like " + allUsers +
                " people are on our platform and " + allAcc + " accommodations and hotel rooms!");
    }

    public String getToUserMsgField() {
        return toUserMsgField.getText();
    }

    public String getMessageArea() {
        return messageArea.getText();
    }

    /**
     * Η μέθοδος αυτή επιτρέπει στον διαχειριστή να στείλει μήνυμα σε κάποιον χρήστη
     */
    public void sendMessage() {
        boolean isSent = base.getAllAdmins().get(position).SendMessage(getToUserMsgField(), getMessageArea());
        msgFail.setVisible(!isSent);
        msgSuccess.setVisible(isSent);
    }

    /**
     * Η μέθοδος αυτή επιτρέπει στον διαχειριστή να ενεργοποιήσει τον λογαριασμό ενός παρόχου που
     * μόλις έχει κάνει εγγραφή στην εφαρμογή
     *
     * @param admin Ο διαχειριστής που είναι συνδεδεμένος στην εφαρμογή
     */
    private void activateUser(Admins admin) {
        boolean userFound = false;
        for (Providers provider : base.getAllProviders()) {
            if (fieldUserToActivate.getText().equalsIgnoreCase(provider.getUsername()) && !provider.accountStatus()) {
                provider.activate();
                userFound = true;
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("providers.bin"))) {
                    out.writeObject(admin.getAllProviders());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        msgActUsrNotExist.setVisible(!userFound);
        msgActUsrExist.setVisible(userFound);

        addInactiveProvidersToTable();
    }

    /**
     * Η μέθοδος αυτή υλοποιεί {@link MouseListener} για τον πίνακα που αποθηκεύει τα ιδιωτικά καταλύματα.
     * Στο κλικ του ποντικού στην στήλη που αποθηκεύει
     * τα ειδικά χαρακτηριστικά του ιδιωτικού καταλύματος, εμφανίζει με την βοήθεια της
     * κλάσης {@link Display}την Λίστα με τα χαρακτηριστικά
     * του καταλύματος {@link Accommodations#getCharacteristics()} και την φωτογραφία
     * του καταλύματος με όνομα {@link Accommodations#getImageName()}
     *
     * @param admin Ο διαχειριστής που είναι συνδεδεμένος στην εφαρμογή
     */
    public void privateAccommodationTableMouseListener(Admins admin)
    {
        PrivateAccomodationsTable.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                int row = PrivateAccomodationsTable.rowAtPoint(evt.getPoint());
                int col = PrivateAccomodationsTable.columnAtPoint(evt.getPoint());
                if (row >= 0 && col == 6)
                {
                    int rowSelected = PrivateAccomodationsTable.getSelectedRow();
                    int accommodationFound = admin.getAccommodations().FindAccommodation(Integer.parseInt(PrivateAccomodationsTable.getValueAt(rowSelected, 7).toString()));
                    Display temp = new Display(admin.getAccommodations().getAirbnb().get(accommodationFound).getCharacteristics(),
                            admin.getAccommodations().getAirbnb().get(accommodationFound).getImageName());
                    temp.pack();
                    temp.setVisible(true);

                }
            }
        });
    }

    /**
     * Η μέθοδος αυτή υλοποιεί {@link MouseListener} για τους πίνακες που αφορούν τα ξενοδοχειακά δωμάτια.
     * Στο κλικ του ποντικού στην στήλη που αποθηκεύει
     * τα ειδικά χαρακτηριστικά του δωματίου, εμφανίζει με την βοήθεια της
     * κλάσης {@link Display}την Λίστα με τα χαρακτηριστικά
     * του καταλύματος {@link Accommodations#getCharacteristics()} και την φωτογραφία
     * του καταλύματος με όνομα {@link Accommodations#getImageName()}
     *
     * @param admin Ο διαχειριστής που είναι συνδεδεμένος στην εφαρμογή
     */
    public void roomTableMouseListener(Admins admin)
    {
        HotelRoomsTable.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                int row = HotelRoomsTable.rowAtPoint(evt.getPoint());
                int col = HotelRoomsTable.columnAtPoint(evt.getPoint());
                if (row >= 0 && col == 8)
                {
                    int rowSelected = HotelRoomsTable.getSelectedRow();
                    int roomFound = admin.getAccommodations().FindRoom(Integer.parseInt(HotelRoomsTable.getValueAt(rowSelected, 6).toString()));
                    Display temp = new Display(admin.getAccommodations().getRooms().get(roomFound).getCharacteristics(),
                            admin.getAccommodations().getRooms().get(roomFound).getImageName());
                    temp.pack();
                    temp.setVisible(true);

                }
            }
        });
    }

}
