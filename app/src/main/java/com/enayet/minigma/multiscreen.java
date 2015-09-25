package com.enayet.minigma;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@TargetApi(11)
public class multiscreen extends AppCompatActivity {
    private static String s_output; //keep it global so it can be passed around
    private static boolean need_compat;
    private static boolean need_compat_dialog;
    CoordinatorLayout snackbarParentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_multiscreen);
        snackbarParentLayout = (CoordinatorLayout) findViewById(R.id.coordinator); //TODO: fix so FAB works
        Uri intent = getIntent().getData();
        if (intent != null) {
            parseDeepLink(intent);
        }

        need_compat = Build.VERSION.SDK_INT <= 9;
        need_compat_dialog = Build.VERSION.SDK_INT < 11;

       /* FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.myFAB); //TODO: delete comment tags
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shareMessage();
            }
        });*/
    }


    protected void onResume() {
        super.onResume();
        if (need_compat) {
            setPassword(); //sets the default password jic user changed it in prefs
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final SharedPreferences touch_prefs = PreferenceManager.getDefaultSharedPreferences(this); //allows preferences to be accessed in the function
        if (!need_compat) { //doesn't open up incompatible fragment if gingerbread detected (API too low)
            getMenuInflater().inflate(R.menu.menu_multiscreen, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_gingerbread_compat, menu);
        }

        final Button decodeButton = (Button) findViewById(R.id.decodeButton); //getting the decode button
        decodeButton.setOnTouchListener(new View.OnTouchListener() { //setting the touchlistener for the button
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) { //ontouch listener so we can perform actions while the button is held down
                TextView text = (TextView) findViewById(R.id.messageOutput); //so we can set the text of message output
                EditText message_input = (EditText) findViewById(R.id.messageEdit); //gets the message thing
                EditText pass_input = (EditText) findViewById(R.id.passEdit); //gets the password thing
                TextView decode_hint = (TextView) findViewById(R.id.textHint);
                decoder decode = new decoder(); //new instance of classes
                ran verify = new ran(); //new instance of ran class
                boolean legal; //boolean variable that will indicate whether the function should proceed or not

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: //what happens while user is holding button down (b/c they might not just press it)
                        InputMethodManager inputManager = (InputMethodManager) //hide keyboard on button press
                                getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), //hides keyboard on button press
                                InputMethodManager.HIDE_NOT_ALWAYS);
                        legal = verify.verifyString(message_input); //checks to see if message is aight
                        if (!legal) { //stops function if message contains unsupported characters
                            illegalCharacter(verify.illegal_character);
                        } else if (legal) { //allows functions to proceed if the message is ok
                            s_output = decode.decode_message(message_input, pass_input);
                            decode_hint.setText(R.string.decode_hint_prompt);

                            if (touch_prefs.getBoolean("instaclear_checkbox", true)) { //checking prefs
                                pass_input.setText(R.string.clear_string);
                            }
                            if (touch_prefs.getBoolean("clear_message_checkbox", true)) { //checking prefs
                                message_input.setText(R.string.clear_string);
                            }
                            text.setText(s_output); //displays the decoded string
                        }
                        break;
                    case MotionEvent.ACTION_UP:

                        if (touch_prefs.getBoolean("passpeek_checkbox", true)) { //checking preferences
                            text.setText(R.string.clear_string);
                            decode_hint.setText(R.string.clear_string);
                        }
                        break;
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) { //uses popup to show information to user
            infoDialog("About", R.string.about_popup_description);
            return true;

        } else if (id == R.id.help_screen) {
            infoDialog("Help", R.string.helpString2); //shows dialog with information
            return true;
        } else if (id == R.id.settings_screen) { //opens the settings activity via intent
            Intent openSettings = new Intent(this, SettingsActivity.class);
            startActivity(openSettings);
            //getting new settings activity
        }
        return super.onOptionsItemSelected(item);

        //methods corresponding to multiscreen fragment

    }

    public void fabShare(View view) {
        shareMessage();
    }

    public void message_encode(View view) {
        InputMethodManager inputManager = (InputMethodManager) //hide the keyboard on button press
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        TextView text = (TextView) findViewById(R.id.messageOutput); //so we can set the text of message output
        EditText message_input = (EditText) findViewById(R.id.messageEdit); //gets the message thing
        EditText pass_input = (EditText) findViewById(R.id.passEdit); //gets the password thing
        encoder encode = new encoder(); //new instance of classes
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this); //gets preferences

        TextView encode_hint = (TextView) findViewById(R.id.textHint); //gets texthint box
        ran fuck = new ran(); //new instance of ran
        boolean legal = fuck.verifyString(message_input); //makes variable to check if message contains any "illegal" characters

        s_output = encode.return_message(message_input, pass_input); //runs function to get the encoded string

        if (prefs.getBoolean("instaclear_checkbox", true)) { //checks to see if pref is selected
            pass_input.setText(R.string.clear_string);

        }

        if (prefs.getBoolean("clear_message_checkbox", true)) { //checks to see if clear message pref is selected
            message_input.setText(R.string.clear_string);
        }

        if (legal) { //if message is ok, then function will proceed
            encode_hint.setText(R.string.encode_hint_prompt); //sets the descriptive text

            text.setText(s_output); //displays the encoded string
        } else if (!legal) { //if message has unsupported characters, calls function to mitigate
            illegalCharacter(fuck.illegal_character);

        }
    }

    public void clearPassword(View view) { //clears password, shows toast
        EditText password_input = (EditText) findViewById(R.id.passEdit);
        password_input.setText(R.string.clear_string);
        Snackbar.make(snackbarParentLayout, R.string.clear_pass_toast, Snackbar.LENGTH_SHORT)
                .show();
    }

    public void clearMessage(View view) { //clears message, shows toast

        EditText message_input = (EditText) findViewById(R.id.messageEdit);
        TextView text_hint = (TextView) findViewById(R.id.textHint);
        message_input.setText(R.string.clear_string);
        text_hint.setText(R.string.clear_string);
        Snackbar.make(snackbarParentLayout, R.string.clear_mess_toast, Snackbar.LENGTH_SHORT)
                .show();
    }

    public void illegalCharacter(char illegal_char) { //checks to see if any unsupported characters are used
        TextView text = (TextView) findViewById(R.id.messageOutput);
        text.setText(R.string.clear_string);
        String illegal_char_message = "Illegal character used in message! (" + illegal_char + ")"; //shows toast indicating which character is illegal
        Snackbar.make(snackbarParentLayout, illegal_char_message, Snackbar.LENGTH_SHORT)
                .show();
    }

    @TargetApi(11)
    private void setPassword() {
        final SharedPreferences touch_prefs_check = PreferenceManager.getDefaultSharedPreferences(this);
        EditText pass_input = (EditText) findViewById(R.id.passEdit);
        if (touch_prefs_check.getBoolean("instaclear_checkbox", false)) { //checks to see if pref is selected
            pass_input.setText("");
        } else {
            String default_password = touch_prefs_check.getString("default_password", "");
            pass_input.setText(default_password);
        }

    }

    private void parseDeepLink(Uri uri) {
        String coded_incoming_message = uri.getEncodedPath().substring(1);
        EditText mess_input = (EditText) findViewById(R.id.messageEdit);
        String decoded_message = java.net.URLDecoder.decode(coded_incoming_message);
        mess_input.setText(decoded_message);
    }

    public void shareMessage() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (s_output == null || s_output.isEmpty() || s_output.equals("")) {
            View view = this.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);


            Snackbar.make(snackbarParentLayout, R.string.sending_nothing, Snackbar.LENGTH_SHORT)
                    .show();

        } else {
            Intent sendIntent = new Intent();


            if (prefs.getBoolean("share_uri", true)) {
                String message_uri = "minigma.com/" + java.net.URLEncoder.encode(s_output);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, message_uri);
            } else {
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, s_output);
            }
            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
        }
    }

    void infoDialog(String title, int message) { //creates a cancellable dialog with "OK" button
        if (need_compat_dialog) {
            new AlertDialog.Builder(this) //adjusts theme for API compatibility
                    .setTitle(title)
                    .setMessage(message)
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
        }

        else {
            new AlertDialog.Builder(this, R.style.DialogTheme) //adjusts theme for API compatibility
                    .setTitle(title)
                    .setMessage(message)
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
        }
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            return inflater.inflate(R.layout.fragment_multiscreen, container, false);
        }
    }
}
