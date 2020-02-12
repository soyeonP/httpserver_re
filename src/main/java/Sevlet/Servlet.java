package Sevlet;

import models.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Servlet {
    private Logger logger = LoggerFactory.getLogger(Servlet.class);
    public static final String CRLF = "\r\n";
    
    public static Body writeHttp(Map<String, String> data) {
        StringBuilder html = new StringBuilder();
        writeHtmlFront(html,"Send Data");
        for (String key: data.keySet()){ html.append(String.format("<h1>%s : %s</h1>", key, data.get(key))).append(CRLF); }
        writeHtmlBack(html);
        Body body = new Body(html.toString());
        return body;
    }

    protected static void writeHtmlFront(StringBuilder html,String title){
        html.append("<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">").append(CRLF);
        html.append("<html><head>").append(CRLF);
        html.append(String.format("<title>"+ title +"</title>")).append(CRLF);
        html.append("</head><body>").append(CRLF);
    }

    protected static void writeHtmlBack(StringBuilder html){
        html.append("</body></html>").append(CRLF);
    }

}
