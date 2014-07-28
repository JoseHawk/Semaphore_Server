#define WIN32_LEAN_AND_MEAN

//Necesario para que funcione getaddrinfo
#define _WIN32_WINNT 0x501
#include <WS2tcpip.h>

typedef unsigned char BYTE;

//Librerias necesarias
#include <windows.h>
#include <winsock2.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <conio.h>

#include <iostream>
#include <string>

using namespace std;

#define DEFAULT_BUFFER_LENGTH 2
#define Puerto1 "5000"
#define Puerto2 "6000"
#define PuertoRecepcion "7000"

SOCKET conectarSocketPuerto(int puerto){

    //Creamos el Socket
        SOCKET ConnectSocket = INVALID_SOCKET;
        WSADATA wsaData;

        int iResult = WSAStartup(MAKEWORD(2,2), &wsaData);

        struct addrinfo    *result = NULL, *ptr = NULL, hints;


        ZeroMemory(&hints, sizeof(hints));
            hints.ai_family = AF_UNSPEC;
            hints.ai_socktype = SOCK_STREAM;
            hints.ai_protocol = IPPROTO_TCP;

        if(puerto==1){
            iResult = getaddrinfo("localHost", Puerto1, &hints, &result);
        }else{
            iResult = getaddrinfo("localHost", Puerto2, &hints, &result);
        }

        ptr = result;

        //Conectamos el socket
        ConnectSocket = socket(ptr->ai_family, ptr->ai_socktype, ptr->ai_protocol);

        iResult = connect(ConnectSocket, ptr->ai_addr, (int)ptr->ai_addrlen);
        freeaddrinfo(result);

        return ConnectSocket;

}
SOCKET iniciarServidor(){

    WSADATA wsaData;
    int iResult;

    SOCKET ListenSocket = INVALID_SOCKET;
    SOCKET ClientSocket = INVALID_SOCKET;

    struct addrinfo *result = NULL;
    struct addrinfo hints;

    int iSendResult;
    char bufferEntrada[DEFAULT_BUFFER_LENGTH];
    int recvbuflen = DEFAULT_BUFFER_LENGTH;

    iResult = WSAStartup(MAKEWORD(2,2), &wsaData);

    ZeroMemory(&hints, sizeof(hints));
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_protocol = IPPROTO_TCP;
    hints.ai_flags = AI_PASSIVE;

    iResult = getaddrinfo(NULL, PuertoRecepcion, &hints, &result);

    ListenSocket = socket(result->ai_family, result->ai_socktype, result->ai_protocol);

    iResult = bind( ListenSocket, result->ai_addr, (int)result->ai_addrlen);

    freeaddrinfo(result);

    iResult = listen(ListenSocket, SOMAXCONN);
    return ListenSocket;
}

void lecturaObtencion(SOCKET socket){


   char bufferEntrada[DEFAULT_BUFFER_LENGTH];
   int nbs = recv(socket,bufferEntrada,DEFAULT_BUFFER_LENGTH,0);

    if(nbs>0){

       char msg[DEFAULT_BUFFER_LENGTH];
       memset(&msg,0,sizeof(msg));
       strncpy(msg,bufferEntrada,nbs);

    }


}

void lecturaLiberacion(SOCKET socket){

   //Recibimos
   char bufferEntrada[DEFAULT_BUFFER_LENGTH];
   int nbs = recv(socket,bufferEntrada,DEFAULT_BUFFER_LENGTH,0);

   if(nbs>0){

     char msg[DEFAULT_BUFFER_LENGTH];
     memset(&msg,0,sizeof(msg));
     strncpy(msg,bufferEntrada,nbs);

    }
}

