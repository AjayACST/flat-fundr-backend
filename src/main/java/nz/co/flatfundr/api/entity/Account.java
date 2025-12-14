package nz.co.flatfundr.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "accounts", schema = "financial")
public class Account {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "flat_id", nullable = false)
    private Flat flat;

    @Column
    private String akahuId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal currentBalance;

    @Column(nullable = false)
    private String institutionName;

    @Column
    private String accountLogo;
}
