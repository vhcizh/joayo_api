package api.joayo.security;

import api.joayo.member.Member;
import api.joayo.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class JpaSecurityMemberService implements UserDetailsService {

    private final MemberRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일과 일치하는 회원을 찾을 수 없습니다."));
        return new SecurityMember(member);
    }

}