int main(int argc, char **argv){

    int valorMenu;
    int valorRecurso;
    int posesionRecurso[3];
    posesionRecurso[0]=0;
    posesionRecurso[1]=0;
    posesionRecurso[2]=0;
    int conexion=1;
    SOCKET server;

    while(conexion==1){
        //Iniciamos server para posterior obtencion
        //server=iniciarServidor();

        //Mostramos el menu
        printf("1.- Access to resource (DOWN) 1 \n2.- Release resource (UP) 2 \n3.- Disconnect 3\n");

        //Elegimos un valor del menu
        do{
            scanf("%d",&valorMenu);
        }while(valorMenu<1 || valorMenu>3);


        if(valorMenu==1){       //DOWN
            valorMenu=0;
            printf("Select the resource to access \n");
            //Elegimos un valor del recurso
            do{
                scanf("%d",&valorRecurso);
            }while(valorRecurso<1 || valorRecurso>3);

            //Comprobamos si lo tiene
            if(posesionRecurso[valorRecurso-1]==1){
                printf("You have this resource right now \n");
            }else{  //Si no lo tiene...

                //Obtenemos el recurso en funcion del valor
                if(valorRecurso==1){
                    SOCKET conexion1 = conectarSocketPuerto(1);
                    //Creamos buffer de salida
                    BYTE mensaje[1]= {0};
                    //Modificamos valores de buffer de salida
                    mensaje[0]=(BYTE)4;
                    send(conexion1, (char*)mensaje, strlen((char*)mensaje), 0);
                    lecturaObtencion(conexion1);
                    printf("Resource 1 obtained \n");

                }

                if(valorRecurso==2){
                    SOCKET conexion2 = conectarSocketPuerto(1);
                    //Creamos buffer de salida
                    BYTE mensaje[1]= {0};
                    //Modificamos valores de buffer de salida
                    mensaje[0]=(BYTE)5;
                    send(conexion2, (char*)mensaje, strlen((char*)mensaje), 0);
                    //Esperamos la respuesta
                    lecturaObtencion(conexion2);
                    printf("Resource 2 obtained \n");

                }

                if(valorRecurso==3){
                    SOCKET conexion3 = conectarSocketPuerto(2);
                    //Creamos buffer de salida
                    BYTE mensaje[1]= {0};
                    //Modificamos valores de buffer de salida
                    mensaje[0]=(BYTE)6;
                    send(conexion3, (char*)mensaje, strlen((char*)mensaje), 0);
                    //Esperamos la respuesta
                    lecturaObtencion(conexion3);
                    printf("Resource 3 obtained \n");

                }
                //Indicamos que poseemos el recurso
                posesionRecurso[valorRecurso-1]=1;

            }
        }       //FINAL DEL DOWN

        if(valorMenu==2){       //UP

            valorMenu=0;
            printf("Select the resource to release \n");
            //Elegimos un valor del recurso
            do{
                scanf("%d",&valorRecurso);
            }while(valorRecurso<1 || valorRecurso>3);

            //Comprobamos si no lo tiene
            if(posesionRecurso[valorRecurso-1]==0){
                printf("You don't have this resource right now \n");
            }else{

                if(valorRecurso==1){
                    SOCKET conexion1 = conectarSocketPuerto(1);
                    //Creamos buffer de salida
                    BYTE mensaje[1]= {0};
                    //Modificamos valores de buffer de salida
                    mensaje[0]=(BYTE)10;
                    send(conexion1, (char*)mensaje, strlen((char*)mensaje), 0);
                    //Esperamos la respuesta
                    lecturaLiberacion(conexion1);
                    printf("Resource 1 released \n");

                }

                if(valorRecurso==2){
                    SOCKET conexion2 = conectarSocketPuerto(1);
                    //Creamos buffer de salida
                    BYTE mensaje[1]= {0};
                    //Modificamos valores de buffer de salida
                    mensaje[0]=(BYTE)11;
                    send(conexion2, (char*)mensaje, strlen((char*)mensaje), 0);
                    //Esperamos la respuesta
                    lecturaLiberacion(conexion2);
                    printf("Resource 2 released \n");

                }

                if(valorRecurso==3){
                    SOCKET conexion3 = conectarSocketPuerto(2);
                    //Creamos buffer de salida
                    BYTE mensaje[1]= {0};
                    //Modificamos valores de buffer de salida
                    mensaje[0]=(BYTE)12;
                    send(conexion3, (char*)mensaje, strlen((char*)mensaje), 0);
                    //Esperamos la respuesta
                    lecturaLiberacion(conexion3);
                    printf("Resource 3 released \n");

                }

                //Indicamos que no poseemos el recurso
                posesionRecurso[valorRecurso-1]=0;

            }

        }       //FINAL DEL UP

        if(valorMenu==3){
            conexion=0;
        }

    }

}


