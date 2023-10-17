import numpy as np


def print_eigenvector(eigenvector, matrix_size):
    for i in range(matrix_size):
        if i == 0:
            print('p = [%9.7f' % eigenvector[i], end=', ')
        elif i == 14:
            print('%0.7f' % eigenvector[i], end='')
        else:
            print('%0.7f' % eigenvector[i], end=' ')
    print(']')


def calculate_eigenvector(Google_Matrix, matrix_size):
    eigenvector = [G[0][i] for i in range(matrix_size)]
    print()
    for k in range(2 * matrix_size):

        # Πολλαπλασιάζουμε το τυχαίο ιδιοδιάνυσμα eigenvector με τον πίνακα Google_Matrix
        eigenvector = np.matmul(Google_Matrix, eigenvector)

        # Για κάθε στοιχείο του ιδιοδιανύσματος x διαιρούμε την τιμή αυτή με το eigenvector[0]
        for i in range(matrix_size):
            eigenvector = eigenvector / eigenvector[0]

    # Βρίσκουμε το άθροισμα των στοιχείων του ιδιοδιανύσματος eigenvector
    sum_of_eigenvector = 0
    for i in range(matrix_size):
        sum_of_eigenvector += eigenvector[i]

    # Διαιρούμε κάθε στοιχείο του ιδιοδιανύσματος eigenvector με το άθροισμα που βρήκαμε παραπάνω ώστε να
    # κανονικοποιηθεί
    for i in range(matrix_size):
        eigenvector[i] /= sum_of_eigenvector

    return eigenvector


def calculate_nj(A_matrix, matrix_size):
    # Αρχικοποίηση του πίνακα αθροισμάτων των στηλών του πίνακα A_matrix
    nj_matrix = np.zeros(matrix_size)

    # Υπολογισμός του πίνακα nj_matrix
    for i in range(matrix_size):
        for j in range(matrix_size):
            nj_matrix[i] += A_matrix[i][j]

    return nj_matrix


def calculateG(A_matrix, nj_matrix, matrix_size, prob_q):

    Google_Matrix = [[0] * n for i in range(n)]

    # Υπολογισμός του πίνακα Google σύμφωνα με τον τύπο
    for i in range(matrix_size):
        for j in range(matrix_size):
            Google_Matrix[i][j] = (prob_q / matrix_size) + (A_matrix[j][i] * (1 - prob_q)) / nj_matrix[j]

    return Google_Matrix


# Αρχικοποίηση του μεγέθους των πινάκων
n = 15
# Πιθανότητα q
q = 0.15

# Δημιουργία πίνακα A
A = [[0] * n for i in range(n)]
A[0][1] = A[0][8] = A[1][2] = A[1][4] = A[1][6] = A[2][1] = A[2][5] = A[2][7] = A[3][2] = \
    A[3][11] = A[4][0] = A[4][9] = \
    A[5][9] = A[5][10] = A[6][9] = A[6][10] = A[7][3] = A[7][10] = A[8][4] = A[8][5] = A[8][9] = \
    A[9][12] = A[10][14] = A[11][6] = A[11][7] = A[11][10] = A[12][8] = A[12][13] = A[13][9] = \
    A[13][10] = A[13][12] = A[13][14] = A[14][11] = A[14][13] = 1

nj = calculate_nj(A, n) # Υπολογισμός του πίνακα nj
G = calculateG(A, nj, n, q) # Υπολογισμός του πίνακα Google


# Εμφάνιση του αθροισμάτων των στηλών του πίνακα G
print("\t\tΕρώτημα 1ο: Απόδειξη ότι ο πίνακας Google είναι στοχαστικός! ")
print()
print("Το άθροισμα των στηλών του πίνακα G ώστε να αποδείξουμε ότι είναι στοχαστικός: ")
for i in range(n):
    sumofG = 0
    for j in range(n):
        sumofG += G[j][i]
    print("%0.5f" % sumofG, end=" ")
print("\nΕπομένως ο πίνακας Google είναι στοχαστικός!")
print()

# <--------------------------------------------- ΑΣΚΗΣΗ 2 <--------------------------------------------
print("\t\tΕρώτημα 2ο: ιδιοδιάνυσμα μέγιστης ιδιοτιμής πίνακα Google")

# Υπολογισμός του πίνακα "ιδιοδιανύσματος p"
p = calculate_eigenvector(G, n)
print('Το μέγιστο ιδιοδιάνυσμα της μέγιστης ιδιοτιμής είναι το: ')
print_eigenvector(p, n)
print()

# ----------------------------------> ΑΣΚΗΣΗ 3 <----------------------------------------
q_1 = 0.02  # Πιθανότητα μεταπήδησης
q_2 = 0.6

