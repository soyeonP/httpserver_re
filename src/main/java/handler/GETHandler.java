package handler;

import Sevlet.GetServlet;
import error.HttpError;
import models.Body;
import models.Request;
import models.Response;
import models.ResponseHeaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GETHandler {
    private Response response;
    private Body body;
    private Logger logger = LoggerFactory.getLogger(GETHandler.class);

    public Response doGet(Request request, String etag) throws HttpError, IOException { //리소스만 넘겨도 될거같다.
        String resource = request.getHeader().getResource();
        logger.debug("resource : "+resource);

        //build header
        ResponseHeaderBuilder builder = new ResponseHeaderBuilder();

        response = new Response();
        int contentLength;
        Map<String,String> queris = null;
        if(isQuery(resource)){
            String[] resourceTokens = resource.split("\\?");
            resource = resourceTokens[0];
            String[] queries = resourceTokens[1].split("\\&");
            queris = getQuery(queries);
            body = GetServlet.writeHttp(queris);
           response.setBody(body);
           contentLength = body.getBytes().length;
            builder.setContextType();
        }else{
            File resourceFile = new File(resource);

            contentLength = (int)resourceFile.length();

            byte[] bodydata = new byte[contentLength];
            new FileInputStream(resourceFile).read(bodydata);
            body = new Body(bodydata);
            logger.debug(body.toString());
            response.setBody(body);
            builder.setContextType(resourceFile);
        }

        builder.setState(); // write stateline
        builder.setETag(etag);
        builder.setField("Content-Length", String.valueOf(contentLength));

        response.setHeader(builder.build());


        return response;
    }

    private boolean isQuery(String resource) {
        return resource.contains("?");
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
    private boolean isText(String contentType) {
        return contentType.startsWith("text") || contentType.endsWith("xml");
    }
}
