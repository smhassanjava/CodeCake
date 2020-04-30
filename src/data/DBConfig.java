package data;

public final class DBConfig
{
    public static final String Type = "mysql";
    public static final String Server = "localhost";
    public static final int Port = 3306;
    public static final String Database = "projectfunding";
    public static final String User = "root";
    public static final String Password = "1234";

    public static final String GetUrl()
    {
        return "jdbc:" + Type + "://" + Server + ":" + Port + "/" + Database;
    }

    private DBConfig()
    {
    }
}
