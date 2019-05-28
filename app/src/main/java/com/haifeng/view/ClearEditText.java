package com.haifeng.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.haifeng.R;

import java.util.regex.Pattern;

@SuppressLint("AppCompatCustomView")
public class ClearEditText extends EditText {

    /**
     * 清楚按钮的图标
     */
    private Drawable drawableClear;
    private static final String PATTERN_IDCARD = "([0-9]{17}([0-9]|X|x))|([0-9]{15})";
    /**
     * 设置银行卡四位一空格
     */


    int beforeTextLength = 0;
    int onTextLength = 0;
    boolean isChanged = false;
    int location = 0;
    private char[] tempChar;
    private StringBuffer buffer = new StringBuffer();
    int konggeNumberB = 0;

    private OnTextLengthListener mOnTextLengthListener;


    private boolean isBankNoType;

    private int maxLength;


    /**
     * 最大长度
     *
     * @param maxLength
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        invalidate();
    }


    /**
     * 银行卡类型
     *
     * @param bankNoType
     */
    public void setBankNoType(boolean bankNoType) {
        isBankNoType = bankNoType;
        invalidate();
    }

    public interface OnTextLengthListener {
        /**
         * 按钮可点击
         */
        void onButtonEnable();

        /**
         * 按钮不可点击
         */
        void onButtonUnEnable();
    }

    public void setOnTextLengthListener(OnTextLengthListener onTextLengthListener) {
        this.mOnTextLengthListener = onTextLengthListener;
    }


    public ClearEditText(Context context) {
        super(context);
        init(context, null);

    }

    public ClearEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ClearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 获取自定义属性
        drawableClear = getResources().getDrawable(R.drawable.delete_selector);
        updateIconClear();

        // 设置TextWatcher用于更新清除按钮显示状态
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (isBankNoType) {
                    beforeTextLength = s.length();
                    if (buffer.length() > 0) {
                        buffer.delete(0, buffer.length());
                    }
                    konggeNumberB = 0;
                    for (int i = 0; i < s.length(); i++) {
                        if (s.charAt(i) == ' ') {
                            konggeNumberB++;
                        }
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isBankNoType) {

                    onTextLength = s.length();
                    buffer.append(s.toString());
                    if (onTextLength == beforeTextLength || onTextLength <= 3 || isChanged) {
                        isChanged = false;
                        return;
                    }
                    isChanged = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateIconClear();
                if (isChanged && isBankNoType) {
                    location = getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == ' ') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }

                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        if ((index == 4 || index == 9 || index == 14 || index == 19)) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                        index++;
                    }

                    if (konggeNumberC > konggeNumberB) {
                        location += (konggeNumberC - konggeNumberB);
                    }

                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if (location > str.length()) {
                        location = str.length();
                    } else if (location < 0) {
                        location = 0;
                    }

                    setText(str);
                    Editable etable = getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                }
                sendLengthState(s, maxLength);
            }
        });

        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                updateIconClear();
            }
        });

    }

    /**
     * 发送editText的长度符合要求的状态
     *
     * @param s
     * @param maxLength
     */
    private void sendLengthState(Editable s, int maxLength) {
        int etLength = s.length();
        String etText = s.toString();
        if (maxLength == 23) {
            //银行卡
            if (etLength <= 23 && etLength >= 18) {
                etText.replaceAll(" ", "");
                dispatchEnable();
            } else {
                dispatchUnEnable();
            }
        } else if (maxLength == 18 && checkIdNumber(etText)) {
            //身份证
            if (maxLength == etLength) {
                dispatchEnable();
            } else {
                dispatchUnEnable();
            }
        } else if (maxLength != 0) {
            //纯数字
            if (maxLength == etLength) {
                dispatchEnable();
            } else {
                dispatchUnEnable();
            }
        } else {
            //姓名类
            if (etLength != 0) {
                dispatchEnable();
            } else {
                dispatchUnEnable();
            }

        }
    }

    private void dispatchEnable() {
        if (mOnTextLengthListener != null) {
            mOnTextLengthListener.onButtonEnable();
        }
    }

    private void dispatchUnEnable() {
        if (mOnTextLengthListener != null) {
            mOnTextLengthListener.onButtonUnEnable();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int xDown = (int) event.getX();
        if (event.getAction() == MotionEvent.ACTION_DOWN && xDown >= (getWidth() - getCompoundPaddingRight() * 2) && xDown < getWidth()) {
            // 清除按钮的点击范围 按钮自身大小 +-padding
            setText("");
            return false;
        }
        super.onTouchEvent(event);
        return true;
    }


    /**
     * 更新清除按钮图标显示
     */
    private void updateIconClear() {
        // 获取设置好的drawableLeft、drawableTop、drawableRight、drawableBottom
        Drawable[] drawables = getCompoundDrawables();
        if (length() > 0 && isFocused()) {
            showIcon(true, drawables);
        } else {
            showIcon(false, drawables);
        }
    }

    private void showIcon(boolean isShow, Drawable[] drawables) {
        if (isShow) {
            setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], drawableClear,
                    drawables[3]);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(drawables[0], drawables[1], null,
                    drawables[3]);
        }
    }

    /**
     * 清空文本的方法
     */
    public void clearText() {
        setText("");
    }


    /**
     * 判断身份证位数或格式的正确性
     *
     * @param idNumber
     * @return
     */

    public boolean checkIdNumber(String idNumber) {
        return Pattern.matches(PATTERN_IDCARD, idNumber);

    }


}

