package api.joayo.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class FilterChainExceptionHandler extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter()
                    .write(
                            objectMapper.writeValueAsString(
                                    new FilterChainExceptionResponse(
                                            response.getStatus(),
                                            "토큰이 만료되었습니다."
                                    )
                            )
                    );
        } catch (JwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter()
                    .write(
                            objectMapper.writeValueAsString(
                                    new FilterChainExceptionResponse(
                                            response.getStatus(),
                                            e.getMessage()
                                    )
                            )
                    );
        } catch (RuntimeException e) {

            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter()
                    .write(
                            objectMapper.writeValueAsString(
                                    new FilterChainExceptionResponse(
                                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                            e.getMessage()
                                    )
                            )
                    );
        }
    }

    @Data
    @AllArgsConstructor
    private static class FilterChainExceptionResponse {
        int status;
        String message;
    }

}
