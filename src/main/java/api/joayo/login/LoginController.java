package api.joayo.login;

import api.joayo.member.Member;
import api.joayo.member.MemberDTO;
import api.joayo.member.MemberRepository;
import api.joayo.member.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    /**
     * 회원가입 v1 : 시큐리티 x db에서 바로
     */
    @PostMapping("api/join/v1")
    public Long join1(@RequestBody CreateMemberRequest request) {
        Member member = new Member(request.getEmail(), request.getNickname(), request.getPassword());
        return memberService.join1(member);
    }

    @PostMapping("api/join/v2")
    public Long join2(@RequestBody @Valid CreateMemberRequest request) {
        MemberDTO member = new MemberDTO(request.getEmail(), request.getNickname(), request.getPassword());
        return memberService.join2(member);
    }

    @Data
    static class CreateMemberRequest {

        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "유효한 이메일 주소 형식이 아닙니다.")
        private String email;  // 이메일

        @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,}$", message = "닉네임은 한글, 영문, 숫자로 이루어져야 하며 2글자 이상이어야 합니다.")
        private String nickname;    // 닉네임

        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{8,}$", message = "비밀번호는 영문 소문자 또는 대문자와 숫자를 포함하여 8글자 이상이어야 합니다.")
        private String password;    // 비밀번호
    }

    /**
     * 로그인 v1 : 시큐리티 x 디비에서 바로 조회
     */
    @PostMapping("/api/login/v1")
    public Long login1(@RequestBody LoginData loginData) {
        return memberRepository.findByEmailAndPassword(loginData.getEmail(), loginData.getPassword())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND))
                .getId();
    }

    @Data
    public static class LoginData {
        private String email;
        private String password;
    }

    /**
     * 로그인 v2 : 시큐리티 o
     */
    @GetMapping("/api/login/v2")
    public UserDetails login2(@RequestBody LoginData loginData) {
        return memberService.login2(loginData.getEmail(), loginData.getPassword());
    }

    @GetMapping("/api/login/v3")
    public boolean login3(@RequestBody LoginData loginData) {
        return memberService.login3(loginData.getEmail(), loginData.getPassword());
    }

    // Authentication

}
