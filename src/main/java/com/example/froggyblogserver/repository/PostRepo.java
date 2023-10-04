package com.example.froggyblogserver.repository;

import com.example.froggyblogserver.dto.request.PostSearchRequest;
import com.example.froggyblogserver.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepo extends JpaRepository<PostEntity, String> {
    @Override
    @Query(value = "FROM PostEntity p WHERE p.id = :s AND p.isDelete=0")
    Optional<PostEntity> findById(String s);

    @Query(value = "FROM PostEntity p WHERE ((:#{#req.keyword} IS NULL OR p.title LIKE %:#{#req.keyword}%) " +
            "OR (:#{#req.keyword} IS NULL OR p.content LIKE %:#{#req.keyword}%)) AND p.isDelete = 0 AND p.status = 'PUBLISHED' ")
    Page<PostEntity> search(@Param("req") PostSearchRequest req, Pageable pageable);

    @Query(value = "SELECT p FROM PostEntity p LEFT JOIN PostTopicEntity pt ON p.id = pt.postId AND pt.isDelete = 0 WHERE pt.topicId = :topicId AND p.isDelete = 0 AND p.status = 'PUBLISHED'")
    Page<PostEntity> searchByTopicId(String topicId, Pageable pageable);

    @Query(value = "SELECT p FROM PostEntity p LEFT JOIN UserPostEntity up ON p.id = up.postId AND up.isDelete = 0 WHERE up.userId = :userId AND p.isDelete = 0 AND p.status = 'PUBLISHED' ORDER BY up.createDate")
    Page<PostEntity> searchByUserId(String userId,Pageable pageable);

    @Query(value = "WITH temp AS ( " +
            " WITH likesCount AS( " +
            " SELECT l.post_id,count(l.post_id) as totalLikes FROM likes l " +
            " WHERE ((l.create_date BETWEEN  :startTime AND :endTime) OR (l.update_date BETWEEN  :startTime AND :endTime) ) " +
            " AND l.is_delete = 0 " +
            " GROUP BY l.post_id " +
            " ORDER BY totalLikes desc " +
            " ) SELECT c.totalLikes, row_number() over (order by c.totalLikes desc ) as ranks,p.* FROM likesCount c JOIN posts p ON p.id = c.post_id " +
            " )SELECT t.id,t.content,t.update_date,t.author_id,t.create_date,t.credit,t.publish_date,t.thumbnail,t.title,t.status,t.is_delete,t.user_id,t.create_id,t.update_id,t.raw,t.time_read FROM temp t WHERE t.ranks < 6 LIMIT 5 ",nativeQuery = true)
    List<PostEntity> trendingPost(LocalDateTime startTime,LocalDateTime endTime);
}
