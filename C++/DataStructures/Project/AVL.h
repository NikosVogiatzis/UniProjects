#pragma once

#include <iostream>
#include <string>
#include <cmath>
#include "node.h"

using namespace std;

class AVL
{
private:
    node* root;  // Ρίζα του δέντρου AVL
    node* perform_rotations(node*, string word, long long int);  // Συνάρτηση που πραγματοποιεί τις απαραίτητες περιστροφές
    node* rebalance(node* &, string);  // Συνάρτηση που μας επιτρέπει να διαμορφώνουμε το δέντρο σε ισοζυγισμένο
    node* remove(node*, string);  // Συνάρτηση που μας επιτρέπει να διαγράψουμε κάποιο κόμβο απο το δέντρο
    node* min(node*);  // Εύρεση και επιστροφή ελάχιστου κόμβου
    node* RightRotation(node*);  // Δεξιά περιστροφή
    node* LeftRotation(node*);  // Αριστερή περιστροφή
    node* insert(node* , const string& );
    node* search(string);
    
    int max(int, int);
    int get_height(node*);
    
    void destroytree(node*);  // Χρησιμοποιείται στο destructor για να διαγραφεί το δέντρο
    void inOrder (node*, string);
    void preOrder(node*, string);
    void postOrder(node*, string);
public:
    ~AVL();                                         // Καταστροφέας του δέντρου(διαγράφει κάθε κόμβο του δέντρου)
    AVL();                                          // Κενός κατασκευαστής του δέντρου(θέτει τη ρίζα σε nullptr)

    void printInOrder (string);                     // Ενδοδιατεταγμένη διάσχιση
    void printPreOrder(string);                     // Προδιατεταγμένη διάσχιση
    void printPostOrder(string);                    // Μεταδιατεταγμένη διάσχιση

    bool Insert(string word);                       // Εισαγωγή στο απλό δυαδικό δέντρο αναζήτησης
    bool search(string word, long long int&);       // Αναζήτηση κόμβου
    bool remove(string word);                       // Διαγραφή κόμβου       
};

AVL::AVL()
{
    root = nullptr;
}

AVL::~AVL()  //Καταστροφέας του AVL που χρησιμοποιεί τη destroytree(root)
{
    destroytree(root);
}

void AVL::destroytree(node* root)  // Συνάρτηση που διαγράφει το AVL δέντρο από το σωρό
{
    if (root != nullptr)  // Όσο υπάρχουν κόμβοι στο δέντρο
    {
        destroytree(root->left);  // Διαγράφουμε το αριστερό υποδέντρο καλώντας τη destroytree αναδρομικά
        destroytree(root->right);  // Διαγράφουμε το δεξί υποδέντρο καλώντας τη destroytree αναδρομικά

        delete (root);
    }
}

int AVL::get_height(node* root)  // Συνάρτηση που μας επιτρέπει να λάβουμε το ύψος του υποδέντρου ρίζας root στο σημείο κλήσης
{
    // Αν ο κόμβος root του οποίου θέλουμε να βρούμε είναι nullptr επιστρέφουμε 0
    if (root == nullptr)
        return 0;

    return root->height;  // Διαφορετικά επιστρέφουμε το ύψος του δέντρου ρίζας root
}


int AVL::max(int height1, int height2)  // Συνάρτηση που χρησιμοποιείται για την εύρεση του μέγιστου μεταξύ δύο υψών (χρησιμοποιείται στις συναρτήσεις που υλοποιούν περιστροφές)
{
    return((height1 > height2) ? height1 : height2);
}


