package com.pnam.services;

import com.pnam.pojo.ChatThread;
import java.util.List;

public interface ChatThreadService {

    ChatThread getThreadById(Long id);

    List<ChatThread> getThreadsByCourse(Long courseId);

    List<ChatThread> getThreadsByUsers(Long userAId, Long userBId);

    ChatThread createThread(ChatThread ct);

    ChatThread updateThread(ChatThread ct);

    void deleteThread(Long id);
}
