package com.easybuy.mobile.ui.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easybug.mobile.R
import com.easybuy.mobile.aop.SingleClick
import com.easybuy.mobile.app.TitleBarFragment
import com.easybuy.mobile.http.api.ClassApi
import com.easybuy.mobile.http.model.HttpData
import com.easybuy.mobile.ui.activity.HomeActivity
import com.easybuy.mobile.ui.activity.SearchActivity
import com.easybuy.mobile.ui.adapter.BigClassAdapter
import com.easybuy.mobile.ui.adapter.HdkBigClassAdapter
import com.easybuy.mobile.ui.adapter.HdkTwoClassAdapter
import com.easybuy.mobile.ui.adapter.TwoClassAdapter
import com.hjq.base.BaseAdapter
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.shape.view.ShapeTextView

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 发现 Fragment
 */
class ClassFragment : TitleBarFragment<HomeActivity>(),
    HdkTwoClassAdapter.OnItemClickListener {

    companion object {

        fun newInstance(): ClassFragment {
            return ClassFragment()
        }
    }

    private lateinit var twoClassAdapter: HdkTwoClassAdapter
    private val bigClass: RecyclerView? by lazy { findViewById(R.id.big_class) }
    private val twoClassList: RecyclerView? by lazy { findViewById(R.id.two_class_list) }
    private val searchView: ShapeTextView? by lazy { findViewById(R.id.search_view) }

    override fun getLayoutId(): Int {
        return R.layout.class_fragment
    }

    override fun initView() {
        setOnClickListener(searchView)
        twoClassList?.let {
            it.layoutManager = LinearLayoutManager(context)
            twoClassAdapter = HdkTwoClassAdapter(this)
            it.adapter = twoClassAdapter
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
                    bigClass?.let { it ->

                        classData?.get(0)?.checked = true

                        it.layoutManager = LinearLayoutManager(context)
                        val bigClassAdapter = context?.let { it1 -> HdkBigClassAdapter(it1) }
                        bigClassAdapter?.setOnItemClickListener(object :
                            BaseAdapter.OnItemClickListener {
                            override fun onItemClick(
                                recyclerView: RecyclerView?,
                                itemView: View?,
                                position: Int
                            ) {
                                classData?.forEachIndexed { index, menuDto ->
                                    menuDto.checked = false
                                    bigClassAdapter.notifyItemChanged(index)
                                }
                                classData?.get(position)?.checked = true
                                bigClassAdapter.notifyItemChanged(position)

                                try {
                                    twoClassAdapter.let {
                                        classData?.get(position)?.data?.let {
                                            twoClassAdapter.setData(it)
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        })
                        it.adapter = bigClassAdapter
                        bigClassAdapter?.setData(classData)

                        twoClassAdapter.let {
                            classData?.get(0)?.data?.let {
                                twoClassAdapter.setData(it)
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