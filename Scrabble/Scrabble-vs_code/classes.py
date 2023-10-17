# This python file will host all classes and their operations
import json
import random
import itertools

# Ορισμός μίας λίστας που χρησιμοποιείται κατά την κλήρωση γραμμάτων.
greek_alphabet = ['Α', 'Β', 'Γ', 'Δ', 'Ε', 'Ζ', 'Η', 'Θ', 'Ι', 'Κ', 'Λ', 'Μ', 'Ν', 'Ξ', 'Ο', 'Π' ,\
                    'Ρ', 'Σ', 'Τ', 'Υ', 'Φ', 'Χ', 'Ψ', 'Ω']


def case_end(letters_available, player1_letters, player2_letters, max_letters_per_player):
    # Check if the number of available letters is zero
    if letters_available == 0:
        # Check if both players have a full hand
        if len(player1_letters) != max_letters_per_player and len(player2_letters) != max_letters_per_player:
            return True

    return False

def ShowLettersValue(Player_object, Sak_Object):
    """ 
        Η μέθοδος αυτή είναι βοηθητική και χρησιμοποιείται για να εκτυπώνονται
        τα γράμματα που έχει διαθέσιμα ο κάθε παίκτης μαζί με την αξία τους!
    """

    letter_value = []
    for i in  Player_object.letters:
        for j in Sak_Object.letters_dict:
            if i==j :
                letter_value.append(Sak_Object.letters_dict[j][1])
    letter_list = []
    for i, letter in enumerate(Player_object.letters):
        letter_list.append((letter, letter_value[i]))

    string_list = "["
    for letter, score in letter_list:
        string_list += f"('{letter}',{score}), "
    string_list = string_list[:-2] + "]"    
    return string_list  


