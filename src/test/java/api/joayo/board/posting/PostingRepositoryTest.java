package api.joayo.board.posting;

import api.joayo.board.State;
import api.joayo.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest
@Transactional
class PostingRepositoryTest {
//
//    @Autowired
//    PostingRepository postingRepository;
//
//    @Test
//    public void testPosting() {
//        // given
//        Posting posting = new Posting("");
////        posting.setWriter(new Member(1L, "email111@gmail.com", "nickname123" , "pwd1234"));
//        posting.setState(State.UPLOADED);
//        posting.setTitle("글제목");
//        posting.setContents("글내용123123");
//
//        // when
//        postingRepository.save(posting);
//        Optional<Posting> foundPosting = postingRepository.findOne(posting.getId());
//
//        // then
//        Assertions.assertThat(posting).isEqualTo(foundPosting.get());
//
//    }
}