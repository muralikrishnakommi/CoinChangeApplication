package com.coinchange.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class APIResponse<T> {
    private String status;
    private String message;
    private T results;
    public APIResponse(String status, String message){
        this.status = status;
        this.message= message;
    }
}
