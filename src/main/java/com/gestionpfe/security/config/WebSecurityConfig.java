package com.gestionpfe.security.config;

import com.gestionpfe.enums.AppUserRole;
import com.gestionpfe.security.PasswordEncoder;
import com.gestionpfe.security.filters.CustomAuthenticationFilter;
import com.gestionpfe.security.filters.CustomAuthorizationFilter;
import com.gestionpfe.services.AppUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AppUserService appUserService;
    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(AppUserService appUserService, PasswordEncoder passwordEncoder) {
        this.appUserService = appUserService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(), appUserService);
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        httpSecurity.cors().configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();

                    corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:4200/"));
                    corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                    corsConfiguration.setAllowCredentials(true);
                    corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                    corsConfiguration.setExposedHeaders(List.of("Authorization"));
                    corsConfiguration.setMaxAge(3600L);

                    return corsConfiguration;
                }).and()
                .csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers("/api/login", "/api/v*/registration/**").permitAll()
                .antMatchers("/api/v1/branch/**").authenticated()
                .antMatchers("/api/v1/department/**").permitAll()
                .antMatchers("/api/v1/domain/**").hasAuthority("ADMIN")
                .antMatchers("/api/v1/establishment/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/pfe-subject/").hasAuthority("SUPERVISOR")
                .antMatchers(HttpMethod.GET,
                        "/api/v1/pfe-subject/",
                        "/api/v1/pfe-subject/search",
                        "/api/v1/pfe-subject/{supervisorId}",
                        "/api/v1/pfe-subject/university/{university-id}",
                        "/api/v1/pfe-subject/university/{university-id}/search",
                        "/api/v1/pfe-subject/establishment/{establishment-id}",
                        "/api/v1/pfe-subject/establishment/{establishment-id}/search",
                        "/api/v1/pfe-subject/department/{department-id}",
                        "/api/v1/pfe-subject/department/{department-id}/search",
                        "/api/v1/pfe-subject/pfe-subject/{pfe-subject-id}",
                        "/api/v1/pfe-subject/student-group/{student-group-id}").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/rendezvous/{student-group-id}").authenticated()
                .antMatchers(HttpMethod.POST, "/api/v1/rendezvous/{student-group-id}").hasAuthority("STUDENT_ACCEPTED_IN_GROUP")
                .antMatchers(HttpMethod.POST, "/api/v1/rendezvous/{rendezvous-id}/group/{student-group-id}").hasAuthority("SUPERVISOR")
                .antMatchers(HttpMethod.POST, "/api/v1/rendezvous/{rendezvous-id}/group/{student-group-id}/accept").hasAuthority("STUDENT_ACCEPTED_IN_GROUP")
                .antMatchers(HttpMethod.POST, "/api/v1/rendezvous/{rendezvous-id}/group/{student-group-id}/reject").hasAuthority("STUDENT_ACCEPTED_IN_GROUP")
                .antMatchers( "/api/v1/student/**").authenticated()
                .antMatchers( "/api/v1/student-group/{pfe-subject-id}").permitAll()
                .antMatchers( "/api/v1/student-group/completed-groups/{pfe-subject-id}").hasAuthority("SUPERVISOR")
                .antMatchers( "/api/v1/student-group/pfe-subject/{pfe-subject-id}").hasAuthority("STUDENT")
                .antMatchers( "/api/v1/student-group/pfe-subject/{pfe-subject-id}/join/{group-id}").hasAuthority("STUDENT")
                .antMatchers( "/api/v1/student-group/pfe-subject/remove-student/{group-id}").hasAuthority("STUDENT")
                .antMatchers( "/api/v1/student-group/accept/{group-id}").hasAuthority("SUPERVISOR")
                .antMatchers( "/api/v1/student-group/refuse/{group-id}").hasAuthority("SUPERVISOR")
                .antMatchers( "/api/v1/student-group/{group-id}/add-drive/").hasAuthority("STUDENT_ACCEPTED_IN_GROUP")
                .antMatchers( "/api/v1/student-group/{group-id}/publish-drive-link/").hasAuthority("SUPERVISOR")
                .antMatchers( "/api/v1/student-group/student/{student-id}").permitAll()
                .antMatchers( "/api/v1/student-group/group/{student-group-id}").authenticated()
                .antMatchers( "/api/v1/supervisor/**").permitAll()
                .antMatchers( "/api/v1/university/**").permitAll()
                .and().addFilter(customAuthenticationFilter)
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        //For h2 console
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder.bCryptPasswordEncoder());
        provider.setUserDetailsService(appUserService);

        return provider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