node* AVL::RightRotation(node* root)  // Συνάρτηση που μας επιτρέπει να πραγματοποιήσουμε δεξιά περιστροφή
{
    node* new_root = root->left;  // Δημιουργείται κόμβος (νέα ρίζα) και παίρνει τις ιδιότητες του αριστερού παιδιού της root που θέλουμε να ισοζυγίσουμε
    node* child = new_root->right;  // Δημιουργείται κόμβος (παιδί) που παίρνει τις ιδιότητες του δεξιού παιδιού της νέας ρίζας

    new_root->right = root;  // Το δεξί παιδί της νέας ρίζας παίρνει τις ιδιότητες της ρίζας root
    root->left = child;  // Το αριστερό παιδί της ρίζας root παίρνει τις ιδιότητες του παιδιού

    // Ανανεώνονται τα ύψη για τους δύο κόμβους root και new_root αφού πραγματοποιήθηκε η δεξιά περιστροφή
    root->height = max(get_height(root->left), get_height(root->right)) + 1;
    new_root->height = max(get_height(new_root->left), get_height(new_root->right)) + 1;

    return new_root;  // Επιστρέφεται η νέα ρίζα στο σημείο κλήσης της RightRotation
}


node* AVL::LeftRotation(node* root)  // Συνάρτηση που μας επιτρέπει να πραγματοποιήσουμε αριστερή περιστροφή
{
    node* new_root = root->right;  // Δημιουργείται κόμβος (νέα ρίζα) και παίρνει τις ιδιότητες του δεξιού παιδιού της root που θέλουμε να ισοζυγίσουμε
    node* child = new_root->left;  // Δημιουργείται κόμβος (παιδί) που παίρνει τις ιδιότητες του αριστερού παιδιού της νέας ρίζας


    new_root->left = root;  // Το αριστερό παιδί της νέας ρίζας παίρνει τις ιδιότητες της ρίζας root
    root->right = child;  // Το δεξί παιδί της ρίζας root παίρνει τις ιδιότητες του παιδιού

    // Ανανεώνονται τα ύψη για τους δύο κόμβους root και new_root αφού πραγματοποιήθηκε η αριστερή περιστροφή
    root->height = max(get_height(root->left), get_height(root->right)) + 1;
    new_root->height = max(get_height(new_root->left), get_height(new_root->right)) + 1;
    return new_root;  // Επιστρέφεται η νέα ρίζα στο σημείο κλήσης της LeftRotation
}

node* AVL::perform_rotations(node* root, string word, long long int Height_difference)  // Συνάρτηση που ελέγχει ποια ή ποιες περιστροφές είναι απαραίτητες
{
    // !Διευκρίνιση: Η word είναι λέξη του νέου κόμβου που εισάγαμε
    // Αν η διαφορά ύψους των υποδέντρων είναι μεγαλύτερη του ένα τότε το αριστερό υποδέντρο είναι μεγαλύτερο απο το δεξί
    if (Height_difference> 1 && word < root->left->data.value)  // Αν η word είναι μικρότερη της λέξης του αριστερόυ παιδιού του root
        return  RightRotation(root);  // Εκτελούμε δεξιά περιστροφή για τη ρίζα root και επιστρέφουμε την ανανεωμένη τιμή της

    if (Height_difference > 1 && word > root->left->data.value) // Αν η word είναι μεγαλύτερη της λέξης του αριστερού παιδιού(left-right-rotation)
    {
        root->left=LeftRotation(root->left);  // Εκτελούμε αριστερή περιστροφή για το υποδέντρο με ρίζα root->left (ανανεώνοντας την τιμή του)
        return RightRotation(root);  // Εκτελούμε άλλη μία περιστροφή αυτή τη φορα δεξιά για τη ρίζα root και επιστρέφουμε την ανανεωμένη τιμή της
    }

    // Αν η διαφορά ύψους των υποδέντρων είναι μικρότερη του μείον ένα τότε το δεξί υποδέντρο είναι μεγαλύτερο απο το αριστερό
    if (Height_difference < -1 && word > root->right->data.value)  // Αν η word είναι μεγαλύτερη της λέξης του δεξιού παιδιού του root
        return  LeftRotation(root);  // Εκτελούμε αριστερή περιστροφή για τη ρίζα root και επιστρέφουμε την ανανεωμένη τιμή της

    if (Height_difference < -1 && word < root->right->data.value)  // Αν η word είναι μικρότερη της λέξης του αριστερού παιδιού(right-left-rotation)
    {
        root->right=RightRotation(root->right);  // Εκτελούμε δεξιά περιστροφή για το υποδέντρο με ρίζα root->right (ανανεώνοντας την τιμή του)
        return  LeftRotation(root);  // Εκτελούμε άλλη μία περιστροφή αυτή τη φορα αριστερή για τη ρίζα root και επιστρέφουμε την ανανεωμένη τιμή της
    }

    return root;
}


