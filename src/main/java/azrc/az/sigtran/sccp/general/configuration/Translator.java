package azrc.az.sigtran.sccp.general.configuration;

import azrc.az.sigtran.sccp.address.GlobalTitleIndicator;
import azrc.az.sigtran.sccp.address.NatureOfAddress;
import azrc.az.sigtran.sccp.general.NumberingPlan;
import lombok.Data;

import java.util.List;

@Data
public class Translator {
    private String name;
    private GlobalTitleIndicator globalTitleIndicator;
    private Integer translationType;
    private NumberingPlan numberingPlan;
    private NatureOfAddress natureOfAddress;
    private List<TranslatorRule> rules;
}
