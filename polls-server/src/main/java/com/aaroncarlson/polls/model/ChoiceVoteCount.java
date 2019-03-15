package com.aaroncarlson.polls.model;

import lombok.Getter;
import lombok.Setter;

/**
 * ChoiceVoteCount is used bby VoteRepository to return custom results from the query
 */
@Getter
@Setter
public class ChoiceVoteCount {

    private Long choiceId;
    private Long voteCount;

    public ChoiceVoteCount(Long choiceId, Long voteCount) {
        this.choiceId = choiceId;
        this.voteCount = voteCount;
    }

}
