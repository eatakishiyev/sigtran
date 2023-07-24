package dev.ocean.sigtran.sccp.general.configuration;

import dev.ocean.sigtran.sccp.address.NatureOfAddress;
import dev.ocean.sigtran.sccp.address.RoutingIndicator;
import dev.ocean.sigtran.sccp.general.NumberingPlan;
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
