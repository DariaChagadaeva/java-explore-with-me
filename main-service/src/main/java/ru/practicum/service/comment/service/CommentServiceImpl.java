package ru.practicum.service.comment.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.comment.dto.CommentDto;
import ru.practicum.service.comment.dto.NewCommentDto;
import ru.practicum.service.comment.mapper.CommentMapper;
import ru.practicum.service.comment.model.Comment;
import ru.practicum.service.comment.repository.CommentRepository;
import ru.practicum.service.event.enums.EventState;
import ru.practicum.service.event.model.Event;
import ru.practicum.service.event.service.EventService;
import ru.practicum.service.exception.ConflictException;
import ru.practicum.service.exception.EntityNotFoundException;
import ru.practicum.service.user.model.User;
import ru.practicum.service.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentServiceImpl implements CommentService {
    final CommentRepository commentRepository;
    final CommentMapper commentMapper;
    final UserService userService;
    final EventService eventService;

    @Override
    @Transactional
    public CommentDto updateCommentByAdmin(Long commentId, NewCommentDto newCommentDto) {
        log.info("Method updateCommentByAdmin invoke");
        Comment comment = findComment(commentId);
        comment.setText(newCommentDto.getText());
        CommentDto updatedComment = commentMapper.fromModelToDto(commentRepository.save(comment));
        log.info("Comment has been updated {}", updatedComment);
        return updatedComment;
    }

    @Override
    @Transactional
    public void deleteCommentByAdmin(Long commentId) {
        log.info("Method deleteCommentByAdmin invoke");
        commentRepository.delete(commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException("Comment not found")));
        log.info("Category {} deleted", commentId);
    }

    @Override
    public CommentDto getCommentByIdPublic(Long commentId) {
        CommentDto commentDto = commentMapper.fromModelToDto(findComment(commentId));
        log.info("Method getCommentByIdPublic invoke");
        return commentDto;
    }

    @Override
    public List<CommentDto> getCommentsForEventPublic(Long eventId, Integer from, Integer size) {
        log.info("Method getCommentsForEventPublic invoke");
        eventService.getEvent(eventId);
        Pageable page = PageRequest.of(from / size, size);
        List<Comment> commentsListForEvent = commentRepository.findAllByEventId(eventId, page);
        log.info("Get comments for event id = {}", eventId);
        return commentsListForEvent.stream().map(commentMapper::fromModelToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addCommentPrivate(Long userId, Long eventId, NewCommentDto newCommentDto) {
        log.info("Method addCommentPrivate invoke");
        User user = userService.findUserById(userId);
        Event event = eventService.getEvent(eventId);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event is not published");
        }
        CommentDto newComment = commentMapper.fromModelToDto(commentRepository.save(commentMapper.fromDtoToModel(newCommentDto, user, event)));
        log.info("New comment added {}", newComment);
        return newComment;
    }

    @Override
    @Transactional
    public CommentDto updateCommentPrivate(Long userId, Long commentId, NewCommentDto newCommentDto) {
        log.info("Method updateCommentPrivate invoke");
        userService.findUserById(userId);
        validateUserIsTheAuthor(userId, commentId);
        Comment comment = findComment(commentId);
        comment.setText(newCommentDto.getText());
        CommentDto updatedComment = commentMapper.fromModelToDto(commentRepository.save(comment));
        log.info("Comment has been updated {}", updatedComment);
        return updatedComment;
    }

    @Override
    public List<CommentDto> getUserCommentsPrivate(Long userId, Long eventId, Integer from, Integer size) {
        log.info("Method getUserCommentsPrivate invoke");
        userService.findUserById(userId);
        List<Comment> comments;
        Pageable page = PageRequest.of(from / size, size);
        if (eventId != null) {
            eventService.getEvent(eventId);
            comments = commentRepository.findAllByAuthorIdAndEventId(userId, eventId, page);
        } else {
            comments = commentRepository.findAllByAuthorId(userId, page);
        }
        return comments.stream().map(commentMapper::fromModelToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCommentByUser(Long userId, Long commentId) {
        log.info("Method deleteCommentByUser invoke");
        validateUserIsTheAuthor(userId, commentId);
        commentRepository.delete(findComment(commentId));
        log.info("Category {} deleted", commentId);
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new EntityNotFoundException("Comment not found"));
    }

    private void validateUserIsTheAuthor(Long userId, Long commentId) {
        userService.findUserById(userId);
        Comment comment = findComment(commentId);
        if (!userId.equals(comment.getAuthor().getId())) {
            throw new ConflictException("User is not the author");
        }
    }
}
