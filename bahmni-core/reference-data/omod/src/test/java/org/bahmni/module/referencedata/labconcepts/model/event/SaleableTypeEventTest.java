package org.bahmni.module.referencedata.labconcepts.model.event;

import org.ict4h.atomfeed.server.service.Event;
import org.junit.Assert;
import org.openmrs.Concept;
import org.junit.Test;
import org.openmrs.ConceptAttribute;
import org.openmrs.ConceptAttributeType;

import static org.bahmni.module.referencedata.labconcepts.model.event.ConceptServiceEventFactory.CONCEPT_URL;
import static org.bahmni.module.referencedata.labconcepts.model.event.ConceptServiceEventFactory.SALEABLE;

public class SaleableTypeEventTest {

    @Test
    public void shouldRaiseEventForConceptWithSaleableAttribute() throws Exception {
        ConceptAttributeType cat = new ConceptAttributeType();
        cat.setDatatypeClassname("org.openmrs.customdatatype.datatype.BooleanDatatype");
        cat.setName(SaleableTypeEvent.SALEABLE_ATTR_NAME);

        Concept procedureConcept = new org.bahmni.test.builder.ConceptBuilder()
                .withClass("Procedure")
                .withUUID("9d583329-5fb1-4e50-9420-dcbbf6991fbc")
                .withName("Dressing Procedure")
                .build();

        ConceptAttribute ca = new ConceptAttribute();
        ca.setAttributeType(cat);
        ca.setVoided(false);
        ca.setValue(true);
        procedureConcept.addAttribute(ca);

        SaleableTypeEvent saleableTypeEvent = new SaleableTypeEvent(CONCEPT_URL, SALEABLE);
        Assert.assertEquals(true, saleableTypeEvent.isApplicable("saveConcept", new Object[]{procedureConcept}));

        Event event = saleableTypeEvent.asAtomFeedEvent(new Object[]{procedureConcept});
        Assert.assertNotNull(event);
        Assert.assertEquals(SALEABLE, event.getCategory());
        Assert.assertEquals("/openmrs/ws/rest/v1/reference-data/resources/9d583329-5fb1-4e50-9420-dcbbf6991fbc", event.getContents());
    }

    @Test
    public void shouldNotRaiseEventForConceptWithSaleableAttributeIfOfRadiologyClass() throws Exception {
        ConceptAttributeType cat = new ConceptAttributeType();
        cat.setDatatypeClassname("org.openmrs.customdatatype.datatype.BooleanDatatype");
        cat.setName(SaleableTypeEvent.SALEABLE_ATTR_NAME);

        Concept procedureConcept = new org.bahmni.test.builder.ConceptBuilder()
                                           .withClass("Radiology")
                                           .withUUID("9d583329-5fb1-4e50-9420-dcbbf6991fbc")
                                           .withName("Dressing Procedure")
                                           .build();

        ConceptAttribute ca = new ConceptAttribute();
        ca.setAttributeType(cat);
        ca.setVoided(false);
        ca.setValue(true);
        procedureConcept.addAttribute(ca);

        SaleableTypeEvent saleableTypeEvent = new SaleableTypeEvent(CONCEPT_URL, SALEABLE);
        Assert.assertEquals(false, saleableTypeEvent.isApplicable("saveConcept", new Object[]{procedureConcept}));

    }

    @Test
    public void shouldNotRaiseEventForReservedConceptsWithSaleableAttribute() throws Exception {
        ConceptAttributeType cat = new ConceptAttributeType();
        cat.setDatatypeClassname("org.openmrs.customdatatype.datatype.BooleanDatatype");
        cat.setName(SaleableTypeEvent.SALEABLE_ATTR_NAME);

        Concept procedureConcept = new org.bahmni.test.builder.ConceptBuilder()
                                           .withClass("ConvSet")
                                           .withUUID("9d583329-5fb1-4e50-9420-dcbbf6991fbc")
                                           .withName("Lab Samples")
                                           .build();

        ConceptAttribute ca = new ConceptAttribute();
        ca.setAttributeType(cat);
        ca.setVoided(false);
        ca.setValue(true);
        procedureConcept.addAttribute(ca);

        SaleableTypeEvent saleableTypeEvent = new SaleableTypeEvent(CONCEPT_URL, SALEABLE);
        Assert.assertEquals(false, saleableTypeEvent.isApplicable("saveConcept", new Object[]{procedureConcept}));

    }
}
