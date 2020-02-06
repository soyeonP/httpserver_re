package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer implements IHttpServer {
    private static int port = 8080;
    private final File droot;
    private static int THREAD_CNT = 200; //코어 스레드 수 : 스레드 수가 증가된후 사용 안하는 스레드를 스레드풀에서 제거할 때 최소 유지해야 하는 스레드의 개수
    private ServerSocket listener = null;
    private static ExecutorService threadPool;
    private Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public HttpServer(String drootName, int port) throws IOException {
        this.droot = new File(drootName);
        listener = new ServerSocket(port);

        threadPool = Executors.newFixedThreadPool(THREAD_CNT);
        if (!droot.exists() || !droot.isDirectory()) {
            throw new RuntimeException("This docroot is not exist or is not a directory: " + drootName);
        }
        String date = new Date().toString();
        System.out.println("http server started at " + port + "port" + date);
    }

    public HttpServer() throws IOException {
        this("./web", port);
    }

    @Override
    public void start() {

            try { //multi thread
                Socket socket;
                while((socket = listener.accept()) != null) {
                    logger.debug("accept :" + socket.toString());
                    threadPool.execute(new ConnectionWrap(droot, socket));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                shutdown();
            }
    }

    @Override
    public void shutdown() {
        logger.debug("start to shut down");
        threadPool.shutdown();
        try {
            listener.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logger.debug("succeed to close");
        }
    }
}
