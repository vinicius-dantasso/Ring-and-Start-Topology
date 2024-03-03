package Estrela.handler;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import Estrela.client.ClientServer;

public class ClientHandler implements Runnable {

    public static Socket client;
    private boolean connection = true;
    public static PrintStream printer;

    public ClientHandler(Socket c) {
        ClientHandler.client = c;
    }

    @Override
    public void run() {
        
        try {

            System.out.println("O cliente " + ClientServer.id + " se conectou.");

            Scanner sc = new Scanner(System.in);

            printer = new PrintStream(client.getOutputStream());

            String msg;
            boolean noProblems = true;

            while(connection) {
                
                System.out.println("=====================================");
                System.out.println("Digite uma mensagem para alguém ou para todos te ouvirem!");
                System.out.println("Ao final de cada mensagem digite /{?cast}/{P?}");
                System.out.println("=====================================");

                msg = sc.nextLine();
                msg = msg.concat("/" + ClientServer.id);

                if(msg.equalsIgnoreCase("fim")) {
                    connection = false;
                }
                else {

                    while(!msg.contains("unicast") && noProblems) {

                        if(msg.contains("broadcast")) {
                            noProblems = false;
                        }
                        else {
                            System.out.println("Comando incorreto! Por favor escreva novamente.");
                            msg = sc.nextLine();
                            msg = msg.concat("/" + ClientServer.id);
                        }
                        
                    }

                    System.out.println("Mensagem enviada...");
                    printer.println(msg);
                    noProblems = true;

                }

            }

            sc.close();
            printer.close();
            client.close();

        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }
    
}
