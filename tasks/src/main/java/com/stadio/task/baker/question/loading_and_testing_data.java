package com.stadio.task.baker.question;

import com.stadio.task.AppMainForTasks;
import com.stadio.task.utils.AppConst;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Andy on 01/22/2018.
 */
public class loading_and_testing_data extends AppMainForTasks
{
    private static PrintStream out;

    public static void main(String[] args)
    throws Exception
    {
        out = System.out;

        Path p = useFM().getSystemResource(AppConst.PATH_QUESTION, "Economics.txt");
        Files.readAllLines(p).stream().forEach(x ->
        {
            if (x.length() == 0)
            {
                out.println("================");
            }
            else if (x.length() > 0)
            {
                if (QuestionExtracter.isQuestion(x))
                {
                    out.println("Question: " + x);
                }
                else if (QuestionExtracter.isAnswer(x))
                {
                    out.println("Answer: "+ x);
                }
            }
        });

    }



}
