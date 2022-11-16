package com.easybuy.mobile.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easybug.mobile.R
import com.easybuy.mobile.app.AppAdapter
import com.easybuy.mobile.http.api.ClassApi
import com.easybuy.mobile.http.glide.GlideApp
import com.easybuy.mobile.widget.SpacesItemDecoration

/**
 * @project : YunKe
 * @Description : 项目描述
 * @author : clb
 * @time : 2022/5/31
 */
class ServiceCategoryListAdapter(val listener: OnItemClickListener) :
    RecyclerView.Adapter<ServiceCategoryListAdapter.ServiceCategoryViewHolder>() {

    private var listData = ArrayList<ClassApi.Data>()

    inner class ServiceCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView14: TextView? by lazy { itemView.findViewById(R.id.textView14) }
        val serviceList: RecyclerView? by lazy { itemView.findViewById(R.id.service_list) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceCategoryViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_service_category, parent, false)
        return ServiceCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceCategoryViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val serviceClassDto = listData[position]
        holder.textView14?.text = serviceClassDto.next_name
        holder.serviceList?.let {
            it.layoutManager = GridLayoutManager(holder.itemView.context, 2)
            val serviceCategoryAdapter = ServiceCategoryAdapter(holder.itemView.context, listener)
            it.adapter = serviceCategoryAdapter
            serviceCategoryAdapter.setData(serviceClassDto.info.toMutableList())
            it.addItemDecoration(SpacesItemDecoration(15))
        }
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    fun setData(listData: ArrayList<ClassApi.Data>) {
        this.listData.clear()
        this.listData.addAll(listData)
        notifyDataSetChanged()
//        notifyItemRangeInserted(0, this.listData.size)
    }

    interface OnItemClickListener {
        fun onItemClick(classDataBean: ClassApi.Info)
    }

    inner class ServiceCategoryAdapter(
        val mContext: Context,
        val listener: ServiceCategoryListAdapter.OnItemClickListener?
    ) :
        AppAdapter<ClassApi.Info>(mContext) {


        inner class ViewHolder : AppViewHolder(R.layout.item_service_item) {
            private val serviceTitle: TextView? by lazy { findViewById(R.id.textView15) }
            private val tvNumber: TextView? by lazy { findViewById(R.id.tv_number) }
            private val serviceIcon: ImageView? by lazy { findViewById(R.id.imageView2) }

            override fun onBindView(position: Int) {
                val item = getItem(position)
                serviceTitle?.text = item.son_name

                //TODO 云客数量未返回
                tvNumber?.text = "0"
                serviceIcon?.let { GlideApp.with(mContext).load(item.imgurl).into(it) }

                getItemView().setOnClickListener {
                    listener?.onItemClick(item)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
            return ViewHolder()
        }
    }
}