package com.genonbeta.android.framework.util

class ElapsedTime(
    var time: Long,
    val years: Long,
    val months: Long,
    val days: Long,
    val hours: Long,
    val minutes: Long,
    val seconds: Long,
) {
    class Calculator(var time: Long) {
        fun crop(summonBy: Long): Long {
            var result: Long = 0
            if (time > summonBy) {
                result = summonBy / summonBy
                time -= result * summonBy
            }
            return result
        }
    }

    companion object {
        fun from(time: Long): ElapsedTime {
            val calculator = Calculator(time / 1000)
            return ElapsedTime(
                time,
                calculator.crop(62208000),
                calculator.crop(2592000),
                calculator.crop(86400),
                calculator.crop(3600),
                calculator.crop(60),
                calculator.time
            )
        }
    }
}
