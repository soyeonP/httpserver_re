package error;

import models.Header;
import models.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

public class ErrorHandler {
    private Logger logger = LoggerFactory.getLogger(ErrorHandler.class);
    private File resourceFile ;

    public void checkHeader(RequestHeader requestHeader, File resourceFile, String eTag, File droot) throws HttpError {

        RequestHeader.Method method = requestHeader.getMethod();
        //GET HEAD POST 밖에 안했다...사실 아직 GET밖엥나했...아니 GET도 다안했...
        if(method != RequestHeader.Method.GET && method != RequestHeader.Method.HEAD && method != RequestHeader.Method.POST){
            throw new HttpError(StatusCode.NOT_IMPLEMENTED,requestHeader.getMethod().toString());
        }

        String resource = requestHeader.getResource();

        logger.debug("resource Path : "+resourceFile.toString());
        if (!resourceFile.exists()&&!resource.contains("?")) {
            logger.debug("resource is not exists : "+ resource);
            throw new HttpError(StatusCode.NOT_FOUND, resource);
        }
        requestHeader.setResource(resourceFile.getPath());
        Map<String,String> field = requestHeader.getHeaders();

        logger.debug(field.toString());

        //조건부 리퀘스트 일때
        if (requestHeader.getHeaders().containsKey("If-Match")) {
            if (!requestHeader.getHeaders().get("If-Match").equals("\"" + eTag + "\"")) {
                throw new HttpError(StatusCode.PRECONDITION_FAILED, resource);
            }
        }

        if (requestHeader.getHeaders().containsKey("If-None-Match")) {
            String[] tags = requestHeader.getHeaders().get("If-None-Match").split(",\\w?");
            for (String tag : tags) {
                if ((eTag).equals(tag)) {
                    throw new HttpError(StatusCode.NOT_MODIFIED, resource);
                }
            }
        }
        ZonedDateTime lastModified = getLastModified(resourceFile);
        if (requestHeader.getHeaders().containsKey(Header.IF_MODIFIED_SINCE.getText())) {
            String value = requestHeader.getHeaders().get(Header.IF_MODIFIED_SINCE.getText());
            ZonedDateTime ifModifiedSince = DateTimeFormatter.RFC_1123_DATE_TIME.parse(value, ZonedDateTime::from);
            if (!lastModified.isAfter(ifModifiedSince)) {
                throw new HttpError(StatusCode.NOT_MODIFIED, resource);
            }
        }
    }


    private ZonedDateTime getLastModified(File f) {
        return ZonedDateTime.ofInstant(new Date(f.lastModified()).toInstant(), ZoneId.of("GMT"));
    }
}
