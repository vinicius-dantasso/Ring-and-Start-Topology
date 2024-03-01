package Anel.object;

import java.io.Serializable;

public class Package <T> implements Serializable{
    
    private static final long serialVersionUID = 1L;
    private T message;

    public Package(T m) {
        this.setMessage(m);
    }

    public T getMessage() {
        return this.message;
    }

    public void setMessage(T message) {
        this.message = message;
    }

}
