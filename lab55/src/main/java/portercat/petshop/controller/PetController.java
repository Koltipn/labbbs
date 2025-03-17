package portercat.petshop.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import portercat.petshop.model.Pet;
import portercat.petshop.service.PetService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v3/pet")
public class PetController
{
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    public ResponseEntity<Pet> addPet(@RequestBody Pet pet) {
        Pet createdPet = petService.addOrUpdatePet(pet);
        return ResponseEntity.ok(createdPet);
    }

    @PutMapping
    public ResponseEntity<Pet> updatePet(@RequestBody Pet pet) {
        Pet updatedPet = petService.addOrUpdatePet(pet);
        return ResponseEntity.ok(updatedPet);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        Optional<Pet> pet = petService.getPetById(id);
        return pet.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<List<Pet>> getPets() {
        List<Pet> pets = petService.getAllPets();
        return ResponseEntity.ok(pets);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }
}
