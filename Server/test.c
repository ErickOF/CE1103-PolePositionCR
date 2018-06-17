#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

#include "server.h"

int main() {
	struct Server* server = (struct Server*)malloc(sizeof(struct Server));
	if (!start_connection(server)){
		printf("Error al iniciar servidor\n");
		free(server);
		return -1;
	}
	if (!start_binding(server)){
		printf("Error de enlace\n");
		free(server);
		return -1;
	}
	if (!start_listen(server)) {
		printf("Error al escuchar\n");
		free(server);
		return -1;
	}

	pthread_t t_server;
    pthread_create(&t_server,NULL, start, (void*)server);
    pthread_join(t_server, NULL);
	
	close_connection(server);
	
	printf("GG\n");
}