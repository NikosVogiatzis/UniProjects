import math as m
import random


# Μέθοδος επιστροφής της τιμής της f(x) για δοθέν x
def f(x):
    return 94 * m.pow(m.cos(x), 3) - 24 * m.cos(x) + 177 * m.pow(m.sin(x), 2) - 108 * m.pow(m.sin(x), 4) - 72 * m.pow(
        m.cos(x), 3) * m.pow(m.sin(x), 2) - 65


# Μέθοδος που επιστρέφει την τιμή της πρώτης παραγώγου της f(x) για δοθέν x
def firstDerivative(x):
    return -282 * m.pow(m.cos(x), 2) * m.sin(x) + 24 * m.sin(x) + 354 * m.sin(x) * m.cos(x) \
           - 432 * m.pow(m.sin(x), 3) * m.cos(x) + 216 * m.pow(m.cos(x), 2) * m.pow(m.sin(x), 3) \
           - 144 * m.pow(m.cos(x), 4) * m.sin(x)


# Μέθοδος που επιστρέφει την τιμή της δεύτερης παραγώγου της f(x) για δοθέν x
def secondDerivative(x):
    return 564 * m.cos(x) * m.pow(m.sin(x), 2) - 282 * m.pow(m.cos(x), 3) + 24 * m.cos(x) \
           + 354 * m.pow(m.cos(x), 2) - 354 * m.pow(m.sin(x), 2) \
           - 1296 * m.pow(m.sin(x), 2) * m.pow(m.cos(x), 2) + 432 * m.pow(m.sin(x), 4) \
           - 432 * m.cos(x) * m.pow(m.sin(x), 3) + 648 * m.pow(m.cos(x), 3) * m.pow(m.sin(x), 2) \
           + 576 * m.pow(m.cos(x), 3) * m.pow(m.sin(x), 2) - 144 * m.pow(m.cos(x), 5)


# Μέθοδος υλοποίησης της μεθόδου διχοτόμησης με τη διαφοροποίηση ότι
# Για μέσο του διαστήματος δεν παίρνεται το (b-a)/2 αλλά ένα τυχαίο σημείο εντός του διαστήματος
def bisectionMethod(left, right):
    rand_middle = 0  # Αρχικοποίηση <<μεσαίου>> σημείου που θα υπολογίζεται τυχαία
    count = 0  # Μετρητής που στο τέλος επιστρέφει το πλήθος των απαιτούμενων επαναλήψεων για την εύρεση της ρίζας
    while abs((right - left)) >= 0.000005:  # Επανάληψη του αλγορίθμου μέχρι το επιτρεπτό σφάλμα
        count += 1

        # Αρχικοποίηση του <<μέσου>> σημείου τυχαία
        rand_middle = random.uniform(left, right)

        # Δια χειρισμός ελέγχων όπως στην κανονική μέθοδο διχοτόμησης (Ασκ-1)
        if f(rand_middle) == 0.0:
            break

        if f(rand_middle) * f(left) < 0:  # έλεγχος για λήψη του επόμενου διαστήματος εξέτασης
            right = rand_middle
        else:
            left = rand_middle

    return rand_middle, count


# Κλασική μέθοδος διχοτόμησης από άσκηση 1
def originalBisectionMethod(x, y, n):
    root_found = 0
    for k in range(m.floor(n) + 1):
        root_found = (x + y) / 2

        if f(root_found) == 0.0:
            break

        if f(root_found) * f(x) < 0:
            y = root_found
        else:
            x = root_found
    return root_found


# Μέθοδος υλοποίησης της τροποποιημένης εκδοχής του αλγορίθμου newton-raphson
def newtonraphson(init):
    xn = init
    counter = 0
    error = 1 / (firstDerivative(xn) / f(xn) - secondDerivative(xn) / (2 * firstDerivative(xn)))
    while abs(error) >= 0.000005:
        counter += 1
        error = 1 / (firstDerivative(xn) / f(xn) - secondDerivative(xn) / (2 * firstDerivative(xn)))

        xn = xn - error
    return xn, counter


# Κλάσικη μέθοδος newton-raphson από άσκηση 1
def originalNewtonraphson(x):
    counter = 0  # Μετρητής επαναλήψεων που απαιτούνται για την εύρεση της ρίζας
    error = f(x) / firstDerivative(x)  # Ορισμός του σφάλματος
    while abs(error) >= 0.000005:  # όσο το σφάλμα είναι μικρότερο της ακρίβειας των πέντε δεκαδικών
        counter += 1
        error = f(x) / firstDerivative(x)  # Αρχικοποίηση νέου σφάλματος
        x = x - error  # Αλλαγή του x για το οποίο ελέγχεται Αν f(x) είναι ρίζα

    return x, counter


def r(x, y):
    return f(y) / f(x)


def q(x, y):
    return f(x) / f(y)


def s(x, y):
    return f(y) / f(x)


def secant(x0, x1, x2):
    x3 = 0
    repetitions = 1
    condition = True
    while condition:
        error = (r(x1, x2) * (r(x1, x2) - q(x0, x1)) * (x2 - x1) + (1 - r(x1, x2)) * s(x0, x2) * (x2 - x0)) / (
                (q(x0, x1) - 1) * (r(x1, x2) - 1) * (s(x0, x2) - 1))
        x3 = x2 - error
        x0 = x1
        x1 = x2
        x2 = x3
        repetitions = repetitions + 1

        condition = abs(error) >= 0.000005
    return x3, repetitions


