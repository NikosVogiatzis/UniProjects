#pragma once
#include <string>

using std::string;

class HashNode
{
public:
    HashNode(string word, int key, int times)
    {
        this->word = word;
        this->key = key;
        this->times = times;
    }
    string word;
    unsigned int key, times;
};

class Hashing
{
private:
    unsigned long long int Size;  // Μέγεθος
    unsigned long long int Elements;  // Σύνολο στοιχείων
    HashNode** Table;  // Πίνακας
protected:
    long long getKey(const string&);  // Μετατροπή λέξεων σε αριθμό
    HashNode*& Get(const int&, const string&);  // Επιστρέφει συγκεκριμένη θέση του πίνακα
    unsigned int Hash(const int&);  // Συνάρτηση που υπολογίζει τη θέση (key MOD Size)
    unsigned int nextHash(unsigned int);  // Συνάρτηση που υπολογίζει επόμενη θέση (key + 1 MOD Size)
public:
    Hashing();  // Κατασκευαστής
    ~Hashing();  // Καταστροφέας
    bool Insert(const string&);  // Εισαγωγή στοιχείου
    bool Find(string&, unsigned int &);  // Αναζήτηση στοιχείου
};