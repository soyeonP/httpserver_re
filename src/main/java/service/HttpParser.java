package service;

import error.HttpError;
import error.StatusCode;
import models.Body;
import models.Request;
import models.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;

/** Request header , body 파싱 후 객체 생성
 *
 */
public class HttpParser {
    private Logger logger = LoggerFactory.getLogger(HttpParser.class);

    //Stringbuilder로 파싱 하는거랑 시간비교해보기!
    public Request parse(BufferedReader bf) throws Exception {
        String line = null;
        RequestHeader header = new RequestHeader();
        Body body = null;
        boolean isfirstLine = true;
        /* 헤더 파싱 */
        while((line = bf.readLine())!= null){
            logger.debug("request line : " + line);
            if(isfirstLine){
                if(line.trim().equals("")){
                    logger.warn("request is empty"); //이건 에러처리를 해야하나..?
                    return null;
                }
                String[] requestLineTokens = line.split("\\s");
                if(requestLineTokens.length !=3) throw new HttpError(StatusCode.BAD_REQUEST);
                header.setMethod(requestLineTokens[0]);
                header.setResource(requestLineTokens[1]);
                header.setHttpVersion(requestLineTokens[2]);
                isfirstLine = false;
                continue;
            }
            if(line.trim().equals("")) break;

            int colon = line.indexOf(':'); //앞에 키 : 뒤 벨류 잖아 그 구별 인덱스 ex. HOST : localhost
            String key = line.substring(0, colon);
            String value = line.substring(colon + 2);
            header.set(key, value); //헤더에 다 박아준다.
        }

        if(RequestHeader.Method.POST == header.getMethod()){
            int content_length = Integer.parseInt(header.get("Content-Length"));
            byte[] bodys = new byte[content_length];
            int byteVal ;
            for (int i = 0; i <content_length ; i++) {
                byteVal = bf.read();
                if(byteVal == -1){ //예외처리해야함
                    logger.error("Reading Error");
                    throw new Exception("parsing error at : Parsing body");
                }else{ bodys[i]=(byte) byteVal; }
            }
            body = new Body(bodys);
        }
        return new Request(header,body);
    }

    //난주 나눠주자...
/*    private RequestHeader parseHeader(){
        RequestHeader header = new RequestHeader();
    }

    private Body parseBody(){
        Body body = new Body();

    }*/

}
