#include <stdio.h>
#include <stdlib.h>

typedef struct {
	int pid;
	int arrival_time;
	int burst_time;
} process;


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
	
	/*Βάσει της παραδοχής ότι οι διεργασίες έρχονται με την σειρά άφιξης τους
	  ο fcfs απλώς τυπώνει τα ID από τον πίνακα τύπου process με την σειρά που εισήχθησαν τα δεδομένα*/
	for( int i = 0; i < n; i++)
	{
		for(int j = 0 ; j < arr[i].burst_time ; j++)
			printf( "%d\n", arr[i].pid );
	}
	

	return 0; /* DO NOT EDIT THIS LINE */
}
