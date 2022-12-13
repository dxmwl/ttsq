package com.shengqiangou.mobile.ui.fragment

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shengqiangou.mobile.R
import com.shengqiangou.mobile.aop.SingleClick
import com.shengqiangou.mobile.app.AppHelper
import com.shengqiangou.mobile.app.TitleBarFragment
import com.shengqiangou.mobile.ui.activity.HomeActivity
import com.shengqiangou.mobile.ui.activity.SearchActivity
import com.shengqiangou.mobile.ui.activity.SearchResultActivity
import com.shengqiangou.mobile.ui.adapter.BigClassAdapter
import com.shengqiangou.mobile.ui.adapter.TwoClassAdapter
import com.hjq.base.BaseAdapter
import com.hjq.shape.view.ShapeTextView
import com.shengqiangou.mobile.http.api.ClassApi
import java.util.ArrayList

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 商品分类 Fragment
 */
class ZtkClassFragment : TitleBarFragment<HomeActivity>(), TwoClassAdapter.ThreeClassAdapter.OnItemClickListener {

    companion object {

        fun newInstance(): ZtkClassFragment {
            return ZtkClassFragment()
        }
    }

    private lateinit var twoClassAdapter: TwoClassAdapter
    private val bigClass: RecyclerView? by lazy { findViewById(R.id.big_class) }
    private val twoClassList: RecyclerView? by lazy { findViewById(R.id.two_class_list) }
    private val searchView: ShapeTextView? by lazy { findViewById(R.id.search_view) }

    override fun getLayoutId(): Int {
        return R.layout.ztk_class_fragment
    }

    override fun initView() {
        setOnClickListener(searchView)
        twoClassList?.let {
            it.layoutManager = LinearLayoutManager(context)
            twoClassAdapter = TwoClassAdapter(this)
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
        val classData = AppHelper.classData
        if (classData.isEmpty()){
            return
        }
        bigClass?.let { it ->
            classData[0].checked = true

            it.layoutManager = LinearLayoutManager(context)
            val bigClassAdapter = context?.let { it1 -> BigClassAdapter(it1) }
            bigClassAdapter?.setOnItemClickListener(object :
                BaseAdapter.OnItemClickListener {
                override fun onItemClick(
                    recyclerView: RecyclerView?,
                    itemView: View?,
                    position: Int
                ) {
                    val classInfo = classData[position]
                    classData.forEachIndexed { index, menuDto ->
                        menuDto.checked = false
                        bigClassAdapter.notifyItemChanged(index)
                    }
                    classInfo.checked = true
                    bigClassAdapter.notifyItemChanged(position)

                    getTwoClassData(classInfo.data)
                }
            })
            it.adapter = bigClassAdapter
            bigClassAdapter?.setData(classData)

            getTwoClassData(classData[0].data)
        }
    }

    private fun getTwoClassData(childs: ArrayList<ClassApi.Data>) {
        twoClassAdapter.setData(childs)
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
        val intent = Intent(getAttachActivity(), SearchResultActivity::class.java)
        intent.putExtra("KEYWORD", classDataBean.son_name)
        startActivity(intent)
    }
}