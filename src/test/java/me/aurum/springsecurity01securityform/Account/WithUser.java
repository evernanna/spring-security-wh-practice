package me.aurum.springsecurity01securityform.Account;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = "aurum", roles = "USER")
public @interface WithUser {
}
