package com.example.demo.Controller;

import com.example.demo.Entity.StfPuzzleSv;
import com.example.demo.Repository.StfPuzzleSvRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Objects;

@Controller
public class KiwiController {

    private final StfPuzzleSvRepository stfPuzzleSvRepository;

    public KiwiController(StfPuzzleSvRepository stfPuzzleSvRepository) {
        this.stfPuzzleSvRepository = stfPuzzleSvRepository;
    }

    @GetMapping("/menu")
    public String menu(Model model){
        StfPuzzleSv puzzleSv=stfPuzzleSvRepository.obtenerUltimoJuego();
        if(puzzleSv!=null){
            model.addAttribute("matrizLineal",puzzleSv.getStfPuzzleBoardStructure());
        }
        return "lab";
    }

    @PostMapping("/subirFoto")
    public ResponseEntity<HashMap<String, Object>>subirFoto(@RequestParam(value = "matriz",required = false)String matrizLineal,
                                                             @RequestParam(value = "imagen",required = false) MultipartFile imagen){
            HashMap<String,Object>response=new HashMap<>();
        HashMap<String,Object>errors=new HashMap<>();
        StfPuzzleSv puzzleSv= stfPuzzleSvRepository.obtenerUltimoJuego();
        if(puzzleSv!=null){
            response.put("status","success");
            return ResponseEntity.ok(response);
        }
        if(imagen==null||imagen.isEmpty()) {
            errors.put("imagen", "Suba una imagen");
            response.put("status", "error");
        }
        String fileName=imagen.getOriginalFilename();
        if (fileName.contains("..")){
            errors.put("imagen","Ataque detectado Uwu");
            response.put("status","error");
        }
        if(matrizLineal==null||matrizLineal.isEmpty()){
            errors.put("matrizLineal","Ingrese una matriz lineal");
            response.put("status","error");
        }
        if(matrizLineal==null||matrizLineal.isEmpty()){
            errors.put("matrizLineal","Ingrese una matriz lineal");
            response.put("status","error");
        }
        try{
            StfPuzzleSv puzzle=new StfPuzzleSv();
            puzzle.setImage(imagen.getBytes());
            puzzle.setStfPuzzleBoardStructure(matrizLineal);
            stfPuzzleSvRepository.save(puzzle);
        } catch (IOException e) {
            errors.put("imagen","Error al guardar la imagen");
            response.put("status","error");
        }
        if(response.get("status")==null){
            response.put("status","success");
            return ResponseEntity.ok(response);
        }else {
            response.put("errors",errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/mostrarImagen")
    public ResponseEntity<byte[]>mostrarImagen(){
        StfPuzzleSv puzzleSv=stfPuzzleSvRepository.obtenerUltimoJuego();
        if(puzzleSv!=null){
            byte[] imagen=puzzleSv.getImage();
            HttpHeaders httpHeaders=new HttpHeaders();
            httpHeaders.setContentType(MediaType.valueOf("image/jpeg"));
            return new ResponseEntity<>(imagen,httpHeaders,HttpStatus.OK);
        }else {
            return null;
        }
    }
    @PostMapping("/eliminarPartida")
    public ResponseEntity<HashMap<String,Object>>eliminarPartida(@RequestParam("matrizLineal")String matrizLineal){
        HashMap<String,Object>response=new HashMap<>();
        HashMap<String,Object>errors=new HashMap<>();
        if(matrizLineal==null){
            response.put("status","error");
            errors.put("matrizLineal","No se ha enviado una matriz lineal para eliminar");
            response.put("errors",errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        stfPuzzleSvRepository.eliminarPartida(matrizLineal);
        response.put("status","success");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/actualizarPartida")
    public ResponseEntity<HashMap<String,Object>>actualizarPartida(@RequestParam("matrizLinealAntigua")String matrizLinealAntigua,
                                                                 @RequestParam("matrizLinealNueva")String matrizLinealNueva){
        HashMap<String,Object>response=new HashMap<>();
        HashMap<String,Object>errors=new HashMap<>();
        if(matrizLinealAntigua==null||matrizLinealNueva==null){
            response.put("status","error");
            errors.put("matrizLineal","No se ha enviado una matriz lineal para eliminar");
            response.put("errors",errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        stfPuzzleSvRepository.actualizarPartida(matrizLinealAntigua,matrizLinealNueva);
        response.put("status","success");
        return ResponseEntity.ok(response);
    }
}
