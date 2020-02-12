import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ErrorTest {
    private static int socket_cnt = 1;
    private static String resource="/index.html";
    private static String Method="GET";
    private static String httpverion ="HTTP/1.1";
    public static final String CRLF = "\r\n";
    public static void main(String[] args) throws IOException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
        ErrorTest test = new ErrorTest();
        System.out.print("Resource : ");
        resource = bf.readLine();
        System.out.print("Method : ");
        Method = bf.readLine();
        test.sendSocket(socket_cnt,resource,Method,httpverion);
    }

    public void sendSocket(int cnt,String resource,String Method, String httpversion) {
        for(int loop = 1; loop <= cnt; loop++) {
            StringBuilder sb = new StringBuilder();
            sb.append(Method).append(" ");
            sb.append(resource).append(" ");
            sb.append(httpversion).append(CRLF);
            sb.append("Host: localhost:8080").append(CRLF).append("Content-Type: multipart/form-data;").append(CRLF).append(CRLF);
            sendSocketData(sb.toString());
        }
    }

    public void sendSocketData(String data) {
        Socket sock = null;
        try {
            sock = new Socket("", 8080);
            OutputStream stream = sock.getOutputStream();
            BufferedOutputStream out = new BufferedOutputStream(stream);
           PrintWriter pw = new PrintWriter(stream);
            BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            System.out.println("Client:Connect status = "+sock.isConnected());
            //Thread.sleep(1500);
            pw.println(data);
            pw.flush();
            System.out.println("Client:Send data");

            String line = null;
            System.out.println("Client:Get data :");
            System.out.println("---------------------<Server send me>-----------------------------");
            while((line = br.readLine())!= null)
                System.out.println(line);
            System.out.println("------------------------------------------------------------------");
            }  catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(sock != null) {
                try {
                    sock.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    }