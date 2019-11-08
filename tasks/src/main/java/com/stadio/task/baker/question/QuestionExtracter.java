package com.stadio.task.baker.question;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Andy on 01/22/2018.
 */
public class QuestionExtracter
{
    public static boolean isQuestion(String x)
    {
        return !x.startsWith("0 ") && !x.startsWith("1 ");
    }

    public static boolean isAnswer(String x)
    {
        return x.startsWith("0 ") || x.startsWith("1 ");
    }

    public static String parseQuestion(String s)
    {
        String rxg = "\\d+:(.*)(\\?|:)";
        Matcher matcher = Pattern.compile(rxg).matcher(s);
        if (matcher.find())
        {
            //String[] q = matcher.group(1).split(":");
            //if (q.length >= 2) return q[1];
            return matcher.group(1);
        }
        return null;
    }

    public static String parseAnswerA(String s)
    {
        String rxg = "A(\\.|:)(.*?)(B(\\.|:)|C(\\.|:)|D(\\.|:)|[\n]|$)";
        Matcher matcher = Pattern.compile(rxg).matcher(s);
        if (matcher.find())
        {
            return matcher.group(2);
        }
        return "";
    }

    public static String parseAnswerB(String s)
    {
        String rxg = "B(\\.|:)(.*?)(A(\\.|:)|C(\\.|:)|D(\\.|:)|[\n]|$)";
        Matcher matcher = Pattern.compile(rxg).matcher(s);
        if (matcher.find())
        {
            return matcher.group(2);
        }
        return "";
    }

    public static String parseAnswerC(String s)
    {
        String rxg = "C(\\.|:)(.*?)(B(\\.|:)|A(\\.|:)|D(\\.|:)|[\n]|$)";
        Matcher matcher = Pattern.compile(rxg).matcher(s);
        if (matcher.find())
        {
            return matcher.group(2);
        }
        return "";
    }

    public static String parseAnswerD(String s)
    {
        String rxg = "D(\\.|:)(.*?)(A(\\.|:)|B(\\.|:)|C(\\.|:)|[\n]|$)";
        Matcher matcher = Pattern.compile(rxg).matcher(s);
        if (matcher.find())
        {
            return matcher.group(2);
        }
        return "";
    }

    public static String randomAnswer()
    {
        String[] s = new String[]{"A", "B", "C", "D"};
        int rnd = new Random().nextInt(s.length);
        return s[rnd];
    }


    public static List<String> question(File f)
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(f.getAbsolutePath());
            HWPFDocument document = new HWPFDocument(fis);
            WordExtractor wk = new WordExtractor(document);

            String content = wk.getText().split("----------HẾT----------")[0];
            return Arrays.asList(content.split("Câu"));

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void main(String[] args)
    {
        String s = " 2: Cho kim loại Ba dư vào dung dịch Al2(SO4)3, thu được sản phẩm có?\t" +
                "A. Một chất khí và hai chất kết tủa.\tB. Một chất khí và không chất kết tủa.\t" +
                "     C. Một chất khí và một chất kết tủa.\tD. Hỗn hợp hai chất khí.\t";
        System.out.println(parseQuestion(s));
        System.out.println(parseAnswerA(s));
        System.out.println(parseAnswerB(s));
        System.out.println(parseAnswerC(s));
        System.out.println(parseAnswerD(s));
        //System.out.println(randomAnswer());
    }


}
