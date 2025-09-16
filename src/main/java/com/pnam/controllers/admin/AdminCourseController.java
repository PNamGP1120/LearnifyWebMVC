package com.pnam.controllers.admin;

import com.pnam.pojo.Course;
import com.pnam.pojo.CourseSection;
import com.pnam.pojo.Lesson;
import com.pnam.services.CategoryService;
import com.pnam.services.CourseService;
import com.pnam.services.CourseSectionService;
import com.pnam.services.InstructorProfileService;
import com.pnam.services.LessonService;
import com.pnam.validator.CourseValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/courses")
public class AdminCourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private InstructorProfileService instructorService;

    @Autowired
    private CourseSectionService sectionService;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private CourseValidator courseValidator;

    @InitBinder("course")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(courseValidator);
    }

    // ==================== Danh sách Course ====================
    @GetMapping
    public String list(@RequestParam(required = false) Map<String, String> params, Model model) {
        if (params == null) {
            params = Map.of();
        }

        var courses = courseService.getCourses(params);
        long count = courseService.countCourses(params);

        String pageStr = params.getOrDefault("page", "1");
        int page = (pageStr != null && pageStr.matches("\\d+")) ? Integer.parseInt(pageStr) : 1;

        String pageSizeStr = params.getOrDefault("pageSize", "10");
        int pageSize = (pageSizeStr != null && pageSizeStr.matches("\\d+")) ? Integer.parseInt(pageSizeStr) : 10;

        model.addAttribute("courses", courses);
        model.addAttribute("count", count);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("param", params);

        model.addAttribute("categories", categoryService.getCategories(Map.of()));
        model.addAttribute("instructors", instructorService.getProfiles(Map.of()));

        return "admin/course/list";
    }

    // ==================== Thêm Course ====================
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("categories", categoryService.getCategories(Map.of()));
        model.addAttribute("instructors", instructorService.getProfiles(Map.of()));
        return "admin/course/form";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("course") @Valid Course c,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getCategories(Map.of()));
            model.addAttribute("instructors", instructorService.getProfiles(Map.of()));
            return "admin/course/form";
        }
        courseService.saveCourse(c);
        return "redirect:/admin/courses";
    }

    // ==================== Sửa Course ====================
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Course c = courseService.getCourseById(id);
        if (c == null) {
            return "redirect:/admin/courses";
        }
        model.addAttribute("course", c);
        model.addAttribute("categories", categoryService.getCategories(Map.of()));
        model.addAttribute("instructors", instructorService.getProfiles(Map.of()));
        return "admin/course/form";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id,
            @ModelAttribute("course") @Valid Course c,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getCategories(Map.of()));
            model.addAttribute("instructors", instructorService.getProfiles(Map.of()));
            return "admin/course/form";
        }
        c.setId(id);
        courseService.saveCourse(c);
        return "redirect:/admin/courses";
    }

    // ==================== Xoá Course ====================
    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        courseService.deleteCourse(id);
        return "redirect:/admin/courses";
    }

    // ==================== Danh sách Section theo Course ====================
// ==================== Danh sách Section theo Course ====================
    @GetMapping("/{courseId}/sections")
    public String sectionList(@PathVariable("courseId") Long courseId, Model model) {
        // Lấy course theo ID
        Course c = courseService.getCourseById(courseId);
        if (c == null) {
            return "redirect:/admin/courses";
        }

        // Lấy section theo courseId
        List<CourseSection> sections = sectionService.getSections(Map.of("courseId", courseId.toString()));

        model.addAttribute("course", c);
        model.addAttribute("sections", sections);
        return "admin/course/section-list";
    }

    // ==================== Danh sách Lesson theo Section ====================
    @GetMapping("/sections/{sectionId}/lessons")
    public String lessonList(@PathVariable("sectionId") Long sectionId, Model model) {
        CourseSection s = sectionService.getSectionById(sectionId);
        if (s == null) {
            return "redirect:/admin/courses";
        }

        List<Lesson> lessons = lessonService.getLessons(Map.of("sectionId", sectionId.toString()));

        model.addAttribute("section", s);
        model.addAttribute("lessons", lessons);
        return "admin/course/lesson-list";
    }
}
