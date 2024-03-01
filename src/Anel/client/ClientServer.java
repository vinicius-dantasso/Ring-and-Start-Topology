package Anel.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import Anel.handlers.ClientHandler;
import Anel.handlers.ServerHandler;

public class ClientServer {
    
    ServerSocket server;
    Socket connectedClient;
    Socket client;
    String ip;
    int port;
    int nextPort;
    public static String id;

    public ClientServer(String i,String id, int p, int p2) {
        this.ip = i;
        this.port = p;
        this.nextPort = p2;
        ClientServer.id = id;
        rodar();
    }

    private void rodar() {

        try {

            // Servidor
            server = new ServerSocket(port);
            System.out.println("Servidor iniciado na porta: " + server.getLocalPort());

            System.out.println("Aguardando conexão do cliente...");

            // Espera a conexão dos clientes
            @SuppressWarnings("resource")
            Scanner sc = new Scanner(System.in);
            sc.nextLine();

            // Cliente
            client = new Socket(ip,nextPort);
            ClientHandler ch = new ClientHandler(client);
            Thread tc = new Thread(ch);
            tc.start();

            
            // Conexão do servidor
            connectedClient = server.accept();

            ServerHandler h = new ServerHandler(connectedClient, ch);
            Thread t = new Thread(h);
            t.start();

        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new ClientServer("127.0.0.1","P1", 5001, 5002);
        //new ClientServer("127.0.0.2","P2", 5002, 5001);
        //new ClientServer("127.0.0.3","P3", 5003, 5004);
        //new ClientServer("127.0.0.4","P4", 5004, 5001);
    }

}
