package projekti.helpers;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class TimestampHelper {
    
    public static ZonedDateTime getCurrentDateTime() {
        return ZonedDateTime.now(ZoneOffset.UTC);
    }
    
}
