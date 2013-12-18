package de.chino;

import java.io.*;
import java.util.*;

public class PinyinConvert
{

    public PinyinConvert()
    {
        pySeparator = new String[TOTALPY];
        pySeparator[PY] = "'";
        pyChars = new String[TOTALPY];
        pyChars[PY] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstu\374vwxyz:012345";
        pyChars[PYTONE] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstu\374vwxyz:012345\u0101\341\u01CE\340\u0113\351\u011B\350\u012B\355\u01D0\354\u014D\363\u01D2\362\u016B\372\u01D4\371\u01D6\u01D8\u01DA\u01DC";
        pyChars[WG] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcde\352fghijklmnopqrstu\374vwxyz^':012345";
        pyChars[YALE] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz012345";
        pyChars[GI] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstu\374vwxyz012345";
        pyChars[BPMF] = "\u3105\u3106\u3107\u3108\u3109\u310A\u310B\u310C\u310D\u310E\u310F\u3110\u3111\u3112\u3113\u3114\u3115\u3116\u3117\u3118\u3119\u311A\u311E\u3122\u3124\u3120\u311C\u311F\u3123\u3125\u3126\u4E00\u3129\u3128\u02D9\u02C9\u02CA\u02C7\u02CB";
        pyChars[GR] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.";
        pyChars[FR] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmno\366pqrstuvwxyz^':012345";
        pyChars[TY] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz012345";
        pyTable = new String[2036][TOTALPY];
        chartree = new Hashtable[TOTALPY];
        for(int i = 0; i < TOTALPY; i++)
            chartree[i] = new Hashtable();

        load_table();
    }

