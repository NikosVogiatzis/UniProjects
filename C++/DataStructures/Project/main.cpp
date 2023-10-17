#include <iostream>
#include "fstream"
#include <string>
#include <time.h>

#include "unorderedArray.h"
#include "orderedArray.h"
#include "Hashing.h"
#include "BST.h"
#include "AVL.h"

using namespace std;

void RemakeWord(string& word_to_insert, string word)
{
    // Τρέχω επανάληψη για όλο το μήκος της
    // συμβολοσειράς που πήρα από το αρχείο
    for (int i = 0, len = word.size(); i < len; i++)
    {
        // Μετατρέπει τα κεφαλαία σε πεζά
        if(isupper(word[i]))
            word[i] = word[i] + 32;

        // Αφού ο χαρακτήρας της συμβολοσειράς στη θέση i είναι πλέον πεζός
        // ελέγχω αν είναι μεταξύ a-z. Αν ναι τον εισάγω στη συμβολοσείρα k
        // κι έτσι το string μου "πετάει" τους χαρακτήρες που δεν θέλω.
        if ((word[i] >= 'a' && word[i] <= 'z'))
            word_to_insert = word_to_insert + word[i];
    }
}

template<typename DataType>
void InsertionProcess(DataType& data, string file_name, string name)
{
    ofstream TimeOutput;
    string word; clock_t START, END;
    ifstream file(file_name, ios::in);

    if (!file.is_open())
    {
        cout << "No such file found" << endl;
        exit (1) ;
    }

    // ΑΡΧΗ: Χρονομέτρηση για την εισαγωγή
    START = clock();

    while (file >> word)
    {
        // Κενή συμβολοσειρά πάνω στην οποία θα "χτιστεί" η συμβολοσειρά
        // που πρέπει να εισάγω (χωρίς σημεία στίξης-αριθμούς-κεφαλαία)
        string word_to_insert = "";
        if(file.eof())
            break;
        RemakeWord(word_to_insert,word);

        // Αν το string διάφορο του κενού τότε το εισάγω στη δομή
        if(word_to_insert != "" )
            data.Insert(word_to_insert);
    }

    file.close();

    END = clock();
    // ΤΕΛΟΣ: Χρονομέτρηση για την εισαγωγή

    // ΑΡΧΗ: Καταγραφή χρόνου σε αρχείο
    double requiredClocks = END - START;
    TimeOutput.open("TimeOutput.txt", ios::app);
    TimeOutput << " (*) " << name << " -> Insert: " << requiredClocks / CLOCKS_PER_SEC << " seconds" << endl;
    TimeOutput.close();
    // ΤΕΛΟΣ: Καταγραφή χρόνου σε αρχείο
}

