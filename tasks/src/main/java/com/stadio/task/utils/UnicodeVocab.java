package com.stadio.task.utils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.stadio.common.files.TomcatFileManager;
import org.apache.commons.io.FileUtils;

public class UnicodeVocab
{
    protected Map<Character, Character> map = new TreeMap<Character, Character>();

    public static UnicodeVocab fromFile()
    {
        TomcatFileManager fm = new TomcatFileManager();

        File f = fm.getResourceFile(AppConst.PATH_NAME, "vietlang-detoning-map.txt");
        try
        {
            return UnicodeVocab.fromFile(FileUtils.readLines(f, "UTF-8"));
        }
        catch (Exception xp)
        {
            return null;
        }
    }

    public static UnicodeVocab fromFile(List<String> list)
    {
        UnicodeVocab res = new UnicodeVocab();

        for (String lk : list)
        {
            int pk = lk.indexOf("->");
            if (pk < 0)
            {
                continue;
            }

            char sk = lk.substring(0, pk).trim().charAt(0);
            char tk = lk.substring(pk + 2).trim().charAt(0);

            res.map.put(sk, tk);
        }

        return res;
    }

    public char lookup(char sk, char dv)
    {
        Character tk = map.get(sk);
        return tk == null ? dv : tk;
    }

    public String lookup(String nk)
    {
        if (nk == null)
        {
            return "";
        }

        StringBuilder res = new StringBuilder();

        for (int k = 0; k < nk.length(); k++)
        {
            char ck = nk.charAt(k);
            res.append(lookup(ck, ck));
        }

        return res.toString();
    }


}
