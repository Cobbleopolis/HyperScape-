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

    /**
     * Drops an amount of elements starting at the index from the passed array
     * @param array The array to drop the index from
     * @param index The index to drop
     * @param size amount after the index to drop
     * @return The array without the removed indexes
     */
    def dropIndex(array: Array[Any], index: Int, size: Int): Array[Any] = {
        array.clone().take(index).++(array.clone().drop(index + size))
    }

    def subset(array: Array[Any], index: Int): Array[Any] = {
        array.dropRight(index)
    }

    def subset(array: Array[Any], startIndex: Int, endIndex: Int): Array[Any] = {
        subset(array, endIndex).drop(startIndex)
    }
}
