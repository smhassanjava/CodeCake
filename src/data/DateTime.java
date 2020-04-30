package data;

import java.util.*;

public class DateTime {
    public long value;

    public DateTime(long value)
    {
        this.value = value;
    }

    public static DateTime Now()
    {
        return new DateTime(new Date().getTime());
    }

    public boolean IsEarlier(DateTime dateTime)
    {
        assert dateTime != null;
        return value < dateTime.value;
    }
}
