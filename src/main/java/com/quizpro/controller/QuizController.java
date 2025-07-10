package com.quizpro.controller;

import com.quizpro.model.Course;
import com.quizpro.model.Question;
import com.quizpro.model.QuestionOption;
import com.quizpro.model.Topic;
import com.quizpro.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class QuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping("/addQuestionForm")
    public String showAddForm(Model model) {
        List<Course> courses = quizService.getAllCourses();
        List<Topic> topics = quizService.getAllTopics();

        model.addAttribute("courses", courses);
        model.addAttribute("topics", topics);
        return "AddQuestion";
    }

    @PostMapping("/addQuestion")
    public String addQuestion(HttpServletRequest request, Model model) {
        Long courseId = Long.parseLong(request.getParameter("courseId"));
        Long topicId = Long.parseLong(request.getParameter("topicId"));
        String questionText = request.getParameter("question");
        String correctAnswer = request.getParameter("correctAnswer");

        Question question = new Question();
        question.setQuestion(questionText);
        question.setCorrectAnswer(correctAnswer);
        question.setCourse(quizService.getCourseById(courseId));
        question.setTopic(quizService.getTopicById(topicId));

        List<String> optionDataList = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            optionDataList.add(request.getParameter("option" + i));
        }

        quizService.saveQuestionWithOptions(question, optionDataList);

        return "redirect:/addQuestionForm?success=true";
    }

    @GetMapping("/goHomepage")
    public String goToHomepage(){
        return "HomePage";
    }

    @GetMapping("/showQuestions")
    public String showQuestions(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long topicId,
            @RequestParam(defaultValue = "1") int page,
            Model model) {

        List<Course> courses = quizService.getAllCourses();
        List<Topic> topics = quizService.getAllTopics();
        model.addAttribute("courses", courses);
        model.addAttribute("topics", topics);

        if (courseId != null && topicId != null) {
            List<Question> questions = quizService.getPaginatedQuestions(courseId, topicId, page, 5);
            long total = quizService.getTotalQuestions(courseId, topicId);

            model.addAttribute("questions", questions);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", (int) Math.ceil((double) total / 5));
            model.addAttribute("selectedCourse", courseId);
            model.addAttribute("selectedTopic", topicId);
        }

        return "ShowQuestions";
    }

    @GetMapping("/editQuestionForm")
    public String showEditForm(@RequestParam Long id, Model model) {
        Question question = quizService.getQuestionById(id);
        List<QuestionOption> options = quizService.getOptionsByQuestionId(id);
        List<Course> courses = quizService.getAllCourses();
        List<Topic> topics = quizService.getAllTopics();

        model.addAttribute("question", question);
        model.addAttribute("options", options);
        model.addAttribute("courses", courses);
        model.addAttribute("topics", topics);

        return "EditQuestion";
    }

    @PostMapping("/updateQuestion")
    public String updateQuestion(HttpServletRequest request) {
        Long questionId = Long.parseLong(request.getParameter("questionId"));
        Long courseId = Long.parseLong(request.getParameter("courseId"));
        Long topicId = Long.parseLong(request.getParameter("topicId"));
        String questionText = request.getParameter("question");
        String correctAnswer = request.getParameter("correctAnswer");

        Question question = quizService.getQuestionById(questionId);
        question.setQuestion(questionText);
        question.setCorrectAnswer(correctAnswer);
        question.setCourse(quizService.getCourseById(courseId));
        question.setTopic(quizService.getTopicById(topicId));
        quizService.updateQuestion(question);

        // Update 4 options
        for (int i = 0; i < 4; i++) {
            String optionValue = request.getParameter("option" + i);
            Long optionId = Long.parseLong(request.getParameter("optionId" + i));
            quizService.updateOption(optionId, optionValue);
        }

        return "redirect:/goHomepage";
    }

    @GetMapping("/deleteQuestion")
    public String deleteQuestion(@RequestParam Long id) {
        quizService.deleteQuestion(id);
        return "redirect:/goHomepage";
    }

    @GetMapping("/viewQuestion")
    public String viewFullQuestion(@RequestParam Long id, Model model) {
        Question question = quizService.getQuestionById(id);
        List<QuestionOption> options = quizService.getOptionsByQuestionId(id);

        model.addAttribute("question", question);
        model.addAttribute("options", options);

        return "ViewQuestion";
    }

}
