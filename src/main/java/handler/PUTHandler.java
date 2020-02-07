package handler;

import models.Body;
import models.Request;
import models.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// put 하고 그 페이지로 이동 or 링크를 띄워주는 창을 만들까 ?
public class PUTHandler {
    private Response response;
    private Body body;
    private Logger logger = LoggerFactory.getLogger(PUTHandler.class);
    public Response doPut(Request request) {
        response = new Response();
        return response;
    }
}
