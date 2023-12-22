package api.joayo.login;

import api.joayo.security.AuthenticationProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/*
     Since Spring Security version 5.7.0-M2, Spring deprecates the use of WebSecurityConfigureAdapter
     and suggests creating configurations without it.
     https://www.baeldung.com/spring-deprecated-websecurityconfigureradapter
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    // jjwt
    // https://github.com/jwtk/jjwt?tab=readme-ov-file#quickstart

    // basic
    // https://www.baeldung.com/spring-security-basic-authentication

    // default-password-encoder since 5
    // https://www.baeldung.com/spring-security-5-default-password-encoder

    // xss
    // https://www.baeldung.com/spring-prevent-xss


    private final AuthenticationProviderService authenticationProvider;

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        return authenticationManagerBuilder.build();
    }

//    @Bean
//    public void configure(AuthenticationManagerBuilder provider) {
//        provider.authenticationProvider(authenticationProvider);
//    }
    // .authenticationProvider(authenticationProvider)

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            // http.securityMatcher("/api/**") // 시큐리티 6 이상
            http
                    .authorizeHttpRequests(req -> req
                            .antMatchers("/api/login/**","/api/join/**").permitAll()
                            .antMatchers("/api/members").permitAll()
                            .antMatchers(HttpMethod.GET, "/api/board/**").permitAll()
                            .antMatchers(HttpMethod.POST,"/api/**").hasAnyAuthority("USER","ADMIN")
                            .anyRequest().denyAll());  // .anyRequest().authenticated()

    //                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT 사용시 적용
    //                .authenticationProvider(new DaoAuthenticationProvider()) // 인증
            http
                    .cors(custom -> custom.configurationSource(corsConfigurationSource()))
                    .logout(Customizer.withDefaults());
            http
                    .httpBasic(AbstractHttpConfigurer::disable)
                    .csrf(AbstractHttpConfigurer::disable)
                    .formLogin(AbstractHttpConfigurer::disable);

            return http.build();
        // https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html
    }

    // https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
//        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));
//        configuration.setAllowCredentials(true);
////        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Authorization-refresh", "Cache-Control", "Content-Type"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/api/**", configuration);
//        return source;
//    }

    // https://www.baeldung.com/spring-cors
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }



//    // 디버그 레벨을 추가하고 이미지같은 특정 경로는 무시함
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.debug(securityDebug)
//                .ignoring()
//                .antMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico");
//    }
//    public DaoAuthenticationProvider authenticationProvider() {
//
//    }

}
