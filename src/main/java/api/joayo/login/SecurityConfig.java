package api.joayo.login;

//import api.joayo.security.AuthenticationProviderService;
import api.joayo.member.MemberRepository;
import api.joayo.security.JpaSecurityMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
//    private final MemberRepository repository;
    private final JpaSecurityMemberService memberDetailsService;
//    private final AuthenticationProviderService authenticationProvider;

    // https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            // http.securityMatcher("/api/**") // 시큐리티 6 이상
            http
                    .authorizeHttpRequests(req -> req
                            .antMatchers("/api/login/**","/api/join/**", "/api/members/**").permitAll()
                            .anyRequest().authenticated());  // .anyRequest().authenticated()

            http.csrf(AbstractHttpConfigurer::disable); // 기본적으로 활성화 되는데 비활성화 시킴

            http.httpBasic();   // 클라이언트가 요청 헤더에 사용자 이름과 비밀번호를 포함시켜야 한다. 인증이나 세션을 다른 방식으로 처리하는 경우에 비활성화할 수 있습니다.



    //                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT 사용시 적용
//            http
//                    .cors(custom -> custom.configurationSource(corsConfigurationSource()))
//                    .logout(Customizer.withDefaults());
//        http.csrf(AbstractHttpConfigurer::disable);

            return http.build();
    }

    // https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html
    // https://www.baeldung.com/spring-cors

    @Bean
    public UserDetailsService userDetailsService(MemberRepository memberRepository) {
//        return new JpaSecurityMemberService(memberRepository);
        return memberDetailsService;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance(); // 기존에 회원가입 시 비밀번호 암호화하지 않아서 우선 임시적으로 문자열 그대로 비교하도록 함
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }


//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
//        configuration.setAllowedMethods(List.of("*"));
//        configuration.setAllowedHeaders(List.of("*"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/api/**", configuration);
//        return source;
//    }



//    // 디버그 레벨을 추가하고 이미지같은 특정 경로는 무시함
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.debug(securityDebug)
//                .ignoring()
//                .antMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico");
//    }


}
