package models;

import Sevlet.Servlet;
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
    //get 요청시 그냥 파일명 혹은 파일명+쿼리가 들어온다.
    public Body build(String resource) throws IOException {
        if(!isQuery(resource)){
            File resourceFile = new File(resource);
            byte[] bodydata = new byte[(int) resourceFile.length()];
            new FileInputStream(resourceFile).read(bodydata);
            return new Body(bodydata, Files.probeContentType(resourceFile.toPath()));
        }
        logger.debug("this request have query");
        String[] resourceTokens = resource.split("\\?");
        Query = getQuery(resourceTokens[1].split("\\&"));
        return Servlet.writeHttp(Query);
    }

    public Body build(Body body){ // 바디 그대로
        String bodytext = body.toString();
        Query = getQuery( bodytext.split("\\&") );
        return Servlet.writeHttp(Query);
    }

    private boolean isQuery(String resource) {
        return resource.contains("?");
    }

    private Map<String, String> getQuery(String[] resource) {
        Map<String,String> Queries = new HashMap<>();
        String value = "";
        for (String s : resource) {
            String[] query =s.split("=");
            if(query.length==2)
                value =query[1];
            else value="";
            Queries.put(query[0], value);
        }
        return Queries;
    }
}
