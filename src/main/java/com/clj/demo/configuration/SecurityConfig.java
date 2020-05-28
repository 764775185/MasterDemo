package com.clj.demo.configuration;

import com.clj.demo.filter.JwtAuthenticationFilter;
import com.clj.demo.filter.JwtAuthorizationFilter;
import com.clj.demo.filter.SecurityBasicAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Autowired
    private AuthenticationEntryPoint authEntryPoint;

    @Autowired
    // 因为UserDetailsService的实现类实在太多啦，这里设置一下我们要注入的实现类
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @Autowired
    private Environment environment;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/user/**","/api/file/**","/api/auth/logout","/api/auth/myselfInfo","/api/auth/permissions","/api/auth/resetPassword").authenticated()
                .anyRequest().permitAll()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint());
    }

    @Bean
    public SecurityBasicAuthFilter securityBasicAuthFilter(){
        boolean enableSwaggerBasicAuth=false;
        String dftUserName="admin",dftPass="admin";
        if (environment!=null){
            String enableAuth=environment.getProperty("swagger.basic.enable");
            enableSwaggerBasicAuth=Boolean.valueOf(enableAuth);
            if (enableSwaggerBasicAuth){
                //如果开启basic验证,从配置文件中获取用户名和密码
                String pUser=environment.getProperty("swagger.basic.username");
                String pPass=environment.getProperty("swagger.basic.password");
                if (pUser!=null&&!"".equals(pUser)){
                    dftUserName=pUser;
                }
                if (pPass!=null&&!"".equals(pPass)){
                    dftPass=pPass;
                }
            }
        }
        SecurityBasicAuthFilter securityBasicAuthFilter=new SecurityBasicAuthFilter(enableSwaggerBasicAuth,dftUserName,dftPass);
        return securityBasicAuthFilter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}