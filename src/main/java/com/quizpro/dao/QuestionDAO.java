package com.quizpro.dao;

import com.quizpro.model.Question;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDAO {
    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public Question saveQuestion(Question question) {
        entityManager.persist(question);
        return question;
    }

    @Transactional
    public Question findById(Long id) {
        return entityManager.find(Question.class, id);
    }

    @Transactional
    public List<Question> getQuestionsByCourseAndTopic(Long courseId, Long topicId, int offset, int limit) {
        String jpaQl = "SELECT q FROM Question q WHERE q.course.courseId = :cid AND q.topic.topicId = :tid ORDER BY q.questionId";
        return entityManager.createQuery(jpaQl, Question.class)
                .setParameter("cid", courseId)
                .setParameter("tid", topicId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Transactional
    public long countQuestionsByCourseAndTopic(Long courseId, Long topicId) {
        String jpaQl = "SELECT COUNT(q) FROM Question q WHERE q.course.courseId = :cid AND q.topic.topicId = :tid";
        return entityManager.createQuery(jpaQl, Long.class)
                .setParameter("cid", courseId)
                .setParameter("tid", topicId)
                .getSingleResult();
    }

    @Transactional
    public void deleteQuestion(Long questionId) {
        Question q = entityManager.find(Question.class, questionId);
        if (q != null)
            entityManager.remove(q);
    }

    @Transactional
    public void updateQuestion(Question question) {
        entityManager.merge(question);
    }

    @Transactional
    public void delete(Question question) {
        entityManager.remove(entityManager.contains(question) ? question : entityManager.merge(question));
    }

}
