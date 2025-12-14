package nz.co.flatfundr.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "transactions", schema = "flats")
public class Transactions {

    @Id
    @GeneratedValue
    private UUID id;


}
