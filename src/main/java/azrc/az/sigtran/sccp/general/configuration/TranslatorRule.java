package azrc.az.sigtran.sccp.general.configuration;

import azrc.az.sigtran.sccp.address.NatureOfAddress;
import azrc.az.sigtran.sccp.address.RoutingIndicator;
import azrc.az.sigtran.sccp.general.NumberingPlan;
import lombok.Data;

@Data
public class TranslatorRule {
    private String name;
    private String gtPattern;
    private NatureOfAddress natureOfAddress;
    private NumberingPlan numberingPlan;
    private RoutingIndicator routingIndicator;
    private Integer translationType;
    private String sccpEntitySet;
    private String conversionRule;
}
