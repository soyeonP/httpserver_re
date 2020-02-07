package models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * Responce header 를 만든다.
 */
public class ResponseHeaderBuilder {
    private Logger logger = LoggerFactory.getLogger(ResponseHeaderBuilder.class);
    private Map<String, String> statusLine;
    private Map<String, String> fields;
    public static final String CRLF = "\r\n";

    public ResponseHeaderBuilder(){
        statusLine = new HashMap<>();
        fields = new HashMap<>();
        fields.put("Server", "soya");
        fields.put("Connection", "keep-alive"); // close ? keep-alive
    } // max 도 적어줘야한다.....

    //200 OK state
    public void setState(){
        setHttpVersion("HTTP/1.1");
        setStatusCode("200");
        setReasonPhrase("OK");
    }

    private void setKeppAlive(int max){

    }

    private void setHttpVersion(String version) { statusLine.put("HttpVersion", version); }

    public  void setETag(String etag){ fields.put("ETag",etag);}

    private void setStatusCode(String code) {    statusLine.put("Code", code); }

    private void setReasonPhrase(String phrase) { statusLine.put("Phrase", phrase); }

    public void setField(String key, String value) { fields.put(key, value); }

    //default type
    public  void setContextType(){
        setField("Content-Type", "text/html; charset=ISO-8859-1");
    }

    public void setContextType(File resourceFile) throws IOException { setField("Content-Type", getContentType(resourceFile)); }

    public void setContextType(String type){ setField("Content-Type",type);}

    public ResponseHeader build() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(statusLine.get("HttpVersion"));
        stringBuilder.append(' ');
        stringBuilder.append(statusLine.get("Code"));
        stringBuilder.append(' ');
        stringBuilder.append(statusLine.get("Phrase"));
        stringBuilder.append(CRLF);
        for (Map.Entry<String, String> entry : fields.entrySet()){
            stringBuilder.append(entry.getKey());
            stringBuilder.append(':');
            stringBuilder.append(entry.getValue());
            stringBuilder.append(CRLF);
        }
        stringBuilder.append(CRLF);
        logger.debug(stringBuilder.toString());
        return new ResponseHeader(stringBuilder.toString().getBytes());
    }

    private String getContentType(File file) throws IOException {

        String name = file.getName().toLowerCase();
        String ct = Files.probeContentType(file.toPath()); //이거 있으면 다 되는거 아닌가 ? ? ??
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("name=%s type=%s", name, ct));
        }

        if (ct != null) {
            logger.debug("probecontentType method can check a mime !");
            return ct;
        }else if (name.endsWith(".xml")) {
            return "text/xml; charset=ISO-8859-1";
        }else if (name.endsWith(".jsp")) {
            return "text/plain; charset=UTF-8";
        }
        else if (name.endsWith(".java") || name.endsWith(".md")) {
            return "text/plain; charset=UTF-8";
        } else if (name.endsWith(".jar")) {
            return "application/x-java-archive";
        } else if(name.endsWith(".pdf")){
            return "application/pdf";
        } else if(name.endsWith(".jpg")||name.endsWith("jpeg")){
            return "image/jpeg";
        } else if(name.endsWith(".gif")){
            return "image/gif";
        } else if(name.endsWith(".doc")) {
            return "application/msword";
        } else if(name.endsWith(".xls")) {
            return "application/vbd.ms-excel";
        }else if(name.endsWith(".zip")) {
            return "application/zip";
        }else
            return "application/octet-stream";
    }
}
