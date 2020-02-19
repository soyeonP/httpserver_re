package service;

/**
* Keep-Alive , responce header에 구현
 * put delete 구현
 * 얘네 테스트할 client 만들기
 * */

import Sevlet.ErrorServlet;
import dispatcher.HttpDispatcher;
import error.HttpError;
import handler.StateInterceptor;
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

public class ConnectionWrap implements Runnable {

    private Socket socket = null;
    private File droot;
    private static int DEFAULT_MAX = 10000000;
    private static final int SOCKET_TIMEOUT = 5000;
    private Logger logger = LoggerFactory.getLogger(ConnectionWrap.class);
    private HttpParser parser ;
    private ErrorServlet errorServlet;
    private HttpDispatcher dispatcher;
    private int max = DEFAULT_MAX ;
    private StateInterceptor interceptor;
    private HttpContext context;

    public ConnectionWrap(File droot, Socket socket) {
        this.socket = socket;
        this.droot = droot;
        this.parser = new HttpParser();
        this.dispatcher = new HttpDispatcher();
        this.errorServlet = new ErrorServlet();
        this.interceptor =new StateInterceptor();
        this.context = new HttpContext(null,null);
    }

    @Override
    public void run() {
        logger.debug("New Client Connect! Connected IP :" + socket.getInetAddress() + ", Port : %d" + socket.getPort());
        try (OutputStream out = socket.getOutputStream()) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            int requestCount = 0;
            //socket connection keep alive
            while (true) {
                try {
                    socket.setSoTimeout(SOCKET_TIMEOUT);
                    Request request = parser.parse(br);
                    if (request != null) {
                        logger.debug(request.toString());
                        String resource = request.getHeader().getResource();
                        if (resource != null) {
                            interceptor.checkHeader(request.getHeader(), droot);
                            request.getHeader().setMax(max-requestCount);
                            requestCount ++;
                            request.getHeader().setSocket_Time(SOCKET_TIMEOUT);
                            context.setRequest(request);
                            //dispatch에서 context의 request에 따라 responce 주입
                            context.setResponse(dispatcher.dispatch(context.getRequest()));
                        }
                        byte[] buffer = new byte[1024];
                        int sz;
                        InputStream inputStream = context.getResponse().getStream();

                        while ((sz  = inputStream.read(buffer)) != -1) {
                          out.write(buffer, 0, sz);
                        }
                        out.flush();

                        if (request.getHeader().getHeaders().containsKey(Header.CONNECTION.getText())) {
                            if ("close".equals(request.getHeader().get(Header.CONNECTION.getText()))) {
                                logger.debug("client requested connection close");
                                break;
                            }
                            if ("keep-alive".equals(request.getHeader().get(Header.CONNECTION.getText()))) {
                                if (request.getHeader().getHeaders().containsKey(Header.KEEP_ALIVE.getText())) {
                                    logger.debug("client requested keep-alive");
                                    max = parseMax(request.getHeader().get(Header.KEEP_ALIVE.getText()));
                                }
                            }
                        }
                    } else break;
                    if(requestCount>=max){
                        logger.debug("over request");
                        break;
                    }
                } catch (HttpError httpError) {
                    errorServlet.writeError(httpError, writer);
                    writer.flush();
                    break;
                } catch (SocketException se) {
                    break;
                } catch (SocketTimeoutException ste) {
                    logger.debug("socket time out");
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
