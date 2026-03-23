package com.example.J2EE_Ktragiuaki.controller;

import com.example.J2EE_Ktragiuaki.entity.Category;
import com.example.J2EE_Ktragiuaki.entity.Student;
import com.example.J2EE_Ktragiuaki.repository.StudentRepository;
import com.example.J2EE_Ktragiuaki.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.transaction.annotation.Transactional;
import java.security.Principal;
import java.util.Optional;

@Controller
public class EnrollmentController {

    private final StudentRepository studentRepository;
    private final CategoryService categoryService;

    public EnrollmentController(StudentRepository studentRepository, CategoryService categoryService) {
        this.studentRepository = studentRepository;
        this.categoryService = categoryService;
    }

    @GetMapping("/enroll")
    public String enrollPage() {
        return "enroll/index";
    }

    @PostMapping("/enroll/{id}")
    @Transactional
    public String enrollCourse(@PathVariable Long id, Principal principal) {
        if (principal == null) return "redirect:/login";
        String username = principal.getName();
        Optional<Student> studentOpt = studentRepository.findByUsername(username);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            try {
                Category course = categoryService.getCategoryById(id);
                if (course != null) {
                    student.getCourses().add(course);
                    studentRepository.save(student);
                }
            } catch (Exception e) {
                // Ignore if not found
            }
        }
        return "redirect:/home?enrollSuccess=true";
    }

    @PostMapping("/unenroll/{id}")
    @Transactional
    public String unenrollCourse(@PathVariable Long id, Principal principal) {
        if (principal == null) return "redirect:/login";
        String username = principal.getName();
        Optional<Student> studentOpt = studentRepository.findByUsername(username);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            try {
                Category course = categoryService.getCategoryById(id);
                if (course != null) {
                    Category toRemove = null;
                    for (Category c : student.getCourses()) {
                        if (c.getId().equals(course.getId())) {
                            toRemove = c;
                            break;
                        }
                    }
                    if (toRemove != null) {
                        student.getCourses().remove(toRemove);
                        studentRepository.save(student);
                    }
                }
            } catch (Exception e) {
                // Ignore if not found
            }
        }
        return "redirect:/home?unenrollSuccess=true";
    }
}
