/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package azrc.az.isup.enums;

/**
 *
 * @author eatakishiyev
 */
 public enum CallDiversionTreatmentIndicator {

        CALL_DIVERSION_ALLOWED(0b00000001),
        CALL_DIVERSION_NOT_ALLOWED(0b00000010),
        UNKNOWN(-1);

        private final int value;

        private CallDiversionTreatmentIndicator(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static CallDiversionTreatmentIndicator getInstance(int value) {
            switch (value) {
                case 0b00000001:
                    return CALL_DIVERSION_ALLOWED;
                case 0b00000010:
                    return CALL_DIVERSION_NOT_ALLOWED;
                default:
                    return UNKNOWN;
            }
        }
    }
