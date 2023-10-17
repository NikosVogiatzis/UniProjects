#include "Hashing.h"

Hashing::Hashing()
{
    Size = 100;
    Elements = 0;

    Table = new HashNode*[Size];  // Δημιουργώ έναν νέο πίνακα και
    for(int i = 0;i < Size; i++)  // θέτω κάθε θέση του πίνακα
        Table[i] = nullptr;       // να μη δείχνει σε κάποια θέση μνήμης
}

Hashing::~Hashing()
{
    delete[] Table;  // Καταστρέφω τον πίνακα
    Table = nullptr;  // Τον θέτω να μη δείχνει σε κάποια θέση μνήμης
    Elements = 0;
    Size = 0;
}

unsigned int Hashing::Hash(const int& key)
{
    return key % Size;
}

unsigned int Hashing::nextHash(unsigned int key)
{
    return (key + 1) % Size;
}

bool Hashing::Insert(const string& word)
{
    int key = getKey(word);

    if (Get(key, word) != nullptr)
        return false;

    if (Elements * 2 >= Size)  // Περίπτωση που το μισό του πίνακα γέμισε
    {
        Size *= 2;
        HashNode **temp;

        // Δημιουργώ ένα προσωρινό πίνακα τύπου HashNode με την νέα θέση που επιθυμώ
        temp = new (std::nothrow) HashNode*[Size];
        for (int i = 0; i < Size; i++)
            temp[i] = nullptr;  // Θέτω κάθε θέση του πίνακα NULL

        // Αντιγράφω τις θέσεις του πίνακα στον προσωρινό
        for (int i = 0; i < Size / 2; i++)
        {
            if (Table[i] != nullptr)
            {
                unsigned int p = Hash(Table[i]->key);

                while (temp[p] != nullptr)
                    p = nextHash(p);

                temp[p] = Table[i];
                Table[i] = nullptr;
            }
        }

        delete[] Table;  // Διαγράφω όλα τα στοιχεία του πίνακα
        Table = temp;  // Μεταφέρω όλες τις ιδιότητες του προσωρινού πίνακα στον κανονικό
    }

    unsigned int HashIndex = Hash(key);
    while (Table[HashIndex] != nullptr)   // Καθώς η θέση είναι δεσμευμένει από κάποια άλλη λέξη με
        HashIndex = nextHash(HashIndex);  // συγκεκριμένο key, μεταβαίνουμε σε επόμενη θέση και ξανά-ελέγχουμε

    // Εκχωρούμε την λέξη στον πίνακα και αυξάνουμε το πλήθος των στοιχείων
    Table[HashIndex] = new HashNode(word, key, 0); Elements++;
    return true;
}

HashNode*& Hashing::Get(const int& key, const string& word)
{
    unsigned int p = Hash(key);
    while (Table[p] != nullptr /*&& Table[p]->key != key*/)  // Ελέγχουμε αν η θέση είναι κενή
    {
        if (Table[p]->word == word)  // Έλεγχος: Αν η λέξη (που είναι προς αναζήτηση) είναι ίδια με αυτού του πίνακα
        {
            Table[p]->times++;  // Αυξάνουμε το πλήθος εμφανίσεων της συγκεκριμένης λέξης
            return Table[p];
        }

        p = nextHash(p);
    }

    return Table[p];
}

bool Hashing::Find(string& word, unsigned int &times)
{
    int key = getKey(word);
    HashNode* p = Get(key, word);  // Μου επιστρέφει την θέση με τις ιδιότητες του πίνακα που ψάχνω

    if (p == nullptr)
        return false;

    times = p->times;  // Με αναφορά "περνάω" τις τιμές
    word = p->word;    // που επιθυμώ στην main

    return true;
}

//    int Hashing::getKey(const string& word)
//    {
//        unsigned int letters = 0;       // Για κάθε χαρακτήρα στο string Value
//        for (char letter : word)        // παίρνω την θέση που έχει στον πίνακα ASCII
//            letters += letter;          // και την προσθέτω στην μεταβλητή που κρατάει
//        return int(letters);            // το σύνολο θέσεων των χαρακτήρων.
//    }

// Περισσότερα στην αναφορά ή στο παρακάτω άρθρο [1]
// [1] Άρθρο: https://cp-algorithms.com/string/string-hashing.html
long long Hashing::getKey(const string& s)
{
    const int p = 31;
    const int m = 1e9 + 9;
    long long hash_value = 0;
    long long p_pow = 1;

    // Για κάθε χαρακτήρα της λέξης, επιτελούνται
    // κάποιες πράξεις[*] με βάση μια πολυωνυμική συνάρτηση
    // περισσότερα στο άρθρο [1].

    // [*] Η κάθε πράξη με γράμμα πραγματοποιείται
    // με βάση την θέση που έχουν τα γράμματα στον πίνακα ASCII.

    for (char c : s) {
        hash_value = (hash_value + (c - 'a' + 1) * p_pow) % m;
        p_pow = (p_pow * p) % m;
    }

    return hash_value;
}