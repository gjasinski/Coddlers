package pl.coddlers.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.coddlers.core.models.entity.Submission;

import java.util.Collection;
import java.util.Optional;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Collection<Submission> findByTaskId(Long taskId);

    Optional<Submission> findByBranchNameAndStudentLessonRepository_RepositoryUrl(String branchName, String repositoryUtl);

    @Query(value = "SELECT count(*) " +
            "FROM submission " +
            "WHERE submission.user_id = :userId " +
            "AND submission.course_edition_id = :courseEditionId " +
            "AND submission.submission_status_type_name = :submissionStatusTypeName", nativeQuery = true)
    int countAllByUserAndCourseEditionAndSubmissionStatusTypeName(@Param("userId") Long userId,
                                                                  @Param("courseEditionId") Long courseEditionId,
                                                                  @Param("submissionStatusTypeName") String submissionStatusTypeName);

    @Query(value = "SELECT count(*) " +
            "FROM submission " +
            "WHERE submission.user_id = :userId " +
            "AND submission.course_edition_id = :courseEditionId ", nativeQuery = true)
    int countAllByUserAndCourseEdition(@Param("userId") Long userId, @Param("courseEditionId") Long courseEditionId);

}