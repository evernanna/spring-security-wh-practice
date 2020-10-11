package me.aurum.springsecurity01securityform.Config;

import me.aurum.springsecurity01securityform.Account.AccountService;
import me.aurum.springsecurity01securityform.filter.LoggingFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
//@Order(Ordered.LOWEST_PRECEDENCE - 10)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    AccountService accountService;

    /*@Autowired
    public AccessDecisionManager accessDecisionManager() {

        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);

        WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
        webExpressionVoter.setExpressionHandler(handler);

        List<AccessDecisionVoter<? extends Object>> voters = Arrays.asList(webExpressionVoter);
        return new AffirmativeBased(voters);
    }*/

    @Autowired
    public SecurityExpressionHandler expressionHandler() {

        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_USER");

        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);

        return handler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 필터 추가
        http.addFilterBefore(new LoggingFilter(), WebAsyncManagerIntegrationFilter.class);


        http.authorizeRequests()
                .mvcMatchers("/", "/info", "/account/**", "/signup").permitAll()
                .mvcMatchers( "/admin").hasRole("ADMIN")
                .mvcMatchers( "/user").hasRole("USER")
                //.accessDecisionManager(accessDecisionManager())
                .anyRequest().authenticated()
                .expressionHandler(expressionHandler());
        http.formLogin();
        http.httpBasic();
        //http.csrf().disable();

        http.logout().logoutSuccessUrl("/");

        http.formLogin()
                //.usernameParameter("my-username")
                //.passwordParameter("my-password");
                .loginPage("/login")
                .permitAll();

        http.sessionManagement()
                .sessionFixation()
                    .changeSessionId()
                .maximumSessions(1)
                    .maxSessionsPreventsLogin(false);

        http.exceptionHandling()
                .accessDeniedPage("/access-denied");
        /*.accessDeniedHandler(new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                String username = principal.getUsername();
                System.out.println(username + " is denied to access" + request.getRequestURI());
                response.sendRedirect("/access-denied");
            }
        })*/


        // 하위 스레드 생성시 인증정보  공유
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //web.ignoring().mvcMatchers("/favicon.ico");
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    // 명시적 (안써도 됨)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService);
    }

    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("aurum").password("{noop}456").roles("USER").and()
                .withUser("admin").password("{noop}123").roles("ADMIN");
    }*/
}
