#include "unorderedArray.h"

using namespace std;

unorderedArray::unorderedArray()
{
    FirstFreeCell = 0;
    Array = nullptr;
}

unorderedArray::~unorderedArray()
{
    delete[] Array;
}

void unorderedArray::Insert(string word)
{
    long long unsigned int temp, times;
    if(Find(word,temp ,times))
        Array[temp].times++;
    else
    {
        ReallocateArray(1);
        Array[FirstFreeCell - 1].value = word;
        Array[FirstFreeCell - 1].times = 1;
    }
}

bool unorderedArray::Delete(string object)
{
    long long unsigned int temp, times;
    if (!Find(object,temp, times))  // Ελέγχουμε αν το στοιχείο υπάρχει στον πίνακα
        return false;

    for (long long unsigned int position = temp; position < FirstFreeCell - 1; position++)
    {
        Array[position].value = Array[position + 1].value;  // Μετάθεση λέξεων μία θέση πίσω
        Array[position].times = Array[position + 1].times;  // Μετάθεση φορών εμφάνισης μία θέση πίσω
    }

    Array[FirstFreeCell - 1].times = 1;
    FirstFreeCell--;

    return true;
}

bool unorderedArray::Find(string object, long long unsigned int& temp, long long unsigned int& times)
{
    if(FirstFreeCell == 0)  // Έλεγχος σε περίπτωση που ο πίνακας είναι κενός
        return false;

    for (unsigned long long int i = 0; i < FirstFreeCell; i++)  // Υλοποίηση σειριαρκής αναζήτησης
    {
        if(Array[i].value == object)
        {
            temp = i;
            times = Array[i].times;
            return true;
        }
    }

    times = 0;
    return false;
}

bool unorderedArray::ReallocateArray(int n)
{
    // Δημιουργείται ένας προσωρινός δυναμικός πίνακας τύπου Arr με το νέο μέγεθος
    Arr* temp = new (std::nothrow) Arr[FirstFreeCell + n];

    if (temp == nullptr) // Ελέγχουμε την περίπτωση που συμβεί κάτι
        return false;

    for (long long unsigned int element = 0; element < FirstFreeCell; element++)  // Αντιγράφουμε τα στοιχεία του πίνακα στον προσωρινό πίνακα (values και times)
    {
        temp[element].value = Array[element].value;
        temp[element].times= Array[element].times;
    }

    delete[] Array;  // Διαγράφουμε τον πίνακα από τον σωρό
    Array = temp;  // Ορίζουμε τις ιδιότητες του temp στο Array μετά την προσθήκη της θέσης που προσθέσαμε

    FirstFreeCell++;  // Αυξάνουμε το μέγεθος κατά n μονάδες
    return true;
}