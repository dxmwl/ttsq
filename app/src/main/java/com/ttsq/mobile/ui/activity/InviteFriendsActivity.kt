package com.ttsq.mobile.ui.activity

import android.view.View
import com.pdlbox.tools.utils.GlideUtils
import com.ttsq.mobile.R
import com.ttsq.mobile.aop.SingleClick
import com.ttsq.mobile.app.AppActivity
import com.ttsq.mobile.app.Constants
import com.ttsq.mobile.ui.dialog.ShareDialog
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import okhttp3.Call

/**
 * @ClassName: InviteFriendsActivity
 * @Description: 邀请好友
 * @Author: 常利兵
 * @Date: 2023/6/24 22:23
 **/
class InviteFriendsActivity : AppActivity() {

    private val banner_img: Banner<String, BannerImageAdapter<String>>? by lazy { findViewById(R.id.banner_img) }

    override fun getLayoutId(): Int {
        return R.layout.activity_invite_friends
    }

    override fun initView() {

        setOnClickListener(R.id.shapeTextView3)

        banner_img?.addBannerLifecycleObserver(this)
        banner_img?.setBannerGalleryEffect(50, 50, 10, 0.8f)
    }

    private val imgList =
        arrayListOf(Constants.TEST_IMG_URL, Constants.TEST_IMG_URL, Constants.TEST_IMG_URL)

    override fun initData() {
        banner_img?.setAdapter(object : BannerImageAdapter<String>(imgList) {
            override fun onBindView(
                holder: BannerImageHolder?,
                data: String?,
                position: Int,
                size: Int
            ) {
                GlideUtils.showRoundCornerImg(data, holder?.imageView, 20f)
            }

        })
    }

    override fun onRightClick(view: View) {
        super.onRightClick(view)
        startActivity(FriendsListActivity::class.java)
    }

    @SingleClick
    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.shapeTextView3 -> {
                banner_img?.let {
                    val umImage = UMImage(this, imgList[it.currentItem])
                    ShareDialog.Builder(this@InviteFriendsActivity)
                        .setShareImage(umImage)
                        .show()
                }
            }
            else -> {}
        }
    }

    override fun onStart(call: Call) {
        super.onStart(call)
        banner_img?.start()
    }

    override fun onStop() {
        super.onStop()
        banner_img?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        banner_img?.destroy()
    }
}