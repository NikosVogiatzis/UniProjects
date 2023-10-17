import math
import numpy as np


# Η συνάρτηση αυτή πολλαπλασιάζει τον πίνακα μεταθέσεων (P) με τον πίνακα στήλη των σταθερών όρων (b) κι επιστρέφει
# τον πίνακα στήλη που προκύπτει

def multiplyMatrices(q_matrix, w_matrix):
    solutionMatrix = []
    for k in range(len(w_matrix)):
        solutionMatrix.append(0)

    for k in range(len(q_matrix)):
        sum_calc = 0
        for r in range(len(q_matrix)):
            sum_calc += q_matrix[k][r] * w_matrix[r]
        solutionMatrix[k] = sum_calc
    return solutionMatrix


# Η συνάρτηση αυτή δημιουργεί έναν τετραγωνικό ταυτοτικό πίνακα
# size*size με 1 στην κύρια διαγώνιο και 0 σε κάθε άλλη θέση
# Χρησιμοποιείται για την δημιουργία του κάτω τριγωνικού πίνακα L και
# του ταυτοτικού πίνακα P (των μεταθέσεων) που θα χρειαστούμε στην
# παραγοντοποίηση PA = LU
def IdentityMatrixCreator(size):
    p = []

    for i in range(size):
        p.append([])

        for j in range(size):
            p[i].append(0)

    for i in range(size):
        for j in range(size):
            if i == j:
                p[i][j] = 1

    return p


# Αφού έχει ολοκληρωθεί η παραγοντοποίηση PA = LU λύνουμε το σύστημα Lc = Pb ως προς c.
# Τα ορίσματα είναι ο κατω τριγωνικός L και το b είναι ο πινακας μεταθέσεων*τον πίνακα στήλη των σταθερών όρων
def Lc_Pb(L_matrix, Pb_matrix):
    c = [[0] * len(Pb_matrix)] * len(Pb_matrix)

    # Για κάθε γραμμή του κάτω τριγωνικού από πάνω προς τα κάτω
    for r in range(len(Pb_matrix)):
        # Αν η τιμή της διαγωνίου είναι 0 τότε και c[i]=0
        if L_matrix[r][r] == 0:
            c[r] = 0
            continue
        sumar = 0
        # βρίσκουμε το άθροισμα των στοιχείων της γραμμής επί των γνωστών τιμών του διανύσματος c
        for k in range(r):
            sumar += L_matrix[r][k] * c[k]
        # Θέτουμε στο διάνυσμα λύσεων την τιμή των σταθερών όρων για τη γραμμή i μείον του αθροίσματος
        # που βρήκαμε και όλο διά την τιμή του διαγώνιου στοιχείου του κάτω τριγωνικού πίνακα L
        c[r] = (Pb_matrix[r] - sumar) / L_matrix[r][r]

    return c


# Η συνάρτηση αυτή αποτελεί το τελευταίο βήμα της λύσης του συστήματος και λύνει Το σύστημα Ux=c οπου
# χ το διάνυσμα που ψάχνουμε, c το διάνυσμα που προέκυψε απο την λύση του Lc=Pb και U ο άνω τριγωνικός
# πίνακας της παραγοντοποίησης PA = LU. Η συνάρτηση λειτουργεί ακριβώς όπως η παραπάνω μόνο που αυτήν την φορά
# για να βρούμε το διάνυσμα λύσεων εργαζόμαστε από κάτω προς τα πάνω λόγω της άνω τριγωνικής μορφής του πίνακα U
def Ux_c(U, b):
    x = [[0] * len(b)] * len(b)

    for i in range(len(b) - 1, -1, -1):
        if U[i][i] == 0:
            x[i] = 0
            continue

        suma = 0
        for j in range(i + 1, len(b), 1):
            suma += U[i][j] * x[j]
        x[i] = (b[i] - suma) / U[i][i]

    return x


