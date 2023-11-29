package api.joayo.board.comment;

import api.joayo.board.posting.Posting;
import api.joayo.board.BoardState;
import api.joayo.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Comment {

    @Id @GeneratedValue
    @Column(name="comment_id")
    private Long id;    // 댓글 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posting_id")
    private Posting posting; // 글 id

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;  // 댓글 작성자 id

    private String contents;    // 댓글 내용

    private LocalDateTime commentDate; // 작성일

    @Enumerated(EnumType.STRING)
    @Column(name = "comment_state")
    private BoardState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent; // 원댓글

    @OneToMany(mappedBy = "parent")
    private List<Comment> child = new ArrayList<>();  // 리댓목록

    // 연관 관계 메서드
    public void addChildComment(Comment child) {
        this.child.add(child);
        child.setParent(this);
    }

}
