package handler;

import models.Body;
import models.Request;
import models.Response;
import models.ResponseHeaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class HEADHandler {
    private Response response;
    private Body body;
    private Logger logger = LoggerFactory.getLogger(GETHandler.class);

    public Response doHead(Request request) throws IOException {
        String resource = request.getHeader().getResource();

        // File resourceFile = new File(resource);
        logger.debug("resource : "+resource);
        File resourceFile = new File(resource);

        response = new Response();
        //build header
        int contentLength = (int)resourceFile.length();
        ResponseHeaderBuilder builder = new ResponseHeaderBuilder();
        builder.setState(); // write stateline
        builder.setField("Content-Length", String.valueOf(resourceFile.length()));
        builder.setContextType(resourceFile);
        response.setHeader(builder.build());
        return response;
    }
}
