package com.denobili.app.helper

import android.content.Context
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.denobili.app.R
import com.denobili.app.helper_utils.ViewExtension.invisible
import com.denobili.app.helper_utils.ViewExtension.visible
import kotlinx.android.synthetic.main.setting_view.view.*
import org.jetbrains.anko.onClick

class SettingsView : LinearLayout {


    var title: String = "title"

    override fun setOrientation(orientation: Int) {
        super.setOrientation(LinearLayout.VERTICAL)
    }

    constructor(context: Context) : super(context) {
        initializeViews(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typed_array = context.obtainStyledAttributes(attrs, R.styleable.SettingsView)
        title = typed_array.getString(R.styleable.SettingsView_name)
        typed_array.recycle()
        initializeViews(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initializeViews(context)
    }

    private fun initializeViews(context: Context) {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.setting_view, this)
        orientation = LinearLayout.VERTICAL

        title_settings.setText(title)

        edit_settings.onClick {
            username_settings_input.enable(true)
            password_settings_input.enable(true)
            sender_id_settings_input.enable(true)

            edit_settings.invisible()
            save_settings.visible()
            check_settings.invisible()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    fun setData(username: String, password: String, senderId: String) {
        username_settings_input.setText(username)
        password_settings_input.setText(password)
        sender_id_settings_input.setText(senderId)

    }

    fun TextInputEditText.enable(enable: Boolean) {
        this.isEnabled = enable
        this.isFocusableInTouchMode = enable
    }

    fun save() {
        username_settings_input.enable(false)
        password_settings_input.enable(false)
        sender_id_settings_input.enable(false)

        username_settings_input.clearFocus()
        password_settings_input.clearFocus()
        sender_id_settings_input.clearFocus()

        edit_settings.visible()
        save_settings.invisible()
        check_settings.visible()
    }

    fun TextInputEditText.isInvalid(textInputLayout: TextInputLayout, error: String): Boolean {
        if (this.text.isNullOrBlank()) {
            textInputLayout.error = error
            return true
        } else return false
    }

    fun activate() {
        chkState_settings.isChecked = true
    }

    fun deactivate() {
        chkState_settings.isChecked = false
    }

    fun validate(): Boolean {
        var proceed = true
        if (username_settings_input
                        .isInvalid(username_settings_view, context.getString(R.string.input_username)))
            proceed = false
        if (password_settings_input
                        .isInvalid(password_settings_view, context.getString(R.string.input_password)))
            proceed = false
        if (sender_id_settings_input
                        .isInvalid(sender_id_settings_view, context.getString(R.string.input_sender_id)))
            proceed = false
        return proceed
    }

    fun getUsername(): String = username_settings_input.text.toString()

    fun getPassword(): String = password_settings_input.text.toString()

    fun getSenderId(): String = sender_id_settings_input.text.toString()

    fun showSwitch() {
        chkState_settings.isChecked = false
        check_settings.visible()
    }


}