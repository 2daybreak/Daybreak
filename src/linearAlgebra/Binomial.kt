package linearAlgebra

fun factorial(n: Int): Int {
    if(n == 0) return 1
    else return n * factorial(n - 1)
}

fun binomialCoef(n: Int, k: Int): Int {
    var k = k
    if (k > n - k) k = n - k
    var res = 1
    for (i in 0 until k) {
        res *= n - i
        res /= i + 1
    }
    return res
}