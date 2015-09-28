package ndfs.mcndfs_1_improved;

import graph.State;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class provides a color map for graph states.
 */
public class Colors {

    private final Map<Integer, Color> map = new ConcurrentHashMap<Integer, Color>();

    /**
     * Returns <code>true</code> if the specified state has the specified color,
     * <code>false</code> otherwise.
     *
     * @param state
     *            the state to examine.
     * @param color
     *            the color
     * @return whether the specified state has the specified color.
     */
    public boolean hasColor(State state, Color color) {

        // The initial color is white, and is not explicitly represented.
        if (color == Color.WHITE) {
            return map.get(state.hashCode()) == null;
        } else {
            return map.get(state.hashCode()) == color;
        }
    }

    /**
     * Gives the specified state the specified color.
     *
     * @param state
     *            the state to color.
     * @param color
     *            color to give to the state.
     */
    public void color(State state, Color color) {
        if (color == Color.WHITE) {
            map.remove(state.hashCode());
        } else {
            map.put(state.hashCode(), color);
        }
    }

    public Color getColor(State state) {
        return map.get(state.hashCode());
    }
}
