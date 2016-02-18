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

	char bufferMes[1024] = {0};								

	//printf("Please enter the message: ");
  	//fgets(bufferMes, 1023, stdin);

  	string input_string;
  	cout << "Enter message: ";
  	cin.getline (bufferMes, 1024);

	int wbytes;

	wbytes = write(sockfd, bufferMes, 1024);
	if(wbytes < 0) perror("Cannot write to socket");

	char bufferHash[256] = {0};
	char bufferSig[256] = {0};

	hash(bufferMes, bufferHash);
	encryption(bufferHash, 3, bufferSig);

	printf("%s\n", bufferMes);
	printf("%s\n", bufferSig);

	wbytes = write(sockfd, bufferSig, 256);
	if(wbytes < 0) perror("Cannot write to socket");

	char rbuff[256];
	int rbytes; 
 
	rbytes = recv(sockfd, rbuff, sizeof(rbuff), 0); // similar to read(), but return -1 if socket closed
	if(rbytes < 0) perror("Cannot read to socket");
	rbuff[rbytes] = '\0'; // set null terminal
	printf("Message: %s\n", rbuff);

	return 0;

}
