#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

typedef struct {
	int pid;
	int arrival_time;
	int burst_time;
} process;


/*
    Μέθοδος τύπου bool που δέχεται ως ορίσματα έναν πίνακα τύπου (struct) process (arr[]) ,
    το μέγεθος του πίνακα (int n) και έναν ακέραιο (int id). Η μέθοδος επιστρέφει true 
    μόνο όταν ο ακέραιος id συνάδει με κάποιο από τα pid των διεργασιών του πίνακα arr[].
    . Σε αυτήν την περίπτωση έχει βρεθεί επιτυχώς το id, διαφορετικά η μέθοδος επιστρέφει false. 
*/
bool contains(process arr[], int n, int id)
{
	bool temp = false;
	for ( int i =0;i<n;i++)
	{
		if(arr[i].pid  == -1)
			break;
		if(arr[i].pid == id)
			temp = true;
	}
	
	return temp;
}

/*
    Μέθοδος που δέχεται στα ορίσματα της έναν πίνακα τύπου (struct) process (arr[]) και το μέγεθος του πίνακα
   (int size).Ταξινομεί τον πίνακα σε αύξουσα διάταξη σύμφωνα με τον χρόνο καταιγισμού CPU (burst_time) 
   των διεργασιών. --bubble sort.
*/

void Sort(process temp_array[], int count)
{
    int i, j;
    for (i = 0; i < count - 1; i++)
    {
        for (j = 0; j < count - i - 1; j++){
            if (temp_array[j].burst_time > temp_array[j + 1].burst_time)
            {
                int temp = temp_array[j].arrival_time;
    		temp_array[j].arrival_time = temp_array[j + 1].arrival_time;
    		temp_array[j + 1].arrival_time = temp;
    		
    		int temp1 = temp_array[j].pid;
    		temp_array[j].pid = temp_array[j + 1].pid;
    		temp_array[j + 1].pid = temp1;
    		
    		int temp2 = temp_array[j].burst_time;
    		temp_array[j].burst_time = temp_array[j + 1].burst_time;
    		temp_array[j + 1].burst_time = temp2;
 
            }
        }
     }
    
}

/*
   void μέθοδος που δέχεται στα ορίσματα της έναν πίνακα τύπου (struct) process arr[], 
   το μέγεθος του πίνακα int n, τον κώδικο (pid) της διεργασίας που θέλουμε να βρούμε, και (με αναφορά) τον δείκτη θέσης που θα βρεθεί το στοιχείο
   int *ind.
   Η μέθοδος βρίσκει το στοιχείο με pid = id, στον πίνακα arr[] και εισάγει τη θέση στην μεταβλητή *ind,

*/

void find_pid(process arr[],int n,int id,int *ind)
{
	
	for(int i = 0; i < n;i++)
	{
		if(id == arr[i].pid){
			*ind = i;
			return ;
		}
	}
	
}

/* Void μέθοδος που δέχεται στα ορίσματα της έναν πίνακα τύπου (struct) process arr[], 
   το μέγεθος του πίνακα int n, και τον δείκτη του στοιχείου που θέλουμε να αφαιρεθεί int index.
   Η μέθοδος μεταφέρει τα στοιχεία απο την θέση index μία θέση πριν

*/
void ExtractElement(process arr[], int n, int index)
{
	if ( index == n - 1){
		return;
	}
	for ( int i = index; i < n; i++)	
		arr[i] = arr[i+1];
	
}

/*  Μέθοδος που προσθέτει τους χρόνους καταιγισμού CPU
    όλων των διεργασιών του πίνακα arr[] και τους
    επιστρέφει.
*/

