package com.veterinaria.vet.Services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterinaria.vet.Models.PagosDeuda;
import com.veterinaria.vet.Repositories.PagosDeudaRepository;

@Service
public class PagosDeudaService {
  @Autowired
  PagosDeudaRepository pagosDeudaRepository;


  public ArrayList<PagosDeuda> getAllPagosDeudas(){
     return (ArrayList<PagosDeuda>) pagosDeudaRepository.findByDeletedAtIsNull();
   }


  public Optional<PagosDeuda> getById(Long id){
    return pagosDeudaRepository.findByIDAndDeletedAtIsNull(id);
  }



  public PagosDeuda savePagosDeuda(PagosDeuda PagosDeuda){
     return pagosDeudaRepository.save(PagosDeuda);
  }

   
  public void eliminarLogico(Long id){
    pagosDeudaRepository.eliminarLogico(id);
  }


}
