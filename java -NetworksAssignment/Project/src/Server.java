import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    //Η πόρτα του Server
    public static int PORT;

    //Ακέραια μεταβλητή η οποία αυξάνεται κατά ένα κάθε φορά που κάποιος νέος χρήστης δημιουργεί account
    public static int authToken_generator = 1000;

    //Ακέραια μεταβλητή η οποία αυξάνεται κατά ένα κάθε φορά που κάποιος χρήστης στέλνει μήνυμα
    public static int message_id_generator = 50;
    //Λίστα με τους λογαριασμούς όλων των χρηστών
    public static List<Account> accounts = new ArrayList<>();

    /**
     * Η μέθοδος αυτή επιτρέπει στον Client να δημιουργήσει νέο λογαριασμό. Εάν το username που έχει
     * βάλει ακολουθεί το πρότυπο (αλφαριθμητικα και "-") τότε γίνεται δεκτός λογαριασμός. Δημιουργείται λογαριασμός
     * μέσω της {@link Account} και το αντικείμενο προστίθεται στη λίστα {@link #accounts}. Για τη δημιουργία,
     * δημιουργείται το authToken_generator το οποίο παραχωρείται στον Client και αυξάνεται κατά ένα.
     * <p>Εαν η εγγραφή είναι επιτυχής επιστρέφεται "OK"  </p>
     * <p>εαν υπάρχει ήδη ο χρήστης "Sorry the user already exists" </p>
     * <p>εαν δόθηκε λάθος username "Invalid Username" </p>
     *
     * @param inputs Τα String εισόδου απο το terminal του Client
     * @param out    η απάντηση στον Client
     */
    public static synchronized void createAccount(String[] inputs, PrintWriter out) {
        if (!inputs[3].matches("^[a-zA-Z0-9-]*$"))
            out.println("Invalid Username");
        else {
            Account newClient = new Account(inputs[3], authToken_generator);

            boolean exists = false;
            if (accounts.size() > 0) {
                for (Account account : accounts) {
                    if (inputs[3].equals(account.username)) {
                        exists = true;
                        break;
                    }
                }
            }
            if (exists)
                out.println("Sorry, the user already exists");
            else {
                out.println(authToken_generator);
                accounts.add(newClient);
                authToken_generator++;
            }
        }
    }

    /**
     * Η μέθοδος αυτή επιτρέπει στον πελάτη να δει όλους τους "εγγεγραμμένους" χρήστες.
     * Εξετάζει αν το authToken που δόθηκε στο terminal του Client αντιστοιχεί σε υπάρχων λογαριασμό.
     * <p>Αν ναι τότε εξετάζει τη λίστα με τους λογαριασμούς και χτίζει ένα String με τα ονόματα όλων των χρηστών
     * * διαχωρισμένα με κόμμα. </p>
     * <p>Αν δοθεί λάθος authToken τότε επιστρέφεται το μήνυμα "Invalid Auth Token" </p>
     *
     * @param inputs Τα String εισόδου απο το terminal του Client
     * @param out    η απάντηση στον Client
     */
    public static synchronized void showClients(String[] inputs, PrintWriter out) {
        boolean exists = false;
        for (Account account : accounts) {
            if (Integer.parseInt(inputs[3]) == account.authToken)
                exists = true;
        }
        StringBuilder responseToClient = new StringBuilder();
        if (exists) {
            if (accounts.size() > 0) {
                for (int i = 0; i < accounts.size(); i++) {
                    if (i == 0)
                        responseToClient.append(accounts.get(i).username);
                    else
                        responseToClient.append(",").append(accounts.get(i).username);
                }
            }
            out.println(responseToClient);
        } else
            out.println("Invalid Auth Token");
    }

    /**
     * Μέθοδος που επιτρέπει στον χρήστη να στείλει μηνύματα.
     * Αρχικά γίνεται αναζήτηση στη λίστα των λογαριασμών ώστε να ελεγχθεί αν δόθηκε ορθό
     * authToken από τον χρήστη -ο αποστολέας υπάρχει -.Αν ναι τότε γίνεται αναζήτηση στη λίστα λογαριασμών
     * για να ελεγχθεί αν ο παραλήπτης που δόθηκε απο τον Client είναι ορθός, διαφορετικά επιστρέφεται στον Client η απάντηση
     * "Invalid Auth Token". Αφού γίνει η αναζήτηση και βρεθεί ο παραλήπτης, τότε δημιουργείται νέο αντικείμενο τύπου
     * {@link Message} και προστίθεται στη λίστα μηνυμάτων {@link Server.Account#messageBox} (με αρχικοποιημένο idRead = false; ). Στην επιτυχή αποστολή
     * μηνύματος επιστρέφεται στον Client το μήνυμα "OK". Αν ο χρήστης προσπαθήσει να αποστείλει μήνυμα στον εαυτό του
     * τότε δέχεται ως απάντηση "You can't send a message to your own account!"
     *
     * @param inputs Τα String εισόδου απο το terminal του Client
     * @param out    η απάντηση στον Client
     */
    public static synchronized void sendMessage(String[] inputs, PrintWriter out) {
        String sender = "";
        String receiver = "";


        for (Account account : accounts) {
            if (Integer.parseInt(inputs[3]) == account.authToken) {
                sender = account.username;
            }
        }

        if (!sender.equals("")) {
            boolean exists = false;
            if (accounts.size() > 0) {

                for (Account account : accounts) {
                    if (inputs[4].equals(account.username)) {
                        receiver = account.username;
                        exists = true;
                        if (!sender.equals(receiver)) {
                            String body = inputs[5];
                            Message newMessage = new Message(message_id_generator, false, sender, receiver, body);
                            account.messageBox.add(newMessage);
                            message_id_generator++;
                            out.println("OK");
                        } else
                            out.println("You can't send a message to your own account!");
                    }
                }
            }
            if (!exists)
                out.println("User does not exist");
        } else
            out.println("Invalid Auth Token");
    }

    /**
     * Μέθοδος που επιτρέπει στον Client να δει τα εισερχόμενα μηνύματα του.
     * Γίνεται αναζήτηση στη λίστα λογαριασμών ώστε να βρεθεί το username του παραλήπτη των
     * μηνυμάτων -ο οποίος θέλει να δει το γραμματοκιβώτιο του-. Εαν η είσοδος είναι ορθή τότε γίνεται αναζήτηση στη
     * λίστα των μηνυμάτων του {@link Server.Account#messageBox} και για κάθε μήνυμα που παραλήπτης(receiver) είναι ο χρήστης με authToken αυτό που έβαλε στο
     * terminal τότε επιστρέφεται η απάντηση του Server προς τον Client ως: <p>message_id. from: sender* εαν είναι αδιάβαστο </p>
     * ή <p>message_id. from: sender εάν είναι διαβασμένο </p>. Αν η είσοδος δεν είναι ορθή επιστρέφεται "Invalid Auth Token"
     * εαν δεν έχει εισερχόμενα μηνύματα, επιστρέφεται "ERROR" και σε αυτή την περίπτωση με έλεγχο από την μερία του Client
     * δεν εμφανίζεται τίποτα στο τερματικό του.
     *
     * @param inputs Τα String εισόδου απο το terminal του Client
     * @param out    η απάντηση στον Client
     */
    public static synchronized void showInbox(String[] inputs, PrintWriter out) {
        StringBuilder responseToClient = new StringBuilder();
        String user_to_read_messages = "";
        boolean client_to_show_inbox = false;
        if (accounts.size() > 0) {
            for (Account account : accounts) {
                if (account.authToken == Integer.parseInt(inputs[3])) {
                    client_to_show_inbox = true;
                    user_to_read_messages = account.username;
                    int i = 0;
                    for (Message message : account.messageBox) {
                        if (user_to_read_messages.equals(message.receiver) && !user_to_read_messages.equals(message.sender)) {
                            if (i == 0) {
                                if (!message.isRead)
                                    responseToClient.append(message.message_id).append(",").append(message.sender).append(",").append("*");
                                else
                                    responseToClient.append(message.message_id).append(",").append(message.sender).append(",").append(" ");
                            } else {
                                if (!message.isRead)
                                    responseToClient.append(",").append(message.message_id).append(",").append(message.sender).append(",").append("*");
                                else
                                    responseToClient.append(",").append(message.message_id).append(",").append(message.sender).append(",").append(" ");
                            }
                            i++;
                        }

                    }
                    if (responseToClient.isEmpty())
                        out.println("ERROR");
                    else
                        out.println(responseToClient);
                }
            }
            if(!client_to_show_inbox)
                out.println("Invalid Auth Token");
        } else
            out.println("No accounts");
    }

    /**
     * Μέθοδος που επιτρέπει στον Client να διαβάσει ένα εισερχόμενο μήνυμα για δοθέν message_id
     * Γίνεται αναζήτηση στη λίστα λογαριασμών για τον έλεγχο της ορθότητας του δοθέντος authToken. Αν είναι
     * μη-ορθό επιστρέφεται στον Client το μήνυμα "Invalid Auth Token". Εαν είναι ορθό, σε μία επανάληψη για τη λίστα
     * μηνυμάτων του, για κάθε μήνυμα εαν είναι παραλήπτης ο χρήστης με το authToken που έδωσε o Client στο τερματικό του
     * κι αν ναι τότε επιστρέφεται ο αποστολέας του μηνύματος και το σώμα του μηνύματος. Εαν δοθεί λάθος message_id τότε επιστρέφεται
     * "Message ID does not exist"
     *
     * @param inputs Τα String εισόδου απο το terminal του Client
     * @param out    η απάντηση στον Client
     */
    public static synchronized void readMessage(String[] inputs, PrintWriter out) {
        String receiver_of_msg = "";
        boolean receiver_exists = false;
        for (Account account : accounts) {
            if (Integer.parseInt(inputs[3]) == account.authToken) {
                receiver_exists = true;
                receiver_of_msg = account.username;
                boolean found = false;
                for (Message message : account.messageBox) {
                    if (Integer.parseInt(inputs[4]) == message.message_id && message.receiver.equals(receiver_of_msg)) {
                        found = true;
                        message.isRead = true;
                        out.println("(" + message.sender + ")" + message.body);
                    }
                }
                if (!found)
                    out.println("Message \bID does not exist");
            }
        }

        if(!receiver_exists)
            out.println("Invalid Auth Token");
    }



    /** Μέθοδος που επιτρέπει στον Client να διαγράψει ένα εισερχόμενο μήνυμα.
     * Με βάση το authToken που δόθηκε στο τερματικό του Client, βρίσκουμε τον reveiver (παραλήπτη) του μηνύματος,
     * ελέγχουμε αν το message_id που δόθηκε στο τερματικό αντιστοιχεί σε κάποιο message_id των εισερχόμενων μηνυμάτωντ του
     * και τότε διαγράφουμε το μήνυμα από τη λίστα {@link Server.Account#messageBox} κι επιστρέφουμε "ΟΚ". Διαφορετικά επιστρέφουμε "Message does not exist"
     * Εαν δοθεί λάθος authToken τότε η παραπάνω διαδικασία δεν εκτελείται κι επιστρέφεται το μήνυμα "Invalid Auth Token"
     * @param inputs Τα String εισόδου απο το terminal του Client
     * @param out η απάντηση στον Client
     */
    public static synchronized void deleteMessage(String[] inputs ,PrintWriter out)
    {
        String receiver_of_msg = "";
        boolean receiver_to_delete = false;
        for (Account account : accounts) {
            if (Integer.parseInt(inputs[3]) == account.authToken) {
                receiver_to_delete = true;
                receiver_of_msg = account.username;
                int i = 0;
                int index = -1;
                for (Message message : account.messageBox) {
                    if (Integer.parseInt(inputs[4]) == message.message_id && message.receiver.equals(receiver_of_msg)) {
                        index = i;
                        out.println("OK");
                        break;
                    }
                    i++;
                }
                if (index != -1)
                    account.messageBox.remove(index);
                else
                    out.println("Message does not exist");
            }
        }

        if(!receiver_to_delete)
            out.println("Invalid Auth Token");
    }
    /**
     *  Μέθοδος στην οποία γίνεται η διαχείριση των δεδομένων από τον Server και επιστρέφονται στον
     * χρήστη τα κατάλληλα μηνύματα. Ανάλογα με κάθε σενάριο και την ορθότητα των δεδομένων στέλνει διαφορετικές απαντήσεις
     * στον χρήστη. Κάθε φορά που κάποιος χρήστης στέλνει αίτημα, τότε η διαχείριση γίνεται σε ξεχωριστό νήμα.
     * @param args Τα inputs από το τερματικό του Client όταν αυτός εκτελείται
     * @throws IOException .
     */
    public static void serverRun(String[] args) throws IOException {
        PORT = Integer.parseInt(args[0]); //Αρχικοποίηση της πόρτας του Server
        ServerSocket serverSocket = new ServerSocket(PORT); //Δημιουργία του ServerSocket στη δοθείσα πόρτα


        //Ό server τρέχει συνεχώς σαν υπηρεσία, οπότε έχουμε ατέρμων βρόχο
        while (true) {
            //Αποδοχή του νέου socket-Client
            Socket socket = serverSocket.accept();

            //Παραχώρηση νήματος
            new Thread(() -> {
                try {
                    //Αρχικοποίηση IO μεταβλητών που χρησιμοποιούνται για την αποδοχή request και,
                    //την αποστολή απαντήσεων
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                    //Μορφοποίηση του request - Εξηγείται αναλυτικά στο Readme.md -.
                    String request = in.readLine();
                    request = request.substring(1, request.length() - 1);

                    String[] dump = request.split(",");
                    String[] tokens = new String[dump.length];

                    for (int i = 0; i < tokens.length; i++) {
                        if (i != 5)
                            tokens[i] = dump[i].trim();
                        else if (dump[i].charAt(0) == ' ')
                            tokens[i] = dump[i].trim();
                        else
                            tokens[i] = dump[i];
                    }
                    //Δημιουργία νέου λογαριασμού
                    if (tokens[2].equals("1"))
                        createAccount(tokens, out);
                    //Εμφάνιση όλων των Clients
                    if (tokens[2].equals("2"))
                        showClients(tokens, out);
                    //Αποστολή μηνύματος
                    if (tokens[2].equals("3"))
                        sendMessage(tokens, out);
                    //Προβολή των εισερχόμενων μηνυμάτων
                    if (tokens[2].equals("4"))
                        showInbox(tokens, out);
                    //Διάβασμα ενός συγκεκριμένου μηνύματος
                    if (tokens[2].equals("5"))
                        readMessage(tokens, out);
                    //Διαγραφή ενός συγκεκριμένου μηνύματος
                    if (tokens[2].equals("6"))
                        deleteMessage(tokens, out);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }}

        public static void main (String[]args) throws Exception {
            serverRun(args);

        }

    /**
     * Κλάση διαχείρισης των λογαριασμών των Clients
     * Έχει ως μεταβλητές:
     * <p>String username: Το όνομα του Client</p>
     * <p>int authToken: Ο αναγνωριστικός αριθμός που δίνεται στον Client κατά την δημιουργία του account του </p>
     * <p>
     *Και ώς μεθόδους έναν constructor με παραμέτρους μία τιμή για κάθε μεταβλητή. Μέσα στον constructor
     * κάθε μεταβλητή της κλάσης παίρνει ως τιμή την αντίστοιχη από τις παραμέτρους.
     * </p>

     */

    public static class Account {

        String username;
        int authToken;

        List<Message> messageBox ;
        public Account(String username, int authToken){
            this.username = username;
            this.authToken = authToken;
            this.messageBox = new ArrayList<>();
        }

    }

    /**
     * Κλάση διαχείρισης των μηνυμάτων των χρηστών.
     * Έχει ως μεταβλητές:
     * <p>int message_id: Αναγνωριστικός αριθμός του μηνύματος</p>
     * <p>boolean isRead: Μεταβλητή που συμβολίζει εάν ένα μήνυμα έχει διαβαστεί. Αδιάβαστο-false -> Διαβασμένο-true</p>
     * <p>String sender: Ο αποστολέας του μηνύματος</p>
     * <p>String receiver: Ο δέκτης του απεσταλμένου μηνύματος</p>
     * <p>String body: Το σώμα του μηνύματος (τα περιεχόμενα του)</p>
     *
     * <p>Και ώς μεθόδους έναν constructor με παραμέτρους μία τιμή για κάθε μεταβλητή. Μέσα στον constructor
     * κάθε μεταβλητή της κλάσης παίρνει ως τιμή την αντίστοιχη από τις παραμέτρους.
     */
    public static class Message {
        int message_id;
        boolean isRead;
        String sender;
        String receiver;
        String body;

        public Message(int message_id, boolean isRead, String sender, String receiver, String body){
            this.message_id = message_id;
            this.isRead = isRead;
            this.sender = sender;
            this.receiver = receiver;
            this.body = body;

        }
    }
}