package com.flin.online;

public class Util {


    public static String maxWord(String par)
    {
        String[]words=par.split(" ");
        String resstring="";
        for(String word: words)
        {
            if(word.length()>resstring.length())
                resstring=word;
        }
        return(resstring);
    }


    public static String maxWordDeleteColor(String par)
    {

        String[]words=par.split(" ");
        String resstring="";
        for(String word: words)
        {
            if(word.length()>resstring.length())
                resstring=word;
        }
        String newString =  resstring.replace("{33CC66}", "");
        String newStrings =  newString.replace("{ffffff}", "");
        resstring = newStrings;
        return(resstring);
    }

}