node* AVL::rebalance(node* &root,string word)
{
    // Αρχικά ανανεώνεται το ύψος της ρίζας του κόμβου (που εισάγαμε ή διαγράψαμε)
    root->height = 1 + max(get_height(root->left), get_height(root->right));

    long long int Height_difference;  // Μεταβλητή που μας δείχνει τη διαφορά του αριστερού από το δεξιό υποδέντρο για κάποια ρίζα
    if(root->left == nullptr && root->right==nullptr)  // Αν η ρίζα που ελέγχουμε δεν έχει παιδία (πχ αν είναι φύλλο σε κάποιο υποδέντρο)
        Height_difference = 0;  // Η διαφορά του ύψους είναι 0 αφού δεν υπάρχουν υποδέντρα
    else  // Διαφορετικά βρίσκουμε τη διαφορά του ύψους του αριστερού απο το δεξί υποδέντρο
        Height_difference = (get_height(root->left) - get_height(root->right));

    if(abs(Height_difference) > 1)  // Αν η απόλυτη τιμή της διαφοράς αυτής είναι μεγαλύτερη του ένα το δέντρο δεν είναι ισοζυγισμένο
    {
        //Aφου γίνουν οι κατάλληλες περιστροφές και η ρίζα του υποδέντρου αλλάξει
        //ανανεώνεται η τιμή της και επιστρέφεται αναφορικά στο σημείο κλήσης της

        root = perform_rotations(root,word,Height_difference);
        return root;  // Επιστρέφουμε αυτόν τον κόμβο στο σημείο κλήσης της rebalance
    }

    return root;  // Αν η απόλυτη τιμή της διαφοράς ύψους δεν είναι μεγαλύτερη του ένα επιστρέφουμε το root στην αρχική του μορφή αφού δεν είχαμε περιστροφές
}

bool AVL::Insert(string word)
{
    // Ενημερώνεται η ρίζα του δέντρου με νέα τιμή αν το δέντρο είναι
    // κενό και εισάγουμε τη ρίζα του ή όταν προκύψει μη-ισοζυγισμένο
    // δέντρο ενδεχομένως να αλλάξει η ρίζα έπειτα απο τον ισοζυγισμό
    // του

    root = insert(root,word);
    if(root == nullptr)
        return false;
    else
        return true;
}
node* AVL::insert(node* root, const string &word)
{
    if(root == nullptr)  // Αν ο κόμβος που ελέγχουμε είναι κενός τότε δημιουργούμε κόμβο με τη λέξη word και times 1
    {
        root = new node(word);
        return root;
    }

    if (word == root->data.value)  // Αν ο κόμβος που ελέγχουμε έχει αποθηκευμένη λέξη ίδια με αυτή που θέλουμε να εισάγουμε αυξάνουμε τη μεταβλητη times κατά ένα
    {
        root->data.times++;
        return root;
    }

    if (word > root->data.value)  // Αν η λέξη προς εισαγωγή είναι μεγαλύτερη της λέξης της ρίζας που εξετάζουμε ανατρέχουμε στο δεξί υποδέντρο
        root->right = insert (root->right,word);  // Επαναλαμβάνουμε αναδρομικά για το δεξί παιδί της root
    else
        root->left = insert (root->left,word);  // Επαναλαμβάνουμε αναδρομικά για το αριστερό παιδί της root

    // Αφού έχει ολοκληρωθεί η εισαγωγή του νέου κόμβου πρέπει να ελεγχθεί αν ολα τα υποδέντρα παιρνοντας αναδρομικα
    // τους προγονους του νεου κομβου φτάνοντας ως τη ρίζα του AVL είναι ισοζυγισμενα (αν οχι ισοζυγιζονται)

    return rebalance(root,word);  // Επιστρέφεται η ρίζα του AVL που προέκυψε απο τις περιστροφές
}

