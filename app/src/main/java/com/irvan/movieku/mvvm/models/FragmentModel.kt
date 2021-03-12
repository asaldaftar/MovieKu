package com.irvan.movieku.mvvm.models

import androidx.fragment.app.Fragment

class FragmentModel {
    var fragment: Fragment? = null
    var tag: String? = null

    constructor(fragment: Fragment?, tag: String?) {
        this.fragment = fragment
        this.tag = tag
    }

}