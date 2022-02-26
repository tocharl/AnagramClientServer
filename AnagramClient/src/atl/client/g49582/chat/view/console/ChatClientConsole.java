package atl.client.g49582.chat.view.console;

import atl.client.g49582.chat.model.ChatClient;
import atl.client.g49582.exception.ViewException;
import atl.client.g49582.view.View;
import atl.message.g49582.chat.users.User;
import atl.message.g49582.message.Message;
import atl.message.g49582.message.Type;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The <code> ChatClientConsole </code> contains all the methods necessary view
 * in console mode the instant messaging client side.
 */
public class ChatClientConsole implements Observer {

    
    /**
     * Entry points to the instant messaging client side.
     *
     * @param args no arguments needed.
     */
    public static void main(String[] args) {
        ChatClient client = null;
        View view = new View();
        try {
            String host;
            if (view.defaultHost()) {
                host = "localhost";
            } else {
                host = view.askHost();
            }
            int port;
            if (view.defaultPort()) {
                port = 12345;
            } else {
                port = Integer.parseInt(view.askPort());
            }
            String name = view.askLogin();
            String password = "";
            client = new ChatClient(host, port, name, password);
            ChatClientConsole console = new ChatClientConsole(client);
            view.start();
            while(true)
                console.askCommand(view, client);
            
        } catch (IOException ex) {
            Logger.getLogger(ChatClientConsole.class.getName()).log(Level.SEVERE, "Main error", ex);
            try {
                client.quit();
            } catch (NullPointerException | IOException clientEx) {
                Logger.getLogger(ChatClientConsole.class.getName()).log(Level.SEVERE, "Quit client error", clientEx);
            }
            System.exit(0);
        } catch (ViewException ex) {
        }
    }

    private final ChatClient model;
    private final DateTimeFormatter formatter;

    /**
     * Constructs the console view. Subscribes to the instant messaging client.
     *
     * @param client instant messaging client.
     */
    public ChatClientConsole(ChatClient client) {
        this.model = client;
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.model.addObserver(this);
    }

    /**
     * Asks to the user a command in console.
     * If the command is list, the console print the list of all connected users.
     * If the command is send, the client sends a message to the given connected user.
     * If the command is quit, the client disconnect from the server.
     */
    private void askCommand(View view, ChatClient client) {
        try {
            String command = view.askCommand();
            switch (command) {
                case "help":
                    view.displayHelp();
                    break;
                case "quit":
                    client.quit();
                    System.exit(0);
                default:
                    client.sendMessage(User.ADMIN, command);
            }
        } catch (ViewException e) {
            view.displayError(e.getMessage());
        } catch (IOException ex) {
            System.err.println(ex.getMessage());;
        }
    }


    private void printUsage(View view) {
        System.out.println("");
        System.out.println("Usage : ");
        System.out.println("\tAfficher la liste de utilisateurs connectés\t:\tlist");
        System.out.println("\tSe deconnecter\t:\tquit");
        System.out.println("");
        view.displayHelp();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null) {
            Message message = (Message) arg;
            String content = (String)message.getContent();
            if (message.getType().equals(Type.CLIENT)) {
                System.out.println(content);
            }
            String[] split = content.split(" ");
            if (message.getType().equals(Type.SCRAMBLE)){
                System.out.println("Quel est l'anagramme de " + split[0]);
                System.out.println(split[1] + " proposition(s) pour ce mot.");
            }
            if (message.getType().equals(Type.STAT)){
                System.out.println("Il reste " + split[0] + 
                        " mot(s) à deviner sur les " + split[1] + " mots disponibles");
                System.out.println("Vous avez trouvé " + split[2] + 
                        " mot(s) et échoué sur " + split[3] + " mot(s) ");
            }
        }
    }

}