class Game:
    def __init__(self):
        self.turn = 1
        self.sak_object = SakClass()
        self.human_player_obj = Human()
        self.computer_player_obj = Computer()
        self.round = 0
        self.called_from_inside = False

    def __repr__(self):
        return 'Game Instance'

    def setup(self):
        """ 
            Η μέθοδος αυτή ορίζει το μενού που εμφανίζεται κατά την εκκίνηση της εφαρμογής"""
        if not self.called_from_inside:
            self.sak_object.randomize_sak()
            self.sak_object.generate_words()
            print("\t\t----- Welcome to Scrabble!!! -----\n")

            self.human_player_obj.set_name(input("Type in your username: "))
            print("\n\t*** " + self.human_player_obj.name + " Επέλεξε ένα από τα παρακάτω!***\n")

        # Λούπα ελέγχου για ορθή καταχώριση τιμών!
        while True:
            print("1: Play game")
            print("2: Settings")
            print("3: Score Statistics")
            print("q: Exit\n")
            input_from_user = input()
            if input_from_user == '1' or input_from_user == '2' or input_from_user == '3' or input_from_user == 'q':
                break
            else:
                print("Πρέπει να εισάγεις '1', '2, '3', ή 'q' βάσει των επιλογών σου!\n")

        if input_from_user == '1':  # Ξεκινά νέα παρτίδα
            print("\t ----------> Have fun! <----------")
            self.run()

        elif input_from_user == '2':  # Ρυθμίσεις -> Εξηγεί με ποιον αλγόριθμο παίζει ο υπολογιστής
            print("Ο υπολογιστής παίζει σύμφωνα με τον αλγόριθμο Smart-Teach\n")
            self.called_from_inside = True
            self.setup()
        elif input_from_user == '3':  # Εμφάνιση προηγούμενων Στατιστικών
            print("Στατιστικά από προηγούμενες παρτίδες!\n")
            list_of_statistics = self.sak_object.file_handler_obj.readStatisticsFromFile(self.sak_object)
            for i in list_of_statistics:
                print(i)
            self.called_from_inside = True
            self.setup()
            # Εδω φορτώνονται τα στατιστικά απο προηγούμενους γύρους
        elif input_from_user == 'q':  # Έξοδος με ελέγχους
            print("\tΕπιβεβαιώστε την έξοδο!\n------------------------------------\n")
            print("1: Έξοδος")
            print("2: Επιστροφή στο μενού")
            answer = input()
            if answer == '1':
                exit(1)
            elif answer == '2':
                self.called_from_inside = True
                self.setup()
            else:
                print("ΠΡΟΣΟΧΗ! Επιλέγετε απο: '1' για έξοδο και '2' για επιστροφή στο μενού\n")
                self.called_from_inside = True
                self.setup()

    def run(self):
        """ 
            Η μέθοδος αυτή καλείται και είναι σε λειτουργία όσο το παιχνίδι μεταξύ ανθρπώπου υπολογιστή παίζεται

        """
        word_human_plays = ""
        end_game = False
        losed_turn = False
        print(" Πραγματοποιείται η κλήρωση των γραμμάτων για τον " +
        self.human_player_obj.name + " και για τον υπολογιστή\n")
        
        # Καλείται η συνάρτηση που κληρώνει 7 γράμματα για τον καθένα. 
        flag_for_randomize = False
        while True:
            res= self.sak_object.get_letters(self.human_player_obj, self.computer_player_obj, 7, 1)
            if not res:
                while True:
                    res2= self.sak_object.get_letters(self.human_player_obj, self.computer_player_obj, 7, 2)
                    if not res2:
                        flag_for_randomize = True
                        break
                if flag_for_randomize:
                    break
        # Το αντικείμενο αυτό χρησιμοποιείται ως βοήθεια ώστε να τρέχει ο smart-teach χωρίς να επηρεάζει τις δομές του human_player_obj και computer_player_obj
        player_advisor_letters = Player()
        while not end_game:
            self.round += 1  # Μεταβλητή που σηματοδοτεί την παρτίδα η οποία παίζεται.
            if self.turn == 1:
                while not losed_turn: 
                    player_advisor_letters.letters = self.human_player_obj.letters.copy()
                    print('Στο σακουλάκι ' , self.sak_object.letters_available , ' γράμματα! - Παίζεις')
                    print('Διαθέσιμα Γράμματα: ', ShowLettersValue(self.human_player_obj, self.sak_object))
                    returned_ , length, word_human_plays = self.human_player_obj.play(self.sak_object) # Καλείται η συνάρτηση που υλοποιείται το παίξιμο από τον παίκτη.
                    if returned_ == "end":
                        end_game = True
                        break
                    if returned_ == 'p':
                        print('Χάνεις σειρα! \n')
                        losed_turn = True
                        while True:
                            res= self.sak_object.get_letters(self.human_player_obj, self.computer_player_obj, 7, 1)
                            if not res:
                                break
                    if returned_ == 'error':
                        print('Έλεγξε αν διαθέτεις όλα τα γράμματα από αυτά που τύπωσες')

                    if returned_ == 'Η λέξη δεν υπάρχει':
                        print('\nΚαταχώρησε έγκυρη λέξη!\n')

                    if returned_ == 'ΟΚ':
                        input_ = input('Πάτησε Enter για συνέχεια: ')
                        losed_turn = True
                        while True:
                            res = self.sak_object.get_letters(self.human_player_obj, self.computer_player_obj, length, 1)
                            if not res:
                                break
                # Τρέχει ο Smart-Teach για να δείξει στον παίκτη αν έπαιξε την καλύτερη λέξη κι αν όχι ποια είναι αυτή
                word, score, dump = self.computer_player_obj.play(player_advisor_letters, self.sak_object, True)
                if (word.upper() == res or score == player_advisor_letters.compute_score(word_human_plays, self.sak_object)):
                    print("Συγχαρητήρια έπαιξες την καλύτερη δυνατή λέξη!")
                else:
                    print("Καλύτερη λέξη σύμφωνα με τον υπολογιστή: ", word, " --- Πόντοι: ", score)
                self.turn = 2
                if end_game:
                    break
            if self.turn == 2:
                print('Στο σακουλάκι ' , self.sak_object.letters_available , ' γράμματα! - Παίζει ο υπολογιστής')
                print('Διαθέσιμα Γράμματα: ', ShowLettersValue(self.computer_player_obj, self.sak_object))
                word, score, length2 = self.computer_player_obj.play(self.computer_player_obj, self.sak_object, False)
                if word == 'pass':
                    print('Ο υπολογιστής επέλεξε πάσο!\n')
                else:
                    print('Καλύτερη λέξη: ', word, '\tΣκορ λέξης: ', score, '\n')
                while True:
                    res= self.sak_object.get_letters(self.human_player_obj, self.computer_player_obj, length2, 2)
                    if not res:
                        break
                self.turn = 1
                
                losed_turn = False
            # Αν τελειωσουν τα γραμματα στο σα
            if case_end(self.sak_object.letters_available, self.human_player_obj.letters, self.computer_player_obj.letters, 7):
                end_game = True
                break
        
        self.end()

    def end(self):
        print("Τέλος παιχνιδιού.")
        self.sak_object.file_handler_obj.write_statistics(self.human_player_obj, self.computer_player_obj, self.round)
        


