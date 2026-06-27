package com.flin.online.gui.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

import androidx.core.content.ContextCompat;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class Utils {
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public static void ShowLayout(View view, boolean z) {
        if (view != null) {
            view.setVisibility(0);
            if (z) {
                fadeIn(view);
            } else {
                view.setAlpha(1.0f);
            }
        }
    }

    public static void HideLayout(View view, boolean z) {
        if (view != null) {
            if (z) {
                fadeOut(view);
            } else {
                view.setAlpha(0.0f);
                view.setVisibility(8);
            }
        }
    }

    private static void fadeIn(View view) {
        if (view != null) {
            view.animate().setDuration(250L).setListener(new AnimatorListenerAdapter() { // from class: com.saint.game.gui.util.Utils.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                }
            }).alpha(1.0f);
        }
    }

    private static void fadeOut(final View view) {
        if (view != null) {
            view.animate().setDuration(250L).setListener(new AnimatorListenerAdapter() { // from class: com.saint.game.gui.util.Utils.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    view.setVisibility(8);
                    super.onAnimationEnd(animator);
                }
            }).alpha(0.0f);
        }
    }

    public static Drawable getRes(Activity activity, int i) {
        return ContextCompat.getDrawable(activity.getApplicationContext(), i);
    }

    public static Spanned transfromColors(String str) {
        int i;
        LinkedList linkedList = new LinkedList();
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < str.length(); i4++) {
            if (str.charAt(i4) == '{' && (i = i4 + 7) < str.length()) {
                StringBuilder sb = new StringBuilder();
                sb.append("#");
                int i5 = i4 + 1;
                sb.append(str.substring(i5, i));
                linkedList.addLast(sb.toString());
                str = str.substring(0, i5) + "repl" + i3 + str.substring(i);
                i3++;
            }
        }
        Iterator it = linkedList.iterator();
        while (it.hasNext()) {
            String str2 = (String) it.next();
            str = i2 == 0 ? str.replaceAll(Pattern.quote("{repl" + i2 + "}"), "<font color='" + str2 + "'>") : str.replaceAll(Pattern.quote("{repl" + i2 + "}"), "</font><font color='" + str2 + "'>");
            i2++;
        }
        if (linkedList.size() >= 1) {
            str = str + "</font>";
        }
        return Html.fromHtml(str.replaceAll(Pattern.quote("\n"), "<br>"));
    }

    public static ArrayList<String> fixFieldsForDialog(ArrayList<String> arrayList) {
        ArrayList<String> arrayList2 = new ArrayList<>();
        int i = 0;
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            int length = arrayList.get(i2).split("\t").length;
            if (length > i) {
                i = length;
            }
        }
        for (int i3 = 0; i3 < arrayList.size(); i3++) {
            StringBuilder sb = new StringBuilder(arrayList.get(i3));
            for (int length2 = arrayList.get(i3).split("\t").length; length2 != i; length2++) {
                sb.append("\\t ");
            }
            arrayList2.add(sb.toString());
        }
        return arrayList2;
    }

    public static String randomString(int i) {
        StringBuilder sb = new StringBuilder(i);
        for (int i2 = 0; i2 < i; i2++) {
            sb.append(AB.charAt(rnd.nextInt(62)));
        }
        return sb.toString();
    }
}
