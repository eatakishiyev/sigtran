/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.parameters;

/**
 * Digits {PARAMETERS-BOUND : bound} ::= OCTET STRING (SIZE(
 * bound.&minDigitsLength .. bound.&maxDigitsLength))
 * -- Indicates the address signalling digits.
 * -- Refer to ETSI EN 300 356-1 [23] Generic Number & Generic Digits parameters
 * for encoding.
 * -- The coding of the subfields 'NumberQualifier' in Generic Number and
 * 'TypeOfDigits' in
 * -- Generic Digits are irrelevant to the CAP;
 * -- the ASN.1 tags are sufficient to identify the parameter.
 * -- The ISUP format does not allow to exclude these subfields,
 * -- therefore the value is network operator specific.
 * -- -- The following parameters shall use Generic Number:
 * -- - AdditionalCallingPartyNumber for InitialDP
 * -- - AssistingSSPIPRoutingAddress for EstablishTemporaryConnection
 * -- - CorrelationID for AssistRequestInstructions
 * -- - CalledAddressValue for all occurrences, CallingAddressValue for all occurrences.
 *
 * -- -- The following parameters shall use Generic Digits:
 * -- - CorrelationID in EstablishTemporaryConnection
 * -- - number in VariablePart
 * -- - digitsResponse in ReceivedInformationArg
 * -- - midCallEvents in oMidCallSpecificInfo and tMidCallSpecificInfo
 * -- -- In the digitsResponse and midCallevents, the digits may also include
 * the '*', '#', -- a, b, c and d digits by using the IA5 character encoding scheme.
 * If the BCD even or -- BCD odd encoding scheme
 * is used, then the following encoding shall be applied for the -- non-decimal
 * characters: 1011 (*), 1100 (#). -- -- AssistingSSPIPRoutingAddress in
 * EstablishTemporaryConnection and CorrelationID in --
 * AssistRequestInstructions may contain a Hex B digit as address signal. Refer
 * to -- Annex A.6 for the usage of the Hex B digit. -- -- Note that when
 * CorrelationID is transported in Generic Digits, then the digits shall --
 * always be BCD encoded.
 *
 * @author eatakishiyev
 */
public abstract class Digits {

}
