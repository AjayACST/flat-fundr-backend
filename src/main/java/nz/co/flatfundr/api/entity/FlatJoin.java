package nz.co.flatfundr.api.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "flat_join", schema = "flats")
public class FlatJoin {
    @Id
    @GeneratedValue
    private UUID id;

    private String userEmail;

    @ManyToOne
    @JoinColumn(name = "linked_flat", foreignKey = @ForeignKey(name = "fk_flat"))
    private Flat linkedFlat;

    @Column(nullable = false)
    private String joinCode;

    public FlatJoin() {}

    public FlatJoin(String userEmail, Flat linkedFlat, String joinCode) {
        this.userEmail = userEmail;
        this.linkedFlat = linkedFlat;
        this.joinCode = joinCode;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Flat getLinkedFlat() {
        return linkedFlat;
    }

    public void setLinkedFlat(Flat linkedFlat) {
        this.linkedFlat = linkedFlat;
    }

    public String getJoinCode() {
        return joinCode;
    }

    public void setJoinCode(String joinCode) {
        this.joinCode = joinCode;
    }
}

