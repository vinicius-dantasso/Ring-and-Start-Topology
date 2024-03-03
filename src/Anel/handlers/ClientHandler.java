package Anel.handlers;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import Anel.client.ClientServer;
import Anel.object.Package;

public class ClientHandler implements Runnable{

    private Socket client;
    private boolean connection = true;
    private Package<String> pack;
    // Envio de Mensagens
    public ObjectOutputStream output;

    public ClientHandler(Socket c) {
        this.client = c;
    }

    @Override
    public void run() {
        
        try{

            System.out.println("O cliente conectou ao servidor");

            Scanner sc = new Scanner(System.in);
            output = new ObjectOutputStream(client.getOutputStream());

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

                    pack = new Package<String>(msg);
                    output.writeObject(pack);
                    output.flush();
                    System.out.println("Mensagem enviada!");
                    noProblems = true;

                }

            }

            sc.close();
            output.close();
            client.close();
            System.out.println("Conexão finalizada...");

        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }

    public Socket getClient() {
        return this.client;
    }
    
}
