package info.nightscout.plugins.general.food

import app.aaps.interfaces.logging.AAPSLogger
import app.aaps.interfaces.plugin.PluginBase
import app.aaps.interfaces.plugin.PluginDescription
import app.aaps.interfaces.plugin.PluginType
import app.aaps.interfaces.resources.ResourceHelper
import dagger.android.HasAndroidInjector
import info.nightscout.plugins.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoodPlugin @Inject constructor(
    injector: HasAndroidInjector,
    aapsLogger: AAPSLogger,
    rh: ResourceHelper
) : PluginBase(
    PluginDescription()
        .mainType(PluginType.GENERAL)
        .fragmentClass(FoodFragment::class.java.name)
        .pluginIcon(info.nightscout.core.main.R.drawable.ic_food)
        .pluginName(R.string.food)
        .shortName(R.string.food_short)
        .description(R.string.description_food),
    aapsLogger, rh, injector
)