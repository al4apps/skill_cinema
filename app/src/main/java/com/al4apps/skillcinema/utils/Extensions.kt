package com.al4apps.skillcinema.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.res.use
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.math.floor
import kotlin.random.Random

/**
 * Retrieve a color from the current [android.content.res.Resources.Theme].
 */
@ColorInt
@SuppressLint("Recycle")
fun Context.themeColor(
    @AttrRes themeAttrId: Int,
): Int {
    return obtainStyledAttributes(
        intArrayOf(themeAttrId),
    ).use {
        it.getColor(0, Color.MAGENTA)
    }
}

fun Activity.isPermissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        permission,
    ) == PackageManager.PERMISSION_GRANTED
}

fun Activity.isPermissionGranted(permissions: Array<String>): Boolean {
    return permissions.all { permission ->
        ContextCompat.checkSelfPermission(
            this,
            permission,
        ) == PackageManager.PERMISSION_GRANTED
    }
}

fun Activity.hideKeyboardAndClearFocus() {
    val view = currentFocus ?: View(this)
    hideKeyboardFrom(view)
    view.clearFocus()
}

fun Context.hideKeyboardFrom(view: View) {
    getSystemService(Activity.INPUT_METHOD_SERVICE).let { it as InputMethodManager }
        .hideSoftInputFromWindow(view.windowToken, 0)
}

fun Int.fromDpToPx(context: Context): Int {
    val density = context.resources.displayMetrics.densityDpi
    val pixelsInDp = density / DisplayMetrics.DENSITY_DEFAULT
    return this * pixelsInDp
}

fun Int.isLeapYear(): Boolean = this % 400 == 0 || (this % 4 == 0 && this % 100 != 0)

fun androidx.appcompat.widget.SearchView.onQueryTextListenerFlow(): Flow<String> {
    return callbackFlow {
        val listener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                trySendBlocking(newText ?: "")
                return true
            }
        }
        this@onQueryTextListenerFlow.setOnQueryTextListener(listener)
        awaitClose { setOnQueryTextListener(null) }
    }
}

fun EditText.onTextChangedFlow(): Flow<String> {
    return callbackFlow {
        val textWatchListener = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                trySendBlocking(text?.toString().orEmpty())
            }

            override fun afterTextChanged(p0: Editable?) {}
        }
        this@onTextChangedFlow.addTextChangedListener(textWatchListener)
        awaitClose {
            this@onTextChangedFlow.removeTextChangedListener(textWatchListener)
        }
    }
}

fun View.updateLayoutParamsByWindowInsetsTop() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            topMargin = insets.top
        }
        WindowInsetsCompat.CONSUMED
    }
}

fun View.updateLayoutParamsByWindowInsetsBottom() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            bottomMargin = insets.bottom
        }
        WindowInsetsCompat.CONSUMED
    }
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun ViewGroup.inflater(): LayoutInflater = LayoutInflater.from(this.context)

fun <T> MutableList<T>.cut(n: Int): List<T> {
    require(n >= 0) { "Requested element count $n is less than zero." }
    if (n == 0) return emptyList()
    val list = this.take(n)
    repeat(n) { if (isNotEmpty()) removeFirst() }
    return list
}

fun <T> MutableList<T>.cutLast(n: Int): List<T> {
    require(n >= 0) { "Requested element count $n is less than zero." }
    if (n == 0) return emptyList()
    val list = this.takeLast(n)
    repeat(n) { if (isNotEmpty()) removeLast() }
    return list
}

fun getRandomFloat(fromX: Float, untilY: Float, decimals: Int = 2): Float {
    if (fromX >= untilY || decimals < 1) return fromX
    var _decimals = 1
    repeat(decimals) { _decimals *= 10 }
    var float = floor(Random.nextFloat() * _decimals) / _decimals
    val randomInt = Random.nextInt(maxOf(fromX.toInt(), 1), untilY.toInt())
    if (fromX >= 1 || (untilY > 1 && Random.nextBoolean())) {
        float += randomInt.toFloat()
    }
    return float
}

fun Fragment.toast(message: String) {
    Toast.makeText(
        requireContext(),
        message,
        Toast.LENGTH_SHORT,
    ).show()
}

fun Fragment.toast(@StringRes message: Int) {
    Toast.makeText(
        requireContext(),
        getString(message),
        Toast.LENGTH_SHORT,
    ).show()
}

fun Context.toast(@StringRes message: Int) {
    Toast.makeText(this, getString(message), Toast.LENGTH_SHORT).show()
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun EditText.textString(): String = text?.toString() ?: ""

fun EditText.onTextChangedListener(onTextChanged: (text: String) -> Unit): TextWatcher {
    val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(text.toString())
        }

        override fun afterTextChanged(s: Editable?) {}
    }
    addTextChangedListener(watcher)
    return watcher
}
