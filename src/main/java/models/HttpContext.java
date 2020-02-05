package models;

public class HttpContext {

    private Request request;
    private Response response;
/*    private File droot;*/

    public HttpContext(Request request, Response response){
        this.request = request;
        this.response = response;
  /*      this.droot = droot;*/
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

/*    public File getDroot(){return droot;}

    public void setDroot(File path){ this.droot = path;
    }*/
    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
