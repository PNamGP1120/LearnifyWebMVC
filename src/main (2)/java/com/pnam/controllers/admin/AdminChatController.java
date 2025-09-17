package com.pnam.controllers.admin;

import com.pnam.pojo.ChatThread;
import com.pnam.pojo.ChatMessage;
import com.pnam.services.ChatThreadService;
import com.pnam.services.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/chat")
public class AdminChatController {

    @Autowired
    private ChatThreadService threadService;

    @Autowired
    private ChatMessageService messageService;

    // Danh sách tất cả các thread (toàn bộ hệ thống)
    @GetMapping
    public String listThreads(Model model) {
        // TODO: nếu có getAll() thì dùng, tạm để trống
        List<ChatThread> threads = List.of();
        model.addAttribute("threads", threads);
        return "admin/chat/threads";
    }

    // Chi tiết một thread
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        ChatThread thread = threadService.getThreadById(id);
        List<ChatMessage> messages = messageService.getMessagesByThread(id);

        model.addAttribute("thread", thread);
        model.addAttribute("messages", messages);
        return "admin/chat/detail";
    }

    // Xoá thread (nếu admin muốn dọn dẹp)
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        threadService.deleteThread(id);
        return "redirect:/admin/chat";
    }
}
