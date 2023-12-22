package api.joayo.member;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id; // 멤버 id

    @Column(unique = true, nullable = false)
    private String email;  // 이메일

    @Column(nullable = false)
    private String nickname;    // 닉네임

    @Column(nullable = false)
    private String password;    // 비밀번호

    @Enumerated(EnumType.STRING) // Enum 변경사항 발생시 꼬일 수 있으므로 String 타입으로 저장한다.
    @Column(nullable = false)
    private Authority authority;   // 권한 - 시큐리티 추가

    public static Member create(String email, String nickname, String password) {
        Member member = new Member();
        member.email = email;
        member.nickname = nickname;
        member.password = password;
        member.authority = Authority.ROLE_ANONYMOUS;
        return member;
    }

}
