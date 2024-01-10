package api.joayo.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

//    @Autowired
//    MemberService memberService;
//    @Autowired
//    MemberRepository memberRepository;
//
//    @Test
//    @Commit   // 메서드 실행 중에 예외가 발생하지 않으면 트랜잭션이 커밋됨
//    @Rollback(value = false)  //메서드 실행 중에 예외가 발생해도 트랜잭션이 커밋됨 (예외 없을때만 커밋되도록 설정 가능)
//    public void 회원가입() throws Exception {
//        // given
//        Member member = new Member();
//        member.setEmail("qwerty@gmail.com");
//        member.setNickname("쿼티");
//        member.setPassword("pwd1234");
//
//        // when
//        Long savedId = memberService.join(member);
//
//        // then
//        Member findMember = memberRepository.findOne(savedId);
//        Assertions.assertThat(member).isEqualTo(findMember);
////        assertEquals(member, memberRepository.findOne(savedId));
//    }
//
//    @Test
//    public void 중복확인예외() throws Exception {
//        // given
//        Member member1 = new Member();
//        member1.setEmail("member@gmail.com");
//        member1.setNickname("쿼티111");
//        member1.setPassword("pwd1234");
//
//        Member member2 = new Member();
//        member2.setEmail("member@gmail.com");
//        member2.setNickname("쿼티222");
//        member2.setPassword("pwd1234");
//
//        // when
//        memberService.join(member1);
//        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));// 예외 발생해야함.
//
//        // then
//        Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 이메일입니다.");
//
//    }

}