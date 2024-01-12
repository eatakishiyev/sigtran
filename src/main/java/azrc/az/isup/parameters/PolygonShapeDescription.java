/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.TypeOfShape;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author eatakishiyev
 */
public class PolygonShapeDescription extends AbstractShapeDescription {

    private final List<EllipsoidPointShapeDescription> polygonShapePoints = new ArrayList<>();
    private int confidence;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PolygonShapeDescription[");
        polygonShapePoints.forEach((ellipsoidPointShapeDescription) -> {
            sb.append(";EllipsoidPointShapeDescription = ").append(ellipsoidPointShapeDescription);
        });
        sb.append(";Confidence = ").append(confidence).append("]");
        return sb.toString();
    }

    public PolygonShapeDescription() {
    }

    public PolygonShapeDescription(int confidence) {
        this.confidence = confidence;
    }

    @Override
    public void encode(ByteArrayOutputStream baos) {
        int spare = 0;
        baos.write((spare << 4) | polygonShapePoints.size());

        for (EllipsoidPointShapeDescription polygonShapePoint : polygonShapePoints) {
            polygonShapePoint.encode(baos);
        }

        baos.write((spare << 7) | (this.confidence & 0b01111111));
    }

    @Override
    public void decode(ByteArrayInputStream bais) {
        int b = bais.read() & 0xFF;
        int numberOfPoints = b & 0b00001111;
        int i = 0;
        while (bais.available() > 0) {
            EllipsoidPointShapeDescription polygonShapePoint = new EllipsoidPointShapeDescription();
            polygonShapePoint.decode(bais);
            this.polygonShapePoints.add(polygonShapePoint);
        }
        this.confidence = bais.read() & 0b01111111;

    }

    @Override
    public TypeOfShape getType() {
        return TypeOfShape.POLYGON;
    }

    /**
     * @return the polygonShapePoints
     */
    public List<EllipsoidPointShapeDescription> getPolygonShapePoints() {
        return polygonShapePoints;
    }

    /**
     * @return the confidence
     */
    public int getConfidence() {
        return confidence;
    }
}
