package com.example.demo.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "stf_puzzle_sv")
public class StfPuzzleSv {
    @Id
    @Column(name = "stf_puzzle_board_structure", nullable = false)
    private String stfPuzzleBoardStructure;

    @Column(name = "image")
    private byte[] image;

}