# Συνάρτηση υλοποίησης της κλασικής μεθόδου της τέμνουσας από άσκηση 1
def originalSecant(x, y):
    c = 0
    repetitions = 0  # Μετρητής επαναλήψεων

    condition = True
    while condition:
        if f(x) == f(y):
            break

        error = (y - x) * f(x) / (f(y) - f(x))
        c = x - error
        x = y
        y = c
        repetitions = repetitions + 1
        condition = abs(error) >= 0.000005

    return c, repetitions


# -------------------->BISECTION<-----------------------
# Οι λούπες που βρίσκονται σε σχόλια χρησιμοποιούνται για την
# επίλυση του ερωτήματος 2

# Επιλογή των άκρων έτσι ώστε a < b && f(a)*f(b) < 0
a = 0
b = 1

print("\t\tΕρώτημα πρώτο: Εύρεση όλων των ριζών με τις τροποποιημένες μεθόδους")

print("\tΤροποποιημένη μέθοδος διχοτόμησης:")
root1, iterations = bisectionMethod(a, b)
print("----->First root: %.5f" % root1, "Found after %d" % iterations, "iterations")

# Επιλογή των άκρων έτσι ώστε a < b && f(a)*f(b) < 0
a = 1
b = 2

root2, iterations = bisectionMethod(a, b)
print("----->Second root: %.5f" % root2, "Found after %d" % iterations, "iterations")

# Επιλογή των άκρων έτσι ώστε a < b && f(a)*f(b) < 0
a = 2
b = 3

root3, iterations = bisectionMethod(a, b)
print("----->Third root: %.5f" % root3, "Found after %d" % iterations, "iterations")

# ------------------>NewtonRaphson<---------------------
print("\n")
print("\tΤροποποιημένη μέθοδος Newton-Raphson")
root, iterations = newtonraphson(0.2)
print("----->First root: %.5f" % root, "Found after %d" % iterations, "iterations")

root, iterations = newtonraphson(1.5)
print("----->Second root: %.5f" % root, "Found after %d" % iterations, "iterations")

root, iterations = newtonraphson(2.5)
print("----->Third root: %.5f" % root, "Found after %d" % iterations, "iterations")

# ------------------>SECANT<---------------------
print("\n")
print("\tΤροποποιημένη μέθοδος τέμνουσας")
x0_init = 0
x1_init = 0.1
x2_init = 0.5
root1, iterations = secant(x0_init, x1_init, x2_init)
print("----->First root: %.5f" % root1, "Found after %d" % iterations, "iterations")

x0_init = 1
x1_init = 1.5
x2_init = 2
root2, iterations = secant(x0_init, x1_init, x2_init)
print("----->Second root: %.5f" % root2, "Found after %d" % iterations, "iterations")
x0_init = 2
x1_init = 2.5
x2_init = 3
root3, iterations = secant(x0_init, x1_init, x2_init)
print("----->Third root: %.5f" % root3, "Found after %d" % iterations, "iterations")

print("\n\n")

print("\t\tΕρώτημα δεύτερο:")

print("\tΓια την πρώτη ρίζα:")
for i in range(10):
    root1, iterations = bisectionMethod(a, b)
    print("Root %.5f" % root1, "found after %d" % iterations, "iterations")

print("\tΓια την δεύτερη ρίζα:")
for i in range(10):
    root2, iterations = bisectionMethod(a, b)
    print("Root %.5f" % root2, "found after %d" % iterations, "iterations")

print("\tΓια την τρίτη ρίζα:")
for i in range(10):
    root3, iterations = bisectionMethod(a, b)
    print("Root %.5f" % root3, "found after %d" % iterations, "iterations")

print("\n")
print("\t\tΕρώτημα τρίτο:")

print("\tΚλασική μέθοδος διχοτόμησης:")

a = 0
b = 0.9
iterations = (m.log10(b - a) - m.log10(0.000005)) / m.log10(2)
root1 = originalBisectionMethod(a, b, iterations)
print("----->First root: %.5f" % root1, "Found after %d" % iterations, "iterations")

# Επιλογή των άκρων έτσι ώστε a < b && f(a)*f(b) < 0
a = 0.9
b = 1.5
iterations = (m.log10(b - a) - m.log10(0.000005)) / m.log10(2)
root2 = originalBisectionMethod(a, b, iterations)
print("----->Second root: %.5f" % root2, "Found after %d" % iterations, "iterations")

# Επιλογή των άκρων έτσι ώστε a < b && f(a)*f(b) < 0
a = 1.5
b = 3
iterations = (m.log10(b - a) - m.log10(0.000005)) / m.log10(2)
root3 = originalBisectionMethod(a, b, iterations)
print("----->Third root: %.5f" % root3, "Found after %d" % iterations, "iterations")


print("\n")
print("\tΚλασική μέθοδος Newton-Raphson")
root, iterations = originalNewtonraphson(0.2)
print("----->First root: %.5f" % root, "Found after %d" % iterations, "iterations")

root, iterations = originalNewtonraphson(1.5)
print("----->Second root: %.5f" % root, "Found after %d" % iterations, "iterations")

root, iterations = originalNewtonraphson(2.5)
print("----->Third root: %.5f" % root, "Found after %d" % iterations, "iterations")


print("\n")
print("\tΚλασική μέθοδος τέμνουσας")
x0_init = 0
x1_init = 0.5
root1, iterations = originalSecant(x0_init, x1_init)
print("----->First root: %.5f" % root1, "Found after %d" % iterations, "iterations")

x0_init = 1
x1_init = 2
root2, iterations = originalSecant(x0_init, x1_init)
print("----->Second root: %.5f" % root2, "Found after %d" % iterations, "iterations")
x0_init = 2
x1_init = 3
root3, iterations = originalSecant(x0_init, x1_init)
print("----->Third root: %.5f" % root3, "Found after %d" % iterations, "iterations")
