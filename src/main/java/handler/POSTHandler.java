package handler;

import models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class POSTHandler {
    private Response response;
    private Body body;
    private Logger logger = LoggerFactory.getLogger(POSTHandler.class);

    public  Response doPost(Request request) {
        logger.debug("start doPost");
        response = new Response();

        ResponseBodyBuilder bodyBuilder = new ResponseBodyBuilder();
        ResponseHeaderBuilder headerbuilder = new ResponseHeaderBuilder();
        System.out.println("content type 이 뭘까 ㄴ"+request.getHeader().getHeaders().get("Content-Type"));
        if(request.getHeader().getHeaders().get("Content-Type").contains("multipart")){
            System.out.println(request.getHeader().getHeaders().get("Content-Type"));
           // File file = new file();
            body =request.getBody();
        }else
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
