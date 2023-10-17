package photo_characteristicsDisplay;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Η κλάση αυτή χρησιμοποιείται ως βοηθητική κλάση για την εμφάνιση των χαρακτηριστικών
 * και των φωτογραφιών των καταλυμάτων. Καλείται από τους MouseListeners των πινάκων που αφορούν τα καταλύματα
 * και ανοίγει ένα νέο frame στο οποίο εμφανίζονται στο πάνω μέρος τα χαρακτηριστικά και στο κάτω μέρος η φωτογραφία
 * του καταλύματος
 */
public class Display extends JDialog {
    private JPanel PANEL;
    private JPanel characteristicsPanel;
    private JPanel photosPanel;
    private JLabel label;
    private JButton Close;
    private JTable CharacteristicsTable;

    public Display(List<String> characteristics, String imageName) {
        setTitle("[Picture] " + imageName);
        setContentPane(PANEL);
        setModal(true);


        characteristicsPanel.setVisible(true);
        characteristicsPanel.setEnabled(true);
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        //Δημιουργία πίνακα που θα περιέχει τα χαρακτηριστικά του καταλύματος
        Object[][] model = {};
        CharacteristicsTable.setModel(new DefaultTableModel(
                model,
                new Object[]{"Characteristics"}
        ));

        //Εισαγωγή των χαρακτηριστικών του καταλύματος στον πίνακα
        DefaultTableModel model1 = (DefaultTableModel) CharacteristicsTable.getModel();
        for (String characteristic : characteristics) {
            model1.addRow(new Object[]{characteristic});
        }

        characteristicsPanel.setVisible(true);
        try {
            //φορτώνει απο το uploads directory την φωτογραφία με όνομα imageName
            ImageIcon imageIcon = new ImageIcon("uploads/" + imageName);
            Image image = imageIcon.getImage();
            //Resize της φωτογραφίας
            Image newimg = image.getScaledInstance(500, 300, java.awt.Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(newimg);
            label.setIcon(imageIcon);

        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        Close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}
