package util

object ArrayUtil {

    /**
     * Drops the index from the passed array
     * @param array The array to drop the index from
     * @param index The index to drop
     * @return The array without the passed index
     */
    def dropIndex(array: Array[Any], index: Int): Array[Any] = {
        array.clone().take(index).++(array.clone().drop(index + 1))
    }
}
