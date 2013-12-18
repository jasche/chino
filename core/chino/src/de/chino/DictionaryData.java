package de.chino;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.Player;

public class DictionaryData
{
    class PlaySoundThread extends Thread
    {

        public void run()
        {
            pronounce(soundurl, pinyin);
        }

        String soundurl;
        String pinyin;

        public PlaySoundThread(String s, String s1)
        {
            soundurl = s;
            pinyin = s1;
        }
    }

    class PinyinComparator
        implements Comparator
    {

        public int compare(Object obj, Object obj1)
        {
            return getPinyin((String)obj).toUpperCase().compareTo(getPinyin((String)obj1).toUpperCase());
        }

        PinyinComparator()
        {
        }
    }


    public DictionaryData()
    {
        radnum2charKX = new Hashtable(250);
        gbhash = new Hashtable(6000);
        big5hash = new Hashtable(13000);
        simpEntries = new TreeMap();
        tradEntries = new TreeMap();
        allEntries = new TreeMap();
        charEntries = new TreeMap();
        loadDictionary("cedict_ds.u8");
        loadCharDictionary();
        loadKXRadicals();
        loadCASSRadicals();
        loadPY();
        loadAnimations();
        csurname = new TreeSet();
        cforeign = new TreeSet();
        cnumbers = new TreeSet();
        cnotname = new TreeSet();
        loadset(cnumbers, "data/snumbers_u8.txt");
        loadset(cforeign, "data/sforeign_u8.txt");
        loadset(csurname, "data/ssurname_u8.txt");
        loadset(cnotname, "data/snotname_u8.txt");
        loadset(cnumbers, "data/tnumbers_u8.txt");
        loadset(cforeign, "data/tforeign_u8.txt");
        loadset(csurname, "data/tsurname_u8.txt");
        loadset(cnotname, "data/tnotname_u8.txt");
    }

