#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>

// PORT to listen
#define PORT 9003

// Struct to Server
struct Server {
	int sockfd;
	struct sockaddr_in address;
	int clients[4];
	struct sockaddr_in clientsAddress;
	socklen_t addr_size;
	pid_t childpid;
	bool started;
	bool listening;
	char *players[4];
	char *colors[4];
};

// Data to send when connection is accepted
struct Data {
	struct Server *server;
	int pos;
};

// Create socket
bool start_connection(struct Server *server) {
	// Init all server variables
	for (int i = 0; i < 4; ++i) {
		server->clients[i] = -1;
		server->players[i] = (char*) malloc(30*sizeof(char));
		server->players[i] = "";
		server->colors[i] = (char*) malloc(6*sizeof(char));
		server->colors[i] = "";
	}
	// Create socket
	server->sockfd = socket(AF_INET, SOCK_STREAM, 0);
	if (server->sockfd < 0) {
		return false;
	}
	// Setup server params
	memset(&(server->address), '\0', sizeof(server->address));
	server->address.sin_family = AF_INET;
	server->address.sin_port = htons(PORT);
	server->address.sin_addr.s_addr = inet_addr("127.0.0.1");
	server->started = true;
	server->listening = true;
	return true;
}

// Bind socket whith kernel
bool start_binding(struct Server *server) {
	return bind(server->sockfd, (struct sockaddr*)&(server->address),
				sizeof(server->address)) >= 0;
}

// Start listening for possible connections
bool start_listen(struct Server *server) {
	return listen(server->sockfd, 4) == 0;
}

char* get_avaible_colors(struct Server *server) {
	char *colors[4] = {"Blue", "Purple", "Red", "White"};
	char *avaible_colors = (char*) malloc(22*sizeof(char));
	char *sep = ",";
	for (int i=0; i < 4; i++) {
		bool avaible = true;
		for (int j = 0; j < 4; j++) {
			if (strcmp(server->colors[j], colors[i]) == 0) {
				avaible = false;
				break;
			}
		}
		if (avaible) {
			strcat(avaible_colors, colors[i]);
			strcat(avaible_colors, sep);
		}
	}
	// Free memory
	for (int i = 0; i < 4; ++i) {
		colors[i] = NULL;
		free(colors[i]);
	}
	sep = NULL;
	free(sep);

	return avaible_colors;
}

// Listen clients
void* listen_client(void* arg) {
	// Server information
	struct Data *data = (struct Data*) arg;
	struct Server *server = data->server;
	int i = data->pos;
	// Msg buffer
	char buffer[1024];
	//close(server->sockfd);
	while (server->listening) {
		// Wait for msg
		if (recv(server->clients[i], buffer, 1024, 0) >= 3) {
			printf("Client %s\n", buffer);
			// Create msg container
			char *words[4];
		    for (int j = 0; j < 4; ++j) {
		        words[j] = (char*) malloc(30*sizeof(char));
		    }
		    // Split msg
		    char *pch;
		    pch = strtok(buffer, ",");
		    // Get every word
		  	int j = 0;
		    while ((pch != NULL) && (j < 4)) {
		    	// Set word
		        words[j] = pch;
		        printf("%d: %s\n", j, words[j]);
		        j++;
		        // Next word
		        pch = strtok(NULL, ",");
		    }
		    // Client want avaible colors
		    if (strcmp(words[0], "get") == 0) {
		    	// Get avaible colors
		    	char* colors = get_avaible_colors(server);
		    	// Send colors to user
		    	int status = send(server->clients[i], colors, strlen(colors), 0);
				printf("Msg: %s, status: %d\n", colors, status);
		    }
		    else if (strcmp(words[0], "new") == 0) {
			    // Config players
			    server->players[i] = words[1];
			    server->colors[i] = words[2];
			    // Free memory
			    for (int j = 0; j < 4; ++j) {
			    	words[j] = NULL;
			        free(words[j]);
			    }
			    int status = send(server->clients[i], "rcv", strlen("rcv"), 0);
			    printf("Msg: rcv, status: %d\n", status);
			} else {
				printf("Unknown\n");
			}
		}
	}
	return NULL;
}

// Start server
void* start(void* arg) {
	// Get server
	struct Server *server = (struct Server*) arg;
	while (server->started) {
		// Accept new client
		int newClient = accept(server->sockfd, (struct sockaddr*)&(server->clientsAddress),
								&(server->addr_size));
		// If client was accepted
		if (newClient >= 0) {
			// Current client number
			int i = 0;
			// Search for space in clients
			for (; i < 4; ++i){
				if(server->clients[i] == - 1){
					server->clients[i] = newClient;
					break;
				}
			}
			// Save server info
			struct Data data;
			data.server = server;
			data.pos = i;
			// Start to listen that client
			pthread_t client_t;
			pthread_create(&client_t, NULL, listen_client, (void*)&data);
			pthread_detach(client_t);
		}
	}
	// Close client connections
	int i = 0;
	for (; i < 4; ++i){
		close(server->clients[i]);
	}
	// Delete server
	free(server);
	return NULL;
}

// Close server connection
void close_connection(struct Server* server) {
	server->listening = false;
	server->started = false;
}