    private void load_table()
    {
        boolean flag = false;
        boolean flag1 = false;
        int k = 0;
        try
        {
            java.io.InputStream inputstream = ClassLoader.getSystemResourceAsStream("zh_pytoned.txt");
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, "UTF8"));
            String s;
            do
            {
                s = bufferedreader.readLine();
                if(s != null && s.length() != 0 && s.charAt(0) != '#')
                {
                    StringTokenizer stringtokenizer = new StringTokenizer(s);
                    for(int i = 0; stringtokenizer.hasMoreTokens(); i++)
                    {
                        String s1 = stringtokenizer.nextToken();
                        if(s1.indexOf(",") != -1)
                            pyTable[k][i] = s1.substring(0, s1.indexOf(","));
                        else
                            pyTable[k][i] = new String(s1);
                        Hashtable hashtable;
                        for(StringTokenizer stringtokenizer1 = new StringTokenizer(s1, ","); stringtokenizer1.hasMoreTokens(); hashtable.put("EOW", new Integer(k)))
                        {
                            String s2 = stringtokenizer1.nextToken();
                            hashtable = chartree[i];
                            for(int j = 0; j < s2.length(); j++)
                            {
                                if(!hashtable.containsKey(s2.substring(j, j + 1)))
                                    hashtable.put(s2.substring(j, j + 1), new Hashtable());
                                hashtable = (Hashtable)hashtable.get(s2.substring(j, j + 1));
                            }

                        }

                    }

                    k++;
                }
            } while(s != null);
        }
        catch(IOException ioexception)
        {
            System.out.println("IOException:" + ioexception);
        }
    }

    int py_lookup(int i, String s)
    {
        Hashtable hashtable = chartree[i];
        int j = 0;
        Hashtable hashtable1;
        for(hashtable1 = hashtable; hashtable1.containsKey(s.substring(j, j + 1)) && j < s.length(); j++)
            hashtable1 = (Hashtable)hashtable1.get(s.substring(j, j + 1));

        if(hashtable1.containsKey("EOW"))
            return ((Integer)hashtable1.get("EOW")).intValue();
        else
            return -1;
    }

    public String py_lookup(int i, int j, String s)
    {
        int k = 0;
        Hashtable hashtable;
        for(hashtable = chartree[i]; k < s.length() && hashtable.containsKey(s.substring(k, k + 1)); k++)
            hashtable = (Hashtable)hashtable.get(s.substring(k, k + 1));

        if(hashtable.containsKey("EOW"))
            return pyTable[((Integer)hashtable.get("EOW")).intValue()][j];
        else
            return null;
    }

    public String[] findSyllables(int i, String s)
    {
        String as[] = new String[1];
        Vector vector = new Vector();
        int j = 0;
        boolean flag = false;
        for(s = s.toLowerCase(); j < s.length();)
        {
            Hashtable hashtable = chartree[i];
            Hashtable hashtable1 = hashtable;
            int k = j;
            do
            {
                if(j >= s.length() || s.charAt(j) == ' ' || !hashtable.containsKey(s.substring(j, j + 1)))
                    break;
                hashtable = (Hashtable)hashtable.get(s.substring(j, j + 1));
                j++;
                if(hashtable.containsKey("EOW"))
                {
                    k = j;
                    Hashtable hashtable2 = hashtable;
                }
            } while(true);
            if(hashtable.containsKey("EOW"))
                vector.add(pyTable[((Integer)hashtable.get("EOW")).intValue()][i]);
            else
            if(j < s.length() && s.charAt(j) == ' ')
                j++;
            else
            if(j < s.length() && !hashtable.containsKey(s.substring(j, j + 1)))
            {
                if(s.substring(k, j + 1).charAt(0) != ' ' || s.substring(k, j + 1).charAt(0) != '\'')
                    vector.add(s.substring(k, j + 1));
                j++;
            } else
            {
                if(j < s.length() && s.substring(k, j).charAt(0) != ' ')
                    vector.add(s.substring(k, j));
                j++;
            }
        }

        return (String[])vector.toArray(as);
    }

    public String pyConvertString(int i, int j, String s)
    {
        StringBuffer stringbuffer = new StringBuffer("");
        int k = 0;
        boolean flag = false;
        boolean flag1 = true;
        while(k < s.length()) 
        {
            Hashtable hashtable = chartree[i];
            Hashtable hashtable1 = hashtable;
            int l = k;
            do
            {
                if(k >= s.length() || !hashtable.containsKey(s.substring(k, k + 1).toLowerCase()))
                    break;
                hashtable = (Hashtable)hashtable.get(s.substring(k, k + 1).toLowerCase());
                k++;
                Hashtable hashtable2;
                if(hashtable.containsKey("EOW"))
                    hashtable2 = hashtable;
            } while(true);
            if(hashtable.containsKey("EOW"))
            {
                int i1 = capitalizationType(s.substring(l, k));
                stringbuffer.append(applyCapitalization(i1, pyTable[((Integer)hashtable.get("EOW")).intValue()][j]));
            } else
            if(k < s.length() && !hashtable.containsKey(s.substring(k, k + 1)))
            {
                stringbuffer.append(s.substring(l, k + 1));
                k++;
            } else
            {
                stringbuffer.append(s.substring(l, k));
                k++;
            }
        }
        return new String(stringbuffer);
    }

    public int capitalizationType(String s)
    {
        if(s == null)
            return 1;
        if(s.length() == 0)
            return 1;
        boolean flag = true;
        boolean flag1 = true;
        boolean flag2 = true;
        int i = 0;
        do
        {
            if(i >= s.length())
                break;
            if(Character.isLetter(s.charAt(i)) && !Character.isUpperCase(s.charAt(i)))
            {
                flag = false;
                break;
            }
            i++;
        } while(true);
        if(flag)
            return 0;
        i = 0;
        do
        {
            if(i >= s.length())
                break;
            if(Character.isLetter(s.charAt(i)) && !Character.isLowerCase(s.charAt(i)))
            {
                flag1 = false;
                break;
            }
            i++;
        } while(true);
        if(flag1)
            return 1;
        if(!Character.isUpperCase(s.charAt(0)))
            flag2 = false;
        i = 1;
        do
        {
            if(i >= s.length())
                break;
            if(Character.isLetter(s.charAt(i)) && !Character.isLowerCase(s.charAt(i)))
            {
                flag2 = false;
                break;
            }
            i++;
        } while(true);
        return !flag2 ? 3 : 2;
    }

    public String applyCapitalization(int i, String s)
    {
        if(s == null)
            return null;
        if(s.length() == 0)
            return s;
        if(i == 1)
            return s.toLowerCase();
        if(i == 0)
            return s.toUpperCase();
        if(i == 2)
        {
            char ac[] = s.toCharArray();
            ac[0] = Character.toUpperCase(ac[0]);
            return new String(ac);
        } else
        {
            return s.toLowerCase();
        }
    }

    public String pyConvertLine(int i, int j, String s)
    {
        StringBuffer stringbuffer = new StringBuffer("");
        for(int k = 0; k < s.length(); k++)
        {
            if(pyChars[i].indexOf(s.charAt(k)) == -1)
            {
                stringbuffer.append(s.charAt(k));
                continue;
            }
            int l = k;
            int i1;
            for(i1 = k; i1 < s.length() && pyChars[i].indexOf(s.charAt(i1)) != -1; i1++);
            k = i1 - 1;
            stringbuffer.append(pyConvertString(i, j, s.substring(l, i1)));
        }

        return new String(stringbuffer);
    }

    public static void main(String args[])
    {
        PinyinConvert pyconvert = new PinyinConvert();
        String args1[] = pyconvert.findSyllables(PY, "wo3de nu:pengyou");
        for(int i = 0; i < args1.length; i++)
            System.out.println(" " + i + " " + args1[i]);

        System.exit(0);
        if(args.length > 0 && (args[0].equals("h") || args[0].equals("-h")))
        {
            System.out.println("Usage:  pyConvert -[pwyigfht][pwyigfht] sourcefile");
            System.out.println("First letter is source romanization, second letter");
            System.out.println("is target romanization.");
            System.out.println(" p = Hanyu Pinyin, t = Hanyu Pinyin (tones), w = Wade Giles, y = Yale");
            System.out.println(" i = GuoinII, g = Gwoyeu Romatzyh, f = French, t = Tongyong");
            System.exit(0);
        }
        if(args.length < 2)
        {
            System.err.println("Please specify conversion direction and file name.");
            System.exit(0);
        }
        int ai[] = new int[2];
        args[0] = args[0].toLowerCase();
        for(int j = 0; j < 2; j++)
        {
            char c = args[0].charAt(j + 1);
            if(c == 'p')
            {
                ai[j] = PY;
                continue;
            }
            if(c == 't')
            {
                ai[j] = PYTONE;
                continue;
            }
            if(c == 'w')
            {
                ai[j] = WG;
                continue;
            }
            if(c == 'y')
            {
                ai[j] = YALE;
                continue;
            }
            if(c == 'i')
            {
                ai[j] = GI;
                continue;
            }
            if(c == 'g')
            {
                ai[j] = GR;
                continue;
            }
            if(c == 'f')
            {
                ai[j] = FR;
                continue;
            }
            if(c == 't')
                ai[j] = TY;
        }

        try
        {
            FileInputStream fileinputstream = new FileInputStream(args[1]);
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(fileinputstream, "Big5"));
            String s;
            while((s = bufferedreader.readLine()) != null) 
                System.out.println(pyconvert.pyConvertLine(ai[0], ai[1], s));
        }
        catch(Exception exception)
        {
            System.err.println(exception);
        }
    }

    public static int PY = 0;
    public static int PYTONE = 1;
    public static int WG = 2;
    public static int YALE = 3;
    public static int GI = 4;
    public static int BPMF = 5;
    public static int GR = 6;
    public static int FR = 7;
    public static int TY = 8;
    public static int TOTALPY = 9;
    public static final int ALLUPPER = 0;
    public static final int ALLLOWER = 1;
    public static final int FIRSTUPPER = 2;
    public static final int MIXED = 3;
    final Hashtable chartree[];
    String pyTable[][];
    String pySeparator[];
    String pyChars[];
    public static final String pyNames[] = {
        "Hanyu Pinyin (numbers)", "Hanyu Pinyin (tone marks)", "Wade-Giles", "Yale", "GuoinII", "Bopomofo", "Gwoyeu Romatzyh", "French", "Tongyong Pinyin"
    };

}
