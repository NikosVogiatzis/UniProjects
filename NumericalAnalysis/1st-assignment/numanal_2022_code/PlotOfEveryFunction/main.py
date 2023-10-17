import math as m
import numpy as np
import matplotlib.pyplot as plt


# Όρια γραφικής παράστασης για τη συνάρτηση της πρώτης άσκησης
x_axis = np.arange(-2, 2, 0.000001)
y_axis = np.arange(-2, 2, 0.000001)


# Όρια γραφικής παράστασης για τη συνάρτηση της πρώτης άσκησης
# x_axis = np.arange(0, 3, 0.000001)
# y_axis = np.arange(0, 3, 0.000001)

'''
    Η συνάρτηση f(x) της δεύτερης άσκησης
    Για να δουλέψει ο κώδικας πρέπει σε κάθε περίπτωση η μία εκ των 2 def f(x) να είναι εντός σχολίων
  
'''


# def f(x):
#     return 94 * m.pow(m.cos(x), 3) - 24 * m.cos(x) + 177 * m.pow(m.sin(x), 2) \
#            - 108 * m.pow(m.sin(x), 4) - 72 * m.pow(
#         m.cos(x), 3) * m.pow(m.sin(x), 2) - 65


# Η συνάρτηση f(x) της πρώτης άσκησης
def f(x):
    return m.exp(m.pow(m.sin(x), 3)) + m.pow(
        x, 6) - 2 * m.pow(x, 4) - m.pow(x, 3) - 1


i = 0
for item in x_axis:
    y_axis[i] = f(x_axis[i])
    i = i + 1

plt.grid()
plt.title('Plot of f(x)')
plt.xlabel('x')
plt.ylabel('f(x)')
fig = plt.figure(1, figsize=(5, 5))
plt.plot(x_axis, y_axis, color='red')

plt.show()
