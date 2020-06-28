package com.company;

import java.util.List;

public class Exam {
    private int id;
    private String question;
    private List<String> option;
    private String answer;

    public Exam() {
    }

    public Exam(int id, String question, List<String> option, String answer) {
        this.id = id;
        this.question = question;
        this.option = option;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getOption() {
        return option;
    }

    public void setOption(List<String> option) {
        this.option = option;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return question;
    }

}
