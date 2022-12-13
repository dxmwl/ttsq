package com.shengqiangou.mobile.ui.fragment

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shengqiangou.mobile.R
import com.shengqiangou.mobile.aop.SingleClick
import com.shengqiangou.mobile.app.TitleBarFragment
import com.shengqiangou.mobile.http.api.ClassApi
import com.shengqiangou.mobile.http.model.HttpData
import com.shengqiangou.mobile.ui.activity.HomeActivity
import com.shengqiangou.mobile.ui.activity.SearchActivity
import com.shengqiangou.mobile.ui.adapter.HdkBigClassAdapter
import com.shengqiangou.mobile.ui.adapter.HdkTwoClassAdapter
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