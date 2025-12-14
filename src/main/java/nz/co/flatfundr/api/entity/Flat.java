package nz.co.flatfundr.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "flat", schema = "flats")
public class Flat {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String flatName;

    @OneToMany(mappedBy = "flat", cascade = CascadeType.ALL)
    private Set<Account> account;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", unique = true, nullable = false)
    private UserAccount accountOwner;

    public Flat() {}
    public Flat(String flatName, UserAccount ownerAccount) {
        this.flatName = flatName;
        this.accountOwner = ownerAccount;
    }
}
