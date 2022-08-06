package com.amenity_reservation_system.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "amenity_type")
public class AmenityType {
    private static final String SEQ_NAME = "amenity_type_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME,sequenceName = SEQ_NAME,allocationSize = 1)
    private Long id;
    private String amenityName;
    private int capacity;
    private String urlPhoto;
    private String apiTemperature;

}
