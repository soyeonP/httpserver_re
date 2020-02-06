package models;

import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;

public class Response {
    private ResponseHeader header;
    private Body body;

    public InputStream getStream() throws IOException {
        if(body==null) return header.getStream();
        InputStream inputStream = new SequenceInputStream(header.getStream(), body.getStream());
        return inputStream;
    }

    public String getheader(){
        return header.getHeader();
    }

    public void setHeader(ResponseHeader header) {
        this.header = header;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Body getBody(){
        return body;
    }
}
