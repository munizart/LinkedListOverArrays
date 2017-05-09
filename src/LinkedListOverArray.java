import java.util.Arrays;

/**
 * A simple list implemented over arrays.
 * @param <V> The type of data to be stored.
 */
public class LinkedListOverArray<K, V extends Entry<K>> {
	private int[] links;
	private V[] data;
	private Item firstData;
	private Item firstEmpty;
	private int size;
	
	/**
	 * Construct the list within the given initial capability
	 * @param capability The initial capability of the list's arrays;
	 */
	@SuppressWarnings("unchecked")
	public LinkedListOverArray(int capability){
		this.links = fillArray(capability);
		this.data = ((V[]) (new Entry[capability]));
		this.size = 0;
		this.firstData = new Item(-1);
		this.firstEmpty = new Item(0);
	}

	/** 
	 * Get the data represented by the given key.
	 * @param key The key representing an item in the list;
	 * @return the data represented by that key
	 */
	public V get(K key) {
		Item item = getItem(key);
		if (item != null) {
			return item.value();
		} else {
			throw new IllegalArgumentException("The list must have an item within the provided key");
		}
	}

	/**
	 * Get the data stored at the given position 
	 * @param index
	 * @return
	 */
	public V getAt(int index) {
		if (index > -1 && index < this.size) {
			Item item = getItemAt(index);
			return item.value();
		} else {
			throw new IndexOutOfBoundsException();
		}
	}

	/**
	 * Stores data at the given position
	 * @param position Position to insert item. <br/> Must be in the range of 0 < position < n <br/> Where n = list size.
	 * @param data The item to be stored.
	 */
	public void insert(int position, V data){

		if (position > -1 && position <= this.size) {
			expandsIfNecessary(); //Make more room if list is full.
			
			this.size++;
			
			//Lets use the next empty item to store data.
			Item empty = this.firstEmpty;
			
			//Remember to keep track of our empty items.
			this.firstEmpty = empty.nextItem();
			
			//Store the data
			empty.value(data);
			
			if (position > 0) {
				Item previous = getItemAt(position-1);
				Item next = previous.nextItem();

				//We are going to insert our item between this two.
				previous.linkTo(empty);
				empty.linkTo(next);
			} else {
				//Unless we are inserting at the begin.
				empty.linkTo(this.firstData);
				this.firstData = empty;
			}

		} else {
			throw new IndexOutOfBoundsException();
		}
		
	}

	/**
	 * Stores data after the item represented by the given key.
	 * @param key The key representing an item in the list.
	 * @param data The data to be stored.
	 */
	public void insertAfter(K key, V data) {
		Item item = getItem(key);
		if (item != null) {
			int index = item.nextIndex();
			index = index < 0 ? this.size : index;
			insert(index, data);
		} else {
			throw new IllegalArgumentException("The list must have an item within the provided key");
		}
	}

	/**
	 * Stores data before the item represented by the given key.
	 * @param key The key representing an item in the list.
	 * @param data The data to be stored.
	 */
	public void insertBefore(K key, V data) {
		Item item = getItem(key);
		if (item != null) {
			insert(item.index, data);
		} else {
			throw new IllegalArgumentException("The list must have an item within the provided key");
		}
	}

	/**
	 * Stores an data at the begining
	 * @param data The data to be stored
	 */
	public void insertFirst(V data) {
		insert(0, data);
	}

	/**
	 * Stores data at the end
	 * @param data The data to be stored
	 */
	public void insertLast(V data) {
		insert(this.size, data);
	}
	
	/**
	 * Removes the data represented by the given key.
	 * @param key
	 */
	public void remove(K key) {
		if (!this.isEmpty()) {
			if (this.firstData.value().key().equals(key)) {
				size--;
				removeFirstItem();
			} else {	
				Item before = getItemBefore(key);
				if (before != null) {
					size--;
					removeNextTo(before);
				} else {
					throw new IllegalArgumentException("The list must have an item within the provided key");
				}
			}
		} else {
			throw new IllegalStateException();
		}
	}

	/**
	 * remove the item at the given index.
	 * @param index
	 */
	public void removeAt(int index) throws IllegalStateException {
		if (index > -1 && index < this.size) {
			this.size--;
			
			if (index == 0) {
				removeFirstItem();
			} else {
				removeNextTo(getItemAt(index -1));
			}
		} else {
			if (this.isEmpty()) {
				throw new IllegalStateException("List is empty");
			} else {
				throw new IndexOutOfBoundsException();
			}
		}
	}

	/**
	 * removes the first element
	 */
	public void removeFirst() {
		removeAt(0);
	}

