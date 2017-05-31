package bob

import java.lang.reflect.Modifier

class PrettyPrint {

    static boolean prettyPrint(def obj, String prefix = '') {
        if (obj.metaClass.methods.name.contains('prettyPrint')) {
            obj.&prettyPrint(prefix)
            return true
        }

        println "$prefix{ "
        prefix += "  "
        println "$prefix<${obj.getClass().simpleName}>"
        List properties = obj.metaClass.properties.findAll { !(it.modifiers & Modifier.STATIC) }.name - ['class']
        int length = properties*.size().max() + 1
        Map subs = [:]
        properties.each { String name ->
            def value = obj."$name"
            if(value instanceof Closure) {
                int a = 1
            } else if (value instanceof Collection || (value.getClass().canonicalName.contains('bob.') && !(value instanceof Enum)))
                subs."$name" = value
            else
                println "$prefix${name.padRight(length)}: $value"
        }
        subs.sort { a, b ->
            boolean aC = a.value instanceof Collection
            boolean bC = b.value instanceof Collection
            if (aC && bC) return 0
            else if (aC && !bC) return 1
            else if (!aC && bC) return -1
            return 0
        }.each { name, value ->
            def nextPrefix = prefix + ' ' * (length + 2)
            print "$prefix${name.padRight(length)}:"
            if (value instanceof Collection) {
                println " ["
                value.eachWithIndex { it, idx ->
                    def objEnd = prettyPrint(it, nextPrefix + '  ')
                    if (idx < value.size() - 1)
                        if (objEnd)
                            println "${nextPrefix + '  '},"
                        else
                            println ","
                    else if (!objEnd)
                        println()
                }
                println "$nextPrefix]"
            } else {
                println()
                prettyPrint(value, prefix + ' ' * (length + 2))
                println()
            }
        }
        print "${prefix[0..<-2]}}"
        if (prefix.size() == 2)
            println()
        return false
    }
}
