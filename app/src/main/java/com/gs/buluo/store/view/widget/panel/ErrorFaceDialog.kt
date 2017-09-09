package com.gs.buluo.store.view.widget.panel

import android.app.Dialog
import android.content.Context
import android.support.annotation.StyleRes
import android.view.View
import android.widget.TextView
import com.gs.buluo.common.utils.DensityUtils
import com.gs.buluo.store.R
import com.gs.buluo.store.bean.OnActionListener

/**
 * Created by hjn on 2017/9/9.
 */

class ErrorFaceDialog @JvmOverloads constructor(context: Context, @StyleRes themeResId: Int = R.style.myCorDialog) : Dialog(context, themeResId) {
    class Builder constructor(var context: Context) {
        var contentView: View? = null
        var mTheme = R.style.myCorDialog
        var listener: OnActionListener? = null

        init {
            contentView = View.inflate(context, R.layout.dialog_face_error, null)
        }

        fun setLayout(res: Int): Builder {
            contentView = View.inflate(context, res, null)
            return this
        }

        fun setTitle(title: String): Builder {
            (contentView?.findViewById(R.id.error_dialog_title) as TextView).text = title
            return this
        }

        fun setContent(content: String): Builder {
            (contentView?.findViewById(R.id.error_dialog_content) as TextView).text = content
            return this
        }

        fun setAction(listener: OnActionListener): Builder {
            this.listener = listener
            return this
        }

        fun setTheme(themeResId: Int) {
            mTheme = themeResId
        }

        fun create(): ErrorFaceDialog {
            val dialog: ErrorFaceDialog = ErrorFaceDialog(context, mTheme)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(contentView)
            contentView?.findViewById(R.id.btn_error_finish)?.setOnClickListener { listener?.onAct() }
            contentView?.findViewById(R.id.ib_dismiss)?.setOnClickListener { dialog.dismiss() }
//            val params = dialog.window?.attributes
//            params?.width = DensityUtils.dip2px(context, 230f)
//            params?.height = DensityUtils.dip2px(context, 223f)
//            dialog.window?.attributes = params
            dialog.show()
            return dialog
        }
    }
}
