#include "util.hh"

#include <stdio.h>
#include <string.h>

#define N 256

void hash(char* message, char* output)
{
	//Hash
	int i = 0;
    while (i < strlen(message)){
  		output[i % N] += message[i];
  		i++;
  	}
}

//Encrypt the given message and store encryption in output array
void encryption(char* message, int key, char* output)
{
	int i = 0;
	while (message[i] != '\0' && i < strlen(message)){
		output[i] = message[i] + key;
		i++;
	}
}

//Decrypt the given message and store the decryption in output array
void decryption(char* message, int key, char* output)
{
	encryption(message, -key, output);
}

