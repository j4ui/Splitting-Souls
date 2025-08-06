package j4ui.dev.splittingSouls.component;

import org.ladysnake.cca.api.v3.component.Component;

public interface ShardProgressComponent extends Component {
    float getProgress();
    void setProgress(float progress);
    void addProgress(float amount);
    boolean canSplit();
}
// TODO: make shard progress persistent