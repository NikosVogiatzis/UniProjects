import math as m
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.pyplot as plt


def Lagrange(x, x_data, y_data):
    # Αρχικοποίηση μεγέθους για τις επαναλήψεις
    n = len(x_data)
    # Αρχικοποίηση αποτελέσματος σε 0
    result = 0
    for i in range(n):
        Lx = y_data[i]
        for j in range(n):
            if i == j:
                continue
            Lx *= (x - x_data[j]) / (x_data[i] - x_data[j])
        result += Lx
    return result


def Splines(x, x_data, y_data):
    length_of_subspaces = len(x_data) - 1
    n = len(x_data)
    # δι = χ_ι+1 - χ_ι
    delta = [x_data[i + 1] - x_data[i] for i in range(length_of_subspaces)]
    # n-2 εξισώσεις: 3* Δ2/δ2 - Δ1/δ1 όπου Δι = y_i+1 - y_i - οπού n το πλήθος των σημείων -
    second_derivatives = [3 * ((y_data[i + 2] - y_data[i + 1]) / delta[i + 1] - (y_data[i + 1] - y_data[i]) / delta[i])
                          for i in
                          range(length_of_subspaces - 1)]

    # αρχικοποίηση κάτω, επάνω και κύριας διαγωνίου ως λίστα 10 θέσεων
    lower_diagonal, upper_diagonal, main_diagonal = [0] * n, [0] * n, [0] * n

    lower_diagonal[0], main_diagonal[0] = 1, 0

    # Υπολογισμός των τιμών για κάθε διαγώνιο => Ορισμός του τρι-διαγώνιου συστήματος
    for i in range(1, length_of_subspaces):
        lower_diagonal[i] = 2 * (x_data[i + 1] - x_data[i - 1]) - delta[i - 1] * upper_diagonal[i - 1]
        upper_diagonal[i] = delta[i] / lower_diagonal[i]
        main_diagonal[i] = (second_derivatives[i - 1] - delta[i - 1] * main_diagonal[i - 1]) / lower_diagonal[i]

    # Αρχικοποίηση των λύσεων του τρι-διαγώνιου συστήματος
    b, c, d = [0] * n, [0] * n, [0] * n

    lower_diagonal[length_of_subspaces] = 1
    main_diagonal[length_of_subspaces] = 0

    # Εύρεση του c και κατά συνέπεια των b, d
    for i in range(length_of_subspaces - 1, -1, -1):
        c[i] = main_diagonal[i] - upper_diagonal[i] * c[i + 1]
        b[i] = (y_data[i + 1] - y_data[i]) / delta[i] - delta[i] * (c[i + 1] + 2 * c[i]) / 3
        d[i] = (c[i + 1] - c[i]) / (3 * delta[i])

    # Έλεγχος αν η δοθείσα από τον χρήστη τιμή για το χ βρίσκεται εντός ενός από τα υποδιαστήματα
    # Και αν ναι τότε επιστρέφεται η τιμή του πολυωνύμου της φυσικής κυβικής splines με αντικατάσταση
    # των αγνώστων
    for i in range(length_of_subspaces):
        if x_data[i] <= x <= x_data[i + 1]:
            dx = x - x_data[i]
            return y_data[i] + b[i] * dx + c[i] * dx ** 2 + d[i] * dx ** 3
    return None


def Least_Squares(x_input, x_array, y_array):
    # a + bt + ct^2 καμπύλη
    A = [[0 for _ in range(3)] for _ in range(10)]
    AT = [[0 for _ in range(10)] for _ in range(3)]
    AT_A = [[0] * 3 for _ in range(3)]
    AT_b = [0 for _ in range(3)]
    b = [0 for _ in range(len(x_array))]
    for i in range(10):
        b[i] = y_array[i]

    for j in range(10):
        A[j][0] = 1
        A[j][1] = x_array[j]
        A[j][2] = x_array[j] ** 2

    for i in range(3):
        for j in range(10):
            AT[i][j] = A[j][i]

    for i in range(3):
        for j in range(3):
            for k in range(10):
                AT_A[i][j] += A[k][j] * AT[i][k]

    for i in range(3):
        for k in range(10):
            AT_b[i] += AT[i][k] * b[k]

    res = np.linalg.solve(AT_A, AT_b)
    a, b, c = res

    result = a + b * x_input + c * (x_input ** 2)
    return result


