package entites.Group_Module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GroupInfo implements Serializable {
    private String dateCreated;
    private int tagId;
    private String createBy;
    private int tagType;
    private String description;
    private int fansCount;
    private String tagName;
    private String rule;
}
