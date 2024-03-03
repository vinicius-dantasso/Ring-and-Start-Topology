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
    public static List<Socket> connections = new ArrayList<>();

    public ServerHandler(Socket c) {
        this.client = c;
        connections.add(c);
    }

    @Override
    public void run() {
        
        String received;

        try {

            sc = new Scanner(client.getInputStream());
            String splitMsg[];
            String castType, finalMsg;
            int clientId;
            Socket sender = null;
            PrintStream printer = null;

            while(connection) {
                
                received = sc.nextLine();
                splitMsg = received.split("/");
                clientId = Integer.parseInt(splitMsg[2]);
                castType = splitMsg[1];
                finalMsg = splitMsg[0];

                if(clientId == ClientServer.id && castType.equals("unicast")) {
                    System.out.println("Mensagem recebida de " + splitMsg[3] + ": " + finalMsg);
                }
                else if(clientId != 999 && castType.equals("unicast")) {

                    System.out.println("Encaminhando Mensagem de "+splitMsg[3]+" para "+clientId+"...");

                    sender = new Socket("localhost", 5000 + clientId);
                    printer = new PrintStream(sender.getOutputStream());
                    printer.println(received);

                }

                if(castType.equals("broadcast")) {

                    if(ClientServer.id != 1) {
                        System.out.println("Mensagem Broadcast de P"+splitMsg[splitMsg.length - 1]+": "+finalMsg);
                    }
                    else{

                        for(int i=1;i<=connections.size();i++) {

                            if(ClientServer.id == 1 && i == 1 && clientId != 1){
                                System.out.println("Mensagem Broadcast de "+splitMsg[splitMsg.length - 1]+": "+finalMsg);
                            }
                            else if(i != clientId) {
                                sender = new Socket("localhost", 5000 + i);
                                printer = new PrintStream(sender.getOutputStream());
                                printer.println(received);
                            }
    
                        }

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