    private void loadset(TreeSet treeset, String s)
    {
        try
        {
            InputStream inputstream = ClassLoader.getSystemResourceAsStream(s);
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

    private void loadAnimations()
    {
        animationSet = new HashSet();
        try
        {
            InputStream inputstream = ClassLoader.getSystemResourceAsStream("strokedata.txt");
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, "UTF8"));
            do
            {
                String s;
                if((s = bufferedreader.readLine()) == null)
                    break;
                String as[] = s.split("\t");
                if(as.length > 1)
                    animationSet.add(as[0]);
            } while(true);
            bufferedreader.close();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public boolean hasAnimation(String s)
    {
        return animationSet.contains(s);
    }

    public String getAnimation(String s)
    {
        if(!hasAnimation(s))
            return "";
        try
        {
        
        	InputStream inputstream = getClass().getResourceAsStream("strokedata.txt");
        	BufferedReader  bufferedreader = new BufferedReader(new InputStreamReader(inputstream, "UTF8"));

        	String s1;
            while ((s1 = bufferedreader.readLine()) != null) {
            	String[] arrayOfString = s1.split("\t");
            	if (arrayOfString[0].equals(s)) {
            			return arrayOfString[1];
            	}
            }
            bufferedreader.close();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        return "";
    }

    public static boolean isChinese(String s)
    {
        boolean flag = true;
        for(int i = 0; i < s.length(); i++)
            if(Character.UnicodeBlock.of(s.charAt(i)) != Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)
                flag = false;

        return flag;
    }

    public boolean isChineseWord(String s, int i)
    {
        if(s == null)
            return false;
        if(s.length() == 1 && charEntries.containsKey(s))
        {
            int j = ((Integer)charEntries.get(s)).intValue();
            if(i == 1 && !characterData[j][9].equals("-"))
                return true;
            if(i == 0 && !characterData[j][8].equals("-"))
                return true;
            return i == 2;
        }
        if(i == 1)
            return simpEntries.containsKey(s);
        if(i == 0)
            return tradEntries.containsKey(s);
        else
            return simpEntries.containsKey(s) || tradEntries.containsKey(s);
    }

    public boolean isChineseWord(String s)
    {
        return simpEntries.containsKey(s) || tradEntries.containsKey(s);
    }

    public boolean isChineseSurname(String s)
    {
        return csurname.contains(s);
    }

    public int lexiconSize()
    {
        return dictionaryTable.length;
    }

    public void sortByPinyin(Vector vector)
    {
        Collections.sort(vector, new PinyinComparator());
    }

    public void loadDictionary(String s)
    {
        entrycount = 0;
        try
        {
            InputStream inputstream = ClassLoader.getSystemResourceAsStream(s);
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, "UTF8"));
            entrycount = 0;
            String s1 = bufferedreader.readLine();
            if(s1.indexOf("Entries: ") > -1)
            {
                int l = s1.indexOf("Entries: ") + "Entries: ".length();
                int i1;
                for(i1 = l; i1 < s1.length() && Character.isDigit(s1.charAt(i1)); i1++);
                entrycount = Integer.parseInt(s1.substring(l, i1));
            } else
            {
                bufferedreader.mark(0x895440);
                do
                {
                    String s2;
                    if((s2 = bufferedreader.readLine()) == null)
                        break;
                    if(s2.indexOf("#") <= -1 && s2.length() != 0)
                        entrycount++;
                } while(true);
                bufferedreader.reset();
            }
            System.out.println("Adding dictionary " + s + " with entry count " + entrycount);
            dictionaryTable = new String[entrycount][4];
            entrycount = 0;
            do
            {
                String s3;
                if((s3 = bufferedreader.readLine()) == null || entrycount >= dictionaryTable.length)
                    break;
                if(s3.indexOf("#") <= -1 && s3.length() != 0)
                {
                    int i = s3.length();
                    int j;
                    for(j = 0; j < i && !Character.isWhitespace(s3.charAt(j)); j++);
                    dictionaryTable[entrycount][0] = s3.substring(0, j);
                    int k = j + 1;
                    for(j = k; j < i && !Character.isWhitespace(s3.charAt(j)); j++);
                    dictionaryTable[entrycount][1] = s3.substring(k, j);
                    k = j + 2;
                    for(j += 2; j < i && s3.charAt(j) != ']'; j++);
                    dictionaryTable[entrycount][2] = s3.substring(k, j);
                    if(j + 2 < i)
                        dictionaryTable[entrycount][3] = s3.substring(j + 2, i).replace('\'', '`');
                    if(!simpEntries.containsKey(dictionaryTable[entrycount][1]))
                    {
                        Vector vector = new Vector();
                        vector.add(new Integer(entrycount));
                        simpEntries.put(dictionaryTable[entrycount][1], vector);
                    } else
                    {
                        Vector vector1 = (Vector)simpEntries.get(dictionaryTable[entrycount][1]);
                        vector1.add(new Integer(entrycount));
                    }
                    if(!tradEntries.containsKey(dictionaryTable[entrycount][0]))
                    {
                        Vector vector2 = new Vector();
                        vector2.add(new Integer(entrycount));
                        tradEntries.put(dictionaryTable[entrycount][0], vector2);
                    } else
                    {
                        Vector vector3 = (Vector)tradEntries.get(dictionaryTable[entrycount][0]);
                        vector3.add(new Integer(entrycount));
                    }
                    if(!allEntries.containsKey(dictionaryTable[entrycount][1]))
                    {
                        Vector vector4 = new Vector();
                        vector4.add(new Integer(entrycount));
                        allEntries.put(dictionaryTable[entrycount][1], vector4);
                    } else
                    {
                        Vector vector5 = (Vector)allEntries.get(dictionaryTable[entrycount][1]);
                        vector5.add(new Integer(entrycount));
                    }
                    if(!dictionaryTable[entrycount][1].equals(dictionaryTable[entrycount][0]))
                        if(!allEntries.containsKey(dictionaryTable[entrycount][0]))
                        {
                            Vector vector6 = new Vector();
                            vector6.add(new Integer(entrycount));
                            allEntries.put(dictionaryTable[entrycount][0], vector6);
                        } else
                        {
                            Vector vector7 = (Vector)allEntries.get(dictionaryTable[entrycount][0]);
                            vector7.add(new Integer(entrycount));
                        }
                    entrycount++;
                }
            } while(true);
            bufferedreader.close();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void loadPY()
    {
        int i = 0;
        pyhash = new TreeMap();
        try
        {
            InputStream inputstream = ClassLoader.getSystemResourceAsStream("uni8py.txt");
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, "UTF8"));
            String s;
            do
            {
                s = bufferedreader.readLine();
                if(s != null)
                {
                    String as[] = new String[3];
                    as[2] = Integer.toString(i / 400);
                    as[1] = s.substring(2, s.length());
                    if(s.lastIndexOf(' ') == -1)
                        as[0] = as[1];
                    else
                        as[0] = as[1].substring(0, as[1].indexOf(' '));
                    pyhash.put(s.substring(0, 1), as);
                    i++;
                }
            } while(s != null);
        }
        catch(IOException ioexception)
        {
            System.err.println("IOException:" + ioexception);
        }
    }

    public String[] getCharacterPY(String s)
    {
        String as[] = (String[])pyhash.get(s);
        if(as == null)
            return null;
        else
            return as;
    }

    public int getCharacterStrokes(String s)
    {
        if(s != null && charEntries.containsKey(s))
        {
            int i = ((Integer)charEntries.get(s)).intValue();
            return Integer.parseInt(characterData[i][2]);
        } else
        {
            return -1;
        }
    }

    public String getCharacterKXRadical(String s)
    {
        if(s != null && charEntries.containsKey(s))
        {
            int i = ((Integer)charEntries.get(s)).intValue();
            String s1 = characterData[i][1];
            String s2 = s1.substring(0, s1.indexOf("."));
            return (String)radnum2charKX.get(s2);
        } else
        {
            return null;
        }
    }

    public String[] getCharactersWithKXRadical(String s)
    {
        Vector vector = new Vector();
        boolean flag = true;
        String s1;
        if(s != null && (s1 = getKXRadicalIndex(s)) != null)
        {
            Integer integer = (Integer)firstRadical.get(s1);
            for(int i = integer.intValue(); flag; i++)
                if(characterData[i][1].startsWith(s1 + ".") || characterData[i][1].startsWith(s1 + "'."))
                {
                    String s2 = characterData[i][1];
                    String s3 = s2.substring(0, s2.indexOf("."));
                    String s4 = s2.substring(s2.indexOf(".") + 1, s2.length());
                    vector.add("" + (char)Integer.parseInt(characterData[i][0], 16));
                } else
                {
                    flag = false;
                }

            return (String[])vector.toArray(new String[vector.size()]);
        } else
        {
            return null;
        }
    }

    public int getCharacterKXRemainingStrokes(String s)
    {
        if(s != null && charEntries.containsKey(s))
        {
            int i = ((Integer)charEntries.get(s)).intValue();
            String s1 = characterData[i][1];
            String s2 = s1.substring(s1.indexOf(".") + 1, s1.length());
            return Integer.parseInt(s2);
        } else
        {
            return -1;
        }
    }

    private void loadCharDictionary()
    {
        String s3 = "";
        char ac[] = new char[1];
        Vector vector = new Vector();
        firstRadical = new Hashtable();
        boolean flag = false;
        System.out.println("Loading character information");
        if(characterData == null)
            try
            {
                InputStream inputstream = ClassLoader.getSystemResourceAsStream("UNICHIN_IDS.TXT");
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, "UTF8"));
                do
                {
                    String s4;
                    if((s4 = bufferedreader.readLine()) == null)
                        break;
                    if(s4.length() != 0 && s4.charAt(0) != '#')
                    {
                        String as[] = s4.split("\t");
                        if(as.length >= 11)
                        {
                            as[10] = as[10].replaceAll("[\u2FF0-\u2FFF]", "");
                            as[3] = as[3].toLowerCase();
                            vector.add(as);
                        }
                    }
                } while(true);
                characterData = new String[vector.size()][11];
                for(int i = 0; i < vector.size(); i++)
                {
                    String as1[] = (String[])vector.get(i);
                    for(int k = 0; k < 11; k++)
                        characterData[i][k] = as1[k];

                }

                vector = null;
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        for(int j = 0; j < characterData.length; j++)
        {
            ac[0] = (char)Integer.parseInt(characterData[j][0], 16);
            String s = new String(ac);
            charEntries.put(s, new Integer(j));
            if(!characterData[j][9].equals("-"))
                gbhash.put(s, characterData[j][9]);
            if(!characterData[j][8].equals("-"))
                big5hash.put(s, characterData[j][8]);
            String s1;
            if(characterData[j][1].indexOf("'") == -1)
                s1 = characterData[j][1].substring(0, characterData[j][1].indexOf("."));
            else
                s1 = characterData[j][1].substring(0, characterData[j][1].indexOf("'"));
            String s2 = characterData[j][1].substring(characterData[j][1].indexOf(".") + 1, characterData[j][1].length());
            if(!s3.equals(s1))
                firstRadical.put(s1, new Integer(j));
            s3 = s1;
        }

    }

