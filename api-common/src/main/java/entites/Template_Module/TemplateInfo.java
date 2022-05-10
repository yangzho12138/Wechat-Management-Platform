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
    private String templateType;
    private String templateId;
    private String templateContent;
    private String templateName;
    private String templateParams;
    private String wxTemplateId;
    private String createBy;
}
