package de.chino;

import java.io.PrintStream;

public class ChineseNumbers
{

    public ChineseNumbers()
    {
    }

    public static boolean isChineseNumber(String s)
    {
        boolean flag = true;
        int i = 0;
        do
        {
            if(i >= s.length())
                break;
            boolean flag1 = false;
            int j = 0;
            do
            {
                if(j >= numberchars.length)
                    break;
                if(s.charAt(i) == numberchars[j])
                {
                    flag1 = true;
                    break;
                }
                j++;
            } while(true);
            if(!flag1)
            {
                flag = false;
                break;
            }
            i++;
        } while(true);
        return flag;
    }

    public static String EnglishToChineseNumber(long l)
    {
        return EnglishToChineseNumber(l, 0);
    }

    public static String EnglishToChineseNumber(long l, int i)
    {
        int ai[] = new int[30];
        int j3 = 0;
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        StringBuffer stringbuffer = new StringBuffer();
        if(l == 0L)
            return digits[0];
        if(l < 0L)
        {
            flag1 = true;
            l = -l;
        }
        for(; Math.pow(10D, j3) <= (double)l; j3++)
        {
            int k3 = (int)(((double)l % Math.pow(10D, j3 + 1)) / Math.pow(10D, j3));
            ai[j3] = k3;
            l = (long)((double)l - (double)l % Math.pow(10D, j3 + 1));
        }

        for(int j = 0; j < j3; j++)
        {
            if(j % 4 == 0)
            {
                if(ai[j] != 0)
                {
                    flag2 = false;
                    flag3 = true;
                    stringbuffer.insert(0, digits[ai[j]] + afterWan[j / 4]);
                    continue;
                }
                if(j + 3 < j3 && ai[j + 3] != 0 || j + 2 < j3 && ai[j + 2] != 0 || j + 1 < j3 && ai[j + 1] != 0)
                    stringbuffer.insert(0, afterWan[j / 4]);
                continue;
            }
            if(ai[j] != 0)
            {
                flag2 = false;
                flag3 = true;
                if(j3 == 2 && j == 1 && ai[j] == 1)
                    stringbuffer.insert(0, beforeWan[j % 4 - 1]);
                else
                    stringbuffer.insert(0, digits[ai[j]] + beforeWan[j % 4 - 1]);
                continue;
            }
            if(flag3 && !flag2)
            {
                flag2 = true;
                stringbuffer.insert(0, digits[ai[j]]);
            }
        }

        if(flag1)
            stringbuffer.insert(0, "\u8CA0");
        String s = stringbuffer.toString();
        StringBuffer stringbuffer1 = new StringBuffer();
        if(i == 0)
            s = stringbuffer.toString();
        else
        if(i == 1)
        {
            s = stringbuffer.toString();
            for(int k = 0; k < trad2tradformal.length; k += 2)
                s = s.replace(trad2tradformal[k], trad2tradformal[k + 1]);

        } else
        if(i == 2)
        {
            s = stringbuffer.toString();
            for(int i1 = 0; i1 < trad2simp.length; i1 += 2)
                s = s.replace(trad2simp[i1], trad2simp[i1 + 1]);

        } else
        if(i == 3)
        {
            s = stringbuffer.toString();
            for(int j1 = 0; j1 < trad2simpformal.length; j1 += 2)
                s = s.replace(trad2simpformal[j1], trad2simpformal[j1 + 1]);

        } else
        if(i == 4)
        {
label0:
            for(int k1 = 0; k1 < s.length(); k1++)
            {
                int k2 = 0;
                do
                {
                    if(k2 >= trad2pinyin.length)
                        continue label0;
                    if(s.substring(k1, k1 + 1).equals(trad2pinyin[k2]))
                    {
                        stringbuffer1.append(trad2pinyin[k2 + 1]);
                        if(k1 + 1 != s.length())
                            stringbuffer1.append(" ");
                        continue label0;
                    }
                    k2 += 2;
                } while(true);
            }

            s = stringbuffer1.toString();
        } else
        if(i == 5)
        {
label1:
            for(int l1 = 0; l1 < s.length(); l1++)
            {
                int l2 = 0;
                do
                {
                    if(l2 >= trad2jyutpin.length)
                        continue label1;
                    if(s.substring(l1, l1 + 1).equals(trad2jyutpin[l2]))
                    {
                        stringbuffer1.append(trad2jyutpin[l2 + 1]);
                        if(l1 + 1 != s.length())
                            stringbuffer1.append(" ");
                        continue label1;
                    }
                    l2 += 2;
                } while(true);
            }

            s = stringbuffer1.toString();
        } else
        if(i == 6)
        {
label2:
            for(int i2 = 0; i2 < s.length(); i2++)
            {
                int i3 = 0;
                do
                {
                    if(i3 >= trad2yalecant.length)
                        continue label2;
                    if(s.substring(i2, i2 + 1).equals(trad2yalecant[i3]))
                    {
                        stringbuffer1.append(trad2yalecant[i3 + 1]);
                        if(i2 + 1 != s.length())
                            stringbuffer1.append(" ");
                        continue label2;
                    }
                    i3 += 2;
                } while(true);
            }

            s = stringbuffer1.toString();
        } else
        if(i == 7)
        {
            for(int j2 = 0; j2 < s.length(); j2++)
            {
                stringbuffer1.append(Integer.toHexString(s.charAt(j2)));
                if(j2 + 1 != s.length())
                    stringbuffer1.append(" ");
            }

            s = stringbuffer1.toString();
        }
        return s;
    }

