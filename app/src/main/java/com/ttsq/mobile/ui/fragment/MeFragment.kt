package com.ttsq.mobile.ui.fragment

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.blankj.utilcode.util.ClipboardUtils
import com.hjq.base.BaseDialog
import com.hjq.shape.view.ShapeTextView
import com.hjq.umeng.Platform
import com.hjq.umeng.UmengShare
import com.pdlbox.tools.utils.GlideUtils
import com.ttsq.mobile.R
import com.ttsq.mobile.aop.SingleClick
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.app.AppFragment
import com.ttsq.mobile.http.api.UserInfoApi
import com.ttsq.mobile.manager.UserManager
import com.ttsq.mobile.other.AppConfig
import com.ttsq.mobile.ui.activity.InviteFriendsActivity
import com.ttsq.mobile.ui.activity.LoginActivity
import com.ttsq.mobile.ui.activity.MemberCenterActivity
import com.ttsq.mobile.ui.activity.SettingActivity
import com.ttsq.mobile.ui.dialog.MessageDialog
import com.ttsq.mobile.ui.dialog.ShareDialog
import com.ttsq.mobile.utils.livebus.LiveDataBus
import com.umeng.socialize.media.UMWeb

/**
 * @ClassName: LocalToolsFragment
 * @Description: 个人中心
 * @Author: 常利兵
 * @Date: 2023/5/11 22:43
 **/
class MeFragment : AppFragment<AppActivity>() {

    companion object {

        fun newInstance(): MeFragment {
            return MeFragment()
        }
    }

    private val userAvatar: ImageView? by lazy { findViewById(R.id.user_avatar) }
    private val nickName: TextView? by lazy { findViewById(R.id.nick_name) }
    private val member_time: ShapeTextView? by lazy { findViewById(R.id.member_time) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_me
    }

    override fun initView() {
        setOnClickListener(
            R.id.my_member, R.id.kefu_online, R.id.setting, R.id.nick_name,
            R.id.member_time,R.id.invite_friends,R.id.my_order
        )
    }

    override fun initData() {
        UserManager.userInfo?.let {
            setUserInfo(it)
        }

        LiveDataBus.subscribe("upDateUserInfo", this) { data ->
            val userInfoDto = data as UserInfoApi.UserInfoDto
            setUserInfo(userInfoDto)
        }
    }

    private fun setUserInfo(userInfoDto: UserInfoApi.UserInfoDto) {
//        userAvatar?.let { it1 -> GlideUtils.showRoundImg(userInfoDto.avatar, it1) }
        nickName?.text = userInfoDto.nickName
        if (userInfoDto.memberStatus) {
            member_time?.text = userInfoDto.memberTime
        } else {
            member_time?.text = "开通会员"
        }
    }

    @SingleClick
    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.my_order->{

            }
            R.id.invite_friends->{
                val umWeb = UMWeb(AppConfig.getDownloadUrl())
                umWeb.title = "限时福利"
                umWeb.description = "点击领取福利"
                ShareDialog.Builder(requireActivity())
                    .setShareLink(umWeb)
                   .show()
//                startActivity(InviteFriendsActivity::class.java)
            }
            R.id.nick_name -> {
                if (UserManager.userInfo == null) {
                    startActivity(LoginActivity::class.java)
                }
            }
            R.id.my_member, R.id.member_time -> {
                if (UserManager.userInfo == null) {
                    startActivity(LoginActivity::class.java)
                    return
                }
                startActivity(MemberCenterActivity::class.java)
            }
            R.id.kefu_online -> {
                MessageDialog.Builder(requireContext())
                    .setTitle("在线客服")
                    .setMessage("与在线客服一对一沟通需求，方便、快捷地说出你的需求\n微信：dxmcpjl")
                    .setConfirm("复制微信")
                    .setListener(object : MessageDialog.OnListener {
                        override fun onConfirm(dialog: BaseDialog?) {
                            ClipboardUtils.copyText("dxmcpjl")
                            toast("复制成功")
                        }
                    })
                    .show()
            }
            R.id.setting -> {
                startActivity(SettingActivity::class.java)
            }
            else -> {}
        }
    }
}