package com.zd.video;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

/**
 * =====================================
 * 上海众朵信息科技有限公司 研发中心
 * 日期：2019/2/19 下午5:34
 *
 * @author ：junlyuan
 * 版本：1.0
 * 功能描述：
 * <p>
 * 修订日期      修订人        描述
 * 2019/2/19     junlyuan
 * ======================================
 */
public class VideoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
