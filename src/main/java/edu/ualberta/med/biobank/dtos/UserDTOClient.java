package edu.ualberta.med.biobank.dtos;

import java.util.Collection;

/**
 * The DTO returned to external clients
 *
 * Filters out information we do not wish to share, like memberships and the encoded password.
 */
public record UserDTOClient(
    Integer userId,
    String fullname,
    String username,
    Boolean isGlobalAdmin,
    String apiKey,
    Collection<GroupDTO> groups
) {
    public static UserDTOClient fromUserDTO(UserDTO userDTO) {
        return new UserDTOClient(
            userDTO.userId(),
            userDTO.fullName(),
            userDTO.username(),
            userDTO.isGlobalAdmin(),
            userDTO.apiKey(),
            userDTO.groups().values()
        );
    }
}
