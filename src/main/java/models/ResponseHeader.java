package models;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ResponseHeader {
    private byte[] data;

    public ResponseHeader(byte[] data){
        this.data = data;
    }

    public InputStream getStream(){
        return new ByteArrayInputStream(data);
    }

}
