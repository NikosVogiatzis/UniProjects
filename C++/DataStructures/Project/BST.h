#pragma once

#include <fstream>
#include <string>
#include "node.h"

using namespace std;

class BST
{
private:
    node* root;
    node* min(node *);  // Εύρεση ελάχιστου κόμβου
    node* search(string );
    void inOrder(node*, string);
    void preOrder(node*, string);
    void postOrder(node*, string);
    void destroyTree(node* root);  // Χρησιμοποιείται στο destructor για να διαγραφεί το δέντρο
    bool remove (node*);
    bool insert(node* root , const string &word);
public:

    ~BST();
    BST();

    // Οι παρακάτω συναρτήσεις καλλούνται από τη main και η υλοποίηση της κάθε μίας
    // πραγματοποιείται με τις αντίστοιχες στο private κομμάτι παραπάνω

    void printInOrder (string);                // Ενδοδιατεταγμένη διάσχιση
    void printPreOrder(string);                // Προδιατεταγμένη διάσχιση
    void printPostOrder(string);               // Μεταδιατεταγμένη διάσχιση

    bool Insert(string);                       // Εισαγωγή στο απλό δυαδικό δέντρο αναζήτησης
    bool search(string, long long int&);       // Αναζήτηση κόμβου
    bool remove(string);                       // Διαγραφή κόμβου

};


BST::BST()  // Κενός κατασκευαστής ορίζει τη ρίζα σε nullptr
{
    root = nullptr;
}

BST::~BST()
{
    destroyTree(root);
}

void BST::destroyTree(node* root)
{
    if (root != nullptr)
    {
        destroyTree(root->left);
        destroyTree(root->right);
        delete(root);
    }
}

bool BST::Insert(string word)  // Εισαγωγή νέου κόμβου
{
    if (root == nullptr)  // Αν δεν υπάρχει ρίζα(δηλαδή κενό δέντρο) Δημιουργείται ρίζα και επιστρέφεται true
    {
        root = new node(word);
        return true;
    }

    return insert(root, word);  // Αν το δέντρο έχει ήδη ρίζα πραγματοποιείται η εισαγωγή με την private συνάρτηση
}


bool BST::insert(node* root , const string &word)
{
    if (word == root->data.value)  // Αν η λέξη προς εισαγωγή υπάρχει ήδη στο BST, τότε αυξάνονται οι φορές εμφάνισης
    {                              // (times) κατά ένα και τερματίζει η διαδικασία της εισαγωγής.
        root->data.times++;
        return true;
    }

    if (word > root->data.value)  // Αν η λέξη προς εισαγωγή είναι μεγαλύτερη της ρίζας
    {
        if (root->right == nullptr)  // και η ρίζα δεν έχει δεξί παιδί τότε,
        {
            root->right = new node(word);  // η λέξη εισάγεται ως το δεξί παιδί της ρίζας και ο
            root->right->parent = root;  // γονίος του κόμβου που περιέχει τη νέα λέξη είναι η ρίζα

            return true;
        }

        // Αν η ρίζα έχει ήδη δεξί παιδί επαναλαμβάνεται αναδρομικά η διαδικασία της εισαγωγής
        // λαμβάνοντας αυτή τη φορά ως ρίζα το δεξί παιδί της ρίζας.

        return insert (root->right,word);
    }
    else  // Αν η λέξη προς εισαγωγή είναι μικρότερη της ρίζας
    {
        if (root->left==nullptr)  // και η ρίζα δεν έχει αριστερό παιδί τότε,
        {
            root->left = new node(word);  // η λέξη εισάγεται ως το αριστερό παιδί της ρίζας
            root->left->parent = root;  // γονίος του κόμβου που περιέχει τη νέα λέξη είναι η ρίζα

            return true;
        }

        // Αν η ρίζα έχει ήδη αριστερό παιδί επαναλαμβάνεται αναδρομικά η διαδικασία της εισαγωγής
        // λαμβάνοντας αυτή τη φορά ως ρίζα το αριστερό παιδί της ρίζας.

        return insert (root->left, word);
    }
}


void BST::printInOrder(string file_name)  // Διαδικασία ενδοδιατεταγμένης διάσχισης
{
    ofstream file;
    file.open(file_name, ios::app);
    file << "BST In order print" << endl << endl;
    inOrder (root,file_name);  // Κλήση της private inOrder
    file << endl ;

}


void BST::inOrder (node* p, string file_name)
{
    if (p == nullptr)  // Αν δεν έχουμε να ελέγξουμε άλλους κόμβους στο δέντρο τερματίζει
        return;

    inOrder (p->left,file_name);  // Αναδρομική κλήση για το αριστερό υποδέντρο

    // Άνοιγμα αρχείου με όνομα file_name ώστε να γραφτούν
    // μέσα οι κόμβοι σε ενδοδιατεταγμένη σειρά

    ofstream file;
    file.open(file_name, ios::app);
    file <<"string: "<< p->data.value << ", times_existed: " << p->data.times<<"!"<<endl;
    inOrder (p->right,file_name);  // Αναδρομική κλήση για το δεξί υποδέντρο
}

