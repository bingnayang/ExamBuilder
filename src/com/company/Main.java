package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) {
//        createXMLFile();

        List<Exam> examsList = new ArrayList<>();
        List<String> option = new ArrayList<>();

        int id_1 = 0;
        String q1 = "True/False: The base 2 logarithm of 100 is 2";
        String option1_1 = "True";
        String option1_2 = "False";
        String answer = "FALSE";
        option.add(option1_1);
        option.add(option1_2);
        examsList.add(new Exam(id_1,q1,option,answer));

        List<String> option1 = new ArrayList<>();
        int id_2 = 1;
        String q2 = "Big O notation tells";
        String option2_1 = "how the speed of an algorithm relates to the number of items.";
        String option2_2 = "the running time of an algorithm for a given size data structure.";
        String option2_3 = "the running time of an algorithm for a given number of items.";
        String option2_4 = "how the size of a data structure relates to the number of items.";
        String answer2 = "A";
        option1.add(option2_1);
        option1.add(option2_2);
        option1.add(option2_3);
        option1.add(option2_4);

        examsList.add(new Exam(id_2,q2,option1,answer2));
        //-----------------------------------------------

        addQuestionToXML(examsList);

    }
    public static void createXMLFile(){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // Root element
            Element rootElement = doc.createElement("Exams");
            doc.appendChild(rootElement);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("/Users/Bing/Documents/GitHub/BingnaYang.github.io/ExamBuilder/src/com/company/ExamBuilder.xml"));
            transformer.transform(source, result);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addQuestionToXML(List<Exam> exams){
        try {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse("/Users/Bing/Documents/GitHub/BingnaYang.github.io/ExamBuilder/src/com/company/ExamBuilder.xml");
            Element root = document.getDocumentElement();
            Element rootElement = document.getDocumentElement();

            for(Exam examList : exams){
                Element detail = document.createElement("Details");
                rootElement.appendChild(detail);

                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(Integer.toString(examList.getId())));
                detail.appendChild(id);

                Element question = document.createElement("question");
                question.appendChild(document.createTextNode(examList.getQuestion()));
                detail.appendChild(question);

                for(int i=0; i<examList.getOption().size();i++){
                    Element option = document.createElement("option");
                    option.appendChild(document.createTextNode(examList.getOption().get(i)));
                    detail.appendChild(option);
                }

                Element answer = document.createElement("answer");
                answer.appendChild(document.createTextNode(examList.getAnswer()));
                detail.appendChild(answer);

                root.appendChild(detail);
            }


            DOMSource source = new DOMSource(document);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult("/Users/Bing/Documents/GitHub/BingnaYang.github.io/ExamBuilder/src/com/company/ExamBuilder.xml");
            transformer.transform(source, result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