class SakClass:
    letters_available = 102 # Οχι 104 καθως δεν εχουμε μπαλαντερ
    lett_ = []
    def __init__(self):
        self.file_handler_obj = FileHandler()
        self.letters_dict = {}
        self.words_in_dict = {}

    def get_letters(self, Human_object, Computer_object, n, turn):
        """
            Η μέθοδος αυτή βγάζει από το σακουλάκι n γράμματα και τα αναθέτει είτε στον παίκτη-άνθρωπο είτε στον υπολογιστή,
            ανάλογα με το turn που σηματοδοτεί την σειρά του παίκτη για κλήρωση, Συνεπώς όποτε καλείται η συνάρτηση αυτή αναθέτει
            γράμματα σε έναν εκ των 2 παικτών.
        """
     
        if self.letters_available == 0:
            return False
        for i in self.letters_dict:
            if self.letters_dict[i][0] == 0:
                if Human_object.letters.__contains__(i) or Computer_object.letters.__contains__(i):
                    continue
                else:
                    if greek_alphabet.__contains__(i):
                        greek_alphabet.remove(i)

        dump_list = []  
        if (self.letters_available - n) < 0:
            n = self.letters_available
        if turn == 1:  # Κλήρωση γραμμάτων για τον παίκτη-άνθρωπο
            # Σε μία βοηθητική λίστα προστίθενται τα n γράμματα που επιθυμούμε να κληρώσουμε για τον παίκτη.
            dump_list += random.choices(greek_alphabet,k=n)
            # Βοηθητικό λεξικό στο οποίο γίνονται οι έλεγχοι για το ποια γράμματα διαλέχθηκαν από το αλφάβητο
            # και αν είναι εφικτά να δοθούν στον παίκτη ή έχουν τελειώσει 
            new_dict = {} 

            for letter in self.letters_dict:
                count_dict = self.letters_dict[letter][0]
                count_letters = dump_list.count(letter)
                new_dict[letter] = [count_dict, count_letters]
            
            for letter in new_dict:
                if new_dict[letter][0] - new_dict[letter][1] < 0: # Αν κληρώθηκε κάποιο γράμμα το οποίο έχει πλήθος εμφανίσεων 0 τότε επιστρέφεται μήνυμα λάθους
                    return True

            # Αν τα γράμματα είναι valid τότε αφαιρούνται από το σακουλάκι και η λίστα ανατίθεται στη λίστα γραμμάτων του ανθρώπου-παίκτη
            for i in dump_list:
                self.letters_dict[i][0] -=  1
            Human_object.letters += dump_list
            dump_list = []
            self.letters_available -= n # αφαιρεση των n letters από τα συνολικά διαθέσιμα.
    
            return False

        # ΣΗΜΕΙΩΣΗ: Η κλήρωση γραμμάτων για τον υπολογιστή ακολουθεί ακριβώς την ίδια διαδικασία.
        elif turn == 2: # Κλήρωση γραμμάτων για υπολογιστή.
            dump_list += random.choices(greek_alphabet,k=n)
            new_dict = {}

            for letter in self.letters_dict:
                count_dict = self.letters_dict[letter][0]
                count_letters = dump_list.count(letter)
                new_dict[letter] = [count_dict, count_letters]
            
            for letter in new_dict:
                if new_dict[letter][0] - new_dict[letter][1] < 0:
                    return True

            for i in dump_list:
                self.letters_dict[i][0] -=  1 
            Computer_object.letters += dump_list
            dump_list = []
            self.letters_available -= n
            return False

    def put_back_letters(self, letters):
        """
            H συνάρτηση αυτή επιστρέφει γράμματα στο σακουλάκι. Δέχεται ως παράμετρο μία λίστα με γράμματα. Προστίθεται στη μεταβλητή που 
            κρατά τα συνολικά διαθέσιμα γράμματα το μήκος της λίστας και σε μία λούπα, για κάθε γράμμα-κλειδί αυξάνουμε τη δεύτερη τιμή της λίστας value
            από το λεξικό letters_dict 
        """
        self.letters_available += len(letters)
        for i in letters:
            for k in self.words_in_dict:
                if i == k :
                    self.letters_dict[k][0] += 1

    def randomize_sak(self):
        """
            Η συνάρτηση αυτή ορίζει το λεξικό των γραμμάτων. Ως κλειδία χρησιμοποιούνται τα γράμματα της αλφαβήτου, και ως value μία λίστα με 
            το πλήθος διαθέσιμων γραμμάτων στο πρώτο κελί και τους πόντους στο δεύτερο κελί.
        """
        self.letters_dict = {'Α': [12, 1], 'Β': [1, 8], 'Γ': [2, 4], 'Δ': [2, 4], 'Ε': [8, 1], 'Ζ': [1, 10],
                             'Η': [7, 1],
                             'Θ': [1, 10], 'Ι': [8, 1], 'Κ': [4, 2], 'Λ': [3, 3], 'Μ': [3, 3], 'Ν': [6, 1],
                             'Ξ': [1, 10],  
                             'Ο': [9, 1], 'Π': [4, 2], 'Ρ': [5, 2], 'Σ': [7, 1], 'Τ': [8, 1], 'Υ': [4, 2], 'Φ': [1, 8],
                             'Χ': [1, 8], 'Ψ': [1, 10], 'Ω': [3, 3]}
        lets = {'Α':[12,1],'Β':[1,8],'Γ':[2,4],'Δ':[2,4],'Ε':[8,1],
        'Ζ':[1,10],'Η':[7,1],'Θ':[1,10],'Ι':[8,1],'Κ':[4,2],
        'Λ':[3,3],'Μ':[3,3],'Ν':[6,1],'Ξ':[1,10],'Ο':[9,1],
        'Π':[4,2],'Ρ':[5,2],'Σ':[7,1],'Τ':[8,1],'Υ':[4,2],
        'Φ':[1,8],'Χ':[1,8],'Ψ':[1,10],'Ω':[3,3]
        }

    def generate_words(self):
        """
            Η συνάρτηση αυτή καλεί τη βοηθητική συνάρτηση της κλάσης FileHandler ώστε να οριστεί το λεξικό με όλες τις λέξεις του greek7.txt
        """
        self.words_in_dict = self.file_handler_obj.read_all_words()      


