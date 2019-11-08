package com.stadio.task.component;

import com.stadio.model.enu.MessageLabel;
import com.stadio.task.model.ChatMessage;
import com.stadio.model.documents.Comment;
import com.stadio.model.enu.CommentType;
import com.stadio.model.repository.main.CommentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
    public class ExamOnlineCommentTask {

    private Logger logger = LogManager.getLogger(ExamOnlineCommentTask.class);

    @Autowired
    CommentRepository commentRepository;

    private ObjectMapper mapper = new ObjectMapper();

    @RabbitListener(queues = "#{queueExamOnlineComment.name}")
    public void receiveComment(byte[] payload) {

        try {
            String msg = new String(payload, "UTF-8");

            logger.info("ReceiveComment: " + msg);

            ChatMessage message = mapper.readValue(msg, ChatMessage.class);

            Comment comment = new Comment();

            comment.setType(CommentType.EXAM_ONLINE);
            comment.setMessage(message.getMessage());
            comment.setSendTime(message.getSendTime());
            comment.setUserId(message.getSenderId());
            comment.setObjectId(message.getObjectId());
//            if (message.getMessageLabel() == null || MessageLabel.valueOf(message.getMessageLabel()) == null) {
//                comment.setMessageLabel(MessageLabel.PLAIN_TEXT);
//            } else {
//                comment.setMessageLabel(MessageLabel.valueOf(message.getMessageLabel()));
//            }
            comment.setMessageLabel(message.getMessageLabel());

            commentRepository.save(comment);
        } catch (Exception e) {
            //logger.error("receiveComment exception: ", e);
        }

    }


}
