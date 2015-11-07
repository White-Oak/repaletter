package me.oak.imgcolor.octo;

import java.util.*;
import java.util.stream.Stream;
import lombok.NonNull;
import me.oak.imgcolor.Bag;
import me.oak.imgcolor.Color;
import me.whiteoak.minlog.Log;

/**
 *
 * @author White Oak
 */
public class OctoTreeColor {

    private final static me.oak.imgcolor.util.Timer timer = new me.oak.imgcolor.util.Timer();
    private long gettingTime, removingTime;
    private int timesCalled;
    private int timesCalledInstantly;
    private final OctoNode root = new OctoNode(new Cube(0, 0, 0, 256), 0, null);
    private final LinkedList<Bag<Color>> linkedList = new LinkedList<>();

    public boolean insert(@NonNull Color color) {
	return root.insert(color);
    }

    public int size() {
	return root.size();
    }

    public int depth() {
	return root.depth();
    }

    public Collection<Bag<Color>> getAllIn(@NonNull Cube range) {
	timesCalled++;
	timer.start();
	linkedList.clear();
	root.getAllIn(range, linkedList);
	gettingTime += timer.total();
	return linkedList;
    }

    public Bag<Color> get(@NonNull Color color) {
	return root.get(color);
    }

    public boolean remove(@NonNull Color color) {
	timer.start();
	final boolean remove = root.remove(color);
	removingTime += timer.total();
	return remove;
    }

    public void printDebug() {
	System.out.println("Getting time: " + gettingTime + " ms");
	System.out.println("Removing time: " + removingTime + " ms");
	System.out.println("Called get: " + timesCalled + " times");
	System.out.println("Called get and received result instantly: " + timesCalledInstantly + " times");
    }

    public void printCurrentDebug() {
	System.out.println("Size reached is: " + root.size() + "");
	System.out.println("Real size reached is: " + root.realSize() + "");
    }

    public int realSize() {
	return root.realSize();
    }

    public Bag<Color> getNearest(Color color) {
	timesCalled++;
	timer.start();
	Bag<Color> get = get(color);
	if (get != null) {
	    timesCalledInstantly++;
	    return get;
	}
	OctoNode node = root.getNode(color);
	if (node == null) {
	    System.out.println("s");
	}
	int DELTA = node.bounds.size / 2;
	Optional<Bag<Color>> anyIn;
	do {
	    assert DELTA < 10000 : "The shit is real";
	    DELTA += OctoNode.MIN_WIDTH / 2;
	    anyIn = root.getAnyIn(Cube.centeredAround(node.bounds, DELTA));
	} while (!anyIn.isPresent());
	gettingTime += timer.total();
	return anyIn.get();
    }
}

class OctoNode {

    private final static int MAX_ELEMENTS_NODE = 5;
    private final static int MAX_DEPTH = 7;
    final static int MIN_WIDTH = (int) (256 / Math.pow(2, MAX_DEPTH));
    private ArrayList<Bag<Color>> elements = new ArrayList<>(MAX_ELEMENTS_NODE);
    final Cube bounds;
    private OctoNode[] nodes;
    private final int depth;

    private int currentElementsCapacity = MAX_ELEMENTS_NODE;

    private int cachedSize;

    private final OctoNode parent;

    OctoNode(Cube bounds, int depth, OctoNode parent) {
	this.bounds = bounds;
	this.depth = depth;
	this.parent = parent;
    }

    public boolean insert(Color color) {
	if (!bounds.contains(color)) {
	    return false;
	}
	if (!add(color)) {
	    if (nodes == null) {
		subdivide();
	    }
	    boolean anyInserted = false;
	    for (OctoNode node : nodes) {
		if (node.insert(color)) {
		    anyInserted = true;
		}
	    }
	    assert anyInserted;
	}
	cachedSize++;
	return true;
    }

    private void subdivide() {
	Log.debug("octo-tree", "Dividing into smaller pieces");
	if (nodes != null) {
	    throw new RuntimeException("Cannot subdive more than once");
	}
	nodes = new OctoNode[8];
	final int x = bounds.x;
	final int y = bounds.y;
	final int z = bounds.z;
	final int halfSize = bounds.size / 2;
	final int newDepth = depth + 1;
	nodes[0] = new OctoNode(new Cube(x, y, z, halfSize), newDepth, this);
	nodes[1] = new OctoNode(new Cube(x, y + halfSize, z, halfSize), newDepth, this);
	nodes[2] = new OctoNode(new Cube(x, y, z + halfSize, halfSize), newDepth, this);
	nodes[3] = new OctoNode(new Cube(x, y + halfSize, z + halfSize, halfSize), newDepth, this);
	nodes[4] = new OctoNode(new Cube(x + halfSize, y, z, halfSize), newDepth, this);
	nodes[5] = new OctoNode(new Cube(x + halfSize, y + halfSize, z, halfSize), newDepth, this);
	nodes[6] = new OctoNode(new Cube(x + halfSize, y, z + halfSize, halfSize), newDepth, this);
	nodes[7] = new OctoNode(new Cube(x + halfSize, y + halfSize, z + halfSize, halfSize), newDepth, this);
	elements.forEach(element -> {
	    for (OctoNode node : nodes) {
		for (int i = 0; i < element.amount; i++) {
		    node.insert(element.value);
		}
	    }
	});
	elements = null;
    }