    public static String ChineseToEnglishNumber(String s)
    {
        return ChineseToEnglishNumber(s, 0);
    }

    public static String ChineseToEnglishNumber(String s, int i)
    {
        boolean flag = true;
        s = s.trim();
        if(s.indexOf("\u5206\u4E4B") == -1)
        {
            if(s.indexOf("\u7B2C") == 0)
            {
                long l = ChineseToEnglishValue(s.substring(1, s.length()));
                return Long.toString(l);
            }
            if(isDecimalNumber(s))
            {
                String s1 = s.replaceFirst("(\u9EDE|\u70B9|\\.).+$", "");
                long l2 = ChineseToEnglishValue(s1);
                String s2 = s.replaceFirst("^.+(\u9EDE|\u70B9|\\.)", "");
                StringBuffer stringbuffer = new StringBuffer(Long.toString(l2));
                stringbuffer.append(".");
                for(int j = 0; j < s2.length(); j++)
                    stringbuffer.append(getDigitValue(s2.charAt(j)));

                return stringbuffer.toString();
            } else
            {
                long l1 = ChineseToEnglishValue(s);
                return Long.toString(l1);
            }
        } else
        {
            return "";
        }
    }

    public static boolean isDecimalNumber(String s)
    {
        boolean flag = true;
        int i;
        if((i = s.indexOf('\u9EDE')) != -1 || (i = s.indexOf('\u70B9')) != -1 || (i = s.indexOf('.')) != -1)
        {
            for(int j = i + 1; j < s.length(); j++)
                if(!isDigit(s.charAt(j)))
                    flag = false;

        } else
        {
            flag = false;
        }
        return flag;
    }

    public static boolean isDigit(char c)
    {
        return c == '0' || c == '\uFF10' || c == '\u96F6' || c == '\u3007' || c == '1' || c == '\uFF11' || c == '\u4E00' || c == '\u58F9' || c == '2' || c == '\uFF12' || c == '\u4E8C' || c == '\u8CB3' || c == '\u8D30' || c == '\u5169' || c == '\u4E24' || c == '3' || c == '\uFF13' || c == '\u4E09' || c == '\u53C3' || c == '\u53C4' || c == '\u53C1' || c == '4' || c == '\uFF14' || c == '\u56DB' || c == '\u8086' || c == '5' || c == '\uFF15' || c == '\u4E94' || c == '\u4F0D' || c == '6' || c == '\uFF16' || c == '\u516D' || c == '\u9678' || c == '\u9646' || c == '7' || c == '\uFF17' || c == '\u4E03' || c == '\u67D2' || c == '8' || c == '\uFF18' || c == '\u516B' || c == '\u634C' || c == '9' || c == '\uFF19' || c == '\u4E5D' || c == '\u7396';
    }

    public static int getDigitValue(char c)
    {
        if(c == '0' || c == '\uFF10' || c == '\u96F6' || c == '\u3007')
            return 0;
        if(c == '1' || c == '\uFF11' || c == '\u4E00' || c == '\u58F9')
            return 1;
        if(c == '2' || c == '\uFF12' || c == '\u4E8C' || c == '\u8CB3' || c == '\u8D30' || c == '\u5169' || c == '\u4E24')
            return 2;
        if(c == '3' || c == '\uFF13' || c == '\u4E09' || c == '\u53C3' || c == '\u53C4' || c == '\u53C1')
            return 3;
        if(c == '4' || c == '\uFF14' || c == '\u56DB' || c == '\u8086')
            return 4;
        if(c == '5' || c == '\uFF15' || c == '\u4E94' || c == '\u4F0D')
            return 5;
        if(c == '6' || c == '\uFF16' || c == '\u516D' || c == '\u9678' || c == '\u9646')
            return 6;
        if(c == '7' || c == '\uFF17' || c == '\u4E03' || c == '\u67D2')
            return 7;
        if(c == '8' || c == '\uFF18' || c == '\u516B' || c == '\u634C')
            return 8;
        return c != '9' && c != '\uFF19' && c != '\u4E5D' && c != '\u7396' ? -1 : 9;
    }

