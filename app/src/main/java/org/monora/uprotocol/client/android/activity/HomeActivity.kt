package org.monora.uprotocol.client.android.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.monora.uprotocol.client.android.NavHomeDirections
import org.monora.uprotocol.client.android.R
import org.monora.uprotocol.client.android.app.Activity
import org.monora.uprotocol.client.android.database.model.SharedText
import org.monora.uprotocol.client.android.database.model.WebTransfer
import org.monora.uprotocol.client.android.databinding.LayoutUserProfileBinding
import org.monora.uprotocol.client.android.viewmodel.UserProfileViewModel

@AndroidEntryPoint
class HomeActivity : Activity(), NavigationView.OnNavigationItemSelectedListener {

    private val userProfileViewModel: UserProfileViewModel by viewModels()

    private val userProfileBinding by lazy {
        LayoutUserProfileBinding.bind(navigationView.getHeaderView(0)).also {
            it.viewModel = userProfileViewModel
            it.lifecycleOwner = this
            it.editProfileClickListener = View.OnClickListener {
                openItem(R.id.edit_profile)
            }
        }
    }

    private val navigationView: NavigationView by lazy {
        findViewById(R.id.nav_view)
    }

    private val drawerLayout: DrawerLayout by lazy {
        findViewById(R.id.drawer_layout)
    }

    private var pendingMenuItemId = 0

    private val navController by lazy {
        navController(R.id.nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        drawerLayout.addDrawerListener(
            object : DrawerLayout.SimpleDrawerListener() {
                override fun onDrawerClosed(drawerView: View) {
                    applyAwaitingDrawerAction()
                }
            }
        )

        toolbar.setupWithNavController(navController, AppBarConfiguration(navController.graph, drawerLayout))
        navigationView.setNavigationItemSelectedListener(this)
        navController.addOnDestinationChangedListener { _, destination, _ -> title = destination.label }
        userProfileBinding.executePendingBindings()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        when (intent?.action) {
            ACTION_OPEN_SHARED_TEXT -> if (intent.hasExtra(EXTRA_SHARED_TEXT)) {
                val sharedText = intent.getParcelableExtra<SharedText>(EXTRA_SHARED_TEXT)
                if (sharedText != null) {
                    navController.navigate(NavHomeDirections.actionGlobalNavTextEditor(sharedText = sharedText))
                }
            }
            ACTION_OPEN_WEB_TRANSFER -> if (intent.hasExtra(EXTRA_WEB_TRANSFER)) {
                val webTransfer = intent.getParcelableExtra<WebTransfer>(EXTRA_WEB_TRANSFER)
                if (webTransfer != null) {
                    Log.d("HomeActivity", "onNewIntent: $webTransfer")
                    navController.navigate(NavHomeDirections.actionGlobalWebTransferDetailsFragment(webTransfer))
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        openItem(item.itemId)
        return true
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.close()
        } else {
            super.onBackPressed()
        }
    }

    private fun applyAwaitingDrawerAction() {
        if (pendingMenuItemId == 0) {
            return // drawer was opened, but nothing was clicked.
        }

        when (pendingMenuItemId) {
            R.id.edit_profile -> navController.navigate(NavHomeDirections.actionGlobalProfileEditorFragment())
        }

        pendingMenuItemId = 0
    }

    private fun openItem(@IdRes id: Int) {
        pendingMenuItemId = id
        drawerLayout.close()
    }

    companion object {
        const val ACTION_OPEN_TRANSFER_DETAILS = "org.monora.uprotocol.client.android.action.OPEN_TRANSFER_DETAILS"

        const val ACTION_OPEN_WEB_TRANSFER = "org.monora.uprotocol.client.android.action.OPEN_WEB_TRANSFER"

        const val ACTION_OPEN_SHARED_TEXT = "org.monora.uprotocol.client.android.action.OPEN_SHARED_TEXT"

        const val EXTRA_TRANSFER = "extraTransfer"

        const val EXTRA_WEB_TRANSFER = "extraWebTransfer"

        const val EXTRA_SHARED_TEXT = "extraSharedText"
    }
}
