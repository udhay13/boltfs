package org.monora.uprotocol.client.android.activity

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.app.Activity
import org.monora.uprotocol.client.android.util.Resources.attrToRes
import org.monora.uprotocol.client.android.util.Resources.resToDrawable

/**
 * created by: veli
 * date: 13/04/18 19:45
 */
@AndroidEntryPoint
class ContentBrowserActivity : Activity() {
    private val navController by lazy {
        navController(R.id.nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_browser)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val appBarLayout = findViewById<AppBarLayout>(R.id.app_bar)
        val toolbarDefaultBg = R.attr.backgroundTopBar.attrToRes(this).resToDrawable(this)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            title = destination.label

            val bg = when (destination.id) {
                R.id.contentBrowserFragment, R.id.prepareIndexFragment, R.id.webShareLauncherFragment,
                R.id.selectionsFragment -> null
                else -> toolbarDefaultBg
            }
            if (Build.VERSION.SDK_INT < 16) {
                @Suppress("DEPRECATION")
                appBarLayout.setBackgroundDrawable(bg)
            } else {
                appBarLayout.background = bg
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            return navController.navigateUp()
        }

        return super.onOptionsItemSelected(item)
    }
}
