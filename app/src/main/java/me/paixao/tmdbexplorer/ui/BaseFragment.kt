package me.paixao.tmdbexplorer.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Created by pedropaixao on 12/03/2018.
 */

open class BaseFragment <A : BaseActivity> : Fragment() {

    fun obtainViewModel() =
            (activity as A).obtainViewModel()

}