package api.joayo.login;

//import api.joayo.security.AuthenticationProviderService;
import api.joayo.exception.handler.FilterChainExceptionHandler;
import api.joayo.member.MemberRepository;
import api.joayo.security.JpaSecurityMemberService;
import api.joayo.security.LoginFilter;
import api.joayo.security.jwt.JwtFilter;
import api.joayo.security.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/*
     Since Spring Security version 5.7.0-M2, Spring deprecates the use of WebSecurityConfigureAdapter
     and suggests creating configurations without it.
     https://www.baeldung.com/spring-deprecated-websecurityconfigureradapter
 */
//@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JpaSecurityMemberService memberDetailsService;
    private final JwtUtil jwtUtil;

    // https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // http.securityMatcher("/api/**") // 시큐리티 6 이상
        http
                .authorizeHttpRequests(req -> req
                        .antMatchers("/api/login/**","/api/join/**", "/api/members/**").permitAll()
                        .antMatchers(HttpMethod.GET,"/api/board/**").permitAll()
                        .anyRequest().authenticated());  // .anyRequest().authenticated()


        // JWT 전환
        http.csrf(AbstractHttpConfigurer::disable); // 기본적으로 활성화 되는데 비활성화 시킴 (세션방식은 csrf를 필수적으로 방어해줘야하지만 토큰방식을 사용할 경우 disable)
        http.httpBasic(AbstractHttpConfigurer::disable);   // 클라이언트가 요청 헤더에 사용자 이름과 비밀번호를 포함시켜야 한다. 인증이나 세션을 다른 방식으로 처리하는 경우에 비활성화할 수 있습니다.
        http.formLogin(AbstractHttpConfigurer::disable);

        // JWT 방식은 세션을 stateless 하게 관리
        http.sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 기존에 formlogin시 사용되는 UsernamePasswordAuthenticationFilter
        // formlogin을 disable 했으므로 해당 필터를 대체하는 다른 필터를 등록해준다.
        http.addFilterAt(new LoginFilter(authenticationManager(), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        // JWTFilter 등록
        http.addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);

        // CORS 설정
        http.cors(cors-> new CorsConfiguration());

        // filter chain exception handler 추가
        http.addFilterBefore(new FilterChainExceptionHandler(new ObjectMapper()), LogoutFilter.class);

        return http.build();
    }

    // https://docs.spring.io/spring-security/reference/servlet/integrations/cors.html
    // https://www.baeldung.com/spring-cors

    @Bean
    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance(); // 기존에 회원가입 시 비밀번호 암호화하지 않아서 우선 임시적으로 문자열 그대로 비교하도록 함
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(memberDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return new ProviderManager(authenticationProvider);
    }


//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
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