void BST::printPreOrder(string file_name)   // Διαδικασία προδιατεταγμένης διάσχισης
{
    ofstream file;
    file.open(file_name, ios::app);
    file << "BST Pre order print" << endl << endl;
    preOrder (root,file_name);  // Κλήση της private preOrder
    file << endl ;
}


void BST::preOrder (node* p, string file_name)
{
    if (p == nullptr)  // Αν δεν έχουμε να ελέγξουμε άλλους κόμβους στο δέντρο τερματίζει
        return;

    // Άνοιγμα αρχείου με όνομα file_name ώστε να γραφτούν
    // μέσα οι κόμβοι σε ενδοδιατεταγμένη σειρά

    ofstream file;
    file.open(file_name, ios::app);
    file <<"string: "<< p->data.value << ", times_existed: " << p->data.times<<"!"<<endl;

    preOrder (p->left,file_name);  // Αναδρομική κλήση για το αριστερό υποδέντρο
    preOrder (p->right,file_name);  // Αναδρομική κλήση για το δεξί υποδέντρο

}

void BST::printPostOrder(string file_name)  // Διαδικασία μεταδιατεταγμένης διάσχισης
{
    ofstream file;
    file.open(file_name, ios::app);
    file << "BST Post order print" << endl << endl;
    postOrder (root,file_name);  // Κλήση της private postOrder
    file << endl;
}


void BST::postOrder (node* p, string file_name)
{
    if (p == nullptr)  // Αν δεν έχουμε να ελέγξουμε άλλους κόμβους στο δέντρο τερματίζει
        return;

    postOrder (p->left,file_name);  // Αναδρομική κλήση για το αριστερό υποδέντρο
    postOrder (p->right,file_name);  // Αναδρομική κλήση για το δεξί υποδέντρο

    // Άνοιγμα αρχείου με όνομα file_name ώστε να γραφτούν
    // μέσα οι κόμβοι σε ενδοδιατεταγμένη σειρά

    ofstream file;
    file.open(file_name, ios::app);
    file <<"string: "<< p->data.value << ", times_existed: " << p->data.times<<"!"<<endl;
}

bool BST::search(string word, long long int& times_existed)
{
    node* p = search(word);  // Δημιουργία κενού κόμβου ο οποίος θα πάρει τιμή απο τη διαδικασία της private search

    if(p == nullptr)  // Αν η τιμή που πήρε είναι null (δηλαδή δε βρέθηκε η λέξη),
    {
        times_existed = 0;  // η μεταβλητή που μετρά τις φορές εμφάνισης της λέξης word
        return false;  // γίνεται 0 και επιστρέφεται αναφορικά
    }

    // Αν η λέξη βρεθεί η μεταβλητή που μετρά τις φορές εμφάνισης της
    // λέξης word γίνεται όσο η μεταβλητή times του p

    times_existed = p->data.times;
    return true;
}


node* BST::search(string word)
{
    node* p;  // Δημιουργία κενόυ κόμβου
    p = root;  // Ο κόμβος p παίρνει τις ιδιότητες της ρίζας του δέντρου

    while (p != nullptr && word != p->data.value)  // Όσο υπάρχουν κόμβοι προς έλεγχο και η λέξη δεν βρίσκεται,
    {
        if (word > p->data.value)  // Ελέγχεται αν η λέξη προς αναζήτηση είναι μεγαλύτερη από τη λέξης που έχει αποθηκευμένη ο κόμβος
            p = p->right;  // αν ισχύει ο έλεγχος παίρνεται ως νέα ρίζα το δεξί παιδί της (ως τώρα) ρίζας
        else
            p = p->left;  // αν δεν ισχύει ο έλεγχος παίρνεται ως νέα ρίζα το αριστερό παιδί της (ως τώρα)ρίζας
    }
    return p;  // Επιστρέφεται ο κόμβος p (η ρίζα που τροποποιήθηκε παραπάνω)
}

bool BST::remove(string word)  // Διαγραφή κόμβου απο το δέντρο BST
{
    node* child=search(word);  // Δημιουργία κενόυ κόμβου ο οποίος θα πάρει τιμή απο τη διαδικασία της private search

    if (child != nullptr)  // Αν ο κόμβος βρεθεί μέσα στο δέντρο
        return remove (child);  // Αφαιρείται με τη διαδικασία της private remove
    return false;
}


