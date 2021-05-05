package projekti.helpers;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityHelper {
    
    public static boolean accessorIsLoggedInUser(String accessorUsername) {
        return SecurityContextHolder.getContext().getAuthentication().getName().equals(accessorUsername);
    }
    
}
