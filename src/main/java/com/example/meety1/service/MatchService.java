package com.example.meety1.service;

import com.example.meety1.dto.PendingMatchDto;
import com.example.meety1.dto.UserMatchDto;
import com.example.meety1.entity.Match;

import java.util.List;

public interface MatchService {
    // TODO: After implementing security change id parameter to no param function and call SecurityContext
    // TODO: to get user_id
    List<UserMatchDto> getUserMatches(Long id);

    List<PendingMatchDto> getPendingMatches(Long id);

    Match acceptMatchByMatchKey(Long requesterId, Long responderId);

    Match sendMatchInvite(Long requesterId, Long responderId);

    void declineMatchInvite(Long requesterId, Long responderId);
}