void AVL::printInOrder(string file_name)  // Διαδικασία ενδοδιατεταγμένης διάσχισης
{
    ofstream file;
    file.open(file_name,ios::app);
    file << "AVL In order print" << endl << endl;
    inOrder (root,file_name);  // Κλήση της private inOrder
    file << endl;
}


void AVL::inOrder (node* p, string file_name)
{
    if (p == nullptr)  // Αν δεν έχουμε να ελέγξουμε άλλους κόμβους στο δέντρο τερματίζει
        return;

    inOrder (p->left,file_name);  // Αναδρομική κλήση για το αριστερό υποδέντρο/
    ofstream file;
    file.open(file_name,ios::app);  // Άνοιγμα αρχείου με όνομα file_name ώστε να γραφτούν μέσα οι κόμβοι σε ενδοδιατεταγμένη σειρά
    file <<"string: "<< p->data.value << ", times_existed: " << p->data.times<<"!"<<endl;
    inOrder (p->right,file_name);  // Αναδρομική κλήση για το δεξί υποδέντρο
}

void AVL::printPreOrder(string file_name)  //Διαδικασία προδιατεταγμένης διάσχισης
{
    if(root == nullptr)
        return;

    ofstream file;
    file.open(file_name,ios::app);
    file << "AVL Pre order print" << endl << endl;
    preOrder (root,file_name);  // Κλήση της private preOrder
    file << endl ;
}


void AVL::preOrder (node *p,string file_name)
{
    if (p == nullptr)  // Αν δεν έχουμε να ελέγξουμε άλλους κόμβους στο δέντρο τερματίζει
        return;

    ofstream file;
    file.open(file_name,ios::app);  // Άνοιγμα αρχείου με όνομα file_name ώστε να γραφτούν μέσα οι κόμβοι σε προδιατεταγμένη σειρά
    file <<"string: "<< p->data.value << ", times_existed: " << p->data.times<<"!"<<endl;

    preOrder (p->left,file_name);  // Αναδρομική κλήση για το αριστερό υποδέντρο
    preOrder (p->right,file_name);  // Αναδρομική κλήση για το δεξί υποδέντρο

}

void AVL::printPostOrder(string file_name)  // Διαδικασία μεταδιατεταγμένης διάσχισης
{
    ofstream file;
    file.open(file_name,ios::app);
    file << "AVL Post order print" << endl << endl;
    postOrder (root,file_name);  // Κλήση της private postOrder
    file << endl ;
}


void AVL::postOrder (node* p, string file_name)
{
    if (p == nullptr)  // Αν δεν έχουμε να ελέγξουμε άλλους κόμβους στο δέντρο τερματίζει
        return;

    postOrder (p->left,file_name);  // Αναδρομική κλήση για το αριστερό υποδέντρο
    postOrder (p->right,file_name);  // Αναδρομική κλήση για το δεξί υποδέντρο

    ofstream file;
    file.open(file_name, ios::app);  // Άνοιγμα αρχείου με όνομα file_name ώστε να γραφτούν μέσα οι κόμβοι σε μεταδιατεταγμένη σειρά
    file <<"string: "<< p->data.value << ", times_existed: " << p->data.times<<"!"<<endl;
}


bool AVL::search(string word, long long int& times_existed)  // Η διαδικασία της search είναι η ίδια με αυτην στο απλό δυαδικό δέντρο αναζήτησης.
{
    node* p = search(word);

    if (p == nullptr)
    {
        times_existed = 0;
        return false;
    }

    times_existed= p->data.times;
    return true;
}


node* AVL::search(string word)
{
    node* p;
    p = root;

    while (p != nullptr && word != p->data.value)
    {
        if (word > p->data.value)
            p = p->right;
        else
            p = p->left;
    }

    return p;
}

node* AVL::min(node* start)  // Private διαδικασία που βρίσκει τον ελάχιστο κόμβο κάποιου υποδέντρου με ρίζα start
{
    if (start == nullptr)  // Αν το δέντρο είναι κενό δεν μπορεί να πραγματοποιηθεί η λειτουργία και επιστρέφεται nullptr
        return nullptr;

    node* p = start;  // Δημιουργία κόμβου p στον οποίο εκχωρούνται οι ιδιότητες του κόμβου start(ρίζα στο υποδέντρο που θέλουμε να βρούμε min)

    while (p->left!=nullptr)  // Όσο υπάρχουν στοιχεία στο δέντρο
        p = p->left;  // Εκχωρούμε στο p το αριστερό παιδί της ρίζας και η διαδικασία επαναλαμβάνεται μέχρι να φτάσει στο τέρμα αριστερό παιδί(min)
    return p;  // Επιστρέφεται ο κόμβος p
}