class Player:
    def __init__(self):
        """
            Constructor που κάνει initialize στις ιδιότητες της κλάσης. 
        """
        self.letters = []  # list of 7 values max
        self.score = 0
        self.word = ""
        self.passes = 0

    def __repr__(self):
        pass

    def compute_score(self, word, sak_object):
        """ 
            Η μέθοδος αυτή υλοποιεί τον υπολογισμό του σκορ για τη λέξη word. Για κάθε γράμμα στη λέξη (for loop), 
            προστίθεται στο letter_score η τιμή του 2ου κελιού στο λεξικό sak_object.letters_dict
        
        """
        if word.lower() == 'p':
            return 0
        letter_score = 0

        for i in word:
            letter_score += sak_object.letters_dict[i.upper()][1]

        return letter_score
        



class Human(Player):

    def __init__(self):
        """
            Constructor της κλάσης. Καλεί τον super κατασκευαστή της κλάσης Player και ορίζει το όνομα του ανθρώπου σε none
        """
        Player.__init__(self)
        self.quited = False
        self.name = None

    def __repr__(self):
        return 'Human Instance!'

    def play(self, sak_object):
        """ 
            Η μέθοδος αυτή υλοποιεί το "παίξιμο" του παίκτη-ανθρώπου. Ζητά από τον παίκτη να εισάγει μία λέξη κι έπειτα γίνονται οι εξής έλεγχοι:
            1) Αν ο παίκτης πληκτρολόγησε: 'q' τότε επιστρέφεται το αντίστοιχο μήνυμα ώστε να τερματιστεί το παιχνίδι
            2) Αν ο παίκτης πληκτρολόγησε: '' τότε επιστρέφεται το αντίστοιχο μήνυμα και ξανακαλείται η συνάρτηση από την μέθοδο run της κλάσης Game
            3) Αν ο παίκτης πληκτρολόγησε: 'p' τότε επιστρέφονται τα γράμματα του στο σακουλάκι, η λίστα γραμμάτων γίνεται κενή και επιστρέφεται αντίστοιχο μήνυμα
            4) Αν ο παίκτης πλξκτρολόγησε κάποια λέξη τότε ελέγχεται αν τα γράμματα είναι όντως αυτά που διαθέτει και επιστρέφεται μήνυμα λάθους αν δεν.
            5) Αν ο παίκτης εισάγει λέξη με γράμματα τα οποία κατέχει όντως, τότε ελέγχεται αν η λέξη αυτή υπάρχει στο words_in_dict λεξικό. Αν ναι, υπολογίζεται
                    το σκόρ και επιστρέφεται το αντίστοιχο μηνυμα + το μέγεθος της λέξης 
                    -> Αν δεν υπάρχει επιστρέφεται αντίστοιχο μήνυμα  
        
        """
        # Στη μεταβλητή αυτή αποθηκεύεται η λέξη που εισάγει ο παίκτης κατά τη σειρά του.
        self.word = input('ΠΛΗΚΤΡΟΛΟΓΗΣΕ ΛΕΞΗ: \t') 

        # Έλεγχος 1: Τερματισμός 
        if self.word == 'q':
            self.quited = True
            return 'end', -1, ''

        # Έλεγχος 2: Κενή συμβολοσειρά
        if self.word == "":
            return 'error', -1, ''

        count = 0
        #  ΕΛΕΓΧΟΣ 3: p -> πάσο
        if self.word == 'p':
            if sak_object.letters_available == 0: # Αν ο παίκτης πάει να παίξει πάσο ενώ έχουν μείνει 0 γράμματα στο σακουλάκι => παραδόθηκε.
                self.quited = True
                return 'end', -1, ''
            length = len(self.letters)
            sak_object.put_back_letters(self.letters)
            self.letters = []
            self.passes += 1
            return 'p', length, ''


        #  ΕΛΕΓΧΟΣ 4: Αν τα γράμματα είναι όντως από αυτά που έχει διαθέσιμα.
        word_letters = list(self.word.upper())  # Λίστα με τα γράμματα της λέξης
        temp_letters = self.letters.copy()  # Αντίγραφο της λίστας self.letters

        for letter in word_letters:
            if letter in temp_letters:
                temp_letters.remove(letter)
            else:
                return 'error', -1, ''

        #   ΕΛΕΓΧΟΣ 5: Αν τα γράμματα συμπληρώνουν λέξη η οποία να ανήκει στο αρχείο
        first_letter = self.word[0].upper() #Πρώτο γράμμα λέξης (Χρήση για να ψάξω με το κατάλληλο κλειδί στο λεξικό με όλες τις λέξεις )
 
        if self.word.upper() in sak_object.words_in_dict[first_letter]:
            word_score = super().compute_score(self.word, sak_object)  # Βαθμολογία λέξης
            self.score += word_score  # Γενικό σκορ παίκτη
            print('Βαθμολογία Λέξης: - ', word_score, ' --- Συνολικό Σκορ: ', self.score)

            # Αφαίρεση των γραμμάτων από τη λίστα self.letters
            for letter in word_letters:
                self.letters.remove(letter)

            return 'ΟΚ', len(self.word), self.word
   
        return 'Η λέξη δεν υπάρχει', 1, ''


    def set_name(self, name):
        """  Η μέθοδος αυτή ορίζει το όνομα του παίκτη σύμφωνα με την παράμετρο name  """
        self.name = name


