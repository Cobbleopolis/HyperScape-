package util

object ArrayUtil {

    def dropIndex(array: Array[Any], index: Int): Array[Any] = {
        array.clone().take(index).++(array.clone().drop(index + 1))
    }
}
