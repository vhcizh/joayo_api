package api.joayo.member;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
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

}
