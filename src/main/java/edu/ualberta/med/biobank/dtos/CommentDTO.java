package edu.ualberta.med.biobank.dtos;

import java.util.Date;
import jakarta.persistence.Tuple;

public record CommentDTO(Integer id, String message, String user, Date createdAt) {
    public static CommentDTO fromTuple(Tuple data) {
        return new CommentDTO(
            data.get("commentId", Number.class).intValue(),
            data.get("commentMessage", String.class),
            data.get("commentUser", Number.class).intValue(),
            data.get("createdAt", Date.class)
        )
    }
}