class Computer(Player):

    def __init__(self):
        """  Constructor που χρησιμοποιεί τον super κατασκευαστή της κλάσης Player"""
        Player.__init__(self)
        self.words = []

    def __repr__(self):
        return 'Instance of Computer class'

    def play(self, Player_object, sak_object, player_plays):
        """ Η μέθοδος αυτή υλοποιεί τον τρόπο που παίζει ο υπολογιστής. Ο αλγόρθμος που χρησιμοποιείται είναι ο smart-teach. Η μέθοδος αυτή
            καλείται όταν παίζει ο υπολογιστής και γίνονται όλα όπως περιγράφονται στο guidelines της main καθώς και όταν παίζει ο παίκτης όπου υπολογίζεται
            η καλύτερη λέξη αλλά δεν γίνεται κάποια αλλαγή στα δεδομένα και στις δομές του υπολογιστή. """
            
        score = 0
        best_word_score = 0
        best_word = ''
        combinations = []
        for i in range(2,9):
            for permutation in itertools.permutations(Player_object.letters, i):
                word = ''.join(permutation)
                word
                if word in sak_object.words_in_dict[word[0]]:
                    combinations.append(word)

        if len(combinations) > 0:
            for i in combinations:

                score = super().compute_score(i, sak_object)
                if score > best_word_score:
                    best_word_score = score
                    best_word = i
            
            if player_plays == False:
                self.score += best_word_score
                let_ = ""
                for letter in best_word:
                    for let in self.letters:
                        if let == letter:
                            let_ = let
                            break
                    self.letters.remove(let_)

                self.words += best_word
            length = len(best_word)

        elif len(combinations)<=0:
            best_word = 'pass'
            length = len(self.letters)

            if player_plays == False:
                sak_object.put_back_letters(self.letters)

                self.letters = []
            
                self.passes += 1
        return best_word, best_word_score, length
       


