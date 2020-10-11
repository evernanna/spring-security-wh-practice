package me.aurum.springsecurity01securityform.Account;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    /*@Test
    public void index_anonymous() throws Exception {
        mockMvc.perform(get("/").with(anonymous()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void index_user() throws Exception {
        // 유가가 로그인한 상태로 가정(db에는 없음)
        mockMvc.perform(get("/").with(user("aurum").roles("USER")))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void admin_user() throws Exception {
        // 유가가 로그인한 상태로 가정(db에는 없음)
        mockMvc.perform(get("/admin").with(user("aurum").roles("USER")))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void admin_admin() throws Exception {
        // 유가가 로그인한 상태로 가정(db에는 없음)
        mockMvc.perform(get("/admin").with(user("aurum").roles("ADMIN")))
                .andDo(print())
                .andExpect(status().isOk());
    }*/

    @Test
    @WithAnonymousUser
    public void index_anonymous() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "aurum", roles = "USER")
    public void index_user() throws Exception {
        // 유가가 로그인한 상태로 가정(db에는 없음)
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    // 커스텀 어노테이션 - 메타 어노테이션
    @Test
    @WithUser
    public void admin_user() throws Exception {
        // 유가가 로그인한 상태로 가정(db에는 없음)
        mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "aurum", roles = "ADMIN")
    public void admin_admin() throws Exception {
        // 유가가 로그인한 상태로 가정(db에는 없음)
        mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void login() throws Exception {
        String username = "aurum";
        String password = "123";
        String role = "USER";
        Account user = this.createUser(username, password, role);

        mockMvc.perform(formLogin().user(user.getUsername()).password(password))
                .andExpect(authenticated());

    }

    @Test
    @Transactional
    public void login_fail() throws Exception {
        String username = "aurum";
        String password = "123";
        String role = "USER";
        Account user = this.createUser(username, password, role);

        mockMvc.perform(formLogin().user(user.getUsername()).password(password))
                .andExpect(authenticated());

    }

    private Account createUser(String username, String password, String role) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        account.setRole(role);
        return accountService.createNew(account);
    }

}