package com.shengqianjun.mobile.ui.fragment

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hjq.base.BaseAdapter
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.shengqianjun.mobile.R
import com.shengqianjun.mobile.app.AppFragment
import com.shengqianjun.mobile.http.api.RecommendPinpaiApi
import com.shengqianjun.mobile.http.api.ZhutiPinpaiApi
import com.shengqianjun.mobile.http.model.HttpData
import com.shengqianjun.mobile.ui.activity.GoodsDetailActivity
import com.shengqianjun.mobile.ui.activity.PinpaiGoodsActivity
import com.shengqianjun.mobile.ui.adapter.PinpaiGoodsAdapter
import com.shengqianjun.mobile.ui.adapter.ZhutiPinpaiListAdapter
import java.lang.Exception

class PinpaiRecommendFragment: AppFragment<PinpaiGoodsActivity>() {

    companion object {

        fun newInstance(): PinpaiRecommendFragment {
            return PinpaiRecommendFragment()
        }
    }

    private lateinit var zhutiPinpaiListAdapter: ZhutiPinpaiListAdapter
    private val pinpai_banner: ImageView? by lazy { findViewById(R.id.pinpai_banner) }
    private val logo_img: ImageView? by lazy { findViewById(R.id.logo_img) }
    private val textView11: TextView? by lazy { findViewById(R.id.textView11) }
    private val pinpai_biaoyu: TextView? by lazy { findViewById(R.id.pinpai_biaoyu) }
    private val pinpai_goods_list: RecyclerView? by lazy { findViewById(R.id.pinpai_goods_list) }
    private val zhuti_pinpai_list: RecyclerView? by lazy { findViewById(R.id.zhuti_pinpai_list) }
    private lateinit var pinpaiGoodsAdapter: PinpaiGoodsAdapter

    override fun getLayoutId(): Int {
        return R.layout.fragment_pinpai_recomend
    }

    override fun initView() {
        pinpai_goods_list?.let {
            it.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            pinpaiGoodsAdapter = PinpaiGoodsAdapter(requireContext())
            pinpaiGoodsAdapter.setOnItemClickListener(object : BaseAdapter.OnItemClickListener{
                override fun onItemClick(
                    recyclerView: RecyclerView?,
                    itemView: View?,
                    position: Int,
                ) {
                    val item = pinpaiGoodsAdapter.getItem(position)
                    GoodsDetailActivity.start(requireContext(),item.itemid)
                }

            })
            it.adapter = pinpaiGoodsAdapter
        }

        zhuti_pinpai_list?.let { 
            it.layoutManager = LinearLayoutManager(requireContext())
            zhutiPinpaiListAdapter = ZhutiPinpaiListAdapter(requireContext())
            it.adapter = zhutiPinpaiListAdapter
        }
    }

    override fun initData() {
        getRecommendPinpaiList()
        getZhutiPinpai()
    }

    private fun getZhutiPinpai() {
        EasyHttp.get(this)
            .api(ZhutiPinpaiApi())
            .request(object : OnHttpListener<HttpData<ArrayList<ZhutiPinpaiApi.ZhutiPinpaiDto>>> {
                override fun onSucceed(result: HttpData<ArrayList<ZhutiPinpaiApi.ZhutiPinpaiDto>>?) {
                    result?.getData()?.let {
                        zhutiPinpaiListAdapter.setData(it)
                    }
                }

                override fun onFail(e: Exception?) {
                    toast(e?.message)
                }

            })
    }

    private fun getRecommendPinpaiList() {
        EasyHttp.get(this)
            .api(RecommendPinpaiApi())
            .request(object : OnHttpListener<HttpData<RecommendPinpaiApi.RecommendPinpaiDto>> {
                override fun onSucceed(result: HttpData<RecommendPinpaiApi.RecommendPinpaiDto>?) {
                    result?.getData()?.let {
                        pinpai_banner?.let { it1 ->
                            Glide.with(this@PinpaiRecommendFragment).load(it.data.background).into(it1)
                        }
                        logo_img?.let { it1 ->
                            Glide.with(this@PinpaiRecommendFragment).load(it.data.brand_logo).into(it1)
                        }
                        textView11?.text = it.data.name
                        pinpai_biaoyu?.text = it.data.title
                        pinpaiGoodsAdapter.setData(it.items)

                    }
                }

                override fun onFail(e: java.lang.Exception?) {
                    toast(e?.message)
                }
            })
    }
}