# Η συνάρτηση αυτή επιστρέφει την παραγοντοποίηση PA = LU ενός πίνακα Α
# δέχεται ως ορίσματα τον πίνακα μεταθέσεων P, τον πίνακα των συντελεστών των αγνώστων Α,
# έναν ταυτοτικό πίνακα L που θα μετατραπεί σε κάτω τριγωνικό και τον πίνακα U
# που είναι αρχικά αντίγραφο του πίνακα Α και θα μετατραπεί σε άνω τριγωνικό πίνακα
def PALU(P, A, L, U):
    # Για κάθε γραμμή του πίνακα Α
    for i in range(len(A)):
        # θέτουμε τη γραμμή του οδηγού διαγώνιου στοιχείου ίση με i
        rowOfPivot = i
        # Ελέγχουμε αν στην ίδια στήλη υπάρχει στοιχείο με μεγαλύτερη τιμή σε
        # απόλυτη τιμή και αν ναι αλλάζουμε τη
        # γραμμή του οδηγού
        for j in range(i + 1, len(A), 1):
            if (abs(A[i][i]) < abs(A[j][i])):
                rowOfPivot = j
        # Αν βρέθηκε στοιχείο με μεγαλύτερη κατ απόλυτο τιμή τότε αντιμεταθέτουμε τις γραμμές στον πίνακα U και P
        if rowOfPivot != i:
            temp = U[i]
            U[i] = U[rowOfPivot]
            U[rowOfPivot] = temp
            temp1 = P[i]
            P[i] = P[rowOfPivot]
            P[rowOfPivot] = temp1

            # To ιδιο κάνουμε και για το σημαντικό μέρος του πίνακα L, αυτό για τα στοιχεία κάτω από την κύρια διαγώνιο
            for k in range(i):
                (L[i][k], L[rowOfPivot][k]) = (L[rowOfPivot][k], L[i][k])

        # Σε αυτήν την επανάληψη διαμοφρώνουμε τους πίνακες L και U ως εξής:
        # Θέτουμε ως οδηγό το διαγώνιο στοιχείο και ως πολλαπλασιαστή για την απαλοιφή
        # pivotFactor το στοιχείο της γραμμής που εξετάζουμε για συγκεκριμένη στήλη
        # προς την τιμή του οδηγού-διαγώνιου στοιχείου της στήλης
        for y in range(i + 1, len(A), 1):

            if U[y][i] == 0:
                continue

            pivot = U[i][i]
            pivotFactor = U[y][i] / pivot
            # Μηδενίζουμε τα στοιχεία κάτω απο την κύρια διαγώνιο στον άνω τριγωνικό U
            U[y][i] = 0
            # Εκτελούμε γραμμοπράξεις στα στοιχεία πάνω από την κύρια διαγώνιο για τον πίνακα U
            for j in range(i + 1, len(A), 1):
                U[y][j] -= pivotFactor * U[i][j]
            # Καταχωρούμε την τιμή του πολλαπλασιαστή για την απαλοιφή στον κάτω τριγωνικό πίνακα L
            L[y][i] = pivotFactor
    return P, L, U


def cholesky(A):
    # χρησιμοποιούμε τον πίνακα Χ για να μην αλλάξουμε το πίνακα Α
    X = A.copy()

    # Δημιουργούμε έναν κενό πίνακα L μεγέθους len(A)*len(A)
    # που στη συνέχεια θα μετατραπεί σε κάτω τριγωνικό πίνακα της
    # αποσύνθεσης Cholesky
    L = EmptyMatrixCreator(len(A))
    for i in range(len(X)):

        for j in range(len(X)):
            sum = 0
            # Βρίσκουμε το άθροισμα για τα διαγώνια στοιχεία και το αποθηκεύουμε στον πίνακα L
            if (i == j):
                for k in range(j):
                    sum += math.pow(L[j][k], 2)
                L[i][i] = math.sqrt(X[i][i] - sum)
            else:
                # Βρίσκουμε το άθροισμα για τα μη διαγώνια στοιχεία και τα αποθηκεύουμε στον πίνακα L
                for k in range(j):
                    sum = sum + L[i][k] * L[j][k]
                if (L[j][j] > 0):
                    L[i][j] = (X[i][j] - sum) / L[j][j]

    return L


