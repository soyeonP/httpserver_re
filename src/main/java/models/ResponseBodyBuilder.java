package models;

import Sevlet.GetServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class ResponseBodyBuilder {
    private Logger logger = LoggerFactory.getLogger(ResponseBodyBuilder.class);
    private Map<String, String> Query;

    public ResponseBodyBuilder(){
        Query = new HashMap<>();
    }

    public Body build(String resource) throws IOException { //get 요청시 그냥 파일명 혹은 파일명+쿼리가 들어온다.
        if(!isQuery(resource)){
            File resourceFile = new File(resource);
            byte[] bodydata = new byte[(int) resourceFile.length()];
            new FileInputStream(resourceFile).read(bodydata);
            return new Body(bodydata, Files.probeContentType(resourceFile.toPath()));
        }
        String[] resourceTokens = resource.split("\\?");
        Query = getQuery(resourceTokens[1].split("\\&"));
        return GetServlet.writeHttp(Query);
    }

    public Body build(Body body){ //바디 요청은 바디 그대로
        String bodytext = body.toString();
        System.out.println(bodytext);
        Query = getQuery( bodytext.split("\\&") );
        return GetServlet.writeHttp(Query);
    }

    private boolean isQuery(String resource) {
        logger.debug("this request have query");
        return resource.contains("?");
    }

    private Map<String, String> getQuery(String[] resource) {
        Map<String,String> Queries = new HashMap<>();
        System.out.println(resource[0].toString());
        System.out.println(resource.length);
        for (String s : resource) {
            String[] query =s.split("=");
            System.out.println(query[0].toString());
            System.out.println(query[1]);
            Queries.put(query[0], query[1]);
        }
        return Queries;
    }
}
