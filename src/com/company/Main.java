package com.company;


import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static String filePath = "/Users/Bing/Documents/GitHub/BingnaYang.github.io/ExamBuilder/src/com/company/ExamBuilder.xml";

    public static void main(String[] args) {
        List<Exam> examsList = new ArrayList<>();
        try {
            // Check if ExamBuilder.xml file existed, if not create the xml file
            File fileTesting = new File(filePath);

            if (fileTesting.exists()) {
//                System.out.println("XML file already exist");
                parseXMLtoObject(examsList);
                int selectMenu;
                do{
                    startingMenu();
                    Scanner scanner = new Scanner(System.in);
                    selectMenu = scanner.nextInt();
                    switch (selectMenu){
                        case 1:
                            System.out.println("Add an Exam Question");
                            addExamQuestion(examsList);
                            parseXMLtoObject(examsList);
                            break;
                        case 2:
                            System.out.println("Delete an Exam Question");
                            deleteQuestion(examsList);
                            parseXMLtoObject(examsList);
                            break;
                        case 3:
                            System.out.println("Print Exam to PDF File");
                            printExamToPDF(examsList);
                            break;
                        case 4:
                            System.out.println("Print Exam Questions and Answer to PDF File");
                            printExamAndAnswerToPDF(examsList);
                            break;
                        case 5:
                            System.out.println("Exit");
                            break;
                        default:
                            System.out.println("Not an option");
                            break;
                    }
                }while (selectMenu != 5);

            } else {
                System.out.println("XML file creating....");
                createXMLFile();
                System.out.println("ExamBuilder XML file successfully created");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createXMLFile() {

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.newDocument();

            // Root element
            Element rootElement = document.createElement("Exams");
            document.appendChild(rootElement);

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addExamQuestion(List<Exam> exams){
        Scanner scanner = new Scanner(System.in);
        List<String> optionList = new ArrayList<>();
        System.out.println("==========================");
        System.out.println("++Add an Exam Question Mode++");
        System.out.println("==========================");
        // Scan Question
        System.out.println("Enter the question: ");
        String question = scanner.nextLine();
;
        // Scan option
        // Ask how many option for that question and scan and add the options to the list
        System.out.println("How many answer options for this question? ");
        int numOption = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < numOption; i++) {
            System.out.println("Enter the answer options(" + i + "): ");
            String option = scanner.nextLine();
            optionList.add(option);
        }
        // Scan answer
        System.out.println("Enter the question answer: ");
        String answer = scanner.next();

        // Add exam question, option, and answer to XML file
        addDataToXML(question, optionList, answer,exams);
    }

    public static void deleteQuestion(List<Exam> exams){
        Scanner scanner = new Scanner(System.in);
        printQuestionToConsole(exams);
        System.out.println("Which question need to to delete? [Enter Question Number]: ");
        int deleteNumber = scanner.nextInt();
        int temp = deleteNumber-1;
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;
            Document document;
            dbFactory.setValidating(false);
            dBuilder = dbFactory.newDocumentBuilder();
            document = dBuilder.parse(new FileInputStream(new File(filePath)));
            // retrieve the element
            Element element = (Element) document.getElementsByTagName("Details").item(temp);
            // remove the specific node
            element.getParentNode().removeChild(element);
            // Normalize the DOM tree
            document.normalize();
            DOMSource source = new DOMSource(document);
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(filePath);
            tf.transform(source, result);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void addDataToXML(String questionInput, List<String> optionInput, String answerInput, List<Exam> exams) {
        int getLastID = exams.size()+1;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(filePath);

            Element root = document.getDocumentElement();
            Element rootElement = document.getDocumentElement();
            Element detail = document.createElement("Details");
            rootElement.appendChild(detail);
            Element id = document.createElement("id");
            id.appendChild(document.createTextNode(Integer.toString(getLastID)));
            detail.appendChild(id);

            Element question = document.createElement("question");
            question.appendChild(document.createTextNode(questionInput));
            detail.appendChild(question);

            for (int i = 0; i < optionInput.size(); i++) {
                Element option = document.createElement("option");
                option.appendChild(document.createTextNode(optionInput.get(i)));
                detail.appendChild(option);
            }

            Element answer = document.createElement("answer");
            answer.appendChild(document.createTextNode(answerInput));
            detail.appendChild(answer);
            // Append all elements to the root element
            root.appendChild(detail);

            DOMSource source = new DOMSource(document);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(filePath);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void parseXMLtoObject(List<Exam> exams) {
        List<String> options;

        try {
             //Get Document Builder
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();;
            Document document = dBuilder.parse(new File(filePath));;

            NodeList nodeList = document.getElementsByTagName("Details");
            NodeList nOptionList = null;

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Integer id = Integer.parseInt(element.getElementsByTagName("id").item(0).getChildNodes().item(0).getNodeValue());
                    String question = element.getElementsByTagName("question").item(0).getChildNodes().item(0).getNodeValue();
                    nOptionList = element.getElementsByTagName("option");
                    // By placing here, option list is fresh every time get a new element
                    options = new ArrayList<>();
                    for (int j = 0; j < nOptionList.getLength(); j++) {
                        options.add(String.valueOf(nOptionList.item(j).getTextContent()));
                    }
                    String answer = element.getElementsByTagName("answer").item(0).getChildNodes().item(0).getNodeValue();

                    exams.add(new Exam(id, question, options, answer));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printExamToPDF(List<Exam> exams) {
        String dest = "/Users/Bing/Documents/GitHub/BingnaYang.github.io/ExamBuilder/src/pdf/Exam.pdf";
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(dest));
            // Open
            document.open();
            // Setup font for exam title
            Font titleFront = new Font();
            titleFront.setStyle(Font.BOLD);
            titleFront.setSize(20);

            // Create Exam Title
            Paragraph examTitle = new Paragraph("Java Exam",titleFront);
            examTitle.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            examTitle.setSpacingAfter(20f);
            document.add(examTitle);
            // Create Name, Date, Student_Id
            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.addCell(getCell("Name:__________________", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("Date:_____________", PdfPCell.ALIGN_CENTER));
            table.addCell(getCell("Student_ID:_______________", PdfPCell.ALIGN_RIGHT));
            document.add(table);
            // Print questions and answer options
            for(Exam list:exams){
                document.add(new Paragraph(list.getId()+") "+list.getQuestion()));
                char option = 'a';
                for(int i=0; i<list.getOption().size();i++){
                    document.add(new Paragraph("["+option+"] "+list.getOption().get(i)+"\n"));
                    option++;
                }
                document.add(Chunk.NEWLINE);
            }

            // Close
            document.close();
            System.out.println("Exam PDF File Created");
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public static void printExamAndAnswerToPDF(List<Exam> exams){
        String dest = "/Users/Bing/Documents/GitHub/BingnaYang.github.io/ExamBuilder/src/pdf/ExamAnswer.pdf";
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(dest));
            // Open
            document.open();
            // Setup font for exam title
            Font titleFront = new Font();
            titleFront.setStyle(Font.BOLD);
            titleFront.setSize(20);
            // Create Exam Title
            Paragraph examTitle = new Paragraph("Java Exam and Answer",titleFront);
            examTitle.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
            examTitle.setSpacingAfter(20f);
            document.add(examTitle);

            // Print questions and answer
            for(Exam list:exams){
                document.add(new Paragraph(list.getId()+") "+list.getQuestion()));
                char option = 'a';
                for(int i=0; i<list.getOption().size();i++){
                    document.add(new Paragraph("["+option+"] "+list.getOption().get(i)+"\n"));
                    option++;
                }
                document.add(new Paragraph("Answer: "+list.getAnswer()));
                document.add(Chunk.NEWLINE);
            }
            // Close
            document.close();
            System.out.println("Exam Answer PDF File Created");
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public static void startingMenu() {
        System.out.println("==========================");
        System.out.println("Welcome to Exam Builder");
        System.out.println("==========================");
        System.out.println("1. Add an Exam Question");
        System.out.println("2. Delete an Exam Question");
        System.out.println("3. Print Exam to PDF File");
        System.out.println("4. Print Exam Questions with Answer to PDF File");
        System.out.println("==========================");
        System.out.println("Enter Your Option: ");
    }

    // For Testing
    public static void printQuestionToConsole(List<Exam> exams) {
        for (Exam quiz : exams) {
            System.out.println(quiz.toString());
        }
    }
}
