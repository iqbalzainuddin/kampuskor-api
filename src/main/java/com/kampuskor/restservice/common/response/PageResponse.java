package com.kampuskor.restservice.common.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PageResponse<T>(
    Integer page,
    
    @JsonProperty("items_count")
    Integer itemsCount,

    @JsonProperty("total_pages")
    Integer totalPages,

    @JsonProperty("items_total")
    Long itemsTotal,

    List<T> data
) {
    
}
