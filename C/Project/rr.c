#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>


typedef struct {
	int pid;
	int arrival_time;
	int burst_time;
} process;

/*  Μέθοδος τύπου bool που δέχεται στα ορίσματα,
    έναν πίνακα τύπου process (arr[]), το μέγεθος του
    πίνακα int n, και έναν ακέραιο int id. Η μέθοδος αποφασίζει εάν
    το  int id αποτελεί id σε κάποιο από τα στοιχεία του πίνακα arr[]. Σε αυτήν
    την περίπτωση επιστρέφει true διαφορετικά επιστρέφει false
*/
/*
bool contains(process arr[],int n,int id)
{
	for(int i = 0; i < n; i++)
	{
		if(arr[i].pid == id)
			return true;
	
	}
	return false;

}*/

/*  Μέθοδος τύπου int που δέχεται στα ορίσματα: έναν πίνακα
    τύπου process(arr[]), το μέγεθος του
    πίνακα (int n), και έναν ακέραιο (int id). Η μέθοδος αποφασίζει εάν
    το  int id αποτελεί id σε κάποιο από τα στοιχεία του πίνακα arr[]. Σε αυτήν
    την περίπτωση επιστρέφει τη θέση του πίνακα που βρέθηκε διαφορετικά επιστρέφει -1
*/
int find_pid(process arr[],int n,int id)
{
	int index = -1;
	for(int i = 0; i < n;i++)
	{
		if(id == arr[i].pid){
			index = i;
		}
	}
	
	return index;
}

/*
    Μέθοδος τύπου void που δέχεται στα ορίσματα της έναν πίνακα τύπου process (arr[]),
    το μέγεθος του πίνακα (int n) και έναν ακέραιο (int index) που αποτελεί τη θέση του 
    στοιχείου που θέλουμε να αφαιρέσουμε.
*/
void ExtractElement(process arr[], int n, int index)
{
	if ( index == n - 1)
		return;
	for ( int i = index; i < n;i++)	
		arr[i] = arr[i+1];
	
}

/*  Μέθοδος που προσθέτει τους χρόνους καταιγισμού CPU
    όλων των διεργασιών του πίνακα arr[] και τους
    επιστρέφει.
*/


int burst_time_addition(process arr[], int n)
{
	int count = 0;
	
	for( int i = 0; i < n; i++)
		count += arr[i].burst_time;

	return count;
}

