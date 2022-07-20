package com.example.be.security;

import com.example.be.security.filter.CustomAuthenticationFilter;
import com.example.be.security.filter.CustomeAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(
                authenticationManagerBean()
        );
        customAuthenticationFilter.setFilterProcessesUrl("/login");
        http.cors().configurationSource(request -> {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedOrigins(List.of("http://localhost:3000",
                    "http://localhost:8081"));
            configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
            configuration.setAllowedHeaders(List.of("*"));
            return configuration;
        });
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/login/**", "/refresh-token/**",
                "/api/otp/register/**",
                "/api/otp/validate/**").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/users/**")
                .hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/api/users/save/**")
                .hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(new CustomeAuthorizationFilter(),
                UsernamePasswordAuthenticationFilter.class); //intercept every request before before filters
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
