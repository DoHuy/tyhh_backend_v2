package com.stadio.task.baker.question;

import com.stadio.task.AppMainForTasks;
import com.stadio.model.documents.Question;
import com.stadio.model.enu.QuestionLevel;
import com.stadio.model.enu.QuestionType;
import com.stadio.model.model.Answer;

import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * Created by Andy on 01/24/2018.
 */
public class loading_and_testing_doc_data extends AppMainForTasks
{

    public static void main(String[] args)
    {
        File f = useFM().getDesktopFile("data-deploy/1.doc");

        List<String> questionList = QuestionExtracter.question(f);


        for (String sk: questionList)
        {
            sk = sk.replaceAll(System.lineSeparator(), "\t");
            //System.out.println(sk);
            Question question = new Question();
            String content = QuestionExtracter.parseQuestion(sk);
            if (content == null)
            {
                System.err.println("Content is null");
                continue;
            }
            if (content.startsWith("Chọn ")) continue;

            question.setContent(content.trim());

            Answer answer = new Answer();
            answer.setCode("A");
            String a = QuestionExtracter.parseAnswerA(sk).trim();
            if (a.length() == 0)
            {
                System.err.println(sk);
                System.err.println("\tAnswer A is null");
                break;
            }
            answer.setContent(a);
            answer.setCorrect(false);
            question.getAnswers().add(answer);

            answer = new Answer();
            answer.setCode("B");
            String b = QuestionExtracter.parseAnswerB(sk).trim();
            if (b.length() == 0)
            {
                System.err.println(sk);
                System.err.println("\tAnswer B is null");
                break;
            }
            answer.setContent(b);
            answer.setCorrect(false);
            question.getAnswers().add(answer);

            answer = new Answer();
            answer.setCode("C");
            String c = QuestionExtracter.parseAnswerC(sk).trim();
            if (c.length() == 0)
            {
                System.err.println(sk);
                System.err.println("\tAnswer C is null");
                break;
            }
            answer.setContent(c);
            answer.setCorrect(false);
            question.getAnswers().add(answer);

            answer = new Answer();
            answer.setCode("D");
            String d = QuestionExtracter.parseAnswerD(sk).trim();
            if (d.length() == 0)
            {
                System.err.println(sk);
                System.err.println("\tAnswer D is null");
                break;
            }
            answer.setContent(d);
            answer.setCorrect(false);
            question.getAnswers().add(answer);

            int rk = new Random().nextInt(4);
            for (int i = 0; i < question.getAnswers().size(); i++)
            {
                if (i == rk)
                {
                    System.out.println(i);
                    question.getAnswers().get(i).setCorrect(true);
                    break;
                }
            }

            question.setLevel(QuestionLevel.random());
            question.setType(QuestionType.random());
            //question.setClazz(RandomUtils.number(10, 12));

//                int ck = new Random().nextInt(chapterList.size());
//                question.setChapterId(chapterList.get(ck).getId());
//                questionRepository.save(question);

            System.out.println(question);

        }
    }



}
