package com.yang.assetmanage.ui;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


import com.yang.assetmanage.R;
import com.yang.assetmanage.utils.EncryptUtil;
import com.yang.assetmanage.utils.SPUtil;
import com.yang.assetmanage.view.LockPatternUtils;
import com.yang.assetmanage.view.LockPatternView;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建手势
 *
 * @author hanli
 */
public class GesturePasswordActivity extends BaseActivity {

    private LockPatternView mLockPatternView;

    protected List<LockPatternView.Cell> mChosenPattern = null;

    private TextView mUserTextView, mHeadTextView;
    private Animation mShakeAnim;
    /**
     * 状态
     */
    private int status;

    private View mPreviewViews[][] = new View[3][3];


    boolean isCrete;

    public static String KEY_IS_CREATE = "key_is_create";

    public static String SP_KEY_GESTURE = "sp_key_gesture";

    private int mFailedPatternAttemptsSinceLastTimeout = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.gesturepassword_create_and_unlock;
    }

    @Override
    protected void initView() {
        mLockPatternView = (LockPatternView) this.findViewById(R.id.gesturepwd_lockview);
        mLockPatternView.setOnPatternListener(mChooseNewLockPatternListener);
        mLockPatternView.setTactileFeedbackEnabled(true);
        mHeadTextView = (TextView) findViewById(R.id.gesturepwd_text);
        mUserTextView = (TextView) findViewById(R.id.gesturepwd_user);
        mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake_x);
        ((TextView) findViewById(R.id.gesturepwd_forget)).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.gesturepwd_tip)).setVisibility(View.VISIBLE);
        isCrete = getIntent().getBooleanExtra(KEY_IS_CREATE, true);
        initPreviewViews();
    }

    @Override
    protected void initData() {

    }


    private void initPreviewViews() {
        mPreviewViews = new View[3][3];
        mPreviewViews[0][0] = findViewById(R.id.gesturepwd_setting_preview_0);
        mPreviewViews[0][1] = findViewById(R.id.gesturepwd_setting_preview_1);
        mPreviewViews[0][2] = findViewById(R.id.gesturepwd_setting_preview_2);
        mPreviewViews[1][0] = findViewById(R.id.gesturepwd_setting_preview_3);
        mPreviewViews[1][1] = findViewById(R.id.gesturepwd_setting_preview_4);
        mPreviewViews[1][2] = findViewById(R.id.gesturepwd_setting_preview_5);
        mPreviewViews[2][0] = findViewById(R.id.gesturepwd_setting_preview_6);
        mPreviewViews[2][1] = findViewById(R.id.gesturepwd_setting_preview_7);
        mPreviewViews[2][2] = findViewById(R.id.gesturepwd_setting_preview_8);
    }

    private void updatePreviewViews() {
        if (mChosenPattern == null)
            return;
        for (int i = 0; i < mPreviewViews.length; i++) {
            for (int j = 0; j < mPreviewViews[i].length; j++) {
                mPreviewViews[i][j].setBackgroundResource(R.drawable.bg_white_stoke_circle);
            }
        }
        for (LockPatternView.Cell cell : mChosenPattern) {
            mPreviewViews[cell.getRow()][cell.getColumn()].setBackgroundResource(R.drawable.bg_white_solid_circle);

        }
    }

    private Runnable mClearPatternRunnable = new Runnable() {
        public void run() {
            mLockPatternView.clearPattern();
        }
    };

    protected LockPatternView.OnPatternListener mChooseNewLockPatternListener = new LockPatternView.OnPatternListener() {

        public void onPatternStart() {
            mLockPatternView.removeCallbacks(mClearPatternRunnable);
        }

        public void onPatternCleared() {
            mLockPatternView.removeCallbacks(mClearPatternRunnable);
        }

        public void onPatternDetected(List<LockPatternView.Cell> pattern) {
            if (pattern == null)
                return;
            // Log.i("way", "result = " + pattern.toString());
            if (isCrete) {
                createGesture(pattern);
            } else {
                unLockGesture(pattern);
            }
        }

        public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {

        }

    };


    /**
     * 根据手势密码对象获取密码字符
     *
     * @param pattern
     * @return
     */
    public String GetPassString(List<LockPatternView.Cell> pattern) {
        String pwdString = "";
        for (int i = 0; i < pattern.size(); i++) {
            LockPatternView.Cell cell = pattern.get(i);
            int pwd = cell.getRow() * 3 + cell.getColumn();
            pwdString = pwdString + pwd;
        }
        return pwdString;
    }

    private void createGesture(List<LockPatternView.Cell> pattern) {
        if (status == 0) {
            // 第一次绘制
            if (pattern.size() < LockPatternUtils.MIN_LOCK_PATTERN_SIZE) {
                // showToast("绘制个数不能少于4个");
                mHeadTextView.setText("绘制个数太少");
                mHeadTextView.setTextColor(Color.RED);
                mHeadTextView.startAnimation(mShakeAnim);
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                postClearPatternRunnable();
                return;
            }
            mHeadTextView.setText("请再次绘制您的密码！");
            mHeadTextView.setTextColor(getResources().getColor(R.color.global_white));
            mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
            updatePreviewViews();
            postClearPatternRunnable();
            status++;
        } else if (status == 1) {
            // 第二次确认
            if (mChosenPattern.equals(pattern)) {
                // 绘制成功
                saveChosenPatternAndFinish();
            } else {
                // showToast("两次绘制的不一样,请重新绘制！");
                mHeadTextView.setText("两次绘制的不一样,请重新绘制！");
                mHeadTextView.setTextColor(Color.RED);
                mHeadTextView.startAnimation(mShakeAnim);
                mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
                mChosenPattern.clear();
                updatePreviewViews();
                postClearPatternRunnable();
                status = 0;
            }
        }
    }

    private void unLockGesture(List<LockPatternView.Cell> pattern) {

        if (pattern.size() < LockPatternUtils.MIN_LOCK_PATTERN_SIZE) {
            // showToast("绘制个数不能少于4个");
            mHeadTextView.setText("绘制个数太少");
            mHeadTextView.setTextColor(Color.RED);
            mHeadTextView.startAnimation(mShakeAnim);
            mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
            postClearPatternRunnable();
            return;
        }
        mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Correct);
        try {
            String spPwd = (String) SPUtil.getData(this, SP_KEY_GESTURE, "");
            String encryptPwd = EncryptUtil.encodeByMd5(GetPassString(pattern));
            if (TextUtils.equals(spPwd, encryptPwd)) {
                toActivity(MainActivity.class);
            } else {
                gestureFail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gestureFail() {
        mLockPatternView.setDisplayMode(LockPatternView.DisplayMode.Wrong);
        // if (pattern.size() >=
        // LockPatternUtils.MIN_PATTERN_REGISTER_FAIL) {
        mFailedPatternAttemptsSinceLastTimeout++;
        int retry = LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT - mFailedPatternAttemptsSinceLastTimeout;
        if (retry >= 0) {
            if (retry == 0) {
                mHeadTextView.setText("错误已超过" + LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT + "次，请重新登录！");
            } else {
                mHeadTextView.setText("密码错误，还可以再输入" + retry + "次");
            }
            mHeadTextView.setTextColor(Color.RED);
            mHeadTextView.startAnimation(mShakeAnim);
        }
        if (mFailedPatternAttemptsSinceLastTimeout >= LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT) {
            showMessage("手势密码输入错误已超过" + LockPatternUtils.FAILED_ATTEMPTS_BEFORE_TIMEOUT + "次，请重新登录！");
        } else {
            mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
        }
    }

    // clear the wrong pattern unless they have started a new one
    // already
    private void postClearPatternRunnable() {
        mLockPatternView.removeCallbacks(mClearPatternRunnable);
        mLockPatternView.postDelayed(mClearPatternRunnable, 2000);
    }

    private void saveChosenPatternAndFinish() {
        //保存手势密码
        // AhwApp.getInstance().getLockPatternUtils().saveLockPattern(mChosenPattern);
        try {
            String encryptPwd = EncryptUtil.encodeByMd5(GetPassString(mChosenPattern));
            SPUtil.saveData(this, SP_KEY_GESTURE, encryptPwd);
            toActivity(MainActivity.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
    }

}
