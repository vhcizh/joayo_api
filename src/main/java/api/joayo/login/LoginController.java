package api.joayo.login;

import api.joayo.member.Member;
import api.joayo.member.MemberRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final MemberRepository memberRepository;
    private final LoginService loginService;

    /**
     * 회원가입 v1 : 시큐리티 x db에서 바로
     */
    @PostMapping("api/join/v1")
    public Long join1(@RequestBody CreateMemberRequest request) {
        Member member = Member.create(request.getEmail(), request.getNickname(), request.getPassword());
        return loginService.join1(member);
    }

    @PostMapping("api/join/v2")
    public Long join2(@RequestBody CreateMemberRequest request) {
        Member member = Member.create(request.getEmail(), request.getNickname(), request.getPassword());
        return loginService.join2(member);
    }

    @Data
    static class CreateMemberRequest {
        private String email;  // 이메일
        private String nickname;    // 닉네임
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
        return loginService.login(loginData.getEmail(), loginData.getPassword());
    }

    // Authentication

}
