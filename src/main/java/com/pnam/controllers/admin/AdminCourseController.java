package com.pnam.controllers.admin;

import com.pnam.pojo.Course;
import com.pnam.pojo.CourseSection;
import com.pnam.pojo.Lesson;
import com.pnam.services.CourseService;
import com.pnam.services.CourseSectionService;
import com.pnam.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/courses")
public class AdminCourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseSectionService sectionService;

    @Autowired
    private LessonService lessonService;

    // ================== COURSE ==================
    @GetMapping
    public String listCourses(Model model) {
        List<Course> courses = courseService.getCourses(null);
        model.addAttribute("courses", courses);
        return "admin/courses/list";
    }

    @GetMapping("/{id}")
    public String courseDetail(@PathVariable("id") Long id, Model model) {
        Course course = courseService.getCourseById(id);
        List<CourseSection> sections = sectionService.getSectionsByCourse(id);

        model.addAttribute("course", course);
        model.addAttribute("sections", sections);
        return "admin/courses/detail";
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable("id") Long id) {
        Course course = courseService.getCourseById(id);
        course.setStatus("PUBLISHED");
        courseService.updateCourse(course);
        return "redirect:/admin/courses";
    }

    @PostMapping("/{id}/block")
    public String block(@PathVariable("id") Long id) {
        Course course = courseService.getCourseById(id);
        course.setStatus("UNPUBLISHED");
        courseService.updateCourse(course);
        return "redirect:/admin/courses";
    }

    @GetMapping("/{id}/delete")
    public String deleteCourse(@PathVariable("id") Long id) {
        courseService.deleteCourse(id);
        return "redirect:/admin/courses";
    }

    // ================== SECTION ==================
    @GetMapping("/{courseId}/sections")
    public String listSections(@PathVariable("courseId") Long courseId, Model model) {
        model.addAttribute("course", courseService.getCourseById(courseId));
        model.addAttribute("sections", sectionService.getSectionsByCourse(courseId));
        return "admin/sections/list";
    }

    @GetMapping("/{courseId}/sections/new")
    public String newSection(@PathVariable("courseId") Long courseId, Model model) {
        model.addAttribute("course", courseService.getCourseById(courseId));
        model.addAttribute("section", new CourseSection());
        return "admin/sections/form";
    }

    @PostMapping("/{courseId}/sections/save")
    public String saveSection(@PathVariable("courseId") Long courseId,
                              @ModelAttribute("section") CourseSection section) {
        section.setCourseId(courseService.getCourseById(courseId));
        if (section.getId() == null)
            sectionService.createCourseSection(section);
        else
            sectionService.updateCourseSection(section);
        return "redirect:/admin/courses/" + courseId + "/sections";
    }

    @GetMapping("/{courseId}/sections/{id}/delete")
    public String deleteSection(@PathVariable("courseId") Long courseId,
                                @PathVariable("id") Long id) {
        sectionService.deleteCourseSection(id);
        return "redirect:/admin/courses/" + courseId + "/sections";
    }

    // ================== LESSON ==================
    @GetMapping("/{courseId}/sections/{sectionId}/lessons")
    public String listLessons(@PathVariable("courseId") Long courseId,
                              @PathVariable("sectionId") Long sectionId, Model model) {
        model.addAttribute("course", courseService.getCourseById(courseId));
        model.addAttribute("section", sectionService.getCourseSectionById(sectionId));
        model.addAttribute("lessons", lessonService.getLessonsBySection(sectionId));
        return "admin/lessons/list";
    }

    @GetMapping("/{courseId}/sections/{sectionId}/lessons/new")
    public String newLesson(@PathVariable("courseId") Long courseId,
                            @PathVariable("sectionId") Long sectionId, Model model) {
        model.addAttribute("course", courseService.getCourseById(courseId));
        model.addAttribute("section", sectionService.getCourseSectionById(sectionId));
        model.addAttribute("lesson", new Lesson());
        return "admin/lessons/form";
    }

    @PostMapping("/{courseId}/sections/{sectionId}/lessons/save")
    public String saveLesson(@PathVariable("courseId") Long courseId,
                             @PathVariable("sectionId") Long sectionId,
                             @ModelAttribute("lesson") Lesson lesson) {
        lesson.setSectionId(sectionService.getCourseSectionById(sectionId));
        if (lesson.getId() == null)
            lessonService.createLesson(lesson);
        else
            lessonService.updateLesson(lesson);
        return "redirect:/admin/courses/" + courseId + "/sections/" + sectionId + "/lessons";
    }

    @GetMapping("/{courseId}/sections/{sectionId}/lessons/{id}/delete")
    public String deleteLesson(@PathVariable("courseId") Long courseId,
                               @PathVariable("sectionId") Long sectionId,
                               @PathVariable("id") Long id) {
        lessonService.deleteLesson(id);
        return "redirect:/admin/courses/" + courseId + "/sections/" + sectionId + "/lessons";
    }
}