# Αρχικοποίηση των 10 σημείων χ
x1 = round(-m.pi, 6)
x2 = round(-m.pi / 2, 6)
x3 = round(-m.pi / 4, 6)
x4 = round(-m.pi / 6, 6)
x5 = round(-m.pi / 8, 6)
x6 = 0
x7 = round(m.pi / 7, 6)
x8 = round(m.pi / 4, 6)
x9 = round(m.pi / 2, 6)
x10 = round(m.pi, 6)

# Αρχικοποίηση του πίνακα των 10 μη-ομοιόμορφα κατανεμημένων σημείων
x_values = [x1, x2, x3, x4, x5, x6, x7, x8, x9, x10]
# Εύρεση του sin(x) για κάθε σημείο (x) του init_array
y_values = [round(m.sin(x1), 6), round(m.sin(x2), 6), round(m.sin(x3), 6), round(m.sin(x4), 6),
            round(m.sin(x5), 6), round(m.sin(x6), 6), round(m.sin(x7), 6), round(m.sin(x8), 6),
            round(m.sin(x9), 6), round(m.sin(x10), 6)]
x = float(input("Give an x between [-pi, pi]:"))
result_Lagrange = Lagrange(x, x_values, y_values)
print("sin(x) using Lagrange is : ", round(result_Lagrange, 6))

result_Splines = Splines(x, x_values, y_values)

print("sin(x) using Splines is : ", round(result_Splines, 6))
result_Least_Squares = Least_Squares(x, x_values, y_values)
print("sin(x) using the Least_Square's method is : ", round(result_Least_Squares, 6))

# ----------------------------------> PLOT <-----------------------------------
x_points = np.empty(200)  # Αρχικοποίηση του πίνακα για τα 200 σημεία
y_points = np.empty(200)
y_lagrange = np.empty(200)  # Αρχικοποίηση του πίνακα για τις τιμές sin(x) για τα 200 σημεία με Lagrange
y_splines = np.empty(200)  # -||- me splines
y_least_squares = np.empty(200)  # -||- me least squares.
x_points[0] = -m.pi  # Πρώτο σημείο = -π
x_points[199] = m.pi  # Τελευταίο σημείο = π
step = m.pi / 100  # Αρχικοποίηση του "βήματος" που θα ορίζει κάθε νέο σημείο
for i in range(1, 199, 1):  # Για τα σημεία 1-199 ορίζουμε ότι το σημείο είναι το προηγούμενο του + βήμα
    x_points[i] = x_points[i - 1] + step
    x_points[i] = round(x_points[i], 6)

for i in range(200):
    y_points[i] = m.sin(x_points[i])
    y_points[i] = round(y_points[i], 6)
# Εύρεση των f(x) για κάθε χ με το πολυώνυμο Lagrange
for i in range(200):
    y_lagrange[i] = Lagrange(x_points[i], x_values, y_values)
    y_lagrange[i] = round(y_lagrange[i], 6)

# Εύρεση των f(x) για κάθε χ με τη μέθοδο Splines
for i in range(200):
    y_splines[i] = Splines(x_points[i], x_values, y_values)
    y_splines[i] = round(y_splines[i], 6)

# Εύρεση των f(x) για κάθε χ με τη μέθοδο ελαχίστων τετραγώνων
for i in range(200):
    y_least_squares[i] = Least_Squares(x_points[i], x_values, y_values)
    y_least_squares[i] = round(y_least_squares[i], 6)
sin_x = np.arange(-np.pi, np.pi, 0.1)  # start,stop,step
f_sin_x = np.sin(sin_x)

count1 = 0
count2 = 0
count3 = 0
for i in range(200):
    if y_points[i] - y_lagrange[i] == 0:
        count1 += 1
    if y_points[i] - y_splines[i] == 0:
        count2 += 1
    if y_points[i] - y_least_squares[i] == 0:
        count3 += 1
print("Ταυτίζονται ακριβώς ", count1, "σημεία από τα 200 με lagrange ")
print("Ταυτίζονται ακριβώς ", count2, "σημεία από τα 200 με Splines ")
print("Ταυτίζονται ακριβώς ", count3, "σημεία από τα 200 με Least Squares ")

# Εμφάνιση των αποτελεσμάτων (Αυθεντική sin(x), Lagrange, Splines, Least_Squares).
plt.grid()
plt.title('Plot of f(x)')
plt.xlabel('x')
plt.ylabel('f(x)')

plt.plot(x_points, y_lagrange, 'blue', )
plt.plot(x_points, y_splines, 'green')
plt.plot(x_points, y_least_squares, 'orange')
plt.plot(sin_x, f_sin_x, 'black', linestyle='--')
plt.show()