    public static long ChineseToEnglishValue(String s)
    {
        boolean flag = false;
        boolean flag1 = false;
        int l = 0;
        double d = 0.0D;
        long l1 = 0L;
        s = s.replaceAll("\u4E07\u4EBF", "\u5146");
        s = s.replaceAll("\u842C\u5104", "\u5146");
        s = s.replaceAll("\u500B", "");
        s = s.replaceAll("\u4E2A", "");
        s = s.replaceAll("\u5EFF", "\u4E8C\u5341");
        s = s.replaceAll("\u5344", "\u4E8C\u5341");
        s = s.replaceAll("\u5345", "\u4E09\u5341");
        s = s.replaceAll("\u534C", "\u56DB\u5341");
        int i = s.length();
        for(int j = 0; j < i; j++)
        {
            if(j == 0 && (s.charAt(j) == '\u8D1F' || s.charAt(j) == '\u8CA0'))
            {
                flag = true;
                continue;
            }
            if(j == 0 && s.charAt(j) == '\u7B2C')
                continue;
            if(s.charAt(j) == '\u9EDE' || s.charAt(j) == '\u70B9' || s.charAt(j) == '.')
            {
                flag1 = true;
                l = -1;
                continue;
            }
            if(s.charAt(j) == '\u5146')
            {
                l = 12;
                if(d == 0.0D)
                    d = 1.0D;
                l1 = (long)((double)l1 + d * Math.pow(10D, l));
                d = 0.0D;
                l -= 4;
                continue;
            }
            if(s.charAt(j) == '\u5104' || s.charAt(j) == '\u4EBF')
            {
                l = 8;
                if(d == 0.0D)
                    d = 1.0D;
                l1 = (long)((double)l1 + d * Math.pow(10D, l));
                d = 0.0D;
                l -= 4;
                continue;
            }
            if(s.charAt(j) == '\u842C' || s.charAt(j) == '\u4E07')
            {
                l = 4;
                if(d == 0.0D)
                    d = 1.0D;
                l1 = (long)((double)l1 + d * Math.pow(10D, l));
                d = 0.0D;
                l -= 4;
                continue;
            }
            if(s.charAt(j) == '\u5343' || s.charAt(j) == '\u4EDF')
            {
                d += 1000D;
                continue;
            }
            if(s.charAt(j) == '\u767E' || s.charAt(j) == '\u4F70')
            {
                d += 100D;
                continue;
            }
            if(s.charAt(j) == '\u5341' || s.charAt(j) == '\u4EC0' || s.charAt(j) == '\u62FE')
            {
                d += 10D;
                continue;
            }
            if(s.charAt(j) == '\u96F6' || s.charAt(j) == '\u3007')
            {
                l = 0;
                continue;
            }
            if(!isDigit(s.charAt(j)))
                continue;
            int k = getDigitValue(s.charAt(j));
            if(flag1)
            {
                d += (double)k * Math.pow(10D, l);
                l--;
                for(; j + 1 < i && isDigit(s.charAt(j + 1)); j++)
                {
                    d += (double)getDigitValue(s.charAt(j + 1)) * Math.pow(10D, l);
                    l--;
                }

                continue;
            }
            if(j + 1 < i)
            {
                if(s.charAt(j + 1) == '\u5341' || s.charAt(j + 1) == '\u4EC0' || s.charAt(j + 1) == '\u62FE')
                {
                    d += k * 10;
                    j++;
                    continue;
                }
                if(s.charAt(j + 1) == '\u767E' || s.charAt(j + 1) == '\u4F70')
                {
                    d += k * 100;
                    j++;
                    continue;
                }
                if(s.charAt(j + 1) == '\u5343' || s.charAt(j + 1) == '\u4EDF')
                {
                    d += k * 1000;
                    j++;
                    continue;
                }
                if(isDigit(s.charAt(j + 1)))
                {
                    d *= 10D;
                    d += k;
                    for(; j + 1 < i && isDigit(s.charAt(j + 1)); j++)
                    {
                        d *= 10D;
                        d += getDigitValue(s.charAt(j + 1));
                    }

                } else
                {
                    d += k;
                }
                continue;
            }
            if(j + 1 == i && j > 0)
            {
                if(s.charAt(j - 1) == '\u5146')
                {
                    d += (double)k * Math.pow(10D, 11D);
                    continue;
                }
                if(s.charAt(j - 1) == '\u5104' || s.charAt(j - 1) == '\u4EBF')
                {
                    d += (double)k * Math.pow(10D, 7D);
                    continue;
                }
                if(s.charAt(j - 1) == '\u842C' || s.charAt(j - 1) == '\u4E07')
                {
                    d += (double)k * Math.pow(10D, 3D);
                    continue;
                }
                if(s.charAt(j - 1) == '\u5343' || s.charAt(j - 1) == '\u4EDF')
                {
                    d += k * 100;
                    continue;
                }
                if(s.charAt(j - 1) == '\u767E' || s.charAt(j - 1) == '\u4F70')
                    d += k * 10;
                else
                    d += k;
            } else
            {
                d += k;
            }
        }

        l1 = (long)((double)l1 + d);
        if(flag)
            l1 = -l1;
        return l1;
    }

