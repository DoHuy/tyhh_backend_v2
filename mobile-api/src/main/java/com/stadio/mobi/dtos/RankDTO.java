package com.stadio.mobi.dtos;

import lombok.Data;

import java.util.List;

/**
 * Created by Andy on 03/02/2018.
 */
@Data
public class RankDTO
{
    List<UserRankDTO> weeklyRank;
    List<UserRankDTO> monthlyRank;
}