int main()
{
    string filename = "words.txt";
    string tree_file_name = "tree_print.txt";//εισαγεται ως όρισμα στις inorder,preorder,postorder των δεντρων
    srand(time(0));
    double requiredClocks;

    clock_t START, END;

    unorderedArray Unordered;
    orderedArray Ordered;
    Hashing Hash;
    BST myBst;
    AVL myAvl;


    // Στο "Output.txt" αποθηκεύουμε τις
    // λέξεις και το πλήθος εμφανίσεων κάθε λέξης
    ofstream Output("Output.txt");

    // Στο "TimeOutput.txt" αποθηκεύουμε τους χρόνους
    // εισαγωγής και αναζήτησης για κάθε δομή
    ofstream TimeOutput("TimeOutput.txt"); TimeOutput.close();


    // ΑΡΧΗ: UNORDERED ARRAY

    cout << " ~ Reading and inserting: unordered array" << endl;
    InsertionProcess(Unordered, filename, "Unordered Array");

    long long int from = rand() % (Unordered.getFirstFreeCell()); // θέτει τυχαία τιμή που θα είναι η αρχή για το σύνολο που θέλουμε να κάνουμε αναζητήσεις
    long long int to =  from + (rand() % (Unordered.getFirstFreeCell() - from + 1));  // θέτει τυχαία τιμή μεγαλύτερη της from που αναπαριστά το τέλος του συνόλου Q

    // ΑΡΧΗ: Δημιουργία συνόλου Q

    long long int position = from;
    long long int Q_set_size = to - from; // Q_set_size = μέγεθος του συνόλου Q(αφορά τα στοιχεία του Unordered από το from εως το to)

    // Δημιουργία στατικού πίνακα συμβολοσειρών (Tο σύνολο Q)
    string Q_set[Q_set_size];

    for (long long unsigned int i = 0; i < Q_set_size; i++)
    {
        Q_set[i] = Unordered[position];
        position++;
    }

    // ΤΕΛΟΣ: Δημιουργία συνόλου Q


    cout << " ~ Searching: unordered array" << endl;
    Output << "From = " << from << ", To = " << to << endl;

    // ΑΡΧΗ: Χρονομέτρηση για την αναζήτηση
    START = clock();

    for (long long unsigned int i = 0; i < Q_set_size; i++)
    {
        long long unsigned int temp, times;
        string element = Q_set[i];
        Unordered.Find(element, temp, times);

        Output << "Unordered Array: Word '" << element << "' exists [" << times << "] times" << endl;
    }

    END = clock();
    // ΤΕΛΟΣ: Χρονομέτρηση για την αναζήτηση

    // ΑΡΧΗ: Καταγραφή χρόνου σε αρχείο
    requiredClocks = END - START;
    TimeOutput.open("TimeOutput.txt", ios::app);
    TimeOutput << " (*) Unordered Array -> Search: " << requiredClocks / CLOCKS_PER_SEC << " seconds\n" << endl;
    TimeOutput.close();
    // ΤΕΛΟΣ: Καταγραφή χρόνου σε αρχείο

    cout << " ~ Done: unordered array\n" << endl;
    Output << endl;

    // ΤΕΛΟΣ: UNORDERED ARRAY


    // ΑΡΧΗ: ORDERED ARRAY

    cout << " ~ Reading and inserting: ordered array" << endl;
    InsertionProcess(Ordered, filename, "Ordered Array");

    cout << " ~ Searching: ordered array" << endl;
    Output << "From = " << from << ", To = " << to << endl;

    // ΑΡΧΗ: Χρονομέτρηση για την αναζήτηση
    START = clock();

    for (long long unsigned int i = 0; i < Q_set_size; i++)
    {
        long long times;
        string element = Q_set[i];
        Ordered.Find(element, times);
        Output << "Ordered Array: Word '" << element << "' exists [" << times << "] times" << endl;
    }

    END = clock();
    // ΤΕΛΟΣ: Χρονομέτρηση για την αναζήτηση

    // ΑΡΧΗ: Καταγραφή χρόνου σε αρχείο
    requiredClocks = END - START;
    TimeOutput.open("TimeOutput.txt", ios::app);
    TimeOutput << " (*) Ordered Array -> Search: " << requiredClocks / CLOCKS_PER_SEC << " seconds\n" << endl;
    TimeOutput.close();
    // ΤΕΛΟΣ: Καταγραφή χρόνου σε αρχείο

    cout << " ~ Done: ordered array \n" << endl;
    Output << endl;

    // ΤΕΛΟΣ: ORDERED ARRAY


    // ΑΡΧΗ: BST

    cout << " ~ Reading and inserting: BST" << endl;
    InsertionProcess(myBst, filename, "BST");

    cout << " ~ Searching: BST" << endl;
    Output << "From = " << from << ", To = " << to << endl;

    // ΑΡΧΗ: Χρονομέτρηση για την αναζήτηση
    START = clock();

    for (long long unsigned int i = 0; i < Q_set_size; i++)
    {
        long long times;
        string element = Q_set[i];
        myBst.search(element, times);
        Output << "BST: Word '" << element << "' exists [" << times << "] times" << endl;
    }

    END = clock();
    // ΤΕΛΟΣ: Χρονομέτρηση για την αναζήτηση

    // ΑΡΧΗ: Καταγραφή χρόνου σε αρχείο
    requiredClocks = END - START;
    TimeOutput.open("TimeOutput.txt", ios::app);
    TimeOutput << " (*) BST -> Search: " << requiredClocks / CLOCKS_PER_SEC << " seconds\n" << endl;
    TimeOutput.close();
    // ΤΕΛΟΣ: Καταγραφή χρόνου σε αρχείο

    cout << " ~ Done: BST \n" << endl;
    Output << endl;

    // ΤΕΛΟΣ: BST


    // ΑΡΧΗ: AVL

    cout << " ~ Reading and inserting: AVL" << endl;
    InsertionProcess(myAvl, filename, "AVL");

    cout << " ~ Searching: AVL" << endl;
    Output << "From = " << from << ", To = " << to << endl;
    // ΑΡΧΗ: Χρονομέτρηση για την αναζήτηση
    START = clock();

    for (long long unsigned int i = 0; i < Q_set_size; i++)
    {
        long long times;
        string element = Q_set[i];
        myAvl.search(element, times);
        Output << "AVL: Word '" << element << "' exists [" << times << "] times" << endl;
    }

    END = clock();
    // ΤΕΛΟΣ: Χρονομέτρηση για την αναζήτηση

    // ΑΡΧΗ: Καταγραφή χρόνου σε αρχείο
    requiredClocks = END - START;
    TimeOutput.open("TimeOutput.txt", ios::app);
    TimeOutput << " (*) AVL -> Search: " << requiredClocks / CLOCKS_PER_SEC << " seconds\n" << endl;
    TimeOutput.close();
    // ΤΕΛΟΣ: Καταγραφή χρόνου σε αρχείο

    cout << " ~ Done: AVL \n" << endl;
    Output << endl;

    // ΤΕΛΟΣ: AVL


    // ΑΡΧΗ: HASH TABLE

    cout << " ~ Reading and inserting: hash table" << endl;
    InsertionProcess(Hash, filename, "Hash");

    cout << " ~ Searching: hash table" << endl;
    Output << "From = " << from << ", To = " << to << endl;

    // ΑΡΧΗ: Χρονομέτρηση για την αναζήτηση
    START = clock();

    for (long long unsigned int i = 0; i < Q_set_size; i++)
    {
        long long times;
        string element = Q_set[i];
        Hash.Find(element, reinterpret_cast<unsigned int &>(times));
        Output << "Hash Table: Word '" << element << "' exists [" << times << "] times" << endl;
    }

    END = clock();
    // ΤΕΛΟΣ: Χρονομέτρηση για την αναζήτηση

    // ΑΡΧΗ: Καταγραφή χρόνου σε αρχείο
    requiredClocks = END - START;
    TimeOutput.open("TimeOutput.txt", ios::app);
    TimeOutput << " (*) Hash -> Search: " << requiredClocks / CLOCKS_PER_SEC << " seconds\n" << endl;
    TimeOutput.close();
    // ΤΕΛΟΣ: Καταγραφή χρόνου σε αρχείο

    cout << " ~ Done: hash table \n" << endl;
    Output << endl;

    // ΤΕΛΟΣ: HASH TABLE

    return 0;
}