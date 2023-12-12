package api.joayo.board.posting;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostingRepository {

    private final EntityManager em;

    // 등록
    public void save(Posting posting) {
        if(posting.getId() == null) {
            em.persist(posting);
        } else {
            em.merge(posting);
        }
    }

    // 조회
    public Optional<Posting> findOne(Long id) {
        Posting posting = em.find(Posting.class, id);
        return Optional.ofNullable(posting);
    }

    // 모두 조회하기 - 검색조건  추가하기 (State.DELETED, UPLOADED, MODIFIED
    public List<Posting> findAll() {
        return em.createQuery("select p from Posting p", Posting.class)
                .getResultList();
    }

    // 수정


    // 삭제

}
