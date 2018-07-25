package pl.coddlers.core.models.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Entity
public class AccountType {
    @NotNull
    @Size(max = 50)
    @Id
    @Column(length = 50, nullable = false)
    private String name;
}
