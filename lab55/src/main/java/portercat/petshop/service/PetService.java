package portercat.petshop.service;

import org.springframework.stereotype.Service;
import portercat.petshop.repository.PetRepository;
import portercat.petshop.model.Pet;

import java.util.List;
import java.util.Optional;

@Service
public class PetService
{
    private final PetRepository _petRepository;
//    public PetService(PetRepository petRepository)
//    {
//        _petRepository = petRepository;
//    }
//
//    public List<Pet> getAllPets()
//    {
//        return _petRepository.findAll();
//    }
//
//    public Optional<Pet> getPetById(Long id)
//    {
//        return _petRepository.findById(id);
//    }
//
//    public Pet addOrUpdatePet(Pet pet)
//    {
//        return _petRepository.save(pet);
//    }
//
//    public void deletePet(Long id)
//    {
//        _petRepository.deleteById(id);
//    }
    public PetService(PetRepository petRepository) {
        _petRepository = petRepository;
    }

    public List<Pet> getAllPets() {
        return _petRepository.findAll();
    }

    public Optional<Pet> getPetById(Long id) {
        return _petRepository.findById(id);
    }

    public Pet addOrUpdatePet(Pet pet) {
        return _petRepository.save(pet);
    }

    public void deletePet(Long id) {
        _petRepository.deleteById(id);
    }
}
