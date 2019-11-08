package com.stadio.mobi.service.impl;

import com.hoc68.users.documents.User;
import com.stadio.common.utils.ResponseCode;
import com.stadio.mobi.RabbitProducer;
import com.stadio.mobi.dtos.comment.CommentFormDTO;
import com.stadio.mobi.dtos.examOnline.ExamCommentDTO;
import com.stadio.mobi.response.ResponseResult;
import com.stadio.mobi.service.ICommentService;
import com.stadio.model.documents.Comment;
import com.stadio.model.documents.Topic;
import com.stadio.model.dtos.mobility.comment.CommentItemDTO;
import com.stadio.model.enu.CommentType;
import com.stadio.model.enu.MessageLabel;
import com.stadio.model.repository.main.CommentRepository;
import com.stadio.model.repository.main.TopicRepository;
import com.stadio.model.repository.user.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentService extends BaseService implements ICommentService {


    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TopicRepository topicRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private Logger logger = LogManager.getLogger(CommentService.class);

    @Autowired
    RabbitProducer rabbitProducer;

    @Override
    public ResponseResult getListCommentFromExamOnline(String id, int page, int limit) {

        PageRequest request = new PageRequest(page - 1, limit, new Sort(Sort.Direction.DESC, "created_date"));

        List<Comment> commentList = commentRepository.findByObjectIdAndType(id, CommentType.EXAM_ONLINE, request).getContent();

        List<ExamCommentDTO> examCommentDTOList = new ArrayList<>();

        for (Comment comment: commentList) {
            ExamCommentDTO examCommentDTO = new ExamCommentDTO();

            examCommentDTO.setId(comment.getId());
            examCommentDTO.setMessage(comment.getMessage());
            examCommentDTO.setSendTime(comment.getSendTime());
            if (comment.getMessageLabel() != null) {
                examCommentDTO.setMessageLabel(comment.getMessageLabel().toString());
            } else {
                examCommentDTO.setMessageLabel(MessageLabel.PLAIN_TEXT.toString());
            }

            if (comment.getUserId() == null) continue;

            User user = userRepository.findOne(comment.getUserId());
            if (user == null) continue;

            if (StringUtils.isNotBlank( user.getFullName())) {
                examCommentDTO.setName(user.getFullName());
            } else {
                examCommentDTO.setName(user.getUsername());
            }

            examCommentDTO.setAvatar(user.getAvatar());

            examCommentDTOList.add(examCommentDTO);
        }

        return ResponseResult.newSuccessInstance(examCommentDTOList);
    }

    @Override
    public ResponseResult processSendingMessage(String id, String msg) {
//        User currentUser = getUserRequesting();
//        ChatMessage chatMessage = new ChatMessage(msg, currentUser.getId());
//        chatMessage.setAvatar(currentUser.getAvatar());
//        if (StringUtils.isNotBlank(currentUser.getFullName())) {
//            chatMessage.setName(currentUser.getFullName());
//        } else {
//            chatMessage.setName(currentUser.getUsername());
//        }
//        chatMessage.setObjectId(id);
//        rabbitProducer.sendMessage("examOnline", chatMessage);
        return ResponseResult.newSuccessInstance(null);
    }

}
