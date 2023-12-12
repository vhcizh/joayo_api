package api.joayo.member;

import lombok.RequiredArgsConstructor;
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


    /**
     * 회원 가입
     */
    @Transactional // readOnly = false
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    public void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail()).ifPresent(x->{
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        });
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원 한명 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    // 회원 수정
    @Transactional
    public void update(Long id, String nickname, String password) {
        Member member = memberRepository.findOne(id);
        member.setNickname(nickname);
        member.setPassword(password);
    }

    public void delete(Long id) {
//        memberRepository.delete();
    }
}
