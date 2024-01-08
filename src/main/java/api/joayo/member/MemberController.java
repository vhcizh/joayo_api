package api.joayo.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    // 가입
    @PostMapping
    public CreateMemberResponse join(@RequestBody CreateMemberRequest request) {
        Member member = new Member(request.getEmail(), request.getNickname(), request.getPassword());
        Long id = memberService.join1(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest {
        private String email;  // 이메일
        private String nickname;    // 닉네임
        private String password;    // 비밀번호
    }

    @Data
    @AllArgsConstructor
    static class CreateMemberResponse {
        private Long id;
    }

//    // 조회 - 프로필
//    @GetMapping("/{id}")
//    public Member getProfile(@PathVariable Long id) {
//        return memberService.findOne(id);
//    }

//    // 전체 회원 조회
//    @GetMapping("/v1")
//    public List<Member> findAllMembers1() {
//        return memberService.findMembers();
//    }
//

    // 전체 회원 조회
    @GetMapping("/v2")
    public Result<List<MemberDTO>> findAllMembers() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDTO> collect = findMembers.stream()
                .map(m -> new MemberDTO(m.getEmail(), m.getNickname(), m.getPassword()))
                .collect(Collectors.toList());
        return new Result<>(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int total;
        private T data;
    }


    // 수정
    @PatchMapping("/{id}")
    public UpdateMemberResponse updateProfile(@PathVariable Long id, @RequestBody UpdateMemberRequest request) {
        memberService.update(id, request.getNickname(), request.getPassword());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId());
    }

    @Data
    static class UpdateMemberRequest {
        private String nickname;    // 닉네임
        private String password;    // 비밀번호
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
    }

//    // 삭제
//    @DeleteMapping("/{id}")
//    public void deleteAccount(@PathVariable Long id) {
//        memberService.delete(id);
//    }


}
