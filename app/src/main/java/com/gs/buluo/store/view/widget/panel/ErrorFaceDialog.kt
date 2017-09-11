package com.gs.buluo.store.view.widget.panel

import android.app.Dialog
import android.content.Context
import android.support.annotation.StyleRes
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.gs.buluo.store.R
import com.gs.buluo.store.bean.OnActionListener

/**
 * Created by hjn on 2017/9/9.
 */

class ErrorFaceDialog @JvmOverloads constructor(context: Context, @StyleRes themeResId: Int = R.style.myCorDialog) : Dialog(context, themeResId) {
    class Builder constructor(var context: Context) {
        var contentView: View? = null
        var mTheme = R.style.myCorDialog

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

        fun setTheme(themeResId: Int) {
            mTheme = themeResId
        }

        private var posListener: OnActionListener? = null
        fun setPositiveButton(text: String, onActionListener: OnActionListener): Builder {
            val btAction = contentView?.findViewById(R.id.btn_error_act) as Button
            btAction.visibility = View.VISIBLE
            btAction.text = text
            posListener = onActionListener
            return this
        }

        private var negListener: OnActionListener? = null
        fun setNegativeButton(text: String, onActionListener: OnActionListener): Builder {
            val btAction = contentView?.findViewById(R.id.btn_error_finish) as Button
            btAction.text = text
            negListener = onActionListener
            return this
        }

        fun create(): ErrorFaceDialog {
            val dialog = ErrorFaceDialog(context, mTheme)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(contentView)
            contentView?.findViewById(R.id.ib_dismiss)?.setOnClickListener { dialog.dismiss() }
            contentView?.findViewById(R.id.btn_error_finish)?.setOnClickListener {
                dialog.dismiss()
                negListener?.onAct()
            }
            contentView?.findViewById(R.id.btn_error_act)?.setOnClickListener {
                dialog.dismiss()
                posListener?.onAct()
            }
//            val params = dialog.window?.attributes
//            params?.width = DensityUtils.dip2px(context, 230f)
//            params?.height = DensityUtils.dip2px(context, 223f)
//            dialog.window?.attributes = params
            dialog.show()
            return dialog
        }
    }
}
