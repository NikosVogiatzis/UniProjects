import math as m


# Η Συνάρτηση αυτή επιστρέφει τη f(x) για δοθέν x
def f(x):
    return m.exp(m.pow(m.sin(x), 3)) + m.pow(
        x, 6) - 2 * m.pow(x, 4) - m.pow(x, 3) - 1


# Η συνάρτηση αυτή επιστρέφει την τιμή της (παραγώγου)f'(x) για δοθέν x
def derivative_f(x):
    return 3 * m.exp(m.pow(m.sin(x), 3)) * m.pow(m.sin(x), 2) * m.cos(x) + \
           6 * m.pow(x, 5) - 8 * m.pow(x, 3) - 3 * m.pow(x, 2) - 1


# Η συνάρτηση αυτή επιστρέφει την τιμή της (δεύτερης παραγώγου)f''(x) για δοθέν x
def secondDerivativeF(x):
    -3 * m.exp(m.pow(m.sin(x), 3)) * m.pow(m.sin(x), 3) + 6 * m.exp(m.pow(m.sin(x), 3)) * m.pow(m.cos(x), 2) * m.sin(x) \
    + 9 * m.exp(m.pow(m.sin(x), 3)) * m.pow(m.cos(x), 2) * m.pow(m.sin(x), 4) + 30 * m.pow(x, 4) \
    - 24 * m.pow(x, 2) - 6 * x


'''
    Η συνάρτηση αυτή υλοποιεί τη μέθοδο της διχοτόμησης. Δέχεται ως ορίσματα
    την αρχή και το πέρας του διαστήματος που ψάχνουμε ρίζα (a,b) και μία τρίτη παράμετρο
    που δηλώνει τις επαναλήψεις που θα χρειαστούν για την εύρεση ρίζας με την επιθυμητή
    ακρίβεια των πέντε δεκαδικών ψηφίων. Διατρέχει επανάληψη n φορές μέσα στην οποία υπολογίζεται 
    το μέσο του διαστήματος (m = (a+b)/2). Αν αυτό είναι ρίζα η επανάληψη σταματά,
    διαφορετικά με κατάλληλους ελέγχους ορίζεται εκ νέου το διάνυσμα εξέτασης ρίζας.
    Επιστρέφεται η προσέγγιση της ρίζας μετά απο n επαναλήψεις
'''


def bisectionMethod(x, y, n):
    root = 0
    for i in range(m.floor(n) + 1):
        root = (x + y) / 2

        if f(root) == 0.0:
            break

        if f(root) * f(x) < 0:
            y = root
        else:
            x = root
    return root


def newtonraphson(x):
    counter = 0  # Μετρητής επαναλήψεων που απαιτούνται για την εύρεση της ρίζας
    error = f(x) / derivative_f(x)  # Ορισμός του σφάλματος
    while abs(error) >= 0.000005:  # όσο το σφάλμα είναι μικρότερο της ακρίβειας των πέντε δεκαδικών
        counter += 1
        error = f(x) / derivative_f(x)  # Αρχικοποίηση νέου σφάλματος
        x = x - error  # Αλλαγή του x για το οποίο ελέγχεται Αν f(x) είναι ρίζα

    return x, counter


# Συνάρτηση υλοποίησης της μέθοδος της τέμνουσας
def secant(x, y):
    c = 0
    repetitions = 0  # Μετρητής επαναλήψεων

    condition = True
    while condition:
        if f(x) == f(y):
            print('Root found')
            break

        error = (y - x) * f(x) / (f(y) - f(x))
        c = x - error
        x = y
        y = c
        repetitions = repetitions + 1
        condition = abs(error) >= 0.00001

    return c, repetitions


# Από τη γραφική παράσταση βλέπουμε ότι για α = -2 , b = -0.5 ,f(a) > 0
# f(b) < 0 άρα f(a)*f(b) < 0
a = -2
b = -0.5

print("\t\tΕρώτημα 1ο: Μέθοδος διχοτόμησης!")
# Υπολογισμός μέγιστου αριθμού επαναλήψεων που θα χρειαστεί η μέθοδος της διχοτόμησης.
iterations = (m.log10(b - a) - m.log10(0.00001)) / m.log10(2)
print("Οι επαναλήψεις που θα χρειαστούν είναι: ", m.floor(iterations) + 1)

# Αντίστοιχα με τον τρόπο που διαλέξαμε πριν τα διαστήματα για την πρώτη ρίζα επιλέγουμε το a = 0.5 , b = 2
root1 = bisectionMethod(a, b, iterations)
print("\tΠρώτη ρίζα στο διάστημα [-2,-0.5]: --> root: %.5f" % root1)

a = 0.5
b = 2
# Υπολογισμός μέγιστου αριθμού επαναλήψεων που θα χρειαστεί η μέθοδος της διχοτόμησης.
iterations = (m.log10(b - a) - m.log10(0.00001)) / m.log10(2)
print("Οι επαναλήψεις που θα χρειαστούν είναι: ", m.floor(iterations) + 1)

root2 = bisectionMethod(a, b, iterations)  # παίρνουμε ως άκρα τα [-0.5, -2]
print("\tΔεύτερη ρίζα στο διάστημα [0.5,2]: --> root: %.5f" % root2)

print("\n")
print("\t\tΕρώτημα 2ο: Μέθοδος Newthon-Raphson")
# Επιλογή του αρχικού σημείου x0 = -2 , διότι f(-2)*f''(-2) > 0
x0 = -2

firstRoot, counter1 = newtonraphson(x0)
print("Πρώτη ρίζα με αρχική εκτίμηση -2: --> root: %.5f" % firstRoot, "σε ", counter1, " επαναλήψεις")

x0 = 2  # Επιλογή του αρχικού σημείου x0 = 2 , διότι f(2)*f''(2) > 0
secondRoot, counter2 = newtonraphson(2)
print("Δεύτερη ρίζα με αρχική εκτίμηση 2: --> root: %.5f" % secondRoot, "σε ", counter2, "επαναλήψεις")

print("\n")
print("\t\tΕρώτημα 3ο: Μέθοδος τέμνουσας")
# Αρχικά σημεία για υπολογισμό 1ης ρίζας
x0 = -1.5
x1 = -1

firstRoot, counter1 = secant(x0, x1)
print("Πρώτη ρίζα με αρχικές εκτιμήσεις x0=-1.5, x1=-1 --> root: %.5f" % firstRoot, "σε ", counter1, " επαναλήψεις")

# Αρχικά σημεία για υπολογισμό 2ης ρίζας
x0 = 1.2
x1 = 2
secondRoot, counter2 = secant(x0, x1)
print("Δεύτερη ρίζα με αρχικές εκτιμήσεις x0=1.2, x1=2 --> root: %.5f" % secondRoot, "σε ", counter2, " επαναλήψεις")

print(derivative_f(-1.19762))
print(derivative_f(1.53013))
