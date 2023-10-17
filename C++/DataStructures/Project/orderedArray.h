#pragma once
#include <string>
using namespace std;

//Η κλάση αυτή του ταξινομημένου πίνακα έχει την ίδια δομή με αυτή του αταξινόμητου όσον αφορά την εισαγωγή στοιχείων τη δυναμική δέσμευση/το struct


class orderedArray
{
private:
    struct Arr
    {
        string value;
        long long unsigned int times;
    };
    Arr* Array;
    long long unsigned int FirstFreeCell;  // Τελευταία ελεύθερη θέση στον πίνακα
private:
    bool binarySearch(string,long long  int,long long  int,long long int& ,long long int&);  // Υλοποίηση δυαδικής αναζήτησής
    bool ReallocateArray(int);  // Αυξάνεται το μέγεθος του πίνακα
    void InsertionSort();  // Υλοποίηση του αλγορίθμου ταξινόμησης
public:
    orderedArray();  // Δημιουργεί έναν μηδενικό πίνακα
    ~orderedArray();  // Αποδέσμευση μνήμης από το stack

    void Insert(string);  // Εισαγωγή ενός στοιχείου στον πίνακα
    bool Find(string, long long&);  // Αναζήτηση μιας λέξης
    bool Delete(string);  // Διαγραφή ενός στοιχείου του πίνακα

    long long unsigned int getFirstFreeCell() { return FirstFreeCell; }

    string& operator[](unsigned int possition) { return Array[possition].value; }  // Δυνατότητα να επιστρέφω συγκεκριμένο στοιχείο (με αναφορά) ανάλογα με τη θέση του
};