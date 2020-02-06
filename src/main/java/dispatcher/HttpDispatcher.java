package dispatcher;


import error.HttpError;
import error.StatusCode;
import handler.GETHandler;
import handler.HEADHandler;
import handler.POSTHandler;
import models.Request;
import models.RequestHeader;
import models.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

//가져온 리퀘스트로 리스폰스를 만들어 context에 넣어준다.
public class HttpDispatcher {
    private Logger logger = LoggerFactory.getLogger(HttpDispatcher.class);
    private GETHandler getHandler;
    private POSTHandler postHandler;
    private HEADHandler headHandler;
    private Response response;


    public HttpDispatcher(){ //핸들러 넣어주기
        this.getHandler = new GETHandler();
        this.postHandler = new POSTHandler();
        this.headHandler = new HEADHandler();
        this.response = null;
    }

    public Response dispatch(Request request) throws HttpError, IOException { // context 는 request 를 가지고 있다. response달아줘야함
        RequestHeader.Method method = request.getHeader().getMethod();
        //request의 Method에 따라 다른 response header와 body를 생성한다.

        switch (method){ //리퀘스트를 보내줄게 리스폰스를 가져와줘 :D
            case GET:
                response = getHandler.doGet(request);
                break;
            case POST:
                response = postHandler.doPost(request);
                break;
            case HEAD:
                response = headHandler.doHead(request);
                break;
            case PUT:
                break;
            case DELETE:
                break;
            default:
                throw new HttpError(StatusCode.NOT_IMPLEMENTED); //지원안하는 메소드얌
        }
        //쿠키가 있다면 쿠키 핸들러도 한번 보내준다.
        return response;
    }
}
