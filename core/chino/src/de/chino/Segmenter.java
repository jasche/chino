package de.chino;

import java.io.*;
import java.util.*;

public class Segmenter
{
    private DictionaryData dictdata;
    private String debugencoding;
    private int characterForm;
    private boolean debug;
    public static final int TRAD = 0;
    public static final int SIMP = 1;
    public static final int BOTH = 2;

    public Segmenter(DictionaryData dictionarydata)
    {
        debug = false;
        debugencoding = "UTF-8";
        boolean flag = false;
        if(dictionarydata == null)
            dictionarydata = new DictionaryData();
        else
            dictdata = dictionarydata;
    }

    private void loadset(TreeSet treeset, String s)
    {
        try
        {
            java.io.InputStream inputstream = getClass().getResourceAsStream(s);
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"));
            do
            {
                String s1;
                if((s1 = bufferedreader.readLine()) == null)
                    break;
                if(s1.indexOf("#") <= -1 && s1.length() != 0)
                    treeset.add(s1);
            } while(true);
            bufferedreader.close();
        }
        catch(Exception exception)
        {
            System.err.println("Exception loading data file" + s + " " + exception);
        }
    }

    public void setCharForm(int i)
    {
        characterForm = i;
    }

    public boolean isNotCJK(String s)
    {
        boolean flag = true;
        int i = 0;
        do
        {
            if(i >= s.length())
                break;
            if(Character.UnicodeBlock.of(s.charAt(i)) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)
            {
                flag = false;
                break;
            }
            i++;
        } while(true);
        return flag;
    }

    public String stemWord(String s)
    {
        String as[] = {
            "\u7B2C", "\u526F", "\u4E0D"
        };
        String as1[] = {
            "\u4E86", "\u7684", "\u5730", "\u4E0B", "\u4E0A", "\u4E2D", "\u91CC", "\u5230", "\u5185", "\u5916", 
            "\u4EEC"
        };
        String as2[] = {
            "\u5F97", "\u4E0D"
        };
        StringBuffer stringbuffer = new StringBuffer(s);
        for(int i = 0; i < as.length; i++)
            if(stringbuffer.substring(0, 1).equals(as[i]) && (dictdata.isChineseWord(stringbuffer.substring(1, stringbuffer.length())) || stringbuffer.length() == 2))
            {
                printDebug("Stemmed " + stringbuffer);
                stringbuffer.deleteCharAt(0);
                return stringbuffer.toString();
            }

        for(int j = 0; j < as1.length; j++)
            if(stringbuffer.substring(stringbuffer.length() - 1, stringbuffer.length()).equals(as1[j]) && (dictdata.isChineseWord(stringbuffer.substring(0, stringbuffer.length() - 1)) || stringbuffer.length() == 2))
            {
                System.out.println("Stemmed suffix");
                try
                {
                    System.out.println(new String(stringbuffer.toString().getBytes(debugencoding)));
                }
                catch(Exception exception) { }
                stringbuffer.deleteCharAt(stringbuffer.length() - 1);
                return stringbuffer.toString();
            }

        for(int k = 0; k < as2.length; k++)
            if(stringbuffer.length() == 3 && stringbuffer.substring(1, 2).equals(as2[k]) && dictdata.isChineseWord(stringbuffer.substring(0, 1) + stringbuffer.substring(2, 3)))
            {
                System.out.println("Stemmed infix");
                stringbuffer.deleteCharAt(1);
                return stringbuffer.toString();
            }

        return stringbuffer.toString();
    }

    public String segmentLine(String s, String s1)
    {
        int ai[] = segmentLineOffsets(s);
        StringBuffer stringbuffer = new StringBuffer(s);
        int j = s1.length();
        if(ai.length == 0)
            return s;
        for(int i = ai.length - 2; i >= 0; i--)
            if(ai[i] > 0 && i + ai[i] != s.length() && !s.substring(i, i + j).equals(s1) && !s.substring(i + ai[i], i + ai[i] + j).equals(s1))
                stringbuffer.insert(i + ai[i], s1);

        return stringbuffer.toString();
    }

    public LinkedList segmentLine(String s)
    {
        int ai[] = segmentLineOffsets(s);
        LinkedList linkedlist = new LinkedList();
        for(int i = 0; i < ai.length; i++)
            if(ai[i] > 0)
                linkedlist.add(new Integer(i));

        return linkedlist;
    }

