package entites.Template_Module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TemplateInfo {
    String templateType;
    String templateId;
    String templateContent;
    String templateName;
    String templateParams;
    String wxTemplateId;
    String createBy;
}
