package pl.java.scalatech.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@ComponentScan(basePackages="pl.java.scalatech.security")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {




    @Override
    public void configure(WebSecurity web) throws Exception {
        // @formatter:off
        web.ignoring().
        antMatchers("/assets/**")
        .antMatchers("/css/**")
        .antMatchers("/js/**")
        .antMatchers("/images/**")
        .antMatchers("/favicon.ico")
        .antMatchers("/webjars/**");
        // @formatter:on
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requiresChannel().anyRequest().requiresSecure();
        // @formatter:off
        http.authorizeRequests().anyRequest().authenticated()
             .and().x509()
             .and().sessionManagement()
             .sessionCreationPolicy(SessionCreationPolicy.NEVER).and()
        .csrf().disable();

       // @formatter:on
    }

   @Autowired
    public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
     // @formatter:off

        auth.inMemoryAuthentication().withUser("przodownik").password("slawek").roles("USER").and()
                                     .withUser("admin").password("slawek").roles( "ADMIN").and()
                                     .withUser("Borowiec").password("slawek").roles("USER");
     // @formatter:on
    }

}