bool AVL::remove(string word)  // Διαγραφή κόμβου απο το δέντρο BST
{
    root=remove (root,word);  // Η ρίζα του δέντρου θα αλλάξει και θα πάρει τιμή από τη remove

    if(root != nullptr)
        return true;
    else
        return false;
}

node* AVL::remove(node* root, string word)  // Private διαδικασία που αφαιρεί τον κόμβο με τη λέξη word από το δέντρο
{
    if (root == nullptr)  // Αν το δέντρο είναι εξαρχής κενό επιστρέφουμε τη ρίζα που είναι nullptr
        return root;


    if (word < root->data.value)  // Αν η λέξη είναι μικρότερη απο αυτή της root
        root->left = remove(root->left, word);  // Επαναλαμβάνεται αναδρομικά η διαδικασία για το αριστερό υποδέντρο


    else if(word > root->data.value)  // Αν η λέξη είναι μεγαλύτερη απο αυτή της root
        root->right = remove(root->right, word);  // Επαναλαμβάνεται αναδρομικά η διαδικασία για το δεξί υποδέντρο


    if(word == root->data.value)  // Αν η τιμή του κόμβου που ελέγχουμε(βάσει της λέξης) είναι ίδια με τη λέξη word
    {
        if(root->left == nullptr && root->right != nullptr)  //Ελέγχουμε αν ο κόμβος root έχει μόνο δεξί παιδί
        {
            node* temp = root->right;  // Αντιγράφουμε σε νέο προσωρινό κόμβο τις ιδιότητες του δεξιού παιδιού του root
            *root = *temp;  // Αναθέτουμε τις ιδιότητες του temp στο root(το δεξί παιδί έγινε ρίζα και η ρίζα διαγράφεται)
            delete(temp);
        }
        if(root->right == nullptr && root->left != nullptr) // Ελέγχουμε αν ο κόμβος root έχει μόνο αριστερό παιδί
        {
            node* temp = root->left;  // Αντιγράφουμε σε νέο προσωρινό κόμβο τις ιδιότητες του αριστερού παιδιού του root
            *root = *temp;  // Αναθέτουμε τις ιδιότητες του temp στο root(το δεξί παιδί έγινε ρίζα και η ρίζα διαγράφεται)
            delete(temp);
        }
        if(root->left == nullptr && root->right == nullptr)  // Αν ο κόμβος δεν έχει καθόλου παιδιά(είναι φύλλο) θέτουμε τον κόμβο σε nullptr
            root = nullptr;

        else  // Αν ο κόμβος που ελέγχουμε έχει 2 παιδία τοτε,
        {
            node* temp = min(root->right);  //Βρίσκουμε τον κόμβο με τη μικρότερη τιμή στο δεξί υποδέντρο του root
            root->data.value = temp->data.value;  //Αντιγράφουμε τα στοιχεία του ελάχιστου κόμβου στη ρίζα του υποδέντρου που ελέγχουμε
            root->data.times = temp->data.times;

            root->right = remove(root->right, temp->data.value);  // Επαναλαμβάνουμε αναδρομικά την delete για να διαγράψουμε τον ελάχιστο κόμβο του δεξιού υποδέντρου ρίζας root
        }
    }

    if (root == nullptr)  // Αν το δέντρο είχε μόνο ρίζα και πλέον διαγράφηκε(δηλαδή έχουμε κενό δέντρο) επιστρέφουμε τη ρίζα χωρίς να γίνει επαναζυγισμός του δέντρου αφού είναι κενό
        return root;

    return rebalance(root,word);  // Σε περίπτωση που το δέντρο έχει ακόμη κόμβους μετά τη διαγραφή το ισοζυγίζουμε αν χρειαστεί
}

