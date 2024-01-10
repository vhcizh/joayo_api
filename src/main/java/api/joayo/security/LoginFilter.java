package api.joayo.security;

import api.joayo.security.jwt.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

//    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
//        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        setAuthenticationManager(authenticationManager);

        // 로그인 경로 변경
        // UsernamePasswordAuthenticationFilter의 디폴트 url이 /login인데, 다른 컨트롤러 경로와 통일하기 위해서 바꿨다.
        setFilterProcessesUrl("/api/login");
    }

/// 부모클래스의 메서드를 그대로 사용. 대신 상위 클래스에서 AuthenticationManager가 null이어서 생성자에서 setAuthenticationManager 해줌.

//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//
//
//        String username = this.obtainUsername(request);
//        String password = this.obtainPassword(request);
//
//        UsernamePasswordAuthenticationToken authToken = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
//        this.setDetails(request, authToken);
//
//        return authenticationManager.authenticate(authToken);
//    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        System.out.println("authentication = " + authentication);
        String token = jwtUtil.generateToken(authentication);

        response.addHeader("Authorization", "Bearer " + token);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");

    }

    // 로그인 실패시 예외 발생
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        System.out.println("=== login failed ===");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
//        throw failed;
        throw new JwtException("로그인 실패");
    }


}
