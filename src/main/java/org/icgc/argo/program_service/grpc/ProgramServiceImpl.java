package org.icgc.argo.program_service.grpc;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.icgc.argo.program_service.ProgramDetails;
import org.icgc.argo.program_service.ProgramServiceGrpc;
import org.icgc.argo.program_service.converters.RobConverter;
import org.icgc.argo.program_service.grpc.interceptor.EgoAuthInterceptor.EgoAuth;
import org.icgc.argo.program_service.model.entity.Program;
import org.icgc.argo.program_service.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProgramServiceImpl extends ProgramServiceGrpc.ProgramServiceImplBase {

  private final ProgramRepository programRepository;
  private final RobConverter robConverter;

  @Autowired
  public ProgramServiceImpl(ProgramRepository programRepository,
       RobConverter robConverter) {
    this.programRepository = programRepository;
    this.robConverter = robConverter;
  }

  @Override
  @EgoAuth(typesAllowed = {"ADMIN"})
  public void create(
      ProgramDetails request, io.grpc.stub.StreamObserver<ProgramDetails> responseObserver) {

    val programProto = request.getProgram();

    val programDao =
        Program.builder()
            .name(programProto.getName())
            .shortName(programProto.getShortName())
            .commitmentDonors(programProto.getCommitmentDonors())
            .genomicDonors(programProto.getGenomicDonors())
            .membershipType(programProto.getMembershipType().toString())
            .website(programProto.getWebsite())
            .submittedDonors(programProto.getSubmittedDonors())
            .build();

    programRepository.save(programDao);

    responseObserver.onNext(request);
    responseObserver.onCompleted();
  }
}