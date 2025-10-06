package com.kampuskor.restservice.features.User.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kampuskor.restservice.features.User.User;

public record UsersResponse(
    Integer page,

    @JsonProperty("number_of_users")
    Integer numberOfUsers,

    @JsonProperty("total_pages")
    Integer totalPages,
    
    @JsonProperty("total_number_of_users")
    Long totalNumberOfUsers,
    
    List<User> data
) {
}