# this funtion creates a size*size matrix
# as a list of lists with 0 in all cells
def EmptyMatrixCreator(size):
    p = []

    for i in range(size):
        p.append([])

        for j in range(size):
            p[i].append(0)
    # for i in range(3):
    #     print(p[i])
    return p


def emptyList(size):
    u = []

    for i in range(size):
        u.append(0.0)

    return u


def suma(q):
    su = 0
    for i in range(len(q)):
        su += math.fabs(q[i])
    return su


def Gauss_Seidel(A_matrix, b_matrix, x_matrix):
    for j in range(0, len(A_matrix)):

        d = b_matrix[j]

        for i in range(0, len(A_matrix)):
            if j != i:
                d -= A_matrix[j][i] * x_matrix[i]
        x_matrix[j] = d / A_matrix[j][j]
    return x_matrix


# Πίνακα των συντελεστών των αγνώστων
A = [[2, 1, 5],
     [4, 4, -4],
     [1, 3, 1]]
# πίνακας στήλη των σταθερών όρων
b = [5, 0, 6]
print("\t\t---->Άσκηση 3η<---- ")
print("a)   Ερώτημα πρώτο: Επίλυση γραμμικού συστήματος με τη μέθοδο PA = LU")

print("A matrix is: ")
for i in A:
    print(i)
print("b is", b)
P = IdentityMatrixCreator(len(A))  # πίνακας μεταθέσεων(αρχικά ταυτοτικός)
U = A.copy()
L = IdentityMatrixCreator(len(A))  # κάτω τριγωνικός L της PA = LU (αρχικοποιείται ως ταυτοτικός)
P, L, U = PALU(P, A, L, U)

Pb = multiplyMatrices(P, b)  # multiply matrix P with vector b
c = Lc_Pb(L, Pb)
x = Ux_c(U, c)

print("The solution of Ax = b system is: ", x)

print("\n")

print("b)   Ερώτημα δεύτερο: Αποσύνθεση Cholesky")
A = [[4, -2, 2],
     [-2, 2, -4],
     [2, -4, 11]]

print("A matrix is: ")
for i in A:
    print(i)

L = cholesky(A)
print("Ο κάτω τριγωνικός πίνακας L που δημιουργήθηκε με την αποσύνθεση Cholesky είναι ο εξής: ")
for i in range(len(L)):
    print(L[i])
L_T = EmptyMatrixCreator(len(A))
print()
for i in range(3):
    for j in range(3):
        L_T[i][j] = L[j][i]
# Για έλεγχο της εγκυρότητας της αποσύνθεσης μπορούμε να πολλαπλασιάσουμε τον κάτω τριγωνικό L
# με τον άνω τριγωνικό L_T και παίρνουμε στην έξοδο τον πίνακα Α

print("Επαλήθευση: Ο πολλαπλασιασμός του κάτω τριγωνικού L με τον άνω τριγωνικό L_T μας δίνει τον αρχικό πίνακα:")
q = np.matmul(L, L_T)
for i in range(3):
    print(q[i])

print("\n")
print("c)   Ερώτημα 3ο: Gauss-Seidel")

b = []
for i in range(10):
    if i == 0 or i == 9:
        b.append(3)
    else:
        b.append(1)

A = []
for i in range(10):
    A.append([])

    for j in range(10):
        A[i].append(0)
for i in range(10):
    for j in range(10):
        if i == j:
            A[i][j] = 5
        else:
            if j == i + 1 or j == i - 1:
                A[i][j] = -2

x = emptyList(len(A))

tolerableError = 1
x = Gauss_Seidel(A, b, x)
iterations = 0
while tolerableError >= 0.00005:
    xold = x.copy()
    x = Gauss_Seidel(A, b, x)
    sumx = suma(x)
    sumx_old = suma(xold)
    tolerableError = math.fabs(sumx - sumx_old)
    iterations += 1
print("Επαναλήψεις που χρειάστηκαν για την εύρεση λύσης του συστήματος με ακρίβεια τεσσάρων δεκαδικών ψηφίων:",
      iterations)
for i in range(len(A)):
    print((x[i]))
