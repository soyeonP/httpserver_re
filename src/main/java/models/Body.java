package models;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

public class Body {
    private byte[] data;

    public Body(byte[] data){
        this.data = data;
    }

    public Body(String body){
        this.data = body.getBytes();
    }

    public byte[] getBytes() {
        return Arrays.copyOf(data, data.length);
    }

    public InputStream getStream(){
        return new ByteArrayInputStream(data);
    }

    @Override
    public String toString() {
        return new String(data);
    }
}
