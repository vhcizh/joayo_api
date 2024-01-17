package api.joayo.board.posting;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostingService {

    private final PostingRepository postingRepository;

    /**
     * 글쓰기
     */
    @Transactional
    public Long write(Posting posting) {
        postingRepository.save(posting);
        return posting.getId();
    }

    /**
     * 전체 글 조회
     * TODO:  검색조건  추가하기
     */
    public List<Posting> findAll() {
        return postingRepository.findAll();
    }

    // 한개 조회
    @Transactional
    public Optional<Posting> findOne(Long postingId) {
        Posting posting = postingRepository.findOne(postingId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 글입니다."));
        return Optional.of(posting);
    }

    @Transactional
    public void addView(Long postingId) {
        postingRepository.findOne(postingId).ifPresent(Posting::addView);
    }

    // 수정
    @Transactional
    public void modify(Long postingId, String newTitle, String newContents) {
        postingRepository.findOne(postingId).ifPresent(old -> old.modify(newTitle, newContents));
    }

    // 삭제
    @Transactional
    public void delete(Long postingId) {
        postingRepository.findOne(postingId).ifPresent(Posting::delete);
    }

    // 작성자로 조회하기 (내가 쓴 글) Member에 있어야?

}
