package handler;

import models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class POSTHandler {
    private Response response;
    private Body body;
    private Logger logger = LoggerFactory.getLogger(POSTHandler.class);

    public  Response doPost(Request request) {
        response = new Response();

        ResponseBodyBuilder bodyBuilder = new ResponseBodyBuilder();
        ResponseHeaderBuilder builder = new ResponseHeaderBuilder();

        body = bodyBuilder.build(request.getBody());
        logger.debug(body.toString());

        response.setBody(body);

        builder.setState(); // write stateline
        builder.setField("Content-Length", String.valueOf(body.getBytes().length));
        builder.setContextType("text/html; charset=UTF-8");

        response.setHeader(builder.build());

        return response;
    }
}
