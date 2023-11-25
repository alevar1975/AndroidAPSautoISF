package app.aaps.plugins.main.skins

import app.aaps.core.interfaces.configuration.Config
import app.aaps.plugins.main.R
import app.aaps.plugins.main.databinding.OverviewFragmentBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SkinLargeDisplay @Inject constructor(private val config: Config) : SkinInterface {

    override val description: Int get() = R.string.largedisplay_description
    override val mainGraphHeight: Int get() = 290       // was 400
    override val secondaryGraphHeight: Int get() = 90   // was 150

    override fun preProcessLandscapeOverviewLayout(binding: OverviewFragmentBinding, isLandscape: Boolean, isTablet: Boolean, isSmallHeight: Boolean) {
        super.preProcessLandscapeOverviewLayout(binding, isLandscape, isTablet, isSmallHeight)
        if (!config.NSCLIENT && (isSmallHeight || isLandscape)) moveButtonsLayout(binding.root)
    }
}
