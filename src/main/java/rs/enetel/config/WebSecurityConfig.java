package rs.enetel.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

    @EnableWebSecurity
    @Configuration
    public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()  // Refactor login form
                    .headers().addHeaderWriter(
                    new XFrameOptionsHeaderWriter(
                            XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)).and()

                    .formLogin()
                    .defaultSuccessUrl("/index.html")
                    .loginPage("/login.html")
                    .failureUrl("/login.html?error")
                    .permitAll()

                    .and()
                    .logout()
                    .logoutSuccessUrl("/login.html?logout")
                    .logoutUrl("/logout.html")
                    .permitAll()

                    .and()
                    .authorizeRequests()
                    .antMatchers("/assets/**").permitAll()
                    .anyRequest().authenticated()
                    .and();
        }


        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .inMemoryAuthentication()
                    .withUser("peronska").password("tabla").roles("USER").and()
                    .withUser("stanicna").password("tabla").roles("ADMIN","USER");
        }
}
