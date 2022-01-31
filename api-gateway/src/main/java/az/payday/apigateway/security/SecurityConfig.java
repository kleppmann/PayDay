package az.payday.apigateway.security;

import az.payday.apigateway.security.handler.CustomAccessDeniedHandler;
import az.payday.apigateway.security.handler.CustomAuthenticationEntryPoint;
import az.payday.apigateway.security.jwt.AuthenticationFilter;
import az.payday.apigateway.security.jwt.JwtConfig;
import az.payday.apigateway.security.jwt.JwtTokenVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.actuate.metrics.export.prometheus.PrometheusScrapeEndpoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import javax.crypto.SecretKey;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String ALLOWED_PATH = "/auth/signin";
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final JwtTokenVerifier jwtTokenVerifier;

    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .authorizeRequests()
                .requestMatchers(
                        EndpointRequest.to(InfoEndpoint.class, HealthEndpoint.class, PrometheusScrapeEndpoint.class))
                .permitAll()
                .mvcMatchers(
                        ALLOWED_PATH
                ).permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler)
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .addFilter(
                        new AuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
                .addFilterAfter(jwtTokenVerifier, AuthenticationFilter.class)
                .authorizeRequests()
                .anyRequest()
                .authenticated();
    }
}

