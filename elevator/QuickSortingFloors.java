
package elevator;

/**
 * <p>
 * choose the last element of the array is pivot.
 * </p>
 * 
 * @see <a href="http://www.java2novice.com/java-sorting-algorithms/quick-sort/">Java2Novice - Quick Sort</a>
 * @see <a href="https://www.geeksforgeeks.org/quick-sort/">Geeks for Geeks - Quick Sort</a>
 * 
 * @author Shahriar (Shawn) Emami
 * @version Feb 13, 2018
 * 
 * /**
 * Assignment 2 Reference from Professor Shahriar (Shawn) Emami
 * 
 * @author Yi Jiang Class QuickSortingFloors
 * @version March.11, 2018
 */
 
public class QuickSortingFloors{

	/**
	 * method will sort the floor which user have chosed
	 * @param array floor list
	 * @param size arry length
	 */
	public static void sort( int[] array, final int size){
		if( array == null || array.length < 2){
			return;
		}
		try {
		sortRecursive( array, 0, size - 1);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
/**
 * the method will sort the floor recursive
 * @param array floor list
 * @param low- the minimun floor number 
 * @param high-the maximun floor number
 */
	public static void sortRecursive( int[] array, final int low, final int high){
		// base case all data has been sorted
		if( low > high){
			return;
		}

		int pivot = partition( array, low, high);
		//break array on pivot point
		sortRecursive( array, low, pivot - 1);
		sortRecursive( array, pivot + 1, high);

	}

	/**
	 * method will show the floor partition
	 * @param array list of floor
	 * @param low - the minimun floor number 
	 * @param high -the maximun floor number
	 * @return
	 */
	private static int partition( int[] array, final int low, final int high){
		int pivot = array[high];// choose last element as pivot
		int i = (low - 1); // assume left most element is smallest
		try {
		for( int j = low; j < high; j++){// start at smallest index
			// If current element is smaller than or equal to pivot
			if( array[j] <= pivot){
				i++;// keep track of the last partitioned number smaller than pivot
				swap( array, i, j);// swap smaller than pivot number with index j
			}
		}
		// bring pivot to correct position
		swap( array, i + 1, high);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return i + 1;
	}

	/**
	 * method will sort the floor use swap method
	 * @param array -list of floor
	 * @param source -different floor
	 * @param dest -different target floor
	 */
	private static void swap( int[] array, final int source, final int dest){
		try {
		int num = array[source];
		array[source] = array[dest];
		array[dest] = num;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	}
