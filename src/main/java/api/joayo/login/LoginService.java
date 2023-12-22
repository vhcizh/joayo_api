package api.joayo.login;

import api.joayo.member.Member;
import api.joayo.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /**
     * 로그인
     */
    public UserDetails login(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password)
                                        .orElseThrow(() -> new UsernameNotFoundException("로그인 실패!!!!!!"));

        return User.withUsername(member.getEmail())
                .password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(member.getPassword()))
                .roles("ROLE_USER")
                .build();
    }

    /**
     * 회원가입 v1
     */
    public Long join1(Member member) {
//        UsernamePasswordAuthenticationToken
        validateDuplicateMember(member);
        PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(member.getPassword());
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 회원가입 v2
     */
    public Long join2(Member member) {
        validateDuplicateMember(member);

        memberRepository.save(member);

        return 0L;
    }

    /**
     * 이메일 중복 조회
     */
    public void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail()).ifPresent(x->{
            throw new IllegalStateException(member.getEmail()+"는 이미 존재하는 이메일입니다.");
        });
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
