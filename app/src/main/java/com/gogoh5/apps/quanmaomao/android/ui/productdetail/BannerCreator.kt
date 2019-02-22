package com.gogoh5.apps.quanmaomao.android.ui.productdetail

import android.view.View
import android.widget.ImageView
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator
import com.bigkoo.convenientbanner.holder.Holder
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.library.environment.SysContext

class BannerCreator: CBViewHolderCreator {
    override fun createHolder(itemView: View?): Holder<*> =
        object: Holder<String>(itemView) {
            override fun initView(itemView: View?) {
            }


            override fun updateUI(data: String?) {
                SysContext.getGlide().loadNetPicDirectly(data, itemView as ImageView)
            }
        }

    override fun getLayoutId(): Int = R.layout.view_image
}