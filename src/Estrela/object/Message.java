package Estrela.object;

import java.io.Serializable;

public class Message <T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private T message;

    public Message(T m) {
        this.setMessage(m);
    }

    public T getMessage() {
        return this.message;
    }

    public void setMessage(T message) {
        this.message = message;
    }

}
