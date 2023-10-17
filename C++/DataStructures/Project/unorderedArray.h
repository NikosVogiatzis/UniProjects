#pragma once
#include <string>

using std::string;

class unorderedArray
{
private:
    struct Arr
    {
        string value;
        long long unsigned int times;
    };
    Arr* Array;
    long long unsigned int FirstFreeCell;  // Πρώτη κενή θέση
protected:
    bool ReallocateArray(int);  // Αυξάνεται το μέγεθος του πίνακα
public:
    unorderedArray();  // Δημιουργεί -- αρχικά -- έναν μηδενικό πίνακα
    ~unorderedArray();  // Αποδέσμευση μνήμης από το stack

    void Insert(string);  // Εισαγωγή ενός στοιχείου στο τέλος του πίνακα
    bool Delete(string);  // Διαγραφή ενός στοιχείου του πίνακα
    bool Find(string, long long unsigned int&,  long long unsigned int&);  // Αναζήτηση στοιχείου στον πίνακα

    long long unsigned int getFirstFreeCell() { return FirstFreeCell; }

    // Δυνατότητα να επιστρέφω συγκεκριμένο στοιχείο (με αναφορά) ανάλογα με τη θέση του στον πίνακα
    string& operator[](unsigned int possition) { return Array[possition].value; }
};