package Estrela.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Estrela.handler.*;

public class ClientServer {
    
    ServerSocket server;
    Socket connectedClient;
    Socket client;
    String ip;
    int port;
    int nextPort;
    public static String id;
    public static List<Socket> connections = new ArrayList<>();

    public ClientServer(String i, String id, int p, int p2) {
        this.ip = i;
        this.port = p;
        this.nextPort = p2;
        ClientServer.id = id;
        rodar();
    }

    private void rodar() {

        try {

            server = new ServerSocket(port);
            System.out.println("Servidor iniciado na porta: " + server.getLocalPort());

            @SuppressWarnings("resource")
            Scanner sc = new Scanner(System.in);
            sc.nextLine();

            client = new Socket(ip,nextPort);
            ClientHandler ch = new ClientHandler(client);
            Thread tc = new Thread(ch);
            tc.start();

            while(true) {
                connectedClient = server.accept();
                ServerHandler sh = new ServerHandler(connectedClient);
                Thread ts = new Thread(sh);
                ts.start();
            }

        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new ClientServer("127.0.0.1","P1",5001,5001);
        //new ClientServer("127.0.0.2","P2",5002,5001);
        //new ClientServer("127.0.0.3","P3",5003,5001);
        //new ClientServer("127.0.0.4","P4",5004,5001);
    }

}
