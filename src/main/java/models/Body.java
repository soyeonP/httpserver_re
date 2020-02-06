package models;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;

public class Body {
    private byte[] data;
    private String dataType;

    public Body(byte[] data){
        this.data = data;
    }

    public Body(String body){
        this.data = body.getBytes();
    }

    public Body(byte[] data, String dataType){
        this.data = data;
        this.dataType = dataType;
    }

    public byte[] getBytes() {
        return Arrays.copyOf(data, data.length);
    }

    public InputStream getStream(){
        return new ByteArrayInputStream(data);
    }

    public void setDataType(String dataType){ this.dataType = dataType; }

    public String getDataType(){ return dataType; }

    @Override
    public String toString() {
        return new String(data);
    }
}
