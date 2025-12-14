package nz.co.flatfundr.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "auth")
public class UserAccount {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), schema = "auth")
    @Column(name = "role")
    private Set<String> roles;
    @Getter
    @Column(nullable = false)
    private String  firstName;
    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private boolean enabled;
    @Column(nullable = false)
    private boolean accountExpired;
    @Column(nullable = false)
    private boolean accountLocked;
    @Column
    private boolean firstTimeSetup;

    @ManyToOne
    @JoinColumn(name = "linked_flat", nullable = false)
    private Flat linkedFlat;

    @OneToOne(mappedBy = "accountOwner")
    private Flat ownedFlat;


    public UserAccount() {}

    public UserAccount(String email, String password, Set<String> roles,  String firstName, String lastName, Flat linkedFlat) {
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = true;
        this.accountExpired = false;
        this.accountLocked = false;
        this.firstTimeSetup = true;
        this.linkedFlat = linkedFlat;
    }
}

