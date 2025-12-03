package nz.co.flatfundr.api.repository;

import nz.co.flatfundr.api.entity.FlatJoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FlatJoinRepository extends JpaRepository<FlatJoin, UUID> {
    Optional<FlatJoin> findByJoinCode(String joinCode);
}