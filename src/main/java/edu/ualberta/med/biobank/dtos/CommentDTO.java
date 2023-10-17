package edu.ualberta.med.biobank.dtos;

import java.util.Date;

public record CommentDTO(Integer id, String message, String user, Date createdAt) {
}
