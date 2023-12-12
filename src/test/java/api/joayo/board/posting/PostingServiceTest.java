package api.joayo.board.posting;

import api.joayo.board.State;
import api.joayo.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostingServiceTest {

    @Autowired PostingService postingService;
    @Autowired EntityManager em;

    @Test
    public void 글쓰기() {
        //given
        Member member = Member.create("email@gmail.com", "nickname", "pwd111");
//         em.persist(member);
        Posting posting = Posting.create(member, "글제목111", "글내용11111");
        em.persist(posting);

        //when
        Long writtenId = postingService.write(posting);

        //then
        Assertions.assertThat(posting).isEqualTo(postingService.findOne(writtenId).get());

    }
    @Test
    public void 글삭제() {
        //given
        Member member = Member.create("email@gmail.com", "nickname", "pwd111");
        Posting posting = Posting.create(member, "글제목111", "글내용11111");
        Long postingId = postingService.write(posting);

        //when
        postingService.delete(postingId);

        //then
        Posting deletedPosting = postingService.findOne(postingId).get();
        Assertions.assertThat(deletedPosting.getState()).isEqualTo(State.DELETED);

    }

    @Test
    public void 글수정() {
        //given
        Member member = Member.create("email@gmail.com", "nickname", "pwd111");
        Posting posting = Posting.create(member, "글제목111", "글내용11111");
        Long postingId = postingService.write(posting);

        //when
        postingService.modify(postingId, "글제목 수정~~", "글내용 수정!!!");

        //then
        Posting modifiedPosting = postingService.findOne(postingId).get();
        Assertions.assertThat(modifiedPosting.getState()).isEqualTo(State.MODIFIED);
        Assertions.assertThat(modifiedPosting.getTitle()).isEqualTo("글제목 수정~~");
        Assertions.assertThat(modifiedPosting.getContents()).isEqualTo("글내용 수정!!!");
    }


}