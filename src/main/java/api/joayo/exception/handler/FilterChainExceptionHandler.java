package api.joayo.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FilterChainExceptionHandler extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
//            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            response.getWriter().write(e.getMessage());

            ObjectMapper objectMapper = new ObjectMapper();
            Response res = new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Internal Server Error",
                        e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(res));

        }
    }

    @Data
    @AllArgsConstructor
    private static class Response {
        int status;
        String error;
        String message;
    }

}
