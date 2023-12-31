## ΕΡΓΑΣΙΑ ΔΙΚΤΥΑΚΟΥ ΠΡΟΓΡΑΜΜΑΤΙΣΜΟΥ 


Το παρόν πρότζεκτ υλοποιεί ένα κατανεμημένο σύστημα ανταλλαγής μηνυμάτων που χρησιμοποιεί
ένα request-reply protocol. Υλοποιήθηκε με τη χρήση των παρακάτω τεχνολογιών: Sockets-I/O-threads.

Ο Server είναι μία υπηρεσία που "τρέχει" συνεχώς και περιμένει να "ακούσει" αιτήματα από τους Clients. Αντίθετα, οι Clients
συνδέονται στην υπηρεσία χρησιμοποιώντας το **ip** του Server και την πόρτα (**port**) στην οποία ακούει ο Server, υποβάλλουν το αίτημα τους
για μία ενέργεια από τις 6 που προσφέρονται και όταν λάβει απάντηση από τον Server η σύνδεση μεταξύ των δύο τερματίζεται. 


**ΥΠΟΘΕΣΕΙΣ:** Για την υλοποίηση του project θεώρησα ότι οι είσοδοι κατά την εκτέλεση τόσο του προγράμματος του
εξυπηρετητή όσο και του Client, δίνονται πάντα με το σωστό format. Δεν υπάρχει έλεγχος για παράδειγμα στην περίπτωση που δοθεί 
έλλειπες στοιχείο π.χ (java Client localhost 500 1), όπως επίσης δεν υπάρχει έλεγχος αν δοθεί λάθος τύπος
π.χ (java Client localhost 500 **a** Nikos).

--------------------------------------------------> **Client:** <-------------------------------------------------------------------------------


Το πρόγραμμα του Client τρέχει μέσω της συνάρτησης **clientRun** που καλείται από τη main. Δημιουργεί ένα Socket αντικείμενο το οποίο
συνδέεται στον Server αν δοθούν τα κατάλληλα inputs (args[0] - το IP του Server και args[1] η πόρτα που ακούει ο Server).Έπειτα δημιουργούνται
δύο IO αντικείμενα: Ένα τύπου PrintWriter, το οποίο επιτρέπει στο πρόγραμμα να πάρει τα inputs που βάζει ο Client στο τερματικό του και να τα στείλει
στον Server, και ένα BufferedReader στο οποίο θα αποθηκευθεί η απάντηση που δέχθηκε από τον Server. Γίνονται έλεγχοι ανάλογα με το αναγνωριστικό
της λειτουργίας που θα εκτελεστεί, διότι σε 2 περιπτώσεις επιστρέφεται πίνακας ενώ στις υπόλοιπες τυπώνεται ένα μόνο String. Οι είσοδοι που δίνονται
στο τερματικό αποστέλλονται στον Server ως ένα Arrays.toString. Δηλαδή, στην περίπτωση που δοθεί: java Client localhost 500 1 user1, 
ο σέρβερ θα λάβει ένα String στην εξής μορφή: "[localhost, 500, 1, user1]". Παρακάτω εξηγείται γιατί δίνεται σε τέτοιο format.


-------------------------------------------------> **Server:** <-------------------------------------------------------------------------------


Το πρόγραμμα του εξυπηρετητή εκτελείται από τη συνάρτηση **serverRun** που καλείται από τη main. Λόγω του ότι τρέχει συνεχώς ως υπηρεσία, εκτελείται
ένας ατέρμων βρόχος μέσα στον οποίο μπορεί να "ακούσει" και να διαχειριστεί -ταυτόχρονα- διάφορους Clients.
Όπως και ο Client, ο εξυπηρετητής χρησιμοποιεί δύο IO μεταβλητές, στη μία αποθηκεύει τις εισόδους του Client και
στην άλλη αποθηκεύει την απάντηση που στέλνει πίσω. Όπως αναφέρθηκε παραπάνω, οι Clients στέλνουν την είσοδο στον Server ως ένα String στη μορφή πίνακα.
Έτσι, πρώτη δουλεία του εξυπηρετητή αφού δημιουργήσει το Socket αντικείμενο, είναι να διαχωρίσει το String με διαχωριστή το κόμμα, ώστε να μπορεί να διαχειριστεί 
τις εισόδους του πελάτη και αποθηκεύει κάθε στοιχείο σε έναν String πίνακα. Έπειτα, ανάλογα με το FN_ID που δόθηκε από τον Client καλεί την κατάλληλη συνάρτηση και παράγει έξοδο-απάντηση. Οι 6 μέθοδοι
που διαχειρίζονται τις διαφορετικές λειτουργίες εξηγούνται με inline σχόλια καθώς και χρήση javadoc, συνεπώς δε θα σχολιαστούν περαιτέρω εδώ. Να σημειωθεί
ότι οι προαναφερθείσες συναρτήσεις είναι τύπου **synchronized**, για τη σωστότερη-συγχρονισμένη διαχείριση πολλαπλών νημάτων ταυτόχρονα.
Όσα αναφέρθηκαν παραπάνω, δηλαδή οι αρχικοποίηση των μεταβλητών και η κλήση των συναρτήσεων, γίνεται αφού έχει ανοίξει ένα νέο thread. Έτσι πετυχαίνουμε τη σύνδεση
πολλαπλών Clients με τον εξυπηρετητή και την διαχείριση των αιτημάτων τους χωρίς να δημιουργείται conflict μεταξύ τους.

Στην κλάση του Server υπάρχουν 2 νέες κλάσεις: Η μία αφορά τους λογαριασμούς των χρηστών και την αποθήκευση των απαραίτητων στοιχείων τους: userName, authToken και ArrayList<Message> messageBox,
η δεύτερη αφορά τα μηνύματα τα οποία χρειάζονται αυτές τις μεταβλητές: message_id, isRead, sender, receiver, body. Μεταβλητές του Server αποτελούν:
η πόρτα στην οποία θα ακούει αιτήματα των Clients (PORT), το αναγνωριστικό που δίνεται στους χρήστες κατά την εγγραφή τους (authToken), το αναγνωριστικό
των μηνυμάτων που στέλνουν οι χρήστες (message_id) καθώς και μία λίστα για την αποθήκευση όλων των χρηστών (accounts).