int addBurstTime(process arr[], int n)
{
	int count = 0;
	for(int i = 0; i < n; i++)
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

	/* Μεταβλητή που δείχνει το τέλος του πίνακα, Κάθε φορά που ένα στοιχείο του πίνακα arr
	   μπαίνει στην έτοιμη ουρά τότε αφαιρείται από τον πρώτο ώστε να μην ελέγχεται ξανά
	*/
	int varrying_size_of_array = n;
	int addition_burst_time = addBurstTime(arr,n); // Άθροισμα των χρόνων καταιγισμού CPU όλων των διεργασιών
	int time_unit = 0; // Χρονική στιγμή που βρισκόμαστε κατά την εκτέλεση των διεργασιών "clock"
	
	int result_array[addition_burst_time]; // Ο πίνακας που θα αποθηκεύει την έξοδο. Τα id's των διεργασιών που θα εκτελούνται κάθε χρονική στιγμή 

	process *ready_queue; // Ουρά έτοιμων διεργασιών
	ready_queue = malloc(n * sizeof(process)); //Δεύσμευση χώρου όσου του πίνακα διεργασιών arr[]

	process dump_element; // Δημιουργία dump μεταβλητής τύπου process με όλα τα πεδία ίσα με -1. Η ουρά έτοιμων διεργασιών αρχικοποιείται αρχικά με αυτή τη μεταβλητή σε κάθε κελί
	dump_element.pid = -1;
	dump_element.arrival_time = -1;
	dump_element.burst_time = -1;

	for (int i = 0; i < n; i++)
		ready_queue[i] = dump_element;
	
	ready_queue[0] = arr[0];
	
	int items_in_ready_queue = 1; // Αριθμός στοιχείων που περιέχει η ουρά έτοιμων διεργασιών. Κάθε φορά που εισάγεται μία διεργασία στην ουρά αυξάνεται κατά ένα
	arr[0].burst_time--; //Μείωση του χρόνου καταιγισμού κατά ένα του πρώτου στοιχείου του πίνακα (αφού συμπίππτει με το πρώτο στοιχείο της έτοιμης ουράς)
	ready_queue[0].burst_time--; //Μείωση του χρόνου καταιγισμού κατά ένα του πρώτου στοιχείου της ουράς έτοιμων διεργασιών
	
	result_array[0] = arr[0].pid; //Εισαγωγή του id της διεργασίας στη θέση 0 της ουράς έτοιμων διεργασιών στον πίνακα εξόδου
	
	time_unit++; // Αύξηση του "clock" κατά ένα
	
	if (arr[0].burst_time == 0) // Αν ο χρόνος καταιγισμού της διεργασίας που μπήκε στον πίνακα αποτελεσμάτων έγινε 0 τότε αφαιρείται το στοιχείο από τον πίνακα arr[]
				    // και από την ουρά έτοιμων διεργασιών	 
	{	
		ExtractElement(arr, varrying_size_of_array,0);
		ExtractElement(ready_queue, varrying_size_of_array, 0);
		varrying_size_of_array--;
		items_in_ready_queue = 0;
	
	}
	
	
	
	for (int i = 1;  i < addition_burst_time ; i++) // Μέχρι να γεμίσει ο πίνακας εξόδου
	{
		
		for ( int j = 0; j < varrying_size_of_array; j++) //Για κάθε διεργασία του πίνακα arr[]
		{
			if(arr[j].arrival_time > time_unit) 
				break;
			
			// Αν ο χρόνος άφιξης <= "clock" ΚΑΙ δεν περιέχεται ήδη στην ουρά έτοιμων διεργασιών
			if(arr[j].arrival_time == time_unit && !contains(ready_queue, items_in_ready_queue,arr[j].pid))
			{
				ready_queue[items_in_ready_queue] = arr[j]; //Εισαγωγή του στοιχείου στην ουρά έτοιμων διεργασιών
				items_in_ready_queue++;
			
			} 
		}	
		
		Sort(ready_queue, items_in_ready_queue); // Ταξινόμηση της ουράς έτοιμων διεργασιών βάσει του χρόνου καταιγισμού τους.	
		
		result_array[i] = ready_queue[0].pid; // Εισαγωγή της πρώτης διεργασίας της ουράς έτοιμων διεργασιών στον πίνακα αποτελεσμάτων 
		time_unit++; // Αύξηση του "clock" κατά ένα
		
		int min_id = 0; //Δείκτης που συμβολίζει τη θέση του στοιχείου στον πίνακα arr[], της διεργασίας που βρίσκεται στην 1η θέση στην έτοιμη ουρά 
		find_pid(arr,varrying_size_of_array,ready_queue[0].pid,&min_id);
					
		if(ready_queue[0].burst_time > 0) { //Αν ο χρόνος καταιγισμού της διεργασίας στην πρώτη θέση της έτοιμης ουράς δεν έχει μηδενιστεί
			
			arr[min_id].burst_time--; //Μείωση του χρόνου καταιγισμού κατά ένα του στοιχείου στην θέση min_id του πίνακα (αφού συμπίππτει με το πρώτο στοιχείο της έτοιμης ουράς)
			ready_queue[0].burst_time--; //Μείωση του χρόνου καταιγισμού κατά ένα του πρώτου στοιχείου της ουράς
				
			if(ready_queue[0].burst_time == 0) { //Αν ο χρόνος καταιγισμού μηδενιστεί τότε αφαιρείται από τον πίνακα διεργασιών arr[] και απο την ουρρά έτοιμων διεργασιών.
				
				ExtractElement(arr,varrying_size_of_array,min_id);
				varrying_size_of_array--;
				
				ExtractElement(ready_queue, items_in_ready_queue, 0);
				items_in_ready_queue--;
			}
		}
	}
		
		
	// Εμφάνιση αποτελεσμάτων
	for (int i = 0; i < addition_burst_time; i++)
		printf("%d\n", result_array[i]);
	
	return 0; /* DO NOT EDIT THIS LINE */
}

