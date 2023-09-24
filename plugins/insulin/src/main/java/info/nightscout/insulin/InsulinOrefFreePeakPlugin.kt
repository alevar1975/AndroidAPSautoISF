package info.nightscout.insulin

import app.aaps.interfaces.configuration.Config
import app.aaps.interfaces.insulin.Insulin
import app.aaps.interfaces.logging.AAPSLogger
import app.aaps.interfaces.profile.ProfileFunction
import app.aaps.interfaces.resources.ResourceHelper
import app.aaps.interfaces.rx.bus.RxBus
import app.aaps.interfaces.sharedPreferences.SP
import app.aaps.interfaces.ui.UiInteraction
import app.aaps.interfaces.utils.HardLimits
import dagger.android.HasAndroidInjector
import info.nightscout.core.utils.extensions.putInt
import info.nightscout.core.utils.extensions.storeInt
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by adrian on 14/08/17.
 */
@Singleton
class InsulinOrefFreePeakPlugin @Inject constructor(
    injector: HasAndroidInjector,
    private val sp: SP,
    rh: ResourceHelper,
    profileFunction: ProfileFunction,
    rxBus: RxBus,
    aapsLogger: AAPSLogger,
    config: Config,
    hardLimits: HardLimits,
    uiInteraction: UiInteraction
) : InsulinOrefBasePlugin(injector, rh, profileFunction, rxBus, aapsLogger, config, hardLimits, uiInteraction) {

    override val id get(): Insulin.InsulinType = Insulin.InsulinType.OREF_FREE_PEAK

    override val friendlyName get(): String = rh.gs(R.string.free_peak_oref)

    override fun configuration(): JSONObject = JSONObject().putInt(info.nightscout.core.utils.R.string.key_insulin_oref_peak, sp, rh)
    override fun applyConfiguration(configuration: JSONObject) {
        configuration.storeInt(info.nightscout.core.utils.R.string.key_insulin_oref_peak, sp, rh)
    }

    override fun commentStandardText(): String {
        return rh.gs(R.string.insulin_peak_time) + ": " + peak
    }

    override val peak: Int
        get() = sp.getInt(info.nightscout.core.utils.R.string.key_insulin_oref_peak, DEFAULT_PEAK)

    companion object {

        private const val DEFAULT_PEAK = 75
    }

    init {
        pluginDescription
            .pluginIcon(R.drawable.ic_insulin)
            .pluginName(R.string.free_peak_oref)
            .preferencesId(R.xml.pref_insulinoreffreepeak)
            .description(R.string.description_insulin_free_peak)
    }
}