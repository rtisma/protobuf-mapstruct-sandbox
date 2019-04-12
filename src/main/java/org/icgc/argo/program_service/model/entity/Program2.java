package org.icgc.argo.program_service.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Program2 {

  private UUID id;

  private String shortName;

  private String name;

  private String description;

  private MembershipType2 membershipType;

  private int commitmentDonors;

  private int submittedDonors;

  private int genomicDonors;

  private String website;

  private Date dateCreated;

  public static enum MembershipType2 {
    FULL,
    ASSOCIATE;
  }

}
