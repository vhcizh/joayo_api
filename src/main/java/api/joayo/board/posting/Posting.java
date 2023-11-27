package api.joayo.board.posting;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Posting {

    int number;         // 글번호
    String writer;      // 작성자
    String title;       // 제목
    String contents;    // 내용
    int viewCount;      // 조회수
    int voteCount;      // 좋아요 수
    int commentCount;   // 댓글 수
    String postingDate;   // 작성일


}
