package com.gs.buluo.store.eventbus;

/**
 * Created by hjn on 2017/8/15.
 */

public class ManagerSwitchEvent {
    private boolean isChecked;

    public ManagerSwitchEvent(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
