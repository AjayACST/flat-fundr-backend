package nz.co.flatfundr.api.repository;

import nz.co.flatfundr.api.entity.Flat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlatRepository extends JpaRepository<Flat, UUID> {
}
