package edu.ualberta.med.biobank.matchers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.compose;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.exparity.hamcrest.date.DateMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import edu.ualberta.med.biobank.domain.Comment;
import edu.ualberta.med.biobank.dtos.CommentDTO;

public class CommentMatcher {

    private CommentMatcher() {
        throw new AssertionError();
    }

    public static Matcher<CommentDTO> matches(Comment expected) {
        return compose("a comment with", hasFeature("id", CommentDTO::id, equalTo(expected.getId())))
            .and(hasFeature("message", CommentDTO::message, equalTo(expected.getMessage())))
            .and(
                hasFeature(
                    "createdAt",
                    CommentDTO::createdAt,
                    DateMatchers.within(1, ChronoUnit.SECONDS, expected.getCreatedAt())
                )
            )
            .and(hasFeature("user", CommentDTO::user, equalTo(expected.getUser().getLogin())));
    }

    // see https://stackoverflow.com/a/27590279
    public static Matcher<Iterable<? extends CommentDTO>> containsAll(Set<Comment> items) {
        List<Matcher<? super CommentDTO>> matchers = new ArrayList<Matcher<? super CommentDTO>>();
        for (Comment item : items) {
            matchers.add(matches(item));
        }
        return Matchers.contains(matchers);
    }
}
