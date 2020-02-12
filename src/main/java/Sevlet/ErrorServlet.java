package Sevlet;

import error.HttpError;
import models.ResponseHeader;
import models.ResponseHeaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;

public class ErrorServlet extends Servlet {
    private Logger logger = LoggerFactory.getLogger(ErrorServlet.class);

    public void writeError(HttpError httpError, BufferedWriter out) throws IOException {
        logger.debug("returning " + httpError);

        StringBuilder html = new StringBuilder();
        String title = new StringBuilder().append(httpError.getCode().getId()).append(" ").append(httpError.getCode().getDescription()).toString();
        writeHtmlFront(html, title);
        html.append(String.format("<h1> %s %s</h1>",httpError.getCode().getId() ,httpError.getCode().getDescription())).append(CRLF);
        writeHtmlBack(html);

        ResponseHeaderBuilder builder = new ResponseHeaderBuilder();
        builder.setErrorState(httpError);
        builder.setContextType();

        if(httpError.getCode().getId()==304){
            builder.setField("Cache-Control","max-age=120");
        }
        builder.setField("Content-Length: ",Integer.toString(html.length()));
        builder.setConnection(false);

        ResponseHeader responseheaeder = builder.build();;
        out.write(responseheaeder.getHeader());
        out.write(html.toString());
    }
}
