package calculate


val COPPER = 0
val ALUM = 1

val al_cross_section = arrayOf(2.5, 4.0, 6.0, 10.0, 16.0, 25.0)
val cop_cross_section = arrayOf(1.5, 2.5, 4.0, 6.0, 10.0, 16.0)


fun calculate_interval(power: Float?, imphour: Int?): Int {
    if (power != null && imphour != null) {
        return (3600 / (power * imphour)).toInt()
    }
    return 0
}

fun calculate_time(power: Float?, imphour: Int?, imps: Int?): Int {
    return ((3600* imps!!)/(power!! * imphour!!)).toInt()
}

fun format_seconds(t: Int?): Pair<Int, Int> {
    if (t != null) {
        if (t >= 60) {
            var minutes = t / 60
            var sec = t - minutes * 60
            return Pair(minutes, sec)
        }
        return Pair(0, t)
    }
    return Pair(0, 0)
}

fun get_power(volt: Float, amper: Float):Float{
    return volt*amper
}

fun validate(str: String?): Boolean{
    if (str == null || str.isBlank()){
        return true
    }
    return false
}

abstract class Wire{
    abstract var cross_section_amperage: Map<Double, Double>
    abstract var resistivity: Double

    open fun get_resistance(len_wire: Double, cross: Double): Double {
         return (resistivity * len_wire) / cross
    }

    open fun get_amperage(power:Double): Double{
        return power*0.75/220
    }

    open fun get_deltaU(amper: Double, resist: Double): Pair<Double, Double>{
        var delta = amper*resist
        var percent = delta/(220/100)
        return Pair(delta, percent)
    }

    open fun get_cross(amper: Double, len_wire: Double): Double{
        for((cross, amp) in cross_section_amperage){
            if(amper < amp) {
                var res = (resistivity * len_wire) / cross
                var (delta, percent) = get_deltaU(amper, res)
                if (percent < 5) {
                    return cross
                }
            }
        }
        return 0.0
    }

    open fun get_max_amperage(cross:Double): Double{
        if(cross in cross_section_amperage){
            return cross_section_amperage[cross]!!
        }
        return 0.0
    }

}

class Copper: Wire(){
    override var cross_section_amperage = mapOf(
        1.5 to 19.0,
        2.5 to 25.0,
        4.0 to 35.0,
        6.0 to 42.0,
        10.0 to 55.0,
        16.0 to 75.0,
    )
    override var resistivity: Double = 0.0175

}

class Alumin: Wire(){
    override var cross_section_amperage = mapOf(
        2.5 to 19.0,
        4.0 to 27.0,
        6.0 to 32.0,
        10.0 to 42.0,
        16.0 to 60.0,
        25.0 to 75.0,
    )
    override var resistivity: Double = 0.0218

}