    private void loadKXRadicals()
    {
        int j = 1;
        radicalTableKX = new String[214][4];
        boolean flag = false;
        try
        {
            InputStream inputstream = ClassLoader.getSystemResourceAsStream("kxradicals.u8");
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, "UTF8"));
            String s;
            while((s = bufferedreader.readLine()) != null) 
            {
                String as[] = s.split("\t");
                radicalTableKX[j - 1][0] = as[1];
                radicalTableKX[j - 1][1] = as[0];
                radicalTableKX[j - 1][2] = as[2];
                radicalTableKX[j - 1][3] = as[3];
                String as1[] = as[1].split(" ");
                for(int i = 0; i < as1.length; i++)
                {
                    radicalIndexKX.put(as1[i], Integer.toString(j));
                    radicalPYKX.put(as1[i], as[2]);
                }

                j++;
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        try
        {
            InputStream inputstream1 = ClassLoader.getSystemResourceAsStream("radicals.txt");
            BufferedReader bufferedreader1 = new BufferedReader(new InputStreamReader(inputstream1, "UTF8"));
            String s1;
            while((s1 = bufferedreader1.readLine()) != null) 
            {
                String s3 = s1.substring(0, s1.indexOf("\t"));
                String s2 = s1.substring(s1.indexOf("\t") + 1, s1.length());
                radnum2charKX.put(s3, s2);
            }
        }
        catch(Exception exception1)
        {
            exception1.printStackTrace();
        }
    }

