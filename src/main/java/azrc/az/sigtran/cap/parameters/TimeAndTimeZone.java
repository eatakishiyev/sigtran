/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.parameters;

import azrc.az.gsm.string.utils.bcd.BCDStringUtils;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author eatakishiyev
 */
public class TimeAndTimeZone {

    private final int TIME_ZONE_SIGN_POSITIVE = 0;
    private final int TIME_ZONE_SIGN_NEGATIVE = 1;

    private String dateTime;
    private int offset;

    DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private Date date;

    public TimeAndTimeZone(byte[] data) throws IOException, ParseException {
        this.decode(new ByteArrayInputStream(data));
    }

    public TimeAndTimeZone(AsnInputStream ais) throws IOException, ParseException {
        this.decode(ais);
    }

    public TimeAndTimeZone(Calendar calendar) {
        this.dateTime = new SimpleDateFormat("yyyyMMddHHmmss").format(calendar.getTime());
        this.offset = (calendar.get(Calendar.DST_OFFSET) + calendar.get(Calendar.ZONE_OFFSET)) / 1000 / 60;
    }

    /**
     *
     * @param dateTime - yyyyMMddHHmmss formatted date
     * @param offset - offset time in minutes from GMT,included daylighsaving
     */
    public TimeAndTimeZone(String dateTime, int offset) {
        this.dateTime = dateTime;
        this.offset = offset;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws AsnException, IOException {
        aos.writeTag(tagClass, true, tag);
        int lenPos = aos.StartContentDefiniteLength();
        ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
        this.encode(baos);
        aos.write(baos.toByteArray());
        aos.FinalizeContent(lenPos);
    }

    public final void encode(ByteArrayOutputStream baos) throws IOException, AsnException {

        BCDStringUtils.toTBCDString(getDateTime(), baos);
        int sign = offset < 0 ? TIME_ZONE_SIGN_NEGATIVE : TIME_ZONE_SIGN_POSITIVE;
        int _3gppTimeZone = sign << 7 | (Math.abs(this.offset) / 15 & 0x7F);
        BCDStringUtils.toTBCDString(_3gppTimeZone, baos);
    }

    public final void decode(AsnInputStream ais) throws IOException, ParseException {
        byte[] data = new byte[ais.readLength()];
        ais.read(data);

        this.decode(new ByteArrayInputStream(data));

    }

    public final void decode(ByteArrayInputStream bais) throws IOException, ParseException {
        byte[] data = new byte[7];
        bais.read(data);

        this.dateTime = BCDStringUtils.fromTBCDToString(new ByteArrayInputStream(data));
        data = new byte[1];
        bais.read(data);

        int tmp = BCDStringUtils.fromTBCDToByte((data[0]));
        int sign = (tmp >> 7) & 0b00000001;
        this.offset = (tmp & 0b01111111) * 15;
        if (sign == TIME_ZONE_SIGN_NEGATIVE) {
            this.offset = (-1) * this.offset;
        }

        String timeZoneString = "GMT" + (offset < 0 ? "-".concat(String.valueOf(Math.abs(offset) / 60))
                : "+".concat(String.valueOf(Math.abs(offset) / 60)));
//        dateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneString));
        this.date = dateFormat.parse(dateTime);
    }

    /**
     * @return the dateTime
     */
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    /**
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }

    public Date getDate() {
        return this.date;
    }

    public enum Sign {

        POSITIVE,
        NEGATIVE;

        public static Sign valueOf(int value) {
            switch (value) {
                case 0:
                    return POSITIVE;
                default:
                    return NEGATIVE;
            }
        }
    }
}
