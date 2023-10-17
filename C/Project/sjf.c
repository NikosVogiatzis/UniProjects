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
void Sort(process temp_array[], int size)
{
    int i, j;
    for (i = 0; i < size - 1; i++)
    {
        for (j = 0; j < size - i - 1; j++){
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
			return;}
	}
}

/* Void μέθοδος που δέχεται στα ορίσματα της έναν πίνακα τύπου (struct) process arr[], 
   το μέγεθος του πίνακα int n, και τον δείκτη του στοιχείου που θέλουμε να αφαιρεθεί int index.
   Η μέθοδος μεταφέρει τα στοιχεία απο την θέση index μία θέση πριν

*/
void ExtractElement(process arr[], int n, int index)
{
	if ( index == n - 1)
		return;
	for ( int i = index; i < n; i++)	
		arr[i] = arr[i+1];
	
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

	
	process *res_arr; // Πίνακας αποτελεσμάτων στον οποίο θα εισαχθούν οι διεργασίες σύμφωνα με την sjf δρομολόγηση
	res_arr = malloc(n * sizeof(process)); // Δεύσμευση χώρου όσου και στον πίνακα δεδομένων arr
	
	process *ready_queue; // Ουρά έτοιμων διεργασιών
	ready_queue = malloc(n * sizeof(process));  
	
	/* Μεταβλητή που δείχνει το τέλος του πίνακα, Κάθε φορά που ένα στοιχείο του πίνακα arr
	   μπαίνει στην έτοιμη ουρά τότε αφαιρείται από τον πρώτο ώστε να μην ελέγχεται ξανά
	*/
	
 	int varying_size_of_array = n; 

	int burst_time_count_on_list = 0; // Συνολικός "χρόνος καταιγισμού" των διεργασιών που εισάγονται στον πίνακα αποτελεσμάτων
	
	res_arr[0] = arr[0]; //Το πρώτο στοιχείο του πίνακα αποτελεσμάτων είναι το πρώτο στοιχείο του πίνακα διεργασιών με χρόνο_άφιξης = 0
	
	burst_time_count_on_list = arr[0].burst_time; //krataw to burst time gia na kserw poies diergasies mporoun na mpoun sth temp lista
	
	ExtractElement(arr,varying_size_of_array, 0); //Αφαίρεση από τον πίνακα διεργασιών του πρώτου στοιχείου ώστε να μην ελεγχθεί ξανά
	varying_size_of_array--; //Μείωση του μεγέθους του πίνακα κατά ένα.
	
	process dump_element; //Δημιουργία dump μεταβλητής τύπου process με όλα τα πεδία ίσα με -1. Η ουρά έτοιμων διεργασιών αρχικοποιείται αρχικά με αυτή τη μεταβλητή σε κάθε κελί 
	dump_element.pid = -1;
	dump_element.arrival_time = -1;
	dump_element.burst_time = -1;
	
	
	
	//Αρχικοποίηση όλων των στοιχείων του πίνακα ready_queue σε -1
	for (int i = 0; i < n; i++)
		ready_queue[i] = dump_element;
	
	int items_in_ready_queue = 0; // Αριθμός στοιχείων που περιέχει η ουρά έτοιμων διεργασιών. Κάθε φορά που εισάγεται μία διεργασία στην ουρά αυξάνεται κατά ένα
	
	for (int i=1; i < n; i++) // Μέχρι να γεμίσει ο πίνακας αποτελεσμάτων res_arr[]
	{
		for ( int j = 0; j < varying_size_of_array; j++) // Για κάθε διεργασία που έχει απομείνει στον πίνακα arr[]
		{
			/* Αν ο χρόνος άφιξης της διεργασίας είναι μικροτερος/ίσος του μέχρι τώρα χρόνου καταιγισμού που καταλαμβάνουν οι διεργασίες στον πίνακα αποτελεσμάτων
			   και αν δεν υπάρχει ήδη η διεργασία στην ουρά έτοιμων διεργασιών, τότε το στοιχείο εισάγεται στην τελευταία */
			if (arr[j].arrival_time <= burst_time_count_on_list && !contains(ready_queue,items_in_ready_queue,arr[j].pid))
			{
				ready_queue[items_in_ready_queue] = arr[j];
				items_in_ready_queue++;
			}
		
		}
		// Ταξινόμηση της ουράς έτοιμων διεργασιών βάσει του χρόνου καταιγισμού τους.
		Sort(ready_queue,items_in_ready_queue);
		
		int min_id; //Δείκτης που συμβολίζει τη θέση του στοιχείου στον πίνακα arr[], της διεργασίας που βρίσκεται στην 1η θέση στην έτοιμη ουρά 
		find_pid(arr,varying_size_of_array,ready_queue[0].pid,&min_id);
		
		res_arr[i] = ready_queue[0]; // Εισαγωγή του στοιχείου στον πίνακα αποτελεσμάτων 
		
		burst_time_count_on_list+=res_arr[i].burst_time; // Αύξηση του χρόνου καταιγισμού του πίνακα αποτελεσμάτων όσο το burst_time της διεργασίας που μπήκε στον πίνακα
		
		ExtractElement(arr,varying_size_of_array,min_id); // Αφαίρεση του στοιχείου που μόλις μπήκε στον πίνακα αποτελεσμάτων από τον πίνακα διεργασιών arr[]
		varying_size_of_array--;
		ExtractElement(ready_queue, items_in_ready_queue, 0); // Αφαίρεση του στοιχείου που μόλις μπήκε στον πίνακα αποτελεσμάτων από την ουρά έτοιμων διεργασιών ready_queue
		items_in_ready_queue--;
		
	}
	
	
	for( int i = 0; i < n; i++) //Εμφάνιση των ID των διεργασιών του πίνακα αποτελεσμάτων τόσες φορές όσες το burst_time τους
	{
		for(int j = 0 ; j < res_arr[i].burst_time ; j++)
			printf( "%d\n", res_arr[i].pid );
	}

	return 0; /* DO NOT EDIT THIS LINE */
}
