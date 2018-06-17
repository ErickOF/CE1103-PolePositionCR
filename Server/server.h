#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>

#define PORT 10009

struct Server {
	int sockfd;
	struct sockaddr_in address;
	int clients[4];
	struct sockaddr_in clientsAddress;
	socklen_t addr_size;
	pid_t childpid;
	bool started;
	bool listening;
};

struct Data {
	struct Server *server;
	int pos;
};

bool start_connection(struct Server *server) {
	for (int i = 0; i < 4; ++i) {
		server->clients[i] = -1;
	}

	server->sockfd = socket(AF_INET, SOCK_STREAM, 0);
	if (server->sockfd < 0) {
		return false;
	}

	memset(&(server->address), '\0', sizeof(server->address));
	server->address.sin_family = AF_INET;
	server->address.sin_port = htons(PORT);
	server->address.sin_addr.s_addr = inet_addr("127.0.0.1");
	server->started = true;
	server->listening = true;
	return true;
}

bool start_binding(struct Server *server) {
	return bind(server->sockfd, (struct sockaddr*)&(server->address), sizeof(server->address)) >= 0;
}

bool start_listen(struct Server *server) {
	return listen(server->sockfd, 4) == 0;
}

void* listen_client(void* arg) {
	struct Data *data = (struct Data*) arg;
	struct Server *server = data->server;
	int i = data->pos;
	char buffer[1024];
	//close(server->sockfd);
	while (server->listening) {
		if (recv(server->clients[i], buffer, 1024, 0) >= 0) {
			printf("Client %s\n", buffer);
			if (send(server->clients[i], "rcv\n", strlen("rcv"), 0) < 0) {
				printf("Error to send msg\n");
			} else {
				printf("Msg sended\n");
			}
		} else {
			break;
		}
	}
	return NULL;
}

void* start(void* arg) {
	struct Server *server = (struct Server*) arg;
	printf("Iniciado\n");
	while (server->started) {
		printf("Listen\n");
		int newClient = accept(server->sockfd, (struct sockaddr*)&(server->clientsAddress),
								&(server->addr_size));
		if (newClient >= 0) {
			int i = 0;
			for (; i < 4; ++i){
				if(server->clients[i] == - 1){
					server->clients[i] = newClient;
					break;
				}
			}
			struct Data data;
			data.server = server;
			data.pos = i;
			pthread_t client_t;
			pthread_create(&client_t, NULL, listen_client, (void*)&data);
			pthread_join(client_t, NULL);
		}
	}
	
	printf("End\n");

	int i = 0;
	for (; i < 4; ++i){
		close(server->clients[i]);
	}

	free(server);
	return NULL;
}

void close_connection(struct Server* server) {
	server->listening = false;
	server->started = false;
}