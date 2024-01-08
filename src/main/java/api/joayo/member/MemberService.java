package api.joayo.member;

import api.joayo.security.SecurityMember;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // lombok - final 필드만 생성
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;


    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원 한명 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }


    /**
     * 회원 가입 v1 - 시큐리티 X
     */
    @Transactional // readOnly = false
    public Long join1(Member member) {
//        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 회원가입 v2 - 시큐리티 O
     */
    @Transactional
    public Long join2(MemberDTO dto) {
        validateDuplicateMember(dto);
        Member member = new Member(dto.getEmail(), dto.getNickname(), encoder.encode(dto.getPassword()));
        memberRepository.save(member);
        return member.getId();
    }

    public void validateDuplicateMember(MemberDTO member) {
        memberRepository.findByEmail(member.getEmail()).ifPresent(x -> {
            throw new IllegalStateException(member.getEmail() + "는 이미 존재하는 이메일입니다.");
        });
    }

    /**
     * 로그인
     */
    @Transactional
    public UserDetails login2(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new UsernameNotFoundException("로그인 실패!!!!!!"));

        return new SecurityMember(member);
    }

    public boolean login3(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("로그인 실패!!!!!!"));

        return encoder.matches(password, member.getPassword());
    }

    // 회원 수정
    @Transactional
    public void update(Long id, String nickname, String password) {
        Member member = memberRepository.findOne(id);
        member.updateNicknamePassword(nickname, password);
    }


//
//    public void delete(Long id) {
////        memberRepository.delete();
//    }
}
