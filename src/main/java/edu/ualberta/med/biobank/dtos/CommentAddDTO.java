package edu.ualberta.med.biobank.dtos;

import jakarta.persistence.Tuple;
import jakarta.validation.constraints.NotBlank;

public record CommentAddDTO(
    @NotBlank(message = "message cannot be blank")
    String message
) {
    public static CommentAddDTO fromTuple(Tuple data) {
        return new CommentAddDTO(
            data.get("commentMessage", String.class)
        );
    }
}
