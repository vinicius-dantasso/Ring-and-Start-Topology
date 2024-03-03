package Anel.handlers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import Anel.client.ClientServer;
import Anel.object.Package;

public class ServerHandler implements Runnable {

    ClientHandler nextClient;
    Socket client;
    boolean connection = true;
    // Recebimento de Objetos
    ObjectInputStream input;

    public ServerHandler(Socket c, ClientHandler ch) {
        this.client = c;
        this.nextClient = ch;
    }

    @Override
    public void run() {
        
        String receivedMsg;

        System.out.println("Conexão com o cliente: " + client.getLocalAddress());

        try {

            input = new ObjectInputStream(client.getInputStream());

            while(connection) {

                // Mensagem recebida do cliente
                @SuppressWarnings({ "unchecked", "rawtypes" })
                Package<String> pack = (Package) input.readObject();
                receivedMsg = pack.getMessage();

                // Separa o id do cliente da mensagem
                String[] splitMsg = receivedMsg.split("/");
                String clientId = splitMsg[2];
                String castType = splitMsg[1];
                String finalMsg = splitMsg[0];

                // Caso a mensagem deva ser recebida por este id
                if(clientId.equals(ClientServer.id) && castType.equals("unicast")) {
                    System.out.println("Mensagem recebida de " + splitMsg[3] + ": " + finalMsg);
                }
                // Caso a mensagem deva ser encaminhada para outro id
                else if(!clientId.equals("anything") && castType.equals("unicast")){
                    System.out.println("Encaminhando Mensagem...");
                    nextClient.output.writeObject(pack);
                    nextClient.output.flush();
                }

                // Caso a mensagem seja broadcast
                if(castType.equals("broadcast") && !ClientServer.id.equals(clientId)) {
                    System.out.println("Mensagem Broadcast de " + splitMsg[splitMsg.length - 1] + ": " + finalMsg);
                    nextClient.output.writeObject(pack);
                    nextClient.output.flush();
                }
                
                // Caso queira finalizar a conexão
                if(receivedMsg.equalsIgnoreCase("fim")) {
                    connection = false;
                }

            }

            input.close();
            client.close();

        }
        catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    
}
