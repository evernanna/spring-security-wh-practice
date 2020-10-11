package me.aurum.springsecurity01securityform.form;

import me.aurum.springsecurity01securityform.Account.Account;
import me.aurum.springsecurity01securityform.Account.AccountContext;
import me.aurum.springsecurity01securityform.common.SecurityLogger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SampleService {

    public void dashboard() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails) authentication.getPrincipal();

        System.out.println("=====================");
        System.out.println(authentication);
        System.out.println(principal.getUsername());

        /*Account account = AccountContext.getAccount();
        System.out.println("=====================");
        System.out.println(account.getUsername());*/

        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
        Object credentials = authentication.getCredentials();
        boolean authenticated = authentication.isAuthenticated();*/
    }

    @Async
    public void asyncService() {
        SecurityLogger.log("Async Service");
        System.out.println("Async service is called");
    }
}