class FileHandler:
    """Η κλάση αυτή εκτελεί όλες τις απαραίτητες ενέργειες που απαιτούνται από το 
        πρόγραμμα αναφορικά με τη διαχείρηση αρχείων. Διαθέτει 2 μεθόδους εκ των οποίων η μία διαβάζει τις λέξεις από
        το αρχείο greek7.txt και η άλλη έπειτα από την ολοκλήρωση μίας παρτίδας γράφει στο αρχείο στατιστικών τα απαραίτητα
        δεδομένα (Εξηγούνται στο docstring των μεθόδων)    
    """

    def __init__(self):
        """
            Constructor που ορίζει το λεξικό στο οποίο θα αποθηκευθούν όλες οι λέξεις της ελληνικής
        """
        self.words_in_dict = {}

    def __repr__(self):
        return 'FileHandler instance'

    def read_all_words(self):
        """
            Η μέθοδος αυτή είναι υπεύθυνη για το διάβασμα των λέξεων από το αρχείο greek7.txt και την αποθήκευσή του στο self.words_in_dict λεξικό
            Ανοίγουμε το αρχείο για διάβασμα (με encoding UTF8 για αναγνώριση της ελληνικής), και διαβάζουμε το αρχείο γραμμή γραμμή. Έπειτα καθαρίζουμε
            το \n και σε μία λούπα για κάθε λέξη, ελέγχουμε αν το πρώτο γράμμα της λέξης αποτελεί υπάρχον κλειδί στο λεξικό. Αν ναι τότε απλώς προσθέτουμε
            στην "τιμή" του λεξικού δηλαδή τη λίστα, τη λέξη. Αν δεν αποτελεί υπάρχον κλειδί τότε δημιουργείται αρχικοποιώντας ως value του λεξικού για το 
            συγκεκριμένο κλειδί μία λίστα στην οποία θα μπουν όσες λέξεις ξεκινούν με αυτό το γράμμα.
            -> ΣΗΜΕΙΩΣΗ: Παρατήρησα ότι στο greek7.txt αναγράφονται
            και ώς λέξεις κάποιες με μήκος ένα πχ το ΄Δ΄ οπότε αυτές φιλτράρονται και δεν προστίθενται στο λεξικό μας.
        """

        with open('greek7.txt', 'r', encoding='UTF8') as file:
            words = file.readlines()
            for index, i in enumerate(words):
                words[index] = i.strip('\n')

        for word in words:
            if word[0] not in self.words_in_dict.keys():

                if len(word) == 1:
                    continue

                self.words_in_dict[word[0]] = []
                self.words_in_dict[word[0]].append(word)
            else:
                if word not in self.words_in_dict[word[0]]:
                    self.words_in_dict[word[0]].append(word)
        file.close()
        return self.words_in_dict

    def write_statistics(self, human_player_obj, computer_player_obj, rounds):
        winner_score = 0
        winner = ""
        if human_player_obj.score > computer_player_obj.score:
            winner = human_player_obj.name
            winner_score = human_player_obj.score
            print("Συγχαρητήρια " , human_player_obj.name , " κέρδισες!! με συνολικό σκορ: " , human_player_obj.score)
        elif human_player_obj.score < computer_player_obj.score: 
            winner = 'Computer'
            winner_score = computer_player_obj.score
            print("Κέρδισε ο υπολογιστής! με σκορ: " ,  computer_player_obj.score)
        else: 
            winner = 'None its a draw!'
        quited = "Όχι"
        if human_player_obj.quited: 
            quited = "Ναι!"

        try:
            with open('statistics.json', 'r') as file:
                existing_data = json.load(file)
        except (FileNotFoundError, json.decoder.JSONDecodeError):
            existing_data = []

        data = {
            'Παρτίδες που παίχτηκαν ': rounds,
            'Όνομα Παίχτη ':  human_player_obj.name,
            'Πάσο Παίχτη ': human_player_obj.passes,
            'Παίχτης Παραδόθηκε; ': quited,
            'Πόντοι Παίχτη ': human_player_obj.score,
            'Πόντοι Υπολογιστή ': computer_player_obj.score,
            'Πάσο Υπολογιστή ': computer_player_obj.passes,
            'ΝΙΚΗΤΗΣ! ':  winner 

        }
        existing_data.append(data)
        with open('statistics.json', 'w', encoding='UTF8') as file:
            json.dump(existing_data, file, indent=4)
        
    
    def readStatisticsFromFile(self, sak_object):
    
        try:
            with open('statistics.json', 'r') as file:
                existing_data = json.load(file)
        except (FileNotFoundError, json.decoder.JSONDecodeError):
            existing_data = ["Καμία παρτίδα δεν έχει παιχτεί ακόμη!"]
        return existing_data
    
