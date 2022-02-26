package atl.client.g49582.chat.model;

import atl.message.g49582.chat.users.Members;
import atl.message.g49582.chat.users.User;
import atl.client.g49582.client.AbstractClient;
import atl.message.g49582.message.Message;
import atl.message.g49582.message.MessageProfile;
import atl.message.g49582.message.MessageToServer;
import atl.message.g49582.message.Type;
import java.io.IOException;

/**
 * The <code> ChatClient </code> contains all the methods necessary to set up a
 * Instant messaging client.
 */
public class ChatClient extends AbstractClient {

    private final Members members;
    private User mySelf;

    /**
     * Constructs the client. Opens the connection with the server. Sends the
     * user name inside a <code> MessageProfile </code> to the server. Builds an
     * empty list of users.
     *
     * @param host the server's host name.
     * @param port the port number.
     * @param name the name of the user.
     * @param password the password needed to connect.
     * @throws IOException if an I/O error occurs when opening.
     */
    public ChatClient(String host, int port, String name, String password) throws IOException {
        super(host, port);
        openConnection();
        updateName(name);
        members = new Members();
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        Message message = (Message) msg;
        Type type = message.getType();
        switch (type) {
            case PROFILE:
                setMySelf(message.getAuthor());
                break;
            case CLIENT:
                showMessage(message);
                break;
            case SCRAMBLE:
                showMessage(message);
                break;
            case STAT:
                showMessage(message);
                break;
            case MEMBERS:
                Members members = (Members) message.getContent();
                updateMembers(members);
                break;
            default:
                throw new IllegalArgumentException("Message type unknown " + type);
        }
    }

    /**
     * Quits the client and closes all aspects of the connection to the server.
     *
     * @throws IOException if an I/O error occurs when closing.
     */
    public void quit() throws IOException {
        closeConnection();
    }

    /**
     * Return all the connected users.
     *
     * @return all the connected users.
     */
    public Members getMembers() {
        return members;
    }

    /**
     * Return the user with the given id.
     *
     * @param id of the user.
     * @return the user with the given id.
     */
    public User getUser(int id) {
        return members.getUser(id);
    }

    /**
     * Send the text message to the given user.
     *
     * @param recipient recipient of the message.
     * @param text message send.
     * @throws IOException if an I/O error occurs when closing.
     */
    public void sendMessage(User recipient, String text) throws IOException {
        if (recipient == null) {
            throw new IllegalArgumentException("Pas de destinataire selectionne");
        }
        if (text.contains("check") || text.contains("pass") || text.contains("start")) {
            MessageToServer message = new MessageToServer(Type.MAIL_TO, getMySelf(), text);
            sendToServer(message);
        }
    }

    /**
     * Return the user.
     *
     * @return the user.
     */
    public User getMySelf() {
        return mySelf;
    }

    void setMySelf(User user) {
        this.mySelf = user;
    }

    void updateMembers(Members members) {
        this.members.clear();
        for (User member : members) {
            this.members.add(member);
        }
        notifyChange();
    }

    void showMessage(Message message) {
        notifyChange(message);
    }

    private void updateName(String name) throws IOException {
        sendToServer(new MessageProfile(0, name));
    }

    private void notifyChange() {
        setChanged();
        notifyObservers();
    }

    private void notifyChange(Message message) {
        setChanged();
        notifyObservers(message);
    }

    /**
     * Return the numbers of connected users.
     *
     * @return the numbers of connected users.
     */
    public int getNbConnected() {
        return members.size();
    }

}
