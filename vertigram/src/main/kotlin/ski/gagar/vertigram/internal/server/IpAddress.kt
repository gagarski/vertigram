package ski.gagar.vertigram.internal.server

import java.math.BigInteger

private const val BITS_IN_OCTET = 8
private const val BITS_IN_DOUBLE_OCTET = BITS_IN_OCTET + BITS_IN_OCTET
private const val MAX_OCTET = (1 shl (BITS_IN_OCTET)) - 1
private const val ALL_ONES_OCTET = (1 shl (BITS_IN_OCTET)) - 1
private const val MAX_DOUBLE_OCTET = (1 shl (BITS_IN_DOUBLE_OCTET)) - 1

/**
 * Interface for IP address
 */
interface IpAddress {
    /**
     * Protocol version (4 or 6)
     */
    val version: Int

    /**
     * Bytes of the address
     */
    val bytes: List<Int>

    companion object {
        /**
         * Create an address from [String]. IPv4 and IPv6 addresses are supported.
         */
        operator fun invoke(addr: String) = try {
            IpV4Address(addr)
        } catch (ex: IllegalArgumentException) {
            IpV6Address(addr)
        }

        /**
         * Create an address from list of bytes represented as [List] of [Int]. IPv4 and IPv6 addresses are supported.
         */
        operator fun invoke(bytes: List<Int>) = when (bytes.size) {
            IpV6Address.ADDR_LENGTH -> IpV6Address(bytes)
            IpV4Address.ADDR_LENGTH -> IpV4Address(bytes)
            else -> throw IllegalArgumentException("Only IP v4 and IP v6 addresses are supported")
        }
    }
}

/**
 * IPv6 address
 */
class IpV6Address(bytes: List<Int>) : IpAddress {
    override val bytes: List<Int> = bytes.toList()

    init {
        require(bytes.size == ADDR_LENGTH) {
            "Address should be $ADDR_LENGTH bytes length: got ${bytes.size}"
        }
        require(bytes.all { it in 0..MAX_DOUBLE_OCTET })
    }
    override val version: Int = VERSION

    override fun toString(): String = buildString {
        val nums = bytes.chunked(OCTETS_IN_CHUNKS).map { ((it[0] shl BITS_IN_OCTET) or  it[1]) }
        val skip = longestSkip(nums)
        val beforeSkip = nums.subList(0, skip.first)
        if (beforeSkip.isNotEmpty())
            append(beforeSkip.joinToString(DELIMITER) { it.toString(CHUNK_RADIX) })

        if (skip.last - skip.first > 0)
            append(SKIP_DELIMITER)

        val afterSkip = nums.subList(skip.last + 1, nums.size)
        if (afterSkip.isNotEmpty())
            append(afterSkip.joinToString(DELIMITER) { it.toString(CHUNK_RADIX) })

    }

    companion object {
        const val VERSION = 6
        private const val CHUNK_RADIX = 16
        private const val OCTETS_IN_CHUNKS = 2
        private const val SKIP_DELIMITER = "::"
        private const val DELIMITER = ":"
        const val ADDR_LENGTH = 16

        private fun longestSkip(nums: List<Int>): IntRange {
            var longestStart = -1
            var longestEnd = -1
            var currentStart = -1

            fun handleEnd(ix: Int) {
                if (ix - currentStart > longestEnd - longestStart) {
                    longestStart = currentStart
                    longestEnd = ix
                }
            }

            for ((ix, num) in nums.withIndex()) {
                if (num == 0 && currentStart == -1) {
                    currentStart = ix
                } else if (num != 0 && currentStart != -1) {
                    handleEnd(ix)
                    currentStart = -1
                }
            }

            if (currentStart != -1) {
                handleEnd(nums.size)
            }

            return longestStart until longestEnd
        }

        private fun parsePart(part: String): List<Int> {
            if (part.isEmpty()) return listOf()

            return part.split(DELIMITER).asSequence().map {
                val res = it.toInt(CHUNK_RADIX)
                require(res in 0..MAX_DOUBLE_OCTET) {
                    "Bad address chunk ${res.toString(CHUNK_RADIX)} should be in " +
                            "[0, 0x${MAX_DOUBLE_OCTET.toString(CHUNK_RADIX)}]"
                }
                res
            }.flatMap { listOf(it / (MAX_OCTET + 1), it % (MAX_OCTET + 1)).asSequence() }.toList()
        }

        /**
         * Create IPv6 address from [String]
         */
        operator fun invoke(addr: String): IpV6Address {
            val parts = addr.split(SKIP_DELIMITER)
            require(parts.size <= 2) {
                "Can have only one block skipped"
            }

            val bytesBefore = parsePart(parts[0])

            require(bytesBefore.size == ADDR_LENGTH || parts.size == 2) {
                "Address should be $ADDR_LENGTH bytes, $bytesBefore found"
            }

            val bytesAfter = parts.getOrNull(1)?.let { parsePart(it) } ?: listOf()

            return IpV6Address((
                    bytesBefore.asSequence() +
                            generateSequence { 0 }.take(ADDR_LENGTH - (bytesBefore.size + bytesAfter.size)) +
                            bytesAfter.asSequence()).toList())
        }

        /**
         * Create network address from [Int] representation.
         */
        fun mask(number: Int) = intMaskToAddr(number, ADDR_LENGTH * BITS_IN_OCTET)
    }
}

