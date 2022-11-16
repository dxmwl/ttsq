package com.easybuy.mobile.ui.fragment

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.easybug.mobile.R
import com.easybuy.mobile.aop.SingleClick
import com.easybuy.mobile.app.TitleBarFragment
import com.easybuy.mobile.http.api.ClassApi
import com.easybuy.mobile.http.glide.GlideApp
import com.easybuy.mobile.http.model.HttpData
import com.easybuy.mobile.ui.activity.HomeActivity
import com.easybuy.mobile.ui.activity.SearchActivity
import com.easybuy.mobile.ui.adapter.IndustryListAdapter
import com.easybuy.mobile.ui.adapter.ServiceCategoryListAdapter
import com.hjq.base.BaseAdapter
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.shape.view.ShapeTextView
import com.hjq.widget.view.CountdownView
import com.hjq.widget.view.SwitchButton

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 发现 Fragment
 */
class FindFragment : TitleBarFragment<HomeActivity>(),
    ServiceCategoryListAdapter.OnItemClickListener {

    companion object {

        fun newInstance(): FindFragment {
            return FindFragment()
        }
    }

    private lateinit var serviceCategoryListAdapter: ServiceCategoryListAdapter
    private val industryList: RecyclerView? by lazy { findViewById(R.id.industry_list) }
    private val serviceList: RecyclerView? by lazy { findViewById(R.id.service_list) }
    private val searchView: ShapeTextView? by lazy { findViewById(R.id.search_view) }

    override fun getLayoutId(): Int {
        return R.layout.find_fragment
    }

    override fun initView() {
        setOnClickListener(searchView)
        serviceList?.let {
            it.layoutManager = LinearLayoutManager(context)
            serviceCategoryListAdapter = ServiceCategoryListAdapter(this)
            it.adapter = serviceCategoryListAdapter
        }
    }

    override fun initData() {
        getClassData()

    }

    /**
     * 获取分类数据
     */
    private fun getClassData() {
        EasyHttp.get(this)
            .api(ClassApi())
            .request(object : OnHttpListener<HttpData<ArrayList<ClassApi.ClassInfo>>> {
                override fun onSucceed(result: HttpData<ArrayList<ClassApi.ClassInfo>>?) {
                    val classData = result?.getData()
                    industryList?.let { it ->

                        classData?.get(0)?.checked = true

                        it.layoutManager = LinearLayoutManager(context)
                        val industryListAdapter = context?.let { it1 -> IndustryListAdapter(it1) }
                        industryListAdapter?.setOnItemClickListener(object :
                            BaseAdapter.OnItemClickListener {
                            override fun onItemClick(
                                recyclerView: RecyclerView?,
                                itemView: View?,
                                position: Int
                            ) {
                                classData?.forEachIndexed { index, menuDto ->
                                    menuDto.checked = false
                                    industryListAdapter.notifyItemChanged(index)
                                }
                                classData?.get(position)?.checked = true
                                industryListAdapter.notifyItemChanged(position)

                                try {
                                    serviceCategoryListAdapter.let {
                                        classData?.get(position)?.data?.let {
                                            serviceCategoryListAdapter.setData(it)
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })
                        it.adapter = industryListAdapter
                        industryListAdapter?.setData(classData)

                        serviceCategoryListAdapter.let {
                            classData?.get(0)?.data?.let {
                                serviceCategoryListAdapter.setData(it)
                            }
                        }
                    }
                }

                override fun onFail(e: java.lang.Exception?) {
                    toast(e?.message)
                }

            })
    }

    @SingleClick
    override fun onClick(view: View) {
        when (view) {
            searchView -> {
                startActivity(SearchActivity::class.java)
            }
            else -> {}
        }
    }

    override fun isStatusBarEnabled(): Boolean {
        return false
    }

    override fun onItemClick(classDataBean: ClassApi.Info) {
//        getAttachActivity()?.let { SearchResultActivity.start(it, classDataBean.title) }
    }
}