package Estrela.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Estrela.handler.*;

public class ClientServer {
    
    ServerSocket server;
    Socket connectedClient;
    Socket client;
    String ip;
    int port;
    int nextPort;
    public static int id;

    public ClientServer(String i, int id, int p, int p2) {
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
        new ClientServer("127.0.0.1",1,5001,5001);
        //new ClientServer("127.0.0.2",2,5002,5001);
        //new ClientServer("127.0.0.3",3,5003,5001);
        //new ClientServer("127.0.0.4",4,5004,5001);
    }

}
