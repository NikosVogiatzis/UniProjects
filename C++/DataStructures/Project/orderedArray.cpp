#include "orderedArray.h"

orderedArray::orderedArray()
{
    FirstFreeCell = 0;
    Array = nullptr;
}

orderedArray::~orderedArray()
{
    delete[] Array;
}

void orderedArray::Insert(string word)
{
    long long  int temp , times;

    if (binarySearch(word, 0, FirstFreeCell - 1, temp, times))
        Array[temp].times++;
    else
    {
        ReallocateArray(1);
        Array[FirstFreeCell - 1].value = word;
        Array[FirstFreeCell - 1].times = 1;
        InsertionSort();
    }
}

void orderedArray::InsertionSort()  // Υλοποίηση της InsertionSort
{
    int i,j,Time;
    string key;
    for(i = 1; i < FirstFreeCell; i++)
    {
        key = Array[i].value;
        Time = Array[i].times;

        j=i-1;

        while(j >= 0 && Array[j].value > key)
        {
            Array[j + 1].value = Array[j].value;
            Array[j + 1].times = Array[j].times;
            j=j-1;
        }

        Array[j + 1].value = key;
        Array[j + 1].times = Time;
    }
}

bool orderedArray::Delete(string object)
{
    long long  int temp, times;

    if (!binarySearch(object, 0, FirstFreeCell, temp, times))  // Ελέγχουμε αν το στοιχείο υπάρχει στον πίνακα
        return false;

    for (long long unsigned int position = temp; position < FirstFreeCell - 1; position++)
    {
        Array[position].value = Array[position + 1].value;
        Array[position].times = Array[position + 1].times;
    }

    FirstFreeCell--;
    return true;
}

bool orderedArray::ReallocateArray(int n)
{

    Arr* temp = new (std::nothrow) Arr[FirstFreeCell + n];  // Δημιουργείται ένας προσωρινός δυναμικός πίνακας με το νέο μέγεθος

    if (temp == nullptr)  // Ελέγχουμε την περίπτωση που συμβεί κάτι
        return false;

    for (long long unsigned int element = 0; element < FirstFreeCell; element++)  // Αντιγράφουμε τα στοιχεία του πίνακα στον προσωρινό πίνακα
    {
        temp[element].value = Array[element].value;
        temp[element].times=Array[element].times;
    }

    delete[] Array;  // Διαγράφουμε τον πίνακα από τον σωρό
    Array = temp;  // Ορίζουμε τις ιδιότητες του temp στο Array μετά την προσθήκη της θέσης που προσθέσαμε
    FirstFreeCell++;  // Αυξάνουμε το μέγεθος του πίνακα

    return true;
}

bool orderedArray::binarySearch(string word, long long  int left, long long int right, long long& temp, long long &times)
{
    // Αν το δεξί μέρος γίνει μεγαλύτερο από το αριστερό που σημάινει ότι δεν βρέθηκε το στοιχείο επιστρέφουμε false
    if (right >= left)
    {
        // Ορίζω μια ακέραια τιμή mid που δείχνει την κεντρική θέση του πίνακα
        long long  int mid = left + (right - left) / 2;

        // Αν η συμβολοσειρά στη θέση mid είναι αυτή που ψάχνω επιστρέφω true
        // και αρχικοποιώ το temp=mid ώστε να επιστρέψω με αναφορά τη θέση του στοιχείου που βρέθηκε
        if (Array[mid].value == word)
        {
            temp = mid;
            times = Array[mid].times;
            return true;
        }

        // Αν το στοιχείο που ψάχνω είναι μεγαλύτερο (γραμματικά) του στοιχείου στην κεντρική θέση
        // "πετάω" το μισό απο το left ως το mid και αναδρομικά συνεχίζω την αναζήτηση στο πρώτο μισό
        if (Array[mid].value > word)
            return binarySearch(word,left, mid - 1,temp , times);

        // Αν το στοιχείο που ψάχνω είναι μεγαλύτερο (γραμματικά) του στοιχείου στην κεντρική θέση
        // "πετάω" το μισό απο το left ως το mid και αναδρομικά συνεχίζω την αναζήτηση στο δεύτερο μισό
        return binarySearch(word,mid + 1, right,temp , times);
    }

    times = 0;
    return false;
}

bool orderedArray::Find(string word, long long& times)
{
    long long temp;
    return binarySearch(word, 0, FirstFreeCell, temp, times);
}
