package com.example.demo.Repository;

import com.example.demo.Entity.StfPuzzleSv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface StfPuzzleSvRepository extends JpaRepository<StfPuzzleSv, String> {
    @Query(nativeQuery = true,value = "select * from stf_puzzle_sv limit 1")
    StfPuzzleSv obtenerUltimoJuego();

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from stf_puzzle_sv where stf_puzzle_board_structure=?1")
    void eliminarPartida(String matrizLineal);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "update stf_puzzle_sv set stf_puzzle_board_structure=?2 where stf_puzzle_board_structure=?1")
    void actualizarPartida(String antiguaMatrizLineal,String nuevaMatrizLineal);
}