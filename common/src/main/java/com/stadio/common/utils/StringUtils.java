package com.stadio.common.utils;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.rmi.CORBA.Util;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Andy on 8/13/2017.
 */
public class StringUtils
{

    public static final char dash_2013 = '\u2013';

    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)"
                    + "(?=.*[a-z])"
                    + "(?=.*[A-Z])"
                    + "(?=.*[@#$%~!%^&()]))";

    private static final Pattern PASSWORD_PATTERN_OBJ = Pattern.compile(PASSWORD_PATTERN);


    public static boolean isValidPassword(String password)
    {
        return password != null && password.length() >= 6 && password.length() < 20;
    }

    public static String iso_8859_1(String s)
            throws Exception
    {
        if (s == null)
        {
            return "";
        }
        return new String(s.getBytes(), "iso-8859-1");
    }

    public static String encode(String s, String utf)
    {
        if (s == null)
        {
            return "";
        }
        try
        {
            return new String(s.getBytes(), utf);
        }
        catch (Exception xp)
        {
            return s;
        }
    }

    public static String iso_8859_1tc(Object s)
    {
        if (s == null)
        {
            return "";
        }
        try
        {
            return new String(s.toString().getBytes(), "iso-8859-1");
        }
        catch (Exception xp)
        {
            return s.toString();
        }
    }

    public static byte[] md5(String msg) throws Exception
    {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return md.digest(msg.getBytes());
    }

    public static String box(String nk)
    {
        return "[[" + nk + "]]";
    }

    public static void boxAndShow(String nk)
    {
        System.out.println(box(nk));
    }

    public static String md5_string(String msg) throws Exception
    {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] res = md.digest(msg.getBytes());
        return new String(res);
    }

    public static String md5_hex(String msg) throws Exception
    {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] res = md.digest(msg.getBytes());
        return toHexString(res);
    }

    public static String toHexString(byte[] t)
    {
        final StringBuilder builder = new StringBuilder();
        for (byte b : t)
        {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public static String join(String cm, double... v)
    {
        String res = "";
        for (int k = 0; k < v.length; k++)
        {
            res += (k == 0 ? "" : cm) + fmt4(v[k]);
        }

        return res;
    }

    public static String join(String cm, Object... v)
    {
        String res = "";
        for (int k = 0; k < v.length; k++)
        {
            res += (k == 0 ? "" : cm) + v[k];
        }

        return res;
    }

    public static String joinList(String cm, List<String> v)
    {
        String res = "";
        for (int k = 0; k < v.size(); k++)
        {
            res += (k == 0 ? "" : cm) + v.get(k);
        }

        return res;
    }

    public static String defval(String v, String dval)
    {
        return v == null ? dval : v;
    }

    public static String fmt4(double d)
    {
        return d + "";
    }

    public static boolean isValid(String sk)
    {
        return sk != null && sk.trim().length() > 0;
    }

    public static String getVideoIdFromVimeoUrl(String url)
    {
        if (!StringUtils.isNotNull(url)) {
            return null;
        }
        String regex = "(?<=vimeo.com.).*";
        Matcher matcher =
                Pattern.compile("(?<=vimeo.com.).*").matcher(url);
        try {
            if (matcher.find()) {
                return matcher.group(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }

    public static String beforeLast(char dot, String name)
    {
        int pk = name.lastIndexOf(dot);
        return pk < 0 ? name : name.substring(0, pk);
    }

    public static String beforeFirst(char dot, String name)
    {
        int pk = name.indexOf(dot);
        return pk < 0 ? name : name.substring(0, pk);
    }

    public static String afterFirst(char dot, String name)
    {
        int pk = name.indexOf(dot);
        return pk < 0 ? "" : name.substring(pk);
    }

    public static String afterFirst1(char dot, String name)
    {
        int pk = name.indexOf(dot);
        return pk < 0 ? "" : name.substring(pk + 1);
    }

    public static String afterLast(char dot, String name)
    {
        int pk = name.lastIndexOf(dot);
        return pk < 0 ? "" : name.substring(pk);
    }

    public static String afterLast1(char dot, String name)
    {
        int pk = name.lastIndexOf(dot);
        return pk < 0 ? "" : name.substring(pk + 1);
    }

    public static String identifier256(String key)
    {
        try
        {
            return toHexString(MathUtils.sha256(key));
        }
        catch (Exception xp)
        {
            return null;
        }
    }

    public static String identifier256()
    {
        return StringUtils.identifier256(System.currentTimeMillis() + "/" + Math.random());
    }

    public static String identifier1(String key)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return toHexString(md.digest(key.getBytes()));
        }
        catch (Exception xp)
        {
            return null;
        }
    }

    public static String identifier_MD5(String key)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return toHexString(md.digest(key.getBytes()));
        }
        catch (Exception xp)
        {
            return null;
        }
    }

    public static String urlEncode(String name)
    {
        try
        {
            return URLEncoder.encode(name, "UTF-8");
        }
        catch (Exception xp)
        {
            return name;
        }
    }

    public static String identifier5(String key)
    {
        String res = identifier256(key);
        return res.substring(0, 5);
    }

    public static String urlDecode(String name)
    {
        try
        {
            return URLDecoder.decode(name, "UTF-8");
        }
        catch (Exception xp)
        {
            return name;
        }
    }

    public static double filterDigitsToDouble(String s)
    {
        s = s.replaceAll("\\D+", "");
        return Double.parseDouble(s);
    }

    public static List<String> splitAndRemove(String s, String cm)
    {
        List<String> res = new ArrayList<String>();

        for (String sk : s.split(cm))
        {
            String tk = sk.trim();
            if (!tk.isEmpty())
            {
                res.add(tk);
            }
        }

        return res;
    }

    public static String dotdot(String str, int len)
    {
        if (str == null)
        {
            return str;
        }
        if (str.length() > len)
        {
            str = str.substring(0, len) + "...";
        }
        return str;
    }

    public static boolean notNullAndEquals(String a, String b)
    {
        return a != null && a.equals(b);
    }


    public static List<Object> sort(List<String> items)
    {
        return items.stream().sorted().collect(Collectors.toList());
    }

    public static String dotdot5(String str)
    {
        return dotdot(str, 5);
    }

    public static String dotdot11(String str)
    {
        return dotdot(str, 11);
    }

    public static String comma(String... args)
    {
        for (String ak : args)
        {
            if (ak != null)
            {
                return ak;
            }
        }
        return null;
    }

    public static String[] splitByFirst(char c, String nk)
    {
        int pk = nk.indexOf(c);
        String left = nk.substring(0, pk).trim();
        String right = nk.substring(pk + 1).trim();
        return new String[]{left, right};
    }

    public static String notNull(Object s)
    {
        if (s == null || s.equals("null"))
        {
            return "";
        }
        return s.toString();
    }

    public static String notNull(String s, String dv)
    {
        return s == null || s.length() == 0 ? dv : s;
    }

    public static String rep(String sub, int level)
    {
        StringBuilder res = new StringBuilder();
        for (int k = 0; k < level; k++)
        {
            res.append(sub);
        }
        return res.toString();
    }

    public static String convert_8859_to_utf8(String value) throws Exception
    {
        return new String(value.getBytes("ISO-8859-1"), "UTF-8");
    }

    public static String toPrettyURL(String string)
    {
        return Normalizer.normalize(string.toLowerCase(), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^\\p{Alnum}]+", "-");
    }

    public static boolean isValidEmailAddress(String email)
    {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isNotNull(String string)
    {
        if (string == null || string.length() == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public static boolean isPhoneNumber(String phone)
    {
        String regexStr = "^[0-9]*$";
        String removeCode = phone.replace("+84", "0");

        if (removeCode.matches(regexStr))
        {
            if (removeCode.length() >= 10)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        ;

        return false;
    }

    public static boolean isSamePhoneNumber(String phone1, String phone2) {
        if (StringUtils.isPhoneNumber(phone1) && StringUtils.isPhoneNumber(phone2)) {
            String removeCodePhone1 = StringUtils.convertToFullPhoneNumber(phone1,"+84");
            String removeCodePhone2 = StringUtils.convertToFullPhoneNumber(phone2,"+84");

            return (removeCodePhone1.equals(removeCodePhone2));
        }
        return false;
    }

    public static String convertToFullPhoneNumber(String phone,String nationCode) {
        if (!StringUtils.isNotNull(phone)) {
            return "";
        }
        if (!phone.contains("+") && phone.startsWith("0")) {
            return phone.replaceFirst("^0",nationCode);
        } else if (!phone.contains("+") && !phone.startsWith("0")) {
            return nationCode + phone;
        } else  {
            return phone;
        }
    }

    public static String friendly(String s)
    {

        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

        s = pattern.matcher(temp).replaceAll("");
        s = s.toLowerCase();
        s = s.replaceAll("đ", "d");
        s = s.replaceAll("Đ", "d");
        s = s.replaceAll("\\W", " ");
        s = s.replaceAll(" ", "-");
        s = s.replaceAll("/-+-/g", "-");
        s = s.replaceAll("/^\\-+|\\-+$/g", "");
        s = s.replaceAll("[,]", "");
        s = s.replaceAll("--", "-");
        s = s.replaceAll("---", "-");

        return s;
    }

    public static String hmac256(String key, String data)
    {
        try
        {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
        }
        catch (Exception xp)
        {
            xp.printStackTrace();
            return "";
        }
    }

    public static String toStringWithMinLenght(String str, int minLenght) {
        if (str.length() < minLenght) {
            StringBuilder stringBuilder = new StringBuilder(str);
            for (int i = 0; i < minLenght - str.length(); i++) {
                stringBuilder.insert(0,"0");
            }
            return stringBuilder.toString();
        } else {
            return str;
        }
    }

    public static String normalized(String str) {
        try {
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d").toLowerCase();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String separatorNumberWithCharacter(Number number){
        return String.format( "%,d", number).replace(',', '.');
    }
}

