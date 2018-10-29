package com.hour24.ygy.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.hour24.ygy.R;
import com.hour24.ygy.adapter.UserListAdapter;
import com.hour24.ygy.databinding.MainActivityBinding;
import com.hour24.ygy.model.UserModel;
import com.hour24.ygy.network.retrofit.RetrofitCall;
import com.hour24.ygy.network.retrofit.RetrofitRequest;
import com.hour24.ygy.network.retrofit.service.GitHubService;
import com.hour24.ygy.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = MainActivity.class.getName();

    private MainActivityBinding mBinding;

    private UserListAdapter mAdapter;

    private ArrayList<UserModel.Item> mShowList; // 화면에 실제로 보여주는 리스트
    private ArrayList<UserModel.Item> mUserList; // 검색된 유저 리스트
    private ArrayList<UserModel.Item> mLikeList; // Like 된 리스트

    private boolean mIsUserList = true; // 화면에 어떤 종류를 보여줄 것인가 유무

    // Delay Handler - 특정시간 입력 이후 서버 데이터 전송
    private Runnable mDelayRunnable;
    private Handler mDelayHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);

        initLayout();
        initVariable();
        initEventListener();
        onRecognizeDelay();

        mBinding.search.setText("tom");
        requestSearchUser("tom");

    }

    @Override
    public void onStop() {
        super.onStop();
        // Handler Remove
        if (mDelayHandler != null) {
            mDelayHandler.removeCallbacks(mDelayRunnable);
        }
    }

    private void initLayout() {

        // binding
        mBinding.setHandler(new BindingHandler());
        mBinding.setIsUserList(mIsUserList);

        mBinding.showList.setLayoutManager(new GridLayoutManager(this, 3));

        // like like
        mBinding.userList.setOnClickListener(this);
        mBinding.likeList.setOnClickListener(this);

        // 엔터키
//        mBinding.search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mBinding.search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Utils.setKeyboardShowHide(MainActivity.this, mBinding.search, false);
                        }
                    });

                    if (v.getText().toString().trim().length() != 0) {
                        requestSearchUser(v.getText().toString().trim());
                    }
                    return true;
                }
                return false;
            }
        });

    }

    private void initVariable() {

        mShowList = new ArrayList<>();
        mUserList = new ArrayList<>();
        mLikeList = new ArrayList<>();

        mAdapter = new UserListAdapter(this, mShowList);
        mBinding.showList.setAdapter(mAdapter);

    }

    private void initEventListener() {

    }

    // 인식 딜레이
    private void onRecognizeDelay() {
        mDelayHandler = new Handler();
        mDelayRunnable = new Runnable() {
            @Override
            public void run() {
                // run 이 되는 순간 입력완료라고 판단
                if (mBinding.search.length() > 0) {
                    // 자동완성 데이터 통신
                    requestSearchUser(mBinding.search.getText().toString());
                }
            }
        };
    }

    /**
     * User 검색
     */
    private void requestSearchUser(final String search) {

        // 딜레이 해제
        mDelayHandler.removeCallbacks(mDelayRunnable);

        setIsUserList(true);

        GitHubService service = RetrofitRequest.createRetrofitJSONService(GitHubService.class, "https://api.github.com/");

        Call<UserModel> call = service.getUserList(
                "620d007b3da1488f4c9d",
                "e2004e763172eae9b59f75f352c99e8678dbe082",
                search, "", "");

        RetrofitCall.enqueueWithRetry(call, new Callback<UserModel>() {

            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                try {

                    mUserList.clear();
                    mLikeList.clear();
                    mShowList.clear();

                    if (search.length() < 1) {
                        mAdapter.notifyDataSetChanged();
                        return;
                    }

                    if (response.body() != null) {

                        UserModel model = response.body();
                        if (model != null) {
                            mUserList.addAll(model.getItems());
                            mShowList.addAll(mUserList);

                            mAdapter.notifyDataSetChanged();
                            mBinding.showList.scheduleLayoutAnimation();
                        }

                        Log.e(TAG, "" + model.toString());
                    } else {
                        Log.e(TAG, "" + response.errorBody().string());
                        Snackbar.make(mBinding.getRoot(), getString(R.string.main_search_empty), Snackbar.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                    Snackbar.make(mBinding.getRoot(), getString(R.string.main_search_empty), Snackbar.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View view) {

        Utils.setKeyboardShowHide(this, view, false);

        switch (view.getId()) {
            case R.id.user_list:

                // 검색한 User List 보여줌
                setIsUserList(true);

                mShowList.clear();
                mShowList.addAll(mUserList);

                // 리스트 상태 변화
                mAdapter.notifyDataSetChanged();

                break;

            case R.id.like_list:

                // 선택된 아이템만 가져온다.
                mLikeList = mAdapter.getLikeList();

                if (mLikeList.size() > 0) {

                    setIsUserList(false);

                    mShowList.clear();
                    mShowList.addAll(mLikeList);

                    // 리스트 상태 변화
                    mAdapter.notifyDataSetChanged();
                } else {
                    Snackbar.make(view, getString(R.string.main_like_empty), Snackbar.LENGTH_SHORT).show();
                }

                break;
        }
    }

    // 검색화면 결과 보여줄 것인지 유무 Flag
    private void setIsUserList(boolean isUserList) {
        mIsUserList = isUserList;
        mBinding.setIsUserList(mIsUserList);
        mAdapter.setIsUserList(mIsUserList);
    }

    public class BindingHandler {

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                mDelayHandler.postDelayed(mDelayRunnable, 500); // 0.5 초 딜레이
            } else {
                mUserList.clear();
                mLikeList.clear();
                mShowList.clear();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

}
