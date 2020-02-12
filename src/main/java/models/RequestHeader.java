package models;

import java.util.HashMap;
import java.util.Map;

public class RequestHeader {
    private Map<String, String> fields;
    private Method method;
    private String resource;
    private String httpVersion="HTTP/1.1";
    private String eTag;
    private boolean keepAlive=false;
    private int max;
    private int socket_Time;


    public void setSocket_Time(int socket_Time){
        this.socket_Time=socket_Time/1000;
    }
    public void setMax(int max){
        this.max = max;
    }
    public int getSocket_Time() {
        return socket_Time;
    }

    public int getMax() {
        return max;
    }

    public enum Method {
        GET,
        POST,
        HEAD,
        PUT,
        DELETE
    }

    public RequestHeader() {
        fields = new HashMap<>();
    }

    public String get(String key){
        return fields.get(key);
    }

    public void set(String key, String val){
        fields.put(key, val);
        if(key.equals("Connection")){
            if(val.equals("keep-alive")){ keepAlive=true; } else {keepAlive=false;}
        }
    }

    public Method getMethod(){ return method; }

    public Map<String,String> getHeaders(){ return fields; }

    public void setMethod(String method){ this.method = Method.valueOf(method);}

    public String getResource() {return resource;}

    public void setResource(String resource) {this.resource = resource;}

    public String getHttpVersion(){return httpVersion;}

    public void setHttpVersion(String httpversion) { this.httpVersion = httpversion;}


    public String Protocol(){
        return fields.get("protocol");
    }

    public String geteTag(){
        return eTag;
    }

    public void seteTag(String eTag){
        this.eTag = eTag;
    }

    public boolean isKeepAlive(){
        return keepAlive;
    }


    @Override
    public String toString() {
        return fields.toString();
    }


}
