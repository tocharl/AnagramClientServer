package atl.message.g49582.chat.users;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The <code> Members </code> represents a list of all connected users.
 */
public class Members implements Iterable<User>, Serializable {

    private final List<User> users;

    /**
     * Constructs an empty list of users.
     *
     */
    public Members() {
        users = new ArrayList<>();
    }

    /**
     * Creates a new instance of an user and add this user to the list.
     *
     * @param id userID of the new user.
     * @param name name of the new user.
     * @param address IP address of the new user.
     * @return the userID of the new user.
     */
    public int add(int id, String name, InetAddress address) {
        User user = new User(id, name, address);
        add(user);
        return user.getId();
    }

    /**
     * Add a user to the list of connected users.
     *
     * @param user user connected.
     */
    public void add(User user) {
        users.add(user);
    }

    /**
     * Remove the user with the current userID from the list of connected users.
     *
     * @param id userID of the user disconnected.
     */
    public void remove(int id) {
        Iterator<User> it = users.iterator();
        boolean find = false;
        User current = null;
        while (it.hasNext() && !find) {
            current = it.next();
            find = current.is(id);
        }
        if (find) {
            users.remove(current);
        }
    }

    /**
     * Return the number of users connected.
     *
     * @return the number of users connected.
     */
    public int size() {
        return users.size();
    }

    /**
     * Clear the list of connected users.
     */
    public void clear() {
        users.clear();
    }

    @Override
    public Iterator<User> iterator() {
        return users.iterator();
    }

    /**
     * Update the name of a user.
     *
     * @param name new value of the name.
     * @param id userId of the user to update.
     */
    public void changeName(String name, int id) {
        User user = getUser(id);
        if (user != null) {
            user.setName(name);
        }
    }

    /**
     * Return the user of the given id.
     *
     * @param id userdId of the user to return.
     * @return the user of the given id.
     */
    public User getUser(int id) {
        Iterator<User> it = users.iterator();
        boolean find = false;
        User current = null;
        while (it.hasNext() && !find) {
            current = it.next();
            find = current.is(id);
        }
        return find ? current : null;
    }
}
