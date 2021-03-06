package handler;

import models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GETHandler {
    private Response response;
    private Body body;
    private Logger logger = LoggerFactory.getLogger(GETHandler.class);

    public Response doGet(Request request) throws IOException { //리소스만 넘겨도 될거같다.
        response = new Response();
        String resource = request.getHeader().getResource();
        logger.debug("resource : "+resource);
        String etag = request.getHeader().geteTag();

        ResponseHeaderBuilder headerbuilder = new ResponseHeaderBuilder();
        ResponseBodyBuilder bodyBuilder = new ResponseBodyBuilder();

        body = bodyBuilder.build(resource);

        response.setBody(body);

        int contentLength = body.getBytes().length;
        headerbuilder.setState(); // write stateline
        boolean isKeepAlive = request.getHeader().isKeepAlive();
        headerbuilder.setConnection(isKeepAlive);
        if(isKeepAlive){ //max,request time 삽입
            headerbuilder.setKeepAlive(request.getHeader().getSocket_Time(),request.getHeader().getMax());
        }
        headerbuilder.setETag(etag);
        headerbuilder.setField(Header.CONTENT_LENGTH.getText(), String.valueOf(contentLength));
        if(body.getDataType()!=null) headerbuilder.setContextType(body.getDataType());
        else headerbuilder.setContextType();

        response.setHeader(headerbuilder.build());

        return response;
    }
}
