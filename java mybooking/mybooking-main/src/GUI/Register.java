package GUI;

import users.Users;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Register extends JDialog
{
    private final Users base;
    public boolean usrExists;
    private JPanel contentPane;
    private JButton submit;
    private JButton cancel;
    private JComboBox userType;
    private JTextField username;
    private JPasswordField password;
    private JTextField compName;
    private JLabel usrExistsErrMsg;
    private JComboBox gender;
    private JLabel fillCompNameErrMsg;
    private JLabel fillErrMsg;

    public Register(Users base)
    {
        setTitle("Register to MyBooking");
        this.base = base;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(submit);

        usrExists = false;

        submit.addActionListener(e -> signUp());

        cancel.addActionListener(e -> dispose());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void signUp()
    {
        String psw = new String(password.getPassword());
        fillErrMsg.setVisible(username.getText().isBlank() || psw.isBlank());
        fillCompNameErrMsg.setVisible(getUserType().equals("Provider") && compName.getText().isBlank());

        if (!username.getText().isBlank() && !psw.isBlank())
        {
            if (getUserType().equals("Provider") && compName.getText().isBlank())
                return;

            usrExists = base.Register(
                    username.getText(),
                    psw,
                    getUserType(),
                    getGender(),
                    compName.getText()
            );

            if (usrExists)
                usrExistsErrMsg.setVisible(true);
        }

        if (!usrExists &&
                !(username.getText().isBlank() || psw.isBlank()) &&
                !(getUserType().equals("Provider") && compName.getText().isBlank())
        )
        {
            dispose();
            JOptionPane.showMessageDialog(null, "You have successfully created your new account");
        }

        usrExists = false;
    }

    public String getUserType()
    {
        String typeToString = null;
        int type = userType.getSelectedIndex();

        switch (type)
        {
            case 0 -> typeToString = "Customer";
            case 1 -> typeToString = "Provider";
        }

        return typeToString;
    }

    public String getGender()
    {
        String genderToString = null;
        int type = gender.getSelectedIndex();

        switch (type)
        {
            case 0 -> genderToString = "Male";
            case 1 -> genderToString = "Female";
        }

        return genderToString;
    }
}
