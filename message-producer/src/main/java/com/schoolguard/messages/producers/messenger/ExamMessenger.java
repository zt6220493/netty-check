package com.schoolguard.messages.producers.messenger;

import com.google.common.base.Optional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Rogers on 15-4-30.
 */
public class ExamMessenger extends Messenger {
    private String examName;
    private List<ExamScore> examScores;

    public ExamMessenger(long messageId,
                         int schoolId,
                         String schoolName,
                         int senderId,
                         String senderName,
                         String senderAvatar,
                         String signature,
                         Date scheduleTime,
                         Date createTime,
                         String examName,
                         List<ExamScore> examScores) {
        this.jobType = JobType.EXAM;
        this.messageId = checkNotNull(messageId);
        this.schoolId = checkNotNull(schoolId);
        this.senderId = checkNotNull(senderId);
        this.schoolName = checkNotNull(schoolName);
        this.senderName = checkNotNull(senderName);
        this.senderAvatar = checkNotNull(senderAvatar);
        this.signature = signature;
        this.scheduleTime = scheduleTime;
        this.createTime = checkNotNull(createTime);
        this.examName = checkNotNull(examName);
        this.examScores = checkNotNull(examScores);
    }

    public static class ExamScore implements Serializable {
        protected static final long serialVersionUID = 8989488L;

        private Integer studentId;
        private Optional<BigDecimal> totalScore;
        private Optional<Integer> rank;
        private Map<String, BigDecimal> scores;
        private Optional<String> comment;

        public ExamScore(Integer studentId, BigDecimal totalScore, Integer rank,
                         Map<String, BigDecimal> scores, String comment) {
            this.studentId = studentId;
            this.totalScore = Optional.fromNullable(totalScore);
            this.rank = Optional.fromNullable(rank);
            this.scores = checkNotNull(scores);
            this.comment = Optional.fromNullable(comment);
        }

        public Integer getStudentId() {
            return studentId;
        }

        public Optional<BigDecimal> getTotalScore() {
            return totalScore;
        }

        public Optional<Integer> getRank() {
            return rank;
        }

        public Map<String, BigDecimal> getScores() {
            return scores;
        }

        public Optional<String> getComment() {
            return comment;
        }
    }

    public List<ExamScore> getExamScores() {
        return examScores;
    }

    public void setExamScores(List<ExamScore> examScores) {
        this.examScores = examScores;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }
}
