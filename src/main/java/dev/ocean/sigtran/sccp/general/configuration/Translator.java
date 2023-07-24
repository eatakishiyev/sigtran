package dev.ocean.sigtran.sccp.general.configuration;

import dev.ocean.sigtran.sccp.address.GlobalTitleIndicator;
import dev.ocean.sigtran.sccp.address.NatureOfAddress;
import dev.ocean.sigtran.sccp.general.NumberingPlan;
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
