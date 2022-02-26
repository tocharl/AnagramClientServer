package atl.server.g49582.chat.model;

import atl.message.g49582.chat.users.Members;
import atl.message.g49582.chat.users.User;
import atl.message.g49582.message.Message;
import atl.message.g49582.message.MessageMembers;
import atl.message.g49582.message.MessageProfile;
import atl.message.g49582.message.MessageScrambleWord;
import atl.message.g49582.message.MessageStatistics;
import atl.message.g49582.message.MessageToClient;
import atl.message.g49582.message.Type;
import atl.server.g49582.anagram.exception.ModelException;
import atl.server.g49582.server.AbstractServer;
import atl.server.g49582.server.ConnectionToClient;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The <code> ChatServer </code> contains all the methods necessary to set up a
 * Instant messaging server.
 */
public class ChatServer extends AbstractServer {

    private static final int PORT = 12_345;
    static final String ID_MAPINFO = "ID";

    private static InetAddress getLocalAddress() {
        try {
            Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
            while (b.hasMoreElements()) {
                for (InterfaceAddress f : b.nextElement().getInterfaceAddresses()) {
                    if (f.getAddress().isSiteLocalAddress()) {
                        return f.getAddress();
                    }
                }
            }
        } catch (SocketException e) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "NetworkInterface error", e);
        }
        return null;
    }

    private int clientId;

    private final Members members;

    /**
     * Constructs the server. Build a thread to listen connection request.
     *
     * @throws IOException if an I/O error occurs when creating the server
     * socket.
     */
    public ChatServer() throws IOException {
        super(PORT);
        members = new Members();
        clientId = 0;
        this.listen();
    }

    /**
     * Return the list of connected users.
     *
     * @return the list of connected users.
     */
    public Members getMembers() {
        return members;
    }

    /**
     * Return the server IP address.
     *
     * @return the server IP address.
     */
    public String getIP() {
        if (getLocalAddress() == null) {
            return "Unknown";
        }
        return getLocalAddress().getHostAddress();
    }

    /**
     * Return the number of connected users.
     *
     * @return the number of connected users.
     */
    public int getNbConnected() {
        return getNumberOfClients();
    }

    /**
     * Quits the server and closes all aspects of the connection to clients.
     *
     * @throws IOException
     */
    public void quit() throws IOException {
        this.stopListening();
        this.close();
    }

    /**
     * Return the next client id.
     *
     * @return the next client id.
     */
    final synchronized int getNextId() {
        clientId++;
        return clientId;
    }
    
    private void ExecuteCommand(String message, ConnectionToClient client){
        String[] command = message.split(" ");
        try {
            switch (command[0]) {
                case "pass": {
                    executePass(client);
                    break;
                }
                case "check" : {
                    executeCheck(client, command);
                    break;
                }
                case "start" : {
                    executeStart(client);
                }
            }
        } catch (ModelException ex) {
        }
    }
    
    private void executeStart(ConnectionToClient client) throws ModelException{
        sendToClient(new MessageScrambleWord(""+client.getAnagram().getCurrentWord()+
                    " "+client.getAnagram().getNbProposal()),
                    (int)client.getInfo(ID_MAPINFO));
    }
    
    private void executeCheck(ConnectionToClient client, String[] command) throws ModelException {
        Boolean result = client.getAnagram().propose(command[1]);
        if(result) {
            sendToClient(new MessageToClient("Bravo !! "), (int)client.getInfo(ID_MAPINFO));
        } else{
            sendToClient(new MessageToClient("Mauvaise réponse. Essaie encore "),
                    (int)client.getInfo(ID_MAPINFO));
        }
        if(client.getAnagram().canAskNextWord()){
            sendStatistics(client);
            sendToClient(new MessageScrambleWord(""+client.getAnagram().nextWord()+
                    " "+client.getAnagram().getNbProposal()),
                    (int)client.getInfo(ID_MAPINFO));
        }
    }

    private void executePass(ConnectionToClient client) throws ModelException {
        String answer = "La réponse est " + client.getAnagram().pass();
        sendToClient(new MessageToClient(answer), (int)client.getInfo(ID_MAPINFO));
        sendStatistics(client);
        sendToClient(new MessageScrambleWord(""+client.getAnagram().nextWord()+
                " "+client.getAnagram().getNbProposal()),
                (int)client.getInfo(ID_MAPINFO));
    }
    
    private void sendStatistics(ConnectionToClient client) throws ModelException {
        sendToClient(new MessageStatistics(""+client.getAnagram().getNbRemainingWords()
                +" "+client.getAnagram().getNbWords()
                +" "+client.getAnagram().getNbSolvedWords()
                +" "+client.getAnagram().getNbUnsolvedWords()),
                (int)client.getInfo(ID_MAPINFO));
    }

    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
        Message message = (Message) msg;
        Type type = message.getType();
        switch (type) {
            case PROFILE:
                int memberId = (int) client.getInfo(ID_MAPINFO);
                User author = message.getAuthor();
                members.changeName(author.getName(), memberId);
                Message messageName = new MessageProfile(memberId, author.getName());
                sendToClient(messageName, memberId);
                sendToAllClients(new MessageMembers(members));
                break;
            case MAIL_TO:
                ExecuteCommand((String)message.getContent(), client);
                break;
            case MEMBERS:
                break;
            default:
                throw new IllegalArgumentException("Message type unknown " + type);
        }
        setChanged();
        notifyObservers(message);
    }

    @Override
    protected void clientConnected(ConnectionToClient client) {
        super.clientConnected(client);
        int memberId = members.add(getNextId(), client.getName(), client.getInetAddress());
        client.setInfo(ID_MAPINFO, memberId);
        sendToAllClients(new MessageMembers(members));
        try {
            sendToClient(new MessageScrambleWord(""+client.getAnagram().nextWord()
                    +" "+client.getAnagram().getNbProposal()),
                    (int)client.getInfo(ID_MAPINFO));
        } catch (ModelException ex) {
        }
        setChanged();
        notifyObservers();
    }

    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        int id = -1;
        for (User member : members) {
            if(client.getId() == member.getId()) {
                id = member.getId();
                break;
            }
        }
        members.remove(id);
    }

    @Override
    protected synchronized void clientException(ConnectionToClient client, Throwable exception) {
        super.clientException(client, exception);
        try {
            if (client.isConnected()) {
                client.sendToClient(new IllegalArgumentException("Message illisible " + exception.getMessage()));
            }
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, "Impossible d envoyer erreur au client", ex);
        }
    }

    void sendToClient(Message message, User recipient) {
        sendToClient(message, recipient.getId());
    }

    void sendToClient(Message message, int clientId) {
        boolean isConnected = false;
        for (User member : members) {
            isConnected = member.getAddress().equals(members.getUser(clientId).getAddress());
            if(isConnected) break;
        }
        ConnectionToClient client;
        if (isConnected){
            for (Thread clientConnection : getClientConnections()) {
                client = (ConnectionToClient) clientConnection;
                if ((int) client.getInfo(ID_MAPINFO) == clientId) {
                    try {
                        client.sendToClient(message);
                    } catch (IOException ex) {
                    }
                }
            }
        }
    }
}
