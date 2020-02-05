import service.HttpServer;

import java.io.IOException;

//메인 클래스
public class App {

    public static void main(String[] args) throws IOException {
        HttpServer server = new HttpServer(); //멀티쓰레드 서버
        server.start();
    }
}
