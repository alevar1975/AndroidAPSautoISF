package info.nightscout.automation.actions

import androidx.annotation.DrawableRes
import app.aaps.interfaces.aps.Loop
import app.aaps.interfaces.configuration.ConfigBuilder
import app.aaps.interfaces.logging.UserEntryLogger
import app.aaps.interfaces.plugin.PluginBase
import app.aaps.interfaces.plugin.PluginType
import app.aaps.interfaces.pump.PumpEnactResult
import app.aaps.interfaces.queue.Callback
import app.aaps.interfaces.queue.CommandQueue
import app.aaps.interfaces.rx.bus.RxBus
import app.aaps.interfaces.rx.events.EventRefreshOverview
import dagger.android.HasAndroidInjector
import info.nightscout.automation.R
import info.nightscout.database.entities.UserEntry
import info.nightscout.database.entities.UserEntry.Sources
import javax.inject.Inject

class ActionLoopDisable(injector: HasAndroidInjector) : Action(injector) {

    @Inject lateinit var loopPlugin: Loop
    @Inject lateinit var configBuilder: ConfigBuilder
    @Inject lateinit var commandQueue: CommandQueue
    @Inject lateinit var rxBus: RxBus
    @Inject lateinit var uel: UserEntryLogger

    override fun friendlyName(): Int = info.nightscout.core.ui.R.string.disableloop
    override fun shortDescription(): String = rh.gs(info.nightscout.core.ui.R.string.disableloop)
    @DrawableRes override fun icon(): Int = R.drawable.ic_stop_24dp

    override fun doAction(callback: Callback) {
        if (loopPlugin.isEnabled()) {
            (loopPlugin as PluginBase).setPluginEnabled(PluginType.LOOP, false)
            configBuilder.storeSettings("ActionLoopDisable")
            uel.log(UserEntry.Action.LOOP_DISABLED, Sources.Automation, title)
            commandQueue.cancelTempBasal(true, object : Callback() {
                override fun run() {
                    rxBus.send(EventRefreshOverview("ActionLoopDisable"))
                    callback.result(result).run()
                }
            })
        } else {
            callback.result(PumpEnactResult(injector).success(true).comment(R.string.alreadydisabled)).run()
        }
    }

    override fun isValid(): Boolean = true
}
