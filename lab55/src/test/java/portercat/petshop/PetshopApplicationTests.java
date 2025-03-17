package portercat.petshop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import portercat.petshop.model.Pet;
import portercat.petshop.service.PetService;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PetshopApplicationTests {

    private static final String PET_API_URL = "/api/v3/pet";
    private static final String PET_ID = "/1";
    private static final String PET_NAME = "Bob";
    private static final String PET_STATUS_SOLD = "SOLD";
    private static final String PET_STATUS_AVAILABLE = "AVAILABLE";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    private Pet testPet;

    @BeforeEach
    void setUp() {
        testPet = new Pet();
        testPet.setId(1L);
        testPet.setName(PET_NAME);
        testPet.setStatus(PET_STATUS_SOLD);
    }

    @Test
    void contextLoads() {}

    @Test
    void testGetPetById_WhenPetNotFound_ReturnsNotFound() throws Exception {
        when(petService.getPetById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get(PET_API_URL + PET_ID))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePet_WhenValidPet_ReturnsCreatedPet() throws Exception {
        when(petService.addOrUpdatePet(any(Pet.class))).thenReturn(testPet);

        mockMvc.perform(post(PET_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + PET_NAME + "\",\"status\":\"" + PET_STATUS_SOLD + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(PET_NAME))
                .andExpect(jsonPath("$.status").value(PET_STATUS_SOLD));
    }

    @Test
    void testDeletePet_WhenPetExists_ReturnsNoContent() throws Exception {
        doNothing().when(petService).deletePet(1L);

        mockMvc.perform(delete(PET_API_URL + PET_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdatePet_WhenValidPet_ReturnsUpdatedPet() throws Exception {
        Pet updatedPet = new Pet();
        updatedPet.setId(1L);
        updatedPet.setName("Max");
        updatedPet.setStatus(PET_STATUS_AVAILABLE);

        when(petService.addOrUpdatePet(any(Pet.class))).thenReturn(updatedPet);

        mockMvc.perform(put(PET_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Max\",\"status\":\"" + PET_STATUS_AVAILABLE + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Max"))
                .andExpect(jsonPath("$.status").value(PET_STATUS_AVAILABLE));
    }
}