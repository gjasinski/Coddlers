package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coddlers.core.models.entity.CourseEdition;
import pl.coddlers.core.models.entity.Submission;
import pl.coddlers.core.models.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Collection<Submission> findByTaskId(Long taskId);

    Optional<Submission> findByBranchNameAndStudentLessonRepository_RepositoryUrl(String branchName, String repositoryUtl);


    int countAllByUserAndCourseEditionAndSubmissionStatusTypeName(User user,
                                                                  CourseEdition courseEdition,
                                                                  String submissionStatusTypeName);

    int countAllByUserAndCourseEdition(User user, CourseEdition courseEdition);

}