package com.ttsq.mobile.ui.fragment

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hjq.http.EasyHttp
import com.hjq.http.listener.HttpCallback
import com.hjq.http.listener.OnHttpListener
import com.ttsq.mobile.R
import com.ttsq.mobile.aop.SingleClick
import com.ttsq.mobile.app.AppFragment
import com.ttsq.mobile.http.api.GetBdshBannerApi
import com.ttsq.mobile.http.api.GetMenuListApi
import com.ttsq.mobile.http.api.SwitchActivityLinkApi
import com.ttsq.mobile.http.model.HttpData
import com.ttsq.mobile.http.model.MenuDto
import com.ttsq.mobile.ui.activity.BrowserActivity
import com.ttsq.mobile.ui.activity.HomeActivity
import com.ttsq.mobile.ui.adapter.BannerBdshAdapter
import com.ttsq.mobile.ui.adapter.HomeMenuListAdapter
import com.youth.banner.Banner
import java.lang.Exception

/**
 * @ClassName: BdshFragment
 * @Description:
 * @Author: 常利兵
 * @Date: 2024/2/21 0021 14:54
 **/
class BdshFragment: AppFragment<HomeActivity>() {

    companion object {
        fun newInstance(): BdshFragment {
            return BdshFragment()
        }
    }

    private lateinit var homeMenuListAdapter: HomeMenuListAdapter
    private var bannerBdshAdapter: BannerBdshAdapter? = null
    private val banner: Banner<GetBdshBannerApi.TimeActivity, BannerBdshAdapter>? by lazy { findViewById(R.id.banner) }
    private val menuList: RecyclerView? by lazy { findViewById(R.id.menu_list) }
    private var meituanWmDto: ArrayList<GetBdshBannerApi.RedActivity>? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_bdsh
    }

    override fun initView() {

        setOnClickListener(R.id.btn_meituan)

        banner?.let {
//            it.setBannerGalleryEffect(39, 16)
            it.addBannerLifecycleObserver(this)
        }
        bannerBdshAdapter = BannerBdshAdapter(ArrayList())
        banner?.setAdapter(bannerBdshAdapter)
        bannerBdshAdapter?.setOnItemClickListener(object :BannerBdshAdapter.OnItemClick{
            override fun clickItem(data: GetBdshBannerApi.TimeActivity?) {
                switchActivityLink(data?.activity_id)
            }
        })
        menuList?.let {
            it.layoutManager = GridLayoutManager(context, 5)
            homeMenuListAdapter = HomeMenuListAdapter(requireContext())
            it.adapter = homeMenuListAdapter
        }
    }

    override fun initData() {
        getBannerList()
        getMenuList(0)
    }

    private fun getMenuList(type: Int) {
        EasyHttp.post(this)
            .api(GetMenuListApi().apply {
                this.type = type
            })
            .request(object :OnHttpListener<HttpData<ArrayList<GetMenuListApi.AppMenuDto>>>{
                override fun onSucceed(result: HttpData<ArrayList<GetMenuListApi.AppMenuDto>>?) {
                    result?.getData()?.let {
                        homeMenuListAdapter.setData(it)
                    }
                }

                override fun onFail(e: Exception?) {
                    
                }

            })
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_meituan -> {
                if (meituanWmDto.isNullOrEmpty()){
                    return
                }
                switchActivityLink(meituanWmDto?.get(0)?.activity_id)
            }
            else -> {}
        }
    }
    private fun getBannerList() {
        EasyHttp.get(this)
            .api(GetBdshBannerApi())
            .request(object :OnHttpListener<HttpData<GetBdshBannerApi.BdshBannerDto>>{
                override fun onSucceed(result: HttpData<GetBdshBannerApi.BdshBannerDto>?) {
                    result?.getData()?.let {
                        meituanWmDto = it.red_activity
                        bannerBdshAdapter?.setDatas(it.time_activity)
                    }
                }

                override fun onFail(e: Exception?) {
                    toast(e?.message)
                }

            })
    }

    /**
     * 美团活动转链
     */
    private fun switchActivityLink(activityId: String?) {
        EasyHttp.post(this)
            .api(SwitchActivityLinkApi().apply {
                activity_id = activityId
            })
            .request(object : HttpCallback<HttpData<SwitchActivityLinkApi.SwitchActivityLinkDto>>(this){
                override fun onSucceed(result: HttpData<SwitchActivityLinkApi.SwitchActivityLinkDto>?) {
                    result?.getData()?.let {
                        BrowserActivity.start(requireContext(),it.url)
                    }
                }
            })
    }
}