    public static void main(String args[])
    {
        long l;
        if(args.length == 0)
            l = 0xfffffffd99848d3cL;
        else
            l = Long.parseLong(args[0]);
        System.out.println(EnglishToChineseNumber(l));
        System.out.println(ChineseToEnglishNumber("\u4E8C\u5341\u4E5D"));
        System.out.println(ChineseToEnglishNumber("\u4E00\u4E5D\u4E5D\u4E5D"));
    }

    public static final String MINUS = "\u8CA0";
    public static final String sminus = "\u8D1F";
    public static final String digits[] = {
        "\u96F6", "\u4E00", "\u4E8C", "\u4E09", "\u56DB", "\u4E94", "\u516D", "\u4E03", "\u516B", "\u4E5D"
    };
    public static final String beforeWan[] = {
        "\u5341", "\u767E", "\u5343"
    };
    public static final String sbeforeWan[] = {
        "\u5341", "\u767E", "\u5343"
    };
    public static final String afterWan[] = {
        "", "\u842C", "\u5104", "\u5146"
    };
    public static final String safterWan[] = {
        "", "\u4E07", "\u4EBF", "\u5146"
    };
    public static final String ALTTWO = "\u5169";
    public static final long TEN = 10L;
    public static final int TRADITIONAL = 0;
    public static final int TRADITIONAL_FORMAL = 1;
    public static final int SIMPLIFIED = 2;
    public static final int SIMPLIFIED_FORMAL = 3;
    public static final int PINYIN = 4;
    public static final int JYUTPIN = 5;
    public static final int YALE_CANTONESE = 6;
    public static final int UNICODEHEX = 7;
    public static final int TYPE_TOTAL = 8;
    public static final int NOCOMMAS = 0;
    public static final int COMMAS = 1;
    public static final int WORDS = 2;
    public static final String typenames[] = {
        "Traditional", "Traditional (formal)", "Simplified", "Simplified (formal)", "Pinyin", "Cantonese (Jyutpin)", "Cantonese (Yale)", "Unicode Hex"
    };
    public static final String engTypesNames[] = {
        "Arabic numerals", "Arabic numerals (with commas)", "English words"
    };
    public static final char all2trad[] = {
        '\u8CA0', '\u8CA0', '\u8D1F', '\u8CA0', '\u3007', '\u96F6', '\u96F6', '\u96F6', '\u4E00', '\u4E00', 
        '\u4E8C', '\u4E8C', '\u4E09', '\u4E09', '\u56DB', '\u56DB', '\u4E94', '\u4E94', '\u516D', '\u516D', 
        '\u4E03', '\u4E03', '\u516B', '\u516B', '\u4E5D', '\u4E5D', '\u5341', '\u5341', '\u767E', '\u767E', 
        '\u5343', '\u5343', '\u842C', '\u842C', '\u4E07', '\u842C', '\u5104', '\u5104', '\u4EBF', '\u5104', 
        '\u5146', '\u5146', '\u5169', '\u5169', '\u4E24', '\u5169', '\u58F9', '\u4E00', '\u8CB3', '\u4E8C', 
        '\u53C3', '\u4E09', '\u8086', '\u56DB', '\u4F0D', '\u4E94', '\u9678', '\u516D', '\u67D2', '\u4E03', 
        '\u634C', '\u516B', '\u7396', '\u4E5D', '\u62FE', '\u5341', '\u4F70', '\u767E', '\u4EDF', '\u5343', 
        '\u58F9', '\u4E00', '\u8D30', '\u4E8C', '\u53C2', '\u4E09', '\u9646', '\u516D'
    };
    public static final char trad2tradformal[] = {
        '\u8CA0', '\u8CA0', '\u96F6', '\u96F6', '\u4E00', '\u58F9', '\u4E8C', '\u8CB3', '\u4E09', '\u53C3', 
        '\u56DB', '\u8086', '\u4E94', '\u4F0D', '\u516D', '\u9678', '\u4E03', '\u67D2', '\u516B', '\u634C', 
        '\u4E5D', '\u7396', '\u5341', '\u62FE', '\u767E', '\u4F70', '\u5343', '\u4EDF', '\u842C', '\u842C', 
        '\u5104', '\u5104', '\u5146', '\u5146', '\u5169', '\u5169'
    };
    public static final char trad2simp[] = {
        '\u8CA0', '\u8D1F', '\u96F6', '\u96F6', '\u4E00', '\u4E00', '\u4E8C', '\u4E8C', '\u4E09', '\u4E09', 
        '\u56DB', '\u56DB', '\u4E94', '\u4E94', '\u516D', '\u516D', '\u4E03', '\u4E03', '\u516B', '\u516B', 
        '\u4E5D', '\u4E5D', '\u5341', '\u5341', '\u767E', '\u767E', '\u5343', '\u5343', '\u842C', '\u4E07', 
        '\u5104', '\u4EBF', '\u5146', '\u5146', '\u4E24', '\u5169'
    };
    public static final char trad2simpformal[] = {
        '\u8CA0', '\u8CA0', '\u96F6', '\u96F6', '\u4E00', '\u58F9', '\u4E8C', '\u8D30', '\u4E09', '\u53C2', 
        '\u56DB', '\u8086', '\u4E94', '\u4F0D', '\u516D', '\u9646', '\u4E03', '\u67D2', '\u516B', '\u634C', 
        '\u4E5D', '\u7396', '\u5341', '\u62FE', '\u767E', '\u4F70', '\u5343', '\u4EDF', '\u842C', '\u4E07', 
        '\u5104', '\u4EBF', '\u5146', '\u5146', '\u5169', '\u5169'
    };
    public static final String trad2pinyin[] = {
        "\u8CA0", "fu4", "\u96F6", "ling2", "\u4E00", "yi1", "\u4E8C", "er4", "\u4E09", "san1", 
        "\u56DB", "si4", "\u4E94", "wu3", "\u516D", "liu4", "\u4E03", "qi1", "\u516B", "ba1", 
        "\u4E5D", "jiu3", "\u5341", "shi2", "\u767E", "bai3", "\u5343", "qian1", "\u842C", "wan4", 
        "\u5104", "yi4", "\u5146", "zhao4", "\u5169", "liang3"
    };
    public static final String trad2yalecant[] = {
        "\u8CA0", "fu", "\u96F6", "ling2", "\u4E00", "yat", "\u4E8C", "yih7", "\u4E09", "saam1", 
        "\u56DB", "sei5", "\u4E94", "ng4", "\u516D", "luhk", "\u4E03", "chat1", "\u516B", "baat1", 
        "\u4E5D", "gao3", "\u5341", "sap7", "\u767E", "baak5", "\u5343", "chin1", "\u842C", "maahn", 
        "\u5104", "yik1", "\u5146", "siu", "\u5169", "leung4"
    };
    public static final String trad2jyutpin[] = {
        "\u8CA0", "fu6", "\u96F6", "ling4", "\u4E00", "jat1", "\u4E8C", "ji6", "\u4E09", "saam1", 
        "\u56DB", "sei3", "\u4E94", "ng5", "\u516D", "luk6", "\u4E03", "cat1", "\u516B", "baat3", 
        "\u4E5D", "gau2", "\u5341", "sap6", "\u767E", "baak3", "\u5343", "cin1", "\u842C", "maan6", 
        "\u5104", "jik1", "\u5146", "siu6", "\u5169", "loeng5"
    };
    public static final char numberchars[] = {
        '\u96F6', '\u4E00', '\u4E8C', '\u4E09', '\u56DB', '\u4E94', '\u516D', '\u4E03', '\u516B', '\u4E5D', 
        '\u5341', '\u767E', '\u5343', '\u842C', '\u4E07'
    };

}
