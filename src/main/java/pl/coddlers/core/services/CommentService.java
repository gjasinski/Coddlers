package pl.coddlers.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coddlers.core.exceptions.NotSuchCommentTypeSpecifiedException;
import pl.coddlers.core.models.dto.*;
import pl.coddlers.core.models.entity.Comment;
import pl.coddlers.core.models.entity.CommentTypeEnum;
import pl.coddlers.core.models.entity.Submission;
import pl.coddlers.core.repositories.CommentRepository;

import java.sql.Timestamp;

import static pl.coddlers.core.models.entity.CommentTypeEnum.*;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserDetailsServiceImpl userDetailsService) {
        this.commentRepository = commentRepository;
        this.userDetailsService = userDetailsService;
    }

    public Comment createComment(Commentable commentable, Submission submission) throws NotSuchCommentTypeSpecifiedException {
        CommentTypeEnum commentTypeEnum = null;
        if (commentable instanceof SubmissionCommentDto) {
            commentTypeEnum = GENERAL_COMMENT;
        } else if (commentable instanceof SubmissionReopenDto) {
            commentTypeEnum = REOPEN_REASON;
        } else if (commentable instanceof SubmissionGradeDto) {
            commentTypeEnum = GRADE_COMMENT;
        } else if (commentable instanceof SubmissionRequestChangesDto) {
            commentTypeEnum = REQUEST_CHANGES_REASON;
        }

        if (commentTypeEnum == null) {
            throw new NotSuchCommentTypeSpecifiedException(commentable.getClass().getName());
        }

        return createCommentEntity(commentable, commentTypeEnum, submission);
    }

    private Comment createCommentEntity(Commentable commentable, CommentTypeEnum commentTypeEnum, Submission submission) {
        if (commentable.getComment() != null && !commentable.getComment().equals("")) {
            Comment comment = new Comment();
            comment.setText(commentable.getComment());
            comment.setCommentType(commentTypeEnum.getCommentType());
            comment.setCreationTime(new Timestamp(System.currentTimeMillis()));
            comment.setAuthor(userDetailsService.getCurrentUserEntity());
            comment.setSubmission(submission);

            return commentRepository.save(comment);
        }

        return null;
    }
}