/**
 * IPv4 address
 */
class IpV4Address(bytes: List<Int>) : IpAddress {
    override val version: Int = 4
    override val bytes: List<Int> = bytes.toList()

    init {
        require(bytes.size == ADDR_LENGTH) {
            "Address should be $ADDR_LENGTH bytes length: got ${bytes.size}"
        }

        require(bytes.all { it in 0..MAX_OCTET })
    }

    override fun toString(): String = bytes.joinToString(DELIMITER)

    companion object {
        const val ADDR_LENGTH = 4
        const val DELIMITER = "."

        /**
         * Create IPv4 address from [String]
         */
        operator fun invoke(addr: String): IpV4Address {
            val parts = addr.split(DELIMITER).map { it.toInt() }
            require(parts.size == ADDR_LENGTH) {
                "Address should be $ADDR_LENGTH bytes length: got ${parts.size}"
            }
            return IpV4Address(parts)
        }

        /**
         * Create network address from [Int] representation.
         */
        fun mask(number: Int) = intMaskToAddr(number, IpV6Address.ADDR_LENGTH * BITS_IN_OCTET)
    }
}

/**
 * A bundle of IP address and network mask
 */
class IpNetworkAddress(val ip: IpAddress, val mask: IpAddress) {
    init {
        require(ip.version == mask.version) {
            "Mask and address should have the same version"
        }
        require(ip.toBigInteger() and mask.toBigInteger().not() == BigInteger.ZERO) {
            "Not a network address ${ip}/${mask}"
        }
    }
    val version: Int = ip.version
    private val maskInt: Int
        get() {
            val bigInt = mask.toBigInteger()
            val bits = ip.bytes.size * 8
            var count = 0
            var onesEnded = false
            for (i in bits - 1 downTo 0) {
                if (!bigInt.testBit(i)) {
                    onesEnded = true
                } else if (!onesEnded) {
                    count++
                } else {
                    return -1
                }
            }
            return count
        }

    operator fun contains(ip: IpAddress) =
        ip.version == this.version && ip.toBigInteger() and mask.toBigInteger() == this.ip.toBigInteger()

    fun toString(briefIfPossible: Boolean): String {
        val mask = if (briefIfPossible) {
            val maskInt = maskInt
            if (maskInt == -1) mask.toString() else maskInt.toString()
        } else {
            mask.toString()
        }
        return "$ip/$mask"
    }

    override fun toString(): String = toString(true)

    companion object {
        const val DELIMITER = "/"

        /**
         * Create [IpNetworkAddress] from string like `192.168.0.1/24`
         */
        operator fun invoke(str: String): IpNetworkAddress {
            val split = str.split(DELIMITER)

            require(split.size == 2) {
                "Network mask should have format \"addr/mask\""
            }

            val (ipStr, maskStr) = split

            val ip = IpAddress(ipStr)

            val maskInt = maskStr.toIntOrNull()

            val mask = maskInt?.let { intMaskToAddr(it, ip.bytes.size * BITS_IN_OCTET) } ?:
                    IpAddress(maskStr)


            return IpNetworkAddress(ip, mask)
        }
    }
}

private fun IpAddress.toBigInteger(): BigInteger =
    BigInteger(1, this.bytes.map { it.toByte() }.toByteArray())


private fun intMaskToAddr(mask: Int, targetLen: Int): IpAddress {
    require(targetLen % 8 == 0) {
        "Target length should be divisible by 8"
    }
    require(mask <= targetLen) {
        "Too big mask"
    }
    val bytes = mutableListOf<Int>()

    for (i in 0 until mask / 8)
        bytes.add(ALL_ONES_OCTET)

    if (mask != targetLen)
        bytes.add(0xff shl (BITS_IN_OCTET - mask % BITS_IN_OCTET) and 0xff)

    for (i in mask / BITS_IN_OCTET + 1 until targetLen / BITS_IN_OCTET) {
        bytes.add(0)
    }

    return IpAddress(bytes)
}
