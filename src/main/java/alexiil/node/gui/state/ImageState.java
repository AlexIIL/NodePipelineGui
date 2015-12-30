package alexiil.node.gui.state;

import java.awt.image.BufferedImage;
import java.util.*;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import alexiil.node.gui.ImageMeta;
import alexiil.node.gui.ResourceManager;

public class ImageState {
    public final BufferedImage image;
    public final ImageMeta meta;
    private final String identifier;

    private Map<String, ImageState> states = new HashMap<>();
    private ImmutableSet<IState> thisStates;
    private Map<ImmutableSet<IState>, String> stateChanges = new HashMap<>();

    public ImageState(String imgLoc, IState... states) {
        this(imgLoc, Lists.newArrayList(states));
    }

    public ImageState(String imgLoc, Collection<IState> states) {
        this.identifier = imgLoc;
        this.image = ResourceManager.loadImageCrashing(imgLoc);
        this.meta = ResourceManager.loadImageMetaCrashing(imgLoc.replace(".png", ".json"), image);
        this.states.put(identifier, this);
        thisStates = ImmutableSet.copyOf(states);
        stateChanges.put(thisStates, identifier);
    }

    public void addState(String imgLoc, IState... alts) {
        ImageState state = states.get(imgLoc);
        if (state == null) {
            state = new ImageState(imgLoc, alts);
            states.put(imgLoc, state);
            // So there is only a single hash map object for all the image states
            state.states = states;
            state.stateChanges = stateChanges;
        }
        addState(state, alts);
    }

    /** Creates a new mapping from the alt states to the image state. */
    public void addState(ImageState newState, IState... alts) {
        ImmutableSet<IState> newStateSet = ImmutableSet.copyOf(alts);
        stateChanges.put(newStateSet, newState.identifier);
    }

    public ImageState getState(IState state) {
        return states.get(getFor(state));
    }

    private String getFor(IState replacer) {
        Set<IState> states = new HashSet<>();
        states.add(replacer);
        for (IState state : thisStates) {
            if (!replacer.replaces(state))
                states.add(state);
        }
        return stateChanges.get(states);
    }

    @Override
    public String toString() {
        return thisStates + " -> " + identifier;
    }

    public interface IState {
        boolean replaces(IState state);
    }
}