	/**
	 * removes the last element
	 */
	public void removeLast() {
		removeAt(size - 1);
	}

	/**
	 * @return The list's size
	 */
	public int size(){
		return this.size;
	}

	/**
	 * Avoid memory leak by keeping references to an object in the data array.
	 * @param index The index to be cleared
	 */
	private void clearSlotAt(int index) {
		if (0 < index && index < this.data.length) {
			this.data[index] = null;
		}
	}

	/**
	 * When list is full, expands its capability 
	 */
	private void expandsIfNecessary() {
		if (this.size == this.links.length) {
			this.firstEmpty = new Item(this.links.length);
			this.links = fillArray(links, links.length);
			this.data = Arrays.copyOf(this.data, this.links.length);
		}
	}

	/**
	 * @param capability The array size
	 * @return An new array whose each elements are filled ascendently from 1. 
	 */
	private int[] fillArray(int capability) {
		return fillArray(new int[capability], 0);
	}

	/**
	 * @param array The array to be appended
	 * @param offset the amount to increase the array's length
	 * @return An new array resulting from merging the supplied array with an array filled ascendently from offset+1 to 2offset.
	 */
	private int[] fillArray(int[] array, int offset) {
		int[] newArray = Arrays.copyOf(array, array.length+offset);
		
		for (int i = offset; i < newArray.length; i++) {
			newArray[i] = i+1;
		}
	
		return newArray;
	}

	/**
	 * Return the pseudo-node item for the given key;
	 * @param key
	 * @return
	 */
	private Item getItem(K key) {
		if (!this.isEmpty()) {
			Item current = this.firstData;
			Item prev;
			do {
				prev = current;
				if (current.value().key().equals(key)) {
					return current;
				} else {
					current = current.nextItem();
				}
			} while (prev.hasNext());
		}
		return null;
	}

	/**
	 * Get item at given index.
	 * @param index the item's index. Check if it's inbounds first!
	 * @return the item that lies in the supplied position
	 */
	private Item getItemAt(int index) {
		Item item = this.firstData;
		for (int i = 0; i < index; i++) {  
			item = item.nextItem();
		}
		return item;
	}
	
	/**
	 * Find the node before the one represented by the given key
	 * @param key
	 * @return
	 */
	private Item getItemBefore(K key) {
		Item current = this.firstData;
		int i = 1;
		while (++i < this.size && !current.nextItem().value().key().equals(key)) {
			current = current.nextItem();
		}
		if (current.nextItem().value().key().equals(key)) {
			return current;
		} else {
			return null;
		}
	}

	/**
	 * @return either the list is empty or not 
	 */
	private boolean isEmpty() {
		return this.size == 0;
	}

	/**
	 * Take an item, prepend it to the empty slots list and erasure the data array at that index to prevent memory leak
	 * @param item
	 */
	private void markAsFree(Item item){
		//Mark that slot as free again.
		item.linkTo(this.firstEmpty);
		this.firstEmpty = item;
	
		//Avoid memory leak by keeping references to an object.
		clearSlotAt(item.index);
	}

	/**
	 * Remove the read of list an transforms it into its sub list.
	 */
	private void removeFirstItem(){
		Item toBeRemoved = this.firstData;
	
		this.firstData = this.firstData.nextItem();
	
		markAsFree(toBeRemoved);
	}

	/**
	 * Take an item and remove the next item by link to the next-next item.
	 * @param item The item that precedes the deleted-to-be item.
	 */
	private void removeNextTo(Item item) {
		Item toBeRemoved = item.nextItem();

		item.linkTo(toBeRemoved.nextItem());

		markAsFree(toBeRemoved);
	}
	
	/**
	 * This class interfaces a node behavior.
	 */
	private class Item {
		boolean emptyItemFlag;
		int index;
		public Item(int index) {
			this.index = index;
			emptyItemFlag = index < 0;
		}
		
		public boolean hasNext() {
			return !this.nextItem().emptyItemFlag;
		}

		public void linkTo(Item next) {
			link(this.index, next.index);
		}

		public Item nextItem() {
			if (!this.emptyItemFlag) {
				return new Item(this.nextIndex());
			} else {
				return null;
			}
		}

		public void value(V item) {
			data[this.index] = item;
		}

		public V value() {
			if (this.emptyItemFlag) {
				return null;
			} else {
				return data[this.index];
			}
		}

		private int link(int k) {
			return links[k];
		}
		
		private void link(int k, int j) {
			links[k] = j;
		}

		private int nextIndex() {
			return link(this.index);
		}

	}
	
}