A_q3 = [[0] * n for i in range(n)]
# Αφαιρώ την A_q3[1][2]
# Προσθέτω τις A_q3[2][8], A_q3[1][8], A_q3[5][8], A_q3[10][8] # shmantikothta ths selidas 9
A_q3[0][1] = A_q3[0][8] = A_q3[1][4] = A_q3[1][6] = A_q3[1][8] = A_q3[2][1] = A_q3[2][5] = A_q3[2][7] = \
    A_q3[2][8] = A_q3[3][2] = A_q3[3][11] = A_q3[4][0] = A_q3[4][9] = A_q3[5][8] = A_q3[5][9] = A_q3[5][10] = A_q3[6][
    9] = \
    A_q3[6][10] = A_q3[7][3] = A_q3[7][10] = A_q3[8][4] = A_q3[8][5] = A_q3[8][9] = A_q3[9][12] = A_q3[10][8] = \
    A_q3[10][14] = \
    A_q3[11][6] = A_q3[11][7] = A_q3[11][10] = A_q3[12][8] = A_q3[12][13] = A_q3[13][9] = \
    A_q3[13][10] = A_q3[13][12] = A_q3[13][14] = A_q3[14][11] = A_q3[14][13] = 1

# Υπολογισμός του πίνακα αθροισμάτων των γραμμών nj_q3
nj_q3 = calculate_nj(A_q3, n)

print("\t\tΕρώτημα 3ο: Πρόσθεση 4 συνδέσεων και αφαίρεση μίας ώστε να αυξηθεί η σημαντικότητα της σελίδας που επιλέγω ("
      "σελίδα: 9)")

# Υπολογισμός του πίνακα G_q1
G_q3 = calculateG(A_q3, nj_q3, n, q)
# Υπολογισμός του μέγιστου ιδιοδιανύσματος
p_q3 = calculate_eigenvector(G_q3, n)

print()
print('Το μέγιστο ιδιοδιάνυσμα της μέγιστης ιδιοτιμής για q = 0.15 είναι το: ')
print_eigenvector(p_q3, n)

print("Απόδειξη ότι αυξήθηκε η σημαντικότητα της 9. Αρχικά p= %0.7f" % p[8], "Με τις αλλαγές p= %0.7f" % p_q3[8])
# --------------------------------> ΑΣΚΗΣΗ 4<--------------------------------------------

nj_q3 = calculate_nj(A_q3, n)
G_q3 = calculateG(A_q3, nj_q3, n, q_1)
p_q3 = calculate_eigenvector(G_q3, n)

print("\t\tΕρώτημα 4: Αλλαγή της πιθανότητας μεταπηδησης σε 0.02 και 0.6")
print('Το μέγιστο ιδιοδιάνυσμα της μέγιστης ιδιοτιμής για q = 0.02 είναι το: ')
print_eigenvector(p_q3, n)

print()

nj_q3 = calculate_nj(A_q3, n)
G_q3 = calculateG(A_q3, nj_q3, n, q_2)
p_q3 = calculate_eigenvector(G_q3, n)

print('Το μέγιστο ιδιοδιάνυσμα της μέγιστης ιδιοτιμής για q=0.6 είναι το: ')
print_eigenvector(p_q3, n)
print()
# ---------------------------------------------> ΑΣΚΗΣΗ 5 <-------------------------------------------------------

print("\t\tΕρώτημα 5ο: Βελτίωση τάξης της σελίδας 11 σε σχέση με τη σελίδα 10")
# Δημιουργία πίνακα A
A = [[0] * n for i in range(n)]
A[0][1] = A[0][8] = A[1][2] = A[1][4] = A[1][6] = A[2][1] = A[2][5] = A[2][7] = A[3][2] = \
    A[3][11] = A[4][0] = A[4][9] = \
    A[5][9] = A[5][10] = A[6][9] = A[6][10] = A[7][3] = A[7][10] = A[8][4] = A[8][5] = A[8][9] = \
    A[9][12] = A[10][14] = A[11][6] = A[11][7] = A[11][10] = A[12][8] = A[12][13] = A[13][9] = \
    A[13][10] = A[13][12] = A[13][14] = A[14][11] = A[14][13] = 1
A[8][11] = A[12][11] = 3

# Αρχικοποίηση του πίνακα αθροισμάτων των στηλών του πίνακα A
nj = calculate_nj(A, n)
G = calculateG(A, nj, n, q)
p = calculate_eigenvector(G, n)

print('Το μέγιστο ιδιοδιάνυσμα της μέγιστης ιδιοτιμής είναι το: ')
print_eigenvector(p, n)
print()

# ------------------------------------------> ΑΣΚΗΣΗ 6 <--------------------------------------------------

print("\t\tΕρώτημα 6ο: Αφαίρεση της σελίδας 10")
# array size
n = 14
A = [[0] * n for i in range(n)]
A[0][1] = A[0][8] = A[1][2] = A[1][4] = A[1][6] = A[2][1] = A[2][5] = A[2][7] = A[3][2] = A[3][10] = A[4][0] = A[4][
    9] = 1
A[5][9] = A[6][9] = A[7][3] = A[8][4] = A[8][5] = A[8][9] = A[9][11] = 1
A[10][6] = A[10][7] = A[11][8] = A[11][12] = A[12][9] = A[12][11] = A[12][13] = A[13][10] = A[13][12] = 1

nj = calculate_nj(A, n)
G = calculateG(A, nj, n, q)
p = calculate_eigenvector(G, n)
print('Το μέγιστο ιδιοδιάνυσμα της μέγιστης ιδιοτιμής έπειτα από την αφαίρεση της σελίδας 10 είναι το: ')
print_eigenvector(p, n)
print()

