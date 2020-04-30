package data;

public final class WebConfig
{
    public static final String WebpageDir = "/pages/";
    public static final String FreeMarkerExt = ".ftl";
    public static final String Protocol = "http";
    public static final String Host = "localhost";
    public static final int Port = 8080;


    public static final String GetWebpage(String pageName)
    {
        return WebpageDir + pageName + FreeMarkerExt;
    }

    public static final String GetUrl()
    {
        return Protocol + "://" + Host + ":" + Port + "/";
    }

    private WebConfig()
    {
    }
}
