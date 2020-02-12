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
        ResponseHeaderBuilder headerbuilder = new ResponseHeaderBuilder();

        body = bodyBuilder.build(request.getBody());
        logger.debug(body.toString());

        response.setBody(body);
        headerbuilder.setState(); // write stateline
        boolean isKeepAlive = request.getHeader().isKeepAlive();
        headerbuilder.setConnection(isKeepAlive);
        if(isKeepAlive){ //max,request time 삽입
            headerbuilder.setKeepAlive(request.getHeader().getSocket_Time(),request.getHeader().getMax());
        }
        headerbuilder.setField(Header.CONTENT_LENGTH.getText(), String.valueOf(body.getBytes().length));
        headerbuilder.setContextType();

        response.setHeader(headerbuilder.build());
        return response;
    }
}
