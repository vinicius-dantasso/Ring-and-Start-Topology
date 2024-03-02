package Estrela.handler;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Estrela.client.ClientServer;

public class ServerHandler implements Runnable {

    private Socket client;
    private boolean connection = true;
    private Scanner sc = null;
    private static int cont = -1;
    public static List<Socket> connections = new ArrayList<>();

    public ServerHandler(Socket c) {
        this.client = c;
        connections.add(c);
    }

    @SuppressWarnings("resource")
    @Override
    public void run() {
        
        String received;

        try {

            sc = new Scanner(client.getInputStream());
            String splitMsg[];
            String clientId, castType, finalMsg;
            Socket sender = null;
            PrintStream printer = null;

            while(connection) {
                
                received = sc.nextLine();
                splitMsg = received.split("/");
                clientId = splitMsg[2];
                castType = splitMsg[1];
                finalMsg = splitMsg[0];

                if(cont == 2) {
                    castType = "cast";
                    cont = -1;
                }

                if(clientId.equals(ClientServer.id) && castType.equals("unicast")) {
                    System.out.println("Mensagem recebida de " + splitMsg[3] + ": " + finalMsg);
                }
                else if(!clientId.equals("anything") && castType.equals("unicast")) {

                    System.out.println("Encaminhando Mensagem de "+splitMsg[3]+" para "+clientId+"...");

                    switch(clientId) {

                        case "P2":
                            for(Socket cli : connections) {
                                String address = cli.getLocalAddress().toString();
                                if(address.equals("/127.0.0.2")) {
                                    
                                    sender = new Socket("127.0.0.2", 5002);
                                    printer = new PrintStream(sender.getOutputStream());
                                    printer.println(received);

                                }
                            }
                        break;

                        case "P3":
                            for(Socket cli : connections) {
                                String address = cli.getLocalAddress().toString();
                                if(address.equals("/127.0.0.3")) {
                                    
                                    sender = new Socket("127.0.0.3", 5003);
                                    printer = new PrintStream(sender.getOutputStream());
                                    printer.println(received);

                                }
                            }
                        break;

                        case "P4":
                            for(Socket cli : connections) {
                                String address = cli.getLocalAddress().toString();
                                if(address.equals("/127.0.0.4")) {
                                    
                                    sender = new Socket("127.0.0.4", 5004);
                                    printer = new PrintStream(sender.getOutputStream());
                                    printer.println(received);

                                }
                            }
                        break;

                        default:
                        break;

                    }

                }

                if(castType.equals("broadcast") && !ClientServer.id.equals(clientId)) {
                    
                    switch(ClientServer.id) {

                        case "P1":

                        switch(clientId) {

                            case "P2":

                            if(cont == -1){
                                System.out.println("Mensagem Broadcast de "+splitMsg[splitMsg.length - 1]+": "+finalMsg);
                                cont++;
                            }

                            if(cont == 0) {
                                sender = new Socket("127.0.0.1",5003);
                                printer = new PrintStream(sender.getOutputStream());
                                printer.println(received);
                                cont++;
                            }
                            else if(cont == 1) {
                                sender = new Socket("127.0.0.1",5004);
                                printer = new PrintStream(sender.getOutputStream());
                                printer.println(received);
                                cont++;
                            }

                            break;

                            case "P3":

                            if(cont == -1){
                                System.out.println("Mensagem Broadcast de "+splitMsg[splitMsg.length - 1]+": "+finalMsg);
                                cont++;
                            }

                            if(cont == 0) {
                                sender = new Socket("127.0.0.1",5002);
                                printer = new PrintStream(sender.getOutputStream());
                                printer.println(received);
                                cont++;
                            }
                            else if(cont == 1) {
                                sender = new Socket("127.0.0.1",5004);
                                printer = new PrintStream(sender.getOutputStream());
                                printer.println(received);
                                cont++;
                            }

                            break;

                            case "P4":

                            if(cont == -1){
                                System.out.println("Mensagem Broadcast de "+splitMsg[splitMsg.length - 1]+": "+finalMsg);
                                cont++;
                            }

                            if(cont == 0) {
                                sender = new Socket("127.0.0.1",5002);
                                printer = new PrintStream(sender.getOutputStream());
                                printer.println(received);
                                cont++;
                            }
                            else if(cont == 1) {
                                sender = new Socket("127.0.0.1",5003);
                                printer = new PrintStream(sender.getOutputStream());
                                printer.println(received);
                                cont++;
                            }

                            break;

                        }

                        break;

                        case "P2":
                        System.out.println("Mensagem Broadcast de "+splitMsg[splitMsg.length - 1]+": "+finalMsg);
                        ClientHandler.printer = new PrintStream(ClientHandler.client.getOutputStream());
                        ClientHandler.printer.println(received);
                        break;

                        case "P3":
                        System.out.println("Mensagem Broadcast de "+splitMsg[splitMsg.length - 1]+": "+finalMsg);
                        ClientHandler.printer = new PrintStream(ClientHandler.client.getOutputStream());
                        ClientHandler.printer.println(received);
                        break;

                        case "P4":
                        System.out.println("Mensagem Broadcast de "+splitMsg[splitMsg.length - 1]+": "+finalMsg);
                        ClientHandler.printer = new PrintStream(ClientHandler.client.getOutputStream());
                        ClientHandler.printer.println(received);
                        break;

                        default: break;

                    }

                }

                if(received.equalsIgnoreCase("fim")) {
                    connection = false;
                }

            }

            sc.close();
            sender.close();
            printer.close();
            client.close();

        }
        catch(IOException e) {
            e.printStackTrace();
        } 

    }
    
}
