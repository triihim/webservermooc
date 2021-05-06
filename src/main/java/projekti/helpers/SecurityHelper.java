package projekti.helpers;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityHelper {
    
    public static boolean accessorIsLoggedInUser(String accessorUsername) {
        return SecurityHelper.requesterUsername().equals(accessorUsername);
    }
    
    public static String requesterUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    
}
