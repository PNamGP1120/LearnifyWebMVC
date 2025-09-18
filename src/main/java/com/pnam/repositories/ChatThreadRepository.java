package com.pnam.repositories;

import com.pnam.pojo.ChatThread;
import java.util.List;

public interface ChatThreadRepository {

    ChatThread findById(Long id);

    List<ChatThread> findByCourse(Long courseId);

    List<ChatThread> findByUsers(Long userAId, Long userBId);

    ChatThread save(ChatThread ct);

    void delete(Long id);
}
