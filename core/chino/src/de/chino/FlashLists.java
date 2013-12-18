package de.chino;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;

public class FlashLists
{

    public FlashLists()
    {
        listData = new HashMap();
        for(int i = 0; i < setnames.length; i++)
        {
            Vector vector = new Vector();
            java.io.InputStream inputstream = ClassLoader.getSystemResourceAsStream("studylists/" + setnames[i][2]);
            try
            {
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, "UTF8"));
                do
                {
                    String s;
                    if((s = bufferedreader.readLine()) == null)
                        break;
                    if(s.length() != 0 && s.charAt(0) != '#')
                        vector.add(s.substring(0, s.indexOf("\t")));
                } while(true);
                bufferedreader.close();
                listData.put(setnames[i][0], vector);
            }
            catch(IOException ioexception)
            {
                System.err.println("Exception : " + ioexception);
            }
        }

    }

    public int listSize(int i)
    {
        Vector vector = (Vector)listData.get(Integer.toString(i));
        if(vector == null)
            return 0;
        else
            return vector.size();
    }

    public String getListName(int i)
    {
        for(int j = 0; j < setnames.length; j++)
            if(Integer.parseInt(setnames[j][0]) == i)
                return setnames[j][1];

        return null;
    }

    public int getListID(String s)
    {
        for(int i = 0; i < setnames.length; i++)
            if(setnames[i][1].equalsIgnoreCase(s))
                return Integer.parseInt(setnames[i][0]);

        return -1;
    }

    public String getWordAt(int i, int j)
    {
        Vector vector = (Vector)listData.get(Integer.toString(i));
        if(vector == null)
            return null;
        if(j < 0 || j >= vector.size())
            return null;
        else
            return (String)vector.elementAt(j);
    }

    public Vector listLists()
    {
        Vector vector = new Vector();
        for(int i = 0; i < setnames.length; i++)
            vector.add(setnames[i][1]);

        return vector;
    }

    public Vector listListIDs()
    {
        Vector vector = new Vector();
        for(int i = 0; i < setnames.length; i++)
            vector.add(new Integer(setnames[i][0]));

        return vector;
    }

    public int listCount()
    {
        return setnames.length;
    }

    public void addStatsWord(String s)
    {
    }

    public boolean statsWordExists(String s)
    {
        return true;
    }

    public void updateStats(String s, boolean flag)
    {
    }

    public int getRecentCorrect(String s)
    {
        return 0;
    }

    public int getTotalCorrect(String s)
    {
        return 0;
    }

    public int getTotalTested(String s)
    {
        return 0;
    }

    public void setLearned(String s, boolean flag)
    {
    }

    public boolean getLearned(String s)
    {
        return false;
    }

    private HashMap listData;
    static final String setnames[][] = {
        {
            "0", "Traditional (Top 300)", "tradset300.u8", "0", "0"
        }, {
            "1", "Traditional (Common)", "tradsetc.u8", "0", "0"
        }, {
            "2", "Traditional (Full)", "tradsetf.u8", "0", "0"
        }, {
            "3", "Simplified (Top 300)", "simpset300.u8", "0", "0"
        }, {
            "4", "Simplified (Common)", "simpsetc.u8", "0", "0"
        }, {
            "5", "Simplified (Full)", "simpsetf.u8", "0", "0"
        }, {
            "6", "Kangxi Radicals", "kxradicals.u8", "0", "0"
        }, {
            "7", "PCR Book I", "pcr1-vocabulary.u8", "0", "0"
        }, {
            "8", "PCR Book II", "pcr2-vocabulary.u8", "0", "0"
        }
    };

}
