package info.nightscout.implementation.queue.commands

import app.aaps.interfaces.logging.LTag
import app.aaps.interfaces.plugin.ActivePlugin
import app.aaps.interfaces.pump.PumpEnactResult
import app.aaps.interfaces.queue.Callback
import app.aaps.interfaces.queue.Command
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class CommandExtendedBolus(
    injector: HasAndroidInjector,
    private val insulin: Double,
    private val durationInMinutes: Int,
    callback: Callback?
) : Command(injector, CommandType.EXTENDEDBOLUS, callback) {

    @Inject lateinit var activePlugin: ActivePlugin

    override fun execute() {
        val r = activePlugin.activePump.setExtendedBolus(insulin, durationInMinutes)
        aapsLogger.debug(LTag.PUMPQUEUE, "Result rate: $insulin durationInMinutes: $durationInMinutes success: ${r.success} enacted: ${r.enacted}")
        callback?.result(r)?.run()
    }

    override fun status(): String = rh.gs(info.nightscout.core.ui.R.string.extended_bolus_u_min, insulin, durationInMinutes)

    override fun log(): String = "EXTENDEDBOLUS $insulin U $durationInMinutes min"
    override fun cancel() {
        aapsLogger.debug(LTag.PUMPQUEUE, "Result cancel")
        callback?.result(PumpEnactResult(injector).success(false).comment(info.nightscout.core.ui.R.string.connectiontimedout))?.run()
    }
}