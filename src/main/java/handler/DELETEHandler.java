package handler;

import models.Body;
import models.Request;
import models.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//delete 하고... 204 return / 완료페이지 띄워야하나 ?
public class DELETEHandler {
    private Response response;
    private Body body;
    private Logger logger = LoggerFactory.getLogger(DELETEHandler.class);
    public Response doDelete(Request request) {
        response = new Response();
        return response;
    }
}
