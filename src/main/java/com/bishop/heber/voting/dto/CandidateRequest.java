package com.bishop.heber.voting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateRequest {
    private String name;
    private String description;
    private String symbol;
    private List<String> promises;
}
