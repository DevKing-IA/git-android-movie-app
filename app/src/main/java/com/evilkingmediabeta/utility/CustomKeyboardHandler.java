package com.evilkingmediabeta.utility;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;

public class CustomKeyboardHandler {

    private static InputMethodManager imm;

    public static void showKeyboard(Context context){
        imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethod.SHOW_FORCED, 0);
    }

    public static void hiddenKeyboard(Context context, IBinder token){
        imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(token, 0);
    }

//    public static boolean isSoftKeyboardShown(EditText et){
//
//    }

}
