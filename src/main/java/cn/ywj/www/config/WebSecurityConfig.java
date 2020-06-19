package cn.ywj.www.config;

import cn.ywj.www.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserService userService;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/img/**","/css/**", "/js/**","/images/**","/register","/questionnaire/**","/reply/**","/showAvatar/**")
                .permitAll()
                .anyRequest()
                .authenticated()       //任何请求都必须经过认证才能访问
                .and()
                .formLogin()                                    //定制登录表单
                .loginPage("/login")//设置登录url-定制登录页面
                .defaultSuccessUrl("/manage")
                .permitAll()
                .and()
                .logout().
                logoutSuccessUrl("/login?logout").permitAll()
                .and().csrf().disable();
    }
}
