package info.nightscout.core.iob

import app.aaps.interfaces.iob.CobInfo
import app.aaps.interfaces.resources.ResourceHelper
import app.aaps.interfaces.utils.DecimalFormatter

fun CobInfo.generateCOBString(decimalFormatter: DecimalFormatter): String {
    var cobStringResult = "--g"
    displayCob?.let { displayCob ->
        cobStringResult = decimalFormatter.to0Decimal(displayCob)
        if (futureCarbs > 0)
            cobStringResult += "(${decimalFormatter.to0Decimal(futureCarbs)})"
        cobStringResult += "g"
    }
    return cobStringResult
}

fun CobInfo.displayText(rh: ResourceHelper, decimalFormatter: DecimalFormatter): String? =
    displayCob?.let { displayCob ->
        var cobText = rh.gs(info.nightscout.core.ui.R.string.format_carbs, displayCob.toInt())
        if (futureCarbs > 0) cobText += " (" + decimalFormatter.to0Decimal(futureCarbs) + ")"
        cobText
    }
