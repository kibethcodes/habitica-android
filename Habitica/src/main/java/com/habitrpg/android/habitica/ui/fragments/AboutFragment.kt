package com.habitrpg.android.habitica.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import androidx.compose.material.Text
import androidx.compose.ui.text.toUpperCase
import androidx.core.net.toUri
import androidx.preference.PreferenceManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.habitrpg.android.habitica.R
import com.habitrpg.android.habitica.components.UserComponent
import com.habitrpg.android.habitica.compose.ui.screen.AboutElement
import com.habitrpg.android.habitica.compose.ui.screen.AboutElementType
import com.habitrpg.android.habitica.compose.ui.screen.AboutScreen
import com.habitrpg.android.habitica.compose.ui.screen.AboutState
import com.habitrpg.android.habitica.compose.ui.theme.HabiticaPalette
import com.habitrpg.android.habitica.compose.ui.theme.HabiticaTheme
import com.habitrpg.android.habitica.databinding.FragmentAboutBinding
import com.habitrpg.android.habitica.helpers.AppConfigManager
import com.habitrpg.android.habitica.helpers.MainNavigationController
import com.habitrpg.android.habitica.modules.AppModule
import com.habitrpg.android.habitica.ui.helpers.DataBindingUtils
import com.plattysoft.leonids.ParticleSystem
import javax.inject.Inject
import javax.inject.Named

class AboutFragment : BaseMainFragment<FragmentAboutBinding>() {

    @field:[Inject Named(AppModule.NAMED_USER_ID)]
    lateinit var userId: String

    @Inject
    lateinit var appConfigManager: AppConfigManager

    override fun injectFragment(component: UserComponent) {
        component.inject(this)
    }

    private val privacyPolicyLink = "https://habitica.com/static/privacy"
    private val termsLink = "https://habitica.com/static/terms"
    private val androidSourceCodeLink = "https://github.com/HabitRPG/habitrpg-android/"
    private val twitterLink = "https://twitter.com/habitica"
    private val wikiaLink = "http://habitica.wikia.com/"
    private val habiticaHomeLink = "https://www.habitica.com"

    private var versionNumberTappedCount = 0

