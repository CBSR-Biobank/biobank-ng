package edu.ualberta.med.biobank.dtos;

import java.util.Date;
import edu.ualberta.med.biobank.domain.Comment;
import jakarta.persistence.Tuple;

public record CommentDTO(Integer id, String message, String user, Date createdAt) {
    public static CommentDTO fromTuple(Tuple data) {
        return new CommentDTO(
            data.get("commentId", Number.class).intValue(),
            data.get("commentMessage", String.class),
            data.get("commentUser", String.class),
            data.get("commentCreatedAt", Date.class)
        );
    }

    public static CommentDTO fromComment(Comment comment) {
        return new CommentDTO(
            comment.getId(),
            comment.getMessage(),
            comment.getUser().getCsmUser().getLoginName(),
            comment.getCreatedAt()
        );
    }
}