    public String getKXRadicals(int i)
    {
        if(i < 1 || i > 214)
            return null;
        else
            return radicalTableKX[i - 1][0];
    }

    public String getKXRadical(int i)
    {
        if(i < 1 || i > 214)
            return null;
        else
            return (String)radnum2charKX.get(Integer.toString(i));
    }

    public String getKXRadical(String s)
    {
        int i = Integer.parseInt(s.replace('\'', ' ').trim());
        if(i < 1 || i > 214)
            return null;
        else
            return (String)radnum2charKX.get(Integer.toString(i));
    }

    public String getKXRadicalIndex(String s)
    {
        if(radicalIndexKX.containsKey(s))
            return (String)radicalIndexKX.get(s);
        else
            return null;
    }

    public int getKXRadicalStrokes(int i)
    {
        if(i < 1 || i > 214)
            return 0;
        else
            return Integer.parseInt(radicalTableKX[i - 1][1]);
    }

    public int getKXRadicalStrokes(String s)
    {
        int i = Integer.parseInt(s);
        if(i < 1 || i > 214)
            return 0;
        else
            return Integer.parseInt(radicalTableKX[i - 1][1]);
    }

    public String getKXRadicalPinyin(String s)
    {
        int i = Integer.parseInt(getKXRadicalIndex(s));
        if(i < 1 || i > 214)
            return null;
        else
            return radicalTableKX[i - 1][2];
    }

    public int getKXRadicalCount()
    {
        return 214;
    }

    private void loadCASSRadicals()
    {
    }

    boolean isAllSimp(String s)
    {
        boolean flag = true;
        int i = 0;
        do
        {
            if(i >= s.length())
                break;
            if(!gbhash.containsKey(s.substring(i, i + 1)))
            {
                flag = false;
                break;
            }
            i++;
        } while(true);
        return flag;
    }

    boolean isAllTrad(String s)
    {
        boolean flag = true;
        int i = 0;
        do
        {
            if(i >= s.length())
                break;
            if(!big5hash.containsKey(s.substring(i, i + 1)))
            {
                flag = false;
                break;
            }
            i++;
        } while(true);
        return flag;
    }

    public String getPinyin(String s)
    {
        StringBuffer stringbuffer = new StringBuffer();
        Vector vector;
        if((vector = (Vector)allEntries.get(s)) != null)
        {
            Integer integer = (Integer)vector.get(0);
            return dictionaryTable[integer.intValue()][2];
        }
        if(Character.UnicodeBlock.of(s.charAt(0)) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)
        {
            int i = s.length();
            for(int j = 0; j < i; j++)
            {
                String as[];
                if((as = getCharacterPY(s.substring(j, j + 1))) != null)
                    stringbuffer.append(as[0]);
                if(j + 1 < i)
                    stringbuffer.append(" ");
            }

        }
        return stringbuffer.toString();
    }

