package Sevlet;

import error.HttpError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class ErrorServlet {
    private Logger logger = LoggerFactory.getLogger(ErrorServlet.class);
    public static final String CRLF = "\r\n";

    public void writeError(HttpError httpError, BufferedWriter out, int max) throws IOException {
        logger.debug("returning " + httpError);

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">").append(CRLF);
        html.append("<html><head>").append(CRLF);
        html.append(String.format("<title>%s %s</title>", httpError.getCode().getId(),
                httpError.getCode().getDescription())).append(CRLF);
        html.append("</head><body>").append(CRLF);
        html.append(String.format("<h1>%s</h1>", httpError.getCode().getDescription())).append(CRLF);
        html.append("</body></html>").append(CRLF);

        StringBuilder response = new StringBuilder();
        response.append("HTTP/1.1 " + httpError.getCode().getId() + " " + httpError.getCode().getDescription() + CRLF);
        response.append("Date: " + LocalDateTime.now() + CRLF);
        response.append("Server: soya" + CRLF);
        response.append("Content-Type: text/html; charset=UTF-8" + CRLF);
        response.append("Content-Length: " + html.length() + CRLF);
        response.append("Keep-Alive: timeout=5, max=" + max + CRLF);
        response.append("Connection: close" + CRLF);
        response.append(CRLF);

        out.write(response.toString());
        out.write(html.toString());
    }
}
