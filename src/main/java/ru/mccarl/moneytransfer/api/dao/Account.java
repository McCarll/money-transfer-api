package ru.mccarl.moneytransfer.api.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity
@Table(name = "ACCOUNTS")
@AllArgsConstructor
public class Account {

    @Column(name = "ID", nullable = false)
    @Id
    private Integer id;
    @Column(name = "amount", nullable = false)
    private Long amount;
}
