package com.easybuy.mobile.ui.fragment

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easybug.mobile.R
import com.easybuy.mobile.aop.SingleClick
import com.easybuy.mobile.app.AppHelper
import com.easybuy.mobile.app.TitleBarFragment
import com.easybuy.mobile.http.api.ZtkClassApi
import com.easybuy.mobile.http.model.HttpData
import com.easybuy.mobile.ui.activity.HomeActivity
import com.easybuy.mobile.ui.activity.SearchActivity
import com.easybuy.mobile.ui.activity.SearchResultActivity
import com.easybuy.mobile.ui.adapter.BigClassAdapter
import com.easybuy.mobile.ui.adapter.TwoClassAdapter
import com.hjq.base.BaseAdapter
import com.hjq.http.EasyHttp
import com.hjq.http.listener.OnHttpListener
import com.hjq.shape.view.ShapeTextView
import java.util.ArrayList

/**
 *    author : Android 轮子哥
 *    github : https://github.com/getActivity/AndroidProject-Kotlin
 *    time   : 2018/10/18
 *    desc   : 商品分类 Fragment
 */
class ZtkClassFragment : TitleBarFragment<HomeActivity>(),
    TwoClassAdapter.ThreeClassAdapter.OnItemClickListener {

    companion object {

        fun newInstance(): ZtkClassFragment {
            return ZtkClassFragment()
        }
    }

    private lateinit var twoClassAdapter: TwoClassAdapter.ThreeClassAdapter
    private val bigClass: RecyclerView? by lazy { findViewById(R.id.big_class) }
    private val twoClassList: RecyclerView? by lazy { findViewById(R.id.two_class_list) }
    private val searchView: ShapeTextView? by lazy { findViewById(R.id.search_view) }

    override fun getLayoutId(): Int {
        return R.layout.ztk_class_fragment
    }

    override fun initView() {
        setOnClickListener(searchView)
        twoClassList?.let {
            it.layoutManager = GridLayoutManager(context, 2)
            twoClassAdapter = TwoClassAdapter.ThreeClassAdapter(requireContext(), this)
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

                    getTwoClassData(classInfo.childs)
                }
            })
            it.adapter = bigClassAdapter
            bigClassAdapter?.setData(classData)

            getTwoClassData(classData[0].childs)
        }
    }

    private fun getTwoClassData(childs: ArrayList<ZtkClassApi.ClassInfo>) {
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

    override fun onItemClick(classDataBean: ZtkClassApi.ClassInfo) {
        val intent = Intent(getAttachActivity(), SearchResultActivity::class.java)
        intent.putExtra("KEYWORD", classDataBean.q)
        startActivity(intent)
    }


}