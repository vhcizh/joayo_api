package api.joayo.security.jwt;

import api.joayo.member.Authority;
import api.joayo.member.Member;
import api.joayo.security.SecurityMember;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            System.out.println("json web token : null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        System.out.println("authorization now");
        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];
        boolean jwtIsExpired = false;
            jwtIsExpired = jwtUtil.isExpired(token);
//        try {
//        } catch(ExpiredJwtException e) {
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            response.getWriter().write("JWT expired: " + e.getMessage());
//            return;
//        }
        System.out.println("token is expired : " + jwtIsExpired);

        //토큰 소멸 시간 검증
        if (jwtIsExpired) {

            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        String nickname = jwtUtil.getNickname(token);
        String role = jwtUtil.getRole(token);

        Member member = new Member(username, nickname,"tempPassword", Authority.valueOf(role));
        SecurityMember memberDetails = new SecurityMember(member);

        //스프링 시큐리티 인증 토큰 생성
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(memberDetails, null, memberDetails.getAuthorities());

        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
