package com.aleksandar.exafy.data.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationParametersDto {

    @Schema(defaultValue = "0", description = "The page number (starting from 0)")
    private Integer page;
    @Schema(defaultValue = "10", description = "The number of items per page")
    private Integer size;
    @Schema(defaultValue = "dueDate", description = "Other accepted value is priority")
    private String sortBy;
    @Schema(defaultValue = "asc", description = "The direction of the sorting (asc or desc)")
    private String sortDirection;
}