    private void undivide() {
	if (nodes == null) {
	    throw new RuntimeException("Nothing to undivide");
	}
	elements = new ArrayList<>(MAX_ELEMENTS_NODE);
	for (OctoNode node : nodes) {
	    node.getAll(elements);
	}

	nodes = null;
    }

    private boolean add(Color color) {
	if (elements == null) {
	    return false;
	}
	if (cachedSize == currentElementsCapacity) {
	    if (depth == MAX_DEPTH) {
		currentElementsCapacity *= 2;
		elements.ensureCapacity(currentElementsCapacity);
	    } else {
		return false;
	    }
	}
	Bag bag = null;
	for (Bag<Color> element : elements) {
	    if (element.value == color) {
		bag = element;
	    }
	}
	if (bag == null) {
	    bag = new Bag(color);
	    elements.add(bag);
	}
	bag.amount++;
	return true;
    }

    public int realSize() {
	if (nodes == null) {
	    return elements.size();
	} else {
	    return Stream.of(nodes).mapToInt(node -> node.realSize()).sum();
	}
    }

    public int size() {
	return cachedSize;
//	final int size = elements == null ? 0 : elements.size();
//	if (nodes == null) {
//	    return size;
//	}
//	return size
//		+ Stream.of(nodes)
//		.mapToInt(node -> node.size())
//		.sum();
    }

    public int depth() {
	if (nodes == null) {
	    return depth;
	}
	return Stream.of(nodes)
		.mapToInt(node -> node.depth())
		.max()
		.getAsInt();
    }

    private void getAll(List<Bag<Color>> colors) {
	if (cachedSize > 0) {
	    if (elements != null) {
		elements.forEach(colors::add);
	    } else {
		for (OctoNode node : nodes) {
		    node.getAll(colors);
		}
	    }
	}
    }

    public void getAllIn(Cube range, List<Bag<Color>> colors) {
	if (cachedSize > 0) {
	    if (range.contains(bounds)) {
		getAll(colors);
	    } else if (bounds.intersects(range)) {
		if (elements != null) {
		    elements.stream()
			    .filter(bag -> range.contains(bag.value))
			    .forEach(colors::add);
		} else {
		    for (OctoNode node : nodes) {
			node.getAllIn(range, colors);
		    }
		}
	    }
	}
    }

    public Bag<Color> get(Color color) {
	if (cachedSize > 0 && bounds.contains(color)) {
	    if (elements != null) {
		for (Bag<Color> next : elements) {
		    if (next.value == color) {
			return next;
		    }
		}
	    } else {
		for (OctoNode node : nodes) {
		    final Bag<Color> get = node.get(color);
		    if (get != null) {
			return get;
		    }
		}
	    }
	}
	return null;
    }

    public boolean remove(Color color) {
	if (cachedSize > 0 && bounds.contains(color)) {
	    if (elements != null) {
		for (Iterator<Bag<Color>> iterator = elements.iterator(); iterator.hasNext();) {
		    Bag<Color> next = iterator.next();
		    if (next.value == color) {
			next.amount--;
			if (next.amount == 0) {
			    iterator.remove();
			}
			cachedSize--;
			return true;
		    }
		}
	    } else {
		for (OctoNode node : nodes) {
		    if (node.remove(color)) {
			cachedSize--;
			if (cachedSize <= MAX_ELEMENTS_NODE) {
			    undivide();
			}
			return true;
		    }
		}
	    }
	}
	return false;
    }

    private Bag<Color> getAny() {
	if (cachedSize > 0) {
	    if (elements != null) {
		return elements.get(0);
	    } else {
		for (OctoNode node : nodes) {
		    Bag<Color> any = node.getAny();
		    if (any != null) {
			return any;
		    }
		}
	    }
	}
	return null;
    }

    public Optional<Bag<Color>> getAnyIn(Cube range) {
	if (cachedSize > 0) {
	    if (range.contains(bounds)) {
		return Optional.of(getAny());
	    } else if (bounds.intersects(range)) {
		if (elements != null) {
		    for (Bag<Color> next : elements) {
			if (bounds.contains(next.value)) {
			    return Optional.of(next);
			}
		    }
		} else {
		    for (OctoNode node : nodes) {
			Optional<Bag<Color>> opt = node.getAnyIn(range);
			if (opt.isPresent()) {
			    return opt;
			}
		    }
		}
	    }
	}
	return Optional.empty();
    }

    OctoNode getNode(Color color) {
	if (bounds.contains(color)) {
	    if (elements != null) {
		return this;
	    } else {
		for (OctoNode node : nodes) {
		    final OctoNode get = node.getNode(color);
		    if (get != null) {
			return get;
		    }
		}
	    }
	}
	return null;
    }

    public boolean contains(Color color) {
	return bounds.contains(color);
    }

}