    public void pronounce(String s, String s1)
    {
        System.out.println("playing " + s1 + "...");
        String s2 = "jar:" + s + "!/";
        String as[] = s1.toLowerCase().split(",?\\s+");
        for(int i = 0; i < as.length; i++)
            try
            {
                URL url = new URL(s2 + as[i] + ".mp3");
                InputStream inputstream = url.openStream();
                BufferedInputStream bufferedinputstream = new BufferedInputStream(inputstream);
                javazoom.jl.player.AudioDevice audiodevice = FactoryRegistry.systemRegistry().createAudioDevice();
                Player player = new Player(bufferedinputstream, audiodevice);
                player.play();
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }

    }

    public void pronounceThreaded(String s, String s1)
    {
        PlaySoundThread playsoundthread = new PlaySoundThread(s, s1);
        playsoundthread.start();
    }

    public String[] getDefinition(String s)
    {
        String as[] = null;
        if(s == null)
            return null;
        Vector vector;
        if((vector = (Vector)allEntries.get(s)) != null)
        {
            as = new String[1 + 2 * vector.size()];
            as[0] = s;
            for(int k1 = 0; k1 < vector.size(); k1++)
            {
                Integer integer = (Integer)vector.get(k1);
                as[1 + 2 * k1] = dictionaryTable[integer.intValue()][2];
                as[2 + 2 * k1] = dictionaryTable[integer.intValue()][3];
            }

        } else
        if(isNumber(s))
        {
            int i = s.length();
            StringBuffer stringbuffer = new StringBuffer();
            for(int l = 0; l < i; l++)
                if((as = (String[])pyhash.get(s.substring(l, l + 1))) != null)
                    stringbuffer.append(as[0] + " ");

            as = new String[3];
            as[0] = s;
            as[1] = stringbuffer.toString();
            String s1 = ChineseNumbers.ChineseToEnglishNumber(s);
            as[2] = "/" + s1 + "/";
        } else
        if(isAllForeign(s))
        {
            int j = s.length();
            StringBuffer stringbuffer1 = new StringBuffer();
            for(int i1 = 0; i1 < j; i1++)
                if((as = (String[])pyhash.get(s.substring(i1, i1 + 1))) != null)
                    stringbuffer1.append(as[0] + " ");

            as = new String[3];
            as[0] = s;
            as[1] = stringbuffer1.toString();
            as[2] = "/non-Chinese name or place/";
        } else
        if(Character.UnicodeBlock.of(s.charAt(0)) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)
        {
            int k = s.length();
            StringBuffer stringbuffer2 = new StringBuffer();
            for(int j1 = 0; j1 < k; j1++)
                if((as = (String[])pyhash.get(s.substring(j1, j1 + 1))) != null)
                    stringbuffer2.append(as[0] + " ");

            as = new String[3];
            as[0] = s;
            as[1] = stringbuffer2.toString();
            as[2] = "/??/";
        }
        return as;
    }

    public String[] getCharacterData(String s)
    {
        if(s != null && charEntries.containsKey(s))
        {
            String as[] = new String[11];
            int i = ((Integer)charEntries.get(s)).intValue();
            for(int j = 0; j < 11; j++)
                as[j] = characterData[i][j];

            return as;
        } else
        {
            return null;
        }
    }

