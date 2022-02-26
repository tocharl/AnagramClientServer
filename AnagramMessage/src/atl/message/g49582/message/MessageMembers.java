package atl.message.g49582.message;

import atl.message.g49582.chat.users.Members;
import atl.message.g49582.chat.users.User;

/**
 * The <code> Message </code> represents a message with the list of all
 * connected users.
 */
public class MessageMembers implements Message {

    private final Members members;

    /**
     * Constructs message with the list of all connected users.
     *
     * @param members list of all connected users.
     */
    public MessageMembers(Members members) {
        this.members = members;
    }

    /**
     * Return the administrator. The author of a message with all the connected
     * users is the administrator.
     *
     * @return
     */
    @Override
    public User getAuthor() {
        return User.ADMIN;
    }

    /**
     * Return the recipient of the message.
     *
     * @return the recipient of the message.
     */
    @Override
    public User getRecipient() {
        return User.EVERYBODY;
    }

    /**
     * Return the type of the message, in this case Type.MEMBERS.
     *
     * @return the type of the message, in this case Type.MEMBERS.
     */
    @Override
    public Type getType() {
        return Type.MEMBERS;
    }

    /**
     * Return the content of the message : the list of all connected users.
     *
     * @return the content of the message : the list of all connected users.
     */
    @Override
    public Object getContent() {
        return members;
    }

}
