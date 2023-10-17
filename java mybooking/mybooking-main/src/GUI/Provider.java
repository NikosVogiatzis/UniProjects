package GUI;

import accommodations.Accommodations;
import accommodations.HotelRooms;
import accommodations.PrivateAccommodation;
import accommodations.reservervations.Date;
import photo_characteristicsDisplay.Display;
import users.Providers;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;


public class Provider extends JDialog
{
    private JTextField characteristicsHotelAdd;
    private JTabbedPane tabbedPane1;
    private JRadioButton privateRadioButton;
    private JRadioButton hotelRoomsRadioButton;
    private JTextField roomNumberText;
    private JTextField roomFloorText;
    private JTextField roomSquareText;
    private JTextField roomPriceText;
    private JTextField roomAddressText;
    private JTextField privateAddressText;
    private JTextField typeText;
    private JTextField privateSquareText;
    private JTextField privatePriceText;
    private JButton AddRoom;
    private JButton AddPrivateAccommodation;
    private JPanel PrivateRadioButtonPanel;
    private JPanel roomRadioButtonPanel;
    private JPanel hotelRoomsPanel;
    private JPanel Panel;
    private JPanel panel1;
    private JPanel ShowAccommodations;
    private JTabbedPane tabbedPane2;
    private JScrollPane HotelRoomsPane;
    private JTable privateAccommodation;
    private JTable HotelRooms;
    private JButton LogOut;
    private JPanel welcome;
    private JPanel DeleteAccommodation;
    private JButton MyAccommodations;
    private JTextField deleteAccommodation;
    private JButton deleteAccommodationButton;
    private JButton MyAccommodation;
    private JTextField AddID;
    private JLabel HelloMsg;
    private JButton myAccommodationsButton;
    private JButton addNewAccommodationButton;
    private JButton editAnAccommodationButton;
    private JButton deleteAnAccommodationButton;
    private JButton INBOXButton;
    private JButton reservationsButton;
    private JLabel numberOfAccomm;
    private ButtonGroup G1;
    private JSeparator Seperator;
    private JLabel brandNameMSG;
    private JPanel Reservations;
    private JTabbedPane tabbedPane3;
    private JScrollPane Active;
    private JPanel hotelEdit;
    private JPanel hotelRoomsc1;
    private JPanel Privatec2;
    private JButton editButton;
    private JPanel emptyPanel;
    private JScrollPane hoteledidscroll;
    private JTextField roomSNumberTextField;
    private JTextField roomSFloorTextField;
    private JTextField squareMetresTextField;
    private JTextField priceNight€TextField;
    private JTextField capacityOfPeopleTextField;
    private JPanel RoomsPanelEditTab;
    private JPanel PrivatePanelEditTab;
    private JButton saveChangesButton1;
    private JButton saveChangesButton;
    private JTextField typeTextField;
    private JTextField editRoomCharacteristics;
    private JTextField squareMetresTextField1;
    private JTextField priceTextField;
    private JTextField specialCharacteristicsTextField;
    private JTextField capacityOfPeopleTextField1;
    private JTable ActiveReservations;
    private JTextField capacityHotels;
    private JTextField privateCapacity;
    private JTextField privateCharacteristics;
    private JLabel editHotelName;
    private JLabel editHotelAddress;
    private JLabel editCompanys;
    private JLabel editPrivateAddress;
    private JPanel privateAddPanel;
    private JScrollPane PrivateAccommodationPane;
    private JButton browseButton;
    private JButton browseButton1;
    private JTextField textField2;


    private String imagesPath = "";

