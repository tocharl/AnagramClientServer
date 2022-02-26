package atl.message.g49582.message;

/**
 * The <code> Type </code> represents the type of a message send between a user
 * and the server.
 */
public enum Type {

    /**
     * Message with the profile of a specific user.
     */
    PROFILE,
    /**
     * General message send between two connected users.
     */
    MAIL_TO,
    /**
     * Message with the list of all connected users.
     */
    MEMBERS,
    /**
     * General message from server to client.
     */
    CLIENT,
    /**
     * Message with a scramble word to a client.
     */
    SCRAMBLE,
    /**
     * Message with some statistics about the game.
     */
    STAT;
}
