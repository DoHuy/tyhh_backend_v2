package com.stadio.mobi.service.impl;

        import com.stadio.mobi.dtos.RankDTO;
        import com.stadio.mobi.dtos.UserRankDTO;
        import com.stadio.mobi.response.ResponseResult;
        import com.stadio.mobi.service.IRankService;
        import com.stadio.model.documents.*;
        import com.hoc68.users.documents.User;
        import com.stadio.model.enu.RankType;
        import com.stadio.model.repository.main.RankRepository;
        import com.stadio.model.repository.main.UserRankRepository;
        import com.stadio.model.repository.user.UserRepository;
        import org.apache.logging.log4j.LogManager;
        import org.apache.logging.log4j.Logger;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.data.domain.PageRequest;
        import org.springframework.data.domain.Sort;
        import org.springframework.stereotype.Service;

        import java.util.ArrayList;
        import java.util.List;

/**
 * Created by Andy on 03/02/2018.
 */
@Service
public class RankService extends BaseService implements IRankService {

    private Logger logger = LogManager.getLogger(RankService.class);

    @Autowired
    RankRepository rankRepository;

    @Autowired
    UserRankRepository userRankRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public ResponseResult processBuildUserRank() {
        RankDTO rankDTO = new RankDTO();

        rankDTO.setMonthlyRank(getListUserRankWithType(RankType.MONTHLY));
        rankDTO.setWeeklyRank(getListUserRankWithType(RankType.DAILY));

        return ResponseResult.newSuccessInstance(rankDTO);
    }

    @Override
    public List<UserRankDTO> getListUserRank(String rankId) {
        List<UserRankDTO> userRankDTOList = new ArrayList<>();


        logger.info("Found rank:" + rankId);

        PageRequest request = new PageRequest(0, 100, new Sort(Sort.Direction.DESC, "point"));
        List<UserRank> userRankList = userRankRepository.findByRankIdOrderByPointDesc(rankId, request).getContent();

        int position = 1;

        User currentUser = getUserRequesting();

        for (UserRank userRank : userRankList) {
            User user = userRepository.findOne(userRank.getUserId());
            if (user != null) {
                if (position <= 20 || (currentUser != null && currentUser.getId().equals(user.getId()))) {
                    UserRankDTO userRankDTO = new UserRankDTO();
                    userRankDTO.setId(user.getId());
                    userRankDTO.setFullName(user.getFullName());
                    userRankDTO.setAvatar(user.getAvatar());
                    userRankDTO.setPoint(userRank.getPoint());
                    userRankDTO.setPosition(position);
                    userRankDTO.setUsername(user.getUsername());
                    userRankDTOList.add(userRankDTO);
                }
                position++;
            }
        }

        return userRankDTOList;
    }


    public List<UserRankDTO> getListUserRankWithType(RankType type) {
        Rank rank = rankRepository.findTopByRankTypeOrderByCreatedDateDesc(type);

        if (rank != null) {
            return getListUserRank(rank.getId());
        }

        return new ArrayList<>();
    }
}
