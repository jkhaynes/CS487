#include "util.hh"

#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <string>
#include <iostream>

using namespace std;

#define MAX_SIZE 1024
#define HASH_SIZE 256
#define KEY 3

int main() {
	int sockfd; // socket file descriptor
	int portno; // port number
	struct sockaddr_in serv_addr;
	struct hostent *server;

	sockfd = socket(AF_INET, SOCK_STREAM, 0); // generate file descriptor
	if (sockfd < 0)
	    perror("ERROR opening socket");

	server = gethostbyname("127.0.0.1"); //the ip address (or server name) of the listening server.
	if (server == NULL) {
	    fprintf(stderr,"ERROR, no such host\n");
	    exit(0);
	}

	bzero((char *) &serv_addr, sizeof(serv_addr));
	serv_addr.sin_family = AF_INET;
	bcopy((char *)server->h_addr, (char *)&serv_addr.sin_addr.s_addr, server->h_length);
	serv_addr.sin_port = htons(4447);

	if (connect(sockfd,(struct sockaddr *)&serv_addr,sizeof(serv_addr)) < 0)
	    perror("ERROR connecting");

	char bufferMes[MAX_SIZE] = {0};		
	char bufferHash[HASH_SIZE] = {0};
	char bufferSig[HASH_SIZE] = {0};						

  	string input_string;
  	cout << "Enter message: ";
  	cin.getline (bufferMes, MAX_SIZE);

	int wInt;

	//Send original message
	wInt = write(sockfd, bufferMes, MAX_SIZE);
	if(wInt < 0) perror("Cannot write to socket");

	//Hash message
	hash(bufferMes, bufferHash);

	//Encrypt message
	encryption(bufferHash, KEY, bufferSig);

	//Send signature to server
	wInt = write(sockfd, bufferSig, HASH_SIZE);
	if(wInt < 0) perror("Cannot write to socket");


	//Recieve true/false result from server
	char result[HASH_SIZE];
	int rInt; 
 
	rInt = recv(sockfd, result, sizeof(result), 0); // similar to read(), but return -1 if socket closed
	if(rInt < 0) perror("Cannot read to socket");
	result[rInt] = '\0'; // set null terminal
	printf("Message: %s\n", result);

	return 0;

}
