/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atl.message.g49582.message;

import atl.message.g49582.chat.users.User;

/**
 *
 * @author game
 */
public class MessageToClient implements Message{

    private final Type type;
    private final String text;
    
    public MessageToClient(String text) {
        this.type = Type.CLIENT;
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