    public String[][] searchDictionary(String s, int i, int j)
    {
        boolean flag = true;
        Vector vector = new Vector();
        s = s.trim();
        if(s.length() == 0)
            return (String[][])null;
        if(i == 3)
        {
            i = 2;
            for(int k = 0; k < s.length(); k++)
                if(Character.UnicodeBlock.of(s.charAt(k)) != Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)
                    flag = false;

            if(flag)
            {
                i = 0;
            } else
            {
                boolean flag2 = false;
                for(int l = 0; l < s.length() - 1; l++)
                    if((Character.UnicodeBlock.of(s.charAt(l)) == Character.UnicodeBlock.BASIC_LATIN && Character.isLetter(s.charAt(l)) || s.charAt(l) == ':') && Character.UnicodeBlock.of(s.charAt(l + 1)) == Character.UnicodeBlock.BASIC_LATIN && Character.isDigit(s.charAt(l + 1)))
                        flag2 = true;

                if(flag2)
                    i = 1;
                else
                    i = 2;
            }
        }
        if(i == 0)
            if(j == 0)
                s = "^" + s;
            else
            if(j == 1)
                s = s + "$";
            else
            if(j == 3)
                s = "^" + s + "$";
            else
            if(j != 2);
        if(i == 1)
        {
            String as[] = s.split(" ");
            StringBuffer stringbuffer = new StringBuffer();
            for(int i1 = 0; i1 < as.length; i1++)
            {
                if(Character.isDigit(as[i1].charAt(as[i1].length() - 1)))
                {
                    stringbuffer.append(as[i1]);
                } else
                {
                    stringbuffer.append(as[i1]);
                    stringbuffer.append("[1-5]");
                }
                if(i1 + 1 < as.length)
                    stringbuffer.append("\\s+");
            }

            int l1;
            while((l1 = stringbuffer.indexOf("\374")) != -1) 
                stringbuffer.replace(l1, l1 + 1, "u:");
            while((l1 = stringbuffer.indexOf("v")) != -1) 
                stringbuffer.replace(l1, l1 + 1, "u:");
            if(j == 0)
                stringbuffer.insert(0, "^");
            else
            if(j == 1)
                stringbuffer.append("$");
            else
            if(j != 2 && j == 3)
            {
                stringbuffer.insert(0, "^");
                stringbuffer.append("$");
            }
            s = stringbuffer.toString();
        }
        if(i == 2)
            if(j == 0)
                s = "/" + s;
            else
            if(j == 1)
                s = s + "/";
            else
            if(j == 3)
                s = "/" + s + "/";
            else
            if(j != 2);
        Pattern pattern = Pattern.compile(s, 2);
        for(int j1 = 0; j1 < dictionaryTable.length && dictionaryTable[j1] != null && dictionaryTable[j1][0] != null; j1++)
        {
            boolean flag1 = false;
            if(i == 0 && pattern.matcher(dictionaryTable[j1][0]).find())
                flag1 = true;
            else
            if(i == 0 && pattern.matcher(dictionaryTable[j1][1]).find())
                flag1 = true;
            else
            if(i == 0 && (pattern.matcher(dictionaryTable[j1][0]).find() || pattern.matcher(dictionaryTable[j1][1]).find()))
                flag1 = true;
            else
            if(i == 1 && pattern.matcher(dictionaryTable[j1][2]).find())
                flag1 = true;
            else
            if(i == 2 && pattern.matcher(dictionaryTable[j1][3]).find())
                flag1 = true;
            if(flag1)
                vector.add(new Integer(j1));
        }

        if(vector.size() == 0)
            return (String[][])null;
        String as1[][] = new String[vector.size()][4];
        for(int k1 = 0; k1 < vector.size(); k1++)
        {
            int i2 = ((Integer)vector.elementAt(k1)).intValue();
            as1[k1][0] = dictionaryTable[i2][0];
            as1[k1][1] = dictionaryTable[i2][1];
            as1[k1][2] = dictionaryTable[i2][2];
            as1[k1][3] = dictionaryTable[i2][3];
        }

        return as1;
    }

    public String getSimpVariant(String s)
    {
        Vector vector;
        if((vector = (Vector)tradEntries.get(s)) != null)
        {
            Integer integer = (Integer)vector.get(0);
            return dictionaryTable[integer.intValue()][1];
        }
        StringBuffer stringbuffer = new StringBuffer();
        for(int i = 0; i < s.length(); i++)
        {
            Vector vector1;
            if((vector1 = (Vector)tradEntries.get(s.substring(i, i + 1))) != null)
            {
                Integer integer1 = (Integer)vector1.get(0);
                stringbuffer.append(dictionaryTable[integer1.intValue()][1]);
            } else
            {
                stringbuffer.append(s.substring(i, i + 1));
            }
        }

        return stringbuffer.toString();
    }

