package com.ttsq.mobile.ui.fragment

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ttsq.mobile.R
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.app.AppFragment
import com.ttsq.mobile.app.TitleBarFragment
import com.ttsq.mobile.other.GridSpaceDecoration
import com.ttsq.mobile.ui.adapter.FuliAdapter

/**
 * @ClassName: FuliFragment
 * @Description: 福利单页
 * @Author: 常利兵
 * @Date: 2023/4/3 11:39
 **/
class FuliFragment : TitleBarFragment<AppActivity>() {

    private lateinit var fuliAdapter: FuliAdapter
    private val fuli_list: RecyclerView? by lazy { findViewById(R.id.fuli_list) }

    companion object {

        fun newInstance(): FuliFragment {
            return FuliFragment()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_fuli
    }

    override fun initView() {
        fuli_list?.also {
            it.layoutManager = GridLayoutManager(requireContext(), 2)
            fuliAdapter = FuliAdapter(requireContext())
            it.adapter = fuliAdapter
            it.addItemDecoration(GridSpaceDecoration(resources.getDimension(R.dimen.dp_5).toInt()))
        }
    }

    override fun initData() {
        val fuliListData = ArrayList<FuliItemDto>()
        fuliListData.let {
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/Frhv8hvLKDaE-EB0Rr1KuUGY5AiV","低价包邮","https://4kma.cn/NXDo6"))
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/Fr3XHytO-ADvM1IXADHnmdSAmgmo","福利清单","https://kzurl10.cn/Nnsu4"))
//            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/FlO964brHnAPiJyWFuBUfTAcX3sd","小样种草机","https://kzurl16.cn/Nnsns"))
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/FkjyGtbMcKfnUlJnhn-nkI2sqH3e","天猫国际福利社","https://kzurl18.cn/NnsgR"))
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/FqRCFl6ipL9NDTjsPCNDhQhY0YhF","抖音一元购","https://kzurl09.cn/NXxRW"))
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/Fj4Z3EbV2pL4qzeXL9ATwvPO1BpQ","个性化福利清单","https://kzurl11.cn/NXxTQ"))
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/FmG5NZipcow0YnQjVMbaBlJUFqpA","抖音一分购","https://kzurl05.cn/NXx4n"))
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/FmxeuEOm8EdSnrMUi3OMN-ZF5wD1","支付宝红包","https://kurl03.cn/NXxbw"))
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/FpnZd0OfKaU0wOw6S1Rdqvb6-J2x","抖音商城","https://kurl02.cn/NXe8k"))
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/Fnsal2sYF0tFssUFbyN_woYzu08I","直播好物集结","https://kzurl20.cn/NXesQ"))
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/Foj7tvI4vmW-7xjXDKPBKcxq6o4R","首单礼金","https://kzurl14.cn/NXeJa"))
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/Fg-xSbauwKI5ChhIYdMN19dm3i21","生活必需品","https://4kma.cn/NXeAL"))
//            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/FmTZ8PgetRAdKEYrpMObUQSMaL6r","淘宝吃货专场","https://kzurl20.cn/NXeZn"))
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/FiP5oxKi83KJK6qhMf-GqTU7u2VY","红包签到特价专场","https://kurl04.cn/NXeqZ"))
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/FlrzcAj4LPGKTON6nAgZALQWyDaX","话费充值","https://kurl06.cn/NXeMU"))
//            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/Fn_J8U0mA9yNdXpzsHVXDO_IoJqa","百亿补贴专场","https://kzurl03.cn/NXepD"))
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/Fgn9gnhYfdKwT5CDMtd8Yk8ocV_Y","好单线报","https://kzurl09.cn/NXerA"))
//            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/FuxtmleAiQyJe8kUfI_aTEwq_5OH","品牌好货","https://kzurl20.cn/NXDlC"))
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/Fn5cpknu8_nkuU_3dJezRkf-DzxY","京东热销合集","https://kzurl05.cn/NXD3S"))
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/FpUgwh201MhddhD8tWLQ01GeA5r0","拼多多热销合集","https://kzurl03.cn/NXDwc"))
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/FvMnB_m0iJIPIlpDnf0yypiMngm7","猫超好货","https://kzurl16.cn/NXDsY"))
//            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/FtsoHVb9jnb6jsM2ITCLGH0jQE6t","热销专场","https://kzurl20.cn/NXDeD"))
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/FkHlu8JhltYU9AnPibqBsvGMFk_A","聚划算热销精选","https://2kma.cn/NXDLP"))
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/FooFzxKStuH00rE8GSdipO27EZp7","天猫超市","https://kurl06.cn/NXDy9"))
            it.add(FuliItemDto("http://img-haodanku-com.cdn.fudaiapp.com/FoXHnQBBldvGBV0ZfMQi4fzhUT2g","偏远地区包邮","https://4kma.cn/NXDOT"))
        }
        fuliAdapter.setData(fuliListData)
    }

    override fun isStatusBarEnabled(): Boolean {
        // 使用沉浸式状态栏
        return !super.isStatusBarEnabled()
    }

    /**
     * 福利实体类
     */
    inner class FuliItemDto(val bannerImg: String, val title: String, val url: String)
}