    public int[] segmentLineOffsets(String s)
    {
        int j2 = s.length();
        int ai[] = new int[j2];
        if(debug)
            System.out.println("Line length " + j2);
        if(debug)
            System.out.println("Grouping Chinese, letters, digits and spaces");
        for(int i = 0; i < j2;)
        {
            if(debug)
                System.out.println("i " + i);
            if(Character.UnicodeBlock.of(s.charAt(i)) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)
            {
                int l = 8;
                if(i + l > j2)
                    l = j2 - i;
                for(; i + l <= j2 && l > 1 && !dictdata.isChineseWord(s.substring(i, i + l)); l--);
                ai[i] = l;
                i += l;
            } else
            if(Character.isWhitespace(s.charAt(i)))
            {
                int i1;
                for(i1 = 1; i + i1 < j2 && Character.isWhitespace(s.charAt(i + i1)); i1++);
                ai[i] = i1;
                i += i1;
            } else
            if(Character.isLetter(s.charAt(i)))
            {
                int j1;
                for(j1 = 1; i + j1 < j2 && Character.isLetter(s.charAt(i + j1)) && Character.UnicodeBlock.of(s.charAt(i + j1)) != Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS; j1++);
                ai[i] = j1;
                i += j1;
            } else
            if(Character.isDigit(s.charAt(i)))
            {
                int k1;
                for(k1 = 1; i + k1 < j2 && Character.isDigit(s.charAt(i + k1)); k1++);
                ai[i] = k1;
                i += k1;
            } else
            {
                ai[i] = 1;
                i++;
            }
        }

        if(debug)
            System.out.println("Grouping foreign transliterations");
        for(int j = 0; j < j2; j++)
        {
            if(ai[j] <= 0)
                continue;
            int l1;
            for(; j + ai[j] < j2 && j + ai[j] + ai[j + ai[j]] <= j2 && ai[j + ai[j]] == 1 && dictdata.isAllForeign(s.substring(j, j + ai[j] + ai[j + ai[j]])); ai[j] = ai[j] + l1)
            {
                l1 = ai[j + ai[j]];
                ai[j + ai[j]] = 0;
            }

        }

        if(debug)
            System.out.println("Grouping numbers");
        for(int k = 0; k < j2; k++)
        {
            if(ai[k] <= 0)
                continue;
            int i2;
            for(; k + ai[k] < j2 && k + ai[k] + ai[k + ai[k]] <= j2 && dictdata.isNumber(s.substring(k, k + ai[k] + ai[k + ai[k]])); ai[k] = ai[k] + i2)
            {
                i2 = ai[k + ai[k]];
                ai[k + ai[k]] = 0;
            }

        }

        return ai;
    }

    public int lexiconSize()
    {
        return dictdata.lexiconSize();
    }

    public void segmentFile(String s, String s1)
    {
        String s2 = s + ".seg";
        boolean flag = false;
        try
        {
            FileInputStream fileinputstream = new FileInputStream(s);
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(fileinputstream, s1));
            BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(s2), s1));
            String s4;
            while((s4 = bufferedreader.readLine()) != null) 
            {
                String s3 = segmentLine(s4, " ");
                if(flag)
                    printDebug("Output: " + s3);
                bufferedwriter.write(s3);
                bufferedwriter.newLine();
            }
            bufferedreader.close();
            bufferedwriter.close();
        }
        catch(Exception exception)
        {
            System.err.println("Exception " + exception.toString());
        }
    }

    public void printDebug(String s)
    {
        try
        {
            System.out.println(new String(s.getBytes(debugencoding)));
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public static void printHelp()
    {
        System.out.println("Usage:\njava -jar segmenter.jar [-b|-g|-8|-s|-t] inputfile.txt");
        System.out.println("\t-b Big5, -g GB2312, -8 UTF-8, -s simp. chars, -t trad. chars");
        System.out.println("  Segmented text will be saved to inputfile.txt.seg");
        System.exit(0);
    }

    public static void main(String args[])
    {
        Vector vector = new Vector();
        String s = "BIG5";
        boolean flag = false;
        boolean flag5 = false;
        for(int i = 0; i < args.length; i++)
        {
            if(args[i].equals("-b"))
            {
                if(flag5)
                    System.out.println("Setting to Big5, TRAD");
                s = "BIG5";
                boolean flag1 = false;
                continue;
            }
            if(args[i].equals("-g"))
            {
                if(flag5)
                    System.out.println("Setting to GB, SIMP");
                s = "GBK";
                boolean flag2 = true;
                continue;
            }
            if(args[i].equals("-8"))
            {
                s = "UTF8";
                byte byte0 = 2;
                continue;
            }
            if(args[i].equals("-s"))
            {
                if(flag5)
                    System.out.println("Setting to UTF-8 SIMP");
                s = "UTF8";
                boolean flag3 = true;
                continue;
            }
            if(args[i].equals("-t"))
            {
                if(flag5)
                    System.out.println("Setting to UTF-8 TRAD");
                s = "UTF8";
                boolean flag4 = false;
                continue;
            }
            if(args[i].equals("-h"))
            {
                printHelp();
                continue;
            }
            if(args[i].equals("-d"))
                flag5 = true;
            else
                vector.add(args[i]);
        }

        if(vector.size() == 0)
        {
            System.out.println("ERROR: Please specify name of Chinese text file to segment.\n");
            printHelp();
        }
        System.err.println("Loading segmenter word list.  One moment please.");
        Segmenter segmenter1 = new Segmenter(null);
        System.err.println("Total keys " + segmenter1.lexiconSize());
        for(int j = 0; j < vector.size(); j++)
        {
            File file = new File((String)vector.get(j));
            if(!file.exists())
            {
                System.out.println("ERROR: Source file " + (String)vector.get(j) + " does not exist.\n");
                continue;
            }
            if(file.isDirectory())
            {
                String args1[] = file.list();
                if(args1 == null)
                    continue;
                for(int k = 0; k < args1.length; k++)
                    vector.add((String)vector.get(j) + File.separator + args1[k]);

            } else
            {
                System.err.println("Segmenting " + vector.get(j) + " with encoding " + s);
                segmenter1.segmentFile((String)vector.get(j), s);
            }
        }

    }

}