    private fun openGooglePlay() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = "market://details?id=com.habitrpg.android.habitica".toUri()
        startActivity(intent)
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAboutBinding {
        return FragmentAboutBinding.inflate(layoutInflater, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.hidesToolbar = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun versionClick() {
        versionNumberTappedCount += 1
        when (versionNumberTappedCount) {
            1 -> context?.let { context ->
                Toast.makeText(context, "Oh! You tapped me!", Toast.LENGTH_SHORT).show()
            }
            in 5..7 -> context?.let { context ->
                Toast.makeText(
                    context,
                    "Only ${8 - versionNumberTappedCount} taps left!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            8 -> {
                context?.let { context ->
                    Toast.makeText(context, "You were blessed with cats!", Toast.LENGTH_SHORT)
                        .show()
                }
                doTheThing()
            }
        }
    }

    private val versionName: String by lazy {
        try {
            @Suppress("DEPRECATION")
            activity?.packageManager?.getPackageInfo(activity?.packageName ?: "", 0)?.versionName
                ?: ""
        } catch (e: PackageManager.NameNotFoundException) {
            ""
        }
    }

    private val versionCode: Int by lazy {
        try {
            @Suppress("DEPRECATION")
            activity?.packageManager?.getPackageInfo(activity?.packageName ?: "", 0)?.versionCode
                ?: 0
        } catch (e: PackageManager.NameNotFoundException) {
            0
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if (appConfigManager.lastVersionCode() > versionCode) {
//            binding?.updateAvailableWrapper?.visibility = View.VISIBLE
//            binding?.updateAvailableTextview?.text = getString(
//                R.string.update_available,
//                appConfigManager.lastVersionNumber(),
//                appConfigManager.lastVersionCode()
//            )
//        } else {
//            binding?.updateAvailableWrapper?.visibility = View.GONE
//        }

        val aboutState = AboutState(
            elements = listOf(
                AboutElement(
                    label = getString(R.string.version_info, versionName, versionCode),
                    elementType = AboutElementType.TEXT,
                    onClick = { versionClick() }),
                AboutElement(
                    label = "@habitica",
                    elementType = AboutElementType.LINK,
                    onClick = { openBrowserLink(twitterLink) }),
                AboutElement(
                    label = "www.habitica.com",
                    elementType = AboutElementType.LINK,
                    onClick = { openBrowserLink(habiticaHomeLink) }),
                AboutElement(
                    label = "http://habitica.wikia.com/",
                    elementType = AboutElementType.LINK,
                    onClick = { openBrowserLink(wikiaLink) }),
                AboutElement(
                    label = getString(R.string.privacy_policy),
                    elementType = AboutElementType.BUTTON,
                    onClick = { openBrowserLink(privacyPolicyLink) }),
                AboutElement(
                    label = getString(R.string.terms_of_service),
                    elementType = AboutElementType.BUTTON,
                    onClick = { openBrowserLink(termsLink) }),
                AboutElement(
                    label = getString(R.string.about_rate_our_app),
                    elementType = AboutElementType.BUTTON,
                    onClick = { openGooglePlay() }),
                AboutElement(
                    label = getString(R.string.about_habitica_open_source),
                    elementType = AboutElementType.TEXT,
                    onClick = { openBrowserLink(androidSourceCodeLink) }),
                AboutElement(
                    label = getString(R.string.report_bug),
                    elementType = AboutElementType.BUTTON,
                    onClick = { MainNavigationController.navigate(R.id.bugFixFragment) }),
                AboutElement(
                    label = getString(R.string.about_source_code),
                    elementType = AboutElementType.BUTTON,
                    onClick = { openBrowserLink(androidSourceCodeLink) })
            )
        )
        binding?.aboutCompose?.setContent {
            HabiticaTheme(colorPalette = HabiticaPalette.valueOf(getThemeString(requireContext()))) {
                AboutScreen(aboutState = aboutState)
            }
        }
    }

    private fun openBrowserLink(url: String) {
        val uriUrl = url.toUri()
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(launchBrowser)
    }

    private fun doTheThing() {
        val context = context ?: return
        FirebaseAnalytics.getInstance(context).logEvent("found_easter_egg", null)
        DataBindingUtils.loadImage(context, "Pet-Sabretooth-Base") { bitmap ->
            activity?.runOnUiThread {
                activity?.let {
                    ParticleSystem(it, 50, bitmap, 3000)
                        .setAcceleration(0.00013f, 90)
                        .setSpeedByComponentsRange(-0.08f, 0.08f, 0.05f, 0.1f)
                        .setFadeOut(200, AccelerateInterpolator())
                        .setRotationSpeed(100f)
                        .emitWithGravity(binding?.anchor, Gravity.BOTTOM, 20, 10000)
                }
            }
        }
        DataBindingUtils.loadImage(context, "Pet-Sabretooth-Golden") { bitmap ->
            activity?.runOnUiThread {
                activity?.let {
                    ParticleSystem(it, 50, bitmap, 3000)
                        .setAcceleration(0.00013f, 90)
                        .setSpeedByComponentsRange(-0.08f, 0.08f, 0.05f, 0.1f)
                        .setFadeOut(200, AccelerateInterpolator())
                        .setRotationSpeed(100f)
                        .emitWithGravity(binding?.anchor, Gravity.BOTTOM, 20, 10000)
                }
            }
        }
        DataBindingUtils.loadImage(context, "Pet-Sabretooth-Red") { bitmap ->
            activity?.runOnUiThread {
                activity?.let {
                    ParticleSystem(it, 50, bitmap, 3000)
                        .setAcceleration(0.00013f, 90)
                        .setSpeedByComponentsRange(-0.08f, 0.08f, 0.05f, 0.1f)
                        .setFadeOut(200, AccelerateInterpolator())
                        .setRotationSpeed(100f)
                        .emitWithGravity(binding?.anchor, Gravity.BOTTOM, 20, 10000)
                }
            }
        }
    }

    override var binding: FragmentAboutBinding? = null

    fun getThemeString(context: Context): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString("theme_name", "purple")?.uppercase() ?: "PURPLE"
    }
}
