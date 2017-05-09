import static org.junit.Assert.*;

import org.junit.Test;

public class LLOATest {

	@Test
	public void testGet() {
		LinkedListOverArray<Integer, Entry<Integer>> list = new LinkedListOverArray<>(2);
		Entry<Integer> j = () -> 1;
		Entry<Integer> x = () -> 2;
		list.insertFirst(x);
		list.insertFirst(j);
	
		assertEquals(j, list.get(1));
		assertEquals(x, list.get(2));
	}

	@Test
	public void testGetAt() {
		LinkedListOverArray<Integer, Entry<Integer>> list = new LinkedListOverArray<>(2);
		Entry<Integer> j = () -> 1;
		Entry<Integer> x = () -> 2;
		list.insertFirst(x);
		list.insertFirst(j);
	
		assertEquals(j, list.getAt(0));
		assertEquals(x, list.getAt(1));
	}

	@Test
	public void testInsert() {
		LinkedListOverArray<Integer, Entry<Integer>> list = new LinkedListOverArray<>(5);
		list.insert(0, () -> 1);
		Entry<Integer> j = () -> 2;		
		list.insert(0, j);
		list.insert(2, () -> 3);
		Entry<Integer> x = () -> 4;
		list.insert(2, x);
		assertEquals(4, list.size());
		assertEquals(x, list.get(4));
		assertEquals(j, list.getAt(0));
	}

	@Test
	public void testInsertAfter() {
		LinkedListOverArray<Integer, Entry<Integer>> list = new LinkedListOverArray<>(2);
		Entry<Integer> j = () -> 1;
		Entry<Integer> x = () -> 2;
		list.insertLast(x);
		list.insertAfter(x.key(), j);
		assertEquals(x, list.getAt(0));
		assertEquals(j, list.getAt(1));
		
		assertEquals(2, list.size());
	}

	@Test
	public void testInsertBefore() {
		LinkedListOverArray<Integer, Entry<Integer>> list = new LinkedListOverArray<>(2);
		Entry<Integer> j = () -> 1;
		Entry<Integer> x = () -> 2;
		list.insertFirst(x);
		list.insertBefore(x.key(), j);
		assertEquals(x, list.getAt(1));
		assertEquals(j, list.getAt(0));
		
		assertEquals(2, list.size());
	}

	@Test
	public void testInsertFirst() {
		LinkedListOverArray<Integer, Entry<Integer>> list = new LinkedListOverArray<>(2);
		Entry<Integer> j = () -> 1;
		Entry<Integer> x = () -> 2;
		list.insertFirst(x);
		list.insertFirst(j);
		assertEquals(x, list.getAt(1));
		assertEquals(j, list.getAt(0));
		
		assertEquals(2, list.size());
	}

	@Test
	public void testInsertLast() {
		LinkedListOverArray<Integer, Entry<Integer>> list = new LinkedListOverArray<>(2);
		Entry<Integer> j = () -> 1;
		Entry<Integer> x = () -> 2;
		list.insertLast(x);
		list.insertLast(j);
		assertEquals(x, list.getAt(0));
		assertEquals(j, list.getAt(1));
		
		assertEquals(2, list.size());
	}

	@Test
	public void testRemove() {
		LinkedListOverArray<Integer, Entry<Integer>> list = new LinkedListOverArray<>(4);
		list.insertLast(() -> 1);
		list.insertLast(() -> 2);
		list.insertLast(() -> 3);
		list.insertLast(() -> 4);
		
		list.remove(1);
		assertEquals(3, list.size());
		
		list.remove(2);
		assertEquals(2, list.size());
		
		list.remove(4);
		assertEquals(1, list.size());
		
		int i = list.getAt(0).key();
		
		assertEquals(i, 3);
		
		list.remove(3);
		assertEquals(0, list.size());
	}

	@Test
	public void testRemoveAt() {
		LinkedListOverArray<Integer, Entry<Integer>> list = new LinkedListOverArray<>(2);
		Entry<Integer> j = () -> 1;
		Entry<Integer> x = () -> 2;
		list.insertFirst(x);
		list.insertFirst(j);

		list.removeAt(0);
		
		assertEquals(x, list.getAt(0));
		assertEquals(1, list.size());
		
		list.removeAt(0);
		assertEquals(0, list.size());

		boolean throwed = false;
		try {
			list.getAt(0);
		} catch (Exception e) {
			throwed = true;
		}
		
		assertTrue(throwed);
	}

	@Test
	public void testRemoveFirst() {
		LinkedListOverArray<Integer, Entry<Integer>> list = new LinkedListOverArray<>(100);
		for (int i = 0; i < 100; i++) {
			int key = i;
			list.insertLast(() -> key);
		}
		
		for (int i = 0; i < 10; i++) {
			list.removeFirst();
		}
		
		int key = list.getAt(0).key();
		assertEquals(10, key);

		key = list.getAt(10).key();
		assertEquals(20, key);
		
		assertEquals(90, list.size());
	}
	
	@Test
	public void testRemoveLast() {
		LinkedListOverArray<Integer, Entry<Integer>> list = new LinkedListOverArray<>(100);
		for (int i = 0; i < 100; i++) {
			int key = i;
			list.insertLast(() -> key);
		}
		
		for (int i = 0; i < 10; i++) {
			list.removeLast();
		}
		
		int key = list.getAt(list.size()-1).key();
		assertEquals(89, key);
	
		assertEquals(90, list.size());
	}

}
