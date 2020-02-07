package models;

import java.util.HashMap;
import java.util.Map;

public class RequestHeader {
    private Map<String, String> fields;
    private Method method;
    private String resource;
    private String httpVersion="HTTP/1.1";
    private String eTag;

    public enum Method {
        GET,POST,HEAD,PUT,DELETE
    }
    //메소드 버전 리소스는 따로 만들어줄까? 필수니까.
    public RequestHeader(Map<String, String> fields){
        this.fields = fields;
    }

    public RequestHeader() {
        fields = new HashMap<>();
    }

    public String get(String key){
        return fields.get(key);
    }

    public void set(String key, String val){
        fields.put(key, val);
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
    @Override
    public String toString() {
        return fields.toString();
    }
}
