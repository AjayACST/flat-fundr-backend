package nz.co.flatfundr.api.entity;

import jakarta.persistence.*;
import java.util.Set;
import java.util.UUID;

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
    @JoinColumn(name = "linked_flat", foreignKey = @ForeignKey(name = "fk_flat"))
    private Flat linkedFlat;


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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String username) {
        this.email = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public boolean isFirstTimeSetup() {
        return firstTimeSetup;
    }

    public void setFirstTimeSetup(boolean firstTimeSetup) {
        this.firstTimeSetup = firstTimeSetup;
    }

    public Flat getLinkedFlat() {
        return linkedFlat;
    }

    public void setLinkedFlat(Flat linkedFlat) {
        this.linkedFlat = linkedFlat;
    }
}

