package api.joayo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
/*
참고
https://www.baeldung.com/spring-security-authentication-provider
 */
@Service
@RequiredArgsConstructor
public class AuthenticationProviderService implements AuthenticationProvider {

    private final JpaMemberDetailsService memberDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        MemberDetails member = memberDetailsService.loadUserByUsername(username);

        return checkPassword(member, password, bCryptPasswordEncoder);
    }
    private Authentication checkPassword(MemberDetails member,
                                         String rawPassword,
                                         PasswordEncoder encoder) {
        if (encoder.matches(rawPassword, member.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    member.getUsername(),
                    member.getPassword(),
                    member.getAuthorities());
        } else {
            throw new BadCredentialsException("Bad credentials");
        }

    }

    // 지원되는 인증 구현 형식 지정 (UsernamePasswordAuthenticationToken)
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