int main() {

	/* read in data - DO NOT EDIT (START) */
	int n;
	int quantum;
	scanf("%d", &n);
	scanf("%d", &quantum);
	process *arr;
	arr = malloc(n * sizeof(process));
	for (int i = 0; i < n ; ++i) {
		scanf("%d", &arr[i].pid);
		scanf("%d", &arr[i].arrival_time);
		scanf("%d", &arr[i].burst_time);
	}
	/* read in data - DO NOT EDIT (END) */
	
	
	int total_burst_time = burst_time_addition(arr,n); // Άθροισμα των χρόνων καταιγισμού CPU όλων των διεργασιών
	
	int time_unit = 0; // Χρονική στιγμή που βρισκόμαστε κατά την εκτέλεση των διεργασιών "clock"
	
	int varrying_size_of_array = n; // Μεταβλητό μέγεθος του πίνακα 
	
	int stored_result = 0; //Μετρητής που αυξάνεται κάθε φορά που προστίθεται ένα στοιχείο στον πίνακα αποτελεσμάτων
	
	process element_extracted; // Το στοιχείο που όταν είναι πρώτο στην έτοιμη ουρά μετά πρέπει να μπει στο τέλος εαν υπολείπεται χρόνος καταιγισμού και δεν γίνεται 0.
	
	process *ready_queue; // Ουρά έτοιμων διεργασιών
	ready_queue = malloc(n * sizeof(process));
	
	int result_array[total_burst_time]; // Ο πίνακας που θα αποθηκεύει την έξοδο. Τα id's των διεργασιών που θα εκτελούνται κάθε χρονική στιγμή 

	process dump_element; //Δημιουργία dump μεταβλητής τύπου process με όλα τα πεδία ίσα με -1. Η ουρά έτοιμων διεργασιών αρχικοποιείται αρχικά με αυτή τη μεταβλητή σε κάθε κελί 
	
	dump_element.pid = -1;
	dump_element.arrival_time = -1;
	dump_element.burst_time = -1;
	
	for (int i = 0; i < n; i++)
		ready_queue[i] = dump_element;
	
	
	ready_queue[0] = arr[0]; // Η πρώτη διεργασία του πίνακα arr[] με χρόνο άφιξης=0 μπαίνει στην ουρά έτοιμων διεργασιών.
	
	
	bool burst_time_not_zeroed = false; // bool μεταβλητή που γίνεται true μόνο όταν η διεργασία εκτελώντας το quantum έχει υπολοιπόμενο burst_time
	bool burst_time_zeroed = false; // bool μεταλητή που γίνεται true μόνο όταν η διεργασίωα εκτελώντας το quantum δεν έχει υπολοιπόμενο burst_time
	
	//Εισαγωγή πρώτων στοιχείων στον πίνακα εξόδου ( >0 && <=quantum )
	for (int i = 0; i < quantum; i++)
	{
		
		result_array[stored_result] = ready_queue[0].pid; //Εισαγωγή του id της διεργασίας στη θέση 0 της ουράς έτοιμων διεργασιών στον πίνακα εξόδου
		stored_result++; // Αύξηση μετρητή στοιχείων αποθηκευμένων στον πίνακα εξόδου
		ready_queue[0].burst_time--; //Μείωση του χρόνου καταιγισμού κατά ένα του πρώτου στοιχείου της ουράς
		arr[0].burst_time--; //Μείωση του χρόνου καταιγισμού κατά ένα του πρώτου στοιχείου του πίνακα (αφού συμπίππτει με το πρώτο στοιχείο της έτοιμης ουράς)
		time_unit++; // Αύξηση του "clock" κατά ένα
		
		// Αν δεν υπολείπεται χρόνος καταιγισμού τότε η flag μεταβλητή (burst_time_zeroed) γίνεται αληθής και σπάει η επανάληψη
		if(ready_queue[0].burst_time == 0){
			burst_time_zeroed = true;
			break;
		}	
	}
	
	
	// Αν δεν υπολείπεται χρόνος καταιγισμού στην διεργασία που προηγουμένως ελέγχθηκε τότε το στοιχείο αφαιρείται από τον πίνακα διεργασιών
	if (burst_time_zeroed)
	{
		ExtractElement(arr, varrying_size_of_array, 0);
		varrying_size_of_array--;
		
	}
	// Αν υπολείπεται χρόνος καταιγισμού στην διεργασία που προηγουμένως ελέγχθηκε τότε κρατάμε τα στοιχεία της διεργασίας στην μεταβλητή element_extracted
	// Ώστε μετά την εισαγωγή των διεργασιών με χρόνο άφιξης < "clock" (time_unit) να εισαχθεί και αυτή στο τέλος της ουράς
	else 
	{
		element_extracted = ready_queue[0];
		burst_time_not_zeroed = true;
	}
	
	ExtractElement(ready_queue, n, 0); // Αφαίρεση της διεργασίας από την ουρά έτοιμων διεργασιών
	

	int elements_inserted = 0 ; // Μετρητής που μετράει πόσα στοιχεία περιέχει η ουρά έτοιμων διεργασιών.
	
	for (int i = 1; i < total_burst_time; i++) // Μέχρι να γεμίσει ο πίνακας εξόδου
	{
		
	
		for (int j = 0; j < varrying_size_of_array; j++) { //Για κάθε διεργασία του πίνακα arr[]
		
			// Αν ο χρόνος άφιξης <=σ "clock" ΚΑΙ το στοιχείο δεν αποτελεί αυτό που ππρέπει να μπει στο τέλος της ουράς ΚΑΙ δεν περιέχεται ήδη στην ουρά έτοιμων διεργασιών
			if (arr[j].arrival_time <= time_unit && arr[j].pid != element_extracted.pid && find_pid(ready_queue,n,arr[j].pid) == -1){
				ready_queue[elements_inserted] = arr[j]; //Εισαγωγή του στοιχείου στην ουρά έτοιμων διεργασιών
				elements_inserted++;
				
				
			}
		}
		
		burst_time_zeroed = false; // Αρχικοποίηση του flag για τον έλεγχο της νέας διεργασίας
		int arr_index = find_pid(arr,varrying_size_of_array, ready_queue[0].pid); //Βρίσκουμε τη θέση διεργασίας που είναι πρώτη στην έτοιμη ουρά - στον πίνακα arr[]
		
		// Η παρακάτων διαδικασία είναι η ίδια με την εισαγωγή του πρώτων στοιχείων στον πίνακα εξόδου που εξετάσαμε πριν την επανάληψη 
		if(burst_time_not_zeroed){
			ready_queue[elements_inserted] = element_extracted;
			elements_inserted++;}
		
		burst_time_not_zeroed = false;
		for (int k = 0; k < quantum; k++)
		{
			
			result_array[stored_result] = ready_queue[0].pid;
			stored_result++;
			ready_queue[0].burst_time--;
			arr[arr_index].burst_time--;
			time_unit++;	
			
			
			if(ready_queue[0].burst_time == 0){
				burst_time_zeroed = true;
				break;
			}
			
			
		}
			
		if (burst_time_zeroed)
		{
			ExtractElement(arr, varrying_size_of_array, arr_index);
			varrying_size_of_array--;
		}
		else
		{
			element_extracted = ready_queue[0];
			burst_time_not_zeroed = true;
		}
		if(elements_inserted > 0){
		
			ExtractElement(ready_queue, n, 0);
			elements_inserted--;
		}
		
	}
		
	//Εμφάνιση του πίνακα εξόδου στην τυπική έξοδο
	for(int i=0;i<total_burst_time;i++)
		printf("%d\n", result_array[i]);


	return 0; /* DO NOT EDIT THIS LINE */
}
