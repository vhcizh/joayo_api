package api.joayo.board.posting;

import api.joayo.board.BoardState;
import api.joayo.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Posting {

    @Id @GeneratedValue
    @Column(name = "posting_id")
    private Long id;         // 글번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;  // 작성자

    private int likeCount;      // 좋아요 수
    private int commentCount;   // 댓글 수
    private String title;       // 제목
    private String contents;    // 내용
    private int viewCount;      // 조회수
    private LocalDateTime postingDate;   // 작성일

    @Enumerated(EnumType.STRING) // Enum 변경사항 발생시 꼬일 수 있으므로 String 타입으로 저장한다.
    @Column(name = "posting_state")
    private BoardState state;

}
