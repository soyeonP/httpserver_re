package service;


import Sevlet.ErrorServlet;
import dispatcher.HttpDispatcher;
import error.ErrorHandler;
import error.HttpError;
import models.Header;
import models.HttpContext;
import models.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ConnectionWrap implements Runnable {

    private Socket socket = null;
    private File droot;
    private static int DEFAULT_MAX = 100;
    private static final int SOCKET_TIMEOUT = 5000;
    private Logger logger = LoggerFactory.getLogger(ConnectionWrap.class);
    private HttpParser parser ;
    private ErrorServlet errorServlet;
    private HttpDispatcher dispatcher;
    private int max = DEFAULT_MAX ;
    private ErrorHandler errorHandler;
    private HttpContext context;

    public ConnectionWrap(File droot, Socket socket) {
        this.socket = socket;
        this.droot = droot;
        this.parser = new HttpParser();
        this.dispatcher = new HttpDispatcher();
        this.errorServlet = new ErrorServlet();
        this.errorHandler =new ErrorHandler();
        this.context = new HttpContext(null,null);
    }

    @Override
    public void run() {
        logger.debug("New Client Connect! Connected IP :"+ socket.getInetAddress()+", Port : %d"+ socket.getPort());
        try(OutputStream out = socket.getOutputStream()){
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
           // int requestCount = 0;
            while(true){
                try {
                    //request 헤더 바디가 request에 담겨 있다.
                    socket.setSoTimeout(SOCKET_TIMEOUT);
                    Request request = parser.parse(br);
                    //requestCount ++;
                    logger.debug(request.toString());
                    //받아온 리퀘스트가 가 옳은 리퀘스트인지 확인해보자!
                   if(request!=null){
                       String resource = request.getHeader().getResource();
                       if(resource != null) {
                           //Map<String,String> queris = null;
                           if(resource.equals("/")) { resource = "\\index.html"; }
                           /*if(isQuery(resource)){
                               String[] resourceTokens = resource.split("\\?");
                               resource = resourceTokens[0];
                               String[] queries = resourceTokens[1].split("\\&");
                               queris = getQuery(queries);
                               context.getResponse().setBody(GetServlet.writeHttp(queris));
                           }*/
                           File resourceFile = new File(droot, resource);
                           String eTag = getETag(resourceFile,resource);
                           errorHandler.checkHeader(request.getHeader(),resourceFile,eTag,droot);
                           context.setRequest(request);
                           context.setResponse(dispatcher.dispatch(request,eTag));
                           //if(queris!=null){ context.getResponse().setBody(GetServlet.writeHttp(queris));}
                       }

                       //writer에 response를 다 적어준다.
                       byte[] buffer = new byte[1024];
                       int sz;
                       InputStream inputStream = context.getResponse().getStream();
                       logger.debug("start responding");
                       while ((sz = inputStream.read(buffer)) != -1)
                           out.write(buffer, 0, sz);
                       out.flush();
                       logger.debug("end of handler");
                   }
                   else break;

                    if(request.getHeader().getHeaders().containsKey(Header.CONNECTION.getText())) {
                        if ("close".equals(request.getHeader().get(Header.CONNECTION.getText()))) {
                            //클라가 커넥션클로싱 요청
                            logger.debug("client requested connection close");
                            break;
                        }
                        if ("keep-alive".equals(request.getHeader().get(Header.CONNECTION.getText()))) {
                            //계속 유지
                            if (request.getHeader().getHeaders().containsKey(Header.KEEP_ALIVE.getText())) {
                                max = parseMax(request.getHeader().get(Header.KEEP_ALIVE.getText()));
                            }
                        }
                    }
               //     if (requestCount >= max) break;
                } catch(HttpError httpError){
                    errorServlet.writeError(httpError,writer,max);
                    writer.flush();
                    break;
                } catch (SocketException se){
                    break;
                } catch (SocketTimeoutException ste){
                    logger.debug("socket time out");
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public String getETag(File file, String resource) {
        return String.valueOf((resource + file.lastModified()).hashCode());
    }
    private int parseMax(String header) {
        logger.debug("parseMax");
        String[] tokens = header.split("\\s");
        for (String token : tokens) {
            if (token.startsWith("max=")) {
                return Integer.parseInt(token.substring(4));
            }
        }
        return DEFAULT_MAX;
    }
}
