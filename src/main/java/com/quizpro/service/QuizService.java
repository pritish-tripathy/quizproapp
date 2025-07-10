package com.quizpro.service;

import com.quizpro.dao.CourseDAO;
import com.quizpro.dao.QuestionDAO;
import com.quizpro.dao.QuestionOptionDAO;
import com.quizpro.dao.TopicDAO;
import com.quizpro.model.Course;
import com.quizpro.model.Question;
import com.quizpro.model.QuestionOption;
import com.quizpro.model.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {
    @Autowired
    private CourseDAO courseDAO;
    @Autowired
    private TopicDAO topicDAO;
    @Autowired
    private QuestionDAO questionDAO;
    @Autowired
    private QuestionOptionDAO optionDAO;

    public List<Course> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    public List<Topic> getAllTopics() {
        return topicDAO.getAllTopics();
    }

    public List<Topic> getTopicsByCourseId(Long courseId) {
        return topicDAO.getTopicsByCourseId(courseId);
    }

    public void saveQuestionWithOptions(Question question, List<String> options) {
        Question saved = questionDAO.saveQuestion(question);
        for (String optionData : options) {
            QuestionOption opt = new QuestionOption();
            opt.setOptionData(optionData);
            opt.setQuestion(saved);
            optionDAO.saveOption(opt);
        }
    }

    public Course getCourseById(Long courseId) {
        return courseDAO.findById(courseId);
    }

    public Topic getTopicById(Long topicId) {
        return topicDAO.findById(topicId);
    }

    public List<Question> getPaginatedQuestions(Long courseId, Long topicId, int page, int size) {
        int offset = (page - 1) * size;
        return questionDAO.getQuestionsByCourseAndTopic(courseId, topicId, offset, size);
    }

    public long getTotalQuestions(Long courseId, Long topicId) {
        return questionDAO.countQuestionsByCourseAndTopic(courseId, topicId);
    }

    public Question getQuestionById(Long id) {
        return questionDAO.findById(id);
    }

    public List<QuestionOption> getOptionsByQuestionId(Long qid) {
        return optionDAO.findByQuestionId(qid);
    }

    public void updateQuestion(Question question) {
        questionDAO.updateQuestion(question);
    }

    public void updateOption(Long optionId, String optionData) {
        QuestionOption opt = optionDAO.findById(optionId);
        opt.setOptionData(optionData);
        optionDAO.updateOption(opt);
    }

    public void deleteQuestion(Long id) {
        Question question = questionDAO.findById(id);
        if (question != null) {
            questionDAO.delete(question);
        }
    }
}