    public Provider(Providers prov, Login login)
    {
        setTitle("[Provider] " + prov.getUsername());
        /*
            ActionListener για το κουμπί editButton. Αναζητά στις λίστες με τα αποθηκευμένα καταλύματα το
            ID που δόθηκε στο {@link #AddID}και ανοίγει το αντίστοιχο panel ανάλογα με το είδος του καταλύματος
            αν και εφόσον βρέθηκε. Αν δε βρεθεί ή αν έχει δοθεί λάθος το ID εμφανίζεται αντίστοιχο μήνυμα
         */
        editButton.addActionListener(e ->
        {
            int ID;
            boolean found = false;
            Scanner dump = new Scanner(AddID.getText());
            if (dump.hasNextInt())
            {
                ID = dump.nextInt();
                if (prov.getAccommodations().FindRoom(ID) != -1)
                {
                    for (HotelRooms index : prov.getAccommodations().getRooms())
                    {
                        if (ID == index.getId() && index.getHotelName().equals(prov.getProvidersBrandName()))
                        {
                            RoomsPanelEditTab.setEnabled(true);
                            RoomsPanelEditTab.setVisible(true);
                            editHotelName.setText("Hotel name: " + index.getHotelName());
                            editHotelAddress.setText("Adress: " + index.getAddress());
                            found = true;
                        }
                    }
                }
                if (prov.getAccommodations().FindAccommodation(Integer.parseInt(AddID.getText())) != -1)
                {
                    for (PrivateAccommodation index : prov.getAccommodations().getAirbnb())
                    {
                        if (ID == index.getId() && index.getCompanyName().equals(prov.getProvidersBrandName()))
                        {
                            PrivatePanelEditTab.setEnabled(true);
                            PrivatePanelEditTab.setVisible(true);
                            editCompanys.setText("Company's name: " + index.getCompanyName());
                            editPrivateAddress.setText("Adress: " + index.getAddress());
                            found = true;
                        }
                    }
                }
                if (!found)
                    JOptionPane.showMessageDialog(null, "You don't own an accommodation with ID: " + Integer.parseInt(AddID.getText()));
            } else
                JOptionPane.showMessageDialog(null, "Make sure you have given the ID correctly! ");
            AddID.setText("");

        });

        /*
            Απενεργοποίηση των JPanels στο EditAccommodation Tab
            γιατί θέλουμε να εμφανίζονται μόνο όταν βρεθεί το κατάλυμα
         */
        RoomsPanelEditTab.setEnabled(false);
        RoomsPanelEditTab.setVisible(false);
        PrivatePanelEditTab.setEnabled(false);
        PrivatePanelEditTab.setVisible(false);

        //Πρόσθεση των προσωπικών στοιχείων του Provider στο welcome Tab
        brandNameMSG.setText("Your brand name is: " + prov.getProvidersBrandName());
        HelloMsg.setText("Hello " + prov.getUsername());
        numberOfAccomm.setText("You already own: " + prov.numberOfAccommodations(prov.getProvidersBrandName()) +
                " accommodations!");


        //Δημιουργία των JTables με τα HotelRooms,PrivateAccommodation,Reservations
        createHotelRoomsTable();
        createPrivateAccommodationTable();
        createMyReservationsTable();

        //Δημιουργία ButtonGroup των 2 radioButtons στο AddAccommodation tab ώστε να ανοίγει το αντίχτοιχο panel μόνο
        G1 = new ButtonGroup();
        G1.add(hotelRoomsRadioButton);
        G1.add(privateRadioButton);

        //Συναρτηση με το Action Listener του hotelRoomRadioButton
        hotelRoomRadioButton();
        //Συναρτηση με το Action Listener του PrivateRadioButton
        PrivateRadioButton();
        //Συνάρτηση με τον ActionListener του LogOut button
        logOut(login);


        myAccommodations();
        DeleteAccommodationButton(prov);

        addRoom(prov);
        addPrivateAccommodation(prov);


        roomTableMouseListener(prov);
        privateAccommodationTableMouseListener(prov);

        /*
            -ActionListeners για τα κουμπιά που πατώντας τα θέλουμ να μεταφερθούμε
            σε διαφορετικά Tab του tabbedPane1
         */
        myAccommodationsButton.addActionListener(e -> tabbedPane1.setSelectedIndex(3));
        addNewAccommodationButton.addActionListener(e -> tabbedPane1.setSelectedIndex(1));
        editAnAccommodationButton.addActionListener(e -> tabbedPane1.setSelectedIndex(2));
        deleteAnAccommodationButton.addActionListener(e -> tabbedPane1.setSelectedIndex(4));
        INBOXButton.addActionListener(e -> JOptionPane.showMessageDialog(null, prov.getMessages()));
        reservationsButton.addActionListener(e -> tabbedPane1.setSelectedIndex(5));
        MyAccommodation.addActionListener(e -> tabbedPane1.setSelectedIndex(3));


        /*
            ActionListener για το κουμπί saveChangesButton που επιτρέπει την επεξεργασία καταλύματος τύπου HotelRooms
            Αν έχουν δωθεί σωστά οι τιμές στα αντίστοιχα πεδία τότε γίνεται επιτυχής επεξεργασία του δωματίου και
            εμφανίζεται το αντίστοιχο μήνυμα, διαφορετικά εμφανίζεται μήνυμα λάθους. Το μήνυμα αυτό εμφανίζεται κι όταν
            ο πάροχος προσπαθήσει να ορίσει ως νέο αριθμό δωματίου έναν αριθμό ο οποίος αντιστοιχεί σε άλλο δωμάτιο στο
            συγκεκριμένο ξενοδοχείο στην συγκεκριμένη διεύθυνση.Έπειτα απο την επιτυχημέμη ή αποτυχημένη επεξεργασία,
            καθαρίζονται οι τιμές απο τα πεδία και κρύβεται το panel επεξεργασίας δωματίου.
            Δημιουργείται ένα αντίγραφο του δωματίου το οποίο ο πάροχος θέλει να επεξεργαστεί και μέσω της EditRoom που βρίσκεται
            στην κλάση Providers λαμβάνει νέες τιμές. Αν γίνει επιτυχημένη επεξεργασία στην EditRoom τότε διαγράφεται το δωμάτιο
            με τις παλίες τιμές απο τη λίστα με τα δωμάτια (rooms) της κλάσης Accommodations και προσθέτεται το επεξεργασμένο δωμάτιο.
            Το ίδιο συμβαίνει και στον πίνακα HotelRooms. Αφαιρείται το παλιό δωμάτιο και στη θέση του εισάγεται το επεξεργασμένο.
         */
        saveChangesButton.addActionListener(e ->
        {
            accommodations.HotelRooms room = null;
            int ID;
            Scanner dump = new Scanner(AddID.getText());
            if (dump.hasNextInt())
            {
                ID = dump.nextInt();
                if (prov.getAccommodations().FindRoom(ID) != -1)
                {
                    for (HotelRooms index : prov.getAccommodations().getRooms())
                    {
                        if (ID == index.getId() && index.getHotelName().equals(prov.getProvidersBrandName()))
                        {
                            room = index;
                        }
                    }
                }

                assert room != null;
                HotelRooms newRoom = prov.EditRoom(roomSNumberTextField.getText(), roomSFloorTextField.getText(), squareMetresTextField.getText(),
                        priceNight€TextField.getText(), capacityOfPeopleTextField.getText(), editRoomCharacteristics.getText(), room);
                if (newRoom == null)
                {
                    JOptionPane.showMessageDialog(null, "There was an error trying to edit your room!");
                    RoomsPanelEditTab.setEnabled(false);
                    RoomsPanelEditTab.setVisible(false);
                } else
                {
                    prov.getAccommodations().getRooms().remove(room);
                    prov.getAccommodations().getRooms().add(newRoom);

                    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hotelRooms.bin")))
                    {
                        out.writeObject(prov.getAccommodations().getRooms());
                    } catch (IOException err)
                    {
                        err.printStackTrace();
                    }
                    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("accommodationsIdentifier.bin")))
                    {
                        out.writeObject(prov.getAccommodations().identifierManager());
                    } catch (IOException err)
                    {
                        err.printStackTrace();
                    }

                    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("imageIdentifier.bin")))
                    {
                        out.writeObject(prov.getAccommodations().getImageIdentifier());
                    } catch (IOException err)
                    {
                        err.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(null, "You have successfully edited your Hotel Room!");
                    int row = -1;
                    for (int i = 0; i < HotelRooms.getModel().getRowCount(); i++)
                    {
                        if (HotelRooms.getModel().getValueAt(i, 6).equals("" + AddID.getText()))
                        {
                            row = i;
                            break;
                        }
                    }
                    if (row != -1)
                    {
                        ((DefaultTableModel) HotelRooms.getModel()).removeRow(row);
                        ((DefaultTableModel) HotelRooms.getModel()).addRow(new Object[]{
                                "\uD83D\uDCBC " + newRoom.getHotelName(),
                                "\uD83D\uDCCD " + newRoom.getAddress(),
                                newRoom.getRoomNumber(),
                                newRoom.getFloor(),
                                newRoom.getSquareMetres() + "m²",
                                newRoom.getPrice() + "€",
                                "" + newRoom.getId(),
                                newRoom.getCapacity(),
                                "Click here!"

                        });
                    }

                }
            } else
                JOptionPane.showMessageDialog(null,
                        "You don't own an accommodation with ID: " + AddID.getText());
            roomSNumberTextField.setText("");
            roomSFloorTextField.setText("");
            squareMetresTextField.setText("");
            priceNight€TextField.setText("");
            capacityOfPeopleTextField.setText("");
            editRoomCharacteristics.setText("");
            RoomsPanelEditTab.setEnabled(false);
            RoomsPanelEditTab.setVisible(false);
            AddID.setText("");
        });

        /*
            ActionListener για το κουμπί saveChangesButton1 που επιτρέπει την επεξεργασία καταλύματος τύπου PrivateAccommodations
            Αν έχουν δωθεί σωστά οι τιμές στα αντίστοιχα πεδία τότε γίνεται επιτυχής επεξεργασία του καταλύματος και
            εμφανίζεται το αντίστοιχο μήνυμα, διαφορετικά εμφανίζεται μήνυμα λάθους.
            Έπειτα απο την επιτυχημέμη ή αποτυχημένη επεξεργασία,
            καθαρίζονται οι τιμές απο τα πεδία και κρύβεται το panel επεξεργασίας ιδιωτικού καταλύματος.
            Δημιουργείται ένα αντίγραφο του καταλύματος το οποίο ο πάροχος θέλει να επεξεργαστεί και μέσω της EditAccommodation
            που βρίσκεται στην κλάση Providers λαμβάνει νέες τιμές. Αν γίνει επιτυχημένη επεξεργασία στην EditAccommodation
            τότε διαγράφεται το κατάλυμα με τις παλίες τιμές απο τη λίστα με τα ιδιωτικά καταλύματα (airbnb)
            της κλάσης Accommodations και προσθέτεται το επεξεργασμένο ιδιωτικό κατάλυμα.
            Το ίδιο συμβαίνει και στον πίνακα privateAccommodation.
            Αφαιρείται το παλιό κατάλυμα και στη θέση του εισάγεται το επεξεργασμένο
         */
        saveChangesButton1.addActionListener(e ->
        {
            Scanner dump = new Scanner(AddID.getText());
            int ID;
            System.out.println(AddID.getText());
            PrivateAccommodation accommodation = null;
            System.out.println(dump.hasNextInt());
            if (dump.hasNextInt())
            {
                ID = dump.nextInt();

                if (prov.getAccommodations().FindAccommodation(ID) != -1)
                {
                    for (PrivateAccommodation index : prov.getAccommodations().getAirbnb())
                    {

                        if (ID == index.getId() && index.getCompanyName().equals(prov.getProvidersBrandName()))
                        {
                            accommodation = index;
                            PrivatePanelEditTab.setEnabled(true);
                            PrivatePanelEditTab.setVisible(true);
                        }
                    }

                }

                assert accommodation != null;
                PrivateAccommodation newAccommodation = prov.EditAccommodation(typeTextField.getText(),
                        squareMetresTextField1.getText(), priceTextField.getText(), capacityOfPeopleTextField1.getText(),
                        specialCharacteristicsTextField.getText(), accommodation);
                if (newAccommodation == null)
                    JOptionPane.showMessageDialog(null, "There was an error trying to edit your Accommodation \n" +
                            "Move sensor to the fields to watch the proper insertion!");
                else
                {
                    int row = -1;
                    prov.getAccommodations().getAirbnb().remove(accommodation);
                    prov.getAccommodations().getAirbnb().add(newAccommodation);


                    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("privateAccommodation.bin")))
                    {
                        out.writeObject(prov.getAccommodations().getAirbnb());
                    } catch (IOException err)
                    {
                        err.printStackTrace();
                    }
                    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("accommodationsIdentifier.bin")))
                    {
                        out.writeObject(prov.getAccommodations().identifierManager());
                    } catch (IOException err)
                    {
                        err.printStackTrace();
                    }

                    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("imageIdentifier.bin")))
                    {
                        out.writeObject(prov.getAccommodations().getImageIdentifier());
                    } catch (IOException err)
                    {
                        err.printStackTrace();
                    }
                    for (int i = 0; i < privateAccommodation.getModel().getRowCount(); i++)
                    {
                        if (privateAccommodation.getModel().getValueAt(i, 7).equals(AddID.getText())) {
                            row = i;
                            break;
                        }
                    }
                    if (row != -1)
                    {
                        ((DefaultTableModel) privateAccommodation.getModel()).removeRow(row);

                        ((DefaultTableModel) privateAccommodation.getModel()).addRow(new Object[]{
                                "\uD83D\uDCBC " + newAccommodation.getCompanyName(),
                                "Type: " + newAccommodation.getType(),
                                "\uD83D\uDCCD " + newAccommodation.getAddress(),
                                newAccommodation.getSquareMetres() + "m²",
                                newAccommodation.getPrice() + "€",
                                newAccommodation.getCapacity(),
                                "Click here!",
                                "" + newAccommodation.getId()
                        });

                    }
                    JOptionPane.showMessageDialog(null, "You have successfully edited your private accommodation!");

                }
            } else
                JOptionPane.showMessageDialog(null,
                        "You don't own an accommodation with ID: " + AddID.getText());

            typeTextField.setText("");
            squareMetresTextField1.setText("");
            priceTextField.setText("");
            editRoomCharacteristics.setText("");
            PrivatePanelEditTab.setEnabled(false);
            PrivatePanelEditTab.setVisible(false);
            AddID.setText("");
        });

        /*
            Κρύβονται τα panel παράθυρο επεξεργασίας καταλύματος ώστε να είναι ορατά μόνο
            κατά το πάτημα του αντίστοιχου JRadioButton
        */
        roomRadioButtonPanel.setVisible(false);
        PrivateRadioButtonPanel.setVisible(false);
        setContentPane(Panel);
        setModal(true);

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
        Panel.registerKeyboardAction(e -> System.exit(0), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        tabbedPane1.setEnabled(true);
        myAccommodationsButton.setEnabled(true);
        addNewAccommodationButton.setEnabled(true);
        editAnAccommodationButton.setEnabled(true);
        deleteAnAccommodationButton.setEnabled(true);
        reservationsButton.setEnabled(true);
        INBOXButton.setEnabled(true);

        //Αν ο πάροχος δεν έχει εγκριθεί από κάποιον διαχειριστή Τότε ακυρώνεται το login
        if (!prov.accountStatus()) {
            abortLogging();
        }

        //ACTION LISTENER GIA UPLOAD EIKONAS STA DWMATIA KSENODOXEIWN
        browseButton.addActionListener(e -> {
            if (e.getSource() == browseButton) {

                JFileChooser fileChooser = new JFileChooser();

                fileChooser.setCurrentDirectory(new File(".")); //sets current directory
                fileChooser.addChoosableFileFilter(new ImageFilter());
                fileChooser.setAcceptAllFileFilterUsed(false);
                int response = fileChooser.showOpenDialog(null); //select file to open
                //int response = fileChooser.showSaveDialog(null); //select file to save

                if (response == JFileChooser.APPROVE_OPTION) {
                    imagesPath = fileChooser.getSelectedFile().getAbsolutePath();
                } else
                    imagesPath = "";

            }
        });

        //ACTION LISTENER GIA UPLOAD EIKONAS STA DWMATIA KSENODOXEIWN
        browseButton1.addActionListener(e -> {
            if (e.getSource() == browseButton1) {

                JFileChooser fileChooser1 = new JFileChooser();

                fileChooser1.setCurrentDirectory(new File(".")); //sets current directory
                fileChooser1.addChoosableFileFilter(new ImageFilter());
                fileChooser1.setAcceptAllFileFilterUsed(false);
                int response = fileChooser1.showOpenDialog(null); //select file to open
                //int response = fileChooser.showSaveDialog(null); //select file to save

                if (response == JFileChooser.APPROVE_OPTION) {
                    imagesPath = fileChooser1.getSelectedFile().getAbsolutePath();
                } else
                    imagesPath = "";

            }
        });
    }

    public void abortLogging()
    {
        JOptionPane.showMessageDialog(null,
                """
                        Your account is currently inactive.\s
                        Please contact the administrators at\s
                        ochotzas@csd.auth.gr or xxxx@csd.auth.gr\s
                        if you have any more queries.\s
                        """);

        tabbedPane1.setEnabled(false);
        myAccommodationsButton.setEnabled(false);
        addNewAccommodationButton.setEnabled(false);
        editAnAccommodationButton.setEnabled(false);
        deleteAnAccommodationButton.setEnabled(false);
        reservationsButton.setEnabled(false);
        INBOXButton.setEnabled(false);
    }


    /**
     * Η μέθοδος αυτή δημιουργεί τον πίνακα με τα δωμάτια που έχει στην κατοχή του ο πάροχος
     * στο tab {@link #ShowAccommodations}
     */
    public void createHotelRoomsTable()
    {
        Object[][] data = {};
        HotelRooms.setModel(new DefaultTableModel(
                data,
                new Object[]{"Hotel name", "Location", "Room's number", "Floor",
                        "Square metres", "Price", "ID", "Capacity", "Characteristics"}

        ));
    }

    /**
     * Η μέθοδος αυτή προσθέτει τις ενεργές κρατήσεις για τα καταλύματα του παρόχου
     * στον αντίστοιχο πίνακα του tab {@link #Reservations}
     */
    public void addMyReservations(HashMap<PrivateAccommodation, ArrayList<Date>> allReservationsPrivate,
                                  HashMap<HotelRooms, ArrayList<Date>> allReservationsRooms)
    {
        DefaultTableModel model = (DefaultTableModel) ActiveReservations.getModel();

        if (allReservationsPrivate != null)
        {
            allReservationsPrivate.forEach((key, value) ->
            {
                for (Date date : value)
                {
                    model.addRow(new Object[]{
                            "\uD83D\uDCBC " + key.getCompanyName() + "s",
                            key.getType(),
                            "\uD83D\uDCCD " + key.getAddress(),
                            "for " + key.getPrice() + "€",
                            "ID: " + key.getId(),
                            "\uD83D\uDCC6 " + date.toString()
                    });
                }
            });
        }

        if (allReservationsRooms != null)
        {
            allReservationsRooms.forEach((key, value) ->
            {
                for (Date date : value)
                {
                    model.addRow(new Object[]{
                            "\uD83D\uDCBC " + key.getHotelName() + "'s",
                            "Room: " + key.getRoomNumber(),
                            "\uD83D\uDCCD " + key.getAddress(),
                            "for " + key.getPrice() + "€",
                            "ID: " + key.getId(),
                            "\uD83D\uDCC6 " + date.toString()
                    });
                }
            });
        }

    }

    /**
     * Η μέθοδος αυτή προσθέτει τα δωμάτια ξενοδοχείων που έχει
     * στην κατοχή του ο πάροχος στον αντίστοιχο πίνακα του tab
     * {@link #HotelRoomsPane}
     */
    public void addHotelRoomsToTable(Providers provider)
    {
        DefaultTableModel model = (DefaultTableModel) HotelRooms.getModel();
        if (provider.getAccommodations().getRooms() != null)
        {
            for (HotelRooms room : provider.getAccommodations().getRooms())
            {

                if (room.getHotelName().equals(provider.getProvidersBrandName()))
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
        }


    }

    /**
     * Η μέθοδος αυτή δημιουργεί τον πίνακα με τις ενεργές κρατήσεις για τα καταλύματα
     * του παρόχου στο tab {@link #Reservations}
     */
    private void createMyReservationsTable()
    {
        Object[][] data = {};

        ActiveReservations.setModel(new DefaultTableModel(
                data,
                new Object[]{"Company's name", "Type/Room Number", "Address",
                        "Price", "Accommodation's ID", "Reserved Dates"}
        ));
    }

    /**
     * Η μέθοδος αυτή δημιουργεί τον πίνακα με τα ιδιωτικά καταλύματα
     * που έχει στην κατοχή του ο πάροχος στο tab {@link #ShowAccommodations}
     */
    public void createPrivateAccommodationTable()
    {
        Object[][] data = {};

        privateAccommodation.setModel(new DefaultTableModel(
                data,
                new Object[]{"Company's name", "Type", "Location", "Square metres", "price", "Capacity", "Characteristics", "ID"}
        ));
    }

    /**
     * Η μέθοδος αυτή προσθέτει τα ιδιωτικά καταλύματα που έχει στην κατοχή του ο πάροχος
     * στον αντίστοιχο πίνακα στο tab {@link #PrivateAccommodationPane}.
     */
    public void addPrivateAccommodationsToTable(Providers provider)
    {

        DefaultTableModel model = (DefaultTableModel) privateAccommodation.getModel();

        if (provider.getAccommodations().getAirbnb() != null)
        {
            for (PrivateAccommodation accomm : provider.getAccommodations().getAirbnb())
            {
                if (provider.getProvidersBrandName().equals(accomm.getCompanyName()))
                {
                    model.addRow(new Object[]{
                            "\uD83D\uDCBC " + accomm.getCompanyName(),
                            "Type: " + accomm.getType(),
                            "\uD83D\uDCCD " + accomm.getAddress(),
                            accomm.getSquareMetres() + "m²",
                            accomm.getPrice() + "€",
                            accomm.getCapacity(),
                            "Click here!",
                            "" + accomm.getId()
                    });
                }
            }
        }
    }

    /**
     * Στη μέθοδο αυτή υλοποιείται ο {@link ActionListener} για το κουμπί {@link #LogOut}
     * όπου κατά το πάτημα του αποσυνδέεται ο πάροχος από την εφαρμογή
     */
    public void logOut(Login login)
    {
        LogOut.addActionListener(e ->
        {
            dispose();
            login.dispose();
        });
    }

    /**
     * Στη μέθοδο αυτή υλοποιείται ο {@link ActionListener} του {@link #hotelRoomsRadioButton}.
     * Κατα το πάτημα του εμφανίζεται το πάνελ {@link #roomRadioButtonPanel}
     * των δωματίων και κρύβεται το panel των ιδιωτικών καταλυμάτων {@link #PrivateRadioButtonPanel}
     */
    public void hotelRoomRadioButton()
    {
        hotelRoomsRadioButton.addActionListener(e ->
        {
            hotelRoomsPanel.setVisible(true);
            roomRadioButtonPanel.setVisible(true);
            roomRadioButtonPanel.setVisible(true);
            PrivateRadioButtonPanel.setEnabled(false);
            PrivateRadioButtonPanel.setVisible(false);

        });
    }


    /**
     * Στη μέθοδο αυτή υλοποιείται ο {@link  ActionListener} του {@link #privateRadioButton}.
     * Κατα το πάτημα του εμφανίζεται το panel των ιδιωτικών καταλυμάτων {@link #PrivateRadioButtonPanel} και
     * κρύβεται το panel των δωματίων {@link #hotelRoomsRadioButton}.
     */
    public void PrivateRadioButton()
    {
        privateRadioButton.addActionListener(e ->
        {
            PrivateRadioButtonPanel.setVisible(true);
            hotelRoomsPanel.setVisible(false);
            roomRadioButtonPanel.setEnabled(false);
            roomRadioButtonPanel.setVisible(false);
        });
    }

    /**
     * {@link ActionListener} για το κουμπί {@link #MyAccommodations}.
     * Κατά το πάτημα του ανοίγει το tab {@link #ShowAccommodations}
     */
    public void myAccommodations()
    {
        MyAccommodations.addActionListener(e -> tabbedPane1.setSelectedIndex(3));
    }

    /**
     * Η μέθοδος αυτή υλοποιεί τον {@link ActionListener} του {@link #deleteAccommodationButton}
     * με βάση το ID που δόθηκε στο πεδίο {@link #deleteAccommodation}. Καλείται η συνάρτηση
     * {@link Providers#DeleteAccommodation(String)} όπου αν το κατάλυμα με το ID που δόθηκε ανήκει στον συγκεκριμένο
     * πάροχο, διαγράφεται από την αντίστοιχη λίστα {@link Accommodations#getAirbnb()}.
     * Αν έχει διαγραφεί επιτυχώς τότε ψάχνουμε στον αντίστοιχο
     * πίνακα την γραμμή όπου εμφανίζεται το κατάλυμα με το δοθέν ID και την αφαιρούμε.
     */
    public void DeleteAccommodationButton(Providers prov)
    {
        deleteAccommodationButton.addActionListener(e ->
        {
            //Αν το textField είναι κενό τότε τερματίζεται η διαδικασία
            if (deleteAccommodation.getText().equals(""))
            {
                JOptionPane.showMessageDialog(null, "Make sure you've given the ID");
                return;
            }
            Scanner dump = new Scanner(deleteAccommodation.getText());

            //Αν το textField δεν είναι κενό και είναι συμπληρωμένο με ακέραιους τότε
            //κάνει κανονικά την διαδικασία διαγραφής του καταλύματος

            if (dump.hasNextInt())
            {
                int ID = dump.nextInt();
                if (prov.DeleteAccommodation(deleteAccommodation.getText()))
                {
                    JOptionPane.showMessageDialog(null, "Your accommodation was deleted successfully!");

                    numberOfAccomm.setText("You already own: "
                            + prov.numberOfAccommodations(prov.getProvidersBrandName())
                            + " accommodations!");
                    int row = -1;
                    DefaultTableModel model = (DefaultTableModel) HotelRooms.getModel();
                    for (int i = 0; i < model.getRowCount(); i++)
                    {
                        if (model.getValueAt(i, 6).equals("" + deleteAccommodation.getText()))
                        {
                            row = i;
                            break;
                        }
                    }
                    if (row != -1)
                        model.removeRow(row);//remove row

                    row = -1;

                    DefaultTableModel model1 = (DefaultTableModel) privateAccommodation.getModel();
                    for (int i = 0; i < model1.getRowCount(); i++)
                    {
                        if (model1.getValueAt(i, 5).equals("" + deleteAccommodation.getText())) {
                            row = i;
                            break;
                        }
                    }
                    if (row != -1)
                        model1.removeRow(row);//remove row
                } else
                    JOptionPane.showMessageDialog(null, "You don't own an accommodation with ID:" + ID);
            } else //Αν έχει συμβολοσειρές στην είσοδο του το textfield.
                JOptionPane.showMessageDialog(null, "Make sure you have given the ID correctly!");
            deleteAccommodation.setText("");
        });

    }


    /**
     * Η μέθοδος αυτή υλοποιεί τον {@link  ActionListener} για το κουμπί
     * {@link #AddRoom} . Καλείται η συνάρτηση {@link Providers#AddHotelRoom(String, String, String, String, String, String, List, String)}
     * και η τιμή που επιστρέφει εισάγεται στη μεταβλητή room τύπου {@link HotelRooms}.
     * Αν η τιμή αυτή είναι διάφορη του null
     * δηλαδή είναι εφικτή η πρόσθεση δωματίου, τότε το δωμάτιο προστίθεται λίστα {@link Accommodations#getRooms()}
     * και στον πίνακα {@link #HotelRooms}
     */
    public void addRoom(Providers prov)
    {
        AddRoom.addActionListener(e ->
        {

            List<String> characteristics;
            String[] temp = characteristicsHotelAdd.getText().split("/");
            characteristics = Arrays.asList(temp);
            HotelRooms room = prov.AddHotelRoom(roomNumberText.getText(), roomFloorText.getText(), roomSquareText.getText(),
                    roomPriceText.getText(), roomAddressText.getText(), capacityHotels.getText(), characteristics, imagesPath);
            if (room != null)
            {
                prov.getAccommodations().getRooms().add(room);

                //Εγγραφές στα αρχεία: δωμάτιο/αναγνωριστικός αριθμός δωματίου/αναγνωριστικός αριθμός ονόματος φωτογραφίας
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("hotelRooms.bin")))
                {
                    out.writeObject(prov.getAccommodations().getRooms());
                } catch (IOException err)
                {
                    err.printStackTrace();
                }
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("accommodationsIdentifier.bin")))
                {
                    out.writeObject(prov.getAccommodations().identifierManager());
                } catch (IOException err)
                {
                    err.printStackTrace();
                }

                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("imageIdentifier.bin")))
                {
                    out.writeObject(prov.getAccommodations().getImageIdentifier());
                } catch (IOException err)
                {
                    err.printStackTrace();
                }
                DefaultTableModel model = (DefaultTableModel) HotelRooms.getModel();
                model.addRow(new Object[]{
                        "\uD83D\uDCBC " + room.getHotelName(),
                        "\uD83D\uDCCD " + room.getAddress(),
                        room.getRoomNumber(),
                        room.getFloor(),
                        room.getSquareMetres() + "m²",
                        room.getPrice() + "€",
                        room.getId(),
                        room.getCapacity(),
                        "Click here!"
                });


                JOptionPane.showMessageDialog(null, "Your room has been added!");
                numberOfAccomm.setText("You already have :"
                        + prov.numberOfAccommodations(prov.getProvidersBrandName())
                        + " accommodations! ");

            } else
                JOptionPane.showMessageDialog(null, "There was a problem trying to add your room!");
            //TODO: πρέπει να μπούνε έλεγχοι σχετικά με την εγκυρότητα του τύπου του textField.
            roomFloorText.setText("");
            roomNumberText.setText("");
            capacityHotels.setText("");
            roomAddressText.setText("");
            roomPriceText.setText("");
            roomSquareText.setText("");
            characteristicsHotelAdd.setText("");
            roomRadioButtonPanel.setEnabled(false);
            roomRadioButtonPanel.setVisible(false);
        });

    }


    /**
     * Η μέθοδος αυτή υλοποιεί τον {@link ActionListener} για το κουμπί {@link #AddPrivateAccommodation}.
     * Καλείται η συνάρτηση {@link Providers#AddPrivateAccommodation(String, String, String, String, String, List, String)}
     * και η τιμή που επιστρέφει εισάγεται στη μεταβλητή accommodation τύπου {@link PrivateAccommodation}.
     * Αν η τιμή αυτή είναι διάφορη του null
     * δηλαδή είναι εφικτή η πρόσθεση ιδιωτικού καταλύματος
     * , τότε το κατάλυμα προστίθεται λίστα {@link Accommodations#getAirbnb()}
     * και στον πίνακα {@link #privateAccommodation}
     */
    public void addPrivateAccommodation(Providers prov)
    {
        AddPrivateAccommodation.addActionListener(e ->
        {
            List<String> characteristics;
            String[] temp = privateCharacteristics.getText().split("/");
            characteristics = Arrays.asList(temp);
            PrivateAccommodation accommodation = prov.AddPrivateAccommodation(privateAddressText.getText(),
                    typeText.getText(), privateSquareText.getText(),
                    privatePriceText.getText(), privateCapacity.getText(), characteristics, imagesPath);
            if (accommodation != null)
            {

                prov.getAccommodations().getAirbnb().add(accommodation);

                //Εγγραφές στα αρχεία: ιδιωτικό κατάλυμα/αναγνωριστικός αριθμός καταλύματος
                //αναγνωριστικός αριθμός ονόματος φωτογραφίας
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("privateAccommodation.bin")))
                {
                    out.writeObject(prov.getAccommodations().getAirbnb());
                } catch (IOException err)
                {
                    err.printStackTrace();
                }
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("accommodationsIdentifier.bin")))
                {
                    out.writeObject(prov.getAccommodations().identifierManager());
                } catch (IOException err)
                {
                    err.printStackTrace();
                }

                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("imageIdentifier.bin")))
                {
                    out.writeObject(prov.getAccommodations().getImageIdentifier());
                } catch (IOException err)
                {
                    err.printStackTrace();
                }
                DefaultTableModel model = (DefaultTableModel) privateAccommodation.getModel();

                model.addRow(new Object[]{
                        "\uD83D\uDCBC " + accommodation.getCompanyName(),
                        "Type: " + accommodation.getType(),
                        "\uD83D\uDCCD " + accommodation.getAddress(),
                        accommodation.getSquareMetres() + "m²",
                        accommodation.getPrice() + "€",
                        accommodation.getCapacity(),
                        "Click here!",
                        accommodation.getId()
                });

                JOptionPane.showMessageDialog(null, "Your accommodation has been added!");
                numberOfAccomm.setText("You already have :"
                        + prov.numberOfAccommodations(prov.getProvidersBrandName())
                        + " accommodations! ");
            } else
                JOptionPane.showMessageDialog(null, "There was a problem trying to add your accommodation!");
            privateAddressText.setText("");
            typeText.setText("");
            privateSquareText.setText("");
            privatePriceText.setText("");
            privateCapacity.setText("");
            privateCharacteristics.setText("");
            PrivateRadioButtonPanel.setVisible(false);
            PrivateRadioButtonPanel.setEnabled(false);
        });
    }

    /**
     * Η μέθοδος αυτή υλοποιεί {@link MouseListener} για τον πίνακα
     * {@link #privateAccommodation}.Στο κλικ του ποντικού στην στήλη που αποθηκεύει
     * τα ειδικά χαρακτηριστικά του ιδιωτικού καταλύματος, εμφανίζει με την βοήθεια της
     * κλάσης {@link Display}την Λίστα με τα χαρακτηριστικά
     * του καταλύματος {@link Accommodations#getCharacteristics()} και την φωτογραφία
     * του καταλύματος με όνομα {@link Accommodations#getImageName()}
     *
     * @param provider Ο πάροχος του οποίου το κατάλυμα θα χρησιμοποιηθεί για την εμφάνιση των χαρακτηριστικών του
     */
    public void privateAccommodationTableMouseListener(Providers provider)
    {
        privateAccommodation.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                int row = privateAccommodation.rowAtPoint(evt.getPoint());
                int col = privateAccommodation.columnAtPoint(evt.getPoint());
                if (row >= 0 && col == 6)
                {
                    Display temp = new Display(provider.getAccommodations().getProvidersPrivateAccommodations(provider).get(privateAccommodation.getSelectedRow()).getCharacteristics(),
                            provider.getAccommodations().getProvidersPrivateAccommodations(provider).get(privateAccommodation.getSelectedRow()).getImageName()
                    );
                    temp.pack();
                    temp.setVisible(true);
                }
            }
        });
    }

    /**
     * Η μέθοδος αυτή υλοποιεί {@link MouseListener} για τον πίνακα
     * {@link #HotelRooms}.Στο κλικ του ποντικού στην στήλη που αποθηκεύει
     * τα ειδικά χαρακτηριστικά του δωματίου, εμφανίζει με την βοήθεια της
     * κλάσης {@link Display}την Λίστα με τα χαρακτηριστικά
     * του καταλύματος {@link Accommodations#getCharacteristics()} και την φωτογραφία
     * του καταλύματος με όνομα {@link Accommodations#getImageName()}
     *
     * @param provider Ο πάροχος του οποίου το κατάλυμα θα χρησιμοποιηθεί για την εμφάνιση των χαρακτηριστικών του
     */
    public void roomTableMouseListener(Providers provider)
    {
        HotelRooms.addMouseListener(new java.awt.event.MouseAdapter()
        {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                int row = HotelRooms.rowAtPoint(evt.getPoint());
                int col = HotelRooms.columnAtPoint(evt.getPoint());
                if (row >= 0 && col == 8)
                {
                    Display temp = new Display(provider.getAccommodations().getProvidersRooms(provider).get(HotelRooms.getSelectedRow()).getCharacteristics(),
                            provider.getAccommodations().getProvidersRooms(provider).get(HotelRooms.getSelectedRow()).getImageName()
                    );
                    temp.pack();
                    temp.setVisible(true);
                }
            }
        });
    }
}

