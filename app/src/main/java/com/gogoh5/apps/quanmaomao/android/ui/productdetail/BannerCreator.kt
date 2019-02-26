package com.gogoh5.apps.quanmaomao.android.ui.productdetail

import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith

class BannerCreator(val callback: Callback): CBViewHolderCreator {
    override fun createHolder(itemView: View?): Holder<*> =
        object: Holder<String>(itemView) {
            override fun initView(itemView: View?) {
                itemView?.tapWith {
                    if(it == null)
                        return@tapWith
                    val url = it.getTag(R.id.data) as String? ?:return@tapWith
                    callback.onClick(it, url)
                }
            }


            override fun updateUI(data: String) {
                itemView?.setTag(R.id.data, data)
                SysContext.getGlide().loadNetPicDirectly(data, itemView as ImageView)
            }
        }

    override fun getLayoutId(): Int = R.layout.view_image

    interface Callback {
        fun onClick(view: View, url: String)
    }
}