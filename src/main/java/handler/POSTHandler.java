package handler;

import Sevlet.GetServlet;
import models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class POSTHandler {
    private Response response;
    private Body body;
    private Logger logger = LoggerFactory.getLogger(GETHandler.class);

    public  Response doPost(Request request){
        response = new Response();
        int content_length = Integer.parseInt(request.getHeader().getHeaders().get(Header.CONTENT_LENGTH.getText()));

        Body requestBodydata = request.getBody();
        String bodytext = request.getBody().toString();
        String[] text = bodytext.split("\\&");
        Map Queries = getQuery(text);
        body = GetServlet.writeHttp(Queries);
        response.setBody(body);
        logger.debug(body.toString());
        ResponseHeaderBuilder builder = new ResponseHeaderBuilder();
        builder.setState(); // write stateline
        builder.setField("Content-Length", String.valueOf(body.toString().length()));
        builder.setContextType("text/html; charset=UTF-8");
        response.setHeader(builder.build());
        return response;
    }

    private Map<String, String> getQuery(String[] resource) {
        Map<String,String> Queries = new HashMap<>();
        System.out.println(resource.length);
        for (String s : resource) {
            String[] query =s.split("=");
            System.out.println(query[0]);
            System.out.println(query[1]);
            Queries.put(query[0], query[1]);
        }
        return Queries;
    }

}
