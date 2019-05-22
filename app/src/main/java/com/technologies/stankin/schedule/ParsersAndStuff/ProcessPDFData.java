package com.technologies.stankin.schedule.ParsersAndStuff;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProcessPDFData {

    private DataFromPDF dataFromPDF;
    private List<Float> paraPositions = new ArrayList<>();
    private String text;
    private List<Float> xPositions;

    public ProcessPDFData(String url) throws IOException {
        dataFromPDF = new DataFromPDF(new URL(url));
        text = dataFromPDF.getText();
        while(getDevilTrigger()){}
        xPositions = dataFromPDF.getTextPositions();
        processParaPositions();
    }

    public void setUrl(String url) throws IOException {
        dataFromPDF = new DataFromPDF(new URL(url));
        text = dataFromPDF.getText();
        while(getDevilTrigger()){}
        xPositions = dataFromPDF.getTextPositions();
        processParaPositions();
    }

    public List<List<String>> GetPrecessedText() {

        List<List<String>> allProcessedLessons = new ArrayList<>();

        Matcher deleteUnusedPrefix = Pattern.compile("Суббота").matcher(text);

        deleteUnusedPrefix.find();
        int posUnusedEnd = deleteUnusedPrefix.end();

        for(int i = 0; i < posUnusedEnd;i++) {
            xPositions.remove(i);
        }

        text = text.substring(posUnusedEnd);

        Matcher lessonMatcher = Pattern.compile("]").matcher(text);

        String buffStr;

        if(lessonMatcher.find()) {
            buffStr = text.substring(0,lessonMatcher.end());

            allProcessedLessons.add(getProcessedLesson(buffStr, xPositions.get(0)));

            for(int i = 0; i < lessonMatcher.end();i++) {
                xPositions.remove(i);
            }

            text.substring(lessonMatcher.end());
        }

        return allProcessedLessons;
    }

    private List<String> getProcessedLesson(String lessonStr, float pos) {
        List<String> processedLesson = new ArrayList<>();

        Matcher lessonInfo = Pattern.compile("\\[").matcher(lessonStr);

        lessonInfo.find();

        processedLesson.add(lessonStr.substring(0,lessonInfo.end()));

        for(float paraPos : paraPositions) {
            if((pos <= paraPos)||(pos >= (paraPos-50))) {
                processedLesson.add(String.valueOf(paraPositions.indexOf(paraPos)));
                break;
            }
        }

        Matcher twoDatesMatch = Pattern.compile("\\d\\d\\.\\d\\d\\-\\d\\d\\.\\d\\d").matcher(lessonStr);
        Matcher oneDateMatch = Pattern.compile("\\d\\d\\.\\d\\d").matcher(lessonStr);

        if(twoDatesMatch.find()) {
            processedLesson.add(twoDatesMatch.group().substring(0,4));
            processedLesson.add(twoDatesMatch.group().substring(6));
        }else {
            if(oneDateMatch.find()) {
                processedLesson.add(oneDateMatch.group());
                processedLesson.add(oneDateMatch.group());
            }
        }

        return processedLesson;
    }

    private void processParaPositions() {
        paraPositions.clear();

        Matcher paraPos = Pattern.compile("\\d++:\\d++\\s-\\s\\d++:\\d++").matcher(text);

        while(paraPos.find()) {
            paraPositions.add(xPositions.get(paraPos.start()));
        }
    }

    public boolean getDevilTrigger() {
        return dataFromPDF.getDevilTrigger();
    }

}
