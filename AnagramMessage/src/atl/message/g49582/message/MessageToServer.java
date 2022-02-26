package atl.message.g49582.message;

import atl.message.g49582.chat.users.User;

/**
 * The <code> Message </code> represents a general message send to a user.
 */
public class MessageToServer implements Message {

    private final Type type;
    private final User author;
    private final String text;

    /**
     * Constructs a general text message between users.
     *
     * @param type the type of the message.
     * @param author the author of the message
     * @param text the text of the message.
     */
    public MessageToServer(Type type, User author, String text) {
        this.type = type;
        this.author = author;
        this.text = text;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public User getAuthor() {
        return author;
    }

    @Override
    public User getRecipient() {
        return User.ADMIN;
    }

    /**
     * Return the text within this message.
     *
     * @return the text within this message.
     */
    @Override
    public Object getContent() {
        return text;
    }

}
