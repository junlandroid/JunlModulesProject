package com.zd.video;

import com.zd.video.model.GanKNicePicBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * =====================================
 * 上海众朵信息科技有限公司 研发中心
 * 日期：2019/2/19 下午3:27
 *
 * @author ：junlyuan
 * 版本：1.0
 * 功能描述：
 * <p>
 * 修订日期      修订人        描述
 * 2019/2/19     junlyuan
 * ======================================
 */
public interface VideoApi {


    /**
     * 谜语查询接口
     * size      访问数据条数
     * page      页码
     */
    @GET("xiandu/data/id/appinn/count/{size}/page/{page}")
    Observable<GanKNicePicBean> getGanKNicePic(@Path("size") String size,
                                               @Path("page") String page);

}
