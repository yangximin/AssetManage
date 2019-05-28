package com.yang.assetmanage.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.yang.assetmanage.R;


/**
 * Created by yangxm on 2019/4/25.
 */

public class MyEditTextView extends android.support.v7.widget.AppCompatEditText implements View.OnFocusChangeListener {
    /**
     * 输入type
     */
    private Type mType;

    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;
    /**
     * 查看明文密码图片
     */
    private Drawable mVisiblePasswordDrawable, mInvisiblePasswordDrawable;
    /**
     * 控件是否有焦点
     */
    private boolean hasFocus;
    /*密码输入*/
    private final int INPUT_TYPE_PASSWORD = 1;
    /**
     *
     */
    private final int INPUT_TYPE_SMS = 2;

    private final int INPUT_TYPE_TEXT = 10;


    private boolean isShowPassword;

    /**
     * 是否实时校验正则
     */
    private boolean isChangeShow = false;

    public MyEditTextView(Context context) {
        this(context, null);
    }

    public MyEditTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public MyEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mType = new Type(context, attrs);
        initView(context);
    }

    public void setType(Type type) {
        mType = type;
        initView(getContext());
    }

    private void initView(Context context) {
        setInitBackground();
        initCompoundDrawables(context);
        setOnFocusChangeListener(this);
        setTextChangedListener();
        setEditTextInputType(mType.inputType);
        initHint();
    }

    private void setInitBackground() {
        //框背景
//      EditText背景
//      1:背景框
//      2.下画线
//      3.圆形框
//      4.自定义
        int BG_TYPE_FRAME = 1;
        int BG_TYPE_BOTTOM_LINE = 2;
        int BG_TYPE_ROUND_FRAME = 3;
        int BG_TYPE_CUSTOM = 4;

        if (mType.backgroundType == BG_TYPE_FRAME) {
            this.setBackgroundResource(R.drawable.base_component_rectangle_line_bg);
        }
        //底部线
        else if (mType.backgroundType == BG_TYPE_BOTTOM_LINE) {
            this.setBackgroundResource(R.drawable.base_component_bottom_line_bg);
        }
        //圆形框
        else if (mType.backgroundType == BG_TYPE_ROUND_FRAME) {
            this.setBackgroundResource(R.drawable.base_component_round_line_bg);
        }
        //自定义
        else if (mType.backgroundType == BG_TYPE_CUSTOM) {
            this.setBackground(getBackground());
        } else {
            this.setBackground(null);
        }
    }

    /**
     * 初始化左右按钮
     */
    private void initCompoundDrawables(Context context) {
        if (mType.inputType == INPUT_TYPE_PASSWORD) {
            mVisiblePasswordDrawable = getResDrawable(context, R.drawable.base_component_pwd_visible_icon);
            mInvisiblePasswordDrawable = getResDrawable(context, R.drawable.base_component_pwd_invisible_icon);
            setPwdCompoundDrawables(false);
        } else if (mType.inputType == INPUT_TYPE_SMS) {
            //不显示清除
            mClearDrawable = null;
        } else {
            //默认不显示
            //mClearDrawable = getResDrawable(context, R.drawable.zac_base_component_del_input_icon);

            mClearDrawable = null;
        }
    }

    /**
     * 设置控件是否可见
     *
     * @param isVisible
     */
    private void setPwdCompoundDrawables(boolean isVisible) {
        Drawable leftDrawable = getCompoundDrawables()[0];
        if (isVisible) {
            this.setCompoundDrawables(leftDrawable, null, mVisiblePasswordDrawable, null);
        } else {
            this.setCompoundDrawables(leftDrawable, null, mInvisiblePasswordDrawable, null);
        }
        // 设置光标在最后面
        Editable editable = getText();
        Selection.setSelection(editable, editable.length());
    }

    private void setEditTextInputType(int inputType) {
        // 显示明文
        if (inputType == INPUT_TYPE_PASSWORD) {
            setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else if (inputType == INPUT_TYPE_TEXT) {
            // 显示明文
            setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }


    /**
     * 设置hint大小,原hint字体过大
     */
    private void initHint() {
        CharSequence hint = getHint();
        if (hint == null) {
            return;
        }
        SpannableString ss = new SpannableString(hint);//定义hint的值
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(15, true);//设置字体大小 true表示单位是sp
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setHint(new SpannedString(ss));


        try {
            java.lang.reflect.Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(this, R.drawable.base_color_cursor_bg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setTextChangedListener() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isEnabled() && hasFocus) {
                    setClearIconVisible(s.length() > 0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private Drawable getResDrawable(Context context, int id) {
        Drawable drawable = context.getResources().getDrawable(id);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            return drawable;
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()) && (event.getX() < ((getWidth() - getPaddingRight())));
            //查看密码
            if (touchable && mType.inputType == INPUT_TYPE_PASSWORD && mVisiblePasswordDrawable != null) {
                isShowPassword = !isShowPassword;
                if (isShowPassword) {
                    setEditTextInputType(INPUT_TYPE_TEXT);
                    setPwdCompoundDrawables(true);
                } else {
                    setEditTextInputType(INPUT_TYPE_PASSWORD);
                    setPwdCompoundDrawables(false);
                }

            }
            //清除数据
            else if (touchable && mClearDrawable != null && getCompoundDrawables()[2] == mClearDrawable) {
                this.setText("");
            }
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
            setSelection(0);
        }
    }

    protected void setClearIconVisible(boolean visible) {
        if (mClearDrawable == null) {
            return;
        }
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
    }

    public void setViewText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        setText(text);
    }

    /**
     * 对样式的初始化
     */
    public final static class Type {

        int leftIcon;

        int rightIcon;

        int inputType;

        int backgroundType;

        Type(Context context, AttributeSet attrs) {
            if (attrs == null) {
                return;
            }
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyEditTextView);
            leftIcon = typedArray.getResourceId(R.styleable.MyEditTextView_my_edt_left_icon, leftIcon);
            rightIcon = typedArray.getResourceId(R.styleable.MyEditTextView_my_edt_right_icon, rightIcon);
            inputType = typedArray.getInt(R.styleable.MyEditTextView_my_edt_input_type, inputType);
            backgroundType = typedArray.getInt(R.styleable.MyEditTextView_my_edt_bg_type, backgroundType);
            typedArray.recycle();
        }
    }
}

