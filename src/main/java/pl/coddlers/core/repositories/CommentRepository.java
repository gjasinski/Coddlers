package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.core.models.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
