package api.joayo.board.posting;

import api.joayo.board.State;
import api.joayo.exception.CannotRemoveException;
import api.joayo.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private State state;

//    private Posting(){} // JPA사용시 private 지정 불가!! 엔티티 생성시 기본생성자를 필요로한다
    public static Posting create(Member writer, String title, String contents) {
        Posting posting = new Posting();
        posting.writer = writer;
        posting.title = title;
        posting.contents = contents;

        posting.postingDate = LocalDateTime.now();
        posting.state = State.UPLOADED;

        return posting;
    }

    //== 비즈니스 로직 ==/

    /**
     * 좋아요 증가 ---- 좋아요는 한사람당 한 게시물에 한번만 누를수 있다.
     */
    public void addLike() {
        this.likeCount++;
    }

    /**
     * 좋아요 감소 ---- 취소는 이전에 좋아요를 눌렀을 경우에만 할 수 있다.
     */
    public void removeLike() {
        if(this.likeCount == 0) {
            throw new CannotRemoveException("좋아요 수는 0보다 작을 수 없습니다.");
        }
        this.likeCount--;
    }

    /**
     * 댓글수 1 증가
     */
    public void addComment() { this.commentCount++;}

    /**
     * 댓글수 1 감소
     */
    public void removeComment() {
        if(this.commentCount == 0) {
            throw new CannotRemoveException("댓글 수는 0보다 작을 수 없습니다.");
        }
        this.commentCount--;
    }

    /**
     * 조회수가 1 증가합니다
     */
    public void addView() {
        this.viewCount++;
    }

    /**
     * 글삭제
     */
    public void delete() {
        this.state = State.DELETED;
    }

    /**
     * 글수정 - 제목, 내용, 날짜, state
     */
    public void modify(String title, String contents) {
        this.title = title;
        this.contents = contents;

        this.postingDate = LocalDateTime.now();
        this.state = State.MODIFIED;
    }


}
