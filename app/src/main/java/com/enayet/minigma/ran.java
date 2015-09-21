package com.enayet.minigma;

import android.widget.EditText;

import java.util.Random;

/**
 * Created by Afnan Enayet on 7/2/2014.
 */
public class ran {

    //leave them public
    static char[] standard_cipher = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=', '_', '[', ']', '{', '}', '<', '>', '?', ' ', ',', '.', '\'', '"', '¡', '«', '«', '¢', '©', '÷', 'µ', '·', '¶', '±', '€', '£', '®', '§', '™', '¥', 'á', 'Á', 'à', 'À', 'â', 'Â', 'å', 'Å', 'ã', 'Ã', 'ä', 'Ä', 'æ', 'Æ', 'ç', 'Ç', 'é', 'É', 'è', 'È', 'ê', 'Ê', 'ë', 'Ë', 'í', 'Í', 'ì', 'Ì', 'î', 'Î', 'ï', 'Ï', 'ñ', 'Ñ', 'ó', 'Ó', 'ò', 'Ò', 'ô', 'Ô', 'ø', 'Ø', 'õ', 'Õ', 'ö', 'Ö', 'ß', 'ú', 'Ú', 'ù', 'Ù', 'û', 'Û', 'ü', 'Ü', 'ÿ', '~', ':'};
    public char illegal_character;
    char[] cipher = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '-', '+', '=', '_', '[', ']', '{', '}', '<', '>', '?', ' ', ',', '.', '\'', '"', '¡', '«', '«', '¢', '©', '÷', 'µ', '·', '¶', '±', '€', '£', '®', '§', '™', '¥', 'á', 'Á', 'à', 'À', 'â', 'Â', 'å', 'Å', 'ã', 'Ã', 'ä', 'Ä', 'æ', 'Æ', 'ç', 'Ç', 'é', 'É', 'è', 'È', 'ê', 'Ê', 'ë', 'Ë', 'í', 'Í', 'ì', 'Ì', 'î', 'Î', 'ï', 'Ï', 'ñ', 'Ñ', 'ó', 'Ó', 'ò', 'Ò', 'ô', 'Ô', 'ø', 'Ø', 'õ', 'Õ', 'ö', 'Ö', 'ß', 'ú', 'Ú', 'ù', 'Ù', 'û', 'Û', 'ü', 'Ü', 'ÿ', '~', ':'};

    int gen_seed(EditText password) {
        String password_s = password.getText().toString(); //turns password into a string
        int string_length = password_s.length(); //so we can set up the for loop
        int seed = 0;

        for (int i = 1; i < string_length + 1; i++) { //turns password into a number based on ASCII
            char character = password_s.charAt(i - 1);
            int j = ((int) Math.pow(10, i));
            int k = ((int) character);
            seed = seed + (j * k);
        }

        return seed;
    }

    void shuffleArray(int seed) //shuffles the seed (Fisher)
    {
        Random rnd = new Random(seed);
        for (int i = cipher.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            char a = cipher[index];
            cipher[index] = cipher[i];
            cipher[i] = a;
        }
    }

    public boolean verifyString(EditText message) {
        String message_s = message.getText().toString();
        int message_length = message_s.length();
        char character;
        int indy;
        for (int i = 0; i < message_length; i++) {
            character = message_s.charAt(i);
            indy = new String(standard_cipher).indexOf(character);
            if (indy == -1) { //if index is -1 then character is not in array
                illegal_character = character;
                return false;
            }
        }
        return true;
    }

}
