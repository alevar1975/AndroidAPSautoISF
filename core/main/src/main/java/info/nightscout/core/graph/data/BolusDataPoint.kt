package info.nightscout.core.graph.data

import android.content.Context
import android.graphics.Paint
import app.aaps.interfaces.plugin.ActivePlugin
import app.aaps.interfaces.profile.DefaultValueHelper
import app.aaps.interfaces.resources.ResourceHelper
import app.aaps.interfaces.utils.DecimalFormatter
import info.nightscout.database.entities.Bolus

class BolusDataPoint(
    val data: Bolus,
    private val rh: ResourceHelper,
    private val activePlugin: ActivePlugin,
    private val defaultValueHelper: DefaultValueHelper,
    private val decimalFormatter: DecimalFormatter
) : DataPointWithLabelInterface {

    private var yValue = 0.0

    override fun getX(): Double = data.timestamp.toDouble()
    override fun getY(): Double = if (data.type == Bolus.Type.SMB) defaultValueHelper.determineLowLine() else yValue
    override val label
        get() = decimalFormatter.toPumpSupportedBolus(data.amount, activePlugin.activePump.pumpDescription.bolusStep)
    override val duration = 0L
    override val size = 2f
    override val paintStyle: Paint.Style = Paint.Style.FILL // not used
    override val shape
        get() = if (data.type == Bolus.Type.SMB) PointsWithLabelGraphSeries.Shape.SMB else PointsWithLabelGraphSeries.Shape.BOLUS

    override fun color(context: Context?): Int =
        if (data.type == Bolus.Type.SMB) rh.gac(context, info.nightscout.core.ui.R.attr.smbColor)
        else if (data.isValid) rh.gac(context, info.nightscout.core.ui.R.attr.bolusDataPointColor)
        else rh.gac(context, info.nightscout.core.ui.R.attr.alarmColor)

    override fun setY(y: Double) {
        yValue = y
    }
}
