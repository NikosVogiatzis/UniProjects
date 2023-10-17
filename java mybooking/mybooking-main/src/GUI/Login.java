package GUI;

import users.Users;

import javax.swing.*;
import java.awt.event.*;

public class Login extends JDialog
{
    private JPanel panel;
    private JButton signIn;
    private JButton signUp;
    private JTextField username;
    private JPasswordField password;
    private JComboBox userType;
    private JLabel wrongCredentialsMsg;
    private Users base;

    public Login()
    {
        setTitle("Login to MyBooking");
        setContentPane(panel);
        setModal(true);
        getRootPane().setDefaultButton(signIn);

        signIn.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                dispose();
            }
        });

        signUp.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                register();
            }
        });

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
        panel.registerKeyboardAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
                ;
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public void setBase(Users base)
    {
        this.base = base;
    }

    public String getUsername()
    {
        return username.getText();
    }

    public String getPassword()
    {
        return new String(password.getPassword());
    }

    public String getUserType()
    {
        String typeToString = null;
        int type = userType.getSelectedIndex();

        switch (type)
        {
            case 0:
                typeToString = "Customer";
                break;
            case 1:
                typeToString = "Provider";
                break;
            case 2:
                typeToString = "Admin";
                break;
        }

        return typeToString;
    }

    public void setVisibleWrongCredentialsMsg()
    {
        wrongCredentialsMsg.setVisible(true);
    }

    private void register()
    {
        Register dialog = new Register(base);
        dialog.pack();
        dialog.setVisible(true);
    }
}
