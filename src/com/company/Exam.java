package com.company;

import java.util.List;

public class Exam {
    private int id;
    private String question;
    private List<String> option;
    private String answer;

    public Exam(int id, String question, List<String> option, String answer) {
        this.id = id;
        this.question = question;
        this.option = option;
        this.answer = answer;
    }
    public int getId() {
        return id;
    }
    public String getQuestion() {
        return question;
    }
    public List<String> getOption() {
        return option;
    }
    public String getAnswer() {
        return answer;
    }
    @Override
    public String toString() {
        return id+") "+question;
    }

}
