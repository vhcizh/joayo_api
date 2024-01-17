package api.joayo.board.posting;

import api.joayo.member.Member;
import api.joayo.member.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class PostingController {

    private final PostingService postingService;
    private final MemberRepository memberRepository;

    // 등록
    @PostMapping("/postings")
    public WritePostingResponse writePosting(@RequestBody WritePostingRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow();
        Posting posting = Posting.create(member, request.getTitle(), request.getContents());
        Long id = postingService.write(posting);
        return new WritePostingResponse(id);
    }

    @Data
    static class WritePostingRequest {
        private String email;
        private String title;
        private String contents;
    }

    @Data
    @AllArgsConstructor
    static class WritePostingResponse {
        private Long id;
    }

    // 수정
    @PatchMapping("/postings/{postingId}")
    public ModifyPostingResponse modifyPosting(@PathVariable Long postingId, @RequestBody ModifyPostingRequest request) {
        postingService.modify(postingId, request.getTitle(), request.getContents());
        Posting posting = postingService.findOne(postingId).orElseGet(Posting::new);
        return new ModifyPostingResponse(posting.getId());
    }

    @Data
    static class ModifyPostingRequest {
        private String title;
        private String contents;
    }

    @Data
    @AllArgsConstructor
    static class ModifyPostingResponse {
        private Long id;
    }

    // 삭제
    @DeleteMapping("/postings/{postingId}")
    public void deletePosting(@PathVariable Long postingId) {
        postingService.delete(postingId);
    }

    // 목록 조회
    @GetMapping("/postings")
    public Result<List<PostingDTO>>  postingList() {
        List<PostingDTO> postingDTOList = postingService.findAll().stream()
                .map(PostingDTO::create)
                .collect(Collectors.toList());
        return new Result<>(postingDTOList);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    static class PostingDTO {
        private Long postingId;
        private String nickname;
        private String email;
        private String title;
        private String contents;
        private int viewCount;
        private int commentCount;
        private int likeCount;
        private String postingDate;
        // state

        private static PostingDTO create(Posting posting) {
            PostingDTO dto = new PostingDTO();

            dto.postingId = posting.getId();
            dto.nickname = posting.getWriter().getNickname();
            dto.email = posting.getWriter().getEmail();
            dto.title = posting.getTitle();
            dto.contents = posting.getContents();
            dto.viewCount = posting.getViewCount();
            dto.commentCount = posting.getCommentCount();
            dto.likeCount = posting.getLikeCount();
            dto.postingDate = posting.getPostingDate().format(
                    DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

            return dto;
        }

    }

    // 상세페이지
    @GetMapping("/postings/{postingId}")
    public Result<PostingDTO> getPosting(@PathVariable Long postingId) {
        return new Result<>(
                postingService.findOne(postingId)
                    .map(PostingDTO::create)
                    .orElseGet(PostingDTO::new)
        );
    }

    // 조회수 증가
    @PostMapping("/postings/{postingId}")
    public void addView(@PathVariable Long postingId) {
        postingService.addView(postingId);
    }

}
