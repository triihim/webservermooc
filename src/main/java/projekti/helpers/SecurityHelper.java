package projekti.helpers;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import projekti.configuration.CustomUserDetails;

public class SecurityHelper {
    
    public static boolean accessorIsLoggedInUser(String accessorUsername) {
        return SecurityHelper.requesterUsername().equals(accessorUsername);
    }
    
    public static String requesterUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    
    public static long requesterId() {
        return ((CustomUserDetails)(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getId();
    }

    public static boolean isLoggedIn() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        return ctx.getAuthentication() != null &&
               ctx.getAuthentication().getPrincipal() != null &&
               ctx.getAuthentication().getPrincipal() instanceof CustomUserDetails;
    }
    
}

