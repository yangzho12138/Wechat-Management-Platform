package entites.Group_Module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GroupInfo {
    String dateCreated;
    int tagId;
    String createBy;
    int tagType;
    String description;
    int fansCount;
    String tagName;
}
