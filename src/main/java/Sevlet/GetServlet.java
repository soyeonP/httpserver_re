package Sevlet;

import models.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class GetServlet {
    private Logger logger = LoggerFactory.getLogger(GetServlet.class);

    public static final String CRLF = "\r\n";
    
    public static Body writeHttp(Map<String, String> data) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">").append(CRLF);
        html.append("<html><head>").append(CRLF);
        html.append(String.format("<title> Sent Data </title>")).append(CRLF);
        html.append("</head><body>").append(CRLF);
        for (String key: data.keySet()){
            html.append(String.format("<h1>%s : %s</h1>", key, data.get(key))).append(CRLF);
        }
        html.append("</body></html>").append(CRLF);
        Body body = new Body(html.toString());
        return body;
    }
}
