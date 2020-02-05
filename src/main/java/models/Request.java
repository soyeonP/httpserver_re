package models;

public class Request {
    private RequestHeader header;
    private Body body;

    public Request(RequestHeader header, Body body){
        this.header = header;
        this.body = body;
    }

    public RequestHeader getHeader(){
        return header;
    }

    public Body getBody(){
        return body;
    }

    @Override
    public String toString() {
        if (body != null) return header.toString() + body.toString();
        else return header.toString();
    }
}
