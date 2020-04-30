package utils;

public final class StringUtils
{
    private StringUtils()
    {
    }

    public static String NonNull(String string)
    {
        return string == null ? "" : string;
    }

    public static boolean IsNullOrEmpty(String string)
    {
        return string == null || string.length() == 0;
    }

    public static boolean IsNullOrWhitespace(String string)
    {
        return string == null || string.trim().length() == 0;
    }
}