bool BST::remove(node* child)  // Private διαδικασία που αφαιρεί τον κόμβο child από το δέντρο
{
    node* parent;  // Δημιουργία κενόυ κόμβου

    if (child == nullptr)
        return false;

    parent = child->parent;  // Στον κόμβο που δημιουργήθηκε παραπάνω ορίζονται οι ιδιότητες του γονιού του κόμβου child

    if (child->left == nullptr && child->right == nullptr) // Αν ο κόμβος δεν έχει παιδία (είναι φύλλο)!!
    {
        if (parent==nullptr)  // Αν δεν έχει γονιό (δηλαδή είναι μοναδικός κόμβος του δέντρου και η ρίζα του)
        {
            delete(child);  // Διαγράφεται ο κόμβος
            root = nullptr;  // Η ρίζα ορίζεται null (κενό δέντρο)
        }
        else if (parent->left == child)  // Αν ο κόμβος child είναι το αριστερό παιδί του γονιού του
        {
            delete (parent->left);  // Διαγράφεται ο κόμβος
            parent->left=nullptr;  // Ορίζεται το αριστερό παιδί του κόμβου σε null
        }
        else  // Αν ο κόμβος child είναι το δεξί παιδί του γονιού του
        {
            delete parent->right;  // Διαγράφεται ο κόμβος
            parent->right= nullptr;  // Ορίζεται το δεξί παιδί του κόμβου σε null
        }
    }
    else if (child->left == nullptr || child->right == nullptr )  // Αν ο κόμβος child έχει ένα παιδί!!
    {
        if (parent==nullptr)                  // Αν δεν έχει γονιό (δηλαδή είναι η ρίζα του δέντρου με ένα παιδί)
        {
            if (child->left == nullptr)       // Αν ο κόμβος child δεν έχει αριστερό παιδί (επομένως έχει δεξί παιδί)
                root = child->right;          // Ορίζεται ως ρίζα το δεξί παιδί του κόμβου child
            else                              // Αν ο κόμβος child δεν έχει δεξί παιδί(επομένως έχει αριστερό παιδί)
                root = child->left;           // Ορίζεται ως ρίζα το αριστερό παιδί του κόμβου child
            delete (child);                   // Διαγράφεται ο κόμβος child
        }
        else if (parent->left == child)       // Αν ο κόμβος child είναι το αριστερό παιδί του γονιού του
        {
            if (child->left == nullptr)       // Αν ο κόμβος child δεν έχει αριστερό παιδί(επομένως έχει δεξί παιδί)
                parent->left = child->right;  // Ορίζεται ως αριστερό παιδί του γονιού του child το δεξί παιδί του child
            else                              // Αν ο κόμβος child δεν έχει δεξί παιδί(επομένως έχει αριστερό παιδί)
                parent->left = child->left;   // Ορίζεται ως αριστερό παιδί του γονιού του child το αριστερό παιδί του child
            delete (child);                   // Διαγράφεται ο κόμβος child
        }
        else                                  // Αν ο κόμβος child είναι το δεξί παιδί του γονιού του
        {
            if (child->left == nullptr)       // Αν ο κόμβος child δεν έχει αριστερό παιδί(επομένως έχει δεξί παιδί)
                parent->right = child->right; // Ορίζεται ως δεξί παιδί του γονιού του child το δεξί παιδί του child
            else                              // Αν ο κόμβος child δεν έχει δεξί παιδί(επομένως έχει αριστερό παιδί)
                parent->right = child->left;  // Ορίζεται ως δεξί παιδί του γονιού του child το αριστερό παιδί του child
            delete (child);                   // Διαγράφεται ο κόμβος child

        }
    }
    else // Αν ο κόμβος child έχει 2 παιδία!!
    {
        node *minimum_node  = min(child->right);  //Βρίσκουμε τον ελάχιστο κόμβο του δεξιού υποδέντρου του κόμβου child με την private min() διαδικασία
        child->data.value = minimum_node->data.value;  // Η συμβολοσειρά που έχει αποθηκευμένη ο ελάχιστος κόμβος εκχωρείται στον κόμβο child
        child->data.times = minimum_node->data.times;  //Οι φορές εμφάνισης της συμβολοσειράς του ελάχιστου κόμβου εκχωρούνται στο κόμβο child
        remove(minimum_node);  //Οι παραπάνω εκχωρήσεις έγιναν ώστε να διαγραφεί ο ελάχιστος κόμβος με την αναδρομική κλήση της remove
    }
    return true;
}

node* BST::min(node* start)  // Private διαδικασία που βρίσκει τον ελάχιστο κόμβο κάποιου υποδέντρου με ρίζα start
{
    if (start == nullptr) // Αν το δέντρο είναι κενό δεν μπορεί να πραγματοποιηθεί η λειτουργία και επιστρέφεται nullptr
        return nullptr;

    node* p = start;  // Δημιουργία κόμβου p στον οποίο εκχωρούνται οι ιδιότητες του κόμβου start(ρίζα στο υποδέντρο που θέλουμε να βρούμε min)

    while (p->left!=nullptr)  // Όσο υπάρχουν στοιχεία στο δέντρο
        p = p->left;  // Εκχωρούμε στο p το αριστερό παιδί της ρίζας και η διαδικασία επαναλαμβάνεται μέχρι να φτάσει στο τέρμα αριστερό παιδί (min)
    return p;  // Επιστρέφεται ο κόμβος p
}