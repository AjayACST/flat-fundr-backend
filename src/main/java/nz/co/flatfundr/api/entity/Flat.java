package nz.co.flatfundr.api.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "flat", schema = "flats")
public class Flat {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String flatName;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    public Flat() {}
    public Flat(String flatName) {
        this.flatName = flatName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFlatName() {
        return flatName;
    }

    public void setFlatName(String flatName) {
        this.flatName = flatName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
