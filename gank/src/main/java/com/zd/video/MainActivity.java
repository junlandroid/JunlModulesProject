package com.zd.video;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zd.library.http.RetrofitWrapper;
import com.zd.library.utils.LogUtils;
import com.zd.video.model.GanKNicePicBean;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RetrofitWrapper.getInstance("http://gank.io/api/")
                .create(VideoApi.class)
                .getGanKNicePic("10", "1")
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<GanKNicePicBean>() {
                    @Override
                    public void accept(GanKNicePicBean ganKNicePicBean) throws Exception {
                        Log.d("TAG", "accept");
                        System.out.println(ganKNicePicBean.toString());
                    }
                });
    }
}
