package alexiil.node.gui.state;

import alexiil.node.gui.state.ImageState.IState;

public enum EnumAttached implements IState {
    ATTACHED,
    DETACHED;

    @Override
    public boolean replaces(IState state) {
        return state instanceof EnumAttached;
    }
}
