package com.dogacel;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class App {

    static String[] words;
    static int wordCount, longestWord;

    /**
     * Reads the word list from predefined word list.
     * @throws Exception
     */
    public static void loadWords() throws Exception {
        words = new String[20000000];
        wordCount = 0;
        FileReader inFile = new FileReader("wordlist.txt");

        BufferedReader br = new BufferedReader(inFile);

        String line = br.readLine();

        while (line != null)
        {
            String tmp = "";
            for (int i = 0; line.charAt(i) != '\t' ; i++)
            {
                tmp += line.charAt(i);
            }

            words[wordCount] = tmp;
            wordCount++;

            line = br.readLine();
        }

        br.close();
        inFile.close();


        words[0] = words[0].substring(1, words[0].length());

        for (int i = 0 ; i < wordCount - 1 ; i++) {
            if (compareTurkishString(words[i], words[i + 1]) == 1) {
                System.out.println(words[i] + " : " + words[i + 1]);
                break;
            }
        }

        System.out.println("=============");
        longestWord = 0;
        for (int i = 0 ; i < wordCount ; i++) {
            if (words[i].length() > longestWord)
                longestWord = words[i].length();
        }

        System.out.println("Loaded dict ! Longest word is : " + longestWord + " characters long.");

    }

    public static void main(String[] args) throws Exception {

        Scanner scan = new Scanner(System.in);
        String inStr;

        loadWords();

        do {
            System.out.print("Input: ");
            inStr = scan.nextLine();
            System.out.println("Output: " + hardRefactorString(inStr));
        } while (inStr.length() > 1);

    }

    //TODO: Recursive approach for all words properly seperated by spaces.
    /**
    public static String recursiveRefactorString(String s) {

        if (s == null)
            return null;

        if (s.equals("") || s.equals(".") || s.equals("!") || s.equals("?") || binarySearchWords(s) != null)
            return s;

        if (s.length() < 1)
            return null;

        int[] indexes = new int[50];
        int count = 0;
        for (int i = 2; i < longestWord && i < s.length() ; i++) {
            String result = binarySearchWords(s.substring(0, i));
            if (result != null) {
                indexes[count++] = i;
            }
        }

        for (int i = 0 ; i < count ; i++) {
            System.out.println(s.substring(0, indexes[i]) +  " : " + s.substring(indexes[i], s.length()));
             String tmp = recursiveRefactorString(s.substring(indexes[i], s.length()));
             if (tmp != null) {
                return s.substring(0, indexes[i]) + " " + tmp;
             }
        }

        return s;
    }*/

    /**
     * Translates the turkish string without spaces into a string with spaces.
     * @param s Input String
     * @return Spaced version of s.
     */
    public static String hardRefactorString(String s) {
        int index = 0;
        String retStr = "";

        while(!s.equals("")) {
            index = 0;
            for (int i = 0; i < longestWord && i < s.length() ; i++) {
                String result = binarySearchWords(s.substring(0, i));
                if (result != null)
                    index = i;
            }

            if (index == 0)
                index = 1;

            retStr += s.substring(0, index) + " ";
            s = s.substring(index, s.length());
        }

        return retStr;
    }

    /**
     * Binary searches a word in a lex. ordered word list.
     * @param substr Substring to be searched for.
     * @return null if not exists, substr if exists.
     */
    public static String binarySearchWords(String substr) {
        substr = substr.toLowerCase();
        int low = 0;
        int high = wordCount - 1;
        int mid;

        while (low <= high) {
            mid = (low + high) / 2;

            if (compareTurkishString(words[mid], substr) < 0) {
                low = mid + 1;
            } else if (compareTurkishString(words[mid], substr) > 0) {
                high = mid - 1;
            } else {
                if (words[mid].equals(substr))
                    return substr;
                return null;
            }
        }

        return null;
    }

    /**
     * Ordered chars for word list.
     * @param ch1 input char
     * @return value of the char in terms of turkish.
     */
    public static int turkishCharValue(char ch1) {
        if (ch1 == 'ş')
            return ' ' - 1;

        if (ch1 == 'ı')
            return ' ' - 2;

        if (ch1 == 'ğ')
            return ' ' - 3;

        if (ch1 == 'ü')
            return ' ' - 4;

        if (ch1 == 'ö')
            return ' ' - 5;

        if (ch1 == 'ç')
            return ' ' - 6;

        if (ch1 == '\'')
            return ' ' + 1;

        return ch1;
    }

    /**
     * Compares two characters including turkish characters.
     * @param ch1 inchar 1
     * @param ch2 inchar 2
     * @return 0 if equals, -1 if ch1 < ch2 and 2 if ch1 > ch2
     */
    public static int compareTurkishChar(char ch1, char ch2) {
        int ch1val = turkishCharValue(ch1);
        int ch2val = turkishCharValue(ch2);

        if (ch1val == ch2val)
            return 0;

        if (ch1val > ch2val)
            return 1;

        return -1;
    }

    /**
     * Compares two turkish strings in lex order
     * @param s1 instr 1
     * @param s2 instr 2
     * @return 0 if 1 == 2, 1 if 1 > 2 , -1 if 1 < 2
     */
    public static int compareTurkishString(String s1, String s2) {
        int length = Math.min(s1.length(), s2.length());
        for (int i = 0; i < length ; i++) {
            int comparison = compareTurkishChar(s1.charAt(i), s2.charAt(i));
            if (comparison == 1)
                return 1;
            if (comparison == -1)
                return -1;
        }

        if (s1.length() == s2.length())
            return 0;
        if (s1.length() > s2.length())
            return compareTurkishChar(s1.charAt(s2.length()), ' ');
        return compareTurkishChar(' ', s2.charAt(s1.length()));
    }

}
