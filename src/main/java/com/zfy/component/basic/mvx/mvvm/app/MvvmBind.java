package com.zfy.component.basic.mvx.mvvm.app;

import android.arch.lifecycle.LifecycleOwner;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.zfy.component.basic.mvx.mvvm.ExLiveData;

/**
 * CreateAt : 2019/6/19
 * Describe :
 *
 * @author chendong
 */
public class MvvmBind {

    public static void bindText(
            @NonNull final LifecycleOwner owner,
            @NonNull final TextView textView,
            @NonNull final ExLiveData<CharSequence> data) {
        if (textView instanceof EditText) {
            bindText(owner, ((EditText) textView), data);
            return;
        }
        data.observe(owner, d -> {
            if (d != null) {
                textView.setText(d);
            }
        });
    }

    private static void bindText(
            @NonNull final LifecycleOwner owner,
            @NonNull final EditText textView,
            @NonNull final ExLiveData<CharSequence> data) {
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                data.setValueNoEqual(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        data.observe(owner, d -> {
            if (d != null) {
                String string = textView.getText().toString();
                if (!d.equals(string)) {
                    textView.setText(d);
                    textView.setSelection(string.length() - 1);
                }
            }
        });
    }


    public static void bindCheck(
            @NonNull final LifecycleOwner owner,
            @NonNull final CompoundButton compoundButton,
            @NonNull final ExLiveData<Boolean> data) {
        compoundButton.setOnCheckedChangeListener((buttonView, isChecked) -> data.setValueNoEqual(isChecked));
        data.observe(owner, d -> {
            if (d != null) {
                boolean checked = compoundButton.isChecked();
                if (d != checked) {
                    compoundButton.setChecked(d);
                }
            }
        });
    }


    public static void bindSelected(
            @NonNull final LifecycleOwner owner,
            @NonNull final View view,
            @NonNull final ExLiveData<Boolean> data) {
        data.observe(owner, d -> {
            if (d != null) {
                view.setSelected(d);
            }
        });
    }

}
