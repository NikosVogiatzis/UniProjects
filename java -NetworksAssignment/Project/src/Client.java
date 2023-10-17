import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

public class Client {
        public static void clientRun(String[] args) throws IOException {


            //Δημιουργία του Socket, το οποίο συνδέεται στον Server με port number το δεύτερο argument απο την είσοδο
            Socket socket = new Socket(args[0], Integer.parseInt(args[1]));

            //Δημιουργία IO μεταβλητών ώστε να στέλνονται τα request στον Server και να δέχεται ο πελάτης
            //απάντηση από τον Server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            //Εαν το σενάριο που επιλέγει ο χρήστης είναι ένα απο τα παρακάτω τότε απλώς
            //στέλνει τα args ως string στον server και τυπώνεται η απάντηση από τον σερβερ
            if(args[2].equals("1") || args[2].equals("3") || args[2].equals("6") || args[2].equals("5"))
            {
                out.println(Arrays.toString(args));
                String response = in.readLine();
                System.out.println(response);
            }

            //Αν το σενάριο είναι το δεύτερο όπου έχουμε να εκτυπώσουμε λίστα, τότε κάνουμε split το String που
            //λάβαμε απο τον Server ως απάντηση με "διαχωριστή" το κόμμα και τυπώνουμε με μια for-each τον πίνακα
            //με τους χρήστες.Αν δοθεί λάθος auth-token τυπώνεται το αντίστοιχο μήνυμα
            if(args[2].equals("2")){
                out.println(Arrays.toString(args));
                String response = in.readLine();

                String[] outputString = new String[0];
                if(response!=null)
                    outputString = response.split(",");

                if(outputString[0].equals("Invalid Auth Token"))
                    System.out.println(outputString[0]);
                else {
                    int counter = 1;
                    for (String s : outputString) {
                        System.out.println(counter + ". " + s);
                        counter++;
                    }
                }

        }

        //Αν το σενάριο είναι η εμφάνιση όλων των μηνυμάτων, τότε χωρίζουμε πάλι το String με διαχωριστή το κόμμα
        //και το εμφανίζουμε στο κατάλληλο format.Στην περίπτωση που η απάντηση από τον Server είναι error, τότε
        //δεν τυπώνεται τίποτα καθώς η απάντηση αυτή σημαίνει ότι δεν υπάρχουν εισερχόμενα μηνύματα γι αυτόν τον χρήστη
        if(args[2].equals("4"))
        {
            out.println(Arrays.toString(args));
            String response = in.readLine();
            String[] outputString;
            outputString = response.split(",");
            if(response.equals("Invalid Auth Token"))
                System.out.println(outputString[0]);
            else {
                if (!response.equals("ERROR")) {


                    for (int i = 0; i < outputString.length; i += 3) {
                        System.out.println(outputString[i] + ". from: " + outputString[i + 1] + outputString[i + 2]);
                    }
                }
            }
        }
            socket.close();
    }

    public static void main(String[] args) throws IOException {
        clientRun(args);

    }
}