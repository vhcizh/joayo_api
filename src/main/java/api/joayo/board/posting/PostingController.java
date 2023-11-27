package api.joayo.board.posting;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostingController {

    @GetMapping
    public List<Posting> posts() {
        List<Posting> posting = new ArrayList<>();

        Posting post1 = new Posting();
        post1.setNumber(1);
        post1.setTitle("제목1");
        post1.setContents("내용1");
        post1.setWriter("작성자1");
        post1.setViewCount(0);
        post1.setVoteCount(0);
        post1.setCommentCount(0);
        post1.setPostingDate("2023-11-27");
        posting.add(post1);

        return posting;
    }


}
