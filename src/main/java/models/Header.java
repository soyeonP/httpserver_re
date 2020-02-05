package models;

public enum Header {
    IF_MODIFIED_SINCE("If-Modified-Since"),
    IF_NONE_MATCH("If-None-Match"),
    CONNECTION("Connection"),
    KEEP_ALIVE("Keep-Alive"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type");

    private final String text;

    Header(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return text;
    }
}
