import math as m

a = 0
b = m.pi / 2
N = 10


def f(x):
    return m.sin(x)


def second_derivative_f(x):
    return -m.sin(x)


def Trapezodial_Method(f_x):
    Sigma = 0
    # Εύρεση του Σκ=1 ως Ν-1 f(x_k)
    for i in range(1, 10):
        Sigma += f_x[i]

    integral = ((b - a) / (2 * N)) * (f_x[0] + f_x[10] + 2 * Sigma)

    # Εύρεση του θεωρητικού σφάλματος χρησιμοποιώντας τον τύπου
    theoretical_error = ((b - a) ** 3 / (12 * N ** 2)) * 1
    # Το πραγματικό σφάλμα είναι η | πραγματική τιμή του ολοκληρώματος - της προσέγγισης μας |
    actual_error = m.fabs(1 - integral)
    return integral, theoretical_error, actual_error


def Simpson(f_x):
    sigma1 = 0

    d = 1
    for i in range(int(N/2)-1):
        sigma1 += f_x[2 * d]
        d += 1

    d = 1
    sigma2 = 0
    for r in range(int(N/2)):
        sigma2 += f_x[2 * d - 1]
        d += 1

    integral1 = (b - a) / (3 * N) * (f_x[0] + f_x[10] + 2 * sigma1 + 4 * sigma2)
    simpson_theoretical_error = ((b - a) ** 5 / (180 * N ** 4)) * 1
    simpson_actual_error = m.fabs(1 - integral1)
    return integral1, simpson_actual_error, simpson_theoretical_error


number_of_points = 11
wide_of_subspaces_count = number_of_points - 1
wide_of_subspaces = (b - a) / wide_of_subspaces_count
x_values = [0 for i in range(11)]
y_values = [0 for j in range(11)]
x0 = 0

for i in range(number_of_points):
    x_values[i] = x0 + i * wide_of_subspaces

for j in range(number_of_points):
    y_values[j] = f(x_values[j])

trapezium_integral, trapezium_actual_error, trapezium_theoretical_error \
    = Trapezodial_Method(y_values)
print("Integral of sin(x) in [0,pi/2] using trapezium method is : ", trapezium_integral, "\n",
      "The actual error is |e| = ", trapezium_actual_error, "\n",
      "Theoretical error is |e| = ", trapezium_theoretical_error, "\n")

Simpson_integral, Simpson_actual_error, Simpson_theoretical_error \
    = Simpson(y_values)

print("Integral of sin(x) in [0,pi/2] using Simpson method is : ", Simpson_integral, "\n",
      "The actual error is |e| = ", Simpson_actual_error, "\n",
      "Theoretical error is |e| <= ", Simpson_theoretical_error, "\n")