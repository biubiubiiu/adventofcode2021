package toolbox

data class Point(
    var x: Int,
    var y: Int
) {

    companion object {
        val Zero: Point = Point(0, 0)
        val One: Point = Point(1, 1)
        val Up: Point = Point(0, 1)
        val Down: Point = Point(0, -1)
        val Left: Point = Point(-1, 0)
        val Right: Point = Point(1, 0)

        operator fun invoke(): Point = Point(0, 0)
        operator fun invoke(v: Point): Point = Point(v.x, v.y)
        operator fun invoke(xy: Int): Point = Point(xy, xy)

        fun middle(a: Point, b: Point, out: Point = Point()): Point = out.setTo((a.x + b.x) / 2, (a.y + b.y) / 2)

        private fun square(x: Int) = x * x
        fun distanceSquared(a: Point, b: Point): Int = square(a.x - b.x) + square(a.y - b.y)
    }

    fun setTo(x: Int, y: Int): Point {
        this.x = x
        this.y = y
        return this
    }

    fun neg() = setTo(-this.x, -this.y)
    fun mul(s: Int) = setTo(this.x * s, this.y * s)

    fun add(p: Point) = this.setToAdd(this, p)
    fun add(x: Int, y: Int) = this.setTo(this.x + x, this.y + y)

    fun sub(p: Point) = this.setToSub(this, p)
    fun sub(x: Int, y: Int) = this.setTo(this.x - x, this.y - y)

    fun copyFrom(that: Point) = setTo(that.x, that.y)

    fun setToAdd(a: Point, b: Point): Point = setTo(a.x + b.x, a.y + b.y)
    fun setToSub(a: Point, b: Point): Point = setTo(a.x - b.x, a.y - b.y)
    fun setToMul(a: Point, b: Point): Point = setTo(a.x * b.x, a.y * b.y)
    fun setToMul(a: Point, s: Int): Point = setTo(a.x * s, a.y * s)
    fun setToDiv(a: Point, b: Point): Point = setTo(a.x / b.x, a.y / b.y)
    fun setToDiv(a: Point, s: Int): Point = setTo(a.x / s, a.y / s)
    operator fun plusAssign(that: Point): Unit = run { setTo(this.x + that.x, this.y + that.y) }

    operator fun plus(that: Point): Point = Point(this.x + that.x, this.y + that.y)
    operator fun minus(that: Point): Point = Point(this.x - that.x, this.y - that.y)
    operator fun times(that: Point): Point = Point(this.x * that.x, this.y * that.y)
    operator fun div(that: Point): Point = Point(this.x / that.x, this.y / that.y)
    infix fun dot(that: Point): Int = this.x * that.x + this.y * that.y

    operator fun times(scale: Int): Point = Point(this.x * scale, this.y * scale)

    operator fun div(scale: Int): Point = Point(this.x / scale, this.y / scale)

    operator fun get(index: Int) = when (index) {
        0 -> this.x;
        1 -> this.y
        else -> throw IndexOutOfBoundsException("Point doesn't have $index component")
    }

    fun copy() = Point(this.x, this.y)
}