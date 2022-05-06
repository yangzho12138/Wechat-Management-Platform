package entites.Push_Module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TaskInfo {
    String taskId;
    String creator;
    String taskStatus;
    String createTime;
    String finishTime;
}
