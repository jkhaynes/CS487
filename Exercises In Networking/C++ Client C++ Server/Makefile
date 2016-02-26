CC = gcc
OBJECTS = client.o util.o
OBJECTS2 = server.o util.o

all: client server

client : $(OBJECTS)
	$(CC) $(INCLUDES) $(OBJECTS) -o client

server : $(OBJECTS2)
	$(CC) $(OBJECTS2) -o server	

client.o : client.c
	$(CC) -c client.c

server.o : server.c
	$(CC) -c server.c

util.o : util.c
	$(CC) -c util.c

clean:	
	rm -f *.o client server
	
	