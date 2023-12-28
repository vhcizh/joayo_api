package api.joayo.member;

import lombok.Data;

@Data
public class MemberDTO {

    private final String email;
    private final String nickname;
    private final String password;

}
