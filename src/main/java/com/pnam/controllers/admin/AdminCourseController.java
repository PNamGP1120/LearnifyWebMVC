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
import com.pnam.validator.CourseSectionValidator;
import com.pnam.validator.LessonValidator;
import jakarta.validation.Valid;
import java.util.Date;
import java.util.HashMap;
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
    @Autowired
    private CourseSectionValidator sectionValidator;
    @Autowired
    private LessonValidator lessonValidator;

    @InitBinder("course")
    protected void initCourseBinder(WebDataBinder binder) {
        binder.setValidator(courseValidator);
    }

    @InitBinder("section")
    protected void initSectionBinder(WebDataBinder binder) {
        binder.setValidator(sectionValidator);
    }

    @InitBinder("lesson")
    protected void initLessonBinder(WebDataBinder binder) {
        binder.setValidator(lessonValidator);
    }

    // ==================== Danh sách Course ====================
    @GetMapping
    public String list(@RequestParam(required = false) Map<String, String> params, Model model) {
        if (params == null) {
            params = Map.of();
        }

        List<Course> courses = courseService.getCourses(params);
        long count = courseService.countCourses(params);

        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = params.containsKey("pageSize") ? Integer.parseInt(params.get("pageSize")) : 10;

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

        // set ngày mặc định
        c.setCreatedAt(new Date());
        c.setUpdatedAt(new Date());

        if (c.getCoverFile() != null && !c.getCoverFile().isEmpty()) {
            String url = courseService.uploadCoverImage(c.getCoverFile());
            c.setCoverImage(url);
        }

        courseService.saveCourse(c);
        return "redirect:/admin/courses";
    }

    // ==================== Sửa Course ====================
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id,
            @ModelAttribute("course") @Valid Course c,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getCategories(Map.of()));
            model.addAttribute("instructors", instructorService.getProfiles(Map.of()));
            return "admin/course/form";
        }

        Course dbCourse = courseService.getCourseById(id);
        if (dbCourse == null) {
            return "redirect:/admin/courses";
        }

        c.setId(id);
        c.setCreatedAt(dbCourse.getCreatedAt());   // giữ nguyên createdAt cũ
        c.setUpdatedAt(new Date());               // cập nhật updatedAt mới

        if (c.getCoverFile() != null && !c.getCoverFile().isEmpty()) {
            String url = courseService.uploadCoverImage(c.getCoverFile());
            c.setCoverImage(url);
        } else {
            c.setCoverImage(dbCourse.getCoverImage()); // giữ ảnh cũ nếu không upload mới
        }

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
    @GetMapping("/{courseId}/sections")
    public String sectionList(@PathVariable("courseId") Long courseId,
            @RequestParam(required = false) Map<String, String> params,
            Model model) {
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return "redirect:/admin/courses";
        }

        // thêm filter courseId vào params
        if (params == null || params.isEmpty()) {
            params = Map.of("courseId", String.valueOf(courseId));
        } else {
            params = new HashMap<>(params);
            params.put("courseId", String.valueOf(courseId));
        }

        List<CourseSection> sections = sectionService.getSections(params);
        long count = sectionService.countSections(params);

        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = params.containsKey("pageSize") ? Integer.parseInt(params.get("pageSize")) : 10;

        model.addAttribute("course", course);
        model.addAttribute("sections", sections);
        model.addAttribute("count", count);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("param", params);

        return "admin/course/section-list";
    }

    // ==================== Thêm Section ====================
    @GetMapping("/{courseId}/sections/add")
    public String addSectionForm(@PathVariable("courseId") Long courseId, Model model) {
        model.addAttribute("section", new CourseSection());
        model.addAttribute("courseId", courseId);
        return "admin/course/section-form";
    }

    @PostMapping("/{courseId}/sections/add")
    public String addSection(@PathVariable("courseId") Long courseId,
            @ModelAttribute("section") @Valid CourseSection s,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("courseId", courseId);
            return "admin/course/section-form";
        }
        Course course = courseService.getCourseById(courseId);
        s.setCourseId(course);
        sectionService.saveSection(s);
        return "redirect:/admin/courses/" + courseId + "/sections";
    }

    // ==================== Danh sách Lesson theo Section ====================
    @GetMapping("/sections/{sectionId}/lessons")
    public String lessonList(@PathVariable("sectionId") Long sectionId,
            @RequestParam(required = false) Map<String, String> params,
            Model model) {
        CourseSection section = sectionService.getSectionById(sectionId);
        if (section == null) {
            return "redirect:/admin/courses";
        }

        if (params == null || params.isEmpty()) {
            params = Map.of("sectionId", String.valueOf(sectionId));
        } else {
            params = new HashMap<>(params);
            params.put("sectionId", String.valueOf(sectionId));
        }

        List<Lesson> lessons = lessonService.getLessons(params);
        long count = lessonService.countLessons(params);

        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = params.containsKey("pageSize") ? Integer.parseInt(params.get("pageSize")) : 10;

        model.addAttribute("section", section);
        model.addAttribute("lessons", lessons);
        model.addAttribute("count", count);
        model.addAttribute("page", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("param", params);

        return "admin/course/lesson-list";
    }

    // ==================== Thêm Lesson ====================
    @GetMapping("/sections/{sectionId}/lessons/add")
    public String addLessonForm(@PathVariable("sectionId") Long sectionId, Model model) {
        model.addAttribute("lesson", new Lesson());
        model.addAttribute("sectionId", sectionId);
        return "admin/course/lesson-form";
    }

    @PostMapping("/sections/{sectionId}/lessons/add")
    public String addLesson(@PathVariable("sectionId") Long sectionId,
            @ModelAttribute("lesson") @Valid Lesson l,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("sectionId", sectionId);
            return "admin/course/lesson-form";
        }
        CourseSection section = sectionService.getSectionById(sectionId);
        l.setSectionId(section);
        lessonService.saveLesson(l);
        return "redirect:/admin/courses/sections/" + sectionId + "/lessons";
    }
}
