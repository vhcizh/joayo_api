package api.joayo.member;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class MemberRepository {

//    @PersistenceContext : 표준 애노테이션
    private final EntityManager em;

    // 저장하기
    public void save(Member member) {
        em.persist(member); // 영속성 컨텍스트에 엔티티를 넣음. 트랜잭션 commit 되는 시점에 db에 반영됨
    }

    // 조회하기
    public Optional<Member> findOne(Long id) {
        Member member = em.find(Member.class, id); // (타입, pk) 1건 조회
        return Optional.ofNullable(member);
    }

    // 모두 조회하기
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    // 이메일로 조회하기
    public Optional<Member> findByEmail(String email) {
        List<Member> result = em.createQuery("select m from Member m where email = :email", Member.class)
                                .setParameter("email", email)
                                .getResultList();
        return result.stream().findAny();
    }
}
