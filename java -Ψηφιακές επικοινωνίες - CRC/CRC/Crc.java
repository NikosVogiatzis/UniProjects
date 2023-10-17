import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Crc {

    //Μεταβλητή που μετρά όλα τα σφάλματα που προκύπτουν στο κανάλι
    public int countFromBER  = 0;
    //Μεταβλητή που μετρά τα λάθη που ανιχνεύονται απο το crc στον αποδέκτη.
    public int countFromCrc = 0;


    /**
     * Η μέθοδος αυτή προσομοιώνει τη μετάδοση του μηνύματος μέσω ενός ενόρυβου καναλιού.
     * Δημιουργείται μία βοηθητική λίστα που αποτελεί αντίγραφο του μεταδιδόμενου μηνύματος.
     * Διατρέχεται επανάληψη όσο το μέγεθος της (αντίγραφης) λίστας και για κάθε μπιτ, δημιουργείται
     * μέσω της συνάρτησης rand ένας τυχαίος αριθμός διπλής ακρίβειας απο το 0 εως το 1. Εαν ο αριθμός είναι
     * μικρότερος του  BER ( 0.001) τότε το συγκεκριμένο μπιτ στο μεταδιδόμενο μήνυμα αλλάζει. Στο τέλος
     * της επανάληψης, αν έχει υπάρξει ένα ή περισσότερα σφάλματα αυξάνεται ο μετρητής countFromBer κατά ένα.
     * @param Message Λίστα που στα κελία της περιέχει κάθε μπιτ του μεταδιδόμενου μηνύματος
     * @return Επιστρέφει σε μορφή λίστας το μήνυμα μετά την μετάδοση του από το κανάλι με BER = 10 ^ -6
     */
    ArrayList<Character> BitErrorChannel(ArrayList<Character> Message)
    {
        boolean temp = false;
        ArrayList<Character> message = new ArrayList<>(Message);
        Random rand = new Random();
        for(int i=0;i<message.size(); i++) { //Για κάθε μπιτ του μηνύματος
            double x = rand.nextDouble(); //Generate τυχαίου double στο [0-1]
            if(x <= 0.001) {  //Αν είναι μικρότερος του επιτρεπόμενου σφάλματος,
                              // αλλάζει το μπιτ ανάλογα με την τιμή που αρχικά έχει
                temp = true;
                if(message.get(i) == '0')
                    message.set(i,'1');
                else
                    message.set(i,'0');
            }
        }
        if(temp) //Αν υπήρξε σφάλμα αύξηση του μετρητή.
            countFromBER++;
        return message; //Επιστροφή του (αλλαγμένου) μηνύματος.
    }

    /**
     * Η μέθοδος αυτή αποτελεί την υλοποίηση της πράξης αποκλειστικής διάζευξης (XOR).
     * Διατρέχεται μία επανάληψη όσο το μέγεθος του διεραίτη (y) και για κάθε στοιχείο
     * του διαιρετέου (x) εκτελείται η πράξη x XOR y με το αντιστοιχο ψηφίο του αριθμου y.
     * Χρησιμοποιείται μία βοηθητική λίστα που αποθηκεύει το αποτέλεσμα των πράξεων εντός της
     * επαναληψης.
     * @param x Διεραιτέος με τη μορφή λίστας
     * @param y Διεραίτης με τη μορφή λίστας
     * @return Λίστα αποτελούμενο από τα μπιτ της πράξης x XOR y
     */
    ArrayList<Character> XOR(ArrayList<Character> x, ArrayList<Character> y)
    {
        ArrayList<Character> temp = new ArrayList<>(); //Βοηθητική λίστα
        for(int i=1;i<y.size(); i++) //Για κάθε στοιχείο του αριθμού y
        {
            if(x.get(i) == y.get(i)) // 1 XOR 1 = 0, 0 XOR O = 0 else 1 XOR O = 1, 0 XOR 1 = 1
                temp.add('0');
            else
                temp.add('1');
        }
        return temp; //Επιστρέφεται η βοηθητική λίστα
    }

    /**
     * Η μέθοδος αυτή εκτελεί την modulo - 2 πράξη μεταξύ του μεταδιδόμενου μηνύματος και του αριθμού data
     * πρότυπο P. Χρησιμοποιείται για τον υπολογισμό του FCS αλλά και για την εύρεση του υπολοίπου στην πράξη
     * modulo - 2 μεταξύ FCS και P.
     * @param data Η λίστα που αποθηκεύει τον μήνυμα
     * @param P Ο αριθμός πρότυπο για τον υπολογισμό του CRC.
     * @return Λίστα με το αποτέλεσμα της modulo - 2 πράξης μεταξύ data - P
     */
    ArrayList<Character> mod2div(ArrayList<Character> data, ArrayList<Character> P)
    {
        ArrayList<Character> temp = new ArrayList<>(); //Βοηθητική λίστα η οποία κρατά το υπόλοιπο της XOR σε κάθε βήμα
        int size_P = P.size(); //Ο αριθμός των μπιτ στα οποία θα εκτελεστεί η πράξη XOR κάθε φορά.
        int data_size = data.size(); //Μέγεθος μεταδιδόμενου μηνύματος
        //Αντιγράφονται τα πρώτα size στοιχεία της λίστας που έχει αποθηκευμένο το μεταδιδόμενο μήνυμα.
        for(int i=0;i<size_P;i++)
            temp.add(data.get(i));
        //Διατρέχεται επανάληψη για κάθε στοιχείο αριθμόυ του μηνύματος όσο το μέγεθος P < data_size
        while (size_P < data_size)
        {
            if (temp.get(0) == '1') //Αν το πρώτο μπιτ της λίστας υπολοίπου είναι ΄1΄
                //Εκτελούμε την πράξη XOR μεταξύ P και temp και "κατεβάζουμε" το επόμενο μπιτ του μηνύματος
                temp = XOR(P, temp);
            else //Αν το πρώτο μπιτ της λίστας υπολοίπου είναι '0'
            {
                //Χρησιμοποιόυμε ως P μία λίστα με '0' σε κάθε θέση και εκτελούμε την XOR
                //Έτσι κατεβάζουμε το επόμενο μπιτ από το μεταδιδόμενο μήνυμα και κάνουμε κάτι σαν swift
                //Στη λίστα υπολοίπου ώστε να μην κρατάει άχρηστα 0-νικά στην αρχή της.
                ArrayList<Character> dump = new ArrayList<>();
                for (int i = 0; i < size_P; i++)
                    dump.add('0');
                temp = XOR(dump, temp);
            }
            temp.add((data.get(size_P)));
            //Αυξάνεται το size_P κατά ένα ώστε να κατεβεί το επόμενο μπιτ του
            //μεταδιδόμενου και να γίνει η XOR στα νέα μπιτ δεδομένων
            size_P ++;
        }
        //Τέλος για τα τελευτάια data_size μπιτς εκτελείται εκτός επανάληψης
        //Η ίδια διαδικασία ώστε να μην βγούμε εκτώς ορίων πίνακα και στο τέλος
        //Παίρνουμε ως αποτέλεσμα τον αριθμό των size_P μπιτ που αποτελέι το R κατά την εύρεση του FCS
        //και το υπόλοιπο στον έλεγχο του αποδέκτη όταν θέλουμε να ελέγξουμε αν ο CRC ανίχνευσε το σφάλμα.
        if (temp.get(0) == '1')
            temp = XOR(P, temp);
        else {
            ArrayList<Character> dump = new ArrayList<>();
            for(int i=0;i<size_P;i++)
                dump.add('0');
            temp = XOR(dump , temp);}
        return temp;
    }

    /**
     * Η μέθοδος αυτή εισάγει αρχικά (P_size -1) μηδενικά στο αρχικά μεταδιδόμενο μήνυμα.
     * Έπειτα καλεί την modulo -2 πράξη που εκτελείται μεταξύ μηνύματος και αριθμό προτύπου
     * για τον υπολογισμό του FCS. Η modulo - 2 με τον τρόπο που υλοποιήθηκε επιστρέφει το
     * υπόλοιπο των (P_size-1) μπιτ, οπότε στη συνέχεια αυτό το υπόλοιπο προσαρτάται στο τέλος
     * του αρχικού μηνύματος και πλέον αποτελεί το FCS
     * @param data Λίστα που αποθηκεύει το μεταδιδόμενο μήνυμα
     * @param P Ο αριθμός πρότυπο για τον υπολογισμό του CRC
     * @return FCS
     */
    ArrayList<Character> encodeData(ArrayList<Character> data,ArrayList<Character> P) {
        int P_size = P.size(); //Μέγεθος αριθμού προτύπου P.

        //Λίστα που θα περιέχει το μήνυμα με τα P_size -1 μηδενικά στο τέλος της
        ArrayList<Character> appended_data = new ArrayList<>(data); //Αντιγραφή του μεταδιδόμενου μηνύματος

        for (int i = 0; i < P_size -1; i++) //Εισαγωγή των P_size - 1 μηδενικών.
            appended_data.add('0');

        ArrayList<Character> remainder = mod2div(appended_data, P); //Λίστα που περιέχει το υπόλοιπο της modulo -2 πράξης

        ArrayList<Character> codeword = new ArrayList<>();
        //Δημιουργία τελικού FCS το οποίο αποτελείται
        // από το αρχικό μήνυμα + το υπόλοιπο της πράξης modulo -2
        codeword.addAll(data);
        codeword.addAll(remainder);

        return codeword;
    }

    /**
     * Η μέθοδος αυτή ελέγχει αν το μήνυμα (FCS) περιέχει σφάλμα και αυξάνει τον αντίστοιχο
     * μετρητή (ανίχνευσης λάθους στον αποδέκτη του CRC ->  countFromCrc). Αν το μήνυμα περιέχει
     * σφάλμα, τότε μετά την πραγματοποίηση της modulo-2 πράξης με τον αριθμό πρότυπο P, θα περιέχει
     * τουλάχιστον έναν άσσο στο υπόλοιπο της πράξης.
     * @param FCS Το μήνυμα (FCS) αφότου έχει περάσει από το ενόρυβο κανάλι
     * @param P Ο αριθμός πρότυπο για τον υπολογισμό του CRC.
     */
    void checkCRC(ArrayList<Character> FCS, ArrayList<Character> P)
    {
        ArrayList<Character> dump = mod2div(FCS,P); //Βοηθητική λίστα με το υπόλοιπο της modulo - 2
        if(dump.contains('1')) //Αν το υπόλοιπο της πράξης περιέχει '1', αυξάνεται μετρητής.
        {
            countFromCrc++;
        }
    }
    public static void main(String[] args) {

        Crc object = new Crc(); //Δημιουργία αντικειμένου Crc
        int k = 20; //Αριθμός μπιτ που θα περιέχει κάθε μεταδιδόμενο μήνυμα.
        int numberOfMessages = 10000000;
        ArrayList<ArrayList<Character>> Messages = new ArrayList<>(); //Λίστα λιστών που θα περιέχει N πλήθος δυαδικών μηνυμάτων

        String number; //Προσωρινή μεταβλητή που αποθηκεύει το P όταν διαβαστεί από τον χρήστη
        //Όσο ο χρήστης δίνει ως είσοδο αριθμό που δεν ξεκινά ή/και δεν τελειώνει σε '1' η διαδικασία επαναλαμβάνεται
        do {
            System.out.println("Give the number P in this format e.g: 110101 (1st and last digit must be '1'");
            Scanner input = new Scanner(System.in);
            number = input.nextLine();
        }while(number.charAt(0) != '1' || number.charAt(number.length()-1) != '1');

        ArrayList<Character> P = new ArrayList<>(); //Λίστα που σε κάθε κελί αποθηκεύει ένα μπιτ του αριθμού P
        for (int i = 0; i < number.length(); i++) { //Δηιμουργία του P μέσω του number που διαβάστηκε από τον χρήστη
            P.add(number.charAt(i));
        }

        Random rand = new Random(); //Δημιουργία αντικειμένου από την κλάση Random της Java
        //Σε αυτήν την επανάληψη δημιουργούνται τα (numberOfMessages σε πλήθος) μηνύματα προς μετάδοση των k μπιτ
        for (int i = 0; i < numberOfMessages; i++) {
            ArrayList<Character> dump = new ArrayList<>(); //Βοηθητική λίστα που αποθηκεύει κάθε μπιτ του μηνύματος
            for (int j = 0; j < k; j++) {
                int n = rand.nextInt(2);
                dump.add((char) (n + '0'));
            }
            Messages.add(dump); //Εισαγωγή του μηνύματος στη λίστα λιστών που αποθηκεύει όλα τα μηνύματα.
        }

        //Για κάθε μήνυμα υπολογίζεται το FCS
        //έπειτα το FCS περνά από το κανάλι με BER
        //Τέλος ελέγχεται το crc στον αποδέκτη
        for(int i=0;i<numberOfMessages;i++)
        {
            ArrayList<Character> FCS = object.encodeData(Messages.get(i),P);
            ArrayList<Character> FCS_WITH_BER = object.BitErrorChannel(FCS);
            object.checkCRC(FCS_WITH_BER,P);

        }

        System.out.println("Errors found in BER: " + object.countFromBER);
        System.out.println("Errors found from CRC: " + object.countFromCrc);
        System.out.println("Errors not detected from CRC: " + (object.countFromBER - object.countFromCrc));
    }
}