package com.gogoh5.apps.quanmaomao.android.ui.productdetail

import android.app.Dialog
import android.content.Context
import com.gogoh5.apps.quanmaomao.android.R
import com.gogoh5.apps.quanmaomao.library.environment.SysContext
import com.gogoh5.apps.quanmaomao.library.extensions.Run
import com.gogoh5.apps.quanmaomao.library.extensions.tapWith
import kotlinx.android.synthetic.main.dialog_reward_help.*

class RewardHelperDialog(context: Context?, val run: Run) : Dialog(context, R.style.DialogStyle) {



    override fun show() {
        setContentView(R.layout.dialog_reward_help)
        backBtn.tapWith {
            dismiss()
        }


        val attr = window.attributes

        attr.width = SysContext.getScreenWidth()
        attr.height = SysContext.getScreenHeight()
        attr.horizontalMargin = 0f
        attr.verticalMargin = 0f
        super.show()
    }

    override fun dismiss() {
        super.dismiss()
        run()
    }
}