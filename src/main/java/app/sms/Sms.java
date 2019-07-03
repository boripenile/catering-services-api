package app.sms;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author bd_dev_murtala
 */
public class Sms {    
    private String sender;
    
    private String message;
    
    private String[] toNumbers;

    public Sms() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getToNumbers() {
        return toNumbers;
    }

    public void setToNumbers(String[] toNumbers) {
        this.toNumbers = toNumbers;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.sender);
        hash = 73 * hash + Objects.hashCode(this.message);
        hash = 73 * hash + Arrays.deepHashCode(this.toNumbers);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Sms other = (Sms) obj;
        if (!Objects.equals(this.sender, other.sender)) {
            return false;
        }
        if (!Objects.equals(this.message, other.message)) {
            return false;
        }
        if (!Arrays.deepEquals(this.toNumbers, other.toNumbers)) {
            return false;
        }
        return true;
    }
    
    
}
