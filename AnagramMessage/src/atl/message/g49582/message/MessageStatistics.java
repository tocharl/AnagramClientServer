package atl.message.g49582.message;

import atl.message.g49582.chat.users.User;

/**
 *
 * @author game
 */
public class MessageStatistics implements Message{
    private final Type type;
    private final String text;
    
    public MessageStatistics(String text) {
        this.type = Type.STAT;
        this.text = text;
    }
    
    @Override
    public Type getType() {
        return type;
    }

    @Override
    public User getAuthor() {
        return User.ADMIN;
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