    public String getTradVariant(String s)
    {
        Vector vector;
        if((vector = (Vector)simpEntries.get(s)) != null)
        {
            Integer integer = (Integer)vector.get(0);
            return dictionaryTable[integer.intValue()][0];
        }
        StringBuffer stringbuffer = new StringBuffer();
        for(int i = 0; i < s.length(); i++)
        {
            Vector vector1;
            if((vector1 = (Vector)simpEntries.get(s.substring(i, i + 1))) != null)
            {
                Integer integer1 = (Integer)vector1.get(0);
                stringbuffer.append(dictionaryTable[integer1.intValue()][0]);
            } else
            {
                stringbuffer.append(s.substring(i, i + 1));
            }
        }

        return stringbuffer.toString();
    }

    public String getCharByGB(String s)
    {
        for(int i = 0; i < characterData.length; i++)
            if(s.equalsIgnoreCase(characterData[i][9]))
                return "" + (char)Integer.parseInt(characterData[i][0], 16);

        return null;
    }

    public String getCharByBig5(String s)
    {
        for(int i = 0; i < characterData.length; i++)
            if(s.equalsIgnoreCase(characterData[i][8]))
                return "" + (char)Integer.parseInt(characterData[i][0], 16);

        return null;
    }

    public String getCharByUnicode(String s)
    {
        for(int i = 0; i < characterData.length; i++)
            if(s.equalsIgnoreCase(characterData[i][0]))
                return "" + (char)Integer.parseInt(characterData[i][0], 16);

        return null;
    }

    public String[] searchCantonese(String s)
    {
        Vector vector = new Vector();
        String s1 = s.toUpperCase();
        for(int i = 0; i < characterData.length; i++)
            if(characterData[i][5].indexOf(s1) != -1)
                vector.add("" + (char)Integer.parseInt(characterData[i][0], 16));

        if(vector.size() == 0)
            return null;
        else
            return (String[])vector.toArray(new String[1]);
    }

    public String[] searchStrokeCount(String s)
    {
        Vector vector = new Vector();
        for(int i = 0; i < characterData.length; i++)
            if(s.equalsIgnoreCase(characterData[i][2]))
                vector.add("" + (char)Integer.parseInt(characterData[i][0], 16));

        if(vector.size() == 0)
            return null;
        else
            return (String[])vector.toArray(new String[1]);
    }

    public String[] searchComponents(String s)
    {
        Vector vector = new Vector();
        for(int i = 0; i < characterData.length; i++)
            if(characterData[i][10].indexOf(s) != -1)
                vector.add("" + (char)Integer.parseInt(characterData[i][0], 16));

        if(vector.size() == 0)
            return null;
        else
            return (String[])vector.toArray(new String[1]);
    }

    public boolean isNumber(String s)
    {
        boolean flag = true;
        int i = 0;
        do
        {
            if(i >= s.length())
                break;
            if(!cnumbers.contains(s.substring(i, i + 1)))
            {
                flag = false;
                break;
            }
            i++;
        } while(true);
        if(!debug);
        return flag;
    }

