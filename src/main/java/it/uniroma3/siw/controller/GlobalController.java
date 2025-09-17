package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;


import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.service.CredentialsService;

@ControllerAdvice //this annotation makes the attributes defined inside it, gain global visibility in every template
public class GlobalController { 

    @Autowired
    private CredentialsService credentialsService;
    

    // Returns the current logged-in user's details (either OIDC user or standard UserDetails)
    @ModelAttribute("userDetails")
    public Object getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            Object principal = authentication.getPrincipal();

            // Check if the principal is an OIDC user (OAuth2)
            if (principal instanceof DefaultOidcUser) {
                DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
                return oidcUser;  // You can also return specific attributes like oidcUser.getEmail()
            }
            // Check if the principal is a standard UserDetails user
            else if (principal instanceof UserDetails) {
                return (UserDetails) principal;
            }
        }
        return null;  // Return null if no user is authenticated
    }

    // Returns the credentials of the logged-in user
    @ModelAttribute("credentials")
    public Credentials getAuthenticatedUserCredentials() {
        Credentials credentials = credentialsService.getAuthenticatedUserCredentials().orElse(null);
        if (credentials != null) {
            System.out.println("GLOBALCONTROLLER: Credentials found: " + credentials.getEmail());
        } else {
            System.out.println("GLOBALCONTROLLER: No credentials found.");
        }
        return credentials;
    }
}
