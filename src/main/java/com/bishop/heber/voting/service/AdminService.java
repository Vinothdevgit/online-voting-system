package com.bishop.heber.voting.service;

import com.bishop.heber.voting.dto.VoteResultDto;
import java.util.List;

public interface AdminService {
    List<VoteResultDto> getVoteSummary();
}