    public boolean isAllForeign(String s)
    {
        boolean flag = true;
        int i = 0;
        do
        {
            if(i >= s.length())
                break;
            if(!cforeign.contains(s.substring(i, i + 1)))
            {
                flag = false;
                break;
            }
            i++;
        } while(true);
        return flag;
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

    public int characterCount(String s)
    {
        int i = 0;
        int k = s.length();
        for(int j = 0; j < k; j++)
            if(Character.UnicodeBlock.of(s.charAt(j)) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)
                i++;

        return i;
    }

    public ArrayList countWords(String s)
    {
        HashMap hashmap = new HashMap();
        String s2 = jseg.segmentLine(s, " ");
        for(StringTokenizer stringtokenizer = new StringTokenizer(s2, " \t\n\u3000", false); stringtokenizer.hasMoreTokens();)
        {
            String s1 = stringtokenizer.nextToken();
            if(hashmap.containsKey(s1))
            {
                Integer integer = (Integer)hashmap.get(s1);
                hashmap.put(s1, new Integer(integer.intValue() + 1));
            } else
            {
                hashmap.put(s1, new Integer(1));
            }
        }

        Iterator iterator = hashmap.entrySet().iterator();
        boolean flag = false;
        ArrayList arraylist = new ArrayList(500);
        java.util.Map.Entry entry;
        for(; iterator.hasNext(); arraylist.add(entry.getKey()))
            entry = (java.util.Map.Entry)iterator.next();

        for(int i = 0; i < arraylist.size(); i++)
        {
            for(int j = 0; j < arraylist.size() - 1; j++)
                if(((Integer)hashmap.get((String)arraylist.get(j))).intValue() < ((Integer)hashmap.get((String)arraylist.get(j + 1))).intValue())
                {
                    Object obj = arraylist.get(j);
                    arraylist.set(j, arraylist.get(j + 1));
                    arraylist.set(j + 1, obj);
                }

        }

        return arraylist;
    }

    public HashMap getCharacterCount(String s)
    {
        HashMap hashmap = new HashMap();
        int j = s.length();
        for(int i = 0; i < j; i++)
        {
            if(Character.UnicodeBlock.of(s.charAt(i)) != Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)
                continue;
            String s1 = s.substring(i, i + 1);
            if(hashmap.containsKey(s1))
            {
                Integer integer = (Integer)hashmap.get(s1);
                hashmap.put(s1, new Integer(integer.intValue() + 1));
            } else
            {
                hashmap.put(s1, new Integer(1));
            }
        }

        return hashmap;
    }

    public HashMap getWordCount(String s, int ai[])
    {
        HashMap hashmap = new HashMap();
        if(s == null || ai == null || s.length() == 0)
            return null;
        int j = s.length();
        for(int i = 0; i < j && i < ai.length; i++)
        {
            if(ai[i] <= 0)
                continue;
            String s1 = s.substring(i, i + ai[i]);
            if(!isChinese(s1))
                continue;
            if(hashmap.containsKey(s1))
            {
                Integer integer = (Integer)hashmap.get(s1);
                hashmap.put(s1, new Integer(integer.intValue() + 1));
            } else
            {
                hashmap.put(s1, new Integer(1));
            }
        }

        return hashmap;
    }

    public static void main(String args[])
    {
        DictionaryData dictionarydata = new DictionaryData();
        try
        {
            System.in.read();
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    private TreeMap simpEntries;
    private TreeMap tradEntries;
    private TreeMap allEntries;
    private TreeMap charEntries;
    private int entrycount;
    private boolean debug;
    private String dictionaryTable[][];
    private String characterData[][];
    private String radicalTableKX[][];
    private String radicalTableCASS[][];
    private TreeMap pyhash;
    private Hashtable gbhash;
    private Hashtable big5hash;
    private Hashtable radnum2charKX;
    final Hashtable radicalIndexKX = new Hashtable();
    final Hashtable radicalIndexCASS = new Hashtable();
    final Hashtable radicalPYKX = new Hashtable(214);
    final Hashtable radicalPYCASS = new Hashtable(189);
    private Hashtable firstRadical;
    private TreeSet csurname;
    private TreeSet cforeign;
    private TreeSet cnumbers;
    private TreeSet cnotname;
    public HashSet animationSet;
    public final Segmenter jseg = new Segmenter(this);
    public final PinyinConvert pyconverter = new PinyinConvert();
    public final FlashLists sl = new FlashLists();
    public static final int TRAD = 0;
    public static final int SIMP = 1;
    public static final int BOTH = 2;
    public static final int TCHINESE = 0;
    public static final int SCHINESE = 1;
    public static final int PINYIN = 2;
    public static final int ENGLISH = 3;
    public static final int UNICODE = 0;
    public static final int RADSTROKE = 1;
    public static final int STROKES = 2;
    public static final int CHAR_PINYIN = 3;
    public static final int CHAR_ENGLISH = 4;
    public static final int CANTONESE = 5;
    public static final int VARIANT = 6;
    public static final int FREQ = 7;
    public static final int BIG5 = 8;
    public static final int GB = 9;
    public static final int IDS = 10;
    public static final int TOTALFIELDS = 11;
    public static final int DICT_CHINESE = 0;
    public static final int DICT_PINYIN = 1;
    public static final int DICT_ENGLISH = 2;
    public static final int DICT_STROKECOUNT = 4;
    public static final int DICT_CANTONESE = 5;
    public static final int DICT_UNICODE = 6;
    public static final int DICT_GB = 7;
    public static final int DICT_BIG5 = 8;
    public static final int DICT_IDS = 9;
    public static final int DICT_AUTO = 3;
    public static final int STARTS_WITH = 0;
    public static final int ENDS_WITH = 1;
    public static final int CONTAINS = 2;
    public static final int EXACT_MATCH